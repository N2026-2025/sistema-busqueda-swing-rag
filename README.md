# Sistema de Búsqueda Desktop AI 2026

Aplicación de escritorio moderna para búsqueda en archivos CSV, construida con **Java 25**, **JavaFX** y **Maven**.

## Características

- **Interfaz de usuario JavaFX**: experiencia de escritorio moderna, reemplazando la antigua interfaz Swing
- **Motor de búsqueda CSV**: filtrado por país, ocupación o coincidencia en cualquier campo
- **Capa RAG (Retrieval-Augmented Generation)**: stubs para incrustaciones (embeddings) y store de vectores en memoria
- **Agentes IA**: arquitectura de agentes con `SearchAgent` y `SummarizationAgent` para extensión futura
- **Búsqueda semántica**: compatibilidad con similitud de coseno mediante `InMemoryVectorStore`
- **Modular**: estructura de paquetes limpia (`model`, `search`, `rag`, `agent`, `ui`)

## Stack Tecnológico

- **Java 25** LTS
- **JavaFX 25.0.1** ( controles + FXML)
- **Maven** 3.8+ (build)
- **JUnit 5** (testing)
- **OpenJFX** (UI framework)

## Estructura del Proyecto

```
sistema-busqueda-swing-rag/
├── pom.xml                                          # Configuración Maven
├── README.md                                        # Este archivo
├── LICENSE                                          # Licencia MIT
├── .gitignore                                       # Archivos a excluir
└── src/
    ├── main/java/com/cilsa/
    │   ├── model/                                   # CsvRecord, SearchCriteria
    │   ├── search/                                  # CsvSearchEngine
    │   ├── rag/                                     # EmbeddingService, VectorStore, RagRetrievalService
    │   ├── agent/                                   # Agent, SearchAgent, SummarizationAgent, AgentOrchestrator
    │   └── ui/                                      # MainApp, MainController, CustomStatusBar, MainView.fxml
    ├── main/resources/com/cilsa/
    │   ├── ui/MainView.fxml                         # Definición de interfaz JavaFX
    │   └── application.properties                   # Configuración
    └── test/java/com/cilsa/                         # Tests JUnit 5
        ├── model/CsvRecordTest.java
        ├── search/CsvSearchEngineTest.java
        └── rag/RagRetrievalServiceTest.java
```

## Ejecución

```bash
# Clonar el repositorio
git clone https://github.com/N2026-2025/sistema-busqueda-swing-rag.git
cd sistema-busqueda-swing-rag

# Compilar y ejecutar tests
mvn -B clean verify

# Ejecutar la aplicación JavaFX
mvn javafx:run
```

## Roadmap

- [x] Estructura modular Maven + Java 25 + JavaFX
- [x] Motor de búsqueda CSV refactorizado
- [x] Capa RAG con stubs (embeddings + vector store en memoria)
- [x] Arquitectura de agentes (interfaces + implementaciones)
- [ ] Integrar embeddings reales (OpenAI, HuggingFace)
- [ ] Conectar LLM para resúmenes automáticos
- [ ] Persistencia de historial de búsquedas
- [ ] Modo oscuro / tema claro
- [] Exportar a múltiples formatos (JSON, Excel)

## Licencia

MIT. Ver archivo LICENSE para detalles.