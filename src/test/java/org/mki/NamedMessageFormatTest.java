package org.mki;

import org.junit.Test;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NamedMessageFormatTest {

    private static Map<String, Object> parameters() {
        TreeMap<String, Object> parameters = new TreeMap<>();
        parameters.put("age", 20);
        parameters.put("name", "John");
        parameters.put("price", 31.45);
        return parameters;
    }


    @Test
    public void a() {
        String format = MessageFormat.format("My name 'is {0}' and {0, number, test}", new Object[0]);

        String s = "My name 'is {name}' I am {age} years old. My father''s name was {name}";

        PreparedPattern pattern = PreparedPattern.ofPattern(s);
        assertThat(
                pattern.apply(parameters()),
                is("My name is {name} I am 20 years old. My father's name was John")
        );
    }

    @Test
    public void shouldMatchExample() {
        NamedMessageFormat format = new NamedMessageFormat("{date,time,dd.MM.yyyy}");
        String result = format.format(
                Collections.singletonMap(
                        "date",
                        Date.from(
                                LocalDate.of(2018, 6, 15)
                                        .atStartOfDay()
                                        .toInstant(ZoneOffset.UTC)
                        )
                )
        );

        assertThat(result, is("15.06.2018"));
    }

    @Test
    public void shouldHandleQuotes() {
        assertThat(
                NamedMessageFormat.format("'{''}'", parameters()),
                is("{'}")
        );

        assertThat(
                NamedMessageFormat.format("My '{name}' is {name} and my {age} is '{age}'", parameters()),
                is("My {name} is John and my 20 is {age}")
        );
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

        assertThat(
                NamedMessageFormat.format(
                        "The price of this item is {price, number,$'#'.##}",
                        parameters()
                ),
                is("The price of this item is $#31.45")
        );

        assertThat(
                NamedMessageFormat.format(
                        "{pi,number,#.##}, {pi,number,#.#}",
                        Collections.singletonMap("pi", Math.PI)
                ),
                is("3.14, 3.1")
        );
    }

}
