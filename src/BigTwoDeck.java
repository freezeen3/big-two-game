
/**
 * This is a subclass of the Deck class.
 * It models a deck of cards used in the Big Two card game.
 * 
 * @author chanyuyan
 *
 */
@SuppressWarnings("serial")
public class BigTwoDeck extends Deck{
	
	/**
	 * Overrides the initialize() method in the Deck class.
	 * Removes all cards from the deck, creates 52 Big Two cards and adds them to the deck.
	 * 
	 */
	public void initialize() {
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard card = new BigTwoCard(i, j);
				addCard(card);
			}
		}
	}
}
