package com.example.view;

import com.example.controller.TimestampController;
import com.example.TimestampEntry;
import com.example.view.components.UIComponents;
import com.example.view.components.TimeModificationPanel;
import com.example.view.components.DynamicNotesPanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Swing implementation of the TimestampView interface
 * Contains all GUI components and user interaction handling
 */
public class SwingTimestampView implements TimestampView {
    private TimestampController controller;
    private JFrame frame;
    private JTextField timestampField;
    private DynamicNotesPanel notesPanel;
    private JTable timestampTable;
    private DefaultTableModel tableModel;
    
    public SwingTimestampView() {
        initializeGUI();
    }
    
    @Override
    public void setController(TimestampController controller) {
        this.controller = controller;
        // Initialize table with current data
        refreshTable(controller.getTimestamps());
    }
    
    private void initializeGUI() {
        createMainFrame();
        createComponents();
        layoutComponents();
        setupEventHandlers();
    }
    
    private void createMainFrame() {
        frame = new JFrame("Timestamp Recorder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(950, 600);
        frame.setMinimumSize(new Dimension(800, 500));
        frame.setLocationRelativeTo(null);
    }
    
    private void createComponents() {
        // Create main panels
        JPanel mainPanel = UIComponents.createBorderPanel();
        
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
    }
    
    private void layoutComponents() {
        // Components are already laid out in createComponents()
    }
    
    private void setupEventHandlers() {
        // Event handlers are set up in individual component creation methods
    }
    
    private JPanel createInputPanel() {
        JPanel panel = UIComponents.createTitledPanel("Add Timestamp", new GridBagLayout());
        GridBagConstraints gbc;
        
        // Timestamp input label
        gbc = UIComponents.createGridBagConstraints(0, 0, 1, GridBagConstraints.NONE, 0, 0, 
                                                   GridBagConstraints.CENTER, UIComponents.MEDIUM_INSETS);
        panel.add(UIComponents.createLabel("Timestamp (mm:ss):"), gbc);
        
        // Timestamp input box
        gbc = UIComponents.createGridBagConstraints(1, 0, 2, GridBagConstraints.HORIZONTAL, 1.0, 0, 
                                                   GridBagConstraints.CENTER, UIComponents.MEDIUM_INSETS);
        timestampField = UIComponents.createTextField(0, "Format: mm:ss or hh:mm:ss (e.g., 01:30 or 1:30:45)", "00:00");
        panel.add(timestampField, gbc);
        
        // Notes section
        gbc = UIComponents.createGridBagConstraints(0, 1, 2, GridBagConstraints.NONE, 0, 0, 
                                                   GridBagConstraints.WEST, UIComponents.MEDIUM_INSETS);
        panel.add(UIComponents.createLabel("Notes:"), gbc);
        
        // Create dynamic notes panel with window resize callback
        notesPanel = new DynamicNotesPanel(this::resizeWindow);
        
        gbc = UIComponents.createGridBagConstraints(1, 1, 1, GridBagConstraints.BOTH, 1.0, 1.0, 
                                                   GridBagConstraints.CENTER, UIComponents.MEDIUM_INSETS);
        panel.add(notesPanel, gbc);
        
        // Time modification buttons
        gbc = UIComponents.createGridBagConstraints(0, 2, 2, GridBagConstraints.BOTH, 0.8, 0, 
                                                   GridBagConstraints.CENTER, new Insets(10, 5, 5, 5));
        TimeModificationPanel timeModPanel = new TimeModificationPanel(this::modifyTimestamp);
        panel.add(timeModPanel, gbc);
        
        // Add buttons panel in right bottom corner
        gbc = UIComponents.createGridBagConstraints(2, 2, 1, GridBagConstraints.NONE, 0.2, 0, 
                                                   GridBagConstraints.SOUTHEAST, new Insets(10, 10, 5, 5));
        
        JPanel buttonPanel = UIComponents.createFlowPanel(FlowLayout.RIGHT, 5, 0);
        JButton addButton = UIComponents.createActionButton("Add Timestamp", e -> addTimestamp());
        buttonPanel.add(addButton);
        
        JButton addSubButton = UIComponents.createActionButton("Add Sub-timestamp", e -> addSubTimestamp());
        buttonPanel.add(addSubButton);
        
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private void resizeWindow() {
        if (frame != null) {
            frame.pack();
            Dimension currentSize = frame.getSize();
            Dimension minSize = frame.getMinimumSize();
            if (currentSize.width < minSize.width || currentSize.height < minSize.height) {
                frame.setSize(Math.max(currentSize.width, minSize.width), 
                             Math.max(currentSize.height, minSize.height));
            }
        }
    }
    
    private JPanel createListPanel() {
        JPanel panel = UIComponents.createTitledPanel("Recorded Timestamps", new BorderLayout());
        
        // Create table
        String[] columnNames = {"Time", "Note", "Date Added"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; // Only note column is editable
            }
        };
        
        timestampTable = new JTable(tableModel);
        
        // Apply custom styling to the table
        UIComponents.styleTable(timestampTable);
        
        timestampTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        timestampTable.getColumnModel().getColumn(1).setPreferredWidth(400);
        timestampTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        // Add table listener for note editing
        tableModel.addTableModelListener(e -> {
            if (e.getColumn() == 1) { // Note column
                int row = e.getFirstRow();
                String newNote = (String) tableModel.getValueAt(row, 1);
                if (controller != null) {
                    controller.updateNote(row, newNote);
                }
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
        JPanel buttonPanel = UIComponents.createFlowPanel(FlowLayout.CENTER, 5, 5);
        
        JButton deleteButton = UIComponents.createActionButton("Delete Selected", e -> deleteSelected());
        buttonPanel.add(deleteButton);
        
        JButton clearButton = UIComponents.createActionButton("Clear All", e -> clearAll());
        buttonPanel.add(clearButton);
        
        JButton saveButton = UIComponents.createActionButton("Save as Text", e -> exportToText());
        buttonPanel.add(saveButton);
        
        JButton refreshButton = UIComponents.createActionButton("Refresh Table", e -> refreshTable());
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
    
    // Event handlers that delegate to controller
    private void addTimestamp() {
        if (controller != null) {
            controller.addTimestamp(timestampField.getText(), collectNotes());
        }
    }
    
    private void addSubTimestamp() {
        if (controller != null) {
            controller.addSubTimestamp(timestampField.getText(), collectNotes());
        }
    }
    
    private void modifyTimestamp(int seconds) {
        if (controller != null) {
            controller.modifyTimestamp(timestampField.getText(), seconds);
        }
    }
    
    private void deleteSelected() {
        if (controller != null) {
            controller.deleteSelected(timestampTable.getSelectedRow());
        }
    }
    
    private void clearAll() {
        if (controller != null) {
            controller.clearAll();
        }
    }
    
    private void loadTimestamps() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            controller.loadTimestamps(fileChooser.getSelectedFile());
        }
    }
    
    private void exportToText() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("timestamps_export.txt"));
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            controller.exportToText(fileChooser.getSelectedFile());
        }
    }
    
    private void refreshTable() {
        if (controller != null) {
            refreshTable(controller.getTimestamps());
        }
    }
    
    private List<String> collectNotes() {
        return notesPanel.collectNotes();
    }
    
    private void showAbout() {
        String message = "Timestamp Recorder v3.0 (MVC Architecture)\n\n" +
                        "Features:\n" +
                        "* Record timestamps with notes\n" +
                        "* Add sub-timestamps (indented hierarchical structure)\n" +
                        "* Dynamic multiple notes system with +/- buttons\n" +
                        "* Time adjustment buttons (+/-5s, +/-10s, +/-15s, +/-30s)\n" +
                        "* Save and load timestamp collections\n" +
                        "* Export to text format\n" +
                        "* Edit notes directly in table\n\n" +
                        "Architecture:\n" +
                        "* Model-View-Controller (MVC) design pattern\n" +
                        "* Separated business logic, presentation, and user interaction\n" +
                        "* Observer pattern for data synchronization\n\n" +
                        "Usage:\n" +
                        "1. Add timestamps in mm:ss or hh:mm:ss format\n" +
                        "2. Use 'Add Timestamp' for main entries\n" +
                        "3. Use 'Add Sub-timestamp' for indented sub-entries\n" +
                        "4. Use +/- buttons to add multiple notes per timestamp\n" +
                        "5. Use time adjustment buttons to fine-tune timestamps\n" +
                        "6. Save, load, or export your collection\n\n" +
                        "Perfect for creating hierarchical timestamps for videos, podcasts, or audio files!\n\n" +
                        "Developed with Java Swing using MVC Architecture";
        
        JOptionPane.showMessageDialog(frame, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // TimestampView interface implementation
    @Override
    public void refreshTable(List<TimestampEntry> timestamps) {
        tableModel.setRowCount(0);
        for (TimestampEntry entry : timestamps) {
            tableModel.addRow(entry.toTableRow());
        }
    }
    
    @Override
    public void clearAllNoteFields() {
        notesPanel.clearAllNotes();
    }
    
    @Override
    public void setTimestampField(String timestamp) {
        timestampField.setText(timestamp);
    }
    
    @Override
    public void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    @Override
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(frame, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public boolean confirmClearAll() {
        int confirm = JOptionPane.showConfirmDialog(
            frame, 
            "Are you sure you want to clear all timestamps?", 
            "Confirm Clear All", 
            JOptionPane.YES_NO_OPTION);
        return confirm == JOptionPane.YES_OPTION;
    }
    
    @Override
    public void show() {
        frame.setVisible(true);
    }
    
    @Override
    public void hide() {
        frame.setVisible(false);
        frame.dispose();
    }
}
