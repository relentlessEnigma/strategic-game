package org.example.threads;

import org.example.Building;
import org.example.ConsoleUtils;
import org.example.ResourceAmount;
import org.example.entities.Worker;

import java.util.List;

public class ConstructionBuildingThread extends Thread {

    private Building building;
    private Worker worker;
    private List<ResourceAmount> playerResources;
    private List<Worker> playerWorkersList;

    public ConstructionBuildingThread(Building building, Worker worker, List<ResourceAmount> playerResources, List<Worker> playerWorkersList) {
        this.building = building;
        this.worker = worker;
        this.playerResources = playerResources;
        this.playerWorkersList = playerWorkersList;
    }

    @Override
    public void run() {
        worker.setOccupied(true);
        System.out.println("Worker " + worker.getName() + " começou a tarefa:\n" + worker.getCurrentMission() + "\nTermina dentro de " + building.getConstructionMinutes() + " minutos.");

        building.build(playerResources, playerWorkersList);

        worker.setOccupied(false);
        System.out.printf("Worker %s terminou a tarefa:\n %s", worker.getName(), worker.getCurrentMission());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ConsoleUtils.clearConsole();
    }
}
