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

    public void search(List<ResourceAmount> playerResources, String workerName, String currentMission) {
        Random random = new Random();
        int lowestSearchTime = random.nextInt(ThreadUtils.toMilliseconds(1), timeLimitForSearch/2);
        int totalSearchTime = random.nextInt(lowestSearchTime, timeLimitForSearch);
        int amountMax = amountMaxToBeFound/2;
        if(amountMax < 2) amountMax = 3;
        int lowestAmountToBeFound = random.nextInt(1, amountMax);

        System.out.printf("O %s começou a tarefa de %s\nTermina dentro de %d minutos.\n", workerName, currentMission, ThreadUtils.toMinutes(totalSearchTime));

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
