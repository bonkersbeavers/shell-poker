package core.gameflow

import core.bettinground.ActionType
import core.gameflow.handstate.HandState
import core.gameflow.player.Player
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlindsTest {

    @Test
    fun `postBlinds method should update player and handState`() {
        val player0 = Player(position = 0, stack = 1000) // dealer
        val player1 = Player(position = 1, stack = 1000) // small blind
        val player2 = Player(position = 2, stack = 1000) // big blind
        val player3 = Player(position = 3, stack = 1000)
        val player4 = Player(position = 4, stack = 1000)

        val blinds = Blinds(50, 100)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3, player4),
                blinds = blinds,
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = null,
                bettingRound = BettingRound.PRE_FLOP
        ).build()

        val newState = postBlinds(state)

        assert(newState.players[1].bet == 50)
        assert(newState.players[2].bet == 100)

        assert(newState.players[1].stack == 950)
        assert(newState.players[1].stack == 900)

        assert(newState.players[1].lastAction == ActionType.POST)
        assert(newState.players[2].lastAction == ActionType.POST)

        assert(newState.activePlayer == newState.players[3])
    }
}
