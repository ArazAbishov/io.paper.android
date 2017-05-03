package io.paper.android.commons.tuples;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

@RunWith(JUnit4.class)
public class PairUnitTests {

    @Test
    public void equalsAndHashcodeShouldConformToContract() {
        EqualsVerifier.forClass(Pair.create("one", "two").getClass())
                .suppress(Warning.NULL_FIELDS)
                .verify();
    }
}