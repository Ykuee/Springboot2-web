package com.ykuee.datamaintenance.common.dateconverter;


import static com.ykuee.datamaintenance.common.support.DateUtils.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;

/**
 * String --> LocalTime
 */
public class String2LocalTimeConverter extends BaseDateConverter<LocalTime> implements Converter<String, LocalTime> {

    protected static final Map<String, String> FORMAT = new LinkedHashMap(1);

    static {
        FORMAT.put(DEFAULT_TIME_FORMAT, "^\\d{1,2}:\\d{1,2}:\\d{1,2}$");
    }

    @Override
    protected Map<String, String> getFormat() {
        return FORMAT;
    }

    @Override
    public LocalTime convert(String source) {
        return super.convert(source, (key) -> LocalTime.parse(source, DateTimeFormatter.ofPattern(key)));
    }
}
