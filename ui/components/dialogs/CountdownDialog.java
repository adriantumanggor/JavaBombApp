package ui.components.dialogs;

import service.IBombService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class CountdownDialog extends JDialog {
    private final IBombService bombService;
    private final String bombId;
    private final Timer timer;
    private final JLabel countdownLabel;
    private int remainingSeconds;

    public CountdownDialog(JFrame parent, IBombService bombService, String bombId, int seconds) {
        super(parent, "Countdown", true);
        this.bombService = Objects.requireNonNull(bombService);
        this.bombId = Objects.requireNonNull(bombId);
        this.remainingSeconds = seconds;

        setLayout(new BorderLayout(10, 10));
        setSize(300, 150);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        countdownLabel = new JLabel(String.format("Time remaining: %d seconds", remainingSeconds));
        countdownLabel.setHorizontalAlignment(SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 20));

        timer = new Timer(1000, e -> updateCountdown());
        
        initializeUI();
        addWindowListener();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        mainPanel.add(countdownLabel, BorderLayout.CENTER);
        
        JButton cancelButton = new JButton("Cancel Countdown");
        cancelButton.addActionListener(e -> cancelCountdown());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void addWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancelCountdown();
            }
        });
    }

    public void startCountdown() {
        timer.start();
    }

    private void updateCountdown() {
        remainingSeconds--;
        countdownLabel.setText(String.format("Time remaining: %d seconds", remainingSeconds));
        
        if (remainingSeconds <= 0) {
            timer.stop();
            try {
                bombService.explodeBomb(bombId);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error exploding bomb: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void cancelCountdown() {
        timer.stop();
        dispose();
    }
}