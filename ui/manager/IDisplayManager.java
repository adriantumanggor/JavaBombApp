package ui.manager;

import ui.components.BombListPanel;
import ui.components.HistoryPanel;

public interface IDisplayManager {
    BombListPanel getBombListPanel();
    HistoryPanel getHistoryPanel();
    void refreshDisplay();
}