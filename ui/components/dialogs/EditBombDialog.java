package ui.components.dialogs;

import domain.bomb.*;
import domain.value.*;
import service.IBombService;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class EditBombDialog extends JDialog {
    private final IBombService bombService;
    private final Bomb bomb;
    private final JTextField nameField;
    private final JPanel specificOptionsPanel;
    private final JColorChooser colorChooser;

    public EditBombDialog(JFrame parent, IBombService bombService, Bomb bomb) {
        super(parent, "Edit Bomb", true);
        this.bombService = Objects.requireNonNull(bombService);
        this.bomb = Objects.requireNonNull(bomb);

        setLayout(new BorderLayout(10, 10));
        setSize(500, 600);
        setLocationRelativeTo(parent);

        nameField = new JTextField(20);
        specificOptionsPanel = new JPanel(new CardLayout());
        colorChooser = new JColorChooser();

        initializeUI();
        loadBombData();
    }

    private void initializeUI() {
        // Basic info panel
        JPanel basicPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        basicPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        basicPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        basicPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        basicPanel.add(new JLabel(bomb.getType().getDisplayName()), gbc);

        // Specific options based on bomb type
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
        add(new JScrollPane(colorChooser), BorderLayout.CENTER);
        add(specificOptionsPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeSpecificOptions() {
        switch (bomb.getType()) {
            case TIMED -> initializeTimedOptions();
            case SMOKE -> initializeSmokeOptions();
            case REMOTE -> initializeRemoteOptions();
        }
    }

    private void initializeTimedOptions() {
        JPanel timedPanel = new JPanel();
        JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 3600, 1));
        timedPanel.add(new JLabel("Duration (seconds):"));
        timedPanel.add(durationSpinner);
        specificOptionsPanel.add(timedPanel, BombType.TIMED.toString());
    }

    private void initializeSmokeOptions() {
        JPanel smokePanel = new JPanel();
        JSpinner radiusSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
        smokePanel.add(new JLabel("Radius (meters):"));
        smokePanel.add(radiusSpinner);
        specificOptionsPanel.add(smokePanel, BombType.SMOKE.toString());
    }

    private void initializeRemoteOptions() {
        JPanel remotePanel = new JPanel();
        JTextField frequencyField = new JTextField(10);
        remotePanel.add(new JLabel("Frequency:"));
        remotePanel.add(frequencyField);
        specificOptionsPanel.add(remotePanel, BombType.REMOTE.toString());
    }

    private void loadBombData() {
        nameField.setText(bomb.getName());
        colorChooser.setColor(bomb.getColor());

        switch (bomb.getType()) {
            case TIMED -> loadTimedBombData((TimedBomb) bomb);
            case SMOKE -> loadSmokeBombData((SmokeBomb) bomb);
            case REMOTE -> loadRemoteBombData((RemoteBomb) bomb);
        }
    }

    private void loadTimedBombData(TimedBomb timedBomb) {
        JPanel panel = (JPanel) specificOptionsPanel.getComponent(0);
        JSpinner spinner = (JSpinner) panel.getComponent(1);
        spinner.setValue(timedBomb.getDuration().seconds());
    }

    private void loadSmokeBombData(SmokeBomb smokeBomb) {
        JPanel panel = (JPanel) specificOptionsPanel.getComponent(0);
        JSpinner spinner = (JSpinner) panel.getComponent(1);
        spinner.setValue(smokeBomb.getRadius().meters());
    }

    private void loadRemoteBombData(RemoteBomb remoteBomb) {
        JPanel panel = (JPanel) specificOptionsPanel.getComponent(0);
        JTextField field = (JTextField) panel.getComponent(1);
        field.setText(remoteBomb.getFrequency());
    }

    private void saveBomb() {
        try {
            bomb.setName(nameField.getText().trim());
            bomb.setColor(colorChooser.getColor());

            switch (bomb.getType()) {
                case TIMED -> updateTimedBomb((TimedBomb) bomb);
                case SMOKE -> updateSmokeBomb((SmokeBomb) bomb);
                case REMOTE -> updateRemoteBomb((RemoteBomb) bomb);
            }

            bombService.addBomb(bomb); // This will update the existing bomb
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
        JPanel panel = (JPanel) specificOptionsPanel.getComponent(0);
        JSpinner spinner = (JSpinner) panel.getComponent(1);
        int duration = (Integer) spinner.getValue();
        timedBomb.setDuration(Duration.ofSeconds(duration));
    }

    private void updateSmokeBomb(SmokeBomb smokeBomb) {
        JPanel panel = (JPanel) specificOptionsPanel.getComponent(0);
        JSpinner spinner = (JSpinner) panel.getComponent(1);
        int radius = (Integer) spinner.getValue();
        smokeBomb.setRadius(Distance.ofMeters(radius));
    }

    private void updateRemoteBomb(RemoteBomb remoteBomb) {
        JPanel panel = (JPanel) specificOptionsPanel.getComponent(0);
        JTextField field = (JTextField) panel.getComponent(1);
        remoteBomb.setFrequency(field.getText().trim());
    }
}