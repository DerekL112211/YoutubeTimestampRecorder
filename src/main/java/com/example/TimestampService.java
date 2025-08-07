package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service class for handling timestamp operations
 */
public class TimestampService {
    private static final Pattern TIMESTAMP_PATTERN = Pattern.compile("^([0-9]+:)?[0-9]{1,2}:[0-5][0-9]$");
    
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
     * Convert seconds back to timestamp format with leading zeros
     */
    public String formatSecondsToTimestamp(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
    /**
     * Add a new timestamp entry
     */
    public boolean addTimestamp(String timestamp, String note) {
        return addTimestamp(timestamp, note, TimestampType.MAIN);
    }
    
    /**
     * Add a new timestamp entry with specified type
     */
    public boolean addTimestamp(String timestamp, String note, TimestampType type) {
        if (!isValidTimestamp(timestamp)) {
            return false;
        }
        
        // Check for duplicate timestamp
        if (timestampExists(timestamp)) {
            return false; // Timestamp already exists
        }
        
        TimestampEntry entry = new TimestampEntry(timestamp, note, type);
        timestamps.add(entry);
        
        // Sort timestamps by time after adding
        sortTimestamps();
        
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
        // Sort timestamps after loading
        sortTimestamps();
    }
    
    /**
     * Check if timestamp already exists
     */
    public boolean timestampExists(String timestamp) {
        if (!isValidTimestamp(timestamp)) {
            return false;
        }
        
        return timestamps.stream()
                .anyMatch(entry -> entry.getTimestamp().equals(timestamp));
    }
    
    /**
     * Sort timestamps chronologically by time value
     */
    private void sortTimestamps() {
        timestamps.sort((t1, t2) -> {
            int seconds1 = parseTimestampToSeconds(t1.getTimestamp());
            int seconds2 = parseTimestampToSeconds(t2.getTimestamp());
            return Integer.compare(seconds1, seconds2);
        });
    }
}
