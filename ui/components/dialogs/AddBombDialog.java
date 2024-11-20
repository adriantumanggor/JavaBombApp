package ui.components.dialogs;

import domain.bomb.*;
import domain.value.*;
import service.IBombService;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class AddBombDialog extends JDialog {
    private final IBombService bombService;
    private final JTextField nameField;
    private final JComboBox<BombType> typeComboBox;
    private final JPanel specificOptionsPanel;
    private final JColorChooser colorChooser;

    public AddBombDialog(JFrame parent, IBombService bombService) {
        super(parent, "Add New Bomb", true);
        this.bombService = Objects.requireNonNull(bombService);

        setLayout(new BorderLayout(10, 10));
        setSize(500, 600);
        setLocationRelativeTo(parent);

        // Basic fields
        nameField = new JTextField(20);
        typeComboBox = new JComboBox<>(BombType.values());
        specificOptionsPanel = new JPanel(new CardLayout());
        colorChooser = new JColorChooser();

        // Initialize UI
        initializeUI();
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
        basicPanel.add(typeComboBox, gbc);

        // Specific options for each bomb type
        initializeSpecificOptions();

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancel");
        JButton addButton = new JButton("Add");

        cancelButton.addActionListener(e -> dispose());
        addButton.addActionListener(e -> addBomb());

        buttonPanel.add(cancelButton);
        buttonPanel.add(addButton);

        // Main layout
        add(basicPanel, BorderLayout.NORTH);
        add(new JScrollPane(colorChooser), BorderLayout.CENTER);
        add(specificOptionsPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);

        typeComboBox.addActionListener(e -> {
            CardLayout cl = (CardLayout) specificOptionsPanel.getLayout();
            cl.show(specificOptionsPanel, typeComboBox.getSelectedItem().toString());
        });
    }

    private void initializeSpecificOptions() {
        // Timed bomb options
        JPanel timedPanel = new JPanel();
        JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 3600, 1));
        timedPanel.add(new JLabel("Duration (seconds):"));
        timedPanel.add(durationSpinner);
        specificOptionsPanel.add(timedPanel, BombType.TIMED.toString());

        // Smoke bomb options
        JPanel smokePanel = new JPanel();
        JSpinner radiusSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
        smokePanel.add(new JLabel("Radius (meters):"));
        smokePanel.add(radiusSpinner);
        specificOptionsPanel.add(smokePanel, BombType.SMOKE.toString());

        // Remote bomb options
        JPanel remotePanel = new JPanel();
        JTextField frequencyField = new JTextField(10);
        remotePanel.add(new JLabel("Frequency:"));
        remotePanel.add(frequencyField);
        specificOptionsPanel.add(remotePanel, BombType.REMOTE.toString());
    }

    private void addBomb() {
        try {
            String name = nameField.getText().trim();
            Color color = colorChooser.getColor();
            BombType type = (BombType) typeComboBox.getSelectedItem();

            Bomb bomb = switch (type) {
                case TIMED -> createTimedBomb(name, color);
                case SMOKE -> createSmokeBomb(name, color);
                case REMOTE -> createRemoteBomb(name, color);
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

    private TimedBomb createTimedBomb(String name, Color color) {
        JSpinner durationSpinner = (JSpinner) ((JPanel) specificOptionsPanel.getComponent(0)).getComponent(1);
        int duration = (Integer) durationSpinner.getValue();
        return new TimedBomb(name, color, Duration.ofSeconds(duration));
    }

    private SmokeBomb createSmokeBomb(String name, Color color) {
        JSpinner radiusSpinner = (JSpinner) ((JPanel) specificOptionsPanel.getComponent(1)).getComponent(1);
        int radius = (Integer) radiusSpinner.getValue();
        return new SmokeBomb(name, color, Distance.ofMeters(radius));
    }

    private RemoteBomb createRemoteBomb(String name, Color color) {
        JTextField frequencyField = (JTextField) ((JPanel) specificOptionsPanel.getComponent(2)).getComponent(1);
        String frequency = frequencyField.getText().trim();
        return new RemoteBomb(name, color, frequency);
    }
}
