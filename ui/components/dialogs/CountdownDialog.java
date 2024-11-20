package ui.components.dialogs;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

import service.IBombService;

public class CountdownDialog {
    private final JDialog dialog;
    private final IBombService bombService;
    private final String bombId;
    private final int initialSeconds;
    private int remainingSeconds;
    private final JFrame parent;

    public CountdownDialog(JFrame parent, IBombService bombService, String bombId, int seconds) {
        this.parent = parent;
        this.bombService = bombService;
        this.bombId = bombId;
        this.initialSeconds = seconds;
        this.remainingSeconds = seconds;
        this.dialog = createDialog();
    }

    private JDialog createDialog() {
        JPanel mainPanel = createMainPanel();
        JDialog dialog = new JDialog(parent, "Countdown Bomb: " + bombId, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());
        dialog.add(mainPanel);
        return dialog;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel countdownLabel = createCountdownLabel();
        JLabel infoLabel = createInfoLabel();
        JProgressBar progressBar = createProgressBar();

        mainPanel.add(createCountdownPanel(countdownLabel), BorderLayout.CENTER);
        mainPanel.add(createInfoPanel(infoLabel), BorderLayout.NORTH);
        mainPanel.add(progressBar, BorderLayout.SOUTH);

        startCountdown(countdownLabel, infoLabel, progressBar);

        return mainPanel;
    }

    private JLabel createCountdownLabel() {
        JLabel countdownLabel = new JLabel("", SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Digital-7", Font.BOLD, 72));
        countdownLabel.setForeground(Color.RED);
        return countdownLabel;
    }

    private JLabel createInfoLabel() {
        JLabel infoLabel = new JLabel("The bomb is active!", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        infoLabel.setForeground(Color.YELLOW);
        return infoLabel;
    }

    private JProgressBar createProgressBar() {
        JProgressBar progressBar = new JProgressBar(0, initialSeconds);
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.RED);
        progressBar.setBackground(Color.DARK_GRAY);
        progressBar.setBorderPainted(false);
        return progressBar;
    }

    private JPanel createCountdownPanel(JLabel countdownLabel) {
        JPanel countdownPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        countdownPanel.setBackground(Color.BLACK);
        countdownPanel.add(countdownLabel);
        return countdownPanel;
    }

    private JPanel createInfoPanel(JLabel infoLabel) {
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setBackground(Color.BLACK);
        infoPanel.add(infoLabel);
        return infoPanel;
    }

    private void startCountdown(JLabel countdownLabel, JLabel infoLabel, JProgressBar progressBar) {
        Timer blinkTimer = new Timer(500, e -> countdownLabel.setVisible(!countdownLabel.isVisible()));
        Timer countdownTimer = createCountdownTimer(countdownLabel, infoLabel, progressBar, blinkTimer);

        countdownLabel.setText(formatTime(remainingSeconds));
        progressBar.setValue(0);

        countdownTimer.start();
    }

    private Timer createCountdownTimer(JLabel countdownLabel, JLabel infoLabel, JProgressBar progressBar, Timer blinkTimer) {
        return new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                remainingSeconds--;
                countdownLabel.setText(formatTime(remainingSeconds));
                progressBar.setValue(initialSeconds - remainingSeconds);

                if (remainingSeconds <= 5) {
                    handleWarningPhase(infoLabel, blinkTimer);
                }

                if (remainingSeconds <= 0) {
                    ((Timer) e.getSource()).stop();
                    handleExplosion(blinkTimer);
                }
            }
        });
    }

    private void handleWarningPhase(JLabel infoLabel, Timer blinkTimer) {
        infoLabel.setText("WARNING: The bomb will explode soon!");
        infoLabel.setForeground(Color.RED);
        if (!blinkTimer.isRunning()) {
            blinkTimer.start();
        }
    }

    private void handleExplosion(Timer blinkTimer) {
        blinkTimer.stop();
        dialog.dispose();
        showExplosionEffect();
    }

    private void showExplosionEffect() {
        JDialog explosionDialog = new JDialog(parent, false);
        explosionDialog.setUndecorated(true);
        explosionDialog.setSize(400, 300);
        explosionDialog.setLocationRelativeTo(parent);

        JPanel explosionPanel = new JPanel(new BorderLayout());
        explosionPanel.setBackground(Color.BLACK);

        JLabel explosionLabel = new JLabel("BOOM!", SwingConstants.CENTER);
        explosionLabel.setFont(new Font("Arial", Font.BOLD, 72));
        explosionLabel.setForeground(Color.RED);

        explosionPanel.add(explosionLabel, BorderLayout.CENTER);
        explosionDialog.add(explosionPanel);

        Timer explosionTimer = createExplosionAnimationTimer(explosionLabel, explosionDialog);
        explosionDialog.setVisible(true);
        explosionTimer.start();
    }

    private Timer createExplosionAnimationTimer(JLabel explosionLabel, JDialog explosionDialog) {
        final int[] frame = {0};
        final Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW};
        return new Timer(100, e -> {
            explosionLabel.setForeground(colors[frame[0] % colors.length]);
            frame[0]++;
            if (frame[0] >= 15) {
                ((Timer) e.getSource()).stop();
                explosionDialog.dispose();
                try {
                    bombService.explodeBomb(bombId);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parent, "Error: " + ex.getMessage(), "Explosion Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private String formatTime(int seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }

    public void show() {
        dialog.setVisible(true);
    }
}
