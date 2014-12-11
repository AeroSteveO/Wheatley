/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.CardHand;
import Objects.DeckOfCards;
import Objects.MultiplayerArrayGetQueue;
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
 *    MultiplayerArrayGetQueue
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
 * If the dealer does not bust, each remaining bet wins if its hand is higher than the 
 * dealer's, and loses if it is lower. In the case of a tied score, known as "push" or 
 * "standoff", bets are normally returned without adjustment; however, a blackjack beats 
 * any hand that is not a blackjack, even one with a value of 21. An outcome of blackjack 
 * vs. blackjack results in a push. Wins are paid out at 1:1, or equal to the wager, 
 * except for winning blackjacks, which are traditionally paid at 3:2 (meaning the player 
 * receives three dollars for every two bet), or one-and-a-half times the wager. Many 
 * casinos today pay blackjacks at less than 3:2 at some tables.[4] 
 * 
 * also blackjacks should get higher payout
 * In the modern game, a blackjack refers to any hand of an ace plus a ten or face card, regardless of suits or colours.[3]
 * 
 */
public class GameBlackjack extends ListenerAdapter {
    private final int time = 25; // Seconds before a user is kicked from a game due to inactivity (when its their turn)
    private final int maxUsers = 6; // Maximum number of players in one game of blackjack
    
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
                    String options = null;
                    
                    // OPTIONS FOR THE GAME
                    if (cmdSplit.length>1&&cmdSplit.length<=3){
                        for (int i=1;i<cmdSplit.length;i++){
                            
                            if (cmdSplit[i].matches("[0-9]+")){
                                minBet = Integer.parseInt(cmdSplit[i]);
                            }
                            
                            else if(cmdSplit[i].matches("\\-[0-9\\.]+")){
                                event.getBot().sendIRC().notice(event.getUser().getNick(),"The minimum bet must be a non-negative integer");
                                return;
                            }
                            
                            else if(cmdSplit[i].matches("[a-z]+")){
                                if (cmdSplit[i].equalsIgnoreCase("multi")||cmdSplit[i].equalsIgnoreCase("single")){
                                    options = cmdSplit[i];
                                }
                                else{
                                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Unknown option. Available options include 'Single' and 'Multi'");
                                    return;
                                }
                            }
                        }
                    }
                    else if (cmdSplit.length>3){
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"BlackJack accepts at most 2 options");
                    }// END OPTIONS PARSING
                    
                    
                    if (GameControl.scores.getScore(event.getUser().getNick())<minBet){
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"You don't have enough money to play blackjack with a minimum bet of $"+minBet);
                    }
                    else{
                        
                        GameControl.activeGame.add(gameChan, "blackjack", "long");//Lets add the game to the array well after the input checks
                        
                        int key=(int) (Math.random()*100000+1);
                        int counter = 1; // Current player number, as Wheatley (dealer) is 0, starts at 1
                        int prev = 0;
                        int busted  = 0; // Number of people who've busted their hand
                        boolean running = true;
                        
                        ArrayList<String> users = new ArrayList<>();
                        users.add(event.getBot().getNick());   // BOT is user 0
                        users.add(event.getUser().getNick());  // Any other users are added after the dealer/bot
                        
                        if (options!=null){
                            if (options.equalsIgnoreCase("multi")){
                                event.getBot().sendIRC().message(gameChan,event.getUser().getNick()+" wants to play blackjack, type !dealmein to join");
                                MultiplayerArrayGetQueue getPlayers = new MultiplayerArrayGetQueue(event,"dealmein",25,maxUsers-2);
                                getPlayers.setScoringRequirements(GameControl.scores,minBet);
                                users.addAll(getPlayers.getPlayers());
                            }
                        }
                        
                        
                        QueueTime gameUpdater = new QueueTime(event,time,key);
                        Thread t = new Thread(gameUpdater);
                        gameUpdater.giveT(t);
                        t.start();
                        
                        DeckOfCards deck = new DeckOfCards();
                        deck.shuffle();
                        ArrayList<CardHand> hands = new ArrayList<>();
                        
                        for (int i=0;i<users.size();i++){
                            hands.add(new CardHand(users.get(i)));  // Bot is user 0
                        }
                        
                        // DEAL CARDS TO ALL PLAYERS AND DEALER
                        for (int i=0;i<2;i++){
                            for (int j=0;j<hands.size();j++){
                                hands.get(j).addCard(deck.dealCard());
                            }
                        }// END DEALING
                        
                        if (hands.size()>2){ // MASK HANDS WHEN THERE ARE MULTIPLE PLAYERS IN ONE CHANNEL
                            event.getBot().sendIRC().notice(hands.get(counter).getPlayer(),"Your hand: "+hands.get(counter).toColoredString()+"Total: "+hands.get(counter).getBlackjackHandValue());
                            event.getBot().sendIRC().message(gameChan,hands.get(counter).getPlayer()+"'s turn, will you hit (h) or stay (s)?");
                            prev = counter;
                        }else{
                            event.getBot().sendIRC().message(gameChan,"Your hand: "+hands.get(counter).toColoredString()+"Total: "+hands.get(counter).getBlackjackHandValue()+" Will you hit (h) or stay (s)?");
                        }
                        
                        WaitForQueue queue = new WaitForQueue(event.getBot());
                        
                        while (running){
                            
                            MessageEvent currentEvent = queue.waitFor(MessageEvent.class);
                            String currentMessage = Colors.removeFormattingAndColors(currentEvent.getMessage());
                            String currentChan = currentEvent.getChannel().getName();
                            
                            if (currentMessage.equalsIgnoreCase(Integer.toString(key))){
                                
                                for (int i=0;i<users.size();i++){
                                    if (users.get(i).equalsIgnoreCase(hands.get(counter).getPlayer())){
                                        users.remove(i);
                                        break;
                                    }
                                }
                                event.getBot().sendIRC().message(gameChan,hands.get(counter).getPlayer()+"'s turn will be skipped and they will be dealt out next hand due to inactivity");
                                counter++;
                                
                                if (users.size()>1&&counter<hands.size()){
                                    if (hands.size()>2){ // HIDE YOUR HAND IF THERES MORE THAN JUST TWO PLAYERS (PLAYER AND BOT)
                                        event.getBot().sendIRC().notice(hands.get(counter).getPlayer(),"Your hand: "+hands.get(counter).toColoredString()+"Total: "+hands.get(counter).getBlackjackHandValue());
                                        if (prev!=counter){
                                            event.getBot().sendIRC().message(gameChan,hands.get(counter).getPlayer()+"'s turn, will you hit (h) or stay (s)?");
                                            prev=counter;
                                        }
                                    }
                                    else{
                                        event.getBot().sendIRC().message(gameChan,"Your hand: "+hands.get(counter).toColoredString()+"Total: "+hands.get(counter).getBlackjackHandValue()+" Will you hit (h) or stay (s)?");
                                    }
                                }
                                
                                key=(int) (Math.random()*100000+1);
                                gameUpdater.interrupt();
                                gameUpdater = new QueueTime(event,time,key);
                                t = new Thread(gameUpdater);
                                gameUpdater.giveT(t);
                                t.start();

                            }
                            else if(currentChan.equalsIgnoreCase(gameChan)){
                                if (hands.get(counter).getPlayer().equalsIgnoreCase(currentEvent.getUser().getNick())){
                                    if (currentMessage.equalsIgnoreCase("h")||currentMessage.equalsIgnoreCase("s")){
                                        
                                        if (currentMessage.equalsIgnoreCase("h")){
                                            hands.get(counter).addCard(deck.dealCard());
                                            
                                            if (hands.get(counter).getBlackjackHandValue()>21){
                                                event.getBot().sendIRC().message(gameChan, "You have BUSTED! "+hands.get(counter).getPlayer()+"'s hand was " +hands.get(counter).toColoredString() + "Total: "+hands.get(counter).getBlackjackHandValue());
                                                counter++;
                                                busted++;
                                            }
                                        }// END HIT
                                        
                                        else if (currentMessage.equalsIgnoreCase("s")){
                                            counter++;
                                        }// END STAY
                                        
                                        if (counter<hands.size()){ // SHOW THE PLAYER THEIR HAND
                                            if (hands.size()>2){ // HIDE YOUR HAND IF THERES MORE THAN JUST TWO PLAYERS (PLAYER AND BOT)
                                                event.getBot().sendIRC().notice(hands.get(counter).getPlayer(),"Your hand: "+hands.get(counter).toColoredString()+"Total: "+hands.get(counter).getBlackjackHandValue());
                                                if (prev!=counter){
                                                    event.getBot().sendIRC().message(gameChan,hands.get(counter).getPlayer()+"'s turn, will you hit (h) or stay (s)?");
                                                    prev=counter;
                                                }
                                            }
                                            else{
                                                event.getBot().sendIRC().message(gameChan,"Your hand: "+hands.get(counter).toColoredString()+"Total: "+hands.get(counter).getBlackjackHandValue()+" Will you hit (h) or stay (s)?");
                                            }
                                        }// END CURRENT HAND DISPLAY
                                        
                                        key=(int) (Math.random()*100000+1);
                                        gameUpdater.interrupt();
                                        gameUpdater = new QueueTime(event,time,key);
                                        t = new Thread(gameUpdater);
                                        gameUpdater.giveT(t);
                                        t.start();
                                        
                                    }
                                    
                                    else if (currentMessage.equalsIgnoreCase("!dealmeout")){
                                        boolean removed = false;
                                        for (int i=0;i<users.size();i++){
                                            if (users.get(i).equalsIgnoreCase(currentEvent.getUser().getNick())){
                                                users.remove(i);
                                                event.getBot().sendIRC().notice(currentEvent.getUser().getNick(),"You will be dealt out of the next hand");
                                                break;
                                            }
                                        }
                                        if (removed)
                                            event.getBot().sendIRC().notice(currentEvent.getUser().getNick(),"You're is not currently an active player in this game");
                                    }// END REMOVE USER FROM CURRENT GAME
                                    
                                    
                                    
                                    else if (currentMessage.equalsIgnoreCase("!fuckthis")||currentMessage.equalsIgnoreCase("i give up")){
                                        if (hands.size()==2){
                                            currentEvent.respond("You have given up. " + Global.mainNick + "'s hand was "+hands.get(getHandByPlayer(hands, event.getBot().getNick())).toColoredString() + "Total: "+hands.get(getHandByPlayer(hands, event.getBot().getNick())).getBlackjackHandValue());
                                            queue.close();
                                            break;
                                        }
                                        else{
                                            currentEvent.respond("You have given up. ");
                                            for (int i=0;i<users.size();i++){
                                                if (users.get(i).equalsIgnoreCase(currentEvent.getUser().getNick())){
                                                    users.remove(i);
                                                    event.getBot().sendIRC().notice(currentEvent.getUser().getNick(),"You will be dealt out of the next hand");
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                else if (currentMessage.equalsIgnoreCase("!dealmein")){
                                    if (users.size()<maxUsers){
                                        users.add(currentEvent.getUser().getNick());
                                        event.getBot().sendIRC().notice(currentEvent.getUser().getNick(),"You will be dealt into the next hand");
                                    }
                                    else{
                                        event.getBot().sendIRC().notice(currentEvent.getUser().getNick(),"Current game is full, please wait till another user leaves");
                                    }
                                }// END ADD USER TO GAME
                                else if (currentMessage.equalsIgnoreCase("!dealmeout")){
                                    boolean removed = false;
                                    for (int i=0;i<users.size();i++){
                                        if (users.get(i).equalsIgnoreCase(currentEvent.getUser().getNick())){
                                            users.remove(i);
                                            removed = true;
                                            event.getBot().sendIRC().notice(currentEvent.getUser().getNick(),"You will be dealt out of the next hand");
                                            break;
                                        }
                                    }
                                    if (!removed)
                                        event.getBot().sendIRC().notice(currentEvent.getUser().getNick(),"You're not currently an active player in this game");
                                }// END REMOVE USER FROM CURRENT GAME
                            }
                            
                            // Wheatley's Move
                            if (counter>=hands.size()){
                                counter = 0;
                                if (busted < (hands.size()-1)){
                                    while (hands.get(counter).getBlackjackHandValue()<17||(hands.get(counter).isBlackjackHandSoft()&&hands.get(counter).getBlackjackHandValue()==17)){
                                        hands.get(counter).addCard(deck.dealCard());
                                        event.getBot().sendIRC().message(gameChan,Global.mainNick+" hits: "+hands.get(counter).toMaskedString());
                                    }
                                    if (hands.get(counter).getBlackjackHandValue()>21){
                                        event.getBot().sendIRC().message(gameChan, Global.mainNick+" has BUSTED! "+Global.mainNick+"'s hand was " +hands.get(counter).toColoredString() + "Total: "+hands.get(counter).getBlackjackHandValue());
                                    }
                                }
                                busted = 0;
                            }// END WHEATLEY'S MOVE
                            
                            // Wheatley is the last player to go, if he's done, then all players are done
                            if (counter == 0){
                                
                                ArrayList<Integer> winner = new ArrayList<>();
                                if (hands.get(0).getBlackjackHandValue()<=21)
                                    winner.add(0);
                                
                                int winningHand = 0; // make into an arraylist of winners
                                
                                for (int i=0;i<hands.size();i++){ // START SCORING
                                    
                                    if (hands.get(i).getBlackjackHandValue()<=21){
                                        
                                        if (hands.get(i).getBlackjackHandValue()>hands.get(winningHand).getBlackjackHandValue()|| hands.get(winningHand).getBlackjackHandValue()>21){
                                            winner.clear();
                                            winningHand=i;
                                            winner.add(i);
                                        }
                                        
                                        else if (hands.get(i).getBlackjackHandValue()==hands.get(winningHand).getBlackjackHandValue()){
//if hands are equal to 21, player with blackjack wins
// if both have blackjack, dealer wins, if neither are the dealer, winnings are split
                                            if(hands.get(i).getBlackjackHandValue()==21){
                                                
                                                if (hands.get(i).isHandBlackjack()&&!hands.get(winningHand).isHandBlackjack()){
                                                    winner.clear();
                                                    winner.add(i);
                                                    winningHand = i;
                                                }
                                                else if (hands.get(i).isHandBlackjack()&&hands.get(winningHand).isHandBlackjack()){
                                                    if(hands.get(i).getPlayer().equalsIgnoreCase(event.getBot().getNick())||hands.get(winningHand).getPlayer().equalsIgnoreCase(event.getBot().getNick())){
                                                        winner.clear();
                                                        winner.add(0);
                                                        winningHand=0;
                                                    }
                                                    else{
                                                        winner.add(i);
                                                    }
                                                    
                                                }
                                                    
                                            }
                                            if (!hands.get(i).getPlayer().equalsIgnoreCase(event.getBot().getNick())&&!hands.get(winningHand).getPlayer().equalsIgnoreCase(event.getBot().getNick())){
                                                winningHand=getHandByPlayer(hands, event.getBot().getNick());
                                            }
                                            else if (hands.get(i).getPlayer().equalsIgnoreCase(event.getBot().getNick())){
                                                winningHand=i;
                                            }
                                        }
                                    }
                                }// END SCORING
                                
                                event.getBot().sendIRC().message(gameChan,hands.get(winningHand).getPlayer()+" has won! The winning hand was "+ hands.get(winningHand).toColoredString()+"totaling "+hands.get(winningHand).getBlackjackHandValue()+" and earning $"+minBet*(hands.size()-1)+", everyone else has lost $"+minBet);
                                GameControl.scores.addScore(hands.get(winningHand).getPlayer(),minBet*(hands.size()-1));
                                
//subtract bet from all players scores, winners included
//add all losses/bets into winning pool, split by number of winners evenly

                                // SUBTRACT BET FROM ALL USERS BUT THE WINNER
                                for (int i=0;i<hands.size();i++){
                                    if(i!=winningHand){
//                                    if (!hands.get(i).getPlayer().equalsIgnoreCase(winner)){
                                        GameControl.scores.subtractScore(hands.get(i).getPlayer(),minBet);
                                    }
                                }// END UPDATE ALL USERS SCORES BUT THE WINNER
                                
                                for (int i=0;i<users.size();i++){
                                    if(GameControl.scores.getScore(users.get(i))<minBet){
                                        event.getBot().sendIRC().message(currentEvent.getChannel().getName(),users.get(i)+" does not have enough money to cover the current bet and will not be dealt in this round");
                                        users.remove(i);
                                        i--;
                                    }
                                }// End cleaning user array

                                
                                // ONLY CONTINUE IF MORE THAN THE DEALER ARE PLAYING
                                if (users.size()>1){
                                    counter=1;
                                    deck = new DeckOfCards();
                                    deck.shuffle();
                                    hands.clear();
                                    
                                    // CREATE HANDS
                                    for (int i=0;i<users.size();i++){
                                        hands.add(new CardHand(users.get(i)));
                                    }// END CREATE HANDS
                                    
                                    // DEAL CARDS
                                    for (int i=0;i<2;i++){
                                        for (int j=0;j<hands.size();j++){
                                            hands.get(j).addCard(deck.dealCard());
                                        }
                                    }// END DEAL CARDS
                                    
                                    if (hands.size()>2){ // HIDE YOUR HAND IF THERES MORE THAN JUST TWO PLAYERS (PLAYER AND BOT)
                                        event.getBot().sendIRC().notice(hands.get(counter).getPlayer(),"Your hand: "+hands.get(counter).toColoredString()+"Total: "+hands.get(counter).getBlackjackHandValue());
                                        if (prev!=counter){
                                            event.getBot().sendIRC().message(gameChan,hands.get(counter).getPlayer()+"'s turn, will you hit (h) or stay (s)?");
                                            prev=counter;
                                        }
                                    }
                                    else{
                                        event.getBot().sendIRC().message(gameChan,"Your hand: "+hands.get(counter).toColoredString()+"Total: "+hands.get(counter).getBlackjackHandValue()+" Will you hit (h) or stay (s)?");
                                    }
                                }// END NEXT HAND START
                                else if(users.isEmpty()||users.size()==1){
                                    event.getBot().sendIRC().message(gameChan,"Looks like nobody is around, Thanks for playing BlackJack! Come again soon!");
                                    running = false;
                                    queue.close();
                                }
                            } // ENDS AFTER WHEATLEY'S MOVE
                        }
                        GameControl.activeGame.remove(gameChan,"blackjack"); //updated current index of the game
                    }
                }
                else
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Game Currently running in this channel");
            }
        }
    }
    
    private int getHandByPlayer(ArrayList<CardHand> hands, String player) {
        for (int i=0;i<hands.size();i++){
            if (hands.get(i).getPlayer().equalsIgnoreCase(player))
                return (i);
        }
        throw new NullPointerException("Player not in hands");
    }
}