package org.example;

public enum ConstructionProcess {
    CREATION("Construção"),
    UPDATE("Atualização"),
    ;

    String process;

    ConstructionProcess(String process) {
        this.process = process;
    }
}
