package ui.manager.impl;

import service.IBombService;
import domain.bomb.BombType;
import ui.components.dialogs.*;
import ui.manager.IDialogManager;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class DialogManagerImpl implements IDialogManager {
    private final IBombService bombService;
    private final Window parentWindow;
    private String selectedBombId;
    private CountdownDialog activeCountdownDialog;

    public DialogManagerImpl(IBombService bombService) {
        this.bombService = Objects.requireNonNull(bombService);
        this.parentWindow = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
    }

    @Override
    public void showAddBombDialog() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(parentWindow);
        AddBombDialog dialog = new AddBombDialog(parent, bombService);
        dialog.setVisible(true);
    }

    @Override
    public void showEditBombDialog() {
        if (selectedBombId == null) {
            JOptionPane.showMessageDialog(parentWindow,
                "Please select a bomb to edit",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(parentWindow);
        EditBombDialog dialog = new EditBombDialog(parent, bombService, selectedBombId);
        dialog.setVisible(true);
    }

    @Override
    public void activateSelectedBomb() {
        if (selectedBombId == null) {
            JOptionPane.showMessageDialog(parentWindow,
                "Please select a bomb to activate",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Get the bomb type before activation
            Bomb selectedBomb = bombService.getAllBombs().stream()
                .filter(b -> b.getId().equals(selectedBombId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Selected bomb not found"));

            // Special handling for timed bombs
            if (selectedBomb.getType() == BombType.TIMED) {
                showCountdownDialog(selectedBombId, selectedBomb.getDetonationTime());
            }

            bombService.activateBomb(selectedBombId);
            JOptionPane.showMessageDialog(parentWindow,
                "Bomb activated successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentWindow,
                "Failed to activate bomb: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void deactivateSelectedBomb() {
        if (selectedBombId == null) {
            JOptionPane.showMessageDialog(parentWindow,
                "Please select a bomb to deactivate",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // If there's an active countdown, cancel it
            if (activeCountdownDialog != null && activeCountdownDialog.isVisible()) {
                activeCountdownDialog.dispose();
                activeCountdownDialog = null;
            }

            bombService.deactivateBomb(selectedBombId);
            JOptionPane.showMessageDialog(parentWindow,
                "Bomb deactivated successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentWindow,
                "Failed to deactivate bomb: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void explodeSelectedBomb() {
        if (selectedBombId == null) {
            JOptionPane.showMessageDialog(parentWindow,
                "Please select a bomb to explode",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Bomb selectedBomb = bombService.getAllBombs().stream()
                .filter(b -> b.getId().equals(selectedBombId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Selected bomb not found"));

            String confirmMessage = String.format("Are you sure you want to explode this %s?", 
                selectedBomb.getType().getDisplayName());

            int confirm = JOptionPane.showConfirmDialog(parentWindow,
                confirmMessage,
                "Confirm Explosion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // If there's an active countdown, cancel it
                if (activeCountdownDialog != null && activeCountdownDialog.isVisible()) {
                    activeCountdownDialog.dispose();
                    activeCountdownDialog = null;
                }

                bombService.explodeBomb(selectedBombId);
                JOptionPane.showMessageDialog(parentWindow,
                    "Bomb exploded successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentWindow,
                "Failed to explode bomb: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void showCountdownDialog(String bombId, long seconds) {
        // If there's already an active countdown, dispose it
        if (activeCountdownDialog != null && activeCountdownDialog.isVisible()) {
            activeCountdownDialog.dispose();
        }

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(parentWindow);
        activeCountdownDialog = new CountdownDialog(parent, bombService, bombId, (int) seconds);
        activeCountdownDialog.startCountdown();
        activeCountdownDialog.setVisible(true);
    }

    // Method to update selected bomb ID
    public void setSelectedBombId(String bombId) {
        this.selectedBombId = bombId;
    }

    // Method to check if countdown is active
    public boolean isCountdownActive() {
        return activeCountdownDialog != null && activeCountdownDialog.isVisible();
    }
}