# Java OOP Bomb Management Simulation

Welcome to the Java OOP Bomb Management Simulation repository. This repository contains resources and examples to help you understand Object-Oriented Programming (OOP) concepts in Java through a bomb management simulation application.

## Table of Contents

- [Introduction](#introduction)
- [Getting Started](#getting-started)
- [Running the Application](#running-the-application)
- [Key Concepts](#key-concepts)
- [Examples](#examples)
- [Contributing](#contributing)
- [License](#license)

## Introduction

This repository is designed to provide a comprehensive guide to OOP in Java using a bomb management simulation application. It includes explanations of key concepts, code examples, and best practices.

## Getting Started

To get started with this repository, clone it to your local machine using the following command:

```bash
git clone https://github.com/yourusername/javaoop-reff.git
```
## Compile java files
To compile all the java files run this command

```bash
javac -d out application/BombApplication.java
```
with input entry point of the application file, java compiler will automatically find and compile all the files


## Running the Application

To run the application, navigate to the `/out` directory and execute the following command:

```bash
java application.BombApplication
```

## Key Concepts

- **Classes and Objects**: Learn about the building blocks of OOP.
- **Inheritance**: Understand how classes can inherit properties and methods from other classes.
- **Polymorphism**: Explore how objects can take on multiple forms.
- **Encapsulation**: Discover how to protect the internal state of an object.
- **Abstraction**: Learn how to simplify complex systems by modeling classes appropriate to the problem.

## Examples

This section contains practical examples to illustrate OOP concepts in Java. Each example is located in the `examples` directory.

## Contributing

Contributions are welcome! Please read the [contributing guidelines](CONTRIBUTING.md) before submitting a pull request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.


```mermaid
classDiagram
    %% Application Layer
    class BombApplication {
        +main(String[] args)
    }
    
    class ApplicationConfig {
        -IBombRepository bombRepository
        -IHistoryRepository historyRepository
        -IBombService bombService
        -IDisplayManager displayManager
        -IDialogManager dialogManager
        +bombService() IBombService
        +displayManager() IDisplayManager
        +dialogManager() IDialogManager
    }

    %% Domain - Bomb Classes
    class Bomb {
        <<abstract>>
        -String id
        -String name
        -String location
        -boolean active
        -BombType type
        +getId() String
        +getName() String
        +getLocation() String
        +isActive() boolean
        +getType() BombType
        +setName(String)
        +setLocation(String)
        +activate()
        +deactivate()
        +getCurrentState() String
        +explode()* String
    }

    class TimedBomb {
        -Duration duration
        +getDuration() Duration
        +setDuration(Duration)
        +explode() String
    }

    class SmokeBomb {
        -Distance radius
        +getRadius() Distance
        +setRadius(Distance)
        +explode() String
    }

    class RemoteBomb {
        -String frequency
        +getFrequency() String
        +setFrequency(String)
        +explode() String
    }

    class BombType {
        <<enumeration>>
        TIMED
        SMOKE
        REMOTE
        -String displayName
        +getDisplayName() String
    }

    class BombState {
        -String name
        -String location
        -boolean active
        -BombType type
        +getName() String
        +getLocation() String
        +isActive() boolean
        +getType() BombType
    }

    %% Domain - Validation and Formatting
    class BombValidator {
        <<utility>>
        +validateName(String)
        +validateLocation(String)
        +validateType(BombType)
    }

    class BombStateFormatter {
        <<utility>>
        +formatCurrentState(BombState) String
    }

    %% Domain - Value Objects
    class Duration {
        <<record>>
        +int seconds
        +ofSeconds(int) Duration
    }

    class Distance {
        <<record>>
        +int meters
        +ofMeters(int) Distance
    }

    %% Domain - History
    class ExplosionRecord {
        <<record>>
        +String id
        +String details
        +BombType bombType
    }

    %% Repository Layer
    class IBombRepository {
        <<interface>>
        +save(Bomb)
        +delete(String)
        +findById(String) Optional~Bomb~
        +findAll() List~Bomb~
    }

    class IHistoryRepository {
        <<interface>>
        +saveRecord(ExplosionRecord)
        +getAllRecords() List~ExplosionRecord~
    }

    class InMemoryBombRepository {
        -Map~String,Bomb~ bombs
        +save(Bomb)
        +delete(String)
        +findById(String) Optional~Bomb~
        +findAll() List~Bomb~
    }

    class InMemoryHistoryRepository {
        -List~ExplosionRecord~ records
        +saveRecord(ExplosionRecord)
        +getAllRecords() List~ExplosionRecord~
    }

    %% Service Layer
    class IBombService {
        <<interface>>
        +addBomb(Bomb)
        +removeBomb(String)
        +activateBomb(String)
        +deactivateBomb(String)
        +explodeBomb(String)
        +getAllBombs() List~Bomb~
        +getExplosionHistory() List~ExplosionRecord~
    }

    class BombServiceImpl {
        -IBombRepository bombRepository
        -IHistoryRepository historyRepository
        +addBomb(Bomb)
        +removeBomb(String)
        +activateBomb(String)
        +deactivateBomb(String)
        +explodeBomb(String)
        +getAllBombs() List~Bomb~
        +getExplosionHistory() List~ExplosionRecord~
    }

    %% UI Layer - Main
    class BombManagementUI {
        -IBombService bombService
        -IDisplayManager displayManager
        -IDialogManager dialogManager
        -initializeUI()
    }

    %% UI Layer - Components
    class BombListPanel {
        -IBombService bombService
        -JList~Bomb~ bombList
        -DefaultListModel~Bomb~ bombListModel
        +updateDisplay()
        +getSelectedBomb() Optional~Bomb~
    }

    class HistoryPanel {
        -IBombService bombService
        -JList~ExplosionRecord~ historyList
        -DefaultListModel~ExplosionRecord~ historyListModel
        +updateDisplay()
    }

    class ButtonPanel {
        -IDialogManager dialogManager
        -initializeButtons()
    }

    class BombListCellRenderer {
        +getListCellRendererComponent() Component
    }

    class HistoryListCellRenderer {
        +getListCellRendererComponent() Component
    }

    %% UI Layer - Dialogs
    class AddBombDialog {
        -IBombService bombService
        -JTextField nameField
        -JTextField locationField
        -JComboBox~BombType~ typeComboBox
        -JPanel specificOptionsPanel
        -initializeUI()
        -addBomb()
    }

    class EditBombDialog {
        -Bomb bomb
        -JTextField nameField
        -JTextField locationField
        -JPanel specificOptionsPanel
        -initializeUI()
        -saveBomb()
    }

    class CountdownDialog {
        -JDialog dialog
        -Timer countdownTimer
        -Timer blinkTimer
        -int remainingSeconds
        -TimedBomb timedBomb
        -IBombService bombService
        +start()
    }

    class SimpleInfoDialog {
        +show(Frame, String, String)
    }

    %% UI Layer - Managers
    class IDisplayManager {
        <<interface>>
        +getBombListPanel() BombListPanel
        +getHistoryPanel() HistoryPanel
        +refreshDisplay()
    }

    class IDialogManager {
        <<interface>>
        +showAddBombDialog()
        +showEditBombDialog()
        +activateSelectedBomb()
        +deactivateSelectedBomb()
        +explodeSelectedBomb()
        +showCountdownDialog()
        +showSmokeRadiusDialog()
        +showRemoteBombDialog()
    }

    class DisplayManagerImpl {
        -BombListPanel bombListPanel
        -HistoryPanel historyPanel
        +getBombListPanel() BombListPanel
        +getHistoryPanel() HistoryPanel
        +refreshDisplay()
    }

    class DialogManagerImpl {
        -IBombService bombService
        -IDisplayManager displayManager
        -JFrame parentFrame
        +showAddBombDialog()
        +showEditBombDialog()
        +activateSelectedBomb()
        +deactivateSelectedBomb()
        +explodeSelectedBomb()
    }

    %% Relationships - Application
    BombApplication --> ApplicationConfig
    ApplicationConfig --> IBombService
    ApplicationConfig --> IDisplayManager
    ApplicationConfig --> IDialogManager
    ApplicationConfig --> IBombRepository
    ApplicationConfig --> IHistoryRepository

    %% Relationships - Domain
    Bomb <|-- TimedBomb
    Bomb <|-- SmokeBomb
    Bomb <|-- RemoteBomb
    Bomb --> BombType
    Bomb --> BombState
    Bomb ..> BombValidator
    BombState --> BombType
    BombStateFormatter ..> BombState
    TimedBomb --> Duration
    SmokeBomb --> Distance
    ExplosionRecord --> BombType

    %% Relationships - Repository
    IBombRepository <|.. InMemoryBombRepository
    IHistoryRepository <|.. InMemoryHistoryRepository
    InMemoryBombRepository ..> Bomb
    InMemoryHistoryRepository ..> ExplosionRecord

    %% Relationships - Service
    IBombService <|.. BombServiceImpl
    BombServiceImpl --> IBombRepository
    BombServiceImpl --> IHistoryRepository
    BombServiceImpl ..> Bomb
    BombServiceImpl ..> ExplosionRecord

    %% Relationships - UI Main
    BombManagementUI --> IBombService
    BombManagementUI --> IDisplayManager
    BombManagementUI --> IDialogManager

    %% Relationships - UI Components
    BombListPanel --> IBombService
    BombListPanel --> BombListCellRenderer
    HistoryPanel --> IBombService
    HistoryPanel --> HistoryListCellRenderer
    ButtonPanel --> IDialogManager

    %% Relationships - UI Dialogs
    AddBombDialog --> IBombService
    EditBombDialog --> IBombService
    EditBombDialog ..> Bomb
    CountdownDialog --> IBombService
    CountdownDialog --> TimedBomb

    %% Relationships - UI Managers
    IDisplayManager <|.. DisplayManagerImpl
    IDialogManager <|.. DialogManagerImpl
    DisplayManagerImpl --> BombListPanel
    DisplayManagerImpl --> HistoryPanel
    DialogManagerImpl --> IBombService
    DialogManagerImpl --> IDisplayManager
    DialogManagerImpl ..> AddBombDialog
    DialogManagerImpl ..> EditBombDialog
    DialogManagerImpl ..> CountdownDialog
    DialogManagerImpl ..> SimpleInfoDialog
```