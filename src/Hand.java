
/**
 * This class is a subclass of the CardList class.
 * It models a hand of cards.
 * 
 * @author chanyuyan
 *
 */
@SuppressWarnings("serial")
public abstract class Hand extends CardList{
	
	
	/**
	 * A constructor that builds a hand with the specified player and list of cards.
	 * 
	 * @param player the player whose hand is to be built
	 * @param cards the list of cards from which a hand is to be built
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for (int i = 0; i < cards.size(); ++i) {
			this.addCard(cards.getCard(i));
		}		
		
	}
	
	// The player who plays this hand
	private CardGamePlayer player;
	
	
	/**
	 * Retrieves the player of this hand.
	 * 
	 * @return the player of this hand
	 */
	public CardGamePlayer getPlayer() {
		return this.player;
	}
	
	/**
	 * Retrieves the top card of this hand.
	 * 
	 * @return the top card of this hand
	 */
	public Card getTopCard() {
		this.sort();
		return this.getCard(this.size()-1); 
	}
	
	/**
	 * Checks if this hand beats a specified hand.
	 * To be overridden in the Hand subclasses for hands of length 5.
	 * 
	 * @param hand the specified hand
	 * @return whether this hand beats the specified hand
	 */
	public boolean beats(Hand hand) {
		if (this.size() != hand.size()) {
			return false;
		}
		int compareResult = this.getTopCard().compareTo(hand.getTopCard());
		if (this.size() == 1) {
			if (compareResult == 1) {
				return true;
			} else {
				return false;
			}
		} else if (this.size() == 2) {
			if (compareResult == 1) {
				return true;
			} else {
				return false;
			}
		} else if (this.size() == 3) {
			if (compareResult == 1) {
				return true;
			} else {
				return false;
			}
		} 
		else if (this.size() == 4) {
			return false;
		} else if (this.size() == 5) {
			if (compareResult == 1) {
				return true;
			} else {
				return false;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks if this is a valid hand.
	 * To be overridden
	 * 
	 * @return whether this is a valid hand
	 */
	public abstract boolean isValid();
	
	/**
	 * Returns a string specifying the type of this hand.
	 * To be overridden
	 * 
	 * @return the type of this hand
	 */
	public abstract String getType();
}
