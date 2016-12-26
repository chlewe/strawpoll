package me.lewelup.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * No further description has been provided.
 * TODO: Add javadoc description
 *
 * @author Christian Lewe
 */
public class OptionTest {
    @Test
    public void test_equals() {
        Option op1 = new Option("1");
        Option op2 = new Option("2");
        Option op3 = new Option("1");

        assertFalse(op1.equals(op2));
        assertTrue(op1.equals(op3));
    }
}
