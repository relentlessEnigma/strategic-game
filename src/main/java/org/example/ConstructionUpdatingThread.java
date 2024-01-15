package org.example;

public class ConstructionUpdatingThread extends Thread {

    private Building building;
    private Worker worker;
    public ConstructionUpdatingThread(Building building, Worker worker) {
        this.building = building;
        this.worker = worker;
    }

    @Override
    public void run() {
        worker.setOccupied(true);
        System.out.println("Worker " + worker.getName() + " comecou a tarefa:\n" + worker.getCurrentMission() + "\nTermina dentro de " + building.getConstructionMinutes() + " minutos.");

        this.building.upgrade();

        worker.setOccupied(false);
        System.out.println("Worker " + worker.getName() + " terminou a tarefa:\n" + worker.getCurrentMission() + "\n.");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ConsoleUtils.clearConsole();
    }
}
