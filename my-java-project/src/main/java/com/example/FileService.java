package com.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling file operations (save/load/export)
 */
public class FileService {
    
    /**
     * Save timestamps to file
     */
    public boolean saveTimestamps(List<TimestampEntry> timestamps, File file) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Write header
            writer.println("# Timestamp Recorder Data");
            writer.println("# Format: timestamp|note|dateAdded");
            writer.println("# Generated on: " + java.time.LocalDateTime.now());
            writer.println();
            
            // Write timestamps
            for (TimestampEntry entry : timestamps) {
                writer.println(entry.toString());
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving timestamps: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Load timestamps from file
     */
    public List<TimestampEntry> loadTimestamps(File file) {
        List<TimestampEntry> timestamps = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip comments and empty lines
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                TimestampEntry entry = TimestampEntry.fromString(line);
                if (entry != null) {
                    timestamps.add(entry);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading timestamps: " + e.getMessage());
        }
        
        return timestamps;
    }
    
    /**
     * Export timestamps to formatted text file
     */
    public boolean exportToText(List<TimestampEntry> timestamps, File file) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            if (timestamps.isEmpty()) {
                writer.println("No timestamps recorded.");
                return true;
            }
            
            for (TimestampEntry entry : timestamps) {
                String note = entry.getNote();
                if (note == null) {
                    note = "";
                }
                
                String timestamp = entry.getTimestamp();
                String indentation = "";
                
                // Check if note starts with indentation markers
                if (note.startsWith("　　") || note.startsWith("  ")) {
                    indentation = "　　";
                    // Remove indentation from note if it's at the beginning
                    if (note.startsWith("　　")) {
                        note = note.substring(2);
                    } else if (note.startsWith("  ")) {
                        note = note.substring(2);
                    }
                }
                
                // Format: [indentation][timestamp]　[note]
                if (note.trim().isEmpty()) {
                    writer.printf("%s%-8s%n", indentation, timestamp);
                } else {
                    writer.printf("%s%-8s　%s%n", indentation, timestamp, note.trim());
                }
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting timestamps: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Export timestamps to CSV format
     */
    public boolean exportToCSV(List<TimestampEntry> timestamps, File file) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Write CSV header
            writer.println("Timestamp,Note,Date Added");
            
            // Write timestamps
            for (TimestampEntry entry : timestamps) {
                writer.printf("\"%s\",\"%s\",\"%s\"%n",
                    escapeCSV(entry.getTimestamp()),
                    escapeCSV(entry.getNote()),
                    escapeCSV(entry.getDateAdded()));
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting to CSV: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Escape special characters for CSV format
     */
    private String escapeCSV(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"");
    }
}
