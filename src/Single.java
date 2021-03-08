
/**
 * This class is a subclass of the Hand class.
 * It models a hand of Single.
 * 
 * @author chanyuyan
 *
 */
@SuppressWarnings("serial")
public class Single extends Hand {
	
	/**
	 * A constructor that builds a Single hand.
	 * 
	 * @param player the player whose Single hand is to be built
	 * @param cards the list of cards from which a Single hand is to be built
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * Returns the top card of this Single hand.
	 * 
	 * @return the top card of this Single hand
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard() {
		this.sort();
		return this.getCard(this.size()-1); 
	}
	
	/**
	 * Overrides the abstract isValid method in Hand.
	 * Determines if this is a valid Single hand.
	 * 
	 * @return whether this is a valid Single hand
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		if (this.size() == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Overrides the getType method in Hand.
	 * Gives the name of this hand.
	 * 
	 * @return the type of this hand
	 * @see Hand#getType()
	 */
	public String getType() {
		return "Single";
	}
	
}
