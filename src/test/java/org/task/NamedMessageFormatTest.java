package org.task;

import org.junit.Test;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NamedMessageFormatTest {


    private static Map<String, Object> parameters() {
        TreeMap<String, Object> parameters = new TreeMap<>();
        parameters.put("age", 20);
        parameters.put("name", "John");
        return parameters;
    }

    @Test
    public void shouldMatchExample() {
        NamedMessageFormat format = new NamedMessageFormat("{date,time,dd.MM.yyyy}");
        Map<String, Object> param = new HashMap<>();

        Date date = Date.from(
                LocalDate.of(2018, 6, 15)
                        .atStartOfDay().toInstant(ZoneOffset.UTC)
        );

        param.put("date", date);

        assertThat(format.format(param), is("15.06.2018"));
    }

    @Test
    public void shouldNotFormatQuotedText() {
//        String template = MessageFormat.format(
//                "Hello, my name is '{0}' and I am '{1} years old.",
//                "John",
//                20
//        );
//
//        String formatted = new NamedMessageFormat(
//                "Hello, my name is '{name}' and I am '{age}' years old."
//        ).format(parameters());

    }

    @Test
    public void shouldFormatStatement() {
        assertThat(
                NamedMessageFormat.format(
                        "Hello, my name is {name} and I am {age} years old.",
                        parameters()
                ),
                is("Hello, my name is John and I am 20 years old.")
        );

        assertThat(
                NamedMessageFormat.format(
                        "Hello, my name is {name}. I was named after my father {name}. I am {age} years old.",
                        parameters()
                ),
                is("Hello, my name is John. I was named after my father John. I am 20 years old.")
        );

        assertThat(
                NamedMessageFormat.format(
                        "Hello, my name is {name}. I am {age} years old. I was named after my father {name}.",
                        parameters()
                ),
                is("Hello, my name is John. I am 20 years old. I was named after my father John.")
        );
    }

}
