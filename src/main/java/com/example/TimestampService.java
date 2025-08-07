package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service class for handling timestamp operations
 */
public class TimestampService {
    private static final Pattern TIMESTAMP_PATTERN = Pattern.compile("^([0-9]+:)?[0-5]?[0-9]:[0-5][0-9]$");
    
    private List<TimestampEntry> timestamps;
    
    public TimestampService() {
        this.timestamps = new ArrayList<>();
    }
    
    /**
     * Validate timestamp format (mm:ss or hh:mm:ss)
     */
    public boolean isValidTimestamp(String timestamp) {
        return timestamp != null && TIMESTAMP_PATTERN.matcher(timestamp).matches();
    }
    
    /**
     * Convert timestamp string to seconds
     */
    public int parseTimestampToSeconds(String timestamp) {
        String[] parts = timestamp.split(":");
        int seconds = 0;
        
        if (parts.length == 2) { // mm:ss
            seconds = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        } else if (parts.length == 3) { // hh:mm:ss
            seconds = Integer.parseInt(parts[0]) * 3600 + Integer.parseInt(parts[1]) * 60 + Integer.parseInt(parts[2]);
        }
        
        return seconds;
    }
    
    /**
     * Add seconds to a timestamp string
     */
    public String addSecondsToTimestamp(String timestamp, int seconds) {
        if (!isValidTimestamp(timestamp)) {
            return timestamp;
        }
        
        int totalSeconds = parseTimestampToSeconds(timestamp);
        totalSeconds += seconds;
        
        // Ensure non-negative
        if (totalSeconds < 0) {
            totalSeconds = 0;
        }
        
        return formatSecondsToTimestamp(totalSeconds);
    }
    
    /**
     * Convert seconds back to timestamp format
     */
    public String formatSecondsToTimestamp(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%d:%02d", minutes, seconds);
        }
    }
    /**
     * Add a new timestamp entry
     */
    public boolean addTimestamp(String timestamp, String note) {
        if (!isValidTimestamp(timestamp)) {
            return false;
        }
        
        TimestampEntry entry = new TimestampEntry(timestamp, note);
        timestamps.add(entry);
        return true;
    }
    
    /**
     * Remove timestamp at specific index
     */
    public boolean removeTimestamp(int index) {
        if (index >= 0 && index < timestamps.size()) {
            timestamps.remove(index);
            return true;
        }
        return false;
    }
    
    /**
     * Clear all timestamps
     */
    public void clearAll() {
        timestamps.clear();
    }
    
    /**
     * Get all timestamps
     */
    public List<TimestampEntry> getTimestamps() {
        return new ArrayList<>(timestamps);
    }
    
    /**
     * Get timestamp at specific index
     */
    public TimestampEntry getTimestamp(int index) {
        if (index >= 0 && index < timestamps.size()) {
            return timestamps.get(index);
        }
        return null;
    }
    
    /**
     * Get total number of timestamps
     */
    public int getTimestampCount() {
        return timestamps.size();
    }
    
    /**
     * Update note for timestamp at specific index
     */
    public boolean updateNote(int index, String newNote) {
        TimestampEntry entry = getTimestamp(index);
        if (entry != null) {
            entry.setNote(newNote);
            return true;
        }
        return false;
    }
    
    /**
     * Set timestamps from external list (for loading from file)
     */
    public void setTimestamps(List<TimestampEntry> timestamps) {
        this.timestamps = new ArrayList<>(timestamps);
    }
}
