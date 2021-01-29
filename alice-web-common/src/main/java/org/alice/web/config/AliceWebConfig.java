package org.alice.web.config;

import cn.hutool.core.date.DatePattern;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * The type Alice web config.
 *
 * @author Amse
 * @version 1.0
 * @date 28 /01/2021 15:30
 */
public class AliceWebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(Date.class, new DateFormatter());
        registry.addFormatterForFieldType(LocalDateTime.class, new LocalDateTimeFormatter(DatePattern.NORM_DATETIME_PATTERN));
        registry.addFormatterForFieldType(LocalDate.class, new LocalDateFormatter(DatePattern.NORM_DATE_PATTERN));
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }


}