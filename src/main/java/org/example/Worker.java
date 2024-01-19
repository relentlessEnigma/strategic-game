package org.example;

import java.util.List;
import java.util.Random;

class Worker {
    private String name;
    private boolean isOccupied;
    private ResourceAmount resourceConsumption;
    private String currentMission;

    public Worker(String name) {
        this.name = name;
        this.isOccupied = false;
        this.resourceConsumption = new ResourceAmount(ResourceType.FOOD, 50);
    }

    public void searchResources(ResourceType resourceTypeToLook, List<ResourceAmount> resourcesList) {
        currentMission = String.format("Procurar Recursos: %s\n", resourceTypeToLook.name());
        new SearchResourcesThread(new Resource(resourceTypeToLook), resourcesList, this).start();
    }

    /**
     * This sends an available worker to make a Construction or Update one
     * This will run in a separeted thread
     *
     * @param constructionProcess
     * @param construction
     */
    public void makeConstruction(ConstructionProcess constructionProcess, Building construction) {
        currentMission = String.format("%s de Edifício: %s", constructionProcess.process, construction.getConstructionTypeName());
        if(constructionProcess == ConstructionProcess.CREATION) {
            new ConstructionBuildingThread(construction, this).start();
        } else {
            new ConstructionUpdatingThread(construction, this).start();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentMission() {
        return currentMission;
    }

    public void setCurrentMission(String currentMission) {
        this.currentMission = currentMission;
    }

    public String getStringWithName() {
        return String.format("Worker %s", name);
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public ResourceAmount getResourceConsumption() {
        return resourceConsumption;
    }

    public void setResourceConsumption(ResourceAmount resourceConsumption) {
        this.resourceConsumption = resourceConsumption;
    }
}
