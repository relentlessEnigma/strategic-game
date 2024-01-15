package org.example;

import java.util.*;

class Player {
    private String farmName;
    private EraAge eraAge;
    private List<ResourceAmount> resources = new ArrayList<>();
    private List<Worker> workers = new ArrayList<>();
    private List<Building> buildingList = new ArrayList<>();

    public Player(String farmName) {
        this.farmName = farmName;
    }

    public void addResource(Resource resource, int amount) {
        resources.add(new ResourceAmount(resource, amount));
    }

    public void addEmployee(Worker worker) {
        workers.add(worker);
    }

    public void showResourcesHeader() {
        System.out.println("====================================================================================================================");
        System.out.println("Era do Jogador: " + eraAge.getEraName());
        for (ResourceAmount entry : resources) {
            System.out.printf("%s: %d", entry.getResource().name(), entry.getAmount());
            System.out.print("   *   ");
        }
        System.out.println();
        System.out.println("====================================================================================================================");
    }

    public void searchForResources(Resource resourceToFind) {
        Optional<Worker> worker = workers.stream().filter(x -> !x.isOccupied()).findFirst();

        if (worker.isPresent()) {
            List<ResourceAmount> resourcesFound = worker.get().searchResources(resourceToFind);

            if (!resourcesFound.isEmpty()) {
                Resource resourceFound = resourcesFound.iterator().next().getResource();
                int quantity = resourcesFound.iterator().next().getAmount();

                for(ResourceAmount x : resources) {
                    if(x.getResource().name().equals(resourceFound.name())) {
                        x.setAmount(x.getAmount() + quantity);
                    }
                }

                System.out.println("Recurso encontrado: " + resourceFound + ", Quantidade: " + quantity);
            } else {
                System.out.println("Nenhum recurso encontrado.");
            }
        } else {
            System.out.println("Todos os seus funcionários estão ocupados com tarefas!");
            workers.forEach(w -> System.out.println(w.getName() + " atualmente em " +w.getCurrentMission()));
            System.out.println("Crie mais funcionários.");
        }
    }

    public void setLevel(int level) {
        this.eraAge = EraAge.setByLevel(level);
        assert eraAge != null;

        System.out.println("Descoberta Uma Nova ERA! A Era da " + eraAge.getEraName());
        List<ConstructionType> x = AvailableBuildings.valueOf("LEVEL_" + eraAge.getLevel()).getConstructionsList();
        for(ConstructionType z : x) {
            buildingList.add(
                    new Building(false, z)
            );
        }
    }

    public List<ResourceAmount> getResources() {
        return resources;
    }

    public void setResources(List<ResourceAmount> resources) {
        this.resources = resources;
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

        for (ResourceAmount buildingResource : requiredResources) {
            for (ResourceAmount playerResource : resources) {
                if(playerResource.getResource() == buildingResource.getResource()) {
                    if(playerResource.getAmount() >= buildingResource.getAmount()) {
                        return true;
                    }
                }
            }
        }

        System.out.println("Não tens recursos suficientes!");
        return false;
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

    public void sendWorkersToBuild(ConstructionProcess process, Building construction) {
        Optional<Worker> availableWorker = this.workers.stream().filter(x -> !x.isOccupied()).findFirst();
        prepareNeededResources(construction);
        availableWorker.ifPresent(worker -> worker.makeConstruction(process, construction));
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

    public boolean haveWorkersAvailable() {
        Optional<Worker> availableWorker = this.workers.stream().filter(x -> !x.isOccupied()).findFirst();
        return availableWorker.isPresent();
    }

    public void checkForNewEraConditions() {
        int nextLevel = eraAge.getNextLevel().getLevel();
        if(buildingList.stream().allMatch(x -> x.getLevel() == nextLevel-1)) {
            setLevel(nextLevel);
        }
    }
}
