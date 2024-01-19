package org.example;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

enum ConstructionType {
    TOWN_CENTER("Town Center", 10, 2, 10,
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 200),
                    new ResourceAmount(ResourceType.GOLD, 50),
                    new ResourceAmount(ResourceType.WOOD, 150)
            ),
            List.of(
                    new ResourceAmount(ResourceType.POPULATION, 2)
            )
    ),
    LUMBER_CAMP("Lumber Camp", 2, 5, 2,
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 50),
                    new ResourceAmount(ResourceType.GOLD, 0),
                    new ResourceAmount(ResourceType.WOOD, 100),
                    new ResourceAmount(ResourceType.STONE, 25)
            ),
            List.of(
                    new ResourceAmount(ResourceType.WOOD, 5)
            )
    ),
    MILL("Mill", 2,5, 2,
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 40),
                    new ResourceAmount(ResourceType.GOLD, 0),
                    new ResourceAmount(ResourceType.WOOD, 80),
                    new ResourceAmount(ResourceType.STONE, 20)
            ),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 5)
            )
    ),
    HOUSE("House", 2,10, 1,
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 30),
                    new ResourceAmount(ResourceType.GOLD, 10),
                    new ResourceAmount(ResourceType.WOOD, 60)
            ),
            Collections.emptyList()
    ),
    BARRACKS("Barracks", 2,5, 4,
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 60),
                    new ResourceAmount(ResourceType.GOLD, 30),
                    new ResourceAmount(ResourceType.WOOD, 120)
            ),
            Collections.emptyList()
    ),
    ARCHERY_RANGE("Archery Range", 2,5, 5,
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 70),
                    new ResourceAmount(ResourceType.GOLD, 40),
                    new ResourceAmount(ResourceType.WOOD, 150)
            ),
            Collections.emptyList()
    ),
    STABLE("Stable", 2,3, 7,
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 80),
                    new ResourceAmount(ResourceType.GOLD, 50),
                    new ResourceAmount(ResourceType.WOOD, 180),
                    new ResourceAmount(ResourceType.IRON, 20)
            ),
            Collections.emptyList()
    ),
    MARKET("Market", 2,3, 5,
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 90),
                    new ResourceAmount(ResourceType.GOLD, 60),
                    new ResourceAmount(ResourceType.WOOD, 120),
                    new ResourceAmount(ResourceType.STONE, 30)
            ),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 5)
            )
    ),
    BLACKSMITH("Blacksmith", 2,3, 6,
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 100),
                    new ResourceAmount(ResourceType.GOLD, 70),
                    new ResourceAmount(ResourceType.WOOD, 100),
                    new ResourceAmount(ResourceType.IRON, 40)
            ),
            Collections.emptyList()
    ),
    MONASTERY("Monastery", 2,1, 8,
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 120),
                    new ResourceAmount(ResourceType.GOLD, 80),
                    new ResourceAmount(ResourceType.WOOD, 150),
                    new ResourceAmount(ResourceType.STONE, 50)
            ),
            Collections.emptyList()
    ),
    UNIVERSITY("University", 2,1, 10,
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 150),
                    new ResourceAmount(ResourceType.GOLD, 100),
                    new ResourceAmount(ResourceType.WOOD, 200),
                    new ResourceAmount(ResourceType.STONE, 80),
                    new ResourceAmount(ResourceType.IRON, 30)
            ),
            Collections.emptyList()
    ),
    FARM("Farm", 2,5, 3,
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 40),
                    new ResourceAmount(ResourceType.GOLD, 0),
                    new ResourceAmount(ResourceType.WOOD, 60),
                    new ResourceAmount(ResourceType.WATER, 30)
            ),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 5)
            )
    ),
    WINERY("Winery", 2,2, 6,
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 80),
                    new ResourceAmount(ResourceType.GOLD, 40),
                    new ResourceAmount(ResourceType.WOOD, 100),
                    new ResourceAmount(ResourceType.GRAPES, 50)
            ),
            List.of(
                    new ResourceAmount(ResourceType.GRAPES, 5)
            )
    ),
    DOCK("Dock", 2,3, 5,
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 70),
                    new ResourceAmount(ResourceType.GOLD, 20),
                    new ResourceAmount(ResourceType.WOOD, 120),
                    new ResourceAmount(ResourceType.WATER, 80)
            ),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 5)
            )
    ),
    TEMPLE("Temple", 2,1, 9,
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 100),
                    new ResourceAmount(ResourceType.GOLD, 60),
                    new ResourceAmount(ResourceType.WOOD, 150),
                    new ResourceAmount(ResourceType.STONE, 40),
                    new ResourceAmount(ResourceType.FAVOR, 30)
            ),
            List.of(
                    new ResourceAmount(ResourceType.FAVOR, 2)
            )
    ),
    ;

    private final String name;
    private int amountConstructionsAllowed;
    private final int maxLevel;
    private final int baseConstructionTime;
    final List<ResourceAmount> baseResourcesCost;
    final List<ResourceAmount> baseResourcesProduction;

    ConstructionType(String name, int maxLevel, int amountConstructionsAllowed, int baseConstructionTime, List<ResourceAmount> baseResourcesCost, List<ResourceAmount> baseResourcesProduction) {
        this.name = name;
        this.maxLevel = maxLevel;
        this.amountConstructionsAllowed = amountConstructionsAllowed;
        this.baseConstructionTime = baseConstructionTime;
        this.baseResourcesCost = baseResourcesCost;
        this.baseResourcesProduction = baseResourcesProduction;
    }
    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public List<ResourceAmount> getBaseResourcesCost() {
        return baseResourcesCost;
    }

    public int getAmountConstructionsAllowed() {
        return amountConstructionsAllowed;
    }

    public void setAmountConstructionsAllowed(int amountConstructionsAllowed) {
        this.amountConstructionsAllowed = amountConstructionsAllowed;
    }

    public int getBaseConstructionTime() {
        return baseConstructionTime;
    }

    public List<ResourceAmount> getBaseResourcesProduction() {
        return baseResourcesProduction;
    }

    public static ConstructionType getEnumFromConstant(String name) {
        for(ConstructionType x : ConstructionType.values()) {
            if(Objects.equals(x.name, name)) return x;
        }
        return null;
    }
}
