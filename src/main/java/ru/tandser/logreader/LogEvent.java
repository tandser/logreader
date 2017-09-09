package ru.tandser.logreader;

import com.google.common.base.MoreObjects;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Представляет собой единицу входной информации в виде события из
 * лога.
 *
 * @author Andrew Timokhin
 */
public class LogEvent {

    private LocalDateTime dateTime;
    private int           statusCode;
    private double        duration;

    public LogEvent() {}

    /**
     * Создание единицы входной информации.
     *
     * @param dateTime   дата-время начала запроса к сервису
     * @param statusCode HTTP-код ответа сервиса
     * @param duration   время обработка запроса в мс
     */
    public LogEvent(LocalDateTime dateTime, int statusCode, double duration) {
        this.dateTime   = dateTime;
        this.statusCode = statusCode;
        this.duration   = duration;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, statusCode, duration);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        LogEvent that = (LogEvent) obj;

        return Objects.equals(this.dateTime,   that.dateTime)   &&
               Objects.equals(this.statusCode, that.statusCode) &&
               Objects.equals(this.duration,   that.duration);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("dateTime",   dateTime)
                .add("statusCode", statusCode)
                .add("duration",   duration)
                .toString();
    }
}