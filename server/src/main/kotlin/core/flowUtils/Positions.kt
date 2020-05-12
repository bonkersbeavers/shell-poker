package core.flowUtils

import core.RoomSettings
import core.handflow.HandState
import core.player.getByPosition
import core.player.next
import core.player.prev
import kotlin.random.Random

data class Positions(val button: Int, val smallBlind: Int, val bigBlind: Int)

private fun previousPosition(from: Int, maxPosition: Int): Int = if (from == 0) maxPosition else (from - 1)

fun setRandomPositions(builder: HandState.ImmutableBuilder, setting: RoomSettings): HandState.ImmutableBuilder {
    val playersSeats = builder.players!!.map { it.seat }.sorted()
    assert(playersSeats.count() >= 2)

    val positions = when (playersSeats.count()) {
        2 -> {
            val shuffledSeats = playersSeats.shuffled()
            Positions(button = shuffledSeats[0], smallBlind = shuffledSeats[0], bigBlind = shuffledSeats[1])
        }

        else -> {
            val randomShift = Random.nextInt(playersSeats.count())
            val shiftedSeats = playersSeats.drop(randomShift) + playersSeats.take(randomShift)
            Positions(button = shiftedSeats[0], smallBlind = shiftedSeats[1], bigBlind = shiftedSeats[2])
        }
    }

    return builder.copy(positions = positions)
}

fun shiftPositions(builder: HandState.ImmutableBuilder, setting: RoomSettings): HandState.ImmutableBuilder {
    val players = builder.players!!
    val oldPositions = builder.positions!!

    assert(players.size >= 2)

    val maxPosition = setting.seatsNumber - 1

    // Big blind always moves to the next player.
    val newBigBlindPosition = players.next(oldPositions.bigBlind).seat

    // In heads up game, button and small blind always point to the other player.
    if (builder.players.size == 2) {
        val otherPlayer = players.next(newBigBlindPosition)
        val newPositions = Positions(
                button = otherPlayer.seat,
                smallBlind = otherPlayer.seat,
                bigBlind = newBigBlindPosition
        )
        return builder.copy(positions = newPositions)
    }

    // When there are at least three players:

    // 1) new small blind position is determined first

    val oldBigBlindPlayer = players.getByPosition(oldPositions.bigBlind)
    val newSmallBlindPosition = when (oldBigBlindPlayer) {

        // If previous big blind player left the game, small blind will point to an empty seat
        // and should be placed right before the new big blind.
        null -> previousPosition(newBigBlindPosition, maxPosition)

        // Otherwise small blind always takes the previous big blind's position.
        else -> oldBigBlindPlayer.seat
    }

    // 2) button position is determined at the end

    val playerAfterOldButton = players.next(oldPositions.button)
    val newButtonPosition = when (playerAfterOldButton.seat) {

        // If moving the button to the next player results in button pointing to the same seat
        // as small blind, the button should be placed right before the small blind.
        newSmallBlindPosition -> previousPosition(newSmallBlindPosition, maxPosition)

        // This can happen only when new small blind is dead (points to an empty seat).
        // In such case the button should also be placed right before the small blind.
        newBigBlindPosition -> previousPosition(newSmallBlindPosition, maxPosition)

        // In any other case the button should point to the player that is seated right before
        // the small blind.
        else -> players.prev(newSmallBlindPosition).seat
    }

    val newPositions = Positions(
            newButtonPosition,
            newSmallBlindPosition,
            newBigBlindPosition
    )
    return builder.copy(positions = newPositions)
}
