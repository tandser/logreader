package ru.tandser.logreader;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Основной класс запуска приложения чтения и анализа логов.
 *
 * @author Andrew Timokhin
 */
public class Main {

    private static final int numberOfArgs = 4;

    private static final String timeTemplate = "HH:mm:ss";
    private static final String errorMessage = "Invalid input format";

    /**
     * Основной исполняемый метод.
     *
     * @param  args параметры запуска приложения
     * @throws IllegalArgumentException при неверном формате параметров
     */
    public static void main(String[] args) throws IOException {
        Preconditions.checkArgument(args.length == numberOfArgs, "Invalid number of arguments");

        double durationLimit     = -1;
        double availabilityLimit = -1;

        for (int i = 0; i < args.length; i++) {
            if (i % 2 == 0) {
                try {
                    switch (args[i]) {
                        case "-u": availabilityLimit = Double.parseDouble(args[i + 1]);
                                   break;
                        case "-t": durationLimit     = Double.parseDouble(args[i + 1]);
                                   break;
                        default  : break;
                    }
                } catch (NumberFormatException exc) {
                    throw new IllegalArgumentException(errorMessage);
                }
            }
        }

        if (durationLimit == -1 || availabilityLimit == -1) {
            throw new IllegalArgumentException(errorMessage);
        }

        try (LogReader reader = new LogReader(new InputStreamReader(System.in))) {
            long numberOfRequests = 0;
            long numberOfFailures = 0;

            boolean wasBegan = false; // было ли начало отказа

            List<FailureInterval> failureIntervals = new ArrayList<>();

            while (reader.isMore()) {
                LogEvent event;

                if ((event = reader.next()) != null) {
                    numberOfRequests++;

                    if ((event.getStatusCode() >= 500 && event.getStatusCode() < 600) || Double.compare(event.getDuration(), durationLimit) > 0) {
                        numberOfFailures++;
                    }

                    double availabilityLevel = (double) (numberOfRequests - numberOfFailures) / numberOfRequests * 100;

                    if (Double.compare(availabilityLevel, availabilityLimit) < 0) {
                        LocalDateTime endOfFailure = plusMilliseconds(event.getDateTime(), event.getDuration());

                        if (!wasBegan) {
                            wasBegan = true;

                            // поработаем с осцилляциями
                            if (!failureIntervals.isEmpty() && (event.getDateTime().isEqual(failureIntervals.get(failureIntervals.size() - 1).getStartOfFailure())   ||
                                    event.getDateTime().isEqual(failureIntervals.get(failureIntervals.size() - 1).getEndOfFailure().truncatedTo(ChronoUnit.SECONDS)) ||
                                    event.getDateTime().isEqual(failureIntervals.get(failureIntervals.size() - 1).getEndOfFailure().truncatedTo(ChronoUnit.SECONDS).plusSeconds(1)))) {

                                if (endOfFailure.isAfter(failureIntervals.get(failureIntervals.size() - 1).getEndOfFailure())) {
                                    failureIntervals.get(failureIntervals.size() - 1).setEndOfFailure(endOfFailure);
                                }

                                failureIntervals.get(failureIntervals.size() - 1).setAvailabilityLevel((failureIntervals.get(failureIntervals.size() - 1).getAvailabilityLevel() + availabilityLevel) / 2);
                            } else {
                                failureIntervals.add(new FailureInterval(event.getDateTime(), endOfFailure, availabilityLevel));
                            }
                        } else {
                            if (endOfFailure.isAfter(failureIntervals.get(failureIntervals.size() - 1).getEndOfFailure())) {
                                failureIntervals.get(failureIntervals.size() - 1).setEndOfFailure(endOfFailure);
                            }

                            failureIntervals.get(failureIntervals.size() - 1).setAvailabilityLevel((failureIntervals.get(failureIntervals.size() - 1).getAvailabilityLevel() + availabilityLevel) / 2);
                        }
                    } else {
                        wasBegan = false;
                    }
                }
            }

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(timeTemplate);

            failureIntervals.stream().forEachOrdered(f -> System.out.println(String.format(Locale.ROOT, "%s\t%s\t%.1f", f.getStartOfFailure().format(dateTimeFormatter), f.getEndOfFailure().format(dateTimeFormatter), f.getAvailabilityLevel())));
        }
    }

    /**
     * Возвращает новый объект класса {@link LocalDateTime} с
     * добавленным количеством миллисекунд к входящему времени.
     *
     * @param dateTime входящее время
     * @param ms       количество добавляемых миллисекунд
     * @return время, увеличенное на указанное количество миллисекунд
     */
    private static LocalDateTime plusMilliseconds(LocalDateTime dateTime, double ms) {
        return dateTime.plus((long) ms, ChronoField.MILLI_OF_DAY.getBaseUnit());
    }
}