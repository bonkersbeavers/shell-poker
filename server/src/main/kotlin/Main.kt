import core.RoomSettings
import core.flowUtils.Blinds
import core.flowUtils.Positions
import core.hand.HandManager
import core.hand.HandState
import core.hand.LocalPlayerAdapter
import core.hand.PlayerState

fun main() {

    val adapter = LocalPlayerAdapter()

    val settings = RoomSettings(4)
    val blinds = Blinds(50, 100)
    val positions = Positions(0, 1, 2)
    val players = (0 until 3).map { PlayerState(seat = it, stack = 1000) }

    var state = HandState(players, positions, blinds)

    while (true) {
        val manager = HandManager(settings, adapter)
        state = manager.playHand(state)
    }
}
