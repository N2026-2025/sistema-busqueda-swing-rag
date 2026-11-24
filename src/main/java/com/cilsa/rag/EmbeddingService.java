package com.cilsa.rag;

/**
 * Servicio de embeddings: convierte texto a vector numérico (float[]).
 * Esta es la interfaz que permitiría conectar embeddings reales (OpenAI, HuggingFace, etc.)
 * o implementaciones ligeras (TF-IDF, hashing) sin dependencias externas.
 */
public interface EmbeddingService {
    /**
     * Embed un texto y devuelve su representación vectorial.
     *
     * @param text texto a incrustar
     * @return vector de floats normalizado (puede ser null si el texto está vacío)
     */
    float[] embed(String text);
}