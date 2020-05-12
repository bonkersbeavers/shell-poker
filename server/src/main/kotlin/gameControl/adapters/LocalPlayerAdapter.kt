package gameControl.adapters

import core.betting.*
import core.handflow.HandState
import core.player.getByPosition
import core.flowUtils.pots

class LocalPlayerAdapter() {

    fun update(handState: HandState) {

        println("===============================================================")

        println("table: ${handState.communityCards.joinToString(", ")}")
        handState.pots().forEach { pot ->
            val prefix = if (pot.potNumber > 0) "side pot ${pot.potNumber}" else "main pot"
            println("$prefix: ${pot.size}")
        }
        println("players:")
        for (player in handState.players.sortedBy { it.seat }) {

            val playerStringBuilder = StringBuilder()

            if (player == handState.activePlayer)
                playerStringBuilder.append("!")

            playerStringBuilder.append("${player.seat}: ")

            if (player.seat == handState.positions.button)
                playerStringBuilder.append("D ")

            if (player.seat == handState.positions.smallBlind)
                playerStringBuilder.append("SB ")

            if (player.seat == handState.positions.bigBlind)
                playerStringBuilder.append("BB ")

            playerStringBuilder.append("\t")
            playerStringBuilder.append("stack: ${player.stack},\t")

            if (player.lastAction != null)
                playerStringBuilder.append("${player.lastAction} ")

            if (player.bet > 0)
                playerStringBuilder.append("${player.bet}\t")

            playerStringBuilder.append("\tcards: ${player.holeCards.joinToString(", ")}")
            println(playerStringBuilder.toString())
        }

        println("===============================================================")
        println()
        println()
    }

    fun requestBettingAction(): BettingAction {

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
