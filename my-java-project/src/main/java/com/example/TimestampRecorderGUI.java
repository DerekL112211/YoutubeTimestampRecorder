package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Main GUI class for the YouTube Timestamp Recorder application
 */
public class TimestampRecorderGUI {
    private JFrame frame;
    private JTextField timestampField;
    private JPanel noteInputPanel;
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
        frame.setSize(950, 700); // Increased height from 600 to 700
        frame.setMinimumSize(new Dimension(800, 600)); // Increased minimum height
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
        gbc.weighty = 0.0; // No vertical expansion for timestamp row
        panel.add(new JLabel("Timestamp (mm:ss):"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.3;
        gbc.weighty = 0.0; // No vertical expansion for timestamp row
        timestampField = new JTextField(15);
        timestampField.setText("00:00");
        timestampField.setToolTipText("Format: mm:ss or hh:mm:ss (e.g., 1:30 or 1:30:45)");
        
        // Add real-time duplicate checking
        timestampField.addCaretListener(e -> checkTimestampDuplicate());
        
        panel.add(timestampField, gbc);
        
        // Time modification buttons - positioned after Timestamp input (row 1)
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1; // Single column for label (aligned with other labels)
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(new JLabel("Time Adjust:"), gbc);
        
        // Time modification panel - aligned with other inputs
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridwidth = 2; // Span remaining columns (like other inputs)
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        JPanel timeModPanel = createTimeModificationPanel();
        panel.add(timeModPanel, gbc);
        
        // Notes section - positioned at row 2 (after Time Adjust)
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = 1; // Single column for label
        gbc.weighty = 0.0; // No vertical expansion for label
        gbc.weightx = 0.0; // No horizontal expansion for label
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Notes:"), gbc);
        
        // Dynamic note input panel
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // Give maximum vertical space to notes area
        noteInputPanel = new JPanel();
        noteInputPanel.setLayout(new BoxLayout(noteInputPanel, BoxLayout.Y_AXIS));
        
        // Add initial note field
        addNoteField();
        
        JScrollPane noteScrollPane = new JScrollPane(noteInputPanel);
        noteScrollPane.setPreferredSize(new Dimension(500, 180)); // Increased width and height
        noteScrollPane.setMinimumSize(new Dimension(450, 120)); // Set minimum size
        noteScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        noteScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(noteScrollPane, gbc);
        
        // Action buttons panel (bottom right) - positioned at row 3
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 3; // Span all columns
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.EAST; // Align to right side
        gbc.weightx = 1.0;
        gbc.weighty = 0.0; // No vertical expansion for buttons
        gbc.insets = new Insets(10, 5, 10, 5); // Consistent margins
        
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton addButton = new JButton("Add Timestamp");
        addButton.addActionListener(e -> addTimestamp());
        actionButtonPanel.add(addButton);
        
        JButton subTimestampButton = new JButton("Add Sub-timestamp");
        subTimestampButton.addActionListener(e -> addSubTimestamp());
        subTimestampButton.setToolTipText("Add an indented sub-timestamp");
        actionButtonPanel.add(subTimestampButton);
        
        panel.add(actionButtonPanel, gbc);
        
        return panel;
    }
    
    private void addNoteField() {
        JPanel noteFieldPanel = new JPanel(new BorderLayout(5, 0));
        
        JTextField noteField = new JTextField();
        noteField.setToolTipText("Enter a note description");
        noteFields.add(noteField);
        noteFieldPanel.add(noteField, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
        
        JButton addButton = new JButton("+");
        addButton.setPreferredSize(new Dimension(25, 25));
        addButton.setToolTipText("Add another note");
        addButton.addActionListener(e -> {
            addNoteField();
            refreshNotePanel();
        });
        buttonPanel.add(addButton);
        
        if (noteFields.size() > 1) {
            JButton removeButton = new JButton("-");
            removeButton.setPreferredSize(new Dimension(25, 25));
            removeButton.setToolTipText("Remove this note");
            removeButton.addActionListener(e -> {
                noteFields.remove(noteField);
                refreshNotePanel();
            });
            buttonPanel.add(removeButton);
        }
        
        noteFieldPanel.add(buttonPanel, BorderLayout.EAST);
        noteInputPanel.add(noteFieldPanel);
    }
    
    private void refreshNotePanel() {
        noteInputPanel.removeAll();
        java.util.List<String> currentValues = new java.util.ArrayList<>();
        
        // Save current values
        for (JTextField field : noteFields) {
            currentValues.add(field.getText());
        }
        
        noteFields.clear();
        
        // Recreate fields with saved values
        for (int i = 0; i < currentValues.size(); i++) {
            addNoteField();
            if (i < noteFields.size()) {
                noteFields.get(i).setText(currentValues.get(i));
            }
        }
        
        noteInputPanel.revalidate();
        noteInputPanel.repaint();
    }
    
    private String getCombinedNotes() {
        return noteFields.stream()
                .map(JTextField::getText)
                .map(String::trim)
                .filter(text -> !text.isEmpty())
                .collect(java.util.stream.Collectors.joining(" | "));
    }
    
    private void clearNoteFields() {
        for (JTextField field : noteFields) {
            field.setText("");
        }
    }
    
    private JPanel createTimeModificationPanel() {
        JPanel timeModPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Subtract time buttons (first row)
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        int[] subtractTimes = {-30, -15, -10, -5};
        JPanel subtractPanel = createTimeButtonPanel("Subtract: ", subtractTimes, new Color(255, 200, 200));
        timeModPanel.add(subtractPanel, gbc);
        
        // Add time buttons (second row)
        gbc.gridx = 0; gbc.gridy = 1;
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
        String combinedNotes = getCombinedNotes();
        
        if (timestamp.isEmpty()) {
            showError("Please enter a timestamp");
            return;
        }
        
        if (combinedNotes.isEmpty()) {
            showError("Please enter at least one note");
            return;
        }
        
        // Check for duplicate timestamp first
        if (timestampService.timestampExists(timestamp)) {
            showError("Timestamp " + timestamp + " already exists! Please use a different timestamp or delete the existing one.");
            return;
        }
        
        if (timestampService.addTimestamp(timestamp, combinedNotes)) {
            refreshTable();
            // Clear note fields, keep the timestamp for next entry
            clearNoteFields();
            showSuccess("Timestamp added successfully!");
        } else {
            showError("Invalid timestamp format. Use mm:ss or hh:mm:ss");
        }
    }
    
    private void addSubTimestamp() {
        String timestamp = timestampField.getText().trim();
        String combinedNotes = getCombinedNotes();
        
        if (timestamp.isEmpty()) {
            showError("Please enter a timestamp");
            return;
        }
        
        if (combinedNotes.isEmpty()) {
            showError("Please enter at least one note");
            return;
        }
        
        // Check for duplicate timestamp first
        if (timestampService.timestampExists(timestamp)) {
            showError("Timestamp " + timestamp + " already exists! Please use a different timestamp or delete the existing one.");
            return;
        }
        
        // Add indentation for sub-timestamp
        String indentedNote = "　　" + combinedNotes;
        
        if (timestampService.addTimestamp(timestamp, indentedNote)) {
            refreshTable();
            // Clear note fields, keep the timestamp for next entry
            clearNoteFields();
            showSuccess("Sub-timestamp added successfully!");
        } else {
            showError("Invalid timestamp format. Use mm:ss or hh:mm:ss");
        }
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
                
                // Open the file automatically
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(file);
                    }
                } catch (IOException e) {
                    System.err.println("Could not open file automatically: " + e.getMessage());
                    // Don't show error to user since the save was successful
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
        
        // Refresh duplicate checking for current timestamp field
        checkTimestampDuplicate();
    }
    
    private void showAbout() {
        String message = "Timestamp Recorder v2.1\n\n" +
                        "Features:\n" +
                        "* Record timestamps with notes\n" +
                        "* Time adjustment buttons (+/-5s, +/-10s, +/-15s, +/-30s)\n" +
                        "* Save and load timestamp collections\n" +
                        "* Export to text and CSV formats\n" +
                        "* Edit notes directly in table\n\n" +
                        "Usage:\n" +
                        "1. Add timestamps in mm:ss or hh:mm:ss format\n" +
                        "2. Use time adjustment buttons to fine-tune timestamps\n" +
                        "3. Add optional notes\n" +
                        "4. Save, load, or export your collection\n\n" +
                        "Perfect for recording important moments in videos, podcasts, or audio files!\n\n" +
                        "Developed with Java Swing";
        
        JOptionPane.showMessageDialog(frame, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Utility methods
    private void checkTimestampDuplicate() {
        String timestamp = timestampField.getText().trim();
        
        if (timestamp.isEmpty() || !timestampService.isValidTimestamp(timestamp)) {
            // Reset to normal background for invalid/empty timestamps
            timestampField.setBackground(Color.WHITE);
            timestampField.setToolTipText("Format: mm:ss or hh:mm:ss (e.g., 1:30 or 1:30:45)");
            return;
        }
        
        if (timestampService.timestampExists(timestamp)) {
            // Highlight duplicate timestamp
            timestampField.setBackground(new Color(255, 220, 220)); // Light red
            timestampField.setToolTipText("⚠️ This timestamp already exists! Use a different time or delete the existing entry.");
        } else {
            // Normal background for unique timestamps
            timestampField.setBackground(Color.WHITE);
            timestampField.setToolTipText("Format: mm:ss or hh:mm:ss (e.g., 1:30 or 1:30:45)");
        }
    }
    
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
