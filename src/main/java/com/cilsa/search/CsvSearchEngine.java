package com.cilsa.search;

import com.cilsa.model.CsvRecord;
import com.cilsa.model.SearchCriteria;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Motor de búsqueda sobre archivos CSV.
 * Hereda la lógica original de DesafioFinalV2GUI/DesafioFinalV2 pero operando sobre
 * objetos CsvRecord en memoria para ser usado por la UI JavaFX y la capa RAG/Agentes.
 */
public class CsvSearchEngine {

    private final List<CsvRecord> records = new ArrayList<>();
    private String[] headers;

    /**
     * Carga un archivo CSV y parsea los registros y encabezados.
     *
     * @param filePath ruta al archivo CSV
     * @throws IOException si el archivo no existe o no se puede leer
     */
    public void load(String filePath) throws IOException {
        records.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            if (line != null) {
                String[] headerLine = line.split(",", -1);
                this.headers = headerLine;
            }
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",", -1);
                records.add(new CsvRecord(fields, headers));
            }
        }
    }

    /**
     * Carga datos CSV desde un string (útil para testing unitario).
     *
     * @ contenido CSV formateado como string
     */
    public void loadFromString(String csvContent) {
        records.clear();
        String[] lines = csvContent.split("\n", -1);
        if (lines.length > 0) {
            this.headers = lines[0].split(",", -1);
            for (int i = 1; i < lines.length; i++) {
                String[] fields = lines[i].split(",", -1);
                records.add(new CsvRecord(fields, headers));
            }
        }
    }

    /**
     * Busca registros que coinciden con el criterio y término dados.
     *
     * @param criteria criterio de búsqueda (SearchCriteria.PAIS, .OCUPACIÓN, .ENCABEZADO, .SEMÁNTICO)
     * @param term     término a buscar (minúsculas)
     * @return lista de registros coincidentes
     */
    public List<CsvRecord> search(String criteria, String term) {
        List<CsvRecord> results = new ArrayList<>();
        for (CsvRecord record : records) {
            if (record.matches(criteria, term)) {
                results.add(record);
            }
        }
        return results;
    }

    /**
     * Busca por país (columna índice 2 en el CSV original).
     */
    public List<CsvRecord> searchByCountry(String term) {
        return search(SearchCriteria.PAIS, term);
    }

    /**
     * Busca por ocupación (columna índice 3 en el CSV original).
     */
    public List<CsvRecord> searchByOccupation(String term) {
        return search(SearchCriteria.OCUPACIÓN, term);
    }

    /**
     * Busca por coincidencia en cualquier campo (comportamiento "Encabezado Específico").
     */
    public List<CsvRecord> searchByAnyField(String term) {
        return search(SearchCriteria.ENCABEZADO, term);
    }

    /**
     * Devuelve los encabezados leídos del archivo CSV.
     */
    public String[] getHeaders() {
        return headers;
    }

    /**
     * Devuelve el número total de registros cargados.
     */
    public int getRecordCount() {
        return records.size();
    }
}