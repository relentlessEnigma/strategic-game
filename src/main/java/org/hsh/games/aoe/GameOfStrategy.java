package org.hsh.games.aoe;

import org.hsh.games.aoe.entities.BuildingAndResourceAvailabilityPerLevel;
import org.hsh.games.aoe.entities.ConstructionType;
import org.hsh.games.aoe.entities.Player;
import org.hsh.games.aoe.entities.ResourceType;
import java.util.*;

public class GameOfStrategy {
    private static Scanner scanner = new Scanner(System.in);

    public void start() {
        PlayerService playerService = createPlayer();
        ConsoleUtils.clearConsole();
        startGame(playerService);
    }

    private PlayerService createPlayer() {
        System.out.println("Escolhe um nome para a tua aldeia: ");

        String farmName = scanner.nextLine();
        PlayerService playerService = new PlayerService(new Player(farmName));
        return playerService;
    }

    private void startGame(PlayerService playerService) {

        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            showMenu(playerService);
            int choice = scanner.nextInt();
            scanner.nextLine();

            ConsoleUtils.clearConsole();
            playerService.showResourcesHeader();

            switch (choice) {
                case 1:
                    displayResourcesAvailableToSearchFor(playerService);
                    break;
                case 2:
                    displayBuildingTypes(playerService);
                    break;
                case 0:
                    System.out.println("Obrigado por jogar!");
                    System.exit(0);
                default:
                    System.out.println("Escolha inválida. Tenta novamente.");
            }
        }
    }

    private void showMenu(PlayerService playerService) {
        playerService.showResourcesHeader();
        System.out.println("Menu:");
        System.out.println("1. Procurar por recursos");
        System.out.println("2. Construir/Atualizar edifícios");
        System.out.println("3. Ver status");
        System.out.println("0. Sair do jogo");
        System.out.print("Escolhe uma opção: ");
    }

    private void displayResourcesAvailableToSearchFor(PlayerService playerService) {
        System.out.println("Lista de Recursos:");

        int index = 1;
        List<ResourceType> availableResources = BuildingAndResourceAvailabilityPerLevel.getByLevel(playerService.getPlayer().getEraAge().getLevel()).getAvailableResources();
        for(ResourceType ct : availableResources) {
            System.out.printf("%d. %s\n", index, ct.getDescription());
            index++;
        }

        int input = scanner.nextInt();
        if(playerService.getWorkerAvailable() != null) {
            playerService.sendWorkersToSearchJob(availableResources.get(input-1));
        }
    }

    private void displayBuildingTypes(PlayerService playerService) {
        System.out.println("Lista de Edificios:");
        List<String> constructionTypes = new ArrayList<>(playerService.getAvailableConstructionTypes());
        for (int i = 0; i < constructionTypes.size(); i++) {
            System.out.println((i + 1) + ". " + constructionTypes.get(i));
        }

        int typeOption = scanner.nextInt();
        scanner.nextLine();

        ConsoleUtils.clearConsole();
        playerService.showResourcesHeader();

        if (typeOption > 0 && typeOption <= constructionTypes.size()) {
            displayBuildingsOfType(playerService, constructionTypes.get(typeOption - 1));
        } else {
            System.out.println("Escolha inválida. Tenta novamente.");
        }
        playerService.checkForNewEraConditions();
    }

    private void displayBuildingsOfType(PlayerService playerService, String selectedType) {
        List<Building> buildings = playerService.getBuildingsFromConstructionName(selectedType);
        for (int i = 0; i < buildings.size(); i++) {
            System.out.printf("%d. ", i + 1);
            buildings.get(i).showDetails();
            if (!playerService.checkIfBuildingAmountHasReached(buildings.get(i))) {
                System.out.printf("%d Create a new %s\n", i + 2, buildings.get(i).getConstructionTypeName());
            }
        }

        int option = getOptionFromUser();
        if (option > 0 && option <= buildings.size()) {
            processSelectedBuilding(playerService, buildings.get(option - 1));
        } else if (option == buildings.size() + 1) {
            processNewBuilding(playerService, new Building(false, ConstructionType.getEnumFromConstant(selectedType)));
        } else {
            System.out.println("Invalid choice. Try again.");
        }
    }

    private int getOptionFromUser() {
        System.out.println("Choose a building to construct or update: ");
        int option = scanner.nextInt();
        scanner.nextLine();
        return option;
    }

    private void processSelectedBuilding(PlayerService playerService, Building building) {
        if (playerService.checkIfPlayerHasEnoughResources(building)) {
            ConstructionProcess process = playerService.isFirstTimeBuilding(building) ? ConstructionProcess.CREATION : ConstructionProcess.UPDATE;
            if (playerService.getWorkerAvailable() != null) {
                playerService.sendWorkersToConstructionJob(process, building);
            }
        } else {
            System.out.println("Não tens recursos suficientes!");
        }
    }

    private void processNewBuilding(PlayerService playerService, Building building) {
        if (playerService.checkIfPlayerHasEnoughResources(building)) {
            playerService.addNewBuilding(building);
            playerService.sendWorkersToConstructionJob(ConstructionProcess.CREATION, building);
        } else {
            System.out.println("Não tens recursos suficientes!");
        }
    }
}
