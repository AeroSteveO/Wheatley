/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.CardHand;
import Objects.DeckOfCards;
import Objects.QueueTime;
import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.WaitForQueue;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    CardHand
 *    DeckOfCards
 *    QueueTime
 *    TimedWaitForQueue
 * - Linked Classes
 *    Global
 *    GameControl
 *
 * Activate Commands with:
 *      !blackjack [minimum bet]
 *          Starts a game of blackjack with the minimum bet set to the input
 *          If nothing is input, the minimum bet is set to $10
 * 
 * 
 */
public class GameBlackjack extends ListenerAdapter {
    int time = 25; // SECONDS
    
    @Override
    public void onMessage(MessageEvent event) throws InterruptedException, Exception {
        
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (message.startsWith(Global.commandPrefix)){
            
            String command = message.split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
            
            if (cmdSplit[0].equalsIgnoreCase("blackjack")&&!Global.channels.areGamesBlocked(event.getChannel().getName())){
                
                String gameChan = event.getChannel().getName();
                
                if (!GameControl.activeGame.contains(gameChan, "blackjack")){
                    
                    int minBet = 10;
                    ArrayList<String> options = new ArrayList<>();
                    
                    // OPTIONS FOR THE GAME
                    if (cmdSplit.length>=2){
//                    if (cmdSplit[1].equalsIgnoreCase("single")){
                        for (int i=0;i<cmdSplit.length;i++){
//                        options.add(cmdSplit[1].toLowerCase());
                            
//                        if (cmdSplit.length>=3){
                            if (cmdSplit[i].matches("[0-9]+")){
                                minBet = Integer.parseInt(cmdSplit[2]);
                            }
                            
                            else if(cmdSplit[i].matches("\\-[0-9\\.]+")){
                                event.getBot().sendIRC().notice(event.getUser().getNick(),"You cannot place a negative/non-integer minimum bet");
                                return;
                            }
                            else if(cmdSplit[i].matches("[a-z]+")){
                                options.add(cmdSplit[i]);
                            }
                        }
//                        }
//                        }
                        
                        
//                    else if (cmdSplit[1].matches("[0-9]+")){
//                        minBet = Integer.parseInt(cmdSplit[1]);
//                    }
//
//                    else if(cmdSplit[1].matches("\\-[0-9\\.]+")){
//                        event.getBot().sendIRC().notice(event.getUser().getNick(),"You cannot place a negative/non-integer minimum bet");
//                        return;
//                    }
                    }
                    // END OPTIONS FOR THE GAME SETUP
                    
                    GameControl.activeGame.add(gameChan, "blackjack", "long");//Lets add the game to the array well after the input checks
                    
                    int key=(int) (Math.random()*100000+1);
//                    int updateKey = (int) (Math.random()*100000+1);
                    int counter = 1;
                    boolean running = true;
                    
                    String player = event.getUser().getNick();
                    ArrayList<String> users = new ArrayList<>();
                    users.add(event.getBot().getNick());
                    users.add(player);
                    
                    QueueTime gameUpdater = new QueueTime(event,time,key);
                    Thread t = new Thread(gameUpdater);
                    gameUpdater.giveT(t);
                    t.start();
                    
                    DeckOfCards deck = new DeckOfCards();
                    deck.shuffle();
                    ArrayList<CardHand> hands = new ArrayList<>();
                    
                    hands.add(new CardHand(event.getBot().getNick()));
                    hands.add(new CardHand(player));
                    
                    for (int i=0;i<2;i++){
                        for (int j=0;j<hands.size();j++){
                            hands.get(j).addCard(deck.dealCard());
                        }
                    }
//                    for (int i=0;i<hands.size();i++)
//                        hands.get(i).sortByValue();
                    
                    int idx = getHandByPlayer(hands, player);
                    event.getBot().sendIRC().message(gameChan,hands.get(counter).getPlayer()+"'s hand: "+hands.get(counter).toColoredString()+"Total: "+hands.get(counter).getBlackjackHandValue());
                    event.getBot().sendIRC().message(gameChan,"Will you hit (H) or stay (S)?");
                    
                    WaitForQueue queue = new WaitForQueue(event.getBot());
                    
                    while (running){
                        MessageEvent currentEvent = queue.waitFor(MessageEvent.class);
                        String currentMessage = Colors.removeFormattingAndColors(currentEvent.getMessage());
                        String currentChan = currentEvent.getChannel().getName();
                        
                        if (currentMessage.equalsIgnoreCase(Integer.toString(key))){
                            
                            
//                            counter++;
                            
                            if(users.isEmpty()){
                                event.getBot().sendIRC().message(gameChan,"Looks like nobody is around, Thanks for playing BlackJack! Come again soon!");
                                running = false;
                                queue.close();
                            }
                            else{
                                key=(int) (Math.random()*100000+1);
//                                updateKey = (int) (Math.random()*100000+1);
                            }
                        }
                        else if (currentChan.equalsIgnoreCase(gameChan)&&users.get(counter).equalsIgnoreCase(currentEvent.getUser().getNick())){
                            
                            if (currentMessage.equalsIgnoreCase("h")){
                                hands.get(getHandByPlayer(hands, player)).addCard(deck.dealCard());
                                event.getBot().sendIRC().message(gameChan,"Your hand: "+hands.get(counter).toColoredString()+"Total: "+hands.get(counter).getBlackjackHandValue());
                                
                                if (hands.get(counter).getBlackjackHandValue()>21){
                                    event.getBot().sendIRC().message(gameChan, "You have BUSTED! "+hands.get(counter).getPlayer()+"'s hand was " +hands.get(counter).toColoredString() + "Total: "+hands.get(counter).getBlackjackHandValue()+" You have lost $"+minBet);
                                    counter++;
//                                GameControl.scores.subtractScore(currentEvent.getUser().getNick(),minBet);
//                                queue.close();
//                                break;
                                }
                            }
                            
                            else if (currentMessage.equalsIgnoreCase("s")){
                                counter++;
                                if (counter<hands.size())
                                event.getBot().sendIRC().message(gameChan,hands.get(counter).getPlayer()+"'s turn, will you hit (h) or stay (s)?");
                            }
                            
                            else if (currentMessage.equalsIgnoreCase("!dealmeout")){
                                
                                
                            }
                            else if (currentMessage.equalsIgnoreCase("!fuckthis")||currentMessage.equalsIgnoreCase("i give up")){
                                event.respond("You have given up. " + Global.mainNick + "'s hand was "+hands.get(getHandByPlayer(hands, event.getBot().getNick())).toColoredString() + "Total: "+hands.get(getHandByPlayer(hands, event.getBot().getNick())).getBlackjackHandValue());
                                queue.close();
                                break;
                            }
                            
                            
//                            else if (hands.get(counter).getBlackjackHandValue()>21){
//                                event.getBot().sendIRC().message(gameChan, Global.mainNick+" has BUSTED! "+Global.mainNick+"'s hand was " +hands.get(getHandByPlayer(hands, event.getBot().getNick())).toColoredString() + "Total: "+hands.get(getHandByPlayer(hands, event.getBot().getNick())).getBlackjackHandValue());
////                                GameControl.scores.addScore(currentEvent.getUser().getNick(),minBet);
////                                queue.close();
////                                break;
//                            }
                            
//                            counter++;
                            
                            // Wheatley's Move
                            if (counter>=hands.size()){
                                counter = 0;
                                int derpyHand = getHandByPlayer(hands, event.getBot().getNick());
                                while (hands.get(derpyHand).getBlackjackHandValue()<=17){
                                    hands.get(derpyHand).addCard(deck.dealCard());
                                    event.getBot().sendIRC().message(gameChan,Global.mainNick+" hits: "+hands.get(derpyHand).toMaskedString());
                                }
                                if (hands.get(counter).getBlackjackHandValue()>21){
                                    event.getBot().sendIRC().message(gameChan, Global.mainNick+" has BUSTED! "+Global.mainNick+"'s hand was " +hands.get(counter).toColoredString() + "Total: "+hands.get(counter).getBlackjackHandValue());
                                }
                            }
                            
                            // Wheatley is the last player to go, if he's done, then all players are done
                            if (counter == 0){
                                
                                String winner="";
                                int winningValue=0;
                                
                                for (int i=0;i<hands.size();i++){
                                    
                                    if (hands.get(i).getBlackjackHandValue()<=21){
                                        if (hands.get(i).getBlackjackHandValue()>winningValue){
                                            winner = hands.get(i).getPlayer();
                                            winningValue = hands.get(i).getBlackjackHandValue();
                                        }
                                        else if (hands.get(i).getBlackjackHandValue()==winningValue){
                                            if (hands.get(i).getPlayer().equalsIgnoreCase(event.getBot().getNick())){
                                                winner = hands.get(i).getPlayer();
                                                winningValue = hands.get(i).getBlackjackHandValue();
                                            }
                                            else{
                                                winner = event.getBot().getNick();
                                                winningValue = 21;
                                            }
                                        }
                                    }
                                }
                                if (winner.equals(""))
                                    winner = event.getBot().getNick();
                                
                                event.getBot().sendIRC().message(gameChan,winner+" has won! The winning hand was "+ hands.get(getHandByPlayer(hands, winner)).toColoredString()+" earning $"+minBet*(hands.size()-1)+", everyone else has lost $"+minBet);
                                GameControl.scores.addScore(winner,minBet*(hands.size()-1));
                                
                                for (int i=0;i<users.size();i++){
                                    if (!hands.get(i).getPlayer().equalsIgnoreCase(winner)){
                                        GameControl.scores.subtractScore(hands.get(i).getPlayer(),minBet);
                                    }
                                }
                                queue.close();
                                break;
                            }
//                            else{
//                                event.getBot().sendIRC().message(gameChan,hands.get(counter).getPlayer()+"'s turn, will you hit (h) or stay (s)?");
//                            }
                            
                            
                        }
//                    else if (currentMessage.equalsIgnoreCase(Integer.toString(updateKey))){
//                        if(users.isEmpty()){
//
//                            event.getBot().sendIRC().message(gameChan,"Looks like nobody is around, Thanks for playing BlackJack! Come again soon!");
//                            running = false;
//
//                            queue.close();
//                        }
//
//                        else{
//
////
//                            key=(int) (Math.random()*100000+1);
//                            updateKey = (int) (Math.random()*100000+1);
//
//                            counter = 0;
//                        }
//                    }
                        
                    }
                    GameControl.activeGame.remove(gameChan,"blackjack"); //updated current index of the game
                }
                else
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Game Currently running in this channel");
            }
        }
    }
//    public ArrayList<CardHand> deal(ArrayList<CardHand> handArray){
//
//        for (int i=0;i<handArray.size();i++){
//            for (int j=0;j<2;j++){
//                handArray.get(j).addCard(deck.dealCard());
//            }
//        }
//    }
    
    private int getHandByPlayer(ArrayList<CardHand> hands, String player) {
        for (int i=0;i<hands.size();i++){
            if (hands.get(i).getPlayer().equalsIgnoreCase(player))
                return (i);
        }
        throw new NullPointerException("Player not in hands");
    }
}
