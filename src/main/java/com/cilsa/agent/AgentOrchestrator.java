package com.cilsa.agent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Orchestrator que coordina múltiples agentes IA.
 * Decide qué agente invocar según el tipo de tarea y puede combinar resultados.
 */
public class AgentOrchestrator {

    private final List<Agent> agents;

    public AgentOrchestrator(Agent... agents) {
        this.agents = List.of(agents);
    }

    /**
     * Ejecuta la tarea con el agente apropiado.
     */
    public AgentResult execute(AgentTask task) {
        // Encontrar un agente que soporte este tipo
        for (Agent agent : agents) {
            if (agent.supportedType() == task.type()) {
                return agent.run(task);
            }
        }
        // Ningún agente coincide: resultado por defecto
        return new AgentResult("Ningún agente disponible para el tipo: " + task.type(), task.type(), System.currentTimeMillis());
    }

    /**
     * Ejecuta una tarea y combina resultados de varios agentes si aplican.
     */
    public String executeAndCombine(AgentTask task) {
        List<AgentResult> results = agents.stream()
                .filter(a -> a.supportedType() == task.type())
                .map(a -> a.run(task))
                .toList();

        if (results.isEmpty()) {
            return "No hay agentes disponibles para este tipo de tarea.";
        }

        // Combinar todos los resultados de texto
        return results.stream()
                .map(AgentResult::result)
                .collect(Collectors.joining("\n---\n"));
    }
}