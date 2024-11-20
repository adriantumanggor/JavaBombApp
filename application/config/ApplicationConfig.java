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
        // Initialize repositories
        this.bombRepository = new InMemoryBombRepository();
        this.historyRepository = new InMemoryHistoryRepository();
        
        // Initialize service
        this.bombService = new BombServiceImpl(bombRepository, historyRepository);

        
        // Initialize UI managers
        this.displayManager = new DisplayManagerImpl(bombService);
        this.dialogManager = new DialogManagerImpl(bombService, displayManager);

        // ((InMemoryBombRepository) bombRepository).addListener(() -> displayManager.refreshDisplay());
    }

    public IBombService bombService() { return bombService; }
    public IDisplayManager displayManager() { return displayManager; }
    public IDialogManager dialogManager() { return dialogManager; }
}