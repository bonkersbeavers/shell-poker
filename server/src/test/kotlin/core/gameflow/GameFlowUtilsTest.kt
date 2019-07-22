package core.gameflow

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameFlowUtilsTest {
    private val players1 = listOf(
            Player(position = 0, stack = 0, folded = true),
            Player(position = 1, currentBet = 1000, stack = 0), //all in
            Player(position = 2, stack = 0),
            Player(position = 3, stack = 0)
    )

    private val players2 = listOf(
            Player(position = 0, stack = 0, folded = true),
            Player(position = 1, stack = 0, folded = true),
            Player(position = 1, stack = 0, folded = true),
            Player(position = 3, stack = 0)
    )

    private val noActiveplayers = listOf(
            Player(position = 0, stack = 0, folded = true),
            Player(position = 1, stack = 0, folded = true),
            Player(position = 1, stack = 0, folded = true),
            Player(position = 3, stack = 0, folded = true)
    )

    @Test
    fun `findNextPlayer should find next player`() {
        assert(findNextPlayer(players1, 3) == players1[0])
        assert(findNextPlayer(players1, 1) == players1[2])
    }

    @Test
    fun `findNextActivePlayer should find next active player`() {
        assert(findNextActivePlayer(players1, 3) == players1[2])
        assert(findNextActivePlayer(players1, 0) == players1[2])
        assert(findNextActivePlayer(players1, 2) == players1[3])

        assert(findNextActivePlayer(players2, 3) == players1[3])

        assert(findNextActivePlayer(noActiveplayers, 0) == null)

    }

    @Test
    fun `findPrevPlayer should find previous player`() {
        assert(findPrevPlayer(players1, 2) == players1[1])
        assert(findPrevPlayer(players1, 3) == players1[2])
        assert(findPrevPlayer(players1, 0) == players1[3])
    }

    @Test
    fun `findPrevActivePlayer should find previous active player`() {
        assert(findPrevActivePlayer(players1, 2) == players1[3])
        assert(findPrevActivePlayer(players1, 3) == players1[2])

        assert(findPrevActivePlayer(players2, 3) == players1[3])

        assert(findPrevActivePlayer(noActiveplayers, 0) == null)
    }
}
