package org.hsh.games.aoe;

import org.hsh.games.aoe.entities.EraAge;
import org.hsh.games.aoe.entities.Player;
import org.hsh.games.aoe.entities.ResourceType;
import org.hsh.games.aoe.entities.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PlayerServiceTest {
    private PlayerService playerService;
    private Player player;
    private Worker worker;
    private Building building;

    @BeforeEach
    void setUp() {
        player = new Player("FAMRTEST");
        playerService = new PlayerService(player);
        worker = Mockito.mock(Worker.class);
        building = Mockito.mock(Building.class);
    }

    @Test
    void addResourceIncreasesResourceAmount() {
        when(playerService.getResources()).thenReturn(List.of());

        playerService.addResource(ResourceType.WOOD, 10);
        assertEquals(10, playerService.getResources().get(0).getAmount());
    }

    @Test
    void addWorkerWhenPopulationLimitReachedDoesNotAddWorker() {
        playerService.addWorker(worker);
        playerService.addWorker(worker);
        assertEquals(1, playerService.getWorkers().size());
    }
    @Test
    void checkIfPlayerHasEnoughResourcesReturnsFalseWhenResourcesAreInsufficient() {
        when(building.getResourceCost()).thenReturn(List.of(new ResourceAmount(ResourceType.WOOD, 15)));
        playerService.addResource(ResourceType.WOOD, 10);
        when(building.getResourceCost()).thenReturn(List.of(new ResourceAmount(ResourceType.WOOD, 5)));
        playerService.addResource(ResourceType.WOOD, 10);
        assertTrue(playerService.checkIfPlayerHasEnoughResources(building));
    }

    @Test
    void checkIfPlayerHasEnoughResourcesReturnsTrueWhenResourcesAreSufficient() {
        assertTrue(playerService.checkIfPlayerHasEnoughResources(building));
    }

    @Test
    void checkIfBuildingAmountHasReachedReturnsTrueWhenLimitReached() {
        when(building.getAmountConstructionsAllowed()).thenReturn(1);
        when(building.getConstructionTypeName()).thenReturn("House");
        when(building.getBuilded()).thenReturn(true);
        playerService.addNewBuilding(building);
        assertTrue(playerService.checkIfBuildingAmountHasReached(building));
    }

    @Test
    void checkIfBuildingAmountHasReachedReturnsFalseWhenLimitNotReached() {
        when(building.getAmountConstructionsAllowed()).thenReturn(2);
        when(building.getConstructionTypeName()).thenReturn("House");
        when(building.getBuilded()).thenReturn(true);
        playerService.addNewBuilding(building);
        assertFalse(playerService.checkIfBuildingAmountHasReached(building));
    }

    @Test
    void checkIfBuildingHasReachedItsMaximLevelReturnsTrueWhenMaxLevelReached() {
        when(building.getLevel()).thenReturn(5);
        when(building.getMaxLevel()).thenReturn(5);
        assertTrue(playerService.checkIfBuildingHasReachedItsMaximLevel(building));
    }

    @Test
    void checkIfBuildingHasReachedItsMaximLevelReturnsFalseWhenMaxLevelNotReached() {
        when(building.getLevel()).thenReturn(4);
        when(building.getMaxLevel()).thenReturn(5);
        assertFalse(playerService.checkIfBuildingHasReachedItsMaximLevel(building));
    }
}