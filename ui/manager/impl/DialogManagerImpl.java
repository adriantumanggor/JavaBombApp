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

public class DialogManagerImpl implements IDialogManager {
    private final IBombService bombService;
    private final IDisplayManager displayManager;
    private final JFrame parentFrame;

    public DialogManagerImpl(IBombService bombService, IDisplayManager displayManager) {
        this.bombService = Objects.requireNonNull(bombService);
        this.displayManager = Objects.requireNonNull(displayManager);
        this.parentFrame = (JFrame) KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
    }

    @Override
    public void showAddBombDialog() {
        SwingUtilities.invokeLater(() -> {
            AddBombDialog dialog = new AddBombDialog(parentFrame, bombService);
            dialog.setLocationRelativeTo(parentFrame);
            dialog.setVisible(true);
            displayManager.refreshDisplay(); // Refresh after adding
        });
    }

    @Override
    public void showEditBombDialog() {
        SwingUtilities.invokeLater(() -> {
            Optional<Bomb> selectedBomb = displayManager.getBombListPanel().getSelectedBomb();
            if (selectedBomb.isPresent()) {
                EditBombDialog dialog = new EditBombDialog(parentFrame, bombService, selectedBomb.get());
                dialog.setLocationRelativeTo(parentFrame);
                dialog.setVisible(true);
                displayManager.refreshDisplay(); // Refresh after editing
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Please select a bomb to edit.", "No Bomb Selected",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    @Override
    public void activateSelectedBomb() {
        SwingUtilities.invokeLater(() -> {
            Optional<Bomb> selectedBomb = displayManager.getBombListPanel().getSelectedBomb();
            if (selectedBomb.isPresent()) {
                bombService.activateBomb(selectedBomb.get().getId());
                displayManager.refreshDisplay(); // Refresh after activation
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Please select a bomb to activate.", "No Bomb Selected",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    @Override
    public void deactivateSelectedBomb() {
        SwingUtilities.invokeLater(() -> {
            Optional<Bomb> selectedBomb = displayManager.getBombListPanel().getSelectedBomb();
            if (selectedBomb.isPresent()) {
                bombService.deactivateBomb(selectedBomb.get().getId());
                displayManager.refreshDisplay(); // Refresh after deactivation
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Please select a bomb to deactivate.", "No Bomb Selected",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    @Override
    public void explodeSelectedBomb() {
        SwingUtilities.invokeLater(() -> {
            Optional<Bomb> selectedBomb = displayManager.getBombListPanel().getSelectedBomb();
            if (selectedBomb.isPresent()) {
                Bomb bomb = selectedBomb.get();

                int confirm = JOptionPane.showConfirmDialog(
                        parentFrame,
                        "Are you sure you want to explode this bomb?",
                        "Confirm Explosion",
                        JOptionPane.YES_NO_OPTION);

                bombService.explodeBomb(bomb.getId());
                displayManager.refreshDisplay();

                if (confirm == JOptionPane.YES_OPTION) {
                    if (bomb instanceof TimedBomb timedBomb) {
                        showCountdownDialog(timedBomb, timedBomb.getId(), timedBomb.getDuration().seconds());
                    } else if (bomb instanceof SmokeBomb smokeBomb) {
                        showSmokeRadiusDialog(smokeBomb.getId(), smokeBomb.getRadius().meters());
                    } else if (bomb instanceof RemoteBomb remoteBomb) {
                        showRemoteBombDialog(remoteBomb.getId(), remoteBomb.getFrequency());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(
                        parentFrame,
                        "Please select a bomb to explode.",
                        "No Bomb Selected",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    // Smoke Bomb Radius Animation
    @Override
    public void showCountdownDialog(TimedBomb timedBomb, String bombId, int seconds) {
        SwingUtilities.invokeLater(() -> {
            CountdownDialog dialog = new CountdownDialog(timedBomb, parentFrame, bombService, seconds);
            dialog.start();
            displayManager.refreshDisplay();
        });
    }

    // Smoke Bomb Radius Dialog
    @Override
    public void showSmokeRadiusDialog(String bombId, int radius) {
        SwingUtilities.invokeLater(() -> {
            SimpleInfoDialog.show(parentFrame, "Smoke Bomb:" + bombId, "Duarr ! Bom ini meledak dalam radius " + radius + " meter");
        });
    }

    // Remote Bomb Location Dialog
    @Override
    public void showRemoteBombDialog(String bombId, String frequency) {
        SwingUtilities.invokeLater(() -> {
            SimpleInfoDialog.show(parentFrame, "Remote Bomb:" + bombId, "Duarr ! Bom ini meledak dalam Frekuensi " + frequency + "Hz");
        });
    }
}
