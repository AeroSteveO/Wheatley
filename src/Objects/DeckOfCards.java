 /**
  * DeckOfCards.java -
  *   Object that represents a deck of playing cards.
  *   This object contains an array of PlayingCard
  *   objects.
  *
  * @author Grant William Braught
  * @author Dickinson College
  * @version 3/29/2000
  * 
  * @author Stephen
  * @version 11/4/2014
  * 
  * Some functions are derivatives from:
  * http://math.hws.edu/javanotes/c5/s4.html
  * 
  */

package Objects;

import java.util.ArrayList;
import java.util.Collections;
/**
 * 
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    PlayingCard
 * - Linked Classes
 *    N/A
 *    
 * 
 */
public class DeckOfCards {
    
    // A name for an array of references to Playing Card objects.
    // Each reference in this array will hold a reference
    // to one of the cards in a deck of cards.
    private ArrayList<PlayingCard> theDeck;
    /**
     * Keeps track of the number of cards that have been dealt from
     * the deck so far.
     */
    private int cardsUsed;
    
    /**
     * Creates a standard deck of 52 playing cards.
     */
    public DeckOfCards() {
        // Create the array of references to the playing
        // card object.  The array must be created in the
        // constructor because theDeck is an instance
        // variable.  Therefore, every deck must have its
        // own array of references.
        theDeck = new ArrayList<>();
        
        // Create the cards:
        for (int i=0; i < 13; i++) {
            // Create the Hearts...
            theDeck.add(new PlayingCard(i+1, PlayingCard.HEARTS));
            // Create the Spades...
            theDeck.add(new PlayingCard(i+1, PlayingCard.SPADES));
            // Create the Diamonds...
            theDeck.add(new PlayingCard(i+1, PlayingCard.DIAMONDS));
            // Create the Clubs...
            theDeck.add(new PlayingCard(i+1, PlayingCard.CLUBS));
        }
        cardsUsed = 0;
    }
    
    /**
     * Shuffle the deck of cards.
     */
    public void shuffle(){
        Collections.shuffle(theDeck);
    }
    /**
     * Removes the next card from the deck and return it.  It is illegal
     * to call this method if there are no more cards in the deck.  You can
     * check the number of cards remaining by calling the cardsLeft() function.
     * @return the card which is removed from the deck.
     * @throws IllegalStateException if there are no cards left in the deck
     */
    public PlayingCard dealCard() {
        if (cardsUsed == theDeck.size())
            throw new IllegalStateException("No cards are left in the deck.");
        cardsUsed++;
        return theDeck.get(cardsUsed - 1);
        // Programming note:  Cards are not literally removed from the array
        // that represents the deck.  We just keep track of how many cards
        // have been used.
    }
    /**
     * As cards are dealt from the deck, the number of cards left
     * decreases.  This function returns the number of cards that
     * are still left in the deck.  The return value would be
     * 52 or 54 (depending on whether the deck includes Jokers)
     * when the deck is first created or after the deck has been
     * shuffled.  It decreases by 1 each time the dealCard() method
     * is called.
     */
    public int cardsLeft() {
        return theDeck.size() - cardsUsed;
    }
    /**
     * Test whether the deck contains Jokers.
     * @return true, if this is a 54-card deck containing two jokers, or false if
     * this is a 52 card deck that contains no jokers.
     */
    public boolean hasJokers() {
        return (theDeck.size() == 54);
    }
    /**
     * Return a string representation of the deck of
     * cards.
     *
     * @return a string representation of the deck of
     *         cards.
     */
    public String toString() {
        
        String deckStr = "";
        
        for (int i=0; i<52; i++) {
            deckStr = deckStr + theDeck.get(i).toString() + " ";
        }
        
        return deckStr;
    }
}
