package core.adapters

import core.bettinground.*
import core.gameflow.handstate.HandState

class LocalPlayerAdapter(val playerId: Int) : IPlayerAdapter {

    override fun sendHandUpdate(handState: HandState) {
        print("pots: ")
        handState.pots
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