package org.example.threads;

import org.example.ConsoleUtils;
import org.example.Resource;
import org.example.ResourceAmount;
import org.example.entities.Worker;
import java.util.List;

public class SearchResourcesThread extends Thread {

    private Resource resourceToLookFor;
    private Worker worker;
    private List<ResourceAmount> playerResources;

    public SearchResourcesThread(Resource resourceToLookFor, List<ResourceAmount> playerResources, Worker worker) {
        this.resourceToLookFor = resourceToLookFor;
        this.worker = worker;
        this.playerResources = playerResources;
    }

    @Override
    public void run() {
        worker.setOccupied(true);

        resourceToLookFor.search(playerResources, worker.getName(), worker.getCurrentMission());

        worker.setOccupied(false);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ConsoleUtils.clearConsole();
    }
}
