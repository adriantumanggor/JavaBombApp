package ui.manager;

import domain.bomb.TimedBomb;
import domain.value.Distance;
import service.IBombService;

public interface IDialogManager {
    void showAddBombDialog();

    void showEditBombDialog();

    void activateSelectedBomb();

    void deactivateSelectedBomb();

    void explodeSelectedBomb();

    void showCountdownDialog(IBombService bombService, TimedBomb timedBomb, String bombId, int seconds);

    void showSmokeRadiusDialog(String bombId, int radius);

    void showRemoteBombDialog(String bombId, String location);
}