package core.gameflow

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HandStateTest {

    private val chipsA = 150
    private val playerA = Player(
            position = 0,
            stack = 0,
            chipsInPot = chipsA
    )

    private val chipsB = 300
    private val playerB = Player(
            position = 1,
            stack = 0,
            chipsInPot = chipsB
    )

    private val chipsC = 200
    private val playerC = Player(
            position = 2,
            stack = 0,
            chipsInPot = chipsC
    )

    private val mockBlinds = Blinds(0, 0)

    private val handState = HandState(
            players = listOf(playerA, playerB, playerC),
            blinds = mockBlinds,
            buttonPosition = 0,
            activePlayer = playerB
    )

    @Test
    fun `pot member should return the sum of all players' chipsInPot`() {
        assert(handState.pot == chipsA + chipsB + chipsC)
    }

    @Test
    fun `updateActivePlayer should properly substitute active player with new instance`() {
        val update = playerB.copy(chipsInPot = 500)
        val newState = handState.updateActivePlayer(update)
        assert(newState.activePlayer == update)
        assert(newState.pot == chipsA + 500 + chipsC)
    }

    @Test
    fun `HandState instantiation should fail if activePlayer is not present in players list`() {
        assertThrows<AssertionError> {
            HandState(
                    players = listOf(playerA, playerB),
                    blinds = mockBlinds,
                    buttonPosition = 0,
                    activePlayer = playerC
            )
        }
    }

    @Test
    fun `HandState instantiation should fail if players' positions are not unique`() {
        val illegalPlayer = Player(
                position = 2,
                stack = 0
        )

        assertThrows<AssertionError> {
            HandState(
                    players = listOf(playerA, playerB, playerC, illegalPlayer),
                    blinds = mockBlinds,
                    buttonPosition = 0,
                    activePlayer = playerC
            )
        }
    }
}
