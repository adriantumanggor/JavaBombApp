package ui.components.dialogs;

import javax.swing.*;
import java.awt.*;

public class SimpleInfoDialog extends JDialog {
    public SimpleInfoDialog(Frame parent, String title, String message) {
        super(parent, title, true);  

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel messageLabel = new JLabel(message);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        panel.add(messageLabel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());

        panel.add(okButton, BorderLayout.SOUTH);

        add(panel);

        setSize(600, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public static void show(Frame parent, String title, String message) {
        SwingUtilities.invokeLater(() -> {
            SimpleInfoDialog dialog = new SimpleInfoDialog(parent, title, message);
            dialog.setVisible(true);
        });
    }
}