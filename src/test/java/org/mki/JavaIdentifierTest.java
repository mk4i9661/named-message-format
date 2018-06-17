package org.mki;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JavaIdentifierTest {

    @Test(expected = InvalidJavaIdentifierException.class)
    public void shouldThrowExceptionIfNull() {
        JavaIdentifier.of(null);
    }

    @Test(expected = InvalidJavaIdentifierException.class)
    public void shouldThrowExceptionIfEmpty() {
        JavaIdentifier.of("");
    }

    @Test(expected = InvalidJavaIdentifierException.class)
    public void shouldThrowExceptionIfStartsWithDigit() {
        JavaIdentifier.of("1");
    }

    @Test(expected = InvalidJavaIdentifierException.class)
    public void shouldThrowExceptionIfHasADot() {
        JavaIdentifier.of("test.identifier");
    }

    @Test(expected = InvalidJavaIdentifierException.class)
    public void shouldThrowExceptionIfHasABrace() {
        JavaIdentifier.of("{");
    }

    @Test
    public void shouldBeValid() {
        JavaIdentifier identifier = JavaIdentifier.of("_test$identifier");
        assertThat(identifier.toString(), is("_test$identifier"));

        assertThat(JavaIdentifier.of("_").toString(), is("_"));

        assertThat(JavaIdentifier.of("$").toString(), is("$"));
    }

    @Test
    public void shouldHandleCyrillicNames() {
        assertThat(
                JavaIdentifier.of("$_имя$значение").toString(),
                is("$_имя$значение")
        );
    }
}
