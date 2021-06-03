package me.ohowe12.spectatormode.listener;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.ohowe12.spectatormode.SpectatorMode;
import me.ohowe12.spectatormode.testutils.TestUtils;
import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerMoveEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class OnMoveListenerTest {

    ServerMock serverMock;
    SpectatorMode plugin;

    // Default player location is 0, 5, 0
    PlayerMock playerMock;

    @BeforeEach
    void setUp() {
        serverMock = MockBukkit.mock();
        plugin = MockBukkit.load(SpectatorMode.class);

        serverMock.setPlayers(0);
        playerMock = serverMock.addPlayer();
        playerMock.setGameMode(GameMode.SURVIVAL);

        plugin.reloadConfig();

        plugin.getSpectatorManager().togglePlayer(playerMock);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @ParameterizedTest
    @ValueSource(ints = {-3, -5, -6})
    void testEnabledAndBadYLevel(int shift) {
        TestUtils.setConfigFileOfPlugin(plugin, "badyenabled.yml");

        PlayerMoveEvent event = playerMock.simulatePlayerMove(playerMock.getLocation().add(0, shift, 0));

        assertMoveEventCanceled(event);
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, 1})
    void testEnabledAndGoodYLevel(int shift) {
        TestUtils.setConfigFileOfPlugin(plugin, "badyenabled.yml");

        PlayerMoveEvent event = playerMock.simulatePlayerMove(playerMock.getLocation().add(0, -shift, 0));

        assertMoveEventNotCanceled(event);
    }

    @Test
    void testDisabledAndBadYLevel() {
        TestUtils.setConfigFileOfPlugin(plugin, "badydisabled.yml");

        PlayerMoveEvent event = playerMock.simulatePlayerMove(playerMock.getLocation().add(0, -3, 0));

        assertMoveEventNotCanceled(event);
    }

    private void assertMoveEventCanceled(PlayerMoveEvent event) {
        assertTrue(event.isCancelled());
        assertEquals(event.getFrom(), event.getTo());
    }

    private void assertMoveEventNotCanceled(PlayerMoveEvent event) {
        assertFalse(event.isCancelled());
    }


}