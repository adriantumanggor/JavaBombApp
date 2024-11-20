// application/BombApplication.java
package application;

import application.config.ApplicationConfig;
import ui.BombManagementUI;
import javax.swing.SwingUtilities;

public class BombApplication {
    public static void main(String[] args) {
        ApplicationConfig config = new ApplicationConfig();
        
        SwingUtilities.invokeLater(() -> {
            BombManagementUI ui = new BombManagementUI(
                config.bombService(),
                config.displayManager(),
                config.dialogManager()
            );
            ui.setVisible(true);
        });
    }
}