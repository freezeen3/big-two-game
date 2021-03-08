
/**
 * This class is a subclass of the Card class.
 * It models a card used in a Big Two card game.
 * The compareTo() method from the Card class is overridden to reflect the ordering of cards.
 * 
 * @author chanyuyan
 *
 */
@SuppressWarnings("serial")
public class BigTwoCard extends Card {
	
	/**
	 * A constructor that builds a card with the specified suit and rank.
	 * @param suit an integer between 0 and 3
	 * @param rank an integer between 0 and 12
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
	}
	
	
	/**
	 * Compares the order of this card with the specified card with Big Two rules. 2 and A are ranked above K.
	 * Big Two low to high: '3', '4', '5', '6', '7', '8', '9', '0', 'J', 'Q', 'K', 'A', '2'
	 * Card RANK array order: 'A', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'J', 'Q', 'K'
	 * Determines whether this card ranks less than, equal to, or greater than the specified card
	 * 
	 * @param card the specified card to be compared with this card
	 * @return negative integer, zero, or positive integer if this card is less than, equal to, or greater than the specified card
	 */
	public int compareTo(Card card) {
		// Accounts for '2' as biggest
		if (this.rank == 1 && card.rank != 1) {
			return 1;
		} else if (this.rank != 1 && card.rank == 1) {
			return -1;
		}
		// Accounts for 'A' as second biggest
		if (this.rank == 0 && card.rank >= 2) {
			return 1;
		} else if (this.rank >= 2 && card.rank == 0) {
			return -1;
		}
		
		if (this.rank > card.rank) {
			return 1;
		} else if (this.rank < card.rank) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	}
	
	
	
}
