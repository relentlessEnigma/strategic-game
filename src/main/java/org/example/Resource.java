package org.example;

import java.util.List;
import java.util.Random;

public class Resource {

    ResourceType type;
    String name;
    Difficulty hardToGet;
    int timeLimitForSearch;
    int amountMaxToBeFound;
    int pricePerUnit;
    public Resource(ResourceType resourceType) {
        this.type = resourceType;
        this.name = resourceType.name;
        this.hardToGet = resourceType.hardToGet;
        this.timeLimitForSearch = resourceType.timeLimitForSearch;
        this.amountMaxToBeFound = resourceType.amountMaxToBeFound;
        this.pricePerUnit = resourceType.getPricePerUnit();
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Difficulty getHardToGet() {
        return hardToGet;
    }

    public void setHardToGet(Difficulty hardToGet) {
        this.hardToGet = hardToGet;
    }

    public int getTimeLimitForSearch() {
        return timeLimitForSearch;
    }

    public void setTimeLimitForSearch(int timeLimitForSearch) {
        this.timeLimitForSearch = timeLimitForSearch;
    }

    public int getAmountMaxToBeFound() {
        return amountMaxToBeFound;
    }

    public void setAmountMaxToBeFound(int amountMaxToBeFound) {
        this.amountMaxToBeFound = amountMaxToBeFound;
    }

    public int getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(int pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public void search(List<ResourceAmount> resourcesList, String workerName, String currentMission) {
        Random random = new Random();
        int lowestSearchTime = random.nextInt(60 * 1000, timeLimitForSearch/2);
        int totalSearchTime = random.nextInt(lowestSearchTime, timeLimitForSearch);
        int lowestAmountToBeFound = random.nextInt(1, amountMaxToBeFound/2);

        System.out.printf("O %s começou a tarefa: %s\nTermina dentro de %d minutos.\n", workerName, currentMission, totalSearchTime/(60*1000));

        try {
            Thread.sleep(totalSearchTime);
            ResourceAmount resourceFound = new ResourceAmount(type, random.nextInt(lowestAmountToBeFound, amountMaxToBeFound));
            addToPlayerResources(resourcesList, resourceFound);
            System.out.printf("%s voltou para casa com %d de %s\n", workerName, resourceFound.getAmount(), resourceFound.getResource().name());
        } catch (java.lang.InterruptedException e) {
            System.out.println("O trabalhador aleijou-se enquanto procurava resources!!");
        }
    }

    public void addToPlayerResources(List<ResourceAmount> resourcesList, ResourceAmount resourcesToAdd) {
        for(ResourceAmount x : resourcesList) {
            if(x.getResource().name().equals(resourcesToAdd.getResource().getName())) {
                x.setAmount(x.getAmount() + resourcesToAdd.getAmount());
            }
        }
    }
}
