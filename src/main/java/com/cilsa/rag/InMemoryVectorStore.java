package com.cilsa.rag;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Implementación en memoria de VectorStore usando similitud del coseno.
 * Stub puro: no requiere dependencias externas, sirve para compilar y testear la arquitectura RAG.
 */
public class InMemoryVectorStore implements VectorStore {

    private final List<VectorEntry> entries = new ArrayList<>();

    private static final class VectorEntry {
        final String id;
        final float[] vector;
        final CsvRecord payload;

        VectorEntry(String id, float[] vector, CsvRecord payload) {
            this.id = id;
            this.vector = vector;
            this.payload = payload;
        }
    }

    @Override
    public void upsert(String id, float[] vector, CsvRecord payload) {
        // Si ya existe, lo reemplazamos; de lo contrario, añadimos
        for (VectorEntry entry : entries) {
            if (entry.id.equals(id)) {
                // Reemplazamos vector y payload en lugar
                int idx = entries.indexOf(entry);
                entries.set(idx, new VectorEntry(id, vector, payload));
                return;
            }
        }
        entries.add(new VectorEntry(id, vector, payload));
    }

    @Override
    public List<Scored> searchTopK(float[] query, int k) {
        List<Scored> results = new ArrayList<>();

        for (VectorEntry entry : entries) {
            float sim = cosineSimilarity(query, entry.vector);
            if (sim > 0) { // Solo resultados positivos
                results.add(new Scored(entry.payload, sim));
            }
        }

        // Ordenar de mayor a menor similitud
        results.sort naturalOrder();

        // Limitar a k
        if (k >= 0 && k < results.size()) {
            return results.subList(0, k);
        }
        return results;
    }

    private float cosineSimilarity(float[] a, float[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Vectors must have same length");
        }

        double dot = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += Math.pow(a[i], 2);
            normB += Math.pow(b[i], 2);
        }

        if (normA == 0 || normB == 0) {
            return 0f;
        }

        return (float) (dot / (Math.sqrt(normA) * Math.sqrt(normB)));
    }
}