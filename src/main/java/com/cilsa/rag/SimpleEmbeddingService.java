package com.cilsa.rag;

/**
 * Implementación stub determinista de EmbeddingService.
 * Utiliza un hash simple para mapear texto a vector fijo.
 * Sirve para compilar y testear la estructura sin dependencias de IA externas.
 */
public class SimpleEmbeddingService implements EmbeddingService {

    private static final int VECTOR_DIMENSION = 64;

    @Override
    public float[] embed(String text) {
        if (text == null || text.isBlank()) {
            return new float[VECTOR_DIMENSION];
        }

        // Hash determinista: genera un vector "pseudoaleatorio" pero reproducible
        int h = text.toLowerCase(Locale.ROOT).hashCode();
        float[] vector = new float[VECTOR_DIMENSION];

        // Rellenamos el vector basándonos en el hash
        for (int i = 0; i < VECTOR_DIMENSION; i++) {
            // Usar diferentes partes del hash para cada componente
            int component = (h >> (i * 5)) & 0x1F;
            vector[i] = component / 31.0f; // Normaliza roughly a [-1, 1] range
        }
        return vector;
    }
}