package com.cilsa.model;

/**
 * Representa un registro del CSV con campos tipados y nombre de encabezados.
 */
public class CsvRecord {
    private final String[] fields;
    private final String[] headers;

    public CsvRecord(String[] fields, String[] headers) {
        this.fields = fields;
        this.headers = headers;
    }

    public String getField(String header) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equalsIgnoreCase(header)) {
                return fields[i];
            }
        }
        return null;
    }

    public String getField(int index) {
        if (index >= 0 && index < fields.length) {
            return fields[index];
        }
        return null;
    }

    public int getColumnIndex(String header) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equalsIgnoreCase(header)) {
                return i;
            }
        }
        return -1;
    }

    public int getColumnCount() {
        return fields.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(fields[i]);
        }
        return sb.toString();
    }

    public boolean matches(String criteria, String term) {
        int col = getColumnIndex(criteria);
        if (col >= 0 && col < fields.length) {
            String fieldValue = fields[col];
            if (fieldValue != null) {
                return fieldValue.toLowerCase().contains(term.toLowerCase());
            }
        }
        // Búsqueda en todos los campos (para "Encabezado Específico")
        for (String field : fields) {
            if (field != null && field.toLowerCase().contains(term.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}