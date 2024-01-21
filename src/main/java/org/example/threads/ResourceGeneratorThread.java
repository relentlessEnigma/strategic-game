package org.example.threads;

import org.example.Building;
import org.example.ResourceAmount;
import org.example.ThreadUtils;
import org.example.entities.ResourceType;
import org.example.entities.Worker;

import java.util.List;

public class ResourceGeneratorThread extends Thread {
    private Building building;
    private long sleepDuration;
    private List<ResourceAmount> playerResources;
    private List<Worker> playerWorkersList;

    public ResourceGeneratorThread(Building building, List<ResourceAmount> playerResources, List<Worker> playerWorkersList) {
        this.building = building;
        this.sleepDuration = ThreadUtils.toMinutes(1);
        this.playerResources = playerResources;
        this.playerWorkersList = playerWorkersList;
    }

    @Override
    public void run() {
        while (!interrupted()) {
            try {
                System.out.println();
                Thread.sleep(sleepDuration);
            } catch (InterruptedException e) {
                break;
            }

            synchronized (this) {
                for(ResourceAmount x : building.getResourceProduction()) {
                    if(x.getResource() == ResourceType.POPULATION) {
                        playerWorkersList.add(new Worker("woker_added"));
                    }
                }
                addToPlayerResources(playerResources, building.getResourceProduction());
                System.out.println("received resources:");
                System.out.println(building.getResourceProduction());
            }
        }
    }

    public void addToPlayerResources(List<ResourceAmount> playerResources, List<ResourceAmount> resourcesToAdd) {
        for(ResourceAmount x : playerResources) {
            for(ResourceAmount y : resourcesToAdd) {
                if(x.getResource().equals(y.getResource())) {
                    x.setAmount(x.getAmount() + y.getAmount());
                }
            }

        }
    }
}
