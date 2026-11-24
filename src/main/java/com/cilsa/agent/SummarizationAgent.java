package com.cilsa.agent;

/**
 * Agente de resumen: toma resultados de búsqueda y produce un resumen legible.
 * Implementación stub: simplemente formatea los resultados clave.
 * En una iteración futura conectaría con un LLM real.
 */
public class SummarizationAgent implements Agent {

    private final CsvSearchEngine csvSearchEngine;

    public SummarizationAgent(CsvSearchEngine csvSearchEngine) {
        this.csvSearchEngine = csvSearchEngine;
    }

    @Override
    public AgentResult run(AgentTask task) {
        if (task.params().length >= 1) {
            String query = (String) task.params()[0];
            // Buscar término genérico y resumir
            List<CsvRecord> results = csvSearchEngine.search(SearchCriteria.ENCABEZADO, query);
            String summary;
            if (results.isEmpty()) {
                summary = "No se encontraron resultados para: " + query;
            } else {
                String topResult = results.get(0).toString();
                summary = String.format("Se encontraron %d resultados. El principal coincide con: %s", 
                    results.size(), topResult);
            }
            return new AgentResult(summary, TaskType.SUMMARIZE, System.currentTimeMillis());
        }
        return new AgentResult("Parámetros inválidos para resumen", TaskType.SUMMARIZE, System.currentTimeMillis());
    }

    @Override
    public TaskType supportedType() {
        return TaskType.SUMMARIZE;
    }
}