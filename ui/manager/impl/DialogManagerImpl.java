package ui.manager.impl;

import domain.bomb.*;
import service.IBombService;
import ui.components.dialogs.*;
import ui.manager.IDialogManager;
import ui.manager.IDisplayManager;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;

public class DialogManagerImpl implements IDialogManager {
    private final IBombService bombService;
    private final IDisplayManager displayManager; // Tambahkan ini untuk komunikasi
    private final JFrame parentFrame;
        
    public DialogManagerImpl(IBombService bombService, IDisplayManager displayManager) {
        this.bombService = Objects.requireNonNull(bombService);
        this.displayManager = Objects.requireNonNull(displayManager); // Simpan referensi
        this.parentFrame = (JFrame) KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
    }

    @Override
    public void showAddBombDialog() {
        SwingUtilities.invokeLater(() -> {
            AddBombDialog dialog = new AddBombDialog(parentFrame, bombService);
            dialog.setLocationRelativeTo(parentFrame);
            dialog.setVisible(true);
        });
    }

    @Override
    public void showEditBombDialog() {
    }

    @Override
    public void activateSelectedBomb() {
    }

    @Override
    public void deactivateSelectedBomb() {
    }

    @Override
    public void explodeSelectedBomb() {
    }

    @Override
    public void showCountdownDialog(String bombId, int seconds) {
            CountdownDialog dialog = new CountdownDialog(parentFrame, bombService, bombId, seconds);
            dialog.setLocationRelativeTo(parentFrame);
            dialog.setVisible(true);

            // Schedule the explosion
    }

}