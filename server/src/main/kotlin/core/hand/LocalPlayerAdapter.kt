package core.hand

import core.hand.betting.*
import core.hand.pot.AwardChips
import core.hand.pot.PotAction
import core.hand.showdown.MuckCards
import core.hand.showdown.ShowCards
import core.hand.showdown.ShowdownAction

class LocalPlayerAdapter() {

    fun update(handState: HandState) {

        println("===================================== SHELL - POKER ===================================")

        println("cards:\n${handState.communityCards.joinToString(", ")}")
        handState.pots.forEach { pot ->
            val prefix = if (pot.potNumber > 0) "side pot ${pot.potNumber}" else "main pot"
            println("$prefix: ${pot.size}")
        }
        println("players:")
        for (player in handState.players.sortedBy { it.seat }) {

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

    fun printShowdown(showdownSequence: List<ShowdownAction>) {
        println("+++ SHOWDOWN +++")
        for (action in showdownSequence) {
            val showdownString = when {
                action is ShowCards -> "player ${action.seat} shows ${action.cards.first}, ${action.cards.second}"
                else -> "player ${action.seat} mucks"
            }

            println(showdownString)
        }
    }

    fun printPotDistribution(potActionSequence: List<PotAction>) {
        println("+++ RESULTS +++")
        for (action in potActionSequence) {
            if (action is AwardChips) {
                val awardString = "player ${action.playerSeat} wins ${action.chips} chips from"
                val potString = when (action.potNumber) {
                    0 -> "main pot"
                    else -> "${action.potNumber} side pot"
                }

                println("$awardString $potString")
            }
        }
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
