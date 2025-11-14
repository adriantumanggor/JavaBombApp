package ui;

import service.IBombService;
import ui.components.ButtonPanel;
import ui.manager.IDialogManager;
import ui.manager.IDisplayManager;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class BombManagementUI extends JFrame {
    private final IBombService bombService;
    private final IDisplayManager displayManager;
    private final IDialogManager dialogManager;

    public BombManagementUI(
        IBombService bombService,
        IDisplayManager displayManager,
        IDialogManager dialogManager
    ) {
        this.bombService = Objects.requireNonNull(bombService);
        this.displayManager = Objects.requireNonNull(displayManager);
        this.dialogManager = Objects.requireNonNull(dialogManager);
        
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Bomb Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(displayManager.getBombListPanel(), BorderLayout.CENTER);
        add(displayManager.getHistoryPanel(), BorderLayout.EAST);
        add(new ButtonPanel(bombService, dialogManager), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }
}
