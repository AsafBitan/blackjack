import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class BlackJack {
    private class Card {
        String value;
        String type;

        Card(String value, String type) {
            this.value = value;
            this.type = type;
        }

        public String toString() {
            return value + "-" + type;
        }

        public int getValue() {
            if ("AJQK".contains(value)) { // A J Q K
                if (value == "A") {
                    return 11;
                }
                return 10;
            }
            return Integer.parseInt(value); // 2 - 10
        }

        public boolean isAce() {
            if (value == "A") {
                return true;
            }
            return false;
        }
    }

    ArrayList<Card> deck;
    Random random = new Random();

    // dealer
    Card hiddeCard;
    ArrayList<Card> dealersHand;
    int dealersSum;
    int dealersAceCount;

    // player
    ArrayList<Card> playersHand;
    int playersSum;
    int playersAceCount;

    BlackJack() {
        // deck
        startGame();
        shuffleDeck();

        // dealers hand
        dealersHand = new ArrayList<Card>();
        dealersSum = 0;
        dealersAceCount = 0;

        hiddeCard = deck.remove(deck.size() - 1); // remove card at the last index
        dealersSum += hiddeCard.getValue();
        dealersAceCount += hiddeCard.isAce() ? 1 : 0;

        Card card = deck.remove(deck.size() - 1);
        dealersSum += card.getValue();
        dealersAceCount += card.isAce() ? 1 : 0;
        dealersHand.add(card);

        System.out.println("Dealer: ");
        System.out.println(hiddeCard);
        System.out.println(dealersHand);
        System.out.println(dealersSum);
        System.out.println(dealersAceCount);

        // players hand
        playersHand = new ArrayList<Card>();
        playersSum = 0;
        playersAceCount = 0;

        for (int i = 0; i < 2; i++) {
            card = deck.remove(deck.size() - 1);
            playersSum += card.getValue();
            playersAceCount += card.isAce() ? 1 : 0;
            playersHand.add(card);
        }

        System.out.println("Dealer: ");
        System.out.println(playersHand);
        System.out.println(playersSum);
        System.out.println(playersAceCount);
    }

    public void startGame() {
        // deck
        buildDeck();
    }

    public void buildDeck() {
        deck = new ArrayList<Card>();
        String[] values = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
        String[] type = { "C", "D", "H", "S" };

        for (int i = 0; i < type.length; i++) {
            for (int j = 0; j < values.length; j++) {
                Card card = new Card(values[j], type[i]);
                deck.add(card);
            }
        }
        System.out.println("Build Deck: ");
        System.out.println(deck);
    }

    public void shuffleDeck() {
        for (int i = 0; i < deck.size(); i++) {
            int j = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currCard);
        }
        System.out.println("After Shuffle: ");
        System.out.println(deck);
    }
}
