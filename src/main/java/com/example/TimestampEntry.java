package com.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a timestamp entry with time, notes, and type information
 */
public class TimestampEntry {
    private String timestamp;
    private String notes;
    private TimestampType type;
    private LocalDateTime dateAdded;
    
    public TimestampEntry(String timestamp, String notes, TimestampType type) {
        this.timestamp = timestamp;
        this.notes = notes;
        this.type = type;
        this.dateAdded = LocalDateTime.now();
    }
    
    // Getters
    public String getTimestamp() {
        return timestamp;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public TimestampType getType() {
        return type;
    }
    
    public LocalDateTime getDateAdded() {
        return dateAdded;
    }
    
    // Setters
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public void setType(TimestampType type) {
        this.type = type;
    }
    
    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }
    
    /**
     * Returns the formatted timestamp for display, with indentation for sub-timestamps
     */
    public String getDisplayTimestamp() {
        if (type == TimestampType.SUB) {
            return "\u3000\u3000" + timestamp; // Full-width spaces for indentation
        }
        return timestamp;
    }
    
    /**
     * Returns the formatted notes for display, with indentation for sub-timestamps
     */
    public String getDisplayNotes() {
        if (type == TimestampType.SUB) {
            return "\u3000\u3000" + notes; // Full-width spaces for indentation
        }
        return notes;
    }
    
    /**
     * Converts this entry to a table row for JTable display
     */
    public Object[] toTableRow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new Object[] {
            getDisplayTimestamp(),
            getDisplayNotes(),
            dateAdded.format(formatter)
        };
    }
    
    /**
     * Returns the export format string for text file output
     */
    public String toExportString() {
        if (type == TimestampType.SUB) {
            return "\u3000\u3000" + timestamp + " " + notes;
        }
        return timestamp + " " + notes;
    }
    
    @Override
    public String toString() {
        return "TimestampEntry{" +
                "timestamp='" + timestamp + '\'' +
                ", notes='" + notes + '\'' +
                ", type=" + type +
                ", dateAdded=" + dateAdded +
                '}';
    }
}
