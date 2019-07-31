package core.cards

import kotlin.random.Random

val baseDeck: List<Card> = listOf(
        Card(CardRank.ACE, CardSuit.HEARTS),
        Card(CardRank.TWO, CardSuit.HEARTS),
        Card(CardRank.THREE, CardSuit.HEARTS),
        Card(CardRank.FOUR, CardSuit.HEARTS),
        Card(CardRank.FIVE, CardSuit.HEARTS),
        Card(CardRank.SIX, CardSuit.HEARTS),
        Card(CardRank.SEVEN, CardSuit.HEARTS),
        Card(CardRank.EIGHT, CardSuit.HEARTS),
        Card(CardRank.NINE, CardSuit.HEARTS),
        Card(CardRank.TEN, CardSuit.HEARTS),
        Card(CardRank.JACK, CardSuit.HEARTS),
        Card(CardRank.QUEEN, CardSuit.HEARTS),
        Card(CardRank.KING, CardSuit.HEARTS),
        Card(CardRank.ACE, CardSuit.DIAMONDS),
        Card(CardRank.TWO, CardSuit.DIAMONDS),
        Card(CardRank.THREE, CardSuit.DIAMONDS),
        Card(CardRank.FOUR, CardSuit.DIAMONDS),
        Card(CardRank.FIVE, CardSuit.DIAMONDS),
        Card(CardRank.SIX, CardSuit.DIAMONDS),
        Card(CardRank.SEVEN, CardSuit.DIAMONDS),
        Card(CardRank.EIGHT, CardSuit.DIAMONDS),
        Card(CardRank.NINE, CardSuit.DIAMONDS),
        Card(CardRank.TEN, CardSuit.DIAMONDS),
        Card(CardRank.JACK, CardSuit.DIAMONDS),
        Card(CardRank.QUEEN, CardSuit.DIAMONDS),
        Card(CardRank.KING, CardSuit.DIAMONDS),
        Card(CardRank.ACE, CardSuit.SPADES),
        Card(CardRank.TWO, CardSuit.SPADES),
        Card(CardRank.THREE, CardSuit.SPADES),
        Card(CardRank.FOUR, CardSuit.SPADES),
        Card(CardRank.FIVE, CardSuit.SPADES),
        Card(CardRank.SIX, CardSuit.SPADES),
        Card(CardRank.SEVEN, CardSuit.SPADES),
        Card(CardRank.EIGHT, CardSuit.SPADES),
        Card(CardRank.NINE, CardSuit.SPADES),
        Card(CardRank.TEN, CardSuit.SPADES),
        Card(CardRank.JACK, CardSuit.SPADES),
        Card(CardRank.QUEEN, CardSuit.SPADES),
        Card(CardRank.KING, CardSuit.SPADES),
        Card(CardRank.ACE, CardSuit.CLUBS),
        Card(CardRank.TWO, CardSuit.CLUBS),
        Card(CardRank.THREE, CardSuit.CLUBS),
        Card(CardRank.FOUR, CardSuit.CLUBS),
        Card(CardRank.FIVE, CardSuit.CLUBS),
        Card(CardRank.SIX, CardSuit.CLUBS),
        Card(CardRank.SEVEN, CardSuit.CLUBS),
        Card(CardRank.EIGHT, CardSuit.CLUBS),
        Card(CardRank.NINE, CardSuit.CLUBS),
        Card(CardRank.TEN, CardSuit.CLUBS),
        Card(CardRank.JACK, CardSuit.CLUBS),
        Card(CardRank.QUEEN, CardSuit.CLUBS),
        Card(CardRank.KING, CardSuit.CLUBS)
        )

fun shuffledDeck(seed: Int? = null): List<Card> {
        return when(seed) {
                null -> baseDeck.shuffled()
                else -> baseDeck.shuffled(Random(seed))
        }
}
