package org.task;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class PreparedPatternTest {

    @Test
    public void shouldSplitIntoTokens() {
        // verifies that quoted text remains as is, whereas named groups get split into

        PreparedPattern pattern = PreparedPattern.ofPattern(
                "''Hello, my '{name}' is {name} and my '{age}' is {age}.''"
        );
        List<Token> tokens = pattern.tokens;

        assertThat(tokens, hasSize(5));
        assertThat(tokens.get(0).toString(), is("'Hello, my {name} is "));
        assertThat(tokens.get(1).toString(), is("{name}"));
        assertThat(tokens.get(2).toString(), is(" and my {age} is "));
        assertThat(tokens.get(3).toString(), is("{age}"));
        assertThat(tokens.get(4).toString(), is(".'"));

        String formatted = pattern.apply(Collections.emptyMap());

        assertThat(
                formatted,
                is("'Hello, my {name} is {name} and my {age} is {age}.'")
        );
    }
}
