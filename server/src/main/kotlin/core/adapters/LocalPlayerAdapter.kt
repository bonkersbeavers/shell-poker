package core.adapters

import core.bettinground.*
import core.gameflow.handstate.HandState
import core.gameflow.player.getByPosition
import core.gameflow.pots

class LocalPlayerAdapter(override val playerId: Int) : IPlayerAdapter {

    override fun sendHandUpdate(handState: HandState) {
        println("table: ${handState.communityCards.joinToString(", ")}")
        handState.pots().forEach { pot ->
            val prefix = if (pot.potNumber > 0) "side pot ${pot.potNumber}" else "main pot"
            println("$prefix: ${pot.size}")
        }
        println("seats:")

        val maxPosition = handState.players.map { it.position }.max()!!
        for (i in 0..maxPosition) {

            var playerStringBuilder = StringBuilder().append("$i: ")

            if (i == handState.positions.button)
                playerStringBuilder.append("D ")

            if (i == handState.positions.smallBlind)
                playerStringBuilder.append("SB ")

            if (i == handState.positions.bigBlind)
                playerStringBuilder.append("BB ")

            playerStringBuilder.append("\t")

            val player = handState.players.getByPosition(i)

            if (player == null) {
                playerStringBuilder.append("(empty)")
            }
            else {
                playerStringBuilder.append("stack: ${player.stack},\t")

                if (player.lastAction != null)
                    playerStringBuilder.append("${player.lastAction} ")

                if (player.bet > 0)
                    playerStringBuilder.append("${player.bet}\t")

                if (player.id == this.playerId)
                    playerStringBuilder.append("\tcards: ${player.holeCards.joinToString(", ")}")
            }

            println(playerStringBuilder.toString())
        }
    }

    override fun sendActionValidation(validation: ActionValidation) {
        println(validation)
    }

    override fun requestAction(): BettingAction {

        while (true) {
            print("your action > ")
            try {
                val command = readLine()!!
                return parseAction(command)
            } catch (e: IllegalArgumentException) {
                println(e.message)
            } catch (e: NumberFormatException) {
                println(e.message)
            } catch (e: IndexOutOfBoundsException) {
                println("error, maybe you forgot some arguments")
            }
        }

    }

    private fun parseAction(command: String): BettingAction {
        val words = command.toUpperCase().split(" ")
        val actionString = words[0]

        return when (actionString) {
            "FOLD" -> Fold
            "CHECK" -> Check
            "CALL" -> Call
            "BET" -> {
                val size = words[1].toInt()
                Bet(size)
            }
            "RAISE" -> {
                val size = words[1].toInt()
                Raise(size)
            }
            "ALLIN" -> AllIn
            else -> throw IllegalArgumentException("could not parse any action from command '$command'")
        }
    }
}