package org.task;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class UnquotedTextAreaTest {

    @Test
    public void shouldFindUnquotedText() {
        assertThat(UnquotedTextArea.of("").tokens(), hasSize(0));

        assertThat(UnquotedTextArea.of("''").tokens(), hasSize(0));

        UnquotedTextArea area = UnquotedTextArea.of("'test' {name}''");
        assertThat(area.tokens(), hasSize(1));
        assertThat(area.tokens().get(0).value(), is(" {name}"));

        area = UnquotedTextArea.of("'test' {name}'' {age}");
        assertThat(area.tokens(), hasSize(2));
        assertThat(area.tokens().get(0).value(), is(" {name}"));
        assertThat(area.tokens().get(1).value(), is(" {age}"));

        area = UnquotedTextArea.of("My name is {name}.");
        assertThat(area.tokens(), hasSize(1));
        assertThat(area.tokens().get(0).value(), is("My name is {name}."));
    }
}
