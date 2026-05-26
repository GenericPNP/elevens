package elevens;
import java.util.List;

import javax.swing.ListModel;

import java.util.ArrayList;

/**
 * The ElevensBoard class represents the board in a game of Elevens.
 */
public class ElevensBoard extends Board {

	/**
	 * The size (number of cards) on the board.
	 */
	private static final int BOARD_SIZE = 9;

	/**
	 * The ranks of the cards for this game to be sent to the deck.
	 */
	private static final String[] RANKS =
		{"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};

	/**
	 * The suits of the cards for this game to be sent to the deck.
	 */
	private static final String[] SUITS =
		{"spades", "hearts", "diamonds", "clubs"};

	/**
	 * The values of the cards for this game to be sent to the deck.
	 */
	private static final int[] POINT_VALUES =
		{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 0, 0, 0};

	/**
	 * Creates a new <code>ElevensBoard</code> instance.
	 */
	 public ElevensBoard() {
	 	super(BOARD_SIZE, RANKS, SUITS, POINT_VALUES);
	 }

	/**
	 * Determines if the selected cards form a valid group for removal.
	 * In Elevens, the legal groups are (1) a pair of non-face cards
	 * whose values add to 11, and (2) a group of three cards consisting of
	 * a jack, a queen, and a king in some order.
	 * @param selectedCards the list of the indices of the selected cards.
	 * @return true if the selected cards form a valid group for removal;
	 *         false otherwise.
	 */
	@Override
	public boolean isLegal(List<Integer> selectedCards) {
		/* *** TO BE MODIFIED IN ACTIVITY 11 *** */
		if (selectedCards.size() == 2) {
				return true;
		} else if (selectedCards.size() == 3) {
				return true;
		}
		return false;
	}

	/**
	 * Determine if there are any legal plays left on the board.
	 * In Elevens, there is a legal play if the board contains
	 * (1) a pair of non-face cards whose values add to 11, or (2) a group
	 * of three cards consisting of a jack, a queen, and a king in some order.
	 * @return true if there is a legal play left on the board;
	 *         false otherwise.
	 */
	@Override
	public boolean anotherPlayIsPossible() {
		/* *** TO BE MODIFIED IN ACTIVITY 11 *** */
		List<Integer> cIndexes = cardIndexes();
		return (findPairSum11(cIndexes).size() > 0) || (findJQK(cIndexes).size() > 0);
	}

	/**
	 * Look for an 11-pair in the selected cards.
	 * @param selectedCards selects a subset of this board.  It is list
	 *                      of indexes into this board that are searched
	 *                      to find an 11-pair.
	 * @return a list of the indexes of an 11-pair, if an 11-pair was found;
	 *         an empty list, if an 11-pair was not found.
	 */
	private List<Integer> findPairSum11(List<Integer> selectedCards) {
		/* *** TO BE CHANGED INTO findPairSum11 IN ACTIVITY 11 *** */
		List<Integer> cardsToReturn = new ArrayList<Integer>();
		Card c1, c2;
		
		for(int i = 0; i < selectedCards.size(); i++) {
			c1 = cardAt(selectedCards.get(i));
			if(selectedCards.size() > i + 1){
				for(int j = i + 1; j < selectedCards.size(); j++) {
					c2 = cardAt(selectedCards.get(j));
					if(c1 != null && c2 != null && c1.pointValue() + c2.pointValue() == 11) {
						cardsToReturn.add(selectedCards.get(i));
						cardsToReturn.add(selectedCards.get(j));
						return cardsToReturn;
					}
				}
			}
		}
		return cardsToReturn;
	}

	/**
	 * Look for a JQK in the selected cards.
	 * @param selectedCards selects a subset of this board.  It is list
	 *                      of indexes into this board that are searched
	 *                      to find a JQK group.
	 * @return a list of the indexes of a JQK, if a JQK was found;
	 *         an empty list, if a JQK was not found.
	 */
	private List<Integer> findJQK(List<Integer> selectedCards) {
		List<Integer> list = new ArrayList<Integer>(3);
		int Jack = 0 ,Queen = 0,King = 0;
		boolean foundJack=false;
		boolean foundQueen=false;
		boolean foundKing=false;

			for(Integer kObj : selectedCards){
				int k=kObj.intValue();
				if(cardAt(k).rank().equals("jack")){
					foundJack=true;
					Jack=k;
				}
				else if(cardAt(k).rank().equals("queen")){
					foundQueen=true;
					Queen=k;
				}
				else if(cardAt(k).rank().equals("king")) {
					foundKing=true;
					King=k;
				}
			}
		  
		if(foundJack&&foundQueen&&foundKing){
			list.add(Integer.valueOf(Jack));
			list.add(Integer.valueOf(Queen));
			list.add(Integer.valueOf(King));
		}
		  
		return list;
	}

	/**
	 * Looks for a legal play on the board.  If one is found, it plays it.
	 * @return true if a legal play was found (and made); false othewise.
	 */
	public boolean playIfPossible() {
		List<Integer> positions = cardIndexes();
			List<Integer> playableCards = findJQK(positions);
			if(playableCards.size() == 3) {
				replaceSelectedCards(playableCards);
				return true;
			}
			playableCards=findPairSum11(positions);
			if(playableCards.size() == 2) {
				replaceSelectedCards(playableCards);
				return true;
			}
		return false;
	}

	/**
	 * Looks for a pair of non-face cards whose values sum to 11.
	 * If found, replace them with the next two cards in the deck.
	 * The simulation of this game uses this method.
	 * @return true if an 11-pair play was found (and made); false othewise.
	 */
	private boolean playPairSum11IfPossible() {
		List<Integer> pair = new ArrayList<Integer>();
		List<Integer> allCards = new ArrayList<Integer>();
		allCards = this.cardIndexes();
		pair.add(0);
		pair.add(0);
		for(int i=0; i < allCards.size(); i++) {
				pair.set(0, allCards.get(i));
			
			for(int x=0; x < allCards.size(); x++) {
				if(x != i) {
					pair.set(1, allCards.get(x));
				}
				List<Integer> results = new ArrayList<Integer>();
				if(isLegal(pair)) {
					results = findPairSum11(pair);
					if(results.size() > 0) {
						this.replaceSelectedCards(results);
						return true;
					}
				}
			}
		}
		 return false;
	}

	/**
	 * Looks for a group of three face cards JQK.
	 * If found, replace them with the next three cards in the deck.
	 * The simulation of this game uses this method.
	 * @return true if a JQK play was found (and made); false othewise.
	 */
	private boolean playJQKIfPossible() {
		List<Integer> pair = new ArrayList<Integer>();
		pair.add(0);
		pair.add(0);
		
		for(int i=0; i < this.cardIndexes().size(); i++) {
			pair.set(0, this.cardIndexes().get(i));
			for(int x=0; x < this.cardIndexes().size(); x++) {
				if(x != i) {
					pair.set(1, this.cardIndexes().get(x));
				}
				List<Integer> results = new ArrayList<Integer>();
				if(isLegal(pair)) {
					results = findJQK(pair);
					if(results.size() > 0) {
						this.replaceSelectedCards(results);
						return true;
					}
				}
			}
		}
		 return false;
	}
}
