package org.example;

import org.example.entities.*;

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

        int index = 1;
        Map<Integer, String> typeIndexMap = new HashMap<>();
        for (String constructionType : playerService.getAvailableConstructionTypes()) {
            System.out.println(index + ". " + constructionType);
            typeIndexMap.put(index++, constructionType);
        }

        System.out.println("Escolhe um tipo de edificio: ");
        int typeOption = scanner.nextInt();
        scanner.nextLine();

        ConsoleUtils.clearConsole();
        playerService.showResourcesHeader();

        if (typeOption > 0 && typeOption < index) {
            String selectedType = typeIndexMap.get(typeOption);
            displayBuildingsOfType(playerService, selectedType);
        } else { // Limpar o buffer
            System.out.println("Escolha inválida. Tenta novamente.");
        }
        playerService.checkForNewEraConditions();
    }

    private void displayBuildingsOfType(PlayerService playerService, String selectedType) {
        System.out.printf("Lista de Edificios %s:\n", selectedType);
        List<Building> playerBuildingsOfType = playerService.getBuildingsFromConstructionName(selectedType);
        int index = 1;
        for (Building building : playerBuildingsOfType) {
            System.out.print(index + ". ");
            building.showDetails();
            index++;
            if (!playerService.checkIfBuildingAmountHasReached(building)) {
                System.out.printf("%d Create a new %s\n", index, building.getConstructionTypeName());
            }
        }

        System.out.println("Escolhe um edificio para construir ou atualizar: ");
        int buildingOption = scanner.nextInt();
        scanner.nextLine();

        boolean isUpdatingAnExistentBuilding = buildingOption > 0 && buildingOption <= playerBuildingsOfType.size();
        boolean isCreatingANewBuilding = buildingOption == playerBuildingsOfType.size() + 1;

        if (isUpdatingAnExistentBuilding) {
            Building selectedBuilding = playerBuildingsOfType.get(buildingOption - 1);
            processSelectedBuilding(playerService, selectedBuilding);
        } else if (isCreatingANewBuilding) {
            Building newBuilding = new Building(false, Objects.requireNonNull(ConstructionType.getEnumFromConstant(selectedType)));
            processNewBuilding(playerService, newBuilding);
        } else {
            System.out.println("Escolha inválida. Tenta novamente.");
        }
    }

    private void processSelectedBuilding(PlayerService playerService, Building selectedBuilding) {
        if (playerService.checkIfPlayerHasEnoughResources(selectedBuilding)) {
            if (playerService.isFirstTimeBuilding(selectedBuilding)) {
                if (playerService.getWorkerAvailable() != null) {
                    playerService.sendWorkersToConstructionJob(ConstructionProcess.CREATION, selectedBuilding);
                }
            } else {
                if (playerService.getWorkerAvailable() != null) {
                    playerService.sendWorkersToConstructionJob(ConstructionProcess.UPDATE, selectedBuilding);
                }
            }
        }
    }

    private void processNewBuilding(PlayerService playerService, Building newBuilding) {
        if (playerService.checkIfPlayerHasEnoughResources(newBuilding)) {
            playerService.addNewBuilding(newBuilding);
            playerService.sendWorkersToConstructionJob(ConstructionProcess.CREATION, newBuilding);
        }
    }
}
