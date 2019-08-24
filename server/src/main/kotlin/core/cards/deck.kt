package core.cards

private fun generateBaseDeck(): List<Card> {
        val mutableBaseDeck: MutableList<Card> = mutableListOf()

        for (suit: CardSuit in CardSuit.values())
                for (rank: CardRank in CardRank.values())
                        mutableBaseDeck.add(Card(rank, suit))

        return mutableBaseDeck.toList()
}

val baseDeck: List<Card> = generateBaseDeck()
