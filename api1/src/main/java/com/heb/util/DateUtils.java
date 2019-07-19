package com.heb.util;

import com.heb.pm.alert.AlertCommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Provides a suite of functions related to Date Time convertion.
 *
*/
public class DateUtils {
    private static final Logger logger = LoggerFactory.getLogger(AlertCommentService.class);

    private static final String ERROR_DATE_PARSE = "Error while converting to Date for date value :%s and format %s";

    public static final String DATE_TIME_FORMAT_01 = "yyyy-MM-dd-H.m.s.n";

    /**
     * Verified for the give date is greater than current date.
     * @param dateStr date to be verified.
     * @param dateFormat format of the input date.
     * @return
     */
    public static boolean isGreaterThanToday(String dateStr, String dateFormat) {
        LocalDate effDate = getLocalDate(dateStr, dateFormat);
        return (effDate != null) ? effDate.compareTo(LocalDate.now().plusDays(1L)) >= 0 : false;
    }

    /**
     * Takes string representation of a date aand its format and converts it to LocalDate object.
     * @param dateStr date value.
     * @param dateFormat date format of the input date string.
     * @return
     */
    public static LocalDate getLocalDate(final String dateStr, final String dateFormat) {
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(dateFormat));
        } catch (Exception e) {
            logger.error(String.format(ERROR_DATE_PARSE, dateStr, dateFormat));
            return null;
        }
    }

    /**
     * Used to convert a time stamp in string to LocalDateTime object.
     * @param dateTimeStr time stamp in string.
     * @param dateTimeFormat time stamp format.
     * @return LocalDateTime object.
     */
    public static LocalDateTime toLocalDateTime(final String dateTimeStr, final String dateTimeFormat) {
        try {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(dateTimeFormat));
        } catch (Exception e) {
            logger.error(String.format(ERROR_DATE_PARSE, dateTimeStr, dateTimeFormat));
            return null;
        }
    }

    /**
     * Verified for the give date is greater than or equal current date.
     * @param dateStr date to be verified.
     * @param dateFormat format of the input date.
     * @return true if the date is greater than or equal current date.
     */
    public static boolean isGreaterThanOrEqualToday(String dateStr, String dateFormat) {
        LocalDate effDate = getLocalDate(dateStr, dateFormat);
        return (effDate != null) ? effDate.compareTo(LocalDate.now()) >= 0 : false;
    }
}
