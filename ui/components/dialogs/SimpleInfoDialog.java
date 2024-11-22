package ui.components.dialogs;

import javax.swing.*;
import java.awt.*;

public class SimpleInfoDialog extends JDialog {
    public SimpleInfoDialog(Frame parent, String title, String message) {
        // Call the parent constructor
        super(parent, title, true);  // modal dialog

        // Create the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a label with the message
        JLabel messageLabel = new JLabel(message);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        // Add the label to the panel
        panel.add(messageLabel, BorderLayout.CENTER);

        // Create an OK button
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());

        // Add the button to the panel
        panel.add(okButton, BorderLayout.SOUTH);

        // Add the panel to the dialog
        add(panel);

        // Set dialog properties
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    // Convenience method to show the dialog
    public static void show(Frame parent, String title, String message) {
        SwingUtilities.invokeLater(() -> {
            SimpleInfoDialog dialog = new SimpleInfoDialog(parent, title, message);
            dialog.setVisible(true);
        });
    }
}