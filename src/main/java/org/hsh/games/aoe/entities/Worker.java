package org.hsh.games.aoe.entities;

import org.hsh.games.aoe.Building;
import org.hsh.games.aoe.ConstructionProcess;
import org.hsh.games.aoe.Resource;
import org.hsh.games.aoe.ResourceAmount;
import org.hsh.games.aoe.threads.ConstructionBuildingThread;
import org.hsh.games.aoe.threads.ConstructionUpdatingThread;
import org.hsh.games.aoe.threads.SearchResourcesThread;

import java.util.List;

public class Worker {
    private String name;
    private boolean isOccupied;
    private ResourceAmount resourceConsumption;
    private String currentMission;

    public Worker(String name) {
        this.name = name;
        this.isOccupied = false;
        this.resourceConsumption = new ResourceAmount(ResourceType.FOOD, 50);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCurrentMission() {
        return currentMission;
    }

    public void setCurrentMission(String currentMission) {
        this.currentMission = currentMission;
    }

    public String getStringWithName() {
        return String.format("Worker %s", name);
    }

    public void setResourceConsumption(ResourceAmount resourceConsumption) {
        this.resourceConsumption = resourceConsumption;
    }

    public void searchResources(ResourceType resourceType, List<ResourceAmount> playerResources) {
        currentMission = "Procurar Recursos: " + resourceType.getDescription();
        new SearchResourcesThread(new Resource(resourceType), playerResources, this).start();
    }

    public void makeConstruction(ConstructionProcess process, Building building, List<ResourceAmount> playerResources, List<Worker> workerList) {
        currentMission = process.getProcess() + " de Edifício: " + building.getConstructionTypeName();
        Thread constructionThread = process == ConstructionProcess.CREATION
                ? new ConstructionBuildingThread(building, this, playerResources, workerList)
                : new ConstructionUpdatingThread(building, this, playerResources);
        constructionThread.start();
    }

}
