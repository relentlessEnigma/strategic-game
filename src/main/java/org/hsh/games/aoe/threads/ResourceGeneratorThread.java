package org.hsh.games.aoe.threads;

import org.hsh.games.aoe.Building;
import org.hsh.games.aoe.ResourceAmount;
import org.hsh.games.aoe.entities.ResourceType;
import org.hsh.games.aoe.entities.Worker;
import java.util.List;

public class ResourceGeneratorThread extends Thread {
    private final Building building;
    private final List<ResourceAmount> playerResources;
    private long sleepDuration;
    private final List<Worker> playerWorkersList;

    public ResourceGeneratorThread(Building building, List<ResourceAmount> playerResources, List<Worker> playerWorkersList) {
        this.building = building;
        this.sleepDuration = building.getTimeBetweenProductions();
        this.playerResources = playerResources;
        this.playerWorkersList = playerWorkersList;
    }

    @Override
    public void run() {
        if(building.getResourceProduction().isEmpty()) {
            return;
        } else {
            System.out.println("A iniciar produção de recursos...");
        }
        while (!isInterrupted()) {
            try {
                Thread.sleep(sleepDuration);
            } catch (InterruptedException e) {
                return;
            }

            synchronized (this) {
                addResourcesToPlayerInventory();
            }
        }
    }

    private void addResourcesToPlayerInventory() {
        building.getResourceProduction().forEach(resourceAmount -> {
            playerResources.stream()
                    .filter(playerResource -> playerResource.getResource().equals(resourceAmount.getResource()))
                    .forEach(playerResource -> {
                        playerResource.setAmount(playerResource.getAmount() + resourceAmount.getAmount());
                        System.out.println("Recurso " + playerResource.getResource().getDescription() + " adicionado ao inventário!");
                        if(playerResource.getResource() == ResourceType.POPULATION){
                            addWorkerToPlayerWorkersList();
                        }
                    });
        });
    }

    private void addWorkerToPlayerWorkersList() {
        for(ResourceAmount playerResource : playerResources) {
            if(playerResource.getResource() == ResourceType.POPULATION) {
                if(playerWorkersList.size() < playerResource.getAmount()) {
                    playerWorkersList.add(new Worker("worker_added"));
                    System.out.println("Adinionado +1 Population");
                } else {
                    System.out.println("Chegaste ao limite de trabalhadores!");
                }
            }
        }
    }
}
