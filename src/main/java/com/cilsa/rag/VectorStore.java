package com.cilsa.rag;

/**
 * Registro punteado para resultados de vector store.
 */
public record Scored(CsvRecord record, float score) implements Comparable<Scored> {
    @Override
    public int compareTo(Scored other) {
        return Float.compare(other.score, this.score); // Mayor score primero (descendente)
    }
}

/**
 * Interfaz para store de vectores: insertar y buscar por similitud.
 */
public interface VectorStore {
    /**
     * Inserta un registro con su vector de incrustación.
     *
     * @param id        identificador único
     * @param vector    vector de incrustación
     * @param payload   el registro asociado
     */
    void upsert(String id, float[] vector, CsvRecord payload);

    /**
     * Busca los k vectores más similares a la consulta dada (similitud del coseno).
     *
     * @param query     vector de consulta
     * @param k         número de resultados a retornar
     * @return lista de Scored ordenados de mayor a menor similitud
     */
    List<Scored> searchTopK(float[] query, int k);
}