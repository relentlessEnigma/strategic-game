package org.example;

import java.util.List;
import java.util.ArrayList;

enum AvailableBuildings {
    LEVEL_1(
            List.of(
                    ConstructionType.TOWN_CENTER,
                    ConstructionType.HOUSE,
                    ConstructionType.LUMBER_CAMP,
                    ConstructionType.MILL
            )
    ),
    LEVEL_2(
            List.of(
                    ConstructionType.BARRACKS,
                    ConstructionType.ARCHERY_RANGE
            )
    ),
    LEVEL_3(
            List.of(
                    ConstructionType.STABLE,
                    ConstructionType.MARKET
            )
    ),
    LEVEL_4(
            List.of(
                    ConstructionType.BLACKSMITH,
                    ConstructionType.FARM
            )
    ),
    LEVEL_5(
            List.of(
                    ConstructionType.WINERY,
                    ConstructionType.DOCK
            )
    ),
    LEVEL_6(
            List.of(
                    ConstructionType.MONASTERY,
                    ConstructionType.TEMPLE
            )
    ),
    LEVEL_7(
            List.of(
                    ConstructionType.UNIVERSITY
            )
    ),
    LEVEL_8(new ArrayList<>()),
    LEVEL_9(new ArrayList<>()),
    LEVEL_10(new ArrayList<>()),
    ;

    private final List<ConstructionType> constructionsList;

    AvailableBuildings(List<ConstructionType> constructionsList) {
        this.constructionsList = constructionsList;
    }

    public List<ConstructionType> getConstructionsList() {
        return constructionsList;
    }

}
