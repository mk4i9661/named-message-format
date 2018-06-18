package org.mki.text;

import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class ParametersAwareTokenTest {

    @Test
    public void shouldRedirectToNamedTokenToString() {
        // verifies that if the token passed to the constructor is an instance of
        // NamedToken type, the toString(Object) method gets called

        NamedToken token = mock(NamedToken.class);

        when(token.identifier())
                .thenReturn(JavaIdentifier.of("name"));

        new ParametersAwareToken(
                token,
                Collections.singletonMap("name", "John")
        ).toString();

        verify(
                token
        ).toString("John");
    }

    @Test
    public void shouldFallbackToDefaultToString() {
        // verifies that if the parameters passed to the constructor either do not match
        // with the token identifier or its type is not NamedToken,
        // then the decorator backs to the default toString implementation
        NamedToken token = mock(NamedToken.class);

        when(token.identifier())
                .thenReturn(JavaIdentifier.of("name"));

        when(token.toString())
                .thenReturn("the default toString was called");

        assertThat(
                new ParametersAwareToken(
                        token,
                        Collections.singletonMap("age", "15")
                ).toString(),
                is("the default toString was called")
        );

        verify(
                token,
                never()
        ).toString(any());


        assertThat(
                new ParametersAwareToken(
                        RawToken.of("it is a raw token."),
                        Collections.singletonMap("age", "15")
                ).toString(),
                is("it is a raw token.")
        );
    }
}
