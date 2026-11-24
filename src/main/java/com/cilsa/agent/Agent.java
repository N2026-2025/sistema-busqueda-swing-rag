package com.cilsa.agent;

/**
 * Tipo de tarea que un agente puede procesar.
 */
public enum TaskType {
    SEARCH,
    SUMMARIZE,
    ANALYZE
}

/**
 * Contexto/parámetros para una tarea de agente.
 */
public record AgentTask(TaskType type, String query, Object... params) {
    public AgentTask(TaskType type, String query) {
        this(type, query);
    }
}

/**
 * Resultado que un agente puede producir.
 */
public record AgentResult(String result, TaskType type, long timestamp) {
    public AgentResult(TaskType type, String result) {
        this(result, type, System.currentTimeMillis());
    }
}

/**
 * Interfaz base para todos los agentes IA.
 * Define el contrato: recibir una tarea y producir un resultado.
 * Implementaciones concretas pueden conectarse a LLM, motores de búsqueda, etc.
 */
public interface Agent {
    /**
     * Ejecuta la tarea y devuelve un resultado.
     *
     * @param task la tarea a procesar
     * @return el resultado procesado
     */
    AgentResult run(AgentTask task);

    /**
     * Tipo de tarea que este agente soporta nativamente.
     *
     * @return el TaskType soportado
     */
    TaskType supportedType();
}