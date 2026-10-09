package src.Card;

import java.util.ArrayList;
import java.util.Collections;

public class CardSet {
    private String name;
    private String[] images;

    public CardSet(String name, String[] images) {

        this.name = name;
        this.images = images;
    }

    public String getName() {

        return name;
    }

    public ArrayList<Card> createCards() {

        ArrayList<Card> cards = new ArrayList<>();

        for (String image : images) {

            cards.add(new Card(image));
            cards.add(new Card(image));
        }

        Collections.shuffle(cards);

        return cards;
    }
}
