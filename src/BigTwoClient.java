import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * This class implements the CardGame and NetworkGame interfaces.
 * It is used to model a Big Two card game that supports 4 players playing over the Internet.
 * 
 * @author chanyuyan
 *
 */
public class BigTwoClient implements CardGame, NetworkGame{

	/**
	 * 
	 * A constructor for creating a Big Two client. 
	 * 4 players will be created and added to the list of player. 
	 * A Big Two table which builds the GUI and handles user actions will be created.
	 * A connection to the game server will be made by calling the makeConnection() method from the NetworkGame interface.
	 * 
	 */
	public BigTwoClient() {
		
		// Creates a new player list
		this.playerList = new ArrayList<CardGamePlayer>();
		// Creates a new list of hands of cards player on the table
		this.handsOnTable = new ArrayList<Hand>();
		
		// Creates 4 players
		CardGamePlayer player1 = new CardGamePlayer();
		CardGamePlayer player2 = new CardGamePlayer();
		CardGamePlayer player3 = new CardGamePlayer();
		CardGamePlayer player4 = new CardGamePlayer();
		
		// Adds them into player list
		playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		playerList.add(player4);
		player1.setName(null);
		player2.setName(null);
		player3.setName(null);
		player4.setName(null);
		
		// Prompts the user to enter his/her name
		String nameInput = JOptionPane.showInputDialog("Please input your name: \n");
		while (nameInput.equals("")) {
			nameInput = JOptionPane.showInputDialog("Please input something for your name: \n");
		}
		this.setPlayerName(nameInput);

		// Creates a Big Two table which builds the GUI for the game and handles user actions
		this.table = new BigTwoTable(this);
		this.currentIdx = -1;
		
		// Makes a connection to the game server
		this.makeConnection();
		
	}
	
	// An integer specifying the number of players
	private int numOfPlayers;
	
	// A deck of cards
	private Deck deck;
	
	// A list of players
	private ArrayList<CardGamePlayer> playerList;
	
	// A list of hands played on the table
	private ArrayList<Hand> handsOnTable;
	
	// An integer specifying the playerID of the local player
	private int playerID;
	
	// A string specifying the name of the local player
	private String playerName;
	
	// A string specifying the IP address of the game server
	private String serverIP;
	
	// An integer specifying the TCP port of the game server
	private int serverPort;
	
	// A socket connection to the game server
	private Socket sock;
	
	// An ObjectOutputStream for sending messages to the server
	private ObjectOutputStream oos;
	
	// An integer specifying the index of the player for the current turn
	private int currentIdx;
	
	// A Big Two table which builds the GUI for the game and handles all user actions
	private BigTwoTable table;
	
	
	// NetworkGame interface methods:
	
	/**
	 * A method for getting the playerID of the local player.
	 */
	@Override
	public int getPlayerID() {
		return this.playerID;
	}

	/**
	 * A method for setting the playerID of the local player.
	 * This method would be called from the parseMessage() method when a message of the type PLAYER_LIST is received from the game server.
	 */
	@Override
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
		
	}

	/**
	 * A method for getting the name of the local player.
	 */
	@Override
	public String getPlayerName() {
		return this.playerName;
	}

	/**
	 * A method for setting the name of the local player.
	 */
	@Override
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * A method for getting the IP address of the game server.
	 */
	@Override
	public String getServerIP() {
		return this.serverIP;
	}

	/**
	 * A method for setting the IP address of the game server.
	 */
	@Override
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	/**
	 * A method for getting the TCP port of the game server.
	 */
	@Override
	public int getServerPort() {
		return this.serverPort;
	}

	/**
	 * A method for setting the TCP port of the game server.
	 */
	@Override
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * A method for making a socket connection with the game server.
	 * Upon successful connection, 
	 * (i) an ObjectOutputStream will be created for sending
	 * messages to the game server.
	 * (ii) A thread for receiving messages from the game server will be created.
	 * A message of the type JOIN will be sent to the game server with playerID being -1
	 * and data being a reference to a string representing the name of the local player.
	 * A message of the type READY will be sent to the game server with playerID and data being -1 and null respectively.
	 */
	@Override
	public void makeConnection() {
		this.setServerIP("127.0.0.1");
		this.setServerPort(2396);
		try {
			this.sock = new Socket(this.getServerIP(), this.getServerPort());
			this.oos = new ObjectOutputStream(sock.getOutputStream());
			Thread cThread = new Thread(new ServerHandler(this.sock));
			cThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Cannot use new GameMessage as the server cannot receive
		this.sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, this.getPlayerName()));
		this.sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
	}

	/**
	 * A method for parsing the messages received from the game server.
	 * This method will be called from the thread responsible for receiving messages from the game server.
	 * Based on the message type, different actions will be carried out.
	 */
	@Override
	public void parseMessage(GameMessage message) {
		int msgType = message.getType();
		
		if (msgType == CardGameMessage.PLAYER_LIST) {
			this.setPlayerID(message.getPlayerID());
			String[] nameList = (String[]) message.getData();
			for (int i = 0; i < 4; i++) {
				if (nameList[i] != null) {
					this.playerList.get(i).setName(nameList[i]);
				}
			}
			this.table.repaint();
		} else if (msgType == CardGameMessage.JOIN) {
			this.playerList.get(message.getPlayerID()).setName((String) message.getData());
			if (message.getPlayerID() == this.getPlayerID()) {
				this.sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			}
			String playerName = this.getPlayerList().get(message.getPlayerID()).getName();
			this.table.printMsg(playerName + " joins the game\n");
			this.table.repaint();
		} else if (msgType == CardGameMessage.FULL) {
			this.table.printMsg("Unable to join game: the server is full\n");
			this.table.repaint();
		} else if (msgType == CardGameMessage.QUIT) {
			this.playerList.get(message.getPlayerID()).setName(null);
			if (this.endOfGame() == false) {
				// Stops the game
				this.table.disable();
				this.sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			}
			this.table.repaint();
		} else if (msgType == CardGameMessage.READY) {
			String playerName = this.getPlayerList().get(message.getPlayerID()).getName();
			this.table.printMsg(playerName + " is ready\n");
			this.table.repaint();
		} else if (msgType == CardGameMessage.START) {
			this.start((BigTwoDeck) message.getData());
			this.table.repaint();
		} else if (msgType == CardGameMessage.MOVE) {
			this.checkMove(message.getPlayerID(), (int[]) message.getData());
			this.table.repaint();
		} else if (msgType == CardGameMessage.MSG) {
			this.table.printChatMsg((String) message.getData() + "\n");
			this.table.repaint();
		}
	}

	/**
	 * A method for sending the specified message to the game server.
	 * This method will be called whenever the client wants to communicate with the game server or other clients.
	 */
	@Override
	public void sendMessage(GameMessage message) {
		try {
			this.oos.writeObject(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// CardGame interface methods:

	/**
	 * A method for getting the number of players.
	 */
	@Override
	public int getNumOfPlayers() {
		return this.numOfPlayers;
	}

	/**
	 * A method for getting the deck of cards being used.
	 */
	@Override
	public Deck getDeck() {
		return this.deck;
	}

	/**
	 * A method for getting the list of players.
	 */
	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		return this.playerList;
	}

	/**
	 * A method for getting the list of hands played on the table.
	 */
	@Override
	public ArrayList<Hand> getHandsOnTable() {
		return this.handsOnTable;
	}

	/**
	 * A method for getting the index of the player for the current turn.
	 */
	@Override
	public int getCurrentIdx() {
		return this.currentIdx;
	}

	/**
	 * A method for starting/restarting the game with a given shuffled deck of cards.
	 * All the cards from the players as well as the table will be removed.
	 * The cards will be distributed to the players.
	 * The player who holds the 3 of Diamonds will be identified.
	 * The currentIdx of the BigTwoClient instance will be set to the playerID of the player holding the 3 of Diamonds.
	 * The activePlayer of the BigTwoTable instance will be set to the playerID of the local player.
	 */
	@Override
	public void start(Deck deck) {
		// Removes all cards from players
		for (int i = 0; i < this.playerList.size(); i++) {
			this.playerList.get(i).removeAllCards();
		}
		// Removes all cards from the table
		this.handsOnTable.clear();
		
		// Distributes cards to players one by one
		for (int i = 0; i < 13; ++i) {
			for (int j = 0; j < 4; ++j) {
				playerList.get(j).addCard(deck.getCard(4*i+j));
			}
		}
		
		// Sorts the hands of all players
		for (int i = 0; i < 4; ++i) {
			playerList.get(i).sortCardsInHand();
		}
		
		// Finds the player with three of diamonds
		// Sets currentIdx and activePlayer
		Card threeOfDiamonds = new Card(0, 2);
		
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 13; ++j) {
				if (playerList.get(i).getCardsInHand().contains(threeOfDiamonds)) {
					this.currentIdx = i;
					this.table.setActivePlayer(i);
					break;
				}
			}
		}
		
		this.table.clearMsgArea();
		this.table.printMsg("All players are ready. The game starts now.\n");
		this.printPlayerTurn();
		this.table.repaint();
		
	}

	/**
	 * A method for making a move by a player with the specified playerID using the cards specified by the list of indices.
	 * This method will be called from the BigTwoTable when the local player presses either the "Play" or "Pass" button.
	 * A CardGameMessage object of the type MOVE will be created with the playerID and data in the message being -1 and cardIdx respectively.
	 * It will be sent to the game server using the sendMessage() method from the NetworkGame interface.
	 */
	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		this.sendMessage(new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx));
	}

	/**
	 * A method for checking a move made by a player.
	 * This method will be called from the parseMessage() method from the NetworkGame interface
	 * when a message of the type MOVE is received from the game server.
	 * The playerID and data in the message give the playerID of the player who makes
	 * the move and a reference to a regular array of integers specifying the indices of the selected cards respectively.
	 * These are used as the arguments in calling the checkMove() method.
	 */
	@Override
	public void checkMove(int playerID, int[] cardIdx) {
		CardGamePlayer currentPlayer = playerList.get(playerID);
		CardList currentCards = currentPlayer.play(cardIdx);

		if (cardIdx == null) {
			if (!handsOnTable.isEmpty()) {
				
				// Prevents passing of a player's own hand
				int sizeOnTable = handsOnTable.size();
				Hand lastHand = handsOnTable.get(sizeOnTable-1);
				
				if (lastHand.getPlayer() == currentPlayer) {
					
					table.printMsg("Not a legal move!!!\n");
					return;
					
				} else {
				
					// Legal pass move
					table.printMsg("{Pass}\n");
					currentIdx = (currentIdx + 1) % 4;
					table.setActivePlayer(getCurrentIdx());
					this.printPlayerTurn();
					table.repaint();
					return;
				}
	
			} else if (handsOnTable.isEmpty()) {
				
				// Cannot pass if the player is the first to move
				table.printMsg("Not a legal move!!!\n");
				return;
				
			}
		}
		
		Hand currentHand = composeHand(currentPlayer, currentCards);
		
		Card threeOfDiamonds = new Card(0, 2);

		
		if (currentHand == null) {
			
			// Cannot use a hand that doesn't fit into the Big Two ruleset hand
			table.printMsg("Not a legal move!!!\n");
			return;
			
		} else {
			
			if (handsOnTable.isEmpty()) {
				
				if (!currentHand.contains(threeOfDiamonds) && handsOnTable.isEmpty()) {
					
					// Must have three of diamonds as starting move
					table.printMsg("Not a legal move!!!\n");
					return;
					
				} else {
					
					// Legal first turn move
					table.printMsg("{" + currentHand.getType() + "} ");
					table.printMsg(currentHand.toString()+"\n");
					currentHand.print(true, false);
					// Updates the hands on table
					currentPlayer.removeCards(currentHand);
					this.handsOnTable.add(currentHand);
					currentIdx = (currentIdx + 1) % 4;
					table.setActivePlayer(getCurrentIdx());
					this.printPlayerTurn();
					table.repaint();
					return;
				}
		
			} else {

				// Legal non-pass move
				if (currentHand.beats(this.getHandsOnTable().get(handsOnTable.size()-1))) {
				
					table.printMsg("{" + currentHand.getType() + "} ");
					table.printMsg(currentHand.toString()+"\n");

					currentHand.print(true, false);
					// Updates the hands on table
					currentPlayer.removeCards(currentHand);
					this.handsOnTable.add(currentHand);

					
					// Accounts for the possibility of end of game once a hand is removed
					if (this.endOfGame()) {
						this.endGamePop();
						// Game summary in the end
						table.printMsg("Game ends\n");
						for (int i = 0; i < 4; i++) {
							CardGamePlayer playerInReport = this.playerList.get(i);
							if (playerInReport.getNumOfCards() == 0) {
								table.printMsg(playerInReport.getName() + " wins the game.\n");
								
							} else {
								table.printMsg(playerInReport.getName() + " has " + playerInReport.getNumOfCards() + " cards in hand.\n");
							}
						}
						table.disable();
						return;
					}

					
					currentIdx = (currentIdx + 1) % 4;
					table.setActivePlayer(getCurrentIdx());
					this.printPlayerTurn();
					table.repaint();
					return;
				
				} else {
					
					// When the player beats everyone else and the turn gets back to him
					int sizeOnTable = handsOnTable.size();
					Hand lastHand = handsOnTable.get(sizeOnTable-1);
					if (lastHand.getPlayer() == currentPlayer) {
						
						table.printMsg("{" + currentHand.getType() + "} ");
						table.printMsg(currentHand.toString()+"\n");
						currentHand.print(true, false);
						// Updates the hands on table
						currentPlayer.removeCards(currentHand);
						this.handsOnTable.add(currentHand);
						
						// Accounts for the possibility of end of game once a hand is removed
						if (this.endOfGame()) {
							this.endGamePop();
							// Game summary in the end
							table.printMsg("Game ends\n");
							for (int i = 0; i < 4; i++) {
								CardGamePlayer playerInReport = this.playerList.get(i);
								if (playerInReport.getNumOfCards() == 0) {
									table.printMsg(playerInReport.getName() + " wins the game.\n");
									
								} else {
									table.printMsg(playerInReport.getName() + " has " + playerInReport.getNumOfCards() + " cards in hand.\n");
								}
							}
							table.disable();
							return;
						}

						
						currentIdx = (currentIdx + 1) % 4;
						table.setActivePlayer(getCurrentIdx());
						this.printPlayerTurn();
						table.repaint();
						
					} else {
						
						// Purely a different player with a hand that cannot beat the current hand
						table.printMsg("Not a legal move!!!\n");
						
					}
					
					return;
					
				}
			
			}
			
		}
	}

	/**
	 * A method for checking if the game ends.
	 */
	@Override
	public boolean endOfGame() {
		for (int i = 0; i < this.playerList.size(); i++) {
			if (this.playerList.get(i).getNumOfCards() <= 0) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * An inner class that implements the Runnable interface.
	 * The run() method from the Runnable interface is implemented.
	 * A thread with an instance of this class as its job in the makeConnection() method from the
	 * NetworkGame interface for receiving messages from the game server is created.
	 * Upon receiving a message, the parseMessage() method from the Networkgame interface is called
	 * to parse the messages accordingly.
	 * 
	 * 
	 * @author chanyuyan
	 *
	 */
	class ServerHandler implements Runnable{
		private Socket serverHandlerSocket;
		private ObjectInputStream ois;
		//private CardGameMessage cgMessage;
		
		/**
		 * Creates and returns an instance of the ClientHandler class.
		 * 
		 * @param serverHandlerSocket the socket connection to the server
		 */
		public ServerHandler(Socket serverHandlerSocket) {
			this.serverHandlerSocket = serverHandlerSocket;
			try {
				this.ois = new ObjectInputStream(serverHandlerSocket.getInputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Implementation method from the Runnable interface.
		 */
		public void run() {
			CardGameMessage cgMessage;
			try {
				while ((cgMessage = (CardGameMessage) ois.readObject()) != null) {
					parseMessage(cgMessage);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * A method for creating an instance of BigTwoClient
	 * 
	 * @param args unused
	 */
	public static void main(String[] args) {
		BigTwoClient bigTwoClient = new BigTwoClient();
	}
	
	/**
	 * Gives a valid hand from the specified list of cards of a player.
	 * @param player the player whose hand is to be returned
	 * @param cards the specified list of cards of the player
	 * @return A valid hand of the player from the specified list. Null if no valid hand can be composed.
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		
		if (cards.size() == 1) {
			Hand single = new Single(player, cards);
			if (single.isValid()) {
				return single;
			}
		} else if (cards.size() == 2) {
			Hand formedHand = new Pair(player, cards);
			if (formedHand.isValid()) {
				return formedHand;
			}
		} else if (cards.size() == 3) {
			Hand triple = new Triple(player, cards);
			if (triple.isValid()) {
				return triple;
			}
		} else if (cards.size() == 5) {
			Hand straight = new Straight(player, cards);
			Hand flush = new Flush(player, cards);
			Hand fullhouse = new FullHouse(player, cards);
			Hand quad = new Quad(player, cards);
			Hand straightflush = new StraightFlush(player, cards);
			if (straightflush.isValid()) {
				return straightflush;
			}
			if (quad.isValid()) {
				return quad;
			}
			if (fullhouse.isValid()) {
				return fullhouse;
			}
			if (flush.isValid()) {
				return flush;
			}
			if (straight.isValid()) {
				return straight;
			}
			
		}
		
		return null;
		
	}
	
	/**
	 * A private method for printing which player's turn it is
	 */
	private void printPlayerTurn() {
		
		CardGamePlayer currentPlayer = this.getPlayerList().get(this.getCurrentIdx());
		if (this.getPlayerID() == this.getCurrentIdx()) {
			this.table.printMsg("Your turn:\n");
		} else {
			this.table.printMsg(currentPlayer.getName()+"'s turn:\n");
		}
		
	}
	
	/**
	 * A private method for the pop up dialog of end game results
	 */
	private void endGamePop() {
		
		
		
		String results = "";
		results += "Game ends\n";
		for (int i = 0; i < 4; i++) {
			CardGamePlayer playerInReport = this.playerList.get(i);
			if (playerInReport.getNumOfCards() == 0) {
				if (i == this.getPlayerID()) {
					results += ("You win!\n");
				} else {
					results += (playerInReport.getName() + " wins the game.\n");
				}
			} else {
				if (i == this.getPlayerID()) {
					results += ("You have " + playerInReport.getNumOfCards() + " cards left.\n");
				} else {
					results += (playerInReport.getName() + " has " + playerInReport.getNumOfCards() + " cards left.\n");
				}
			}
		}
		
		JOptionPane.showMessageDialog(null, results);
		
		CardGameMessage endMsg = new CardGameMessage(CardGameMessage.READY, -1, null);
		this.sendMessage(endMsg);
		
	}

}
