package core

enum class CardRank(val strength: Int) {
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(11),
    QUEEN(12),
    KING(13),
    ACE(14);

    companion object {
        fun fromInt(strength: Int): CardRank = CardRank.values().find { it.strength == strength } !!
    }

    operator fun plus(inc: Int): CardRank = fromInt(strength + inc)
    operator fun rangeTo(other: CardRank): List<CardRank> = (strength..(other.strength)).map { fromInt(it) }
}

enum class CardSuit {
    SPADES,
    HEARTS,
    CLUBS,
    DIAMONDS
}

data class Card(val rank: CardRank, val suit: CardSuit)
