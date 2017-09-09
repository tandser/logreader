package ru.tandser.logreader;

import com.google.common.base.MoreObjects;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Представляет собой единицу выходной информации в виде промежутка
 * времени, когда уровень доступности системы был ниже заданного.
 *
 * @author Andrew Timokhin
 */
public class FailureInterval {

    private LocalDateTime startOfFailure;
    private LocalDateTime endOfFailure;
    private double        availabilityLevel;

    public FailureInterval() {}

    /**
     * Создание единицы выходной информации.
     *
     * @param startOfFailure    начало интервала отклонения
     * @param endOfFailure      окончания интервала отклонения
     * @param availabilityLevel зафиксированный уровень доступности
     */
    public FailureInterval(LocalDateTime startOfFailure, LocalDateTime endOfFailure, double availabilityLevel) {
        this.startOfFailure    = startOfFailure;
        this.endOfFailure      = endOfFailure;
        this.availabilityLevel = availabilityLevel;
    }

    public LocalDateTime getStartOfFailure() {
        return startOfFailure;
    }

    public void setStartOfFailure(LocalDateTime startOfFailure) {
        this.startOfFailure = startOfFailure;
    }

    public LocalDateTime getEndOfFailure() {
        return endOfFailure;
    }

    public void setEndOfFailure(LocalDateTime endOfFailure) {
        this.endOfFailure = endOfFailure;
    }

    public double getAvailabilityLevel() {
        return availabilityLevel;
    }

    public void setAvailabilityLevel(double availabilityLevel) {
        this.availabilityLevel = availabilityLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startOfFailure, endOfFailure, availabilityLevel);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        FailureInterval that = (FailureInterval) obj;

        return Objects.equals(this.startOfFailure,    that.startOfFailure) &&
               Objects.equals(this.endOfFailure,      that.endOfFailure)   &&
               Objects.equals(this.availabilityLevel, that.availabilityLevel);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("startOfFailure",    startOfFailure)
                .add("endOfFailure",      endOfFailure)
                .add("availabilityLevel", availabilityLevel)
                .toString();
    }
}