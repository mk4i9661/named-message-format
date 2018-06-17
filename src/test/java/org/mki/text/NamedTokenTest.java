package org.mki.text;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NamedTokenTest {

    @Test
    public void shouldReturnTemplate() {
        assertThat(NamedToken.of("{name}").toString(), is("{name}"));
    }

    @Test
    public void shouldHandleCyrillicNames() {
        NamedToken group = NamedToken.of("{цена,number,Цена: #.##'руб.'}");
        assertThat(group.toString(), is("{цена,number,Цена: #.##'руб.'}"));
        assertThat(group.toString(15.99), is("Цена: 15.99руб."));
    }

    @Test
    public void shouldReturnFormattedValue() {
        assertThat(
                NamedToken.of("{price,number,$#.##}").toString(15.99),
                is("$15.99")
        );
    }

    @Test(expected = InvalidNamedGroupException.class)
    public void shouldFailOnMissingOpeningBrace() {
        NamedToken.of("{name");
    }

    @Test(expected = InvalidNamedGroupException.class)
    public void shouldFailOnMissingClosingBrace() {
        NamedToken.of("name}");
    }
}
