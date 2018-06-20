package org.mki.text;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mki.text.UsFormatter.US_FORMATTER;

public class NamedParameterTest {

    @Test
    public void shouldReturnTemplate() {
        assertThat(NamedParameter.of("{name}").toString(), is("{name}"));
    }

    @Test
    public void shouldHandleCyrillicNames() {
        NamedParameter group = NamedParameter.of("{цена,number,Цена: #.##'руб.'}");
        assertThat(group.toString(), is("{цена,number,Цена: #.##'руб.'}"));
        assertThat(
                group.format(US_FORMATTER, 15.99),
                is("Цена: 15.99руб.")
        );
    }

    @Test
    public void shouldReturnFormattedValue() {
        assertThat(
                NamedParameter.of("{price,number,$#.##}").format(
                        US_FORMATTER,
                        15.99
                ),
                is("$15.99")
        );
    }

    @Test(expected = InvalidNamedGroupException.class)
    public void shouldFailOnMissingOpeningBrace() {
        NamedParameter.of("{name");
    }

    @Test(expected = InvalidNamedGroupException.class)
    public void shouldFailOnMissingClosingBrace() {
        NamedParameter.of("name}");
    }
}
