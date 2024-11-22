package ui.components;

import java.awt.Component;
import javax.swing.*;
import domain.history.ExplosionRecord;

public class HistoryListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        // Call super to preserve the default rendering behavior
        Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        // Customize rendering if the value is an ExplosionRecord
        if (value instanceof ExplosionRecord record) {
            String text = String.format("[%s] %s", 
                    record.bombType().getDisplayName(),record.details());
            setText(text); // Update the displayed text
        }

        return component; // Return the component to render
    }
}
