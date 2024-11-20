package ui.manager;

import domain.bomb.TimedBomb;

public interface IDialogManager {
    void showAddBombDialog();
    void showEditBombDialog();
    void activateSelectedBomb();
    void deactivateSelectedBomb();
    void explodeSelectedBomb();
    void showCountdownDialog(TimedBomb timedBomb, String bombId, int seconds);
}