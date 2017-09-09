package ru.tandser.logreader;

import com.google.common.base.Strings;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Предназначен для чтения логов заданного формата из входного
 * потока.
 *
 * @author Andrew Timokhin
 */
public class LogReader implements Closeable {

    private static final String logEventPattern  = "(.+) - - \\[(.+)\\] \"(.+)\" (.+) (.+) (.+) \"-\" \"(.+)\" (.+)";
    private static final String dateTimeTemplate = "dd/MM/uuuu:HH:mm:ss ZZ";

    private BufferedReader    reader;
    private Matcher           matcher;
    private DateTimeFormatter dateTimeFormatter;

    /**
     * Создание reader'а логов.
     *
     * @param inputStreamReader входной поток данных
     */
    public LogReader(InputStreamReader inputStreamReader) {
        Objects.requireNonNull(inputStreamReader);

        reader            = new BufferedReader(inputStreamReader);
        matcher           = Pattern.compile(logEventPattern).matcher("");
        dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeTemplate);
    }

    /**
     * Возвращает следующее прочитанное из потока данных событие в виде
     * объекта класса {@link LogEvent}. В случае если формат
     * прочитанной строки некорректен, возвращает <code>null</code>.
     *
     * @return прочитанное из лога событие
     * @throws IOException в случае ошибок ввода/ввывода
     */
    public LogEvent next() throws IOException {
        LogEvent logEvent = null;

        String line = reader.readLine();

        if (!Strings.nullToEmpty(line).trim().isEmpty()) {
            matcher.reset(line);

            if (matcher.matches()) {
                try {
                    logEvent = new LogEvent(LocalDateTime.parse(matcher.group(2), dateTimeFormatter), Integer.parseInt(matcher.group(4)), Double.parseDouble(matcher.group(6)));
                } catch (DateTimeParseException | NumberFormatException ignored) { /* нет надобности в обработке */ }
            }
        }

        return logEvent;
    }

    /**
     * Проверяет, есть ли во входном потоке данные для чтения.
     *
     * @return возвращает <code>true</code>, есть данные в потоке есть
     * @throws IOException в случае ошибок ввода/вывода
     */
    public boolean isMore() throws IOException {
        return reader.ready();
    }

    /**
     * Закрывает входной поток данных и освобождает выделенные ресурсы.
     *
     * @throws IOException в случае ошибок ввода/вывода
     */
    @Override
    public void close() throws IOException {
        if (reader == null) {
            return;
        }

        try {
            reader.close();
        } finally {
            reader = null;
        }
    }
}