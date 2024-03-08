package ru.ndsmc.spectatormode.context;

import net.luckperms.api.context.ContextConsumer;
import net.luckperms.api.context.ImmutableContextSet;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import ru.ndsmc.spectatormode.SpectatorManager;
import ru.ndsmc.spectatormode.SpectatorMode;
import ru.ndsmc.spectatormode.state.StateHolder;

import static org.mockito.Mockito.*;

class SpectatorContextCalculatorTest {

    private static SpectatorContextCalculator spectatorContextCalculator;

    private static StateHolder stateHolderMock;
    private static Player playerMock;
    private static ContextConsumer contextConsumerMock;

    @BeforeAll
    public static void setUp() {
        SpectatorMode pluginMock = mock(SpectatorMode.class);
        SpectatorManager spectatorManagerMock = mock(SpectatorManager.class);
        stateHolderMock = mock(StateHolder.class);
        playerMock = mock(Player.class);
        contextConsumerMock = mock(ContextConsumer.class);

        when(pluginMock.getSpectatorManager()).thenReturn(spectatorManagerMock);
        when(spectatorManagerMock.getStateHolder()).thenReturn(stateHolderMock);

        spectatorContextCalculator = new SpectatorContextCalculator(pluginMock);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testCalculate(boolean inState) {
        when(stateHolderMock.hasPlayer(playerMock)).thenReturn(inState);

        spectatorContextCalculator.calculate(playerMock, contextConsumerMock);
        verify(contextConsumerMock, times(1)).accept("SMP Spectator", String.valueOf(inState));
    }

    @Test
    void estimatePotentialContexts_Correct() {
        try (MockedStatic<ImmutableContextSet> mocked = mockStatic(ImmutableContextSet.class)) {
            ImmutableContextSet.Builder builderMock = mock(ImmutableContextSet.Builder.class);
            when(builderMock.add(anyString(), anyString())).thenReturn(builderMock);
            mocked.when(ImmutableContextSet::builder).thenReturn(builderMock);

            spectatorContextCalculator.estimatePotentialContexts();

            verify(builderMock).add("SMP Spectator", "true");
            verify(builderMock).add("SMP Spectator", "false");
        }
    }
}
