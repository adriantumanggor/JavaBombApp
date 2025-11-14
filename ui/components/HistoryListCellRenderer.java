package ui.components;

import java.awt.Component;
import javax.swing.*;
import domain.history.ExplosionRecord;

public class HistoryListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof ExplosionRecord record) {
            String text = String.format("[%s] %s", 
                    record.bombType().getDisplayName(),record.details());
            setText(text); 
        }

        return component; 
    }
}
