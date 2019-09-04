package core.gameflow.handstate

import core.bettinground.ActionType
import core.gameflow.Blinds
import core.gameflow.Player
import core.gameflow.Positions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HandStateExtensionsTest {

    @Test
    fun `playersInGame should find all players that haven't folded yet`() {

        val player0 = Player(position = 0, stack = 0, lastAction = ActionType.ALL_IN)
        val player1 = Player(position = 1, stack = 100, lastAction = ActionType.FOLD)
        val player2 = Player(position = 2, stack = 100)
        val player3 = Player(position = 3, stack = 100)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player2,
                lastLegalBet = 100,
                extraBet = 75
        ).build()

        assert(state.playersInGame == listOf(player0, player2, player3))
    }

    @Test
    fun `decisivePlayers should find all players that can potentially make decisions`() {

        val player0 = Player(position = 0, stack = 0, lastAction = ActionType.ALL_IN)
        val player1 = Player(position = 1, stack = 100, lastAction = ActionType.FOLD)
        val player2 = Player(position = 2, stack = 100)
        val player3 = Player(position = 3, stack = 100)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player2,
                lastLegalBet = 100,
                extraBet = 75
        ).build()

        assert(state.decisivePlayers == listOf(player2, player3))
    }

    @Test
    fun `updateActivePlayer should properly substitute active player in players list`() {

        val player0 = Player(position = 0, stack = 100)
        val player1 = Player(position = 1, stack = 100)
        val player2 = Player(position = 2, stack = 100)
        val player3 = Player(position = 3, stack = 100)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player3
        ).build()

        val newPlayer = player3.copy(stack = 0, bet = 100)
        val newState = state.updateActivePlayer(newPlayer)

        assert(newState.players[3] == newPlayer)
    }

    @Test
    fun `orderedPlayers should return players list in clockwise order`() {

        val player0 = Player(position = 0, stack = 100)
        val player1 = Player(position = 1, stack = 100)
        val player2 = Player(position = 2, stack = 100)
        val player3 = Player(position = 3, stack = 100)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player3
        ).build()

        assert(state.orderedPlayers(2) == listOf(player2, player3, player0, player1))
    }

    @Test
    fun `nextPlayer should find the first player after given position`() {

        val player0 = Player(position = 0, stack = 100)
        val player1 = Player(position = 1, stack = 100)
        val player2 = Player(position = 2, stack = 100)
        val player3 = Player(position = 3, stack = 100)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player3
        ).build()

        assert(state.nextPlayer(0) == player1)
        assert(state.nextPlayer(3) == player0)
    }

    @Test
    fun `nextDecisivePlayer should find the first decisive player after given position`() {

        val player0 = Player(position = 0, stack = 100)
        val player1 = Player(position = 1, stack = 100, lastAction = ActionType.FOLD)
        val player2 = Player(position = 2, stack = 100)
        val player3 = Player(position = 3, stack = 0, lastAction = ActionType.ALL_IN)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player3
        ).build()

        assert(state.nextDecisivePlayer(0) == player2)
        assert(state.nextDecisivePlayer(3) == player0)
    }

    @Test
    fun `rebuild should create new HandState with properly updated fields`() {

        val player0 = Player(position = 0, stack = 100)
        val player1 = Player(position = 1, stack = 100, lastAction = ActionType.FOLD)
        val player2 = Player(position = 2, stack = 100)
        val player3 = Player(position = 3, stack = 0, lastAction = ActionType.ALL_IN)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player3
        ).build()

        val newState = state.rebuild(activePlayer = null, extraBet = 250, minRaise = 700)

        assert(newState.players == state.players)
        assert(newState.blinds == state.blinds)
        assert(newState.positions == state.positions)
        assert(newState.activePlayer == null)
        assert(newState.extraBet == 250)
        assert(newState.minRaise == 700)
    }
}
