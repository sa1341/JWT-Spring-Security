package com.genesislab.videoservice.global.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateUtils {

    public static LocalDateTime convertToLocalDateTime(final Date dateToConvert) {

        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
