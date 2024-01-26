package org.hsh.games.aoe;

import org.hsh.games.aoe.entities.Difficulty;
import org.hsh.games.aoe.entities.ResourceType;

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
        this.name = resourceType.getDescription();
        this.hardToGet = resourceType.getHardToGet();
        this.timeLimitForSearch = resourceType.getTimeLimitForSearch();
        this.amountMaxToBeFound = resourceType.getAmountMaxToBeFound();
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

    public void search(List<ResourceAmount> playerResources, String workerName, String currentMission) {
        Random random = new Random();
        int lowestSearchTime = random.nextInt(ThreadUtils.toMinutes(1), timeLimitForSearch/2);
        int totalSearchTime = random.nextInt(lowestSearchTime, timeLimitForSearch);
        int amountMax = amountMaxToBeFound/2;
        if(amountMax < 2) amountMax = 3;
        int lowestAmountToBeFound = random.nextInt(1, amountMax);

        System.out.printf("O %s começou a tarefa de %s\nTermina dentro de %d minutos.\n", workerName, currentMission, totalSearchTime/(60*1000));

        try {
            Thread.sleep(totalSearchTime);
            ResourceAmount resourceFound = new ResourceAmount(type, random.nextInt(lowestAmountToBeFound, amountMaxToBeFound));
            addToPlayerResources(playerResources, resourceFound);
            System.out.printf("%s voltou para casa com %d de %s\n", workerName, resourceFound.getAmount(), resourceFound.getResource().getDescription());
        } catch (java.lang.InterruptedException e) {
            System.out.println("O trabalhador aleijou-se enquanto procurava resources!!");
        }
    }

    public void addToPlayerResources(List<ResourceAmount> playerResources, ResourceAmount resourcesToAdd) {
        for(ResourceAmount x : playerResources) {
            if(x.getResource().equals(resourcesToAdd.getResource())) {
                x.setAmount(x.getAmount() + resourcesToAdd.getAmount());
            }
        }
    }
}