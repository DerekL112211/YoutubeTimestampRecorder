package com.example;

/**
 * Enum to define timestamp types for hierarchical structure
 */
public enum TimestampType {
    MAIN(""),                    // Main timestamp with no indentation
    SUB("    ");                 // Sub-timestamp with 4 spaces indentation

    private final String prefix;

    TimestampType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public String formatNote(String note) {
        if (note == null || note.trim().isEmpty()) {
            return "";
        }
        return prefix + note;
    }
    
    /**
     * Get the full-width space indentation for export (matches your desired output)
     */
    public String getExportPrefix() {
        if (this == SUB) {
            return "\u3000\u3000"; // Two full-width spaces
        }
        return "";
    }
    
    public String formatNoteForExport(String note) {
        if (note == null || note.trim().isEmpty()) {
            return "";
        }
        return getExportPrefix() + note;
    }
}
