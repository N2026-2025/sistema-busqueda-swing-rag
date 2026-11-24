package com.cilsa.search;

import com.cilsa.model.CsvRecord;
import com.cilsa.model.SearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvSearchEngineTest {

    private CsvSearchEngine searchEngine;

    @BeforeEach
    void setUp() {
        searchEngine = new CsvSearchEngine();
        // Cargar datos de prueba desde string
        searchEngine.loadFromString(
            "Nombre,Teléfono,País,Ocupación\n" +
            "Juan Pérez,555-1234,Argentina,Ingeniero\n" +
            "María García,555-5678,España,Diseñadora\n" +
            "Carlos Rodríguez,555-8765,Argentina,Gerente\n" +
            "Ana Martínez,555-3333,México,Analista"
        );
    }

    @Test
    void testEngineLoadsTestData() {
        assertNotNull(searchEngine);
        assertNotNull(searchEngine.getHeaders());
        assertEquals(4, searchEngine.getRecordCount());
    }

    @Test
    void testSearchByCountryArgentina() {
        List<CsvRecord> results = searchEngine.searchByCountry("Argentina");
        assertFalse(results.isEmpty());
        assertEquals(2, results.size()); // Juan Pérez y Carlos Rodríguez
        
        // Verificar que ambos son de Argentina
        for (CsvRecord r : results) {
            assertEquals("Argentina", r.getField("País"));
        }
    }

    @Test
    void testSearchByOccupationIngeniero() {
        List<CsvRecord> results = searchEngine.searchByOccupation("Ingeniero");
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("Juan Pérez", results.get(0).getField("Nombre"));
    }

    @Test
    void testSearchByAnyFieldEspaña() {
        List<CsvRecord> results = searchEngine.searchByAnyField("España");
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(r -> "España".equalsIgnoreCase(r.getField("País"))));
    }

    @Test
    void testSearchNoResults() {
        List<CsvRecord> results = searchEngine.searchByCountry("Mónaco");
        assertTrue(results.isEmpty());
    }
}