package org.example;

import java.util.List;
import java.util.Random;

public class SearchResourcesThread extends Thread {

    private Resource resource;
    private Worker worker;
    List<ResourceAmount> resourcesList;

    public SearchResourcesThread(Resource resource, List<ResourceAmount> resourcesList, Worker worker) {
        this.resource = resource;
        this.worker = worker;
        this.resourcesList = resourcesList;
    }

    @Override
    public void run() {
        worker.setOccupied(true);

        resource.search(resourcesList, worker.getName(), worker.getCurrentMission());

        worker.setOccupied(false);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ConsoleUtils.clearConsole();
    }
}
