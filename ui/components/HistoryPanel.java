package ui.components;

import domain.history.ExplosionRecord;
import service.IBombService;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class HistoryPanel extends JPanel {
    private final IBombService bombService;
    private final JList<ExplosionRecord> historyList;
    private final DefaultListModel<ExplosionRecord> historyListModel;

    public HistoryPanel(IBombService bombService) {
        this.bombService = Objects.requireNonNull(bombService);

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Explosion History"));
        setPreferredSize(new Dimension(300, 400));

        historyListModel = new DefaultListModel<>();
        historyList = new JList<>(historyListModel);
        historyList.setCellRenderer(new HistoryListCellRenderer());

        add(new JScrollPane(historyList), BorderLayout.CENTER);

        updateDisplay();
    }

    public void updateDisplay() {
        SwingUtilities.invokeLater(() -> {
            historyListModel.clear();
            bombService.getExplosionHistory().forEach(historyListModel::addElement);
        });
    }

}