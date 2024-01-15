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
        this.resourceConsumption = new ResourceAmount(Resource.FOOD, 50);
    }

    public List<ResourceAmount> searchResources(Resource resourceToLook) {
        currentMission = "    Procurar Recursos: " + resourceToLook.name();
        System.out.println(getStringWithName() + " está agora em missão:\n" + currentMission);
        isOccupied = true;

        Random random = new Random();
        int lowestAmount = 10;
        int highestAmount = 100;

        int lowestSearchTime = 5000; // miliseconds
        int highestSearchTime = lowestSearchTime * 5;

        try {
            Thread.sleep(random.nextInt(lowestSearchTime, highestSearchTime));
        } catch (java.lang.InterruptedException e) {
            System.out.println("O trabalhador aleijou-se enquanto procurava resources!!");
        }
        isOccupied = false;

        return List.of(new ResourceAmount(resourceToLook, random.nextInt(lowestAmount, highestAmount)));
    }

    /**
     * This sends an available worker to make a Construction or Update one
     * This will run in a separeted thread
     *
     * @param constructionProcess
     * @param construction
     */
    public void makeConstruction(ConstructionProcess constructionProcess, Building construction) {
        currentMission = constructionProcess.process + " de Edifício: " + construction.getConstructionTypeName();
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
        return "Worker " + name;
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
