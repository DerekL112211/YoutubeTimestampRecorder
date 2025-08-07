package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * Main GUI class for the YouTube Timestamp Recorder application
 */
public class TimestampRecorderGUI {
    private JFrame frame;
    private JTextField timestampField;
    private JPanel notesPanel;
    private java.util.List<JTextField> noteFields;
    private JTable timestampTable;
    private DefaultTableModel tableModel;
    
    // Services
    private TimestampService timestampService;
    private FileService fileService;
    
    public TimestampRecorderGUI() {
        this.timestampService = new TimestampService();
        this.fileService = new FileService();
        this.noteFields = new java.util.ArrayList<>();
    }
    
    public void createAndShowGUI() {
        // Create the main frame
        frame = new JFrame("Timestamp Recorder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(950, 600);
        frame.setMinimumSize(new Dimension(800, 500));
        frame.setLocationRelativeTo(null);
        
        // Create main panels
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Center panel - Timestamp input
        JPanel inputPanel = createInputPanel();
        
        // Bottom panel - Timestamp list
        JPanel listPanel = createListPanel();
        
        // Menu bar
        JMenuBar menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);
        
        // Layout
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(listPanel, BorderLayout.SOUTH);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Add Timestamp"));
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Timestamp input
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(new JLabel("Timestamp (mm:ss):"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.3;
        timestampField = new JTextField(15);
        timestampField.setText("00:00");
        timestampField.setToolTipText("Format: mm:ss or hh:mm:ss (e.g., 01:30 or 1:30:45)");
        panel.add(timestampField, gbc);
        
        // Notes section
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(5, 15, 5, 5);
        panel.add(new JLabel("Notes:"), gbc);
        
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.6;
        notesPanel = createNotesPanel();
        panel.add(notesPanel, gbc);
        
        // Add button
        gbc.gridx = 4; gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(5, 10, 5, 5);
        JButton addButton = new JButton("Add Timestamp");
        addButton.addActionListener(e -> addTimestamp());
        panel.add(addButton, gbc);
        
        // Add Sub-timestamp button
        gbc.gridx = 5; gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        JButton addSubButton = new JButton("Add Sub-timestamp");
        addSubButton.addActionListener(e -> addSubTimestamp());
        panel.add(addSubButton, gbc);
        
        // Time modification buttons - on second row
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 5, 5, 5);
        JPanel timeModPanel = createTimeModificationPanel();
        panel.add(timeModPanel, gbc);
        
        return panel;
    }
    
    private JPanel createNotesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Set notesPanel reference first
        notesPanel = panel;
        
        // Add the first note field with + button
        addNoteField();
        
        return panel;
    }
    
    private void addNoteField() {
        JPanel noteRowPanel = new JPanel(new BorderLayout(5, 0));
        
        JTextField noteField = new JTextField(25);
        noteField.setToolTipText("Optional description for this timestamp");
        noteFields.add(noteField);
        
        noteRowPanel.add(noteField, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        
        // Always show + button
        JButton addButton = new JButton("+");
        addButton.setPreferredSize(new Dimension(30, 25));
        addButton.setToolTipText("Add another note");
        addButton.addActionListener(e -> {
            addNoteField();
            refreshNotesPanel();
        });
        buttonPanel.add(addButton);
        
        // Show - button only if there's more than one note field
        if (noteFields.size() > 1) {
            JButton removeButton = new JButton("-");
            removeButton.setPreferredSize(new Dimension(30, 25));
            removeButton.setToolTipText("Remove this note");
            removeButton.addActionListener(e -> {
                removeNoteField(noteField);
                refreshNotesPanel();
            });
            buttonPanel.add(removeButton);
        }
        
        noteRowPanel.add(buttonPanel, BorderLayout.EAST);
        
        notesPanel.add(noteRowPanel);
        notesPanel.add(Box.createVerticalStrut(3)); // Small spacing between rows
    }
    
    private void removeNoteField(JTextField fieldToRemove) {
        noteFields.remove(fieldToRemove);
    }
    
    private void refreshNotesPanel() {
        notesPanel.removeAll();
        
        // Rebuild the notes panel with current fields
        for (int i = 0; i < noteFields.size(); i++) {
            JTextField noteField = noteFields.get(i);
            JPanel noteRowPanel = new JPanel(new BorderLayout(5, 0));
            
            noteRowPanel.add(noteField, BorderLayout.CENTER);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
            
            // Always show + button
            JButton addButton = new JButton("+");
            addButton.setPreferredSize(new Dimension(30, 25));
            addButton.setToolTipText("Add another note");
            addButton.addActionListener(e -> {
                addNoteField();
                refreshNotesPanel();
            });
            buttonPanel.add(addButton);
            
            // Show - button only if there's more than one note field
            if (noteFields.size() > 1) {
                JButton removeButton = new JButton("-");
                removeButton.setPreferredSize(new Dimension(30, 25));
                removeButton.setToolTipText("Remove this note");
                JTextField currentField = noteField; // Capture for lambda
                removeButton.addActionListener(e -> {
                    removeNoteField(currentField);
                    refreshNotesPanel();
                });
                buttonPanel.add(removeButton);
            }
            
            noteRowPanel.add(buttonPanel, BorderLayout.EAST);
            
            notesPanel.add(noteRowPanel);
            if (i < noteFields.size() - 1) {
                notesPanel.add(Box.createVerticalStrut(3)); // Small spacing between rows
            }
        }
        
        notesPanel.revalidate();
        notesPanel.repaint();
    }
    
    private JPanel createTimeModificationPanel() {
        JPanel timeModPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Label
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        timeModPanel.add(new JLabel("Time Adjust:"), gbc);
        
        // Subtract time buttons (first row)
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        int[] subtractTimes = {-30, -15, -10, -5};
        JPanel subtractPanel = createTimeButtonPanel("Subtract: ", subtractTimes, new Color(255, 200, 200));
        timeModPanel.add(subtractPanel, gbc);
        
        // Add time buttons (second row)
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        int[] addTimes = {5, 10, 15, 30};
        JPanel addPanel = createTimeButtonPanel("Add:         ", addTimes, new Color(200, 255, 200));
        timeModPanel.add(addPanel, gbc);
        
        return timeModPanel;
    }
    
    /**
     * Helper method to create a panel with time adjustment buttons
     */
    private JPanel createTimeButtonPanel(String labelText, int[] times, Color backgroundColor) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.add(new JLabel(labelText));
        
        for (int seconds : times) {
            JButton btn = new JButton((seconds > 0 ? "+" : "") + seconds + "s");
            btn.addActionListener(e -> modifyTimestamp(seconds));
            btn.setPreferredSize(new Dimension(80, 30));
            btn.setBackground(backgroundColor);
            btn.setToolTipText((seconds > 0 ? "Add " : "Subtract ") + Math.abs(seconds) + " seconds");
            panel.add(btn);
        }
        
        return panel;
    }
    
    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Recorded Timestamps"));
        
        // Create table
        String[] columnNames = {"Time", "Note", "Date Added"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; // Only note column is editable
            }
        };
        
        timestampTable = new JTable(tableModel);
        timestampTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        timestampTable.getColumnModel().getColumn(1).setPreferredWidth(400);
        timestampTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        // Add table listener for note editing
        tableModel.addTableModelListener(e -> {
            if (e.getColumn() == 1) { // Note column
                int row = e.getFirstRow();
                String newNote = (String) tableModel.getValueAt(row, 1);
                timestampService.updateNote(row, newNote);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(timestampTable);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Action buttons
        JPanel buttonPanel = createActionButtonPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createActionButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteSelected());
        buttonPanel.add(deleteButton);
        
        JButton clearButton = new JButton("Clear All");
        clearButton.addActionListener(e -> clearAll());
        buttonPanel.add(clearButton);
        
        JButton saveButton = new JButton("Save as Text");
        saveButton.addActionListener(e -> exportToText());
        buttonPanel.add(saveButton);
        
        JButton refreshButton = new JButton("Refresh Table");
        refreshButton.addActionListener(e -> refreshTable());
        buttonPanel.add(refreshButton);
        
        return buttonPanel;
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem loadItem = new JMenuItem("Load Timestamps...");
        loadItem.addActionListener(e -> loadTimestamps());
        fileMenu.add(loadItem);
        
        menuBar.add(fileMenu);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);
        
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    // Event handlers
    private void modifyTimestamp(int seconds) {
        String currentTimestamp = timestampField.getText().trim();
        if (currentTimestamp.isEmpty()) {
            showError("Please enter a timestamp first");
            return;
        }
        
        String modifiedTimestamp = timestampService.addSecondsToTimestamp(currentTimestamp, seconds);
        timestampField.setText(modifiedTimestamp);
    }
    
    private void addTimestamp() {
        String timestamp = timestampField.getText().trim();
        
        if (timestamp.isEmpty()) {
            showError("Please enter a timestamp");
            return;
        }
        
        // Collect all non-empty notes
        StringBuilder combinedNotes = new StringBuilder();
        for (int i = 0; i < noteFields.size(); i++) {
            String note = noteFields.get(i).getText().trim();
            if (!note.isEmpty()) {
                if (combinedNotes.length() > 0) {
                    combinedNotes.append(" | "); // Use pipe separator
                }
                combinedNotes.append(note);
            }
        }
        
        if (timestampService.addTimestamp(timestamp, combinedNotes.toString(), TimestampType.MAIN)) {
            refreshTable();
            // Clear all note fields after successful addition
            clearAllNoteFields();
            showSuccess("Timestamp added successfully!");
        } else {
            showError("Invalid timestamp format or duplicate timestamp. Use mm:ss or hh:mm:ss");
        }
    }
    
    private void addSubTimestamp() {
        String timestamp = timestampField.getText().trim();
        
        if (timestamp.isEmpty()) {
            showError("Please enter a timestamp");
            return;
        }
        
        // Collect all non-empty notes
        StringBuilder combinedNotes = new StringBuilder();
        for (int i = 0; i < noteFields.size(); i++) {
            String note = noteFields.get(i).getText().trim();
            if (!note.isEmpty()) {
                if (combinedNotes.length() > 0) {
                    combinedNotes.append(" | "); // Use pipe separator
                }
                combinedNotes.append(note);
            }
        }
        
        if (timestampService.addTimestamp(timestamp, combinedNotes.toString(), TimestampType.SUB)) {
            refreshTable();
            // Clear all note fields after successful addition
            clearAllNoteFields();
            showSuccess("Sub-timestamp added successfully!");
        } else {
            showError("Invalid timestamp format or duplicate timestamp. Use mm:ss or hh:mm:ss");
        }
    }
    
    private void clearAllNoteFields() {
        // Clear all existing note fields
        for (JTextField noteField : noteFields) {
            noteField.setText("");
        }
        
        // Reset to single note field
        noteFields.clear();
        notesPanel.removeAll();
        addNoteField();
        notesPanel.revalidate();
        notesPanel.repaint();
    }
    
    private void deleteSelected() {
        int selectedRow = timestampTable.getSelectedRow();
        if (selectedRow >= 0) {
            if (timestampService.removeTimestamp(selectedRow)) {
                refreshTable();
                showSuccess("Timestamp deleted successfully!");
            }
        } else {
            showError("Please select a timestamp to delete");
        }
    }
    
    private void clearAll() {
        int confirm = JOptionPane.showConfirmDialog(
            frame, 
            "Are you sure you want to clear all timestamps?", 
            "Confirm Clear All", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            timestampService.clearAll();
            refreshTable();
            showSuccess("All timestamps cleared!");
        }
    }
    
    private void loadTimestamps() {
        JFileChooser fileChooser = new JFileChooser();
        
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            List<TimestampEntry> loadedTimestamps = fileService.loadTimestamps(file);
            
            if (!loadedTimestamps.isEmpty()) {
                timestampService.setTimestamps(loadedTimestamps);
                refreshTable();
                showSuccess("Timestamps loaded successfully! (" + loadedTimestamps.size() + " entries)");
            } else {
                showError("No valid timestamps found in file");
            }
        }
    }
    
    private void exportToText() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("timestamps_export.txt"));
        
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            // Get timestamps and sort them chronologically before exporting
            List<TimestampEntry> sortedTimestamps = timestampService.getTimestamps();
            sortedTimestamps.sort((t1, t2) -> {
                int seconds1 = timestampService.parseTimestampToSeconds(t1.getTimestamp());
                int seconds2 = timestampService.parseTimestampToSeconds(t2.getTimestamp());
                return Integer.compare(seconds1, seconds2);
            });
            
            if (fileService.exportToText(sortedTimestamps, file)) {
                showSuccess("Timestamps saved successfully!");
                
                // Auto-open the saved file with default application
                try {
                    if (java.awt.Desktop.isDesktopSupported()) {
                        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                        if (desktop.isSupported(java.awt.Desktop.Action.OPEN)) {
                            desktop.open(file);
                        }
                    }
                } catch (Exception e) {
                    // If auto-open fails, just show a message (don't interrupt the save success)
                    showError("File saved but could not auto-open: " + e.getMessage());
                }
            } else {
                showError("Error saving timestamps to file");
            }
        }
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<TimestampEntry> sortedTimestamps = timestampService.getTimestamps();
        // Sort timestamps chronologically
        sortedTimestamps.sort((t1, t2) -> {
            int seconds1 = timestampService.parseTimestampToSeconds(t1.getTimestamp());
            int seconds2 = timestampService.parseTimestampToSeconds(t2.getTimestamp());
            return Integer.compare(seconds1, seconds2);
        });
        
        for (TimestampEntry entry : sortedTimestamps) {
            tableModel.addRow(entry.toTableRow());
        }
    }
    
    private void showAbout() {
        String message = "Timestamp Recorder v2.2\n\n" +
                        "Features:\n" +
                        "* Record timestamps with notes\n" +
                        "* Add sub-timestamps (indented hierarchical structure)\n" +
                        "* Dynamic multiple notes system with +/- buttons\n" +
                        "* Time adjustment buttons (+/-5s, +/-10s, +/-15s, +/-30s)\n" +
                        "* Save and load timestamp collections\n" +
                        "* Export to text and CSV formats\n" +
                        "* Edit notes directly in table\n\n" +
                        "Usage:\n" +
                        "1. Add timestamps in mm:ss or hh:mm:ss format\n" +
                        "2. Use 'Add Timestamp' for main entries\n" +
                        "3. Use 'Add Sub-timestamp' for indented sub-entries\n" +
                        "4. Use +/- buttons to add multiple notes per timestamp\n" +
                        "5. Use time adjustment buttons to fine-tune timestamps\n" +
                        "6. Save, load, or export your collection\n\n" +
                        "Perfect for creating hierarchical timestamps for videos, podcasts, or audio files!\n\n" +
                        "Developed with Java Swing";
        
        JOptionPane.showMessageDialog(frame, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Utility methods
    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(frame, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void copyToClipboard(String text) {
        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
            .setContents(new java.awt.datatransfer.StringSelection(text), null);
    }
}
