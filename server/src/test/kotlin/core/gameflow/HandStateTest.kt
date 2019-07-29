 package core.gameflow

 import core.action.bettinground.ActionType
 import core.action.bettinground.AllIn
 import org.junit.jupiter.api.Test
 import org.junit.jupiter.api.TestInstance
 import org.junit.jupiter.api.assertThrows
 import java.lang.AssertionError

 @TestInstance(TestInstance.Lifecycle.PER_CLASS)
 class HandStateTest {

     @Test
     fun `HandState instantiation should fail if there are less than 2 players`() {
         assertThrows<AssertionError> {

             val player = Player(position = 0, stack = 100)

             HandState(
                     players = listOf(player),
                     blinds = Blinds(50, 100),
                     buttonPosition = 0,
                     activePlayer = player
             )
         }
     }

     @Test
     fun `HandState instantiation should fail if players' positions are not unique`() {
         assertThrows<AssertionError> {

             val player0 = Player(position = 0, stack = 100)
             val player1 = Player(position = 1, stack = 100)
             val player2 = Player(position = 1, stack = 100)

             HandState(
                     players = listOf(player0, player1, player2),
                     blinds = Blinds(50, 100),
                     buttonPosition = 0,
                     activePlayer = null
             )
         }
     }

     @Test
     fun `HandState instantiation should fail if active player doesn't point to any of the players`() {
         assertThrows<AssertionError> {

             val player0 = Player(position = 0, stack = 100)
             val player1 = Player(position = 1, stack = 100)
             val player2 = Player(position = 2, stack = 100)

             HandState(
                     players = listOf(player0, player1),
                     blinds = Blinds(50, 100),
                     buttonPosition = 0,
                     activePlayer = player2
             )
         }
     }

     @Test
     fun `HandState instantiation should fail if last aggressor doesn't point to any of the players`() {
         assertThrows<AssertionError> {

             val player0 = Player(position = 0, stack = 100)
             val player1 = Player(position = 1, stack = 100)
             val player2 = Player(position = 2, stack = 100)

             HandState(
                     players = listOf(player0, player1),
                     blinds = Blinds(50, 100),
                     buttonPosition = 0,
                     activePlayer = player1,
                     lastAggressor = player2
             )
         }
     }

     @Test
     fun `pot should properly sum players' bets from previous rounds`() {

         val player0 = Player(position = 0, stack = 0, chipsInPot = 50)
         val player1 = Player(position = 1, stack = 100, chipsInPot = 150)
         val player2 = Player(position = 2, stack = 100, chipsInPot = 150)

         val state = HandState(
                 players = listOf(player0, player1, player2),
                 blinds = Blinds(50, 100),
                 buttonPosition = 0,
                 activePlayer = player1
         )

         assert(state.pot == 50 + 150 + 150)
     }

     @Test
     fun `total bet should return the highest bet made so far in given betting round`() {

         val player0 = Player(position = 0, stack = 0)
         val player1 = Player(position = 1, stack = 100)
         val player2 = Player(position = 2, stack = 100)

         val state = HandState(
                 players = listOf(player0, player1, player2),
                 blinds = Blinds(50, 100),
                 buttonPosition = 0,
                 activePlayer = player1,
                 lastLegalBet = 100,
                 extraBet = 75
         )

         assert(state.totalBet == 100 + 75)
     }

     @Test
     fun `playersInGame should find all players that haven't folded yet`() {

         val player0 = Player(position = 0, stack = 0, lastAction = ActionType.ALL_IN)
         val player1 = Player(position = 1, stack = 100, lastAction = ActionType.FOLD)
         val player2 = Player(position = 2, stack = 100)
         val player3 = Player(position = 3, stack = 100)

         val state = HandState(
                 players = listOf(player0, player1, player2, player3),
                 blinds = Blinds(50, 100),
                 buttonPosition = 0,
                 activePlayer = player2,
                 lastLegalBet = 100,
                 extraBet = 75
         )

         assert(state.playersInGame == listOf(player0, player2, player3))
     }

     @Test
     fun `decisivePlayers should find all players that can potentially make decisions`() {

         val player0 = Player(position = 0, stack = 0, lastAction = ActionType.ALL_IN)
         val player1 = Player(position = 1, stack = 100, lastAction = ActionType.FOLD)
         val player2 = Player(position = 2, stack = 100)
         val player3 = Player(position = 3, stack = 100)

         val state = HandState(
                 players = listOf(player0, player1, player2, player3),
                 blinds = Blinds(50, 100),
                 buttonPosition = 0,
                 activePlayer = player2,
                 lastLegalBet = 100,
                 extraBet = 75
         )

         assert(state.decisivePlayers == listOf(player2, player3))
     }

     @Test
     fun `HandState should properly identify players on blind positions when there are more than two players`() {

         val player0 = Player(position = 0, stack = 100)
         val player1 = Player(position = 1, stack = 100)
         val player2 = Player(position = 2, stack = 100) // BTN
         val player3 = Player(position = 3, stack = 100)

         val state = HandState(
                 players = listOf(player0, player1, player2, player3),
                 blinds = Blinds(50, 100),
                 buttonPosition = 2,
                 activePlayer = player3
         )

         assert(state.smallBlindPlayer == player3)
         assert(state.bigBlindPlayer == player0)
     }

     @Test
     fun `HandState should properly identify players on blind positions when the game is heads up`() {

         val player0 = Player(position = 0, stack = 100)
         val player1 = Player(position = 1, stack = 100) // BTN

         val state = HandState(
                 players = listOf(player0, player1),
                 blinds = Blinds(50, 100),
                 buttonPosition = 1,
                 activePlayer = player0
         )

         assert(state.smallBlindPlayer == player1)
         assert(state.bigBlindPlayer == player0)
     }

     @Test
     fun `updateActivePlayer should properly substitute active player in players list`() {

         val player0 = Player(position = 0, stack = 100)
         val player1 = Player(position = 1, stack = 100)
         val player2 = Player(position = 2, stack = 100)
         val player3 = Player(position = 3, stack = 100)

         val state = HandState(
                 players = listOf(player0, player1, player2, player3),
                 blinds = Blinds(50, 100),
                 buttonPosition = 2,
                 activePlayer = player3
         )

         val newPlayer = player3.copy(stack = 0, bet = 100)
         val newState = state.updateActivePlayer(newPlayer)

         assert(newState.players[3] == newPlayer)
     }

     @Test
     fun `orderedPlayers should return players list in clockwise order`() {

         val player0 = Player(position = 0, stack = 100)
         val player1 = Player(position = 1, stack = 100)
         val player2 = Player(position = 2, stack = 100)
         val player3 = Player(position = 3, stack = 100)

         val state = HandState(
                 players = listOf(player0, player1, player2, player3),
                 blinds = Blinds(50, 100),
                 buttonPosition = 2,
                 activePlayer = player3
         )

         assert(state.orderedPlayers(2) == listOf(player2, player3, player0, player1))
     }

     @Test
     fun `nextPlayer should find the first player after given position`() {

         val player0 = Player(position = 0, stack = 100)
         val player1 = Player(position = 1, stack = 100)
         val player2 = Player(position = 2, stack = 100)
         val player3 = Player(position = 3, stack = 100)

         val state = HandState(
                 players = listOf(player0, player1, player2, player3),
                 blinds = Blinds(50, 100),
                 buttonPosition = 2,
                 activePlayer = player3
         )

         assert(state.nextPlayer(0) == player1)
         assert(state.nextPlayer(3) == player0)
     }

     @Test
     fun `nextDecisivePlayer should find the first decisive player after given position`() {

         val player0 = Player(position = 0, stack = 100)
         val player1 = Player(position = 1, stack = 100, lastAction = ActionType.FOLD)
         val player2 = Player(position = 2, stack = 100)
         val player3 = Player(position = 3, stack = 0, lastAction = ActionType.ALL_IN)

         val state = HandState(
                 players = listOf(player0, player1, player2, player3),
                 blinds = Blinds(50, 100),
                 buttonPosition = 2,
                 activePlayer = player3
         )

         assert(state.nextDecisivePlayer(0) == player2)
         assert(state.nextDecisivePlayer(3) == player0)
     }
 }
