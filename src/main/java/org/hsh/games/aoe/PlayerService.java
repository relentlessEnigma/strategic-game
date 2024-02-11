package org.hsh.games.aoe;

import org.hsh.games.aoe.entities.*;
import org.hsh.games.aoe.threads.ResourceConsumptionThread;

import java.util.*;
import java.util.stream.Collectors;

class PlayerService {
    private Player player;
    private List<ResourceAmount> playerResources = new ArrayList<>();
    private List<Worker> workers = new ArrayList<>();
    private List<Building> buildingList = new ArrayList<>();

    public PlayerService(Player player) {
        this.player = player;
        this.setLevel(1);
        addWorker(new Worker("worker1"));
        addWorker(new Worker("worker2"));
        addWorker(new Worker("worker3"));
    }

    public Player getPlayer() {
        return player;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public List<ResourceAmount> getPlayerResources() {
        return playerResources;
    }

    public void setPlayerResources(List<ResourceAmount> playerResources) {
        this.playerResources = playerResources;
    }

    public void addWorker(Worker worker) {
        for(ResourceAmount playerResource : playerResources) {
            if(playerResource.getResource() == ResourceType.POPULATION) {
                if(workers.size() < playerResource.getAmount()) {
                    workers.add(worker);
                    startConsumptionThread(worker);
                } else {
                    System.out.println("Chegaste ao limite de trabalhadores!");
                }
            }
        }
    }

    public void startConsumptionThread(Worker worker) {
        ResourceConsumptionThread consumptionThread = new ResourceConsumptionThread(worker, playerResources);
        consumptionThread.start();
        consumptionThread.setOnThreadInterruptedListener(() -> removeWorker(worker));
    }

    private void removeWorker(Worker worker) {
        workers.remove(worker);
        Optional<ResourceAmount> populationResource = playerResources.stream()
                .filter(resource -> resource.getResource() == ResourceType.POPULATION)
                .findFirst();
        populationResource.ifPresent(resource -> {
            int populationAmount = resource.getAmount();
            if (populationAmount > 0) {
                resource.setAmount(populationAmount - 1);
            }
        });
        System.out.println("Worker " + worker.getName() + " has died.");
    }



    public void showResourcesHeader() {
        System.out.println("====================================================================================================================");
        System.out.println(player.getEraAge().getEraName());
        for (ResourceAmount entry : playerResources) {

            if(entry.getResource() == ResourceType.POPULATION) {
                System.out.printf("%s: ", entry.getResource().getDescription());
                System.out.print(workers.stream().filter(worker -> !worker.isOccupied()).count() + "/" + workers.size());
            } else {
                System.out.printf("%s: %d", entry.getResource().getDescription(), entry.getAmount());
            }

            System.out.print("   *   ");
        }
        System.out.println();
        System.out.println("====================================================================================================================");
    }

    /**
     * Check if the player is eligible for a new era
     * @return true if the player is eligible for a new era
     * @return false if the player is not eligible for a new era
     */
    public boolean isPlayerEligibleForNewEra() {
        return buildingList.stream().allMatch(building ->
                player.getEraAge()
                        .getRequirementsForNextLevel()
                        .get(ConstructionType.getEnumFromConstant(building.getConstructionTypeName())) <= building.getLevel());
    }

    public void setLevel(int level) {
        player.setEraAge(EraAge.getByLevel(level));
        System.out.println("Descoberta Uma Nova ERA! A Era da " + player.getEraAge().getEraName());

        try {
            Thread.sleep(ApplicationConstants.TIME_TO_SHOW_MESSAGE);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        BuildingAndResourceAvailabilityPerLevel availability = BuildingAndResourceAvailabilityPerLevel
                .getByLevel(player.getEraAge().getLevel());
        
        List<ConstructionType> availableConstructions = availability.getAvailableConstructions();
        List<ResourceType> availableResourceTypes = availability.getAvailableResources();

        buildingList.addAll(availableConstructions.stream()
                .map(constructionType -> new Building(false, constructionType))
                .toList());

        playerResources.addAll(availableResourceTypes.stream()
                .map(resourceType -> new ResourceAmount(resourceType, resourceType.getInitialOffer()))
                .toList());
    }

    public void addResource(ResourceType resourceType, int amount) {
        Optional<ResourceAmount> optionalResource = playerResources.stream()
                .filter(resource -> resource.getResource() == resourceType)
                .findFirst();

        if (optionalResource.isPresent()) {
            ResourceAmount resource = optionalResource.get();
            resource.setAmount(resource.getAmount() + amount);
        } else {
            playerResources.add(new ResourceAmount(resourceType, amount));
        }
    }

    public List<Building> getBuildingList() {
        return buildingList;
    }

    public boolean checkIfPlayerHasEnoughResources(Building building) {
        List<ResourceAmount> requiredResources = building.getResourceCost();
        boolean hasResourcesAvailable = false;

        for (ResourceAmount buildingResource : requiredResources) {
            for (ResourceAmount playerResource : playerResources) {
                if(playerResource.getResource() == buildingResource.getResource()) {
                    hasResourcesAvailable = playerResource.getAmount() >= buildingResource.getAmount();
                    if(!hasResourcesAvailable) return false;
                }
            }
        }

        return hasResourcesAvailable;
    }

    public void addNewBuilding(Building newBuilding) {
        buildingList.add(newBuilding);
    }

    public Boolean isFirstTimeBuilding(Building selectedBuilding) {
        return buildingList.contains(selectedBuilding) && !selectedBuilding.getBuilded();
    }

    public Boolean checkIfBuildingAmountHasReached(Building building) {
        int constructionsAllowed = building.getAmountConstructionsAllowed();
        long constructedCount = getBuildingList().stream()
                .filter(b -> b.getConstructionTypeName().equals(building.getConstructionTypeName()) && b.getBuilded())
                .count();

        return constructedCount >= constructionsAllowed;
    }

    public Boolean checkIfBuildingHasReachedItsMaximLevel(Building building) {
        return building.getLevel() >= building.getMaxLevel();
    }

    public void sendWorkersToConstructionJob(ConstructionProcess process, Building construction) {
        prepareNeededResources(construction);
        getWorkerAvailable().makeConstruction(process, construction, playerResources, workers);
    }

    public void sendWorkersToSearchJob(ResourceType resourcesToSearch) {
        getWorkerAvailable().searchResources(resourcesToSearch, playerResources);
    }

    private void prepareNeededResources(Building construction) {
        List<ResourceAmount> requiredResources = construction.getResourceCost();

        for (ResourceAmount buildingResource : requiredResources) {
            for (ResourceAmount playerResource : playerResources) {
                if(playerResource.getResource() == buildingResource.getResource()) {
                    playerResource.setAmount(playerResource.getAmount() - buildingResource.getAmount());
                }
            }
        }
    }

    public Worker getWorkerAvailable() {
        Optional<Worker> worker = workers.stream().filter(x -> !x.isOccupied()).findFirst();
        if(worker.isPresent()) {
            return worker.get();
        } else {
            System.out.println("Todos os seus trabalhadores estão ocupados com tarefas!");
            System.out.println("Crie mais trabalhadores.");
            return null;
        }
    }

    public void checkForNewEraConditions() {
        int nextLevel = player.getEraAge().getNextLevel().getLevel();
        if(buildingList.stream().allMatch(x -> x.getLevel() == nextLevel-1)) {
            setLevel(nextLevel);
        }
    }

    public Set<String> getAvailableConstructionTypes() {
        return buildingList.stream()
                .filter(building -> !checkIfBuildingAmountHasReached(building) && !checkIfBuildingHasReachedItsMaximLevel(building))
                .map(Building::getConstructionTypeName)
                .collect(Collectors.toSet());
    }

    public List<Building> getBuildingsFromConstructionName(String constructionName) {
        return getBuildingList().stream()
                .filter(building -> building.getConstructionTypeName().equals(constructionName))
                .toList();
    }
}
