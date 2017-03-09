/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import java.util.ArrayList;

/**
 *
 * @author Stephen
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    PlayingCard
 * - Linked Classes
 *    N/A
 * 
 * An object of type Hand represents a hand of cards.  The
 * cards belong to the class Card.  A hand is empty when it
 * is created, and any number of cards can be added to it.
 *
 *
 *
 *
 * Utilized multiple sources
 * http://math.hws.edu/javanotes/c5/s4.html
 */

public class CardHand {
    private ArrayList<PlayingCard> hand;
    private String player = new String();
    
    public CardHand(){
        hand = new ArrayList<>();
        
    }
    public CardHand(String player){
        hand = new ArrayList<>();
        this.player = player;
    }
    
    public String toColoredString(){
        String handString = "";
        for (int i=0;i<hand.size();i++){
            handString += hand.get(i).toColoredString() + " | ";
        }
        return handString;
    }
    
    @Override
    public String toString(){
        String handString = "";
        for (int i=0;i<hand.size();i++){
            handString += hand.get(i).toString() + " ";
        }
        return handString;
    }
    
    public String toMaskedString(){
        String handString = "";
        for (int i=0;i<hand.size();i++){
            handString += hand.get(i).toMaskedString() + " | ";
        }
        return handString;
    }
    
    public String getPlayer(){
        return this.player;
    }
    
    public int getHandValue(){
        int value = 0;
        for (int i=0;i<hand.size();i++){
            value+=hand.get(i).getValue();
        }
        return value;
    }
    
    public boolean isHandBlackjack(){
        if(getBlackjackHandValue()!=21)
            return(false);
        
        boolean containsTen = false;
        boolean containsAce = false;
        for(int i=0;i<hand.size();i++){
            if (hand.get(i).getValue()>=10)
                containsTen=true;
            else if(hand.get(i).getValue()==1)
                containsAce=true;
        }
        return(containsTen&&containsAce&&hand.size()==2);
    }
    
    
    public boolean isBlackjackHandSoft(){
        int value=0;
        for (int i=0;i<hand.size();i++){
            
            if (hand.get(i).getValue()>10){
                value+=10;
            }
            else
                value+=hand.get(i).getValue();
        }
        return (value!=getBlackjackHandValue());
    }
    
    public int getBlackjackHandValue(){
        
        int value = 0;
        int aces = 0;
        for (int i=0;i<hand.size();i++){
            
            if (hand.get(i).getValue()>10){
                value+=10;
            }
            else if (hand.get(i).getValue()==1){
                value+=11;
                aces++;
            }
            else{
                value+=hand.get(i).getValue();
            }
            
        }
        
        if (value>21){
            int i=0;
            while (i<aces&&value>21){
                value-=11;
                value+=1;
                i++;
            }
        }
        return value;
    }
    
    /**
     * Remove all cards from the hand, leaving it empty.
     */
    public void clear() {
        hand.clear();
    }
    
    /**
     * Add a card to the hand.  It is added at the end of the current hand.
     * @param c the non-null card to be added.
     * @throws NullPointerException if the parameter c is null.
     */
    public void addCard(PlayingCard c) {
        if (c == null)
            throw new NullPointerException("Can't add a null card to a hand.");
        hand.add(c);
    }
    
    /**
     * Remove a card from the hand, if present.
     * @param c the card to be removed.  If c is null or if the card is not in
     * the hand, then nothing is done.
     */
    public void removeCard(PlayingCard c) {
        hand.remove(c);
    }
    
    /**
     * Remove the card in a specified position from the hand.
     * @param position the position of the card that is to be removed, where
     * positions are starting from zero.
     * @throws IllegalArgumentException if the position does not exist in
     * the hand, that is if the position is less than 0 or greater than
     * or equal to the number of cards in the hand.
     */
    public void removeCard(int position) {
        if (position < 0 || position >= hand.size())
            throw new IllegalArgumentException("Position does not exist in hand: "
                    + position);
        hand.remove(position);
    }
    
    /**
     * Returns the number of cards in the hand.
     * @return the number of cards in the hand.
     */
    public int getCardCount() {
        return hand.size();
    }
    
    /**
     * Gets the card in a specified position in the hand.  (Note that this card
     * is not removed from the hand!)
     * @param position the position of the card that is to be returned
     * @throws IllegalArgumentException if position does not exist in the hand
     */
    public PlayingCard getCard(int position) {
        if (position < 0 || position >= hand.size())
            throw new IllegalArgumentException("Position does not exist in hand: "
                    + position);
        return hand.get(position);
    }
    
    /**
     * Sorts the cards in the hand so that cards of the same suit are
     * grouped together, and within a suit the cards are sorted by value.
     * Note that aces are considered to have the lowest value, 1.
     */
    public void sortBySuit() {
        ArrayList<PlayingCard> newHand = new ArrayList<>();
        while (hand.size() > 0) {
            int pos = 0;  // Position of minimal card.
            PlayingCard c = hand.get(0);  // Minimal card.
            for (int i = 1; i < hand.size(); i++) {
                PlayingCard c1 = hand.get(i);
                if ( c1.getSuit() < c.getSuit() ||
                        (c1.getSuit() == c.getSuit() && c1.getValue() < c.getValue()) ) {
                    pos = i;
                    c = c1;
                }
            }
            hand.remove(pos);
            newHand.add(c);
        }
        hand = newHand;
    }
    
    /**
     * Sorts the cards in the hand so that cards of the same value are
     * grouped together.  Cards with the same value are sorted by suit.
     * Note that aces are considered to have the lowest value, 1.
     */
    public void sortByValue() {
        ArrayList<PlayingCard> newHand = new ArrayList<>();
        while (hand.size() > 0) {
            int pos = 0;  // Position of minimal card.
            PlayingCard c = hand.get(0);  // Minimal card.
            for (int i = 1; i < hand.size(); i++) {
                PlayingCard c1 = hand.get(i);
                if ( c1.getValue() < c.getValue() ||
                        (c1.getValue() == c.getValue() && c1.getSuit() < c.getSuit()) ) {
                    pos = i;
                    c = c1;
                }
            }
            hand.remove(pos);
            newHand.add(c);
        }
        hand = newHand;
    }
}
