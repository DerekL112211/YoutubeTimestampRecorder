package com.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Data class representing a single timestamp entry with hierarchical support
 */
public class TimestampEntry {
    private String timestamp;
    private String note;
    private String dateAdded;
    private TimestampType type;
    
    public TimestampEntry(String timestamp, String note, String dateAdded, TimestampType type) {
        this.timestamp = timestamp;
        this.note = note;
        this.dateAdded = dateAdded;
        this.type = type != null ? type : TimestampType.MAIN;
    }
    
    public TimestampEntry(String timestamp, String note, TimestampType type) {
        this.timestamp = timestamp;
        this.note = note;
        this.dateAdded = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.type = type != null ? type : TimestampType.MAIN;
    }
    
    // Backward compatibility constructors
    public TimestampEntry(String timestamp, String note, String dateAdded) {
        this(timestamp, note, dateAdded, TimestampType.MAIN);
    }
    
    public TimestampEntry(String timestamp, String note) {
        this(timestamp, note, TimestampType.MAIN);
    }
    
    // Getters
    public String getTimestamp() { return timestamp; }
    public String getNote() { return note; }
    public String getDateAdded() { return dateAdded; }
    public TimestampType getType() { return type; }
    
    /**
     * Get formatted note with proper indentation based on type
     */
    public String getFormattedNote() {
        return type.formatNote(note);
    }
    
    // Setters
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public void setNote(String note) { this.note = note; }
    public void setDateAdded(String dateAdded) { this.dateAdded = dateAdded; }
    public void setType(TimestampType type) { this.type = type != null ? type : TimestampType.MAIN; }
    
    @Override
    public String toString() {
        return timestamp + "|" + note + "|" + dateAdded + "|" + type.name();
    }
    
    /**
     * Create TimestampEntry from string representation
     */
    public static TimestampEntry fromString(String str) {
        String[] parts = str.split("\\|");
        if (parts.length >= 4) {
            TimestampType type = TimestampType.valueOf(parts[3]);
            return new TimestampEntry(parts[0], parts[1], parts[2], type);
        } else if (parts.length >= 3) {
            return new TimestampEntry(parts[0], parts[1], parts[2], TimestampType.MAIN);
        } else if (parts.length == 2) {
            // For backward compatibility with old format
            return new TimestampEntry(parts[0], parts[1], TimestampType.MAIN);
        }
        return null;
    }
    
    /**
     * Convert to array for table display with formatted note
     */
    public Object[] toTableRow() {
        return new Object[]{timestamp, getFormattedNote(), dateAdded};
    }
}
