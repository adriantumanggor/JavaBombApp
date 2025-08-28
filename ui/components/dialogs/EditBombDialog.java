package ui.components.dialogs;

import domain.bomb.*;
import domain.bomb.impl.RemoteBomb;
import domain.bomb.impl.SmokeBomb;
import domain.bomb.impl.TimedBomb;
import domain.value.*;
import service.IBombService;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class EditBombDialog extends JDialog {
    private final IBombService bombService;
    private final Bomb bomb;
    private final JTextField nameField;
    private final JTextField locationField;
    private final JPanel specificOptionsPanel;

    public EditBombDialog(JFrame parent, IBombService bombService, Bomb bomb) {
        super(parent, "Edit Bomb", true);
        this.bombService = Objects.requireNonNull(bombService);
        this.bomb = Objects.requireNonNull(bomb);

        nameField = new JTextField(20);
        locationField = new JTextField(20);
        specificOptionsPanel = new JPanel(new CardLayout());

        setLayout(new BorderLayout(10, 10));
        setSize(400, 500);
        setLocationRelativeTo(parent);

        initializeUI();
        loadBombData();
    }

    private void initializeUI() {
        // Basic info panel
        JPanel basicPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        basicPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        basicPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        basicPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        basicPanel.add(locationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        basicPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        basicPanel.add(new JLabel(bomb.getType().getDisplayName()), gbc);

        // Initialize specific options panel
        initializeSpecificOptions();

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancel");
        JButton saveButton = new JButton("Save");

        cancelButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> saveBomb());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        // Main layout
        add(basicPanel, BorderLayout.NORTH);
        add(specificOptionsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeSpecificOptions() {
        switch (bomb.getType()) {
            case TIMED -> addTimedOptions();
            case SMOKE -> addSmokeOptions();
            case REMOTE -> addRemoteOptions();
        }
    }

    private void addTimedOptions() {
        JPanel timedPanel = new JPanel();
        JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 3600, 1));
        timedPanel.add(new JLabel("Duration (seconds):"));
        timedPanel.add(durationSpinner);
        specificOptionsPanel.add(timedPanel, BombType.TIMED.toString());
    }

    private void addSmokeOptions() {
        JPanel smokePanel = new JPanel();
        JSpinner radiusSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
        smokePanel.add(new JLabel("Radius (meters):"));
        smokePanel.add(radiusSpinner);
        specificOptionsPanel.add(smokePanel, BombType.SMOKE.toString());
    }

    private void addRemoteOptions() {
        JPanel remotePanel = new JPanel();
        JTextField frequencyField = new JTextField(10);
        remotePanel.add(new JLabel("Frequency:"));
        remotePanel.add(frequencyField);
        specificOptionsPanel.add(remotePanel, BombType.REMOTE.toString());
    }

    private void loadBombData() {
        nameField.setText(bomb.getName());
        locationField.setText(bomb.getLocation());

        CardLayout layout = (CardLayout) specificOptionsPanel.getLayout();
        layout.show(specificOptionsPanel, bomb.getType().toString());

        switch (bomb.getType()) {
            case TIMED -> loadTimedBombData((TimedBomb) bomb);
            case SMOKE -> loadSmokeBombData((SmokeBomb) bomb);
            case REMOTE -> loadRemoteBombData((RemoteBomb) bomb);
        }
    }

    private void loadTimedBombData(TimedBomb timedBomb) {
        JPanel timedPanel = (JPanel) specificOptionsPanel.getComponent(0);
        JSpinner spinner = (JSpinner) timedPanel.getComponent(1);
        spinner.setValue(timedBomb.getDuration().seconds());
    }

    private void loadSmokeBombData(SmokeBomb smokeBomb) {
        JPanel smokePanel = (JPanel) specificOptionsPanel.getComponent(1);
        JSpinner spinner = (JSpinner) smokePanel.getComponent(1);
        spinner.setValue(smokeBomb.getRadius().meters());
    }

    private void loadRemoteBombData(RemoteBomb remoteBomb) {
        JPanel remotePanel = (JPanel) specificOptionsPanel.getComponent(2);
        JTextField field = (JTextField) remotePanel.getComponent(1);
        field.setText(remoteBomb.getFrequency());
    }

    private void saveBomb() {
        try {
            bomb.setName(nameField.getText().trim());
            bomb.setLocation(locationField.getText().trim());

            switch (bomb.getType()) {
                case TIMED -> updateTimedBomb((TimedBomb) bomb);
                case SMOKE -> updateSmokeBomb((SmokeBomb) bomb);
                case REMOTE -> updateRemoteBomb((RemoteBomb) bomb);
            }

            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error saving bomb: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateTimedBomb(TimedBomb timedBomb) {
        JPanel timedPanel = (JPanel) specificOptionsPanel.getComponent(0);
        JSpinner spinner = (JSpinner) timedPanel.getComponent(1);
        int duration = (Integer) spinner.getValue();
        timedBomb.setDuration(Duration.ofSeconds(duration));
    }

    private void updateSmokeBomb(SmokeBomb smokeBomb) {
        JPanel smokePanel = (JPanel) specificOptionsPanel.getComponent(1);
        JSpinner spinner = (JSpinner) smokePanel.getComponent(1);
        int radius = (Integer) spinner.getValue();
        smokeBomb.setRadius(Distance.ofMeters(radius));
    }

    private void updateRemoteBomb(RemoteBomb remoteBomb) {
        JPanel remotePanel = (JPanel) specificOptionsPanel.getComponent(2);
        JTextField field = (JTextField) remotePanel.getComponent(1);
        remoteBomb.setFrequency(field.getText().trim());
    }
}
