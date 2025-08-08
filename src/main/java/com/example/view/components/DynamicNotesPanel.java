package com.example.view.components;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic notes panel that manages multiple note input fields
 * Handles adding/removing note fields with proper UI updates
 */
public class DynamicNotesPanel extends JPanel {
    
    private final List<JTextField> noteFields;
    private final Runnable layoutUpdateCallback;
    
    public DynamicNotesPanel(Runnable layoutUpdateCallback) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.noteFields = new ArrayList<>();
        this.layoutUpdateCallback = layoutUpdateCallback;
        setBorder(BorderFactory.createLoweredBevelBorder());
        
        // Add the first note field
        addNoteField();
    }
    
    public void addNoteField() {
        JTextField noteField = UIComponents.createTextField(25, "Optional description for this timestamp", null);
        noteFields.add(noteField);
        refreshPanel();
    }
    
    public void removeNoteField(JTextField fieldToRemove) {
        noteFields.remove(fieldToRemove);
        refreshPanel();
    }
    
    private void refreshPanel() {
        removeAll();
        
        for (int i = 0; i < noteFields.size(); i++) {
            JTextField noteField = noteFields.get(i);
            
            // Show + button only on the LAST note field
            boolean showAddButton = (i == noteFields.size() - 1);
            // Show - button only if there's more than one note field AND it's not the first field
            boolean showRemoveButton = (noteFields.size() > 1 && i > 0);
            
            JPanel noteRowPanel = UIComponents.createNoteRowPanel(
                noteField,
                showAddButton,
                showRemoveButton,
                e -> addNoteField(),
                e -> removeNoteField(noteField)
            );
            
            add(noteRowPanel);
            
            if (i < noteFields.size() - 1) {
                add(Box.createVerticalStrut(3));
            }
        }
        
        revalidate();
        repaint();
        
        // Force parent containers to recalculate layout
        Container parent = getParent();
        while (parent != null) {
            parent.revalidate();
            parent.repaint();
            if (parent instanceof JFrame) break;
            parent = parent.getParent();
        }
        
        // Notify callback for layout updates (e.g., window resizing)
        if (layoutUpdateCallback != null) {
            SwingUtilities.invokeLater(layoutUpdateCallback);
        }
    }
    
    public List<String> collectNotes() {
        List<String> notes = new ArrayList<>();
        for (JTextField noteField : noteFields) {
            notes.add(noteField.getText());
        }
        return notes;
    }
    
    public void clearAllNotes() {
        for (JTextField noteField : noteFields) {
            noteField.setText("");
        }
        
        // Reset to single note field
        noteFields.clear();
        removeAll();
        addNoteField();
        revalidate();
        repaint();
    }
}
