package org.example.threads;

import org.example.Building;
import org.example.ConsoleUtils;
import org.example.ResourceAmount;
import org.example.entities.Worker;
import java.util.List;

public class ConstructionUpdatingThread extends Thread {

    private Building building;
    private Worker worker;
    private List<ResourceAmount> playerResources;

    public ConstructionUpdatingThread(Building building, Worker worker, List<ResourceAmount> playerResources) {
        this.building = building;
        this.worker = worker;
        this.playerResources = playerResources;
    }

    @Override
    public void run() {
        worker.setOccupied(true);
        System.out.println("Worker " + worker.getName() + " comecou a tarefa:\n" + worker.getCurrentMission() + "\nTermina dentro de " + building.getConstructionMinutes() + " minutos.");

        this.building.upgrade();

        worker.setOccupied(false);
        System.out.println("Worker " + worker.getName() + " terminou a tarefa:\n" + worker.getCurrentMission() + "\n.");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ConsoleUtils.clearConsole();
    }
}