package ui.manager;

public interface IDialogManager {
    void showAddBombDialog();
    void showEditBombDialog();
    void activateSelectedBomb();
    void deactivateSelectedBomb();
    void explodeSelectedBomb();
    void showCountdownDialog(String bombId, long seconds);
}