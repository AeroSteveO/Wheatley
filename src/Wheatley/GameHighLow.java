/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.DeckOfCards;
import Objects.PlayingCard;
import Objects.QueueTime;
import Utils.GameUtils;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.WaitForQueue;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 * Inspired By: http://math.hws.edu/javanotes/c5/s4.html
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    DeckOfCards
 *    PlayingCard
 *    QueueTime
 * - Linked Classes
 *    Global
 *    GameControl
 *
 * Activate Command with:
 *      !highlow
 *
 *
 */
public class GameHighLow extends ListenerAdapter {
    
    int time = 10;
    int basePrize = 15;
    
    @Override
    public void onMessage(MessageEvent event) throws InterruptedException, Exception {
        
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        
        if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}"+Global.commandPrefix+"{1,}[ ]{0,}){1,}")){
            
            String command = message.split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
            
            if (cmdSplit[0].equalsIgnoreCase("highlow")&&!GameUtils.areGamesBlocked(event.getChannel().getName())){
                
                if (!GameControl.activeGame.isGameActive(event.getChannel().getName(), "highlow", "long")){
                    
                    int key=(int) (Math.random()*100000+1);
                    boolean running = true;
                    
                    DeckOfCards deck = new DeckOfCards();
                    deck.shuffle();
                    PlayingCard currentCard = deck.dealCard();
                    PlayingCard nextCard = deck.dealCard();
                    
                    String prevPlayer = event.getUser().getNick();
                    
                    QueueTime gameUpdater = new QueueTime(event,time,key);
                    Thread t = new Thread(gameUpdater);
                    gameUpdater.giveT(t);
                    t.start();
                    
                    int correctGuesses = 0;
                    
                    String gameChan = event.getChannel().getName();
                    event.getBot().sendIRC().message(gameChan,"The first card is the "+currentCard.toColoredString());
                    event.getBot().sendIRC().message(gameChan,"Will the next card be higher (H) or lower (L)?");
                    
                    WaitForQueue queue = new WaitForQueue(event.getBot());
                    
                    while (running){
                        
                        MessageEvent currentEvent = queue.waitFor(MessageEvent.class);
                        String currentMessage = Colors.removeFormattingAndColors(currentEvent.getMessage());
                        String currentChan = currentEvent.getChannel().getName();
                        
                        if (currentMessage.equalsIgnoreCase(Integer.toString(key))){
                            
                            if (correctGuesses>0){
                                GameControl.scores.addScore(prevPlayer, basePrize+correctGuesses*correctGuesses);
                                event.getBot().sendIRC().message(gameChan,"Game over! You've run out of time. The card was "+nextCard.toColoredString()+" You made "+correctGuesses+" correct predictions and win $"+(basePrize+correctGuesses*correctGuesses));
                                
                            }
                            else
                                event.getBot().sendIRC().message(gameChan,"Game over! You've run out of time. The card was "+nextCard.toColoredString()+" You made "+correctGuesses+" correct predictions. Sorry, but you haven't won anything.");
                            
                            queue.close();
                            break;
                            
                        }
                        else if (currentChan.equalsIgnoreCase(gameChan)){
                            if(currentMessage.equalsIgnoreCase("h")||currentMessage.equalsIgnoreCase("l")){
                                
                                prevPlayer = currentEvent.getUser().getNick();
                                
                                if (nextCard.getValue()==currentCard.getValue()){
                                    if (correctGuesses>0){
                                        GameControl.scores.addScore(currentEvent.getUser().getNick(), basePrize+correctGuesses*correctGuesses);
                                        event.getBot().sendIRC().message(gameChan,"Game over! The value is the same as the previous card. You lose on ties. Sorry! "+prevPlayer+", you made "+correctGuesses+" correct predictions and win $"+(basePrize+correctGuesses*correctGuesses));
                                    }
                                    else{
                                        event.getBot().sendIRC().message(gameChan,"Game over! The value is the same as the previous card. You lose on ties. Sorry! "+prevPlayer+", you made "+correctGuesses+" correct predictions. Sorry, but you haven't won anything.");
                                    }
                                    
                                    queue.close();
                                    break;
                                    // END GAME HERE
                                }
                                else if( nextCard.getValue()>currentCard.getValue()){
                                    if(currentMessage.equalsIgnoreCase("h")){
                                        event.getBot().sendIRC().message(gameChan,"The next card is the "+nextCard.toColoredString()+" Your prediction was correct. What will be your next guess?");
                                        correctGuesses++;
                                    }
                                    else{
                                        if (correctGuesses>0){
                                            GameControl.scores.addScore(currentEvent.getUser().getNick(), basePrize+correctGuesses*correctGuesses);
                                            event.getBot().sendIRC().message(gameChan,"Game over! Your prediction was incorrect. The card was "+nextCard.toColoredString()+". "+prevPlayer+", you made "+correctGuesses+" correct predictions and win $"+(basePrize+correctGuesses*correctGuesses));
                                        }
                                        else{
                                            event.getBot().sendIRC().message(gameChan,"Game over! Your prediction was incorrect. The card was "+nextCard.toColoredString()+". "+prevPlayer+", you made "+correctGuesses+" correct predictions. Sorry, but you haven't won anything.");
                                        }
                                        queue.close();
                                        break;
                                        // END GAME HERE
                                    }
                                }
                                else{
                                    if(currentMessage.equalsIgnoreCase("l")){
                                        event.getBot().sendIRC().message(gameChan,"The next card is the "+nextCard.toColoredString()+" Your prediction was correct. What will be your next guess?");
                                        correctGuesses++;
                                    }
                                    else{
                                        if (correctGuesses>0){
                                            GameControl.scores.addScore(currentEvent.getUser().getNick(), basePrize+correctGuesses*correctGuesses);
                                            event.getBot().sendIRC().message(gameChan,"Game over! Your prediction was incorrect. The card was "+nextCard.toColoredString()+". "+prevPlayer+", you made "+correctGuesses+" correct predictions and win $"+(basePrize+correctGuesses*correctGuesses));
                                        }
                                        else{
                                            event.getBot().sendIRC().message(gameChan,"Game over! Your prediction was incorrect. The card was "+nextCard.toColoredString()+". "+prevPlayer+", you made "+correctGuesses+" correct predictions. Sorry, but you haven't won anything.");
                                        }
                                        
                                        queue.close();
                                        break;
                                        // END GAME HERE
                                    }
                                }
                                
                                if(deck.cardsLeft()==0){
                                    GameControl.scores.addScore(currentEvent.getUser().getNick(), basePrize+correctGuesses*correctGuesses);
                                    event.getBot().sendIRC().message(gameChan,"Game over! You've completed the deck! "+prevPlayer+", you made "+correctGuesses+" correct predictions and win $"+(basePrize+correctGuesses*correctGuesses));
                                    queue.close();
                                    break;
                                }
                                
                                key = (int) (Math.random()*100000+1);
                                gameUpdater.interrupt();
                                gameUpdater = new QueueTime(event,time,key);
                                t = new Thread(gameUpdater);
                                gameUpdater.giveT(t);
                                t.start();
                                
                                currentCard = nextCard;
                                nextCard = deck.dealCard();
                            }
                            
                            else if (currentEvent.getMessage().equals("!fuckthis")||currentEvent.getMessage().equalsIgnoreCase("I give up")){
                                if (correctGuesses>0){
                                    GameControl.scores.addScore(prevPlayer, basePrize+correctGuesses*correctGuesses);
                                    event.respond("You have given up. The card was "+nextCard.toColoredString()+" You made "+correctGuesses+" correct predictions and win $"+(basePrize+correctGuesses*correctGuesses));
                                    
                                }
                                else
                                    event.respond("You have given up. The card was "+nextCard.toColoredString()+" You made "+correctGuesses+" correct predictions. Sorry, but you haven't won anything.");
                                
                                queue.close();
                                break;
                            }
                        }
                    }
                    GameControl.activeGame.remove(gameChan,"highlow"); //updated current index of the game
                }
                else
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Game Currently running in this channel");
            }
        }
    }
}