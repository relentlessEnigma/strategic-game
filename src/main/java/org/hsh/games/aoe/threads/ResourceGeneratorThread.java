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
        this.sleepDuration = building.getResourceProduction().stream()
                .mapToLong(resourceAmount -> resourceAmount.getResource().getTimeLimitForSearch())
                .summaryStatistics().getMax();
        this.playerResources = playerResources;
        this.playerWorkersList = playerWorkersList;
    }

    @Override
    public void run() {
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
                    }
                    playerResources.stream()
                            .filter(playerResource -> playerResource.getResource().equals(resourceAmount.getResource()))
                            .forEach(playerResource -> playerResource.setAmount(playerResource.getAmount() + resourceAmount.getAmount()));
                });
            }
        }
    }
}