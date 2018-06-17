package org.task;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RawGroupTest {

    @Test
    public void shouldRemoveQuotes() {
        // checks whether the class handles quotes in a way similar to MessageFormat.format

        RawGroup group = RawGroup.of("Hello, my name is 'John'.");
        assertThat(group.toString(), is("Hello, my name is John."));
    }

    @Test
    public void shouldHandleDoubleQuotes() {
        // checks whether the class handles double quotes in a way similar to MessageFormat.format

        RawGroup group = RawGroup.of("He is a friend of John''s.");
        assertThat(group.toString(), is("He is a friend of John's."));
    }
}
