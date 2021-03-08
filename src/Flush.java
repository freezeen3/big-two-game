/**
 * This class is a subclass of the Hand class.
 * It models a hand of Flush.
 * 
 * @author chanyuyan
 *
 */

@SuppressWarnings("serial")
public class Flush extends Hand{
	/**
	 * A constructor that builds a Flush hand.
	 * 
	 * @param player the player whose Flush hand is to be built
	 * @param cards the list of cards from which a Flush hand is to be built
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	
	
	/**
	 * Returns the top card of this Flush hand.
	 * 
	 * @return the top card of this Flush hand
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard() {
		this.sort();
		return this.getCard(this.size()-1);
	}
	
	/**
	 * Overrides the beat method in Hand.
	 * Determines if this Flush hand beats the specified hand.
	 * 
	 * @param hand the specified hand
	 * @see Hand#beats(Hand)
	 */
	public boolean beats(Hand hand) {
		this.sort();
		hand.sort();
		if (this.size() != hand.size()) {
			return false;
		} 
		if (this.getType() == hand.getType()) {
			// A flush with a higher suit beats one with a lower suit
			if (this.getTopCard().getSuit() > hand.getTopCard().getSuit()) {
				return true;
			} else if (this.getTopCard().getSuit() < hand.getTopCard().getSuit()) {
				return false;
			} else {
				if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			String handType = hand.getType();
			// Only other possible hand of 5 that Flush can beat is Straight
			if (handType == "Straight") {
				return true;
			} else {
				return false;
			}
		}
	}
	
	
	/**
	 * Determines if this is a valid Flush hand.
	 * 
	 * @return whether this is a valid Flush hand
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		this.sort();
		if (this.size() != 5) {
			return false;
		}
		if (this.getCard(0).getSuit() == this.getCard(1).getSuit() && this.getCard(1).getSuit() == this.getCard(2).getSuit() && this.getCard(2).getSuit() == this.getCard(3).getSuit() && this.getCard(3).getSuit() == this.getCard(4).getSuit()) {
			return true;
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
		return "Flush";
	}
}
