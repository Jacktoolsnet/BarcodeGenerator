package net.jacktools.barcode.barcodegenerator.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssetsTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getString() {
        assertEquals("Barcode generator", Assets.getString("application.title"));
    }

    @Test
    void testGetString() {
    }
}