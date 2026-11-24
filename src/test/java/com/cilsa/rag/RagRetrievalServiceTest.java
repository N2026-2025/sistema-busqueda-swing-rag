package com.cilsa.rag;

import com.cilsa.model.CsvRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RagRetrievalServiceTest {

    private RagRetrievalService retrievalService;

    @BeforeEach
    void setUp() {
        retrievalService = new RagRetrievalService();
        // Indexar algunos registros de prueba
        String[] headers1 = {"Juan Pérez", "555-1234", "Argentina", "Ingeniero"};
        String[] headers2 = {"María García", "555-5678", "España", "Diseñadora"};
        String[] headers3 = {"Carlos Rodríguez", "555-8765", "Argentina", "Gerente"};

        CsvRecord record1 = new CsvRecord(headers1, headers1);
        CsvRecord record2 = new CsvRecord(headers2, headers2);
        CsvRecord record3 = new CsvRecord(headers3, headers3);

        retrievalService.indexRecord("rec1", record1);
        retrievalService.indexRecord("rec2", record2);
        retrievalService.indexRecord("rec3", record3);
    }

    @Test
    void testServiceInitialization() {
        assertNotNull(retrievalService);
    }

    @Test
    void testSearchByTerm() {
        List<Scored> results = retrievalService.search("Argentina", 10);
        assertFalse(results.isEmpty());
        // Deberíamos tener al menos 2 registros (record1 y record3 son de Argentina)
        assertTrue(results.size() >= 2);
    }

    @Test
    void testSearchReturnsScoredResults() {
        List<Scored> results = retrievalService.search("Argentina", 5);
        assertNotNull(results);
        for (Scored s : results) {
            assertNotNull(s.record());
            assertTrue(s.score() > 0);
        }
    }

    @Test
    void testSearchWithDifferentTerms() {
        List<Scored> results = retrievalService.search("Diseñadora", 10);
        assertFalse(results.isEmpty());
        // María García es Diseñadora en España
        assertTrue(results.stream().anyMatch(s -> s.record().getField("Ocupación").equals("Diseñadora")));
    }
}