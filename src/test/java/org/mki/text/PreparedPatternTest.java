package org.mki.text;

import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mki.text.UsFormatter.US_FORMATTER;

public class PreparedPatternTest {

    @Test
    public void shouldSplitIntoTokens() {
        // verifies that quoted text remains as is, whereas named groups get split into

        PreparedPattern pattern = PreparedPattern.ofPattern(
                "''Hello, my '{name}' is {name} and my '{age}' is {age}.''"
        );
        List<ParameterBinding> tokens = pattern.binders;

        assertThat(tokens, hasSize(5));
        assertThat(tokens.get(0).format(emptyMap(), US_FORMATTER), is("'Hello, my {name} is "));
        assertThat(tokens.get(1).format(emptyMap(), US_FORMATTER), is("{name}"));
        assertThat(tokens.get(2).format(emptyMap(), US_FORMATTER), is(" and my {age} is "));
        assertThat(tokens.get(3).format(emptyMap(), US_FORMATTER), is("{age}"));
        assertThat(tokens.get(4).format(emptyMap(), US_FORMATTER), is(".'"));

        String formatted = pattern.apply(emptyMap(), US_FORMATTER);

        assertThat(
                formatted,
                is("'Hello, my {name} is {name} and my {age} is {age}.'")
        );
    }

    @Test
    public void shouldFormatIfHasASingleToken() {
        // ensures that a single token sequence gets formatted

        PreparedPattern pattern = PreparedPattern.ofPattern("{name}");
        List<ParameterBinding> tokens = pattern.binders;

        assertThat(tokens, hasSize(1));
        assertThat(
                tokens.get(0).format(emptyMap(), US_FORMATTER),
                is("{name}")
        );

        assertThat(
                pattern.apply("name", "John", US_FORMATTER),
                is("John")
        );
    }

    @Test
    public void shouldFormatIfHasNoNamedTokens() {
        // ensures that a sequence without any named tokens gets formatted 'as is'

        PreparedPattern pattern = PreparedPattern.ofPattern("name");
        List<ParameterBinding> tokens = pattern.binders;

        assertThat(tokens, hasSize(1));
        assertThat(
                pattern.apply("name", "John", US_FORMATTER),
                is("name")
        );
    }

    @Test
    public void shouldHandleInnerBraces() {
        // checks whether subformat inner braces
        PreparedPattern pattern = PreparedPattern.ofPattern("{date,time,{dd.MM.yyyy}}");
        String formatted = pattern.apply(
                "date",
                Date.from(
                        LocalDate.of(2018, 6, 15)
                                .atStartOfDay()
                                .toInstant(ZoneOffset.UTC)
                ),
                US_FORMATTER
        );
        assertThat(formatted, is("{15.06.2018}"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailSingleOpeningBrace() {
        PreparedPattern.ofPattern("{");
    }

    @Test
    public void shouldHandleSingleClosingBrace() {
        PreparedPattern pattern = PreparedPattern.ofPattern("}");
        assertThat(pattern.binders, hasSize(1));
        assertThat(
                pattern.binders.get(0).format(emptyMap(), US_FORMATTER),
                is("}")
        );
        assertThat(
                pattern.apply(
                        "ignore",
                        "ignore",
                        US_FORMATTER
                ),
                is("}")
        );
    }

    @Test
    public void shouldIgnoreExtraClosingBraces() {
        // ensures that extra closing braces will not hurt the functionality

        PreparedPattern pattern = PreparedPattern.ofPattern("{price,number,#.##}}");
        List<ParameterBinding> tokens = pattern.binders;

        assertThat(tokens, hasSize(2));
        assertThat(
                tokens.get(0)
                        .format(emptyMap(), US_FORMATTER),
                is("{price,number,#.##}")
        );
        assertThat(
                tokens.get(1).format(emptyMap(), US_FORMATTER), is("}")
        );

        assertThat(
                pattern.apply("price", 15.99, US_FORMATTER),
                is("15.99}")
        );
    }
}
