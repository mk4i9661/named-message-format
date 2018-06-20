package org.mki.text;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mki.text.UsFormatter.US_FORMATTER;

public class RawSequenceTest {

    @Test
    public void shouldRemoveQuotes() {
        // checks whether the class handles quotes in a way similar to MessageFormat.format

        RawSequence group = RawSequence.of("Hello, my name is 'John'.");
        assertThat(group.format(US_FORMATTER), is("Hello, my name is John."));
    }

    @Test
    public void shouldHandleDoubleQuotes() {
        // checks whether the class handles double quotes in a way similar to MessageFormat.format

        RawSequence group = RawSequence.of("He is a friend of John''s.");
        assertThat(group.format(US_FORMATTER), is("He is a friend of John's."));
    }
}
