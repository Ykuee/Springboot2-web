package com.ykuee.datamaintenance.common.dateconverter;

import static com.ykuee.datamaintenance.common.support.DateUtils.DEFAULT_DATE_FORMAT;
import static com.ykuee.datamaintenance.common.support.DateUtils.DEFAULT_DATE_TIME_FORMAT;
import static com.ykuee.datamaintenance.common.support.DateUtils.DEFAULT_MONTH_FORMAT;
import static com.ykuee.datamaintenance.common.support.DateUtils.DEFAULT_YEAR_FORMAT;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

/**
 * String --> Date
 */
public class String2DateConverter extends BaseDateConverter<Date> implements Converter<String, Date> {
    private static Logger logger = LoggerFactory.getLogger(String2DateConverter.class);

    protected static final Map<String, String> FORMAT = new LinkedHashMap(11);

    static {
        FORMAT.put(DEFAULT_YEAR_FORMAT, "^\\d{4}");
        FORMAT.put(DEFAULT_MONTH_FORMAT, "^\\d{4}-\\d{1,2}$");
        FORMAT.put(DEFAULT_DATE_FORMAT, "^\\d{4}-\\d{1,2}-\\d{1,2}$");
        FORMAT.put("yyyy-MM-dd HH", "^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}");
        FORMAT.put("yyyy-MM-dd HH:mm", "^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$");
        FORMAT.put(DEFAULT_DATE_TIME_FORMAT, "^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$");
        FORMAT.put("yyyy/MM", "^\\d{4}/\\d{1,2}$");
        FORMAT.put("yyyy/MM/dd", "^\\d{4}/\\d{1,2}/\\d{1,2}$");
        FORMAT.put("yyyy/MM/dd HH", "^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}");
        FORMAT.put("yyyy/MM/dd HH:mm", "^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}$");
        FORMAT.put("yyyy/MM/dd HH:mm:ss", "^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$");
    }

    /**
     * 格式化日期
     *
     * @param dateStr String 字符型日期
     * @param format  String 格式
     * @return Date 日期
     * @throws RuntimeException 
     */
    protected static Date parseDate(String dateStr, String format) {
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            //严格模式
            dateFormat.setLenient(false);
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            logger.info("转换日期失败, date={}, format={}", dateStr, format, e);
            throw new RuntimeException("500", e);
        }
        return date;
    }

    @Override
    protected Map<String, String> getFormat() {
        return FORMAT;
    }

    @Override
    public Date convert(String source){
        return super.convert(source, (key) -> parseDate(source, key));
    }

}
