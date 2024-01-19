package org.example;

import java.util.*;
import java.util.stream.Collectors;

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

        player.addResource(Resource.POPULATION, 3);
        player.addResource(Resource.GOLD, 50);
        player.addResource(Resource.FOOD, 200);
        player.addResource(Resource.WATER, 100);
        player.addResource(Resource.WOOD, 150);
        player.addResource(Resource.SILVER, 0);
        player.addResource(Resource.IRON, 0);
        player.addResource(Resource.GRAPES, 0);

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
                    player.searchForResources(Resource.WOOD);
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
        System.out.println("Lista de Edificios " + selectedType + ":");
        List<Building> playerBuildingsOfType = player.getBuildingsFromConstructionName(selectedType);
        int index = 1;
        for (Building building : playerBuildingsOfType) {
            System.out.print(index + ". ");
            building.showDetails();
            index++;
            if (!player.checkIfBuildingAmountHasReached(building)) {
                System.out.println(index + ". Create a new " + building.getConstructionTypeName());
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
                if (player.haveWorkersAvailable()) {
                    player.sendWorkersToBuild(ConstructionProcess.CREATION, selectedBuilding);
                }
            } else {
                if (player.haveWorkersAvailable()) {
                    player.sendWorkersToBuild(ConstructionProcess.UPDATE, selectedBuilding);
                }
            }
        }
    }

    private void processNewBuilding(Player player, Building newBuilding) {
        if (player.checkIfPlayerHasEnoughResources(newBuilding)) {
            player.addNewBuilding(newBuilding);
            player.sendWorkersToBuild(ConstructionProcess.CREATION, newBuilding);
        }
    }
}
