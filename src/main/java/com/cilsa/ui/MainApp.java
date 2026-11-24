package com.cilsa.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Punto de entrada JavaFX para la aplicación Sistema de Búsqueda 2026.
 * Carga el archivo FXML y muestra la ventana principal.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cilsa/ui/MainView.fxml"));
        loader.setControllerFactory(MainController::new);
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        // Aplicar estilos básicos si existe el archivo CSS
        try {
            scene.getStylesheets().add(getClass().getResource("/com/cilsa/ui/style.css").toExternalForm());
        } catch (Exception e) {
            // CSS opcional, no es crítico
        }
        
        primaryStage.setTitle("Sistema de Búsqueda 2026");
        primaryStage.setResizable(true);
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}