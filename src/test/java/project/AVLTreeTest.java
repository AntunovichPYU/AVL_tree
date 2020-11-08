package project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AVLTreeTest {

    @Test
    void size() {
        AVLTree<Integer> tree = new AVLTree<>();
        tree.add(3);
        tree.add(12);
        tree.add(5);
        assertEquals(3, tree.size());
    }

    @Test
    void contains() {
        AVLTree<Integer> tree = new AVLTree<>();
        tree.add(44);
        assertTrue(tree.contains(44));
        assertFalse(tree.contains(13));
    }

    @Test
    void height() {
    }

    @Test
    void add() {
    }

    @Test
    void remove() {
    }

    @Test
    void first() {
    }

    @Test
    void last() {
    }
}