package application.config;

import repository.*;
import repository.impl.*;
import service.*;
import service.impl.*;
import ui.manager.*;
import ui.manager.impl.*;

public class ApplicationConfig {
    private final IBombRepository bombRepository;
    private final IHistoryRepository historyRepository;
    private final IBombService bombService;
    private final IDisplayManager displayManager;
    private final IDialogManager dialogManager;


    public ApplicationConfig() {
        this.bombRepository = new InMemoryBombRepository();
        this.historyRepository = new InMemoryHistoryRepository();
        
        this.bombService = new BombServiceImpl(bombRepository, historyRepository);

         
        this.displayManager = new DisplayManagerImpl(bombService);
        this.dialogManager = new DialogManagerImpl(bombService, displayManager);

    }

    public IBombService bombService() { return bombService; }
    public IDisplayManager displayManager() { return displayManager; }
    public IDialogManager dialogManager() { return dialogManager; }
}