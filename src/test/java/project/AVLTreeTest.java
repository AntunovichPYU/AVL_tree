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
        AVLTree<Integer> tree = new AVLTree<>();
        tree.add(5);
        tree.add(2);
        assertEquals(2, tree.height());
        tree.add(3);
        assertEquals(2, tree.height());
    }

    @Test
    void add() {
        AVLTree<Integer> newTree = new AVLTree<>();
        assertTrue(newTree.add(5));
        assertTrue(newTree.add(3));
        assertTrue(newTree.add(9));
        assertFalse(newTree.add(9));
        assertTrue(newTree.add(12));
        assertTrue(newTree.add(4));
        assertTrue(newTree.add(16));
        assertTrue(newTree.add(18));
        assertTrue(newTree.add(25));
        assertTrue(newTree.add(13));
        assertTrue(newTree.add(39));
        assertEquals(4, newTree.height());
    }

    @Test
    void remove() {
        AVLTree<Integer> newTree = new AVLTree<>();
        newTree.add(5);
        newTree.add(3);
        newTree.add(9);
        newTree.add(12);
        newTree.add(4);
        newTree.add(16);
        newTree.add(18);
        newTree.add(25);
        newTree.add(13);
        newTree.add(39);
        assertFalse(newTree.remove(15));
        assertTrue(newTree.remove(3));
    }

    @Test
    void firstAndLast() {
        AVLTree<Integer> newTree = new AVLTree<>();
        newTree.add(5);
        newTree.add(3);
        newTree.add(9);
        newTree.add(12);
        newTree.add(4);
        newTree.add(16);
        newTree.add(18);
        newTree.add(25);
        newTree.add(13);
        newTree.add(39);
        assertEquals(3, newTree.first());
        assertEquals(39, newTree.last());
    }
}