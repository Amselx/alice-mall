package org.alice.web.config;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * The type Local date time formatter.
 *
 * @author Amse
 * @version 1.0
 * @date 28 /01/2021 15:39
 */
public class LocalDateTimeFormatter  implements Formatter<LocalDateTime> {
    private final String pattern;

    /**
     * Instantiates a new Local date time formatter.
     *
     * @param pattern the pattern
     */
    public LocalDateTimeFormatter(String pattern) {
        this.pattern = pattern;

    }

    @Override
    public LocalDateTime parse(String text, Locale locale) throws ParseException {
        return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(this.pattern));
    }

    @Override
    public String print(LocalDateTime object, Locale locale) {
        return DateTimeFormatter.ofPattern(this.pattern).format(object);
    }
}
