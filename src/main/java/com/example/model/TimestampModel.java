package com.example.model;

import com.example.TimestampEntry;
import com.example.TimestampType;
import com.example.TimestampService;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Model class for managing timestamp data and business logic
 * Extends Observable to notify views of data changes
 */
@SuppressWarnings("deprecation")
public class TimestampModel extends Observable {
    private List<TimestampEntry> timestamps;
    private TimestampService timestampService;
    
    public TimestampModel() {
        this.timestamps = new ArrayList<>();
        this.timestampService = new TimestampService();
    }
    
    /**
     * Add a new timestamp entry
     */
    public boolean addTimestamp(String timestamp, String notes, TimestampType type) {
        boolean success = timestampService.addTimestamp(timestamp, notes, type);
        if (success) {
            this.timestamps = timestampService.getTimestamps();
            setChanged();
            notifyObservers("TIMESTAMP_ADDED");
        }
        return success;
    }
    
    /**
     * Remove a timestamp at the specified index
     */
    public boolean removeTimestamp(int index) {
        boolean success = timestampService.removeTimestamp(index);
        if (success) {
            this.timestamps = timestampService.getTimestamps();
            setChanged();
            notifyObservers("TIMESTAMP_REMOVED");
        }
        return success;
    }
    
    /**
     * Update the note of a timestamp at the specified index
     */
    public void updateNote(int index, String newNote) {
        timestampService.updateNote(index, newNote);
        this.timestamps = timestampService.getTimestamps();
        setChanged();
        notifyObservers("NOTE_UPDATED");
    }
    
    /**
     * Clear all timestamps
     */
    public void clearAll() {
        timestampService.clearAll();
        this.timestamps = timestampService.getTimestamps();
        setChanged();
        notifyObservers("ALL_CLEARED");
    }
    
    /**
     * Get all timestamps (sorted chronologically)
     */
    public List<TimestampEntry> getTimestamps() {
        List<TimestampEntry> sortedTimestamps = timestampService.getTimestamps();
        sortedTimestamps.sort((t1, t2) -> {
            int seconds1 = timestampService.parseTimestampToSeconds(t1.getTimestamp());
            int seconds2 = timestampService.parseTimestampToSeconds(t2.getTimestamp());
            return Integer.compare(seconds1, seconds2);
        });
        return sortedTimestamps;
    }
    
    /**
     * Set timestamps (for loading from file)
     */
    public void setTimestamps(List<TimestampEntry> timestamps) {
        timestampService.setTimestamps(timestamps);
        this.timestamps = timestampService.getTimestamps();
        setChanged();
        notifyObservers("TIMESTAMPS_LOADED");
    }
    
    /**
     * Modify a timestamp by adding/subtracting seconds
     */
    public String modifyTimestamp(String timestamp, int seconds) {
        return timestampService.addSecondsToTimestamp(timestamp, seconds);
    }
    
    /**
     * Parse timestamp to seconds for validation
     */
    public int parseTimestampToSeconds(String timestamp) {
        return timestampService.parseTimestampToSeconds(timestamp);
    }
    
    /**
     * Get the underlying TimestampService for file operations
     */
    public TimestampService getTimestampService() {
        return timestampService;
    }
}
