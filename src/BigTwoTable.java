import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


/**
 * A class that implements the CardGameTable interface.
 * It is used to build a GUI for the Big Two card game and handle all user actions.
 * 
 * @author chanyuyan
 *
 */
public class BigTwoTable implements CardGameTable {

	/**
	 * A constructor for creating a BigTwoTable.
	 * 
	 * @param game a reference to a card game associated with this table
	 */
	public BigTwoTable(BigTwoClient game) {
		
		this.game = game;
		this.selected = new boolean[13];
		this.buildGUI();
		
	}

	/**
	 * A method for setting the index of the active player.
	 * 
	 * @see CardGameTable#setActivePlayer(int)
	 */
	@Override
	public void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
	}

	/**
	 * A method for getting an array of indices of the cards selected.
	 * 
	 * @see CardGameTable#getSelected()
	 */
	@Override
	public int[] getSelected() {
		
		int intArrLength = 0;
		for (int i = 0; i < selected.length; i++) {
			if (selected[i] == true) {
				intArrLength++;
			}
		}
		if (intArrLength > 0) {
			int[] intArr = new int[intArrLength];
			int count = 0;
			for (int i = 0; i < selected.length; i++) {
				if (selected[i] == true) {
					intArr[count] = i;
					count++;
				}
			}
			return intArr;
		}
		return null;
		
	}

	/**
	 * A method for resetting the list of selected cards.
	 * 
	 * @see CardGameTable#resetSelected()
	 */
	@Override
	public void resetSelected() {
		
		for (int i = 0; i < selected.length; i++) {
			if (selected[i] == true) {
				selected[i] = false;
			}
		}
		
	}

	/**
	 * A method for repainting the GUI.
	 * 
	 * @see CardGameTable#repaint()
	 */
	@Override
	public void repaint() {
		if (game.getPlayerID() != game.getCurrentIdx()) {
			this.disable();
		} else {
			this.enable();
		}
		frame.repaint();
	}

	/**
	 * A method for printing the specified string to the message area of the GUI.
	 * 
	 * @see CardGameTable#printMsg(String)
	 */
	@Override
	public void printMsg(String msg) {
		msgArea.append(msg);
	}

	/**
	 * A method for clearing the message area of the GUi.
	 * 
	 * @see CardGameTable#clearMsgArea()
	 */
	@Override
	public void clearMsgArea() {
		msgArea.setText(null);
	}
	
	/**
	 * A method for printing the specified string to the chat area of the GUI.
	 * 
	 * @param chatMsg the chat message to be printed
	 */
	public void printChatMsg(String chatMsg) {
		chatMsgArea.append(chatMsg);
	}
	
	/**
	 *  A method for clearing the chat area of the GUI.
	 */
	public void clearChatMsgArea() {
		chatMsgArea.setText(null);
	}

	/**
	 * A method for resetting the GUI.
	 * The list of selected cards will be reset.
	 * The message area will be cleared.
	 * User interactions will be enabled.
	 * 
	 * @see CardGameTable#reset()
	 */
	@Override
	public void reset() {
		
		this.resetSelected();
		this.clearMsgArea();
		this.enable();
		
	}

	/**
	 * A method for enabling user interactions with the GUI.
	 * The "Play" and "Pass" buttons will be enabled.
	 * The BigTwoPanel will be enabled for selection of cards through mouse clicks.
	 * 
	 * @see CardGameTable#enable()
	 */
	@Override
	public void enable() {
		
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		bigTwoPanel.setEnabled(true);
		
	}

	/**
	 * A method for disabling user interactions with the GUI.
	 * The "Play" and "Pass" buttons will be disabled.
	 * The BigTwoPanel will be disabled.
	 * 
	 * @see CardGameTable#disable()
	 */
	@Override
	public void disable() {
		
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
		
	}
	
	/**
	 * A method that builds the GUI for the Big Two card game.
	 * 
	 */
	public void buildGUI() {
		
		// Draws the frame background
		this.frame = new JFrame();
		this.frame.setSize(1200,900);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String playerName = this.game.getPlayerName();
		this.frame.setTitle("Big Two ("+playerName+")");
				
		// Draws the lowerPanel, with buttons "Play" and "Pass", and a text field
		JPanel lowerPanel = new JPanel(new BorderLayout());
		
		JPanel buttonPanel = new JPanel();
		this.playButton = new JButton("Play");
		this.passButton = new JButton("Pass");
		this.playButton.addActionListener(new PlayButtonListener());
		this.passButton.addActionListener(new PassButtonListener());
		buttonPanel.add(playButton);
		buttonPanel.add(passButton);
		lowerPanel.add(buttonPanel, BorderLayout.CENTER);
		
		this.textField = new JTextField(30);
		this.textField.addActionListener(new TextFieldListener());
		JPanel textFieldPanel = new JPanel();
		JLabel textFieldLabel = new JLabel("Message: ");
		textFieldPanel.add(textFieldLabel);
		textFieldPanel.add(textField);
		lowerPanel.add(textFieldPanel, BorderLayout.EAST);
		
		this.frame.add(lowerPanel, BorderLayout.SOUTH);
		
		
		// Draws the menu bar, with "Connect" and "Quit"
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		JMenuItem connectOption = new JMenuItem("Connect");
		JMenuItem quitOption = new JMenuItem("Quit");
		quitOption.addActionListener(new QuitMenuItemListener());
		connectOption.addActionListener(new ConnectMenuItemListener());
		menu.add(connectOption);
		menu.add(quitOption);
		menuBar.add(menu);
		this.frame.setJMenuBar(menuBar);
		
		// Draws the hands table with players
		this.bigTwoPanel = new BigTwoPanel();
		this.bigTwoPanel.addMouseListener(new BigTwoPanel());
		this.bigTwoPanel.setBackground(Color.DARK_GRAY);
		this.frame.add(this.bigTwoPanel, BorderLayout.CENTER);
		
		
		JPanel totalMsgPanel = new JPanel();
		totalMsgPanel.setLayout(new BoxLayout(totalMsgPanel, BoxLayout.Y_AXIS));
		// Draws the move message panel
		this.msgArea = new JTextArea(55, 30);
		msgArea.setLineWrap(true);
 		JScrollPane messagePanel = new JScrollPane(this.msgArea); 		
		
		// Draws chat panel
		this.chatMsgArea = new JTextArea(50, 30);
		chatMsgArea.setLineWrap(true);
		JScrollPane chatPanel = new JScrollPane(this.chatMsgArea);
		totalMsgPanel.add(messagePanel);
		totalMsgPanel.add(chatPanel);
		this.frame.add(totalMsgPanel, BorderLayout.EAST);
		
		

		this.frame.setVisible(true);

	}
	
	
	
	/**
	 * a BigTwoClient game associated with this table.
	 */
	private BigTwoClient game;
	
	/**
	 * a boolean array indicating which cards are being selected.
	 */
	private boolean[] selected;
	
	/**
	 * an integer specifying the index of the active player.
	 */
	private int activePlayer;
	
	/**
	 * the main window of the application.
	 */
	private JFrame frame;
	
	/**
	 * a panel for showing the cards of each player and the cards played on the table.
	 */
	private JPanel bigTwoPanel;
	
	/**
	 * a "Play" button for the active player to play the selected cards.
	 */
	private JButton playButton;
	
	/**
	 * a "Pass" button for the active player to pass his/her turn to the next player.
	 */
	private JButton passButton;
	
	
	
	/**
	 * a text area for showing the current game status as well as the end of game messages.
	 */
	private JTextArea msgArea;
	
	
	/**
	 *  a text area for showing the current chat
	 */
	private JTextArea chatMsgArea;
	
	
	/**
	 *  a field for showing the message to be sent
	 */
	private JTextField textField;
	
	/**
	 * a 2D array storing the images for the faces of the cards.
	 */
	private Image[][] cardImages;
	
	/**
	 * an image for the backs of the cards.
	 */
	private Image cardBackImage;
	
	/**
	 * an array storing the images for the avatars.
	 */
	private Image[] avatars;
	
	
	
	/**
	 * An inner class that extends the JPanel class and implements the MouseListener interface.
	 * It overrides the paintComponent() method inherited from the JPanel class to draw
	 * the card game table. It implements the mouseClicked() method from the MouseListener interface to handle mouse click events.
	 * 
	 * @author chanyuyan
	 *
	 */
	class BigTwoPanel extends JPanel implements MouseListener {
		
		private int frameWidth = frame.getWidth();
		private int frameHeight = frame.getHeight();
		private int playerHeight = frameHeight/5-20;
		private int cardWidth = 80;
		private int cardHeight = 100;
		private int cardSepX = 30;
		
		/**
		 * This method overrides the paintComponent in JComponent.
		 * It paints the avatars of players, cards of each player and the last hand played
		 * 
		 * @param g the graphics object
		 */
		public void paintComponent(Graphics g) {
			// Asks the parent component to draw the background
			super.paintComponent(g);
			
			
			cardBackImage = new ImageIcon("images/cardback.jpg").getImage();
			avatars = new Image[4];
			Image avatar0 = new ImageIcon("images/a0.png").getImage();
			avatars[0] = avatar0;
			Image avatar1 = new ImageIcon("images/a1.png").getImage();
			avatars[1] = avatar1;
			Image avatar2 = new ImageIcon("images/a2.png").getImage();
			avatars[2] = avatar2;
			Image avatar3 = new ImageIcon("images/a3.jpg").getImage();
			avatars[3] = avatar3;

			
			// Initializes the cardImages 2D array with card images. 
			// The images have names in format <rank+suit>.png, where rank in {A, 2, 3, ..., 10, J, Q, K}, suit in {D, C, H, S}
			char[] nameRank = { 'A', '2', '3', '4', '5', '6', '7',
					'8', '9', '0', 'J', 'Q', 'K' };
			char[] nameSuit = {'D', 'C', 'H', 'S'};
			cardImages = new Image[4][13];
			
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 13; j++) {
					String imageName = "images/"+nameRank[j]+nameSuit[i]+".png";
					Image cardImage = new ImageIcon(imageName).getImage();
					cardImages[i][j] = cardImage;
				}
			}
			
			
			// Draws players' avatars and cards
			// Draws the player names and text indication of cards played by a certain player
			g.setColor(Color.white);
			for (int i = 0; i < 4; i++) {
				if (game.getPlayerList().get(i).getName() != null) {
					g.drawString(game.getPlayerList().get(i).getName(), 10, 12+i*(playerHeight-10));
					g.drawImage(avatars[i], 10, 15+i*(playerHeight-10), 100, 100, this);
				}
			}
			// Indicates who the last hand belongs to
			int sizeOnTable = game.getHandsOnTable().size();
			if (sizeOnTable > 0) {
				g.drawString("Played by "+game.getHandsOnTable().get(sizeOnTable-1).getPlayer().getName(), 10, 12+4*(playerHeight-10));
			}
			// Draws the avatars
//			for (int i = 0; i < 4; i++) {
//				g.drawImage(avatars[i], 10, 15+i*(playerHeight-10), 100, 100, this);
//			}
//			
			
			// Draws the cards of players
			
			
			for (int i = 0; i < 4; i++) {
				
				if (i != game.getPlayerID()) {
					// Draws the back of the cards for other players
					for (int j = 0; j < game.getPlayerList().get(i).getNumOfCards(); j++) {
						g.drawImage(cardBackImage, 150+cardSepX*j, 20+i*(playerHeight-10), cardWidth, cardHeight, this);
					}
				} else {
					// Draws the faces of the cards for the active player
					CardList activeCards = game.getPlayerList().get(i).getCardsInHand();
					
					for (int j = 0; j < activeCards.size(); j++) {
						Card activeCard = activeCards.getCard(j);
						int activeRank = activeCard.getRank();
						int activeSuit = activeCard.getSuit();
						Image activeCardImage = cardImages[activeSuit][activeRank];

						if (selected[j]) {
							// Selected card is drawn higher
							g.drawImage(activeCardImage, 150+cardSepX*j, 5+i*(playerHeight-10), cardWidth, cardHeight, this);
						} else {
							// Unselected card is drawn at normal y position
							g.drawImage(activeCardImage, 150+cardSepX*j, 20+i*(playerHeight-10), cardWidth, cardHeight, this);
						}

					}
				}
			}
			
			// Draws the hand last played
			int handsOnTableSize = game.getHandsOnTable().size();
			if (handsOnTableSize > 0) {
				Hand lastPlayedHand = game.getHandsOnTable().get(handsOnTableSize-1);
				for (int i = 0; i < lastPlayedHand.size(); i++) {
					Card playedCard = lastPlayedHand.getCard(i);
					int playedCardRank = playedCard.getRank();
					int playedCardSuit = playedCard.getSuit();
					Image playedCardImage = cardImages[playedCardSuit][playedCardRank];
					g.drawImage(playedCardImage, 10+cardSepX*i, 20+4*(playerHeight-10), cardWidth, cardHeight, this);
				}
			}
			
			
			
			this.setBackground(Color.DARK_GRAY);
			
		}

		/**
		 * This method overrides the mouseClicked method in MouseListener.
		 * It processes a click when it lands on a card, and determines whether to select or deselect the card.
		 * 
		 * @param e an event which indicates a mouse action occurred in a component
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			int mouseX = e.getX();
			int mouseY = e.getY();
			int numOfCards = game.getPlayerList().get(activePlayer).getNumOfCards();
			for (int i = numOfCards-1; i >= 0; i--) {
				// Coordinates of top left corner of card
				int cardX = 150+cardSepX*i;
				int cardY = 20+activePlayer*(playerHeight-10);
				int cardYSelected = cardY-15;
				if (selected[i]) {
					// The case when the rightmost card is selected
					if (i == numOfCards-1) {
						if ((mouseX >= cardX && mouseX <= cardX+cardWidth) && (mouseY >= cardYSelected && mouseY <= cardYSelected+cardHeight) ) {
							selected[i] = false;
							frame.repaint();
							return;
						}
					} else {
						// Accounts for upper inverted L shape hit-box
						if (((mouseX >= cardX && mouseX <= cardX+cardSepX) && (mouseY >= cardYSelected && mouseY <= cardYSelected+cardHeight)) 
								|| ((mouseX >= cardX+cardSepX && mouseX <= cardX+cardWidth) && (mouseY >= cardYSelected && mouseY <= cardY))) {
							selected[i] = false;
							frame.repaint();
							return;
						}
					}
				} else {
					if (i == numOfCards-1) {
						if ((mouseX >= cardX && mouseX <= cardX+cardWidth) && (mouseY >= cardY && mouseY <= cardY+cardHeight) ) {
							selected[i] = true;
							frame.repaint();
							return;
						}
					} else {
						
						
						if ((mouseX >= cardX && mouseX <= cardX+cardSepX) && (mouseY >= cardY && mouseY <= cardY+cardHeight)) {
							selected[i] = true;
							frame.repaint();
							return;
						}
						
						// Accounts for the lower L shape hit-box, when cards to the current card's right are selected
						// 1 card to the right raised
						if (selected[i+1]) {
							if ((mouseX >= cardX+cardSepX && mouseX <= cardX+2*cardSepX) && (mouseY >= cardYSelected+cardHeight && mouseY <= cardY+cardHeight)) {
								selected[i] = true;
								frame.repaint();
								return;
							}
							// 2 cards to the right raised
							if (i+2 <= selected.length-1) {
								if (selected[i+2]) {
									if ((mouseX >= cardX+2*cardSepX && mouseX <= cardX+cardWidth) && (mouseY >= cardYSelected+cardHeight && mouseY <= cardY+cardHeight)) {
										selected[i] = true;
										frame.repaint();
										return;
									}
								}
							}

						}
						
						
					}

				}
			}
			
			this.repaint();
			
		}

		@Override
		public void mousePressed(MouseEvent e) {			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {			
		}
		
	}
	
	/**
	 * An inner class that implements the ActionListener interface.
	 * The actionPerformed() method is implemented to handle button-click events for the "Play" button.
	 * When the "Play" button is clicked, the makeMove() method is called.
	 * 
	 * @author chanyuyan
	 *
	 */
	class PlayButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (game.getPlayerID() == game.getCurrentIdx()) {
				if (getSelected() == null) {
					// Prevents the case of passing when no card is selected and play is pressed
					printMsg("Please select at least 1 card in order to play\n");
					frame.repaint();
				} else {
					game.makeMove(activePlayer, getSelected());
					resetSelected();
					frame.repaint();
				} 
			} else {
				printMsg("Please wait for your turn\n");
			}
		}
		
	}
	
	/**
	 * An inner class that implements the ActionListener interface.
	 * The actionPerformed() method is implemented to handle button-click events for the "Pass" button.
	 * When the "Pass" button is clicked, the makeMove() method is called.
	 * 
	 * @author chanyuyan
	 *
	 */
	class PassButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (game.getPlayerID() == game.getCurrentIdx()) {
				game.makeMove(activePlayer, null);
				frame.repaint();
			} else {
				printMsg("Please wait for your turn\n");
			}
		}
		
	}
	
	
	/**
	 * An inner class that implements the ActionListener interface.
	 * The actionPerformed() method is implemented to handle the entering of texts by the player.
	 * It creates a CardGameMessage of type MSG, playerID -1 and data the text input by the user.
	 * This CardGameMessage is then sent to the server.
	 * The text field is set back to null.
	 * 
	 * @author chanyuyan
	 *
	 */
	class TextFieldListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			CardGameMessage inputMsg = new CardGameMessage(CardGameMessage.MSG, -1, textField.getText());
			game.sendMessage(inputMsg);
			textField.setText(null);
		}
		
	}
	
	
	/**
	 * An inner class that implements the ActionListener interface.
	 * The actionPerformed() method is implemented to handle menu-item-click events for the "Connect" menu item.
	 * When the "Connect" menu item is selected, a connection is made to the server.
	 * 
	 * @author chanyuyan
	 *
	 */
	class ConnectMenuItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			game.makeConnection();
		}
		
		
	}
	
	/**
	 * An inner class that implements the ActionListener interface.
	 * The actionPerformed() method is implemented to handle menu-item-click events for the "Quit" menu item.
	 * When the "Quit" menu item is selected, the application will be terminated.
	 * 
	 * @author chanyuyan
	 *
	 */
	class QuitMenuItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
		
	}
	
}
