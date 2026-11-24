package com.cilsa.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para CsvRecord.
 */
class CsvRecordTest {

    private CsvRecord record;

    @BeforeEach
    void setUp() {
        String[] fields = {"Juan Pérez", "555-1234", "Argentina", "Ingeniero"};
        String[] headers = {"Nombre", "Teléfono", "País", "Ocupación"};
        record = new CsvRecord(fields, headers);
    }

    @Test
    void testGetFieldByHeader() {
        assertEquals("Argentina", record.getField("País"));
        assertEquals("555-1234", record.getField("Teléfono"));
        assertNull(record.getField("NoExiste"));
    }

    @Test
    void testGetFieldByIndex() {
        assertEquals("Argentina", record.getField(2));
        assertEquals("Ingeniero", record.getField(3));
        assertNull(record.getField(-1));
        assertNull(record.getField(10));
    }

    @Test
    void testColumnIndex() {
        assertEquals(2, record.getColumnIndex("País"));
        assertEquals(1, record.getColumnIndex("Teléfono"));
        assertEquals(-1, record.getColumnIndex("NoExiste"));
    }

    @Test
    void testMatches() {
        // Búsqueda por país
        assertTrue(record.matches("País", "Argentina"));
        assertTrue(record.matches("País", "arg"));
        assertFalse(record.matches("País", "Brasil"));

        // Búsqueda por ocupación
        assertTrue(record.matches("Ocupación", "Ingeniero"));
        assertTrue(record.matches("Ocupación", "ingen"));
        assertFalse(record.matches("Ocupación", "diseñador"));

        // Búsqueda en todos los campos (Encabezado Específico)
        assertTrue(record.matches("Encabezado", "Juan"));
        assertTrue(record.matches("Encabezado", "555"));
        assertTrue(record.matches("Encabezado", "Ingeniero"));
        assertFalse(record.matches("Encabezado", "María"));
    }
}