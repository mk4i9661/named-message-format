package org.mki;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RawTokenTest {

    @Test
    public void shouldRemoveQuotes() {
        // checks whether the class handles quotes in a way similar to MessageFormat.format

        RawToken group = RawToken.of("Hello, my name is 'John'.");
        assertThat(group.toString(), is("Hello, my name is John."));
    }

    @Test
    public void shouldHandleDoubleQuotes() {
        // checks whether the class handles double quotes in a way similar to MessageFormat.format

        RawToken group = RawToken.of("He is a friend of John''s.");
        assertThat(group.toString(), is("He is a friend of John's."));
    }
}
