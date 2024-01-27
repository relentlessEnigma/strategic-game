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
        System.out.println("A gerar recursos...");
        while (!isInterrupted()) {
            try {
                Thread.sleep(sleepDuration);
            } catch (InterruptedException e) {
                return;
            }

            synchronized (this) {
                building.getResourceProduction().forEach(resourceAmount -> {
                    if(resourceAmount.getResource() == ResourceType.POPULATION) {
                        playerWorkersList.add(new Worker("worker_added"));
                        System.out.println("Adinionado +1 Population");
                    }
                    playerResources.stream()
                            .filter(playerResource -> playerResource.getResource().equals(resourceAmount.getResource()))
                            .forEach(playerResource -> {
                                playerResource.setAmount(playerResource.getAmount() + resourceAmount.getAmount());
                                System.out.println("Recurso " + playerResource.getResource().getDescription() + " adicionado ao inventário!");
                            });
                });
            }
        }
    }
}