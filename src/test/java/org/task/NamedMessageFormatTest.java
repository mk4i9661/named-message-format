package org.task;

import org.junit.Test;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

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

//        List<NamedGroup> groups = new ArrayList<>();
//        boolean raw = false;
//        int quotes = 0;
//        int from = 0;
//        int to = 0;
//        for (int i = 0; i < s.length(); i++) {
//            char charAt = s.charAt(i);
//            if (charAt == '\'') {
//                if (!raw) {
//
//                }
//                raw = !raw;
//            } else {
//                if (raw)
//                    continue;
//
//                if (charAt == '{') {
//                    from = i;
//                    quotes++;
//                } else if (charAt == '}') {
//                    to = i;
//                    quotes--;
////                    groups.add(s.substring(from, to + 1));
//                    if (quotes == 0) {
//
////                        groups.add(new NamedGroup(s, identifier, from, to + 1));
//                        groups.add(NamedGroup.of(s, from, to + 1));
//                    }
//                }
//            }
//        }
//
//        if (quotes != 0)
//            throw new RuntimeException("Unmatched braces in the pattern.");
//
//        PreparedPattern pattern = PreparedPattern.from(s, groups);

        PreparedPattern pattern = PreparedPattern.ofPattern(s);
        assertThat(
                pattern.apply(parameters()),
                is("My name is {name} I am 20 years old. My father's name was John")
        );


    }

    @Test
    public void shouldMatchExample() {
//        NamedMessageFormat format = new NamedMessageFormat("{date,time,dd.MM.yyyy}");
//        Map<String, Object> param = new HashMap<>();
//
//        Date date = Date.from(
//                LocalDate.of(2018, 6, 15)
//                        .atStartOfDay().toInstant(ZoneOffset.UTC)
//        );
//
//        param.put("date", date);
//
//        assertThat(format.format(param), is("15.06.2018"));


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
//        assertThat(
//                NamedMessageFormat.format("'{''}'", parameters()),
//                is("{'}")
//        );
//
//        assertThat(
//                NamedMessageFormat.format(
//                        "My '{name}' is {name} and my {age} is '{age}'",
//                        parameters()
//                ),
//                is("My {name} is John and my 20 is {age}")
//        );


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
