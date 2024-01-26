package org.hsh.games.aoe.entities;

public class Player {

    private String farmName;
    private EraAge eraAge;

    public Player(String farmName) {
        this.farmName = farmName;
        this.eraAge = EraAge.getByLevel(1);
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public EraAge getEraAge() {
        return eraAge;
    }

    public void setEraAge(EraAge eraAge) {
        this.eraAge = eraAge;
    }
}
