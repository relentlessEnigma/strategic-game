package org.hsh.games.aoe.threads;

import org.hsh.games.aoe.ApplicationConstants;
import org.hsh.games.aoe.Building;
import org.hsh.games.aoe.ConsoleUtils;
import org.hsh.games.aoe.ResourceAmount;
import org.hsh.games.aoe.entities.Worker;

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
        System.out.printf("Trabalhador começou a tarefa:\n %s\nTermina dentro de %d minutos\n", worker.getCurrentMission(), building.getConstructionMinutes());

        building.build(playerResources, playerWorkersList);

        worker.setOccupied(false);
        System.out.printf("Trabalhador terminou a tarefa:\n %s", worker.getCurrentMission());

        try {
            Thread.sleep(ApplicationConstants.TIME_TO_SHOW_MESSAGE);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ConsoleUtils.clearConsole();
    }
}
