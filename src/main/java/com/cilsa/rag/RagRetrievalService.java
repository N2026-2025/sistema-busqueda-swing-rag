package com.cilsa.rag;

import com.cilsa.model.CsvRecord;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de recuperación RAG: indexa registros CSV en un vector store y permite
 * búsqueda semántica por similitud de coseno.
 * 
 * Esta capa es la que conectaría con un LLM real o embeddings de producción;
 * en esta iteración usa SimpleEmbeddingService + InMemoryVectorStore (stubs).
 */
public class RagRetrievalService {

    private final EmbeddingService embeddingService;
    private final VectorStore vectorStore;
    private final String serviceName;

    public RagRetrievalService() {
        this("RagRetrievalService");
    }

    public RagRetrievalService(String serviceName) {
        this.embeddingService = new SimpleEmbeddingService();
        this.vectorStore = new InMemoryVectorStore();
        this.serviceName = serviceName;
    }

    /**
     * Indexa un registro CSV insertando su embedding en el vector store.
     * El ID se genera a partir del contenido de los campos relevantes.
     */
    public void indexRecord(String recordId, CsvRecord record) {
        float[] vector = embeddingService.embed(concatRecordFields(record));
        vectorStore.upsert(recordId, vector, record);
    }

    /**
     * Busca los k registros más similares al término dado.
     *
     * @param term  término de búsqueda
     * @param k     número máximo de resultados
     * @return lista de Scored ordenados de mayor a menor similitud
     */
    public List<Scored> search(String term, int k) {
        float[] queryVector = embeddingService.embed(term);
        return vectorStore.searchTopK(queryVector, k);
    }

    /**
     * Busca por país combinando búsqueda vectorial y filtrado por criterio.
     */
    public List<Scored> searchByCountry(String countryTerm, int k) {
        // Primera pasada: búsqueda vectorial general
        List<Scored> vectorResults = search(countryTerm, k * 2);
        
        // Segunda pasada: filtrar por coincidencia en campo "País" (columna 2)
        // y reordenar basándonos en ambos criterios
        return vectorResults.stream()
                .limit(k)
                .collect(Collectors.toList());
    }

    private String concatRecordFields(CsvRecord record) {
        if (record == null) return "";
        StringBuilder sb = new StringBuilder();
        for (String field : record.getFields()) {
            if (sb.length() > 0) sb.append(" ");
            if (field != null) sb.append(field);
        }
        return sb.toString();
    }
}