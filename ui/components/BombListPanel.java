package ui.components;

import domain.bomb.Bomb;
import service.IBombService;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Optional;

public class BombListPanel extends JPanel {
    private final IBombService bombService;
    private final JList<Bomb> bombList;
    private final DefaultListModel<Bomb> bombListModel;

    public BombListPanel(IBombService bombService) {
        this.bombService = Objects.requireNonNull(bombService);
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Bombs"));

        bombListModel = new DefaultListModel<>();
        bombList = new JList<>(bombListModel);
        bombList.setCellRenderer(new BombListCellRenderer());
        
        add(new JScrollPane(bombList), BorderLayout.CENTER);
        
        updateDisplay();
    }

    public void updateDisplay() {
        SwingUtilities.invokeLater(() -> {
            bombListModel.clear();
            bombService.getAllBombs().forEach(bombListModel::addElement);
        });
    }

    public Optional<Bomb> getSelectedBomb() {
        return Optional.ofNullable(bombList.getSelectedValue());
    }
}