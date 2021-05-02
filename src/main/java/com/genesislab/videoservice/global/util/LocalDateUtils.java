package com.genesislab.videoservice.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LocalDateUtils {

    public static LocalDateTime convertToLocalDateTime(final Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static LocalDateTime parseStartDate(String startDate) {
        LocalDate localStartDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return localStartDate.atStartOfDay();
    }

    public static LocalDateTime parseEndDate(String endDate) {
        LocalDate localEndDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return LocalDateTime.of(localEndDate, LocalTime.of(23, 59, 59));
    }
}
