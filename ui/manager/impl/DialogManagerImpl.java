package ui.manager.impl;

import domain.bomb.Bomb;
import domain.bomb.BombType;
import domain.bomb.RemoteBomb;
import domain.bomb.SmokeBomb;
import domain.bomb.TimedBomb;
import domain.value.Distance;
import service.IBombService;
import ui.components.dialogs.*;
import ui.manager.IDialogManager;
import ui.manager.IDisplayManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
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
    
                if (confirm == JOptionPane.YES_OPTION) {
                    if (bomb instanceof TimedBomb timedBomb) {
                        showCountdownDialog(timedBomb.getId(), timedBomb.getDuration().seconds());
                    } else if (bomb instanceof SmokeBomb smokeBomb) {
                        showSmokeRadiusDialog(smokeBomb.getId(), smokeBomb.getRadius());
                    } else if (bomb instanceof RemoteBomb remoteBomb) {
                        showRemoteBombDialog(remoteBomb.getId(), remoteBomb.getLocation());
                    } else {
                        explodeGeneralBomb(bomb);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(
                        parentFrame,
                        "Please select a bomb to explode.",
                        "No Bomb Selected",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });
    }
        
    // Smoke Bomb Radius Animation
    private void showSmokeRadiusDialog(String bombId, Distance radius) {
        JDialog smokeDialog = new JDialog(parentFrame, "Smoke Bomb Effect", true);
        smokeDialog.setSize(400, 300);
        smokeDialog.setLocationRelativeTo(parentFrame);
        smokeDialog.setUndecorated(true);
    
        JPanel smokePanel = new JPanel(new BorderLayout());
        smokePanel.setBackground(Color.BLACK);
    
        JLabel smokeLabel = new JLabel("SMOKE!", SwingConstants.CENTER);
        smokeLabel.setFont(new Font("Arial", Font.BOLD, 72));
        smokeLabel.setForeground(Color.GRAY);
    
        smokePanel.add(smokeLabel, BorderLayout.CENTER);
        smokeDialog.add(smokePanel);
    
        Timer smokeTimer = new Timer(200, new ActionListener() {
            int frame = 0;
            final int maxFrames =  10; // Adjust frame duration based on radius
    
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                smokeLabel.setForeground(new Color(
                        50 + (frame * 205 / maxFrames), // Gradual lightening of gray
                        50 + (frame * 205 / maxFrames),
                        50 + (frame * 205 / maxFrames)
                ));
                frame++;
                if (frame >= maxFrames) {
                    ((Timer) e.getSource()).stop();
                    smokeDialog.dispose();
                    bombService.explodeBomb(bombId);
                    displayManager.refreshDisplay();
                }
            }
        });
    
        smokeDialog.setVisible(true);
        smokeTimer.start();
    }
    
    // Remote Bomb Location Animation
    private void showRemoteBombDialog(String bombId, String location) {
        JDialog remoteDialog = new JDialog(parentFrame, "Remote Bomb Activation", true);
        remoteDialog.setSize(400, 300);
        remoteDialog.setLocationRelativeTo(parentFrame);
        remoteDialog.setUndecorated(true);
    
        JPanel remotePanel = new JPanel(new BorderLayout());
        remotePanel.setBackground(Color.BLACK);
    
        JLabel locationLabel = new JLabel("Activating at " + location, SwingConstants.CENTER);
        locationLabel.setFont(new Font("Arial", Font.BOLD, 24));
        locationLabel.setForeground(Color.CYAN);
    
        JLabel remoteLabel = new JLabel("REMOTE DETONATION", SwingConstants.CENTER);
        remoteLabel.setFont(new Font("Arial", Font.BOLD, 48));
        remoteLabel.setForeground(Color.RED);
    
        remotePanel.add(locationLabel, BorderLayout.NORTH);
        remotePanel.add(remoteLabel, BorderLayout.CENTER);
        remoteDialog.add(remotePanel);
    
        Timer remoteTimer = new Timer(500, new ActionListener() {
            int frame = 0;
            final Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW};
    
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                remoteLabel.setForeground(colors[frame % colors.length]);
                frame++;
                if (frame >= 10) { // 5 seconds animation
                    ((Timer) e.getSource()).stop();
                    remoteDialog.dispose();
                    bombService.explodeBomb(bombId);
                    displayManager.refreshDisplay();
                }
            }
        });
    
        remoteDialog.setVisible(true);
        remoteTimer.start();
    }
    
    // Generic Bomb Explosion
    private void explodeGeneralBomb(Bomb bomb) {
        JOptionPane.showMessageDialog(
                parentFrame,
                "The bomb has exploded!",
                "Explosion",
                JOptionPane.INFORMATION_MESSAGE
        );
        bombService.explodeBomb(bomb.getId());
        displayManager.refreshDisplay();
    }
    

    @Override
    public void showCountdownDialog(String bombId, int seconds) {
        SwingUtilities.invokeLater(() -> {
            CountdownDialog dialog = new CountdownDialog(parentFrame, seconds);
            dialog.start();
        });
    }
}
