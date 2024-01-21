package org.example.entities;

import org.example.Building;
import org.example.ConstructionProcess;
import org.example.Resource;
import org.example.ResourceAmount;
import org.example.threads.ConstructionBuildingThread;
import org.example.threads.ConstructionUpdatingThread;
import org.example.threads.SearchResourcesThread;
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

    public void searchResources(ResourceType resourceTypeToLook, List<ResourceAmount> playerResources) {
        currentMission = String.format("procurar recursos: %s\n", resourceTypeToLook.getDescription());
        new SearchResourcesThread(new Resource(resourceTypeToLook), playerResources, this).start();
    }

    public void makeConstruction(ConstructionProcess constructionProcess, Building construction, List<ResourceAmount> playerResources, List<Worker> playerWorkersList) {
        currentMission = String.format("%s de Edifício: %s", constructionProcess.getProcess(), construction.getConstructionTypeName());
        if(constructionProcess == ConstructionProcess.CREATION) {
            new ConstructionBuildingThread(construction, this, playerResources, playerWorkersList).start();
        } else {
            new ConstructionUpdatingThread(construction, this, playerResources).start();
        }
    }

}