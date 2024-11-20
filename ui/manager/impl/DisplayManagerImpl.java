package ui.manager.impl;

import service.IBombService;
import ui.components.*;
import ui.manager.IDisplayManager;

public class DisplayManagerImpl implements IDisplayManager {
    private final BombListPanel bombListPanel;
    private final HistoryPanel historyPanel;

    public DisplayManagerImpl(IBombService bombService) {
        this.bombListPanel = new BombListPanel(bombService);
        this.historyPanel = new HistoryPanel(bombService);
    }

    @Override
    public BombListPanel getBombListPanel() {
        return bombListPanel;
    }

    @Override
    public HistoryPanel getHistoryPanel() {
        return historyPanel;
    }

    @Override
    public void refreshDisplay() {
        bombListPanel.updateDisplay();
        historyPanel.updateDisplay();
    }
}
