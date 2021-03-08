/**
 * This class is a subclass of the Hand class.
 * It models a hand of FullHouse.
 * 
 * @author chanyuyan
 *
 */
@SuppressWarnings("serial")
public class FullHouse extends Hand{
	/**
	 * A constructor that builds a FullHouse hand.
	 * 
	 * @param player the player whose FullHouse hand is to be built
	 * @param cards the list of cards from which a FullHouse hand is to be built
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	
	
	/**
	 * Returns the top card of this FullHouse hand.
	 * 
	 * @return the top card of this FullHouse hand
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard() {
		this.sort();
		if (this.getCard(0).getRank() == this.getCard(2).getRank()) {
			return this.getCard(2);
		} else if (this.getCard(2).getRank() == this.getCard(4).getRank()) {
			return this.getCard(4);
		}
		
		return null;
	}
	
	/**
	 * Overrides the beat method in Hand.
	 * Determines if this FullHouse hand beats the specified hand.
	 * 
	 * @param hand the specified hand
	 * @return whether this FullHouse hand beats the specified hand
	 * @see Hand#beats(Hand)
	 */
	public boolean beats(Hand hand) {
		this.sort();
		hand.sort();
		if (this.size() != hand.size()) {
			return false;
		} 
		if (this.getType() == hand.getType()) {
			if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
				return true;
			} else {
				return false;
			}
		} else {
			String handType = hand.getType();
			// Only other possible hands of 5 that can beat full house are Quad and StraightFlush
			if (handType == "Quad" || handType == "StraightFlush") {
				return false;
			} else {
				return true;
			}
		}
	}
	
	
	/**
	 * Determines if this is a valid FullHouse hand.
	 * 
	 * @return whether this is a valid FullHouse hand
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		this.sort();
		if (this.size() != 5) {
			return false;
		}
		if (this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank()) {
			if (this.getCard(3).getRank() == this.getCard(4).getRank() && this.getCard(3).getRank() != this.getCard(0).getRank()) {
				return true;
			}
		} else if (this.getCard(2).getRank() == this.getCard(3).getRank() && this.getCard(3).getRank() == this.getCard(4).getRank()) {
			if (this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(0).getRank() != this.getCard(3).getRank()) {
				return true;
			}
		}
		
		
		return false;
	}
	
	/**
	 * Overrides the getType method in Hand.
	 * Gives the name of this hand.
	 * 
	 * @return the type of this hand
	 * @see Hand#getType()
	 * 
	 */
	public String getType() {
		return "FullHouse";
	}
}
