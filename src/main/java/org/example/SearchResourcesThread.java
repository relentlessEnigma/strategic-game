package org.example;

public class SearchResourcesThread extends Thread {

    private Resource building;
    private Worker worker;
    private int timeToSearch;

    public SearchResourcesThread(Resource building, Worker worker, int timeToSearch) {
        this.building = building;
        this.worker = worker;
        this.timeToSearch = timeToSearch;
    }

    @Override
    public void run() {
        worker.setOccupied(true);
        System.out.println("Worker " + worker.getName() + " começou a tarefa:\n" + worker.getCurrentMission() + "\nTermina dentro de " + timeToSearch + " minutos.");

        try {
            Thread.sleep(timeToSearch);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

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
