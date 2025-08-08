package com.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for file operations (loading and saving timestamps)
 */
public class FileService {
    
    /**
     * Saves timestamps to a text file
     */
    public boolean saveTimestamps(List<TimestampEntry> timestamps, File file) {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            for (TimestampEntry entry : timestamps) {
                writer.write(entry.toExportString());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving timestamps: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Loads timestamps from a text file
     */
    public List<TimestampEntry> loadTimestamps(File file) {
        List<TimestampEntry> timestamps = new ArrayList<>();
        
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            while ((line = reader.readLine()) != null) {
                TimestampEntry entry = parseTimestampLine(line);
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
     * Parses a line from the file to create a TimestampEntry
     */
    private TimestampEntry parseTimestampLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        
        String trimmedLine = line.trim();
        TimestampType type = TimestampType.MAIN;
        
        // Check if it's a sub-timestamp (starts with full-width spaces)
        if (trimmedLine.startsWith("\u3000\u3000")) {
            type = TimestampType.SUB;
            trimmedLine = trimmedLine.substring(2).trim(); // Remove indentation
        }
        
        // Split timestamp and notes
        String[] parts = trimmedLine.split(" ", 2);
        if (parts.length >= 1) {
            String timestamp = parts[0];
            String notes = parts.length > 1 ? parts[1] : "";
            
            return new TimestampEntry(timestamp, notes, type);
        }
        
        return null;
    }
    
    /**
     * Exports timestamps to a formatted text file
     */
    public boolean exportTimestamps(List<TimestampEntry> timestamps, File file) {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            // Write header
            writer.write("Timestamp Export - Generated on " + 
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.newLine();
            writer.write("=" + "=".repeat(50));
            writer.newLine();
            writer.newLine();
            
            // Write timestamps
            for (TimestampEntry entry : timestamps) {
                writer.write(entry.toExportString());
                writer.newLine();
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting timestamps: " + e.getMessage());
            return false;
        }
    }
}
