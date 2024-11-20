package ui.components;

import javax.swing.*;

import domain.bomb.Bomb;

import java.awt.*;

public class BombListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof Bomb) {
            Bomb bomb = (Bomb) value;
            label.setText(String.format("Bomb ID: %s, Type: %s, Status: %s",
                    bomb.getId(), bomb.getType(), bomb.isActive() ? "Active" : "Inactive"));
        } else {
            label.setText("Unknown item");
        }

        return label;
    }
}
