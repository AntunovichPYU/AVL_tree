package project;

import org.junit.jupiter.api.Test;

import java.util.*;

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
        Random random = new Random();
        for (int iteration = 0; iteration < 100; iteration++) {
            Set<Integer> controlSet = new HashSet<>();
            AVLTree<Integer> newTree = new AVLTree<>();
            for (int i = 0; i < 10; i++) {
                controlSet.add(random.nextInt(100));
            }
            for (int el: controlSet) {
                assertTrue(newTree.add(el));
                assertFalse(newTree.add(el));
            }
            System.out.println(controlSet);
            System.out.println(newTree);
            assertEquals(controlSet.size(), newTree.size());
            assertTrue(newTree.height() <= 1.45 * (Math.log(newTree.size() + 2) / Math.log(2)));
            for (int el: controlSet) {
                assertTrue(newTree.contains(el));
            }
        }
    }

    @Test
    void remove() {
        Random random = new Random();
        for (int iteration = 0; iteration < 100; iteration++) {
            Set<Integer> controlSet = new TreeSet<>();
            int removeIndex = random.nextInt(20);
            int toRemove = 0;
            for (int i = 0; i < 20; i++) {
                int newNumber = random.nextInt(100);
                controlSet.add(newNumber);
                if (i == removeIndex) {
                    toRemove = newNumber;
                }
            }
            System.out.println("Initial set: " + controlSet);
            AVLTree<Integer> newTree = new AVLTree<>();
            newTree.addAll(controlSet);
            controlSet.remove(toRemove);

            System.out.println("Control set: " + controlSet);
            int expectedSize = newTree.size() - 1;
            double maxHeight = newTree.height();

            System.out.println("Removing " + toRemove);
            assertTrue(newTree.remove(toRemove));
            assertTrue(newTree.height() <= maxHeight);
            assertFalse(newTree.remove(toRemove));
            assertEquals(expectedSize, newTree.size());
            assertTrue(newTree.height() <= 1.45 * (Math.log(newTree.size() + 2) / Math.log(2)));
            for (int el: controlSet) {
                assertTrue(newTree.contains(el));
            }
        }
    }

    @Test
    void iterator() {
        Random random = new Random();
        for (int iteration = 0; iteration < 100; iteration++) {
            Set<Integer> controlSet = new TreeSet<>();
            for (int i = 0; i < 20; i++) {
                controlSet.add(random.nextInt(100));
            }
            System.out.println("Control set: " + controlSet);
            AVLTree<Integer> newTree = new AVLTree<>();
            assertFalse(newTree.iterator().hasNext());

            newTree.addAll(controlSet);
            Iterator<Integer> controlIt = controlSet.iterator();
            Iterator<Integer> actualIt = newTree.iterator();
            System.out.println("Checking if the iterator works correctly");
            while (controlIt.hasNext()) {
                assertEquals(controlIt.next(), actualIt.next());
            }
            assertThrows(NoSuchElementException.class, actualIt::next);
            System.out.println("All correct");
        }
    }

    @Test
    void toArray() {
        Random random = new Random();
        for (int iteration = 0; iteration < 100; iteration++) {
            Set<Integer> controlArray = new TreeSet<>();
            for (int i = 0; i < 20; i++) {
                controlArray.add(random.nextInt(100));
            }

            AVLTree<Integer> newTree = new AVLTree<>();
            newTree.addAll(controlArray);

            assertTrue(Arrays.equals(controlArray.toArray(), newTree.toArray()));
        }
    }

    @Test
    void testToArray() {
        Random random = new Random();
        for (int iteration = 0; iteration < 100; iteration++) {
            Set<Integer> controlArray = new TreeSet<>();
            for (int i = 0; i < 20; i++) {
                controlArray.add(random.nextInt(100));
            }

            AVLTree<Integer> newTree = new AVLTree<>();
            newTree.addAll(controlArray);

            assertTrue(Arrays.equals(controlArray.toArray(new Integer[0]), newTree.toArray(new Integer[0])));
            assertTrue(Arrays.equals(controlArray.toArray(new Integer[20]), newTree.toArray(new Integer[20])));
        }
    }

    @Test
    void containsAll() {
        Random random = new Random();
        for (int iteration = 0; iteration < 100; iteration++) {
            List<Integer> controlCollection = new ArrayList<>();
            AVLTree<Integer> newTree = new AVLTree<>();
            for (int i = 0; i < 20; i++) {
                int newValue = random.nextInt();
                controlCollection.add(newValue);
                newTree.add(newValue);
            }

            assertTrue(newTree.containsAll(controlCollection));
        }
    }

    @Test
    void addAll() {
        Random random = new Random();
        for (int iteration = 0; iteration < 100; iteration++) {
            List<Integer> controlCollection = new ArrayList<>();
            AVLTree<Integer> newTree = new AVLTree<>();
            for (int i = 0; i < 20; i++) {
                int newValue = random.nextInt();
                controlCollection.add(newValue);
            }

            assertTrue(newTree.addAll(controlCollection));
            assertFalse(newTree.addAll(controlCollection));
            assertTrue(newTree.containsAll(controlCollection));
        }
    }

    @Test
    void retainAll() {
        Random random = new Random();
        for (int iteration = 0; iteration < 100; iteration++) {
            List<Integer> controlCollection = new ArrayList<>();
            Set<Integer> controlSet  = new HashSet<>();
            AVLTree<Integer> newTree = new AVLTree<>();

            for (int i = 0; i < 20; i++) {
                int newValue = random.nextInt(100);
                controlCollection.add(newValue);
                newTree.add(newValue);
            }
            for (int i = 0; i < 10; i++) {
                int newValue = random.nextInt(100);
                newTree.add(newValue);
                if (!controlCollection.contains(newValue))
                    controlSet.add(newValue);
            }
            System.out.println("Control collection:" + controlCollection);
            System.out.println("Control set:" + controlSet);

            System.out.println("Checking if it retains correctly...");
            assertTrue(newTree.retainAll(controlCollection));
            assertFalse(newTree.retainAll(controlCollection));
            assertTrue(controlCollection.containsAll(newTree));
            for (int el: controlSet) {
                assertFalse(newTree.contains(el));
            }
            System.out.println("All clear");
        }
    }

    @Test
    void removeAll() {
        Random random = new Random();
        for (int iteration = 0; iteration < 100; iteration++) {
            List<Integer> controlCollection = new ArrayList<>();
            List<Integer> removed = new ArrayList<>();
            AVLTree<Integer> newTree = new AVLTree<>();
            for (int i = 0; i < 20; i++) {
                int newValue = random.nextInt(100);
                controlCollection.add(newValue);
                newTree.add(newValue);
            }
            for (int i = 0; i < 10; i++) {
                int newValue = random.nextInt(100);
                removed.add(newValue);
                newTree.add(newValue);
            }
            System.out.println("Initial collection " + controlCollection);
            System.out.println("Collection to remove" + removed);

            assertTrue(newTree.removeAll(removed));
            assertFalse(newTree.removeAll(removed));
            assertTrue(controlCollection.containsAll(newTree));
            assertFalse(newTree.containsAll(removed));
        }
    }

    @Test
    void clear() {
        Random random = new Random();
        for (int iteration = 0; iteration < 100; iteration++) {
            AVLTree<Integer> newTree = new AVLTree<>();
            for (int i = 0; i < 20; i++) {
                int newValue = random.nextInt();
                newTree.add(newValue);
            }

            newTree.clear();
            assertTrue(newTree.isEmpty());
        }
    }

    @Test
    void testEqualsAndHashCode() {
        Random random = new Random();
        for (int iteration = 0; iteration < 100; iteration++) {
            AVLTree<Integer> newTree = new AVLTree<>();
            AVLTree<Integer> controlTree = new AVLTree<>();
            for (int i = 0; i < 20; i++) {
                int newValue = random.nextInt();
                newTree.add(newValue);
                controlTree.add(newValue);
            }

            assertEquals(controlTree, newTree);
            assertEquals(controlTree.hashCode(), newTree.hashCode());
        }
    }
}