package ui.manager.impl;

import domain.bomb.*;
import service.IBombService;
import ui.components.dialogs.*;
import ui.manager.IDialogManager;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;

public class DialogManagerImpl implements IDialogManager {
    private final IBombService bombService;
    private final ScheduledExecutorService executorService;
    private final JFrame parentFrame;

    public DialogManagerImpl(IBombService bombService) {
        this.bombService = Objects.requireNonNull(bombService);
        this.parentFrame = (JFrame) KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        this.executorService = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "BombCountdownThread");
            thread.setDaemon(true);
            return thread;
        });
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
        getSelectedBomb().ifPresent(bomb -> {
            SwingUtilities.invokeLater(() -> {
                EditBombDialog dialog = new EditBombDialog(parentFrame, bombService, bomb);
                dialog.setLocationRelativeTo(parentFrame);
                dialog.setVisible(true);
            });
        });
    }

    @Override
    public void activateSelectedBomb() {
        getSelectedBombId().ifPresent(bombId -> {
            try {
                bombService.activateBomb(bombId);
                JOptionPane.showMessageDialog(
                    parentFrame,
                    "Bomb activated successfully!",
                    "Activation Success",
                    JOptionPane.INFORMATION_MESSAGE
                );

                // If it's a timed bomb, start the countdown
                bombService.getAllBombs().stream()
                    .filter(bomb -> bomb.getId().equals(bombId))
                    .filter(bomb -> bomb instanceof TimedBomb)
                    .map(bomb -> (TimedBomb) bomb)
                    .findFirst()
                    .ifPresent(timedBomb -> showCountdownDialog(bombId, timedBomb.getDuration().seconds()));
            } catch (Exception e) {
                showErrorDialog("Failed to activate bomb", e);
            }
        });
    }

    @Override
    public void deactivateSelectedBomb() {
        getSelectedBombId().ifPresent(bombId -> {
            try {
                bombService.deactivateBomb(bombId);
                JOptionPane.showMessageDialog(
                    parentFrame,
                    "Bomb deactivated successfully!",
                    "Deactivation Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (Exception e) {
                showErrorDialog("Failed to deactivate bomb", e);
            }
        });
    }

    @Override
    public void explodeSelectedBomb() {
        getSelectedBombId().ifPresent(bombId -> {
            try {
                int confirmation = JOptionPane.showConfirmDialog(
                    parentFrame,
                    "Are you sure you want to detonate this bomb?",
                    "Confirm Detonation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

                if (confirmation == JOptionPane.YES_OPTION) {
                    bombService.explodeBomb(bombId);
                    JOptionPane.showMessageDialog(
                        parentFrame,
                        "Bomb detonated successfully!",
                        "Detonation Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            } catch (Exception e) {
                showErrorDialog("Failed to detonate bomb", e);
            }
        });
    }

    @Override
    public void showCountdownDialog(String bombId, int seconds) {
        SwingUtilities.invokeLater(() -> {
            CountdownDialog dialog = new CountdownDialog(parentFrame, bombService, bombId, seconds);
            dialog.setLocationRelativeTo(parentFrame);
            dialog.setVisible(true);

            // Schedule the explosion
            executorService.schedule(() -> {
                try {
                    if (dialog.isVisible()) {
                        dialog.dispose();
                    }
                    bombService.explodeBomb(bombId);
                    JOptionPane.showMessageDialog(
                        parentFrame,
                        "Timed bomb detonated!",
                        "Detonation Complete",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (Exception e) {
                    showErrorDialog("Failed to detonate timed bomb", e);
                }
            }, seconds, TimeUnit.SECONDS);
        });
    }

    private Optional<String> getSelectedBombId() {
        return getSelectedBomb().map(Bomb::getId);
    }

    private Optional<Bomb> getSelectedBomb() {
        Component focused = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        if (focused instanceof JList<?>) {
            JList<?> list = (JList<?>) focused;
            Object selectedValue = list.getSelectedValue();
            if (selectedValue instanceof Bomb) {
                return Optional.of((Bomb) selectedValue);
            }
        }
        
        JOptionPane.showMessageDialog(
            parentFrame,
            "Please select a bomb from the list first.",
            "No Selection",
            JOptionPane.WARNING_MESSAGE
        );
        return Optional.empty();
    }

    private void showErrorDialog(String message, Exception e) {
        JOptionPane.showMessageDialog(
            parentFrame,
            message + ": " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}