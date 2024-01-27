package org.hsh.games.aoe;

import org.hsh.games.aoe.entities.*;
import java.util.*;
import java.util.stream.Collectors;

class PlayerService {
    private Player player;
    private List<ResourceAmount> resources = new ArrayList<>();
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

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public List<ResourceAmount> getResources() {
        return resources;
    }

    public void setResources(List<ResourceAmount> resources) {
        this.resources = resources;
    }

    public void addWorker(Worker worker) {
        for(ResourceAmount playerResource : resources) {
            if(playerResource.getResource() == ResourceType.POPULATION) {
                if(workers.size() < playerResource.getAmount()) {
                    workers.add(worker);
                }
                System.out.println("Chegaste ao limite de trabalhadores!");
            }
        }
    }

    public void showResourcesHeader() {
        System.out.println("====================================================================================================================");
        System.out.println(player.getEraAge().getEraName());
        for (ResourceAmount entry : resources) {
            System.out.printf("%s: %d", entry.getResource().getDescription(), entry.getAmount());
            System.out.print("   *   ");
        }
        System.out.println();
        System.out.println("====================================================================================================================");
    }

    public EraAge getEraAge() {
        return player.getEraAge();
    }

    public void setEraAge(EraAge eraAge) {
        this.player.setEraAge(eraAge);
    }

    public void setLevel(int level) {
        player.setEraAge(EraAge.getByLevel(level));
        System.out.println("Descoberta Uma Nova ERA! A Era da " + player.getEraAge().getEraName());

        try {
            Thread.sleep(1500);
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

        resources.addAll(availableResourceTypes.stream()
                .map(resourceType -> new ResourceAmount(resourceType, resourceType.getInitialOffer()))
                .toList());
    }

    public void addResource(ResourceType resourceType, int amount) {
        Optional<ResourceAmount> optionalResource = resources.stream()
                .filter(resource -> resource.getResource() == resourceType)
                .findFirst();

        if (optionalResource.isPresent()) {
            ResourceAmount resource = optionalResource.get();
            resource.setAmount(resource.getAmount() + amount);
        } else {
            resources.add(new ResourceAmount(resourceType, amount));
        }
    }

    public List<Building> getBuildingList() {
        return buildingList;
    }

    public void setBuildingList(List<Building> buildingList) {
        this.buildingList = buildingList;
    }

    public List<Worker> getEmployees() {
        return workers;
    }

    public void setEmployees(List<Worker> workers) {
        this.workers = workers;
    }

    public boolean checkIfPlayerHasEnoughResources(Building building) {
        List<ResourceAmount> requiredResources = building.getResourceCost();
        Boolean hasResourcesAvailable = false;

        for (ResourceAmount buildingResource : requiredResources) {
            for (ResourceAmount playerResource : resources) {
                if(playerResource.getResource() == buildingResource.getResource()) {
                    return playerResource.getAmount() >= buildingResource.getAmount();
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
        getWorkerAvailable().makeConstruction(process, construction, resources, workers);
    }

    public void sendWorkersToSearchJob(ResourceType resourcesToSearch) {
        getWorkerAvailable().searchResources(resourcesToSearch, resources);
    }

    private void prepareNeededResources(Building construction) {
        List<ResourceAmount> requiredResources = construction.getResourceCost();

        for (ResourceAmount buildingResource : requiredResources) {
            for (ResourceAmount playerResource : resources) {
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
            System.out.println("Todos os seus funcionários estão ocupados com tarefas!");
            System.out.println("Crie mais funcionários.");
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
