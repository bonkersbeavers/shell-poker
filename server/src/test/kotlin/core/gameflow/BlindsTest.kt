package core.gameflow

import core.bettinground.ActionType
import core.gameflow.handstate.HandState
import core.gameflow.player.Player
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlindsTest {

    @Test
    fun `postBlindsAndAnte method should update players and handState`() {
        val player0 = Player(position = 0, stack = 1000) // dealer
        val player1 = Player(position = 1, stack = 1000) // small blind
        val player2 = Player(position = 2, stack = 1000) // big blind

        val blinds = Blinds(50, 100, ante = 200)

        val stateBuilder = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2),
                blinds = blinds,
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2)
        )

        val newBuilder = postBlindsAndAnte(stateBuilder)
        val players = newBuilder.players!!

        assert(players[0].chipsInPot == 200)
        assert(players[0].bet == 0)
        assert(players[0].stack == 800)
        assert(players[0].lastAction == null)

        assert(players[1].chipsInPot == 200)
        assert(players[1].bet == 50)
        assert(players[1].stack == 750)
        assert(players[1].lastAction == ActionType.POST)

        assert(players[2].chipsInPot == 200)
        assert(players[2].bet == 100)
        assert(players[2].stack == 700)
        assert(players[2].lastAction == ActionType.POST)
    }

    @Test
    fun `postBlindsAndAnte method should properly process players with not enough chips in the stack`() {
        val player0 = Player(position = 0, stack = 150) // dealer
        val player1 = Player(position = 1, stack = 300) // small blind
        val player2 = Player(position = 2, stack = 275) // big blind

        val blinds = Blinds(50, 100, ante = 200)

        val stateBuilder = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2),
                blinds = blinds,
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2)
        )

        val newBuilder = postBlindsAndAnte(stateBuilder)
        val players = newBuilder.players!!

        assert(players[0].chipsInPot == 150)
        assert(players[0].bet == 0)
        assert(players[0].stack == 0)
        assert(players[0].lastAction == null)

        assert(players[1].chipsInPot == 200)
        assert(players[1].bet == 50)
        assert(players[1].stack == 50)
        assert(players[1].lastAction == ActionType.POST)

        assert(players[2].chipsInPot == 200)
        assert(players[2].bet == 75)
        assert(players[2].stack == 0)
        assert(players[2].lastAction == ActionType.POST)
    }

    @Test
    fun `postBlindsAndAnte method should force big blind post from the players that are new at the table`() {
        val player0 = Player(position = 0, stack = 1000) // dealer
        val player1 = Player(position = 1, stack = 1000) // small blind / new player
        val player2 = Player(position = 2, stack = 1000) // big blind
        val player3 = Player(position = 3, stack = 1000) // new player

        val blinds = Blinds(50, 100, ante = 200)

        val stateBuilder = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = blinds,
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2)
        )

        val newBuilder = postBlindsAndAnte(stateBuilder, newPlayersIds = listOf(1, 3))
        val players = newBuilder.players!!

        assert(players[0].chipsInPot == 200)
        assert(players[0].bet == 0)
        assert(players[0].stack == 800)
        assert(players[0].lastAction == null)

        assert(players[1].chipsInPot == 200)
        assert(players[1].bet == 100)
        assert(players[1].stack == 700)
        assert(players[1].lastAction == ActionType.POST)

        assert(players[2].chipsInPot == 200)
        assert(players[2].bet == 100)
        assert(players[2].stack == 700)
        assert(players[2].lastAction == ActionType.POST)

        assert(players[3].chipsInPot == 200)
        assert(players[3].bet == 100)
        assert(players[3].stack == 700)
        assert(players[3].lastAction == ActionType.POST)
    }
}
