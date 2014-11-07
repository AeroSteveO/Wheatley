/**
 * Models a playing card, with possible values of Ace through King
 * in one of the four suits.  May also be a Joker.
 * <p>
 * Cards are not modifiable once created.
 * <p>
 * When suits need to be sorted, bridge suit value is used, producing the
 * following order: (highest) SPADES, HEARTS, DIAMONDS, CLUBS (lowest).
 *
 * @author Zach Tomaszewski
 * @version 15 Feb 2005
 */

package Objects;

import org.pircbotx.Colors;

public class PlayingCard implements Comparable {
    
    /*
    * Notes/Todo:
    * --There are normally two jokers, distinguishable from each other.
    *   Right now, a joker is a joker.
    * --Java supports Unicode, and unicode has suit characters:
    *   \U+2660 to \U+2667, or #9824 to #9831.
    *   If I can get these to print anywhere, add a static variable
    *   controlling toString's output from plain ascii text.
    *   [char literal: '\\uCODE', w/o first \]
    * --New decks are created A-K.  Perhaps should be 2-A,
    *   so AS is bottom face card?
    *
    * Other possible features:
    * --a getColor method
    * --a face up/face down state
    * --a draw method for use with GUIs
    * --a comparator that sorts suit and then value (for sorting hands)
    */
    
//face card values
    
    
    public static final int ACE = 1;
    public static final int JACK = 11;
    public static final int QUEEN = 12;
    public static final int KING = 13;
    public static final int JOKER = 0;
//suits, in bridge rank order, highest to lowest
    public static final int SPADES = 4;
    public static final int HEARTS = 3;
    public static final int DIAMONDS = 2;
    public static final int CLUBS = 1;
    /** a null suit for Jokers or other non-suit cards */
    public static final int NONE = 0;
    
    
//instance variables
    private int value; //sometimes call pips
    private int suit;
    
    
    /**
     * Creates a new playing card.
     * Must be given a valid value and suit, or will create a JOKER/NONE card.
     *
     * @param value The value of the new card, from ACE (1) to KING (13),
     *              or else JOKER.
     * @param suit  A valid suit, such as HEARTS, SPADES, CLUBS, or DIAMONDS;
     *              a JOKER has a suit of NONE.
     */
    public PlayingCard(int value, int suit) {
        if (value >= 1 && value <= 13 && suit >= 1 && suit <= 4) {
            //a valid playing card
            this.value = value;
            this.suit = suit;
        }else {
            //joker, intentionally or unintentionally
            this.value = JOKER;
            this.suit = NONE;
        }
    }
       /**
    * Returns the suit of this card.
    * @returns the suit, which is one of the constants Card.SPADES, 
    * Card.HEARTS, Card.DIAMONDS, Card.CLUBS, or Card.JOKER
    */
   public int getSuit() {
      return suit;
   }
   
   /**
    * Returns the value of this card.
    * @return the value, which is one of the numbers 1 through 13, inclusive for
    * a regular card, and which can be any value for a Joker.
    */
   public int getValue() {
      return value;
   }
   
   /**
    * Returns a String representation of the card's suit.
    * @return one of the strings "Spades", "Hearts", "Diamonds", "Clubs"
    * or "Joker".
    */
   public String getSuitAsString() {
      switch ( suit ) {
      case SPADES:   return "Spades";
      case HEARTS:   return "Hearts";
      case DIAMONDS: return "Diamonds";
      case CLUBS:    return "Clubs";
      default:       return "Joker";
      }
   }

    
//METHODS (instance) --------
    
    /**
     * Compares two PlayingCards for sorting purposes.
     * Sorts based on value (pips), with ace low and king high.
     * Jokers have a value lower than any other card.
     * In order to keep (x.compareTo(y) == 0) == (x.equals(y)),
     * two cards of the same value do not return 0.  Instead,
     * cards with the same value (pips) are sorted in order by suit.
     *
     * @return -1, 0, or 1 if this card is less than, equal to, or greater
     *          than the given card.
     * @throws ClassCastException   if rhs is not a PlayingCard
     */
    @Override
    public int compareTo(Object rhs) {
        PlayingCard card = (PlayingCard) rhs;
        if (this.value < card.value) {
            return -1;
        }else if (this.value > card.value) {
            return 1;
        }else { //== values
            if (this.suit < card.suit) {
                return -1;
            }else if (this.suit > card.suit) {
                return 1;
            }else {
                return 0;
            }
        }
    }
    
    /**
     * Compares two cards as per {@link #compareTo(Object) compareTo},
     * except that suit is ignored.
     * This means that the ordering produced by this method is inconsistent
     * with equals.  That is, (x.compareToIgnoreSuit(y) == 0) != (x.equals(y))
     */
    public int compareToIgnoreSuit(PlayingCard card) {
        /*
        * ICS211 Note: since this method only exists for PlayingCard objects,
        * I will require a PlayingCard parameter.
        */
        if (this.value < card.value) {
            return -1;
        }else if (this.value > card.value) {
            return 1;
        }else { //== values
            return 0;
        }
    }
    
    
    /**
     * Returns true if both cards have the same value and suit.
     */
    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(this.getClass())) {
            //o is null or not of the same class
            return false;
        }else {
            PlayingCard rhs = (PlayingCard) o;
            //return true only if the two cards have both the same value and same suit
            return (this.value == rhs.value && this.suit == rhs.suit);
        }
    }
    
    /**
     * Returns true if this card and the given card have the same value.
     * Their suits may be different.
     */
    public boolean equalsIgnoreSuit(PlayingCard card) {
        return (this.value == card.value);
    }
    
    public String toMaskedString(){
        return "**";
    }
    /**
     * Return a string representation of this PlayingCard.
     * Each card is in the format of [value][suit].
     * Values are A, 2, 3 ... 10, J, Q, K.
     * Suits are abbreviated to d, h, c, or s.
     * Joker is "jok".
     * Examples: "4h", "10c", "Ks".
     */
    public String toString() {
        String card = "";
        switch (this.value) {
            case JOKER:
                card += "Jok";
                break;
            case ACE:
                card += "A";
                break;
            case JACK:
                card += "J";
                break;
            case QUEEN:
                card += "Q";
                break;
            case KING:
                card += "K";
                break;
            default:
                card += this.value;  //all digits
                break;
        }
        switch (this.suit) {
            case HEARTS:
                card += " <3";
                break;
            case CLUBS:
                card += " -%";
                break;
            case SPADES:
                card += " <3=";
                break;
            case DIAMONDS:
                card += " <>";
                break;
            default:
                //add nothing (such as to "Jok")
                break;
        }
        return card;
    }//<Asamarui> ♤, ♥, ♢, ♧
    //<Asamarui> i.e these ♠♥♦♣
        public String toColoredString() {
        String card = "";
        switch (this.value) {
            case JOKER:
                card += Colors.BOLD+"Jok";
                break;
            case ACE:
                card += Colors.BOLD+"A";
                break;
            case JACK:
                card += Colors.BOLD+"J";
                break;
            case QUEEN:
                card += Colors.BOLD+"Q";
                break;
            case KING:
                card += Colors.BOLD+"K";
                break;
            default:
                card += Colors.BOLD+this.value;  //all digits
                break;
        }
        switch (this.suit) {
            case HEARTS:
                card += " "+Colors.RED+"<3"+Colors.NORMAL;
                break;
            case CLUBS:
                card += " -%"+Colors.NORMAL;
                break;
            case SPADES:
                card += " <3="+Colors.NORMAL;
                break;
            case DIAMONDS:
                card += " "+Colors.RED+"<>"+Colors.NORMAL;
                break;
            default:
                //add nothing (such as to "Jok")
                break;
        }
        return card;
    }
    
    
// INNER CLASSES ------------
    
    /*
    * ICS211 Note: The comparators below are an example of inner classes.
    * You don't see them very often,
    * and I recommend you don't use them unless you have a good reason to.
    * Normally it's for parts or components of a class so intimately
    * connected to that class you want to make it part of that class.
    * (I originally wrote Deck as an inner class, but decided it could be
    * used for non-PlayingCard purposes.  Even if that's not true, a deck
    * seems to be a collection of PlayingCards, rather than some inherent
    * feature or function of a PlayingCard.  So I made Deck a separate class.
    * However, a comparator that sorts Playing Cards seems so closely tied
    * to Playing Cards, I thought to make it an inner class.
    *
    * The equals methods for all my Comparators (which determines whether
    * two comparators are the same) was going to be the same code for all four
    * Comparators.  Rather than write this four times, I made an abstract
    * super class so I could write this method and leave compare (the other
    * method from the Compartor interface) to be implemented later.
    * (Another option would have been to just extend my first Collator,
    * overriding the compare method in each case.)
    */
    
    
    /**
     * The superclass of all PlayingCard.Comparators.
     * It provides the equals functionality specified by
     * java.util.Comparator, but leaves the compare method
     * details to its children classes.
     */
    protected static abstract class AbstractComparator
    implements java.util.Comparator {
        /**
         * Returns whether these two comparators are equal.
         */
        @Override
        public boolean equals(Object o) {
            return (this.getClass().equals(o.getClass()));
        }
    }
    
    /**
     * A Comparator for sorting cards into their natural ordering,
     * which is by value and then by suit.
     *
     * @see PlayingCard#compareTo(Object)
     */
    public static class Comparator extends AbstractComparator {
        
        /**
         * Compares o1 to o2, returning -1 if o1 is less than o2,
         * 1 if o1 is greater than o2, or 0 if the two are equivalent.
         * This comparison is consistent with PlayingCard's equals.
         */
        @Override
        public int compare(Object o1, Object o2) {
            PlayingCard lhs = (PlayingCard) o1;
            PlayingCard rhs = (PlayingCard) o2;
            return lhs.compareTo(rhs);
        }
    }
    
    /**
     * A Comparator for sorting cards into a numerical/value ordering,
     * ignoring their suit.
     *
     * @see PlayingCard#compareToIgnoreSuit(PlayingCard)
     */
    public static class ComparatorIgnoreSuit extends AbstractComparator {
        
        /**
         * Compares o1 to o2, returning -1 if o1 is less than o2,
         * 1 if o1 is greater than o2, or 0 if the two are equivalent.
         * This comparison is inconsistent with PlayingCard's equals,
         * but is consistent with equalsIgnoreSuit.
         */
        @Override
        public int compare(Object o1, Object o2) {
            PlayingCard lhs = (PlayingCard) o1;
            PlayingCard rhs = (PlayingCard) o2;
            return lhs.compareToIgnoreSuit(rhs);
        }
    }
    
    /**
     * A Comparator for sorting cards into their natural ordering,
     * (by value and then by suit), except that aces are considered
     * a higher value than kings.
     */
    public static class ComparatorAceHigh extends AbstractComparator {
        
        /**
         * Compares o1 to o2, returning -1 if o1 is less than o2,
         * 1 if o1 is greater than o2, or 0 if the two are equivalent.
         * This comparison is inconsistent with PlayingCard's equals.
         */
        @Override
        public int compare(Object o1, Object o2) {
            PlayingCard lhs = (PlayingCard) o1;
            PlayingCard rhs = (PlayingCard) o2;
            if (lhs.value == ACE || rhs.value == ACE) {
                //have an ACE to deal with
                if (lhs.value == ACE && rhs.value == ACE) {
                    //two aces, so sort by suit (which compareTo will do for us)
                    return lhs.compareTo(rhs);
                }else if (lhs.value == ACE) { //only lhs is an ace, so lhs is highest
                    return 1;
                }else { //rhs (only) must be an ace, so it is the higher value
                    return -1;
                }
            }else { //no aces at all, so normal comparison
                return lhs.compareTo(rhs);
            }
        }
    }
    
    /**
     * A Comparator for sorting cards into a numerical/value ordering,
     * ignoring their suit.
     *
     * @see PlayingCard#compareToIgnoreSuit(PlayingCard)
     */
    public static class ComparatorAceHighIgnoreSuit extends AbstractComparator {
        
        /**
         * Compares o1 to o2, returning -1 if o1 is less than o2,
         * 1 if o1 is greater than o2, or 0 if the two are equivalent.
         * This comparison is inconsistent with PlayingCard's equals,
         * but is consistent with equalsIgnoreSuit.
         */
        @Override
        public int compare(Object o1, Object o2) {
            PlayingCard lhs = (PlayingCard) o1;
            PlayingCard rhs = (PlayingCard) o2;
            if (lhs.value == ACE && rhs.value != ACE) {
                return 1; //lhs has highest possible value
            }else if (rhs.value == ACE && lhs.value != ACE) {
                return -1;  //rhs has highest value
            }else { //no aces at all, so normal comparison
                return lhs.compareToIgnoreSuit(rhs);
            }
        }
    }
    
    
    
//MAIN (for PlayingCard) --------------
    
    /**
     * Runs a few sample tests on printing cards and creating Comparators,
     * printing results to the screen.
     */
//    public static void main(String[] args) {
//        
//        System.out.print("A few sample PlayingCards: ");
//        System.out.print(new PlayingCard(4, HEARTS) + " ");
//        System.out.print(new PlayingCard(2, CLUBS) + " ");
//        System.out.print(new PlayingCard(10, SPADES) + " ");
//        System.out.print(new PlayingCard(JACK, DIAMONDS) + " ");
//        System.out.print(new PlayingCard(JOKER, NONE) + "\n");
//        System.out.println();
//        
//        PlayingCard card1 = new PlayingCard(KING, CLUBS);
//        PlayingCard card2 = new PlayingCard(KING, SPADES);
//        System.out.print(card1 + " equals " + card2 + " [false]: ");
//        System.out.println(card1.equals(card2));
//        System.out.print(card1 + " equalsIgnoreSuit " + card2 + " [true]: ");
//        System.out.println(card1.equalsIgnoreSuit(card2));
//        System.out.println();
//        
//        java.util.Comparator comp = new PlayingCard.Comparator();
//        System.out.print("A Comparator equals a Comparator [true]: ");
//        System.out.println( comp.equals(new PlayingCard.Comparator()) );
//        System.out.print("A Comparator equals a ComparatorIgnoreSuit [false]: ");
//        java.util.Comparator comp2 = new PlayingCard.ComparatorIgnoreSuit();
//        System.out.println( comp.equals(comp2) );
//        
//    }
    
    
}//end class PlayingCard
