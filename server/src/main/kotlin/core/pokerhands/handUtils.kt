package core.pokerhands

import com.marcinmoskala.math.combinations
import core.cards.Card
import core.cards.CardRank

private inline fun <T> Set<Card>.fiveCardsMethod(f: () -> T): T {
    assert(this.size == 5)
    return f()
}

fun Set<Card>.toPokerHand(): PokerHand = fiveCardsMethod {

    return when {
        this.isRoyalFlush() -> RoyalFlush(this)
        this.isStraightFlush() -> StraightFlush(this)
        this.isFourOfAKind() -> FourOfAKind(this)
        this.isFullHouse() -> FullHouse(this)
        this.isFlush() -> Flush(this)
        this.isStraight() -> Straight(this)
        this.isThreeOfAKind() -> ThreeOfAKind(this)
        this.isTwoPair() -> TwoPair(this)
        this.isPair() -> Pair(this)
        else -> HighCard(this)
    }
}

fun Set<Card>.best5CardHand(): PokerHand = fiveCardsMethod {
    val possibleHands = this.combinations(5).map { it.toPokerHand() }
    return possibleHands.reduce { acc, next -> if (acc > next) acc else next }
}

/**
 * Following methods check if given set of cards make certain poker hand.
 */

fun Set<Card>.isHighCard(): Boolean = fiveCardsMethod {
    return true
}

fun Set<Card>.isPair(): Boolean = fiveCardsMethod {
    val rankCounts = this.groupBy { it.rank }.map { it.value.size }
    return rankCounts.any { it == 2 }
}

fun Set<Card>.isTwoPair(): Boolean = fiveCardsMethod {
    val rankCounts = this.groupBy { it.rank }.map { it.value.size }
    return rankCounts.count { it == 2 } == 2
}

fun Set<Card>.isThreeOfAKind(): Boolean = fiveCardsMethod {
    val rankCounts = this.groupBy { it.rank }.map { it.value.size }
    return rankCounts.any { it == 3 }
}

fun Set<Card>.isStraight(): Boolean = fiveCardsMethod {
    val ranks = this.map { it.rank }.sorted()
    val lowest = ranks.first()

    if (lowest > CardRank.TEN)
        return false

    return when (ranks) {
        (lowest..(lowest + 4)).toList() -> true
        (CardRank.TWO..CardRank.FIVE).toList() + CardRank.ACE -> true
        else -> false
    }
}

fun Set<Card>.isFlush(): Boolean = fiveCardsMethod {
    return this.distinctBy { it.suit }.size == 1
}

fun Set<Card>.isFullHouse(): Boolean = fiveCardsMethod {
    val rankCounts = this.groupBy { it.rank }.map { it.value.size }
    return rankCounts.any { it == 2 } and rankCounts.any { it == 3 }
}

fun Set<Card>.isFourOfAKind(): Boolean = fiveCardsMethod {
    val rankCounts = this.groupBy { it.rank }.map { it.value.size }
    return rankCounts.any { it == 4 }
}

fun Set<Card>.isStraightFlush(): Boolean = fiveCardsMethod {
    return this.isStraight() and this.isFlush()
}

fun Set<Card>.isRoyalFlush(): Boolean = fiveCardsMethod {
    val isHighestStraight = this.map { it.rank }.sorted() == (CardRank.TEN..CardRank.ACE).toList()
    return this.isFlush() and isHighestStraight
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
