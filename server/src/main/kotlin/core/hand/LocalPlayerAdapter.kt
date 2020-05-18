package core.hand

import core.hand.player.betting.*

class LocalPlayerAdapter() {

    fun update(handState: HandState) {

        println("===================================== SHELL - POKER ===================================")

        println("cards: ${handState.communityCards.joinToString(", ")}")
        handState.pots.forEach { pot ->
            val prefix = if (pot.potNumber > 0) "side pot ${pot.potNumber}" else "main pot"
            println("$prefix: ${pot.size}")
        }
        println("players:")
        for (player in handState.playersStates.sortedBy { it.seat }) {

            val playerStringBuilder = StringBuilder()

            playerStringBuilder.append("${player.seat}:".padEnd(3))

            val positionString = when (player.seat) {
                handState.positions.button -> "D"
                handState.positions.smallBlind -> "SB"
                handState.positions.bigBlind -> "BB"
                else -> ""
            }

            val actionString = if (player == handState.activePlayer) " <<<" else ""

            playerStringBuilder.append((positionString + actionString).padEnd(12))

            playerStringBuilder.append("stack: ${player.stack}".padEnd(15))

            playerStringBuilder.append("cards: ${player.cards!!.toList().joinToString(", ")}".padEnd(43))

            if (player.currentActionType != null)
                playerStringBuilder.append("${player.currentActionType} ")

            if (player.currentBet > 0)
                playerStringBuilder.append("${player.currentBet}")

            println(playerStringBuilder.toString())
        }

        println("=======================================================================================")
        println()
        println()
    }

    fun requestBettingAction(playerSeat: Int): BettingAction {

        while (true) {
            print("your action > ")
            try {
                val command = readLine()!!
                return parseAction(command, playerSeat)
            } catch (e: IllegalArgumentException) {
                println(e.message)
            } catch (e: NumberFormatException) {
                println(e.message)
            } catch (e: IndexOutOfBoundsException) {
                println("error, maybe you forgot some arguments")
            }
        }
    }

    private fun parseAction(command: String, playerSeat: Int): BettingAction {
        val words = command.toUpperCase().split(" ")
        val actionString = words[0]

        return when (actionString) {
            "FOLD" -> Fold(playerSeat)
            "CHECK" -> Check(playerSeat)
            "CALL" -> Call(playerSeat)
            "BET" -> {
                val size = words[1].toInt()
                Bet(playerSeat, size)
            }
            "RAISE" -> {
                val size = words[1].toInt()
                Raise(playerSeat, size)
            }
            else -> throw IllegalArgumentException("could not parse any action from command '$command'")
        }
    }
}
