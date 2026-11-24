package com.cilsa.model;

/**
 * Enum que define los criterios de búsqueda disponibles.
 */
public enum SearchCriteria {
    PAIS("País"),
    OCUPACIÓN("Ocupación"),
    ENCABEZADO("Encabezado"),
    SEMÁNTICO("Semántico");

    private final String displayName;

    SearchCriteria(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}