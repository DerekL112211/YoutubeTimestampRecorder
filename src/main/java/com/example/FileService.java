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
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                System.out.println("Processing line " + lineNumber + ": '" + line + "'");
                TimestampEntry entry = parseTimestampLine(line);
                if (entry != null) {
                    timestamps.add(entry);
                    System.out.println("Successfully parsed: " + entry.getTimestamp() + " - " + entry.getNotes());
                } else {
                    System.out.println("Failed to parse line " + lineNumber);
                }
            }
            System.out.println("Total entries loaded: " + timestamps.size());
        } catch (IOException e) {
            System.err.println("Error loading timestamps: " + e.getMessage());
            e.printStackTrace();
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
        
        // Skip comment lines and header lines
        String trimmedLine = line.trim();
        if (trimmedLine.startsWith("#") || trimmedLine.startsWith("=") || 
            trimmedLine.toLowerCase().contains("timestamp export") ||
            trimmedLine.toLowerCase().contains("generated on")) {
            return null;
        }
        
        TimestampType type = TimestampType.MAIN;
        
        // Check if it's a sub-timestamp (starts with full-width spaces or regular spaces)
        if (trimmedLine.startsWith("\u3000\u3000") || trimmedLine.startsWith("    ")) {
            type = TimestampType.SUB;
            if (trimmedLine.startsWith("\u3000\u3000")) {
                trimmedLine = trimmedLine.substring(2).trim(); // Remove full-width spaces
            } else {
                trimmedLine = trimmedLine.substring(4).trim(); // Remove regular spaces
            }
        }
        
        // Split timestamp and notes - look for first space after timestamp pattern
        String[] parts = trimmedLine.split(" ", 2);
        if (parts.length >= 1) {
            String timestamp = parts[0];
            String notes = parts.length > 1 ? parts[1] : "";
            
            // Validate timestamp format (basic check)
            if (timestamp.matches("^\\d+:\\d{2}(:\\d{2})?$")) {
                return new TimestampEntry(timestamp, notes, type);
            } else {
                System.out.println("Invalid timestamp format: " + timestamp);
            }
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
            writer.write("======================================================");
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
    
    /**
     * Alias for exportTimestamps for backward compatibility
     */
    public boolean exportToText(List<TimestampEntry> timestamps, File file) {
        return exportTimestamps(timestamps, file);
    }
}
