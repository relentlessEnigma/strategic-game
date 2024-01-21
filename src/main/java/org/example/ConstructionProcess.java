package org.example;

public enum ConstructionProcess {
    CREATION("Construção"),
    UPDATE("Atualização"),
    ;

    String process;

    ConstructionProcess(String process) {
        this.process = process;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
