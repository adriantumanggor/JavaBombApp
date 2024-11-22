package ui.manager;

import domain.bomb.TimedBomb;
import domain.value.Distance;

public interface IDialogManager {
    void showAddBombDialog();
    void showEditBombDialog();
    void activateSelectedBomb();
    void deactivateSelectedBomb();
    void explodeSelectedBomb();
    void showCountdownDialog(TimedBomb timedBomb, String bombId, int seconds);
    void showSmokeRadiusDialog(String bombId, int radius);
    void showRemoteBombDialog(String bombId, String location);
}