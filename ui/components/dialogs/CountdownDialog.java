package ui.components.dialogs;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class CountdownDialog {
    private final JDialog dialog;
    private final Timer countdownTimer;
    private final Timer blinkTimer;
    private int remainingSeconds;
    private final JProgressBar progressBar;
    private final JLabel countdownLabel;
    private final JLabel warningLabel;
    private final JButton cancelButton;

    public CountdownDialog(JFrame parent, int seconds) {
        this.remainingSeconds = seconds;

        // Initialize dialog
        this.dialog = new JDialog(parent, "Countdown", true);
        this.dialog.setSize(400, 300);
        this.dialog.setLocationRelativeTo(parent);
        this.dialog.setLayout(new BorderLayout());

        // Countdown label
        this.countdownLabel = new JLabel(formatTime(remainingSeconds), SwingConstants.CENTER);
        this.countdownLabel.setFont(new Font("Digital-7", Font.BOLD, 72));
        this.countdownLabel.setForeground(Color.RED);

        // Warning label
        this.warningLabel = new JLabel("Countdown in progress...", SwingConstants.CENTER);
        this.warningLabel.setFont(new Font("Arial", Font.BOLD, 14));
        this.warningLabel.setForeground(Color.YELLOW);

        // Progress bar
        this.progressBar = new JProgressBar(0, seconds);
        this.progressBar.setStringPainted(true);
        this.progressBar.setForeground(Color.RED);
        this.progressBar.setBackground(Color.DARK_GRAY);

        // Cancel button
        this.cancelButton = new JButton("Cancel Countdown");
        this.cancelButton.addActionListener(e -> cancelCountdown());

        // Layout
        JPanel countdownPanel = createCountdownPanel();
        JPanel buttonPanel = createButtonPanel();

        this.dialog.add(countdownPanel, BorderLayout.CENTER);
        this.dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Timers
        this.blinkTimer = new Timer(500, e -> toggleWarningLabelVisibility());
        this.countdownTimer = createCountdownTimer();
    }

    private JPanel createCountdownPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(countdownLabel, BorderLayout.CENTER);
        panel.add(warningLabel, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(cancelButton);
        return panel;
    }

    private Timer createCountdownTimer() {
        return new Timer(1000, e -> {
            remainingSeconds--;
            countdownLabel.setText(formatTime(remainingSeconds));
            progressBar.setValue(progressBar.getMaximum() - remainingSeconds);

            if (remainingSeconds <= 5) {
                handleWarningPhase();
            }

            if (remainingSeconds <= 0) {
                handleCountdownComplete();
            }
        });
    }

    private void handleWarningPhase() {
        warningLabel.setText("WARNING: Explosion imminent!");
        warningLabel.setForeground(Color.RED);
        if (!blinkTimer.isRunning()) {
            blinkTimer.start();
        }
    }

    private void handleCountdownComplete() {
        countdownTimer.stop();
        blinkTimer.stop();
        dialog.dispose();
        showExplosionEffect();
    }

    private void cancelCountdown() {
        countdownTimer.stop();
        blinkTimer.stop();
        dialog.dispose();
    }

    private void toggleWarningLabelVisibility() {
        warningLabel.setVisible(!warningLabel.isVisible());
    }

    private void showExplosionEffect() {
        JDialog explosionDialog = new JDialog(dialog, false);
        explosionDialog.setUndecorated(true);
        explosionDialog.setSize(400, 300);
        explosionDialog.setLocationRelativeTo(dialog);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        JLabel explosionLabel = new JLabel("BOOM!", SwingConstants.CENTER);
        explosionLabel.setFont(new Font("Arial", Font.BOLD, 72));
        explosionLabel.setForeground(Color.RED);

        panel.add(explosionLabel, BorderLayout.CENTER);
        explosionDialog.add(panel);

        Timer explosionAnimationTimer = createExplosionAnimationTimer(explosionLabel, explosionDialog);
        explosionDialog.setVisible(true);
        explosionAnimationTimer.start();
    }

    private Timer createExplosionAnimationTimer(JLabel explosionLabel, JDialog explosionDialog) {
        final int[] frame = {0};
        final Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW};
        Timer timer = new Timer(100, e -> {
            explosionLabel.setForeground(colors[frame[0] % colors.length]);
            frame[0]++;
            if (frame[0] >= 15) {
                ((Timer) e.getSource()).stop();
                explosionDialog.dispose();
                JOptionPane.showMessageDialog(dialog, "Explosion completed!", "Explosion", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return timer;
    }

    private String formatTime(int seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }

    public void start() {
        progressBar.setValue(0);
        countdownLabel.setText(formatTime(remainingSeconds));
        countdownTimer.start();
        dialog.setVisible(true);
    }
}
