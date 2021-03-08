
/**
 * This class is a subclass of the Hand class.
 * It models a hand of Pair.
 * 
 * @author chanyuyan
 *
 */
@SuppressWarnings("serial")
public class Pair extends Hand{
	
	/**
	 * A constructor that builds a Pair hand.
	 * 
	 * @param player the player whose Pair hand is to be built
	 * @param cards the list of cards from which a Pair hand is to be built
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	
	
	/**
	 * Returns the top card of this Pair hand.
	 * 
	 * @return the top card of this Pair hand
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard() {
		this.sort();
		return this.getCard(1); 
	}
	
	/**
	 * Determines if this is a valid Pair hand.
	 * 
	 * @return whether this is a valid Pair hand
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		this.sort();
		if (this.size() != 2) {
			return false;
		}
		if (this.getCard(0).getRank() == this.getCard(1).getRank()) {
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
		return "Pair";
	}

}
