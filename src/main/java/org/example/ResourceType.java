package org.example;

enum ResourceType {
    WOOD(
            //Name of the Resource
            "Madeira",
            //Difficulty, it means there can be enemies around and kill the worker
            Difficulty.EASY,
            // Convert to Minutes (60 * 1000)
            toMinutes(5), 100, 2),
    WATER("Água", Difficulty.EASY, toMinutes(3), 100,1),
    FOOD("Comida", Difficulty.EASY, toMinutes(7), 100,3),
    STONE("Pedra", Difficulty.EASY, toMinutes(10), 70,4),
    IRON("Ferro", Difficulty.MEDIUM, toMinutes(15), 62,10),
    SILVER("Prata", Difficulty.MEDIUM, toMinutes(20), 43,8),
    GRAPES("Uvas", Difficulty.MEDIUM, toMinutes(15), 32,7),
    POPULATION("População", Difficulty.HARD, toMinutes(30), 2,0),
    GOLD("Ouro", Difficulty.EXTREME, toMinutes(30), 8, 50),
    FAVOR("Favor aos Deuses", Difficulty.EXTREME, toMinutes(30), 8,0)
    ;

    String name;
    Difficulty hardToGet;
    int timeLimitForSearch;
    int amountMaxToBeFound; // Cant be lower than 4
    int pricePerUnit;

    ResourceType(String name, Difficulty hardToGet, int timeLimitForSearch, int amountMaxToBeFound, int pricePerUnit) {
        this.name = name;
        this.hardToGet = hardToGet;
        this.timeLimitForSearch = timeLimitForSearch;
        this.amountMaxToBeFound = amountMaxToBeFound;
        this.pricePerUnit = pricePerUnit;
    }

    public static int toMinutes(int number) {
        return number * 60 * 1000;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Difficulty getHardToGet() {
        return hardToGet;
    }

    public void setHardToGet(Difficulty hardToGet) {
        this.hardToGet = hardToGet;
    }

    public int getTimeLimitForSearch() {
        return timeLimitForSearch;
    }

    public void setTimeLimitForSearch(int timeLimitForSearch) {
        this.timeLimitForSearch = timeLimitForSearch;
    }

    public int getAmountMaxToBeFound() {
        return amountMaxToBeFound;
    }

    public void setAmountMaxToBeFound(int amountMaxToBeFound) {
        this.amountMaxToBeFound = amountMaxToBeFound;
    }

    public int getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(int pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
