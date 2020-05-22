import core.settings.RoomSettings
import core.handflow.blinds.Blinds
import core.handflow.positions.Positions
import service.local.LocalHandManager
import core.handflow.hand.HandState
import service.local.LocalConsoleAdapter
import core.handflow.player.Player

fun main() {

    val adapter = LocalConsoleAdapter()

    val settings = RoomSettings(4)
    val blinds = Blinds(50, 100)
    val positions = Positions(0, 1, 2)
    val players = (0 until 3).map { Player(seat = it, stack = 1000) }

    val manager = LocalHandManager(settings, adapter)
    var state = HandState(players, positions, blinds)

    while (true) {
        state = manager.playHand(state)
    }
}
