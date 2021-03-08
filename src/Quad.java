/**
 * This class is a subclass of the Hand class.
 * It models a hand of Quad.
 * 
 * @author chanyuyan
 *
 */
@SuppressWarnings("serial")
public class Quad extends Hand{
	/**
	 * A constructor that builds a Quad hand.
	 * 
	 * @param player the player whose Quad hand is to be built
	 * @param cards the list of cards from which a Quad hand is to be built
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	
	
	/**
	 * Returns the top card of this Quad hand.
	 * 
	 * @return the top card of this Quad hand
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard() {
		this.sort();
		if (this.getCard(0).getRank() == this.getCard(3).getRank()) {
			return this.getCard(3); 
		} else if (this.getCard(1).getRank() == this.getCard(4).getRank()) {
			return this.getCard(4);
		}
		return null;
	}
	
	/**
	 * Overrides the beat method in Hand.
	 * Determines if this quad hand beats the specified hand.
	 * 
	 * @param hand the specified hand
	 * @return whether this Quad hand beats the specified hand
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
			// The only other hand of 5 that can beat Quad is StraightFlush
			if (hand.getType() == "StraightFlush") {
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * Determines if this is a valid Quad hand.
	 * 
	 * @return whether this is a valid Quad hand
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		this.sort();
		if (this.size() != 5) {
			return false;
		}
		if (this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank() && this.getCard(2).getRank() == this.getCard(3).getRank()) {
			return true;
		} else if (this.getCard(1).getRank() == this.getCard(2).getRank() && this.getCard(2).getRank() == this.getCard(3).getRank() && this.getCard(3).getRank() == this.getCard(4).getRank()) {
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
	 */
	public String getType() {
		return "Quad";
	}
}
