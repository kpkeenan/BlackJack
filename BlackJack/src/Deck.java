import java.util.NoSuchElementException;

public class Deck {
    private Card[] deck;
    private int size = 0;

    public Deck(int capacity) {
        deck = new Card[capacity];
    }

    public void push(Card item) {
        if (size == deck.length) {
            throw new IllegalStateException("Cannot add to full stack");
        }
        deck[size++] = item;
    }

    public Card pop() {
        if (size == 0) {
            throw new NoSuchElementException("Cannot pop from empty stack");
        }
        Card result = deck[size-1];
        deck[--size] = null;
        return result;
    }

    public Card peek() {
        if (size == 0) {
            throw new NoSuchElementException("Cannot peek into empty stack");
        }
        return deck[size - 1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}