
/**
 * This class is a subclass of the Hand class.
 * It models a hand of Triple.
 * 
 * @author chanyuyan
 *
 */
@SuppressWarnings("serial")
public class Triple extends Hand{
	/**
	 * A constructor that builds a Triple hand.
	 * 
	 * @param player the player whose Triple hand is to be built
	 * @param cards the list of cards from which a Triple hand is to be built
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * Returns the top card of this Triple hand.
	 * 
	 * @return the top card of this Triple hand
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard() {
		this.sort();
		return this.getCard(2); 
	}
	
	/**
	 * Determines if this is a valid Triple hand.
	 * 
	 * @return whether this is a valid Triple hand
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		this.sort();
		if (this.size() != 3) {
			return false;
		}
		if (this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank()) {
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
		return "Triple";
	}

}
