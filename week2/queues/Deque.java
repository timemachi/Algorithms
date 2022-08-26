import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private Node first;
    private Node last;

    private class Node {
        Item item;
        Node leftNext;
        Node rightNext;
    }

    // construct an empty deque
    public Deque() {
        size = 0;
        first = null;
        last = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("");
        }
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.leftNext = null;
        first.rightNext = null;

        if (isEmpty()) {
            last = first;
        }
        else {
            first.rightNext = oldFirst;
            oldFirst.leftNext = first;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("");
        }
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.leftNext = null;
        last.rightNext = null;

        if (isEmpty()) {
            first = last;
        }
        else {
            last.leftNext = oldLast;
            oldLast.rightNext = last;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item firstItem = first.item;
        size--;
        if (size == 0) {
            first = null;
            last = null;
        }
        else {
            first = first.rightNext;
            first.leftNext = null;
        }
        return firstItem;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item lastItem = last.item;
        size--;
        if (size == 0) {
            first = null;
            last = null;
        }
        else {
            last = last.leftNext;
            last.rightNext = null;
        }
        return lastItem;

    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item currentItem = current.item;
            current = current.rightNext;
            return currentItem;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> dq = new Deque();
        dq.addFirst(4);
        dq.addFirst(3);
        dq.addFirst(2);
        dq.addFirst(1);
        dq.addLast(5);
        for (int i : dq) {
            System.out.println(i);
        }
    }
}
