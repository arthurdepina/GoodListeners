package com.goodlisteners;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class AppTest {

    @Test
    public void testGetGreeting() {
        assertEquals("Hello, World!", App.getGreeting());
    }
}
