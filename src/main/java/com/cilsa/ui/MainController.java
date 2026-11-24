package com.cilsa.ui;

import com.cilsa.model.CsvRecord;
import com.cilsa.model.SearchCriteria;
import com.cilsa.search.CsvSearchEngine;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador JavaFX para MainView.fxml.
 * Mapea las acciones de la UI (buscar, guardar, menú, etc.) a la lógica de negocio.
 */
public class MainController {

    @FXML private TextField archivoField;
    @FXML private ComboBox<String> criterioBox;
    @FXML private TextField terminoField;
    @FXML private TextArea resultadosArea;
    @FXML private Button buscarBtn;
    @FXML private Button limpiarBtn;
    @FXML private TextField encabezado1Field;
    @FXML private TextField encabezado2Field;
    @FXML private TextField encabezado3Field;
    @FXML private TextField encabezado4Field;
    @FXML private Button guardarBtn;
    @FXML private Button cerrarBtn;
    @FXML private CustomStatusBar statusBar;

    private CsvSearchEngine searchEngine;
    private String archivoActual = "";

    public void setSearchEngine(CsvSearchEngine engine) {
        this.searchEngine = engine;
    }

    @FXML
    public void initialize() {
        // Inicializar criterios
        if (criterioBox != null) {
            criterioBox.getItems().addAll("País", "Ocupación", "Encabezado");
            criterioBox.setValue("País");
        }
        
        // Configurar campo de término
        if (terminoField != null) {
            terminoField.setPrefWidth(200);
        }
        
        if (resultadosArea != null) {
            resultadosArea.setWrapText(true);
        }
        
        actualizarEstado("Listo");
    }

    @FXML
    private void handleSeleccionarArchivo(ActionEvent event) {
        if (searchEngine == null) {
            mostrarError("Motor de búsqueda no inicializado");
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo CSV");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );
        
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            archivoActual = selectedFile.getAbsolutePath();
            archivoField.setText(archivoActual);
            
            try {
                searchEngine.load(archivoActual);
                actualizarEstado("Archivo cargado: " + selectedFile.getName() + " (" + searchEngine.getRecordCount() + " registros)");
                
                // Llenar los campos de encabezado automáticamente si existen
                String[] headers = searchEngine.getHeaders();
                if (headers != null && encabezado1Field != null) {
                    encabezado1Field.setText(headers.length > 0 ? headers[0] : "");
                    encabezado2Field.setText(headers.length > 1 ? headers[1] : "");
                    encabezado3Field.setText(headers.length > 2 ? headers[2] : "");
                    encabezado4Field.setText(headers.length > 3 ? headers[3] : "");
                }
            } catch (IOException e) {
                mostrarError("Error al cargar el archivo: " + e.getMessage());
            }
            if (statusBar != null) {
                statusBar.setInfo("Listo");
            }
        }
    }

    @FXML
    private void handleBuscar(ActionEvent event) {
        if (searchEngine == null || archivoActual.isEmpty()) {
            mostrarError("Por favor, seleccione un archivo primero.");
            return;
        }
        
        String termino = terminoField.getText().trim();
        if (termino.isEmpty()) {
            mostrarError("Ingrese un término de búsqueda.");
            return;
        }
        
        String criterioSeleccionado = criterioBox.getValue();
        if (criterioSeleccionado == null) {
            mostrarError("Seleccione un criterio de búsqueda.");
            return;
        }
        
        try {
            SearchCriteria criteria = switch (criterioSeleccionado) {
                case "País" -> SearchCriteria.PAIS;
                case "Ocupación" -> SearchCriteria.OCUPACIÓN;
                case "Encabezado" -> SearchCriteria.ENCABEZADO;
                default -> SearchCriteria.ENCABEZADO;
            };
            
            List<CsvRecord> resultados = searchEngine.search(criteria.name().toLowerCase(), termino);
            
            // Mostrar resultados en el área de texto
            StringBuilder sb = new StringBuilder();
            if (resultados.isEmpty()) {
                sb.append("No se encontraron resultados.");
            } else {
                for (CsvRecord registro : resultados) {
                    sb.append(registro.toString()).append("\n");
                }
            }
            resultadosArea.setText(sb.toString());
            
            actualizarEstado(String.format("Resultados: %d registro(s) encontrado(s)", resultados.size()));
            
        } catch (Exception e) {
            mostrarError("Error durante la búsqueda: " + e.getMessage());
        }
    }

    @FXML
    private void handleGuardaryLimpiar(ActionEvent event) {
        if (searchEngine == null) {
            mostrarError("Motor de búsqueda no inicializado");
            return;
        }
        
        String resultadosTexto = resultadosArea.getText();
        if (resultadosTexto.isEmpty() || resultadosTexto.equals("No se encontraron resultados.")) {
            mostrarInformacion("No hay resultados para guardar.");
            return;
        }
        
        // Obtener los encabezados
        String h1 = encabezado1Field.getText().trim();
        String h2 = encabezado2Field.getText().trim();
        String h3 = encabezado3Field.getText().trim();
        String h4 = encabezado4Field.getText().trim();
        
        if (h1.isEmpty() || h2.isEmpty() || h3.isEmpty() || h4.isEmpty()) {
            mostrarError("Los campos de encabezado no pueden estar vacíos.");
            return;
        }
        
        // Generar nombre de archivo
        String termino = terminoField.getText().trim().replaceAll("[^a-zA-Z0-9]", "_");
        String nombreArchivo = termino.isEmpty()
                ? "resultados_" + System.currentTimeMillis() + ".csv"
                : "resultados_" + termino + ".csv";
        
        try (java.io.BufferedWriter bw = 
                new java.io.BufferedWriter(new java.io.FileWriter(nombreArchivo))) {
            
            // Escribir encabezados
            bw.write(h1 + "," + h2 + "," + h3 + "," + h4);
            bw.newLine();
            
            // Escribir resultados (solo las líneas de datos, sin encabezado duplicado)
            String[] lineas = resultadosTexto.split("\n");
            for (String linea : lineas) {
                if (!linea.trim().isEmpty() && !linea.startsWith("No se")) {
                    bw.write(linea);
                    bw.newLine();
                }
            }
            
        } catch (IOException e) {
            mostrarError("Error al guardar los resultados: " + e.getMessage());
            return;
        }
        
        JOptionPane.showMessageDialog(null, 
            "Resultados guardados en '" + nombreArchivo + "'", 
            "Éxito", 
            JOptionPane.INFORMATION_MESSAGE);
        
        limpiarCampos();
        actualizarEstado("Resultados guardados en '" + nombreArchivo + "'");
    }

    @FXML
    private void handleGuardarResultados(ActionEvent event) {
        // Delegar en handleGuardaryLimpiar para reutilizar lógica
        handleGuardaryLimpiar(event);
    }

    @FXML
    private void handleCerrar(ActionEvent event) {
        // Cerrar la ventana/escena
        Node source = (Node) event.getSource();
        Window owner = source.getScene().getWindow();
        owner.hide();
    }

    @FXML
    private void handleAcercaDe(ActionEvent event) {
        // Diálogo simple de "Acerca de"
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca de");
        alert.setHeaderText(null);
        alert.setContentText("Sistema de Búsqueda 2026\nAplicación JavaFX con estructura modular");
        alert.showAndWait();
    }

    @FXML
    private void handleSalir(ActionEvent event) {
        Window source = ((Node) event.getSource()).getScene().getWindow();
        source.hide();
    }

    @FXML
    private void handleCriterioCambio(ActionEvent event) {
        // Actualizar estado al cambiar criterio
        if (criterioBox.getValue() != null) {
            actualizarEstado("Criterio cambiado a: " + criterioBox.getValue());
        }
    }

    @FXML
    private void handleBuscarArchivo(ActionEvent event) {
        handleSeleccionarArchivo(event);
    }

    private void mostrarError(String mensaje) {
        statusBar.setInfo(mensaje);
        // También mostrar en diálogo
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInformacion(String mensaje) {
        statusBar.setInfo(mensaje);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void actualizarEstado(String mensaje) {
        if (statusBar != null) {
            statusBar.setInfo(mensaje);
        }
    }

    private void limpiarCampos() {
        if (archivoField != null) archivoField.setText("");
        if (terminoField != null) terminoField.setText("");
        if (resultadosArea != null) resultadosArea.setText("");
        if (criterioBox != null) criterioBox.setValue("País");
        actualizarEstado("Listo");
    }
}