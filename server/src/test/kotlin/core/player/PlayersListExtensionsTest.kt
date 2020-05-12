package core.player

import core.betting.ActionType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayersListExtensionsTest {

    @Test
    fun `getByPosition should find player that the position points to`() {

        val player0 = Player(seat = 0, stack = 100)
        val player1 = Player(seat = 1, stack = 100)
        val player2 = Player(seat = 2, stack = 100)
        val player3 = Player(seat = 3, stack = 100)

        val players = listOf(player0, player1, player2, player3)

        assert(players.getByPosition(0) == player0)
        assert(players.getByPosition(3) == player3)
    }

    @Test
    fun `getByPosition should return null if the position is empty`() {

        val player0 = Player(seat = 0, stack = 100)
        val player1 = Player(seat = 1, stack = 100)
        val player3 = Player(seat = 3, stack = 100)

        val players = listOf(player0, player1, player3)

        assert(players.getByPosition(2) == null)
    }

    @Test
    fun `ordered should return players list in clockwise order`() {

        val player0 = Player(seat = 0, stack = 100)
        val player1 = Player(seat = 1, stack = 100)
        val player2 = Player(seat = 2, stack = 100)
        val player3 = Player(seat = 3, stack = 100)

        val players = listOf(player0, player1, player2, player3)

        assert(players.ordered(2) == listOf(player2, player3, player0, player1))
    }

    @Test
    fun `next should find the first player after given position`() {

        val player0 = Player(seat = 0, stack = 100)
        val player1 = Player(seat = 1, stack = 100)
        val player2 = Player(seat = 2, stack = 100)
        val player3 = Player(seat = 3, stack = 100)

        val players = listOf(player0, player1, player2, player3)

        assert(players.next(0) == player1)
        assert(players.next(3) == player0)
    }

    @Test
    fun `prev should find the first player before given position`() {

        val player0 = Player(seat = 0, stack = 100)
        val player1 = Player(seat = 1, stack = 100)
        val player3 = Player(seat = 3, stack = 100)

        val players = listOf(player0, player1, player3)

        assert(players.prev(0) == player3)
        assert(players.prev(2) == player1)
    }

    @Test
    fun `nextDecisive should find the first decisive player after given position`() {

        val player0 = Player(seat = 0, stack = 100)
        val player1 = Player(seat = 1, stack = 100, lastAction = ActionType.FOLD)
        val player2 = Player(seat = 2, stack = 100)
        val player3 = Player(seat = 3, stack = 0, lastAction = ActionType.ALL_IN)

        val players = listOf(player0, player1, player2, player3)

        assert(players.nextDecisive(0) == player2)
        assert(players.nextDecisive(3) == player0)
    }

    @Test
    fun `inGame should find all players that haven't folded yet`() {

        val player0 = Player(seat = 0, stack = 0, lastAction = ActionType.ALL_IN)
        val player1 = Player(seat = 1, stack = 100, lastAction = ActionType.FOLD)
        val player2 = Player(seat = 2, stack = 100)
        val player3 = Player(seat = 3, stack = 100)

        val players = listOf(player0, player1, player2, player3)

        assert(players.inGame() == listOf(player0, player2, player3))
    }

    @Test
    fun `decisivePlayers should find all players that can potentially make decisions`() {

        val player0 = Player(seat = 0, stack = 0, lastAction = ActionType.ALL_IN)
        val player1 = Player(seat = 1, stack = 100, lastAction = ActionType.FOLD)
        val player2 = Player(seat = 2, stack = 100)
        val player3 = Player(seat = 3, stack = 100)

        val players = listOf(player0, player1, player2, player3)

        assert(players.decisive() == listOf(player2, player3))
    }
}
