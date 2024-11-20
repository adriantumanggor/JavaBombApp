package ui.components;

import service.IBombService;
import ui.manager.IDialogManager;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ButtonPanel extends JPanel {
    private final IBombService bombService;
    private final IDialogManager dialogManager;

    public ButtonPanel(IBombService bombService, IDialogManager dialogManager) {
        this.bombService = Objects.requireNonNull(bombService);
        this.dialogManager = Objects.requireNonNull(dialogManager);

        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        initializeButtons();
    }

    @SuppressWarnings("unused")
    private void initializeButtons() {
        JButton addButton = new JButton("Add Bomb");
        JButton editButton = new JButton("Edit Bomb");
        JButton activateButton = new JButton("Activate");
        JButton deactivateButton = new JButton("Deactivate");
        JButton explodeButton = new JButton("EXPLODE!");

        addButton.addActionListener(e -> dialogManager.showAddBombDialog());
        editButton.addActionListener(e -> dialogManager.showEditBombDialog());
        activateButton.addActionListener(e -> dialogManager.activateSelectedBomb());
        deactivateButton.addActionListener(e -> dialogManager.deactivateSelectedBomb());
        explodeButton.addActionListener(e -> dialogManager.explodeSelectedBomb());

        add(addButton);
        add(editButton);
        add(activateButton);
        add(deactivateButton);
        add(explodeButton);
    }
}