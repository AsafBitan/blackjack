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

        public String getImagePath(){
            return "./cards/" + toString() + ".png";
        }
    }

    ArrayList<Card> deck;
    Random random = new Random();

    // dealer
    Card hiddenCard;
    ArrayList<Card> dealersHand;
    int dealersSum;
    int dealersAceCount;

    // player
    ArrayList<Card> playersHand;
    int playersSum;
    int playersAceCount;

    // window 
    int boardWidth = 600;
    int boardHight = boardWidth;

    // card
    int cardWidth = 110;
    int cardHight = 154;

    JFrame frame = new JFrame("Black Jack");
    JPanel gamePanel = new JPanel(){
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            try{
                // draw hidden card
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                if (!stayButton.isEnabled()){
                    hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                }
                g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHight, null);

                // draw dealers hand
                for (int i = 0; i < dealersHand.size(); i++){
                    Card card = dealersHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, cardWidth + 25 + (cardWidth + 5)*i, 20, cardWidth, cardHight, null);
                }

                // draw players hand 
                for (int i = 0; i < playersHand.size(); i++){
                    Card card = playersHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, 20 + (cardWidth + 5)*i, 320, cardWidth, cardHight, null);
                }

                if (!stayButton.isEnabled()){
                    dealersSum = reduceDealerAce();
                    playersSum = reducePlayerAce();
                    System.out.println("STAY: ");
                    System.out.println(dealersSum);
                    System.out.println(playersSum);

                    String message = "";
                    if (playersSum > 21){
                        message = "YOU LOSE!";
                    }
                    else if (dealersSum > 21){
                        message = "YOU WIN!";
                    }
                    else if (playersSum == dealersSum){
                        message = "TIE!";
                    }
                    else if (playersSum > dealersSum){
                        message = "YOU WIN!";
                    }
                    else if (playersSum < dealersSum){
                        message = "YOU LOSE!";
                    }
                    g.setFont(new Font("Arial", Font.PLAIN, 30));
                    g.setColor(Color.white);
                    g.drawString(message, 220, 250);
                }

            } catch(Exception e){
                e.printStackTrace();
            }
            
        }
    };
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");

    BlackJack() {
        startGame();
        
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));
        frame.add(gamePanel);

        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Card card = deck.remove(deck.size() - 1);
                playersSum += card.getValue();
                playersAceCount += card.isAce()? 1 : 0;
                playersHand.add(card);
                if (reducePlayerAce() > 21) {
                    hitButton.setEnabled(false);
                }


                gamePanel.repaint();
            }
        });
        stayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);

                while (dealersSum < 17){
                    Card card = deck.remove(deck.size() - 1);
                    dealersSum += card.getValue();
                    dealersAceCount += card.isAce()? 1 : 0;
                    dealersHand.add(card);
                }

                gamePanel.repaint();
            }
        });
        gamePanel.repaint();
    }

    public void startGame() {
        // deck
        buildDeck();
        shuffleDeck();

        // dealers hand
        dealersHand = new ArrayList<Card>();
        dealersSum = 0;
        dealersAceCount = 0;

        hiddenCard = deck.remove(deck.size() - 1); // remove card at the last index
        dealersSum += hiddenCard.getValue();
        dealersAceCount += hiddenCard.isAce() ? 1 : 0;

        Card card = deck.remove(deck.size() - 1);
        dealersSum += card.getValue();
        dealersAceCount += card.isAce() ? 1 : 0;
        dealersHand.add(card);

        System.out.println("Dealer: ");
        System.out.println(hiddenCard);
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

    public int reducePlayerAce(){
        while (playersSum > 21 && playersAceCount > 0) {
            playersSum -= 10;
            playersAceCount -= 1;
        }
        return playersSum;
    }

    public int reduceDealerAce(){
        while (dealersSum > 21 && dealersAceCount > 0) {
            dealersSum -= 10;
            dealersAceCount -= 1;
        }
        return dealersSum;
    }
}
