package core.pokerhands

import core.cards.Card
import core.cards.CardRank

fun makeHand(cards: Set<Card>): PokerHand {

    assert(cards.size == 5)

    return when {
        isRoyalFlush(cards) -> RoyalFlush(cards)
        isStraightFlush(cards) -> StraightFlush(cards)
        isFourOfAKind(cards) -> FourOfAKind(cards)
        isFullHouse(cards) -> FullHouse(cards)
        isFlush(cards) -> Flush(cards)
        isStraight(cards) -> Straight(cards)
        isThreeOfAKind(cards) -> ThreeOfAKind(cards)
        isTwoPair(cards) -> TwoPair(cards)
        isPair(cards) -> Pair(cards)
        else -> HighCard(cards)
    }
}

/**
 * Following methods check if given list of cards make certain poker hand,
 * cards parameter should always be a valid 5-card hand.
 */

fun isPair(cards: Set<Card>): Boolean {
    val rankCounts = cards.groupBy { it.rank }.map { it.value.size }
    return rankCounts.any { it == 2 }
}

fun isTwoPair(cards: Set<Card>): Boolean {
    val rankCounts = cards.groupBy { it.rank }.map { it.value.size }
    return rankCounts.count { it == 2 } == 2
}

fun isThreeOfAKind(cards: Set<Card>): Boolean {
    val rankCounts = cards.groupBy { it.rank }.map { it.value.size }
    return rankCounts.any { it == 3 }
}

fun isStraight(cards: Set<Card>): Boolean {

    val ranks = cards.map { it.rank }.sorted()
    val lowest = ranks.first()

    if (lowest > CardRank.TEN)
        return false

    return when (ranks) {
        (lowest..(lowest + 4)).toList() -> true
        (CardRank.TWO..CardRank.FIVE).toList() + CardRank.ACE -> true
        else -> false
    }
}

fun isFlush(cards: Set<Card>): Boolean = cards.distinctBy { it.suit }.size == 1

fun isFullHouse(cards: Set<Card>): Boolean {
    val rankCounts = cards.groupBy { it.rank }.map { it.value.size }
    return rankCounts.any { it == 2 } and rankCounts.any { it == 3 }
}

fun isFourOfAKind(cards: Set<Card>): Boolean {
    val rankCounts = cards.groupBy { it.rank }.map { it.value.size }
    return rankCounts.any { it == 4 }
}

fun isStraightFlush(cards: Set<Card>): Boolean = isStraight(cards) and isFlush(cards)

fun isRoyalFlush(cards: Set<Card>): Boolean {
    val isHighestStraight = cards.map { it.rank }.sorted() == (CardRank.TEN..CardRank.ACE).toList()
    return isFlush(cards) and isHighestStraight
}

/**
 * Performs kickers comparison. Returns positive value if the first list contains
 * better kickers, 0 if both lists are equally strong and negative value otherwise.
 * It is assumed that both lists are equally long.
 */
fun compareKickers(kickersA: Set<Card>, kickersB: Set<Card>): Int {
    val ranksA = kickersA.map { it.rank }.sortedDescending()
    val ranksB = kickersB.map { it.rank }.sortedDescending()

    (ranksA zip ranksB).forEach {
        if (it.first != it.second) return it.first.strength - it.second.strength
    }

    return 0
}

/** cards parameter should contain 5 cards that make valid straight. */
fun getStraightRank(cards: Set<Card>): CardRank {
    val ranks = cards.map { it.rank }.sorted()
    val lowestStraight = (CardRank.TWO..CardRank.FIVE).toList() + CardRank.ACE

    return when (ranks) {
        lowestStraight -> CardRank.FIVE
        else -> ranks.last()
    }
}

fun compareStraights(cardsA: Set<Card>, cardsB: Set<Card>): Int {
    val straightRankA = getStraightRank(cardsA)
    val straightRankB = getStraightRank(cardsB)

    return straightRankA.strength - straightRankB.strength
}
