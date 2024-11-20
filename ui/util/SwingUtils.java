// ui/util/SwingUtils.java
package ui.util;

import javax.swing.*;
import java.awt.*;

public class SwingUtils {
    private SwingUtils() {} // Utility class

    public static void centerOnScreen(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = window.getSize();
        
        int x = (screenSize.width - windowSize.width) / 2;
        int y = (screenSize.height - windowSize.height) / 2;
        
        window.setLocation(x, y);
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public static boolean confirmAction(Component parent, String message) {
        return JOptionPane.showConfirmDialog(
            parent,
            message,
            "Confirm",
            JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
    }
}