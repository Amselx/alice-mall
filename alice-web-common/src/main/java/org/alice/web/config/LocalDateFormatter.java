package org.alice.web.config;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * The type Local date formatter.
 *
 * @author Amse
 * @version 1.0
 * @date 28 /01/2021 15:35
 */
public class LocalDateFormatter implements Formatter<LocalDate> {
    private final String pattern;

    /**
     * Instantiates a new Local date formatter.
     *
     * @param pattern the pattern
     */
    public LocalDateFormatter(String pattern) {
        this.pattern = pattern;

    }

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern(this.pattern));
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return DateTimeFormatter.ofPattern(this.pattern).format(object);
    }
}
