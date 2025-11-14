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

public class AddBombDialog extends JDialog {
    private final IBombService bombService;
    private final JTextField nameField;
    private final JTextField locationField;
    private final JComboBox<BombType> typeComboBox;
    private final JPanel specificOptionsPanel;

    public AddBombDialog(JFrame parent, IBombService bombService) {
        super(parent, "Add New Bomb", true);
        this.bombService = Objects.requireNonNull(bombService);

        nameField = new JTextField(20);
        locationField = new JTextField(20);
        typeComboBox = new JComboBox<>(BombType.values());
        specificOptionsPanel = new JPanel(new CardLayout());

        setLayout(new BorderLayout(10, 10));
        setSize(400, 400);
        setLocationRelativeTo(parent);

        initializeUI();
    }

    @SuppressWarnings("unused")
    private void initializeUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel basicPanel = createBasicInfoPanel();
        mainPanel.add(basicPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        initializeSpecificOptions();
        mainPanel.add(specificOptionsPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        JPanel buttonPanel = createButtonPanel();

        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        typeComboBox.addActionListener(e -> {
            CardLayout cl = (CardLayout) specificOptionsPanel.getLayout();
            cl.show(specificOptionsPanel, typeComboBox.getSelectedItem().toString());
        });
    }

    private JPanel createBasicInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        panel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(locationField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        panel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(typeComboBox, gbc);

        return panel;
    }

    private void initializeSpecificOptions() {
        specificOptionsPanel.setBorder(BorderFactory.createTitledBorder("Specific Options"));

        JPanel timedPanel = new JPanel(new GridBagLayout());
        JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 3600, 1));
        addLabelAndComponent(timedPanel, "Duration (seconds):", durationSpinner, 0);
        specificOptionsPanel.add(timedPanel, BombType.TIMED.toString());

        JPanel smokePanel = new JPanel(new GridBagLayout());
        JSpinner radiusSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
        addLabelAndComponent(smokePanel, "Radius (meters):", radiusSpinner, 0);
        specificOptionsPanel.add(smokePanel, BombType.SMOKE.toString());

        JPanel remotePanel = new JPanel(new GridBagLayout());
        JTextField frequencyField = new JTextField(10);
        addLabelAndComponent(remotePanel, "Frequency:", frequencyField, 0);
        specificOptionsPanel.add(remotePanel, BombType.REMOTE.toString());
    }

    private void addLabelAndComponent(JPanel panel, String labelText, Component component, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(component, gbc);
    }

    @SuppressWarnings("unused")
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancel");
        JButton addButton = new JButton("Add");

        cancelButton.addActionListener(e -> dispose());
        addButton.addActionListener(e -> addBomb());

        buttonPanel.add(cancelButton);
        buttonPanel.add(addButton);

        return buttonPanel;
    }

    private void addBomb() {
        try {
            String name = nameField.getText().trim();
            String location = locationField.getText().trim();
            BombType type = (BombType) typeComboBox.getSelectedItem();

            Bomb bomb = switch (type) {
                case TIMED -> createTimedBomb(name, location);
                case SMOKE -> createSmokeBomb(name, location);
                case REMOTE -> createRemoteBomb(name, location);
            };

            bombService.addBomb(bomb);
            
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error creating bomb: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private TimedBomb createTimedBomb(String name, String location) {
        JSpinner durationSpinner = (JSpinner) ((JPanel) specificOptionsPanel.getComponent(0)).getComponent(1);
        int duration = (Integer) durationSpinner.getValue();
        return new TimedBomb(name, location, Duration.ofSeconds(duration));
    }

    private SmokeBomb createSmokeBomb(String name, String location) {
        JSpinner radiusSpinner = (JSpinner) ((JPanel) specificOptionsPanel.getComponent(1)).getComponent(1);
        int radius = (Integer) radiusSpinner.getValue();
        return new SmokeBomb(name, location, Distance.ofMeters(radius));
    }

    private RemoteBomb createRemoteBomb(String name, String location) {
        JTextField frequencyField = (JTextField) ((JPanel) specificOptionsPanel.getComponent(2)).getComponent(1);
        String frequency = frequencyField.getText().trim();
        return new RemoteBomb(name, location, frequency);
    }
}