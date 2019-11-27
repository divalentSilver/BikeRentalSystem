package uk.ac.ed.bikerental;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * This represents a date range between a start date and an end date. This date range does not have a time zone, and
 * times are not stored. Date ranges can be checked for overlap, and the start and end dates can be retrieved.
 *
 * This class is value-based, and as such the equals method should be used to compare equality rather than ==.
 * This class is also immutable and thread safe.
 */
public class DateRange {
    /**
     * The start and end date of the date range, stored as LocalDate objects
     */
    private LocalDate start, end;

    /**
     * Constructor for a date range that spans from the start date until the end date, inclusive.
     * @param start LocalDate at which the date range should begin
     * @param end LocalDate at which the date range should end
     */
    public DateRange(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Get the start date
     * @return The start date
     */
    public LocalDate getStart() {
        return this.start;
    }

    /**
     * Get the end date
     * @return The end date
     */
    public LocalDate getEnd() {
        return this.end;
    }

    /**
     * Returns the length of time in years represented by this date range.
     * @return The number of years represented by this date range
     */
    public long toYears() {
        return ChronoUnit.YEARS.between(this.getStart(), this.getEnd());
    }

    /**
     * Returns the length of time in days represented by this date range.
     * @return The number of years represented by this date range
     */
    public long toDays() {
        return ChronoUnit.DAYS.between(this.getStart(), this.getEnd());
    }

    /**
     * Checks is a date is within the current date range. A date is within the range if it is the same day as the start
     * or end, or if it is before the end date and after the start date.
     * @param date LocalDate to check if in the range of this date range
     * @return true if the date is within the range, otherwise false
     */
    private Boolean withinRange(LocalDate date) {
        return (date.isEqual(getStart()) || date.isAfter(getStart())) &&
                (date.isEqual(getEnd()) || date.isBefore(getEnd()));
    }

    /**
     * Checks if another date range overlaps the current one. Returns true if the date ranges do overlap, false if not.
     * A date range overlaps if any date within the two ranges is shared.
     * @param other date range to check if overlapping
     * @return true if overlapping, false if not overlapping
     */
    public Boolean overlaps(DateRange other) {
        // Overlaps if the other range's start or end is within this date range
        return this.withinRange(other.getStart()) || other.withinRange(this.getStart());
    }

    /**
     * Hashes the object. This is done by hashing the end date with the start date, which ensures that the same
     * date range will always have the same hashcode.
     * @return int hashcode of the object
     */
    @Override
    public int hashCode() {
        // hashCode method allowing use in collections
        return Objects.hash(end, start);
    }

    /**
     * Checks if another object is equal to this one. Another object is equal if they are of the same class and have the
     * same start and end dates.
     * @param obj object to check if equal
     * @return true if equal, false if not equal
     */
    @Override
    public boolean equals(Object obj) {
        // equals method for testing equality in tests
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DateRange other = (DateRange) obj;
        return Objects.equals(end, other.end) && Objects.equals(start, other.start);
    }

}
