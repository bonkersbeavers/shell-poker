package core.gameflow.handstate

import core.gameflow.Blinds
import core.gameflow.player.Player
import core.gameflow.Positions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import java.lang.AssertionError

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HandStateTest {

    @Test
    fun `building HandState should fail if players' positions are not unique`() {
        assertThrows<AssertionError> {

            val player0 = Player(position = 0, stack = 100)
            val player1 = Player(position = 1, stack = 100)
            val player2 = Player(position = 1, stack = 100)

            HandState.ImmutableBuilder(
                    players = listOf(player0, player1, player2),
                    blinds = Blinds(50, 100),
                    positions = Positions(button = 0, smallBlind = 1, bigBlind = 2)
            ).build()
        }
    }

    @Test
    fun `building HandState should fail if active player doesn't point to any of the players`() {
        assertThrows<AssertionError> {

            val player0 = Player(position = 0, stack = 100)
            val player1 = Player(position = 1, stack = 100)
            val player2 = Player(position = 2, stack = 100)

            HandState.ImmutableBuilder(
                    players = listOf(player0, player1),
                    blinds = Blinds(50, 100),
                    positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                    activePlayer = player2
            ).build()
        }
    }

    @Test
    fun `building HandState should fail if last aggressor doesn't point to any of the players`() {
        assertThrows<AssertionError> {

            val player0 = Player(position = 0, stack = 100)
            val player1 = Player(position = 1, stack = 100)
            val player2 = Player(position = 2, stack = 100)

            HandState.ImmutableBuilder(
                    players = listOf(player0, player1),
                    blinds = Blinds(50, 100),
                    positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                    activePlayer = player1,
                    lastAggressor = player2
            ).build()
        }
    }

    @Test
    fun `pot should properly sum players' bets from previous rounds`() {

        val player0 = Player(position = 0, stack = 0, chipsInPot = 50)
        val player1 = Player(position = 1, stack = 100, chipsInPot = 150)
        val player2 = Player(position = 2, stack = 100, chipsInPot = 150)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player1
        ).build()

        assert(state.pot == 50 + 150 + 150)
    }

    @Test
    fun `total bet should return the highest bet made so far in given betting round`() {

        val player0 = Player(position = 0, stack = 0)
        val player1 = Player(position = 1, stack = 100)
        val player2 = Player(position = 2, stack = 100)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player1,
                lastLegalBet = 100,
                extraBet = 75
        ).build()

        assert(state.totalBet == 100 + 75)
    }

    @Test
    fun `HandState should properly find players on blind positions`() {

        val player0 = Player(position = 0, stack = 100)
        val player1 = Player(position = 1, stack = 100)
        val player2 = Player(position = 2, stack = 100) // BTN
        val player3 = Player(position = 3, stack = 100)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 2, smallBlind = 3, bigBlind = 0)
        ).build()

        assert(state.smallBlindPlayer == player3)
        assert(state.bigBlindPlayer == player0)
    }
}
