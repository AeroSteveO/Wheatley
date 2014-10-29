/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.DeckOfCards;
import Objects.PlayingCard;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.WaitForQueue;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 * Inspired By: http://math.hws.edu/javanotes/c5/s4.html
 *
 *
 *
 */
public class GameHighLow extends ListenerAdapter {
    
//I will finish this section by presenting a complete program that uses the Card and Deck classes.
//The program lets the user play a very simple card game called HighLow. A deck of cards is shuffled,
//and one card is dealt from the deck and shown to the user. The user predicts whether the next card from
//the deck will be higher or lower than the current card. If the user predicts correctly, then the next
//card from the deck becomes the current card, and the user makes another prediction.
//This continues until the user makes an incorrect prediction. The number of correct predictions is the user's score.
//
//My program has a static method that plays one game of HighLow. This method has a
//return value that represents the user's score in the game. The main() routine lets
//the user play several games of HighLow. At the end, it reports the user's average score.
//
//I won't go through the development of the algorithms used in this program, but I
//encourage you to read it carefully and make sure that you understand how it works.
//Note in particular that the subroutine that plays one game of HighLow returns the user's
//score in the game as its return value. This gets the score back to the main program,
//where it is needed. Here is the program:
    int time = 15;
    int basePrize = 15;
    
    @Override
    public void onMessage(MessageEvent event) throws InterruptedException, Exception {
        
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (message.startsWith(Global.commandPrefix)){
            
            String command = message.split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
            
            if (cmdSplit[0].equalsIgnoreCase("highlow")&&!Global.channels.areGamesBlocked(event.getChannel().getName())){
                
                int key=(int) (Math.random()*100000+1);
                int updateKey = (int) (Math.random()*100000+1);
                int counter = 0;
                boolean running = true;
                
                DeckOfCards deck = new DeckOfCards();
                deck.shuffle();
                PlayingCard currentCard = deck.dealCard();
                PlayingCard nextCard = deck.dealCard();
                
                int correctGuesses = 0;
                
                String gameChan = event.getChannel().getName();
                event.getBot().sendIRC().message(gameChan,"The first card is the "+currentCard.toString());
                event.getBot().sendIRC().message(gameChan,"Will the next card be higher (H) or lower (L)?");
                
                WaitForQueue queue = new WaitForQueue(event.getBot());
                
                while (running){
                    MessageEvent currentEvent = queue.waitFor(MessageEvent.class);
                    String currentMessage = Colors.removeFormattingAndColors(currentEvent.getMessage());
                    String currentChan = currentEvent.getChannel().getName();
//                    if (currentMessage.equalsIgnoreCase(Integer.toString(updateKey))){
//
//                        counter++;
//                    }
                    if (currentMessage.equalsIgnoreCase(Integer.toString(key))){
//                        event.getBot().sendIRC().message(currentChan,"You did not guess the solution in time, the correct answer would have been "+Colors.BOLD+Colors.RED);
                        queue.close();
                        if (correctGuesses>0){
                            GameControl.scores.addScore(currentEvent.getUser().getNick(), basePrize+correctGuesses*3);
                            event.getBot().sendIRC().message(gameChan,"Game over! You've run out of time. You made "+correctGuesses+" correct predictions and win $"+(basePrize+correctGuesses*3));
                        }
                        queue.close();
                        break;
                        
                    }
                    if (currentChan.equalsIgnoreCase(gameChan)){
                        if(currentMessage.equalsIgnoreCase("h")||currentMessage.equalsIgnoreCase("l")){
                            String guess = currentMessage;
                            event.getBot().sendIRC().message(gameChan,"The next card is the "+nextCard.toString());
                            if (nextCard.getValue()==currentCard.getValue()){
//                                event.getBot().sendIRC().message(gameChan,"The value is the same as the previous card. You lose on ties. Sorry!");
                                if (correctGuesses>0){
                                    GameControl.scores.addScore(currentEvent.getUser().getNick(), basePrize+correctGuesses*3);
                                    event.getBot().sendIRC().message(gameChan,"Game over! The value is the same as the previous card. You lose on ties. Sorry! You made "+correctGuesses+" correct predictions and win $"+(basePrize+correctGuesses*3));
                                }
                                else{
                                    event.getBot().sendIRC().message(gameChan,"Game over! Your prediction was incorrect. You made "+correctGuesses+" correct predictions. Sorry, but you haven't won anything.");
                                }
                                
                                queue.close();
                                break;
                                // END GAM HERA
                            }
                            else if( nextCard.getValue()>currentCard.getValue()){
                                if(guess.equalsIgnoreCase("h")){
                                    event.getBot().sendIRC().message(gameChan,"Your prediction was correct.");
                                    correctGuesses++;
                                }
                                else{
                                    event.getBot().sendIRC().message(gameChan,"Your prediction was incorrect.");
                                    if (correctGuesses>0){
                                        GameControl.scores.addScore(currentEvent.getUser().getNick(), basePrize+correctGuesses*3);
                                        event.getBot().sendIRC().message(gameChan,"Game over! Your prediction was incorrect. You made "+correctGuesses+" correct predictions and win $"+(basePrize+correctGuesses*3));
                                    }
                                    else{
                                        event.getBot().sendIRC().message(gameChan,"Game over! Your prediction was incorrect. You made "+correctGuesses+" correct predictions. Sorry, but you haven't won anything.");
                                    }
                                    
                                    queue.close();
                                    break;
                                    // END GAM HEAR
                                }
                            }
                            else{
                                if(guess.equalsIgnoreCase("l")){
                                    event.getBot().sendIRC().message(gameChan,"Your prediction was correct.");
                                    correctGuesses++;
                                }
                                else{
//                                    event.getBot().sendIRC().message(gameChan,"Your prediction was incorrect.");
                                    if (correctGuesses>0){
                                        GameControl.scores.addScore(currentEvent.getUser().getNick(), basePrize+correctGuesses*3);
                                        event.getBot().sendIRC().message(gameChan,"Game over! Your prediction was incorrect. You made "+correctGuesses+" correct predictions and win $"+(basePrize+correctGuesses*3));
                                    }
                                    else{
                                        event.getBot().sendIRC().message(gameChan,"Game over! Your prediction was incorrect. You made "+correctGuesses+" correct predictions. Sorry, but you haven't won anything.");
                                    }
                                    
                                    queue.close();
                                    break;
                                    // END GAM HEAR
                                }
                            }
                            currentCard = nextCard;
                            nextCard = deck.dealCard();
                            event.getBot().sendIRC().message(gameChan,"Your current card is the "+currentCard.toString()+". What will be your next guess?");
                        }
                    }
                }
            }
        }
    }
}