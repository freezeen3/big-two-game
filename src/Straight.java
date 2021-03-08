/**
 * This class is a subclass of the Hand class.
 * It models a hand of straight.
 * 
 * @author chanyuyan
 *
 */
@SuppressWarnings("serial")
public class Straight extends Hand{
	/**
	 * A constructor that builds a straight hand.
	 * 
	 * @param player the player whose straight hand is to be built
	 * @param cards the list of cards from which a straight hand is to be built
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	
	
	/**
	 * Returns the top card of this Straight hand.
	 * 
	 * @return the top card of this Straight hand
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard() {
		this.sort();
		return this.getCard(this.size()-1);
	}
	
	
	/**
	 * Overrides the beats method in Hand.
	 * Determines whether this Straight hand beats the specified hand.
	 * 
	 * @param hand the specified hand
	 * @return whether this Straight hand beats the specified hand
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
			}
		}
		// All other hands of 5 beat straight
		return false;
	}
	
	/**
	 * Determines if this is a valid Straight hand.
	 * 
	 * @return whether this is a valid Straight hand
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		this.sort();
		if (this.size() == 5) {
			// Adjustment of ranks of '2' and 'A' to Big Two rules
			int [] tempArr = new int[5];
			for (int i = 0; i < 5; i++) {
				if (this.getCard(i).getRank() == 0) {
					tempArr[i] = 13;
				} else if (this.getCard(i).getRank() == 1) {
					tempArr[i] = 14;
				} else {
					tempArr[i] = this.getCard(i).getRank();
				}
			}
			
			// Checks if the ranks are consecutive
			for (int i = 0; i < 4; i++) {
				if (tempArr[i] + 1 != tempArr[i+1]) {
					return false;
				}
			}
			
			// Checks if the suits are the same, if same then it is straightflush and we reject it here
			if (this.getCard(0).getSuit() == this.getCard(1).getSuit() && this.getCard(1).getSuit() == this.getCard(2).getSuit() && this.getCard(2).getSuit() == this.getCard(3).getSuit() && this.getCard(3).getSuit() == this.getCard(4).getSuit()) {
				return false;
			}
			
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
		return "Straight";
	}
}
