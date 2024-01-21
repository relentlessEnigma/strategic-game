package org.example.entities;

import org.example.ThreadUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum ResourceType {
    WOOD(
            // Description of the Resource
            "Madeira",
            // Initial Offer given
            100,
            // Difficulty, it means there can be enemies around and kill the worker
            Difficulty.EASY,
            // Convert to Minutes (60 * 1000)
            ThreadUtils.toMinutes(5), 100, 2),
    POPULATION("População", 3, Difficulty.HARD, ThreadUtils.toMinutes(30), 2,0),
    WATER("Água", 200, Difficulty.EASY, ThreadUtils.toMinutes(3), 100,1),
    FOOD("Comida", 100, Difficulty.EASY, ThreadUtils.toMinutes(7), 100,3),
    STONE("Pedra", 10, Difficulty.EASY, ThreadUtils.toMinutes(10), 70,4),
    IRON("Ferro", 0, Difficulty.MEDIUM, ThreadUtils.toMinutes(15), 62,10),
    SILVER("Prata", 0, Difficulty.MEDIUM, ThreadUtils.toMinutes(20), 43,8),
    GRAPES("Uvas", 0, Difficulty.MEDIUM, ThreadUtils.toMinutes(15), 32,7),
    GOLD("Ouro", 0, Difficulty.EXTREME, ThreadUtils.toMinutes(30), 8, 50),
    FAVOR("Favor aos Deuses", 0, Difficulty.EXTREME, ThreadUtils.toMinutes(30), 8,0)
    ;

    String description;
    Difficulty hardToGet;
    int timeLimitForSearch;
    int amountMaxToBeFound;
    int pricePerUnit;
    int initialOffer;

    ResourceType(
            String description,
            int initialOffer,
            Difficulty hardToGet,
            int timeLimitForSearch,
            int amountMaxToBeFound,
            int pricePerUnit
    ) {
        this.description = description;
        this.hardToGet = hardToGet;
        this.timeLimitForSearch = timeLimitForSearch;
        this.amountMaxToBeFound = amountMaxToBeFound;
        this.pricePerUnit = pricePerUnit;
        this.initialOffer = initialOffer;
    }

    public int getInitialOffer() {
        return initialOffer;
    }

    public void setInitialOffer(int initialOffer) {
        this.initialOffer = initialOffer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public static List<ResourceType> getResourcesPackBasedOnCurrentEra(int currentEraLevel) {
        EraAge currentEra = EraAge.getByLevel(currentEraLevel);
        List<ResourceType> resourcesPack = new ArrayList<>();

        switch (Objects.requireNonNull(currentEra)) {
            case STONE_AGE:
                resourcesPack.addAll(Arrays.asList(POPULATION, FOOD, WATER, WOOD, STONE));
                break;
            case BRONZE_AGE:
                resourcesPack.add(IRON);
                break;
            case IRON_AGE:
                resourcesPack.add(SILVER);
                break;
            case MEDIEVAL_AGE:
                resourcesPack.add(GRAPES);
                break;
            case INDUSTRIAL_AGE:
                resourcesPack.add(GOLD);
                break;
            case MODERN_AGE:
                resourcesPack.add(FAVOR);
                break;
            case RENAISSANCE, FUTURE_AGE, INFORMATION_AGE:
                resourcesPack.addAll(List.of());
                break;
            default:
                throw new IllegalArgumentException("Invalid era level");
        }

        return resourcesPack;
    }
}
