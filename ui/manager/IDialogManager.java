package ui.manager;

import domain.value.*;;

public interface IDialogManager {
    void showAddBombDialog();
    void showEditBombDialog();
    void activateSelectedBomb();
    void deactivateSelectedBomb();
    void explodeSelectedBomb();
    void showCountdownDialog(String bombId, int seconds);
}