package com.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Data class representing a single timestamp entry
 */
public class TimestampEntry {
    private String timestamp;
    private String note;
    private String dateAdded;
    
    public TimestampEntry(String timestamp, String note, String dateAdded) {
        this.timestamp = timestamp;
        this.note = note;
        this.dateAdded = dateAdded;
    }
    
    public TimestampEntry(String timestamp, String note) {
        this.timestamp = timestamp;
        this.note = note;
        this.dateAdded = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
    // Getters
    public String getTimestamp() { return timestamp; }
    public String getNote() { return note; }
    public String getDateAdded() { return dateAdded; }
    
    // Setters
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public void setNote(String note) { this.note = note; }
    public void setDateAdded(String dateAdded) { this.dateAdded = dateAdded; }
    
    @Override
    public String toString() {
        return timestamp + "|" + note + "|" + dateAdded;
    }
    
    /**
     * Create TimestampEntry from string representation
     */
    public static TimestampEntry fromString(String str) {
        String[] parts = str.split("\\|");
        if (parts.length >= 3) {
            return new TimestampEntry(parts[0], parts[1], parts[2]);
        } else if (parts.length == 2) {
            // For backward compatibility with old format
            return new TimestampEntry(parts[0], parts[1]);
        }
        return null;
    }
    
    /**
     * Convert to array for table display
     */
    public Object[] toTableRow() {
        return new Object[]{timestamp, note, dateAdded};
    }
}
