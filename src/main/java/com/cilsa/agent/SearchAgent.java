package com.cilsa.agent;

import com.cilsa.model.CsvRecord;
import com.cilsa.search.CsvSearchEngine;

/**
 * Agente de búsqueda: ejecuta búsquedas sobre el motor CSV o RAG.
 * Implementación stub que usa CsvSearchEngine como fuente de datos.
 */
public class SearchAgent implements Agent {

    private final CsvSearchEngine csvSearchEngine;

    public SearchAgent(CsvSearchEngine csvSearchEngine) {
        this.csvSearchEngine = csvSearchEngine;
    }

    @Override
    public AgentResult run(AgentTask task) {
        if (task.type() == TaskType.SEARCH) {
            String query = task.query();
            // Los params pueden ser: [SearchCriteria, term]
            if (task.params().length >= 2) {
                SearchCriteria criteria = (SearchCriteria) task.params()[0];
                String term = (String) task.params()[1];
                List<CsvRecord> results = csvSearchEngine.search(criteria.name().toLowerCase(), term);
                String summary = String.format("Encontrados %d resultados para %s: %s", 
                    results.size(), query, formatRecords(results));
                return new AgentResult(summary, TaskType.SEARCH, System.currentTimeMillis());
            }
        }
        return new AgentResult("Tarea no soportada: " + task.type(), task.type(), System.currentTimeMillis());
    }

    @Override
    public TaskType supportedType() {
        return TaskType.SEARCH;
    }

    private String formatRecords(List<CsvRecord> records) {
        if (records.isEmpty()) return "ninguno";
        return String.join("; ", records.stream()
                .limit(3)
                .map(CsvRecord::toString)
                .toArray(String[]::new));
    }
}