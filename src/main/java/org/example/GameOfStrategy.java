package org.example;

import java.util.*;

public class GameOfStrategy {
    private static Scanner scanner = new Scanner(System.in);

    public void start() {
        Player player = createPlayer();
        ConsoleUtils.clearConsole();
        startGame(player);
    }

    private Player createPlayer() {
        System.out.println("Escolhe um nome para a tua aldeia: ");

        String farmName = scanner.nextLine();
        Player player = new Player(farmName);

        player.addResource(ResourceType.POPULATION, 3);
        player.addResource(ResourceType.GOLD, 50);
        player.addResource(ResourceType.FOOD, 200);
        player.addResource(ResourceType.WATER, 100);
        player.addResource(ResourceType.WOOD, 150);
        player.addResource(ResourceType.SILVER, 0);
        player.addResource(ResourceType.IRON, 0);
        player.addResource(ResourceType.GRAPES, 0);

        player.addEmployee(new Worker("João"));
        player.addEmployee(new Worker("Maria"));
        player.addEmployee(new Worker("Pedro"));

        player.setLevel(1);

        return player;
    }

    private void startGame(Player player) {

        while (true) {
            try {
                Thread.sleep(500); // Needed so it wont overlap other console outputs that may be there running
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            showMenu(player);
            int choice = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            ConsoleUtils.clearConsole();
            player.showResourcesHeader();

            switch (choice) {
                case 1:
                    displayResourcesAvailableToSearchFor(player);
                    break;
                case 2:
                    displayBuildingTypes(player);
                    break;
                case 0:
                    System.out.println("Obrigado por jogar!");
                    System.exit(0);
                default:
                    System.out.println("Escolha inválida. Tenta novamente.");
            }
        }
    }

    private void showMenu(Player player) {
        player.showResourcesHeader();
        System.out.println("Menu:");
        System.out.println("1. Procurar por recursos");
        System.out.println("2. Construir/Atualizar edifícios");
        System.out.println("3. Ver status");
        System.out.println("0. Sair do jogo");
        System.out.print("Escolhe uma opção: ");
    }

    private void displayResourcesAvailableToSearchFor(Player player) {
        System.out.println("Lista de Recursos:");

        int index = 1;
        for(ResourceType resource : ResourceType.values()) {
            System.out.printf("%d. %s\n", index, resource.name);
            index++;
        }
        int input = scanner.nextInt();
        if(player.getWorkerAvailable() != null) {
            player.sendWorkersToSearchJob(ResourceType.values()[input-1]);
        }
    }

    private void displayBuildingTypes(Player player) {
        System.out.println("Lista de Edificios:");

        int index = 1;
        Map<Integer, String> typeIndexMap = new HashMap<>();
        for (String constructionType : player.getAvailableConstructionTypes()) {
            System.out.println(index + ". " + constructionType);
            typeIndexMap.put(index++, constructionType);
        }

        System.out.println("Escolhe um tipo de edificio: ");
        int typeOption = scanner.nextInt();
        scanner.nextLine();

        ConsoleUtils.clearConsole();
        player.showResourcesHeader();

        if (typeOption > 0 && typeOption < index) {
            String selectedType = typeIndexMap.get(typeOption);
            displayBuildingsOfType(player, selectedType);
        } else {
            System.out.println("Escolha inválida. Tenta novamente.");
        }
        player.checkForNewEraConditions();
    }

    private void displayBuildingsOfType(Player player, String selectedType) {
        System.out.printf("Lista de Edificios %s:", selectedType);
        List<Building> playerBuildingsOfType = player.getBuildingsFromConstructionName(selectedType);
        int index = 1;
        for (Building building : playerBuildingsOfType) {
            System.out.print(index + ". ");
            building.showDetails();
            index++;
            if (!player.checkIfBuildingAmountHasReached(building)) {
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
            processSelectedBuilding(player, selectedBuilding);
        } else if (isCreatingANewBuilding) {
            Building newBuilding = new Building(false, Objects.requireNonNull(ConstructionType.getEnumFromConstant(selectedType)));
            processNewBuilding(player, newBuilding);
        } else {
            System.out.println("Escolha inválida. Tenta novamente.");
        }
    }

    private void processSelectedBuilding(Player player, Building selectedBuilding) {
        if (player.checkIfPlayerHasEnoughResources(selectedBuilding)) {
            if (player.isFirstTimeBuilding(selectedBuilding)) {
                if (player.getWorkerAvailable() != null) {
                    player.sendWorkersToConstructionJob(ConstructionProcess.CREATION, selectedBuilding);
                }
            } else {
                if (player.getWorkerAvailable() != null) {
                    player.sendWorkersToConstructionJob(ConstructionProcess.UPDATE, selectedBuilding);
                }
            }
        }
    }

    private void processNewBuilding(Player player, Building newBuilding) {
        if (player.checkIfPlayerHasEnoughResources(newBuilding)) {
            player.addNewBuilding(newBuilding);
            player.sendWorkersToConstructionJob(ConstructionProcess.CREATION, newBuilding);
        }
    }
}
