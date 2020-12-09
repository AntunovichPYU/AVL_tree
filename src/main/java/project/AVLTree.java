package project;


import java.util.*;

public class AVLTree<T extends Comparable<T>> implements Set<T> {

    private static class Node<T> {
        T value;
        Node<T> left;
        Node<T> right;
        int balance;

        private Node(T value) {
            this.value = value;
            left = null;
            right = null;
            balance = 0;// значение баланса показывает, на сколько правое поддерево узла больше левого
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?> node = (Node<?>) o;
            return balance == node.balance &&
                    value.equals(node.value) &&
                    Objects.equals(left, node.left) &&
                    Objects.equals(right, node.right);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, left, right, balance);
        }
    }

    private Node<T> root = null;
    private int size = 0;

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> begin, T value) {
        int comparison = value.compareTo(begin.value);
        if (comparison == 0) {
            return begin;
        } else if (comparison > 0) {
            if (begin.right == null)
                return begin;
            return find(begin.right, value);
        } else {
            if (begin.left == null)
                return begin;
            return find(begin.left, value);
        }
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new AVLTreeIterator();
    }

    public class AVLTreeIterator implements Iterator<T> {
        Stack<Node<T>> stack = new Stack<>();
        Node<T> current;
        Node<T> prev;

        AVLTreeIterator() {
            pushToLeft(root);
        }

        private void pushToLeft(Node<T> node) {
            if (node != null) {
                stack.push(node);
                pushToLeft(node.left);
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            if (!hasNext())
                throw new NoSuchElementException();
            prev = current;
            current = stack.pop();
            pushToLeft(current.right);
            return current.value;
        }
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        Iterator<T> it = this.iterator();
        for (int i = 0; i < size; i++) {
            result[i] = it.next();
        }
        return result;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a == null) throw new NullPointerException();
        if (a.length < size) {
            return (T1[]) this.toArray();
        } else {
            Iterator<T> it = this.iterator();
            for (int i = 0; i < size; i++) {
                a[i] = (T1) it.next();
            }
        }

        return a;
    }

    public int height() {
        return height(root);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Использованные источники:
     *      https://neerc.ifmo.ru/wiki/index.php?title=%D0%90%D0%92%D0%9B-%D0%B4%D0%B5%D1%80%D0%B5%D0%B2%D0%BE#.D0.91.D0.B0.D0.BB.D0.B0.D0.BD.D1.81.D0.B8.D1.80.D0.BE.D0.B2.D0.BA.D0.B0_2
     *      https://ru.wikipedia.org/wiki/%D0%90%D0%92%D0%9B-%D0%B4%D0%B5%D1%80%D0%B5%D0%B2%D0%BE
     *
     * Добавление узла.
     *
     * Добавление происходит рекурсивно. После добавления происходит обход вверх по пути поиска и корректировка
     * значений балансов узлов. Если подъем идет из левого поддерева, то значение баланса уменьшается на 1, иначе -
     * увеличивается. Если значение баланса какого либо узла стало равно 0, корректировка заканчивается. Если баланс
     * стал равен -1 или 1, значит обход и корректировка продолжаются. При значении баланса 2 или -2 требуется
     * балансировка.
     *
     * Сложность: O(log n).
     */

    @Override
    public boolean add(T t) {
        if (contains(t))
            return false;
        endAdding = false;
        root = addRecursive(root, t);
        size++;
        return true;
    }

    private Node<T> addRecursive(Node<T> begin, T value) {
        if (begin == null)
            return new Node<>(value);

        int comparison = value.compareTo(begin.value);
        if (comparison < 0) {
            begin.left = addRecursive(begin.left, value);
            if (!endAdding) {
                begin.balance--;
                if (Math.abs(begin.balance) == 2)
                    begin = balance(begin);
                if (begin.balance == 0)
                    endAdding = true;
            }
        } else if (comparison > 0) {
            begin.right = addRecursive(begin.right, value);
            if (!endAdding) {
                begin.balance++;
                if (Math.abs(begin.balance) == 2)
                    begin = balance(begin);
                if (begin.balance == 0)
                    endAdding = true;
            }
        }

        return begin;
    }

    private boolean endAdding;

    /**
     * Удаление узла.
     *
     * Удаление также происходит рекурсивно. Если данное значение является листом, мы просто удаляем его.
     * Если у узла только одно поддерево, мы просто меняем узел на его поддерево. Иначе нужно найти наименьший узел
     * в правом поддереве и заменить удаляемый узел на него, уменьшив значение баланса на 1, а сам узел удалить.
     * Далее идет обход вверх по дереву и корректировка балансов. Если поднимаемся из левого поддерева, значение баланса
     * увеличивается на 1, иначе - уменьшается. Если в какой то вершине значение баланса стало -1 или 1 - корректировка
     * останавливается. При значении баланса -2 или 2 происходит балансировка.
     *
     */

    @Override
    public boolean remove(Object o) {
        if (!contains(o)) {
            return false;
        }
        @SuppressWarnings("unchecked")
        T t = (T) o;
        endRemoving = false;
        root = removeRecursive(root, t);
        size--;
        return true;
    }

    private Node<T> removeRecursive(Node<T> begin, T value) {
        if (begin == null)
            return null;

        int comparison = value.compareTo(begin.value);
        if (comparison < 0) {
            begin.left = removeRecursive(begin.left, value);
            if (!endRemoving) {
                begin.balance++;
                begin = balance(begin);
                if (begin.balance != 0)
                    endRemoving = true;
            }
        } else if (comparison > 0) {
            begin.right = removeRecursive(begin.right, value);
            if (!endRemoving) {
                begin.balance--;
                begin = balance(begin);
                if (begin.balance != 0)
                    endRemoving = true;
            }
        } else {
            if (begin.left == null && begin.right == null) {
                return null;
            }
            if (begin.left == null) {
                return begin.right;
            }
            if (begin.right == null) {
                return begin.left;
            }

            Node<T> t = begin;
            begin = findMin(begin.right);
            begin.right = removeRecursive(t.right, begin.value);
            begin.left = t.left;
            begin.balance = t.balance - 1;
        }

        return begin;
    }

    private boolean endRemoving;

    private Node<T> findMin(Node<T> node) {
        return node.left == null ? node : findMin(node.left);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object el: c) {
            if (!this.contains(el)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (this.containsAll(c))
            return false;
        for (T el: c) {
            this.add(el);
        }

        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.containsAll(this))
            return false;
        List<T> control = new ArrayList<>(this);
        for (T el: control) {
            if (!c.contains(el))
                this.remove(el);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (!this.containsAll(c))
            return false;
        for (Object el: c) {
            this.remove(el);
        }

        return true;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Балансировка происходит, когда правое или левое поддерево перевешивает более, чем на 1.
     *
     */

    private Node<T> balance(Node<T> node) {
        if (node.balance == -2) {
            if (node.left.balance <= 0)
                node = smallRightTurn(node);
            else
                node = bigRightTurn(node);
        } else if (node.balance == 2) {
            if (node.right.balance >= 0) {
                node = smallLeftTurn(node);
            } else {
                node = bigLeftTurn(node);
            }
        }

        return node;
    }

    /**
     * Повороты, совершаемые при балансировке.
     *
     */

    private Node<T> smallLeftTurn(Node<T> begin) {
        Node<T> node = begin;
        begin = begin.right;
        node.right = begin.left;
        begin.left = node;

        if (begin.balance == 1) {
            begin.balance = 0;
            begin.left.balance = 0;
        } else {
            begin.balance = -1;
            begin.left.balance = 1;
        }
        return begin;
    }

    private Node<T> smallRightTurn(Node<T> begin) {
        Node<T> node = begin;
        begin = begin.left;
        node.left = begin.right;
        begin.right = node;

        if (begin.balance == -1) {
            begin.balance = 0;
            begin.right.balance = 0;
        } else {
            begin.balance = 1;
            begin.right.balance = -1;
        }
        return begin;
    }

    private Node<T> bigLeftTurn(Node<T> begin) {
        Node<T> left = begin;
        Node<T> right = begin.right;
        begin = begin.right.left;
        left.right = begin.left;
        right.left = begin.right;
        begin.left = left;
        begin.right = right;

        if (begin.balance == 0) {
            begin.right.balance = 0;
            begin.left.balance = 0;
        } else if (begin.balance == -1) {
            begin.balance = 0;
            begin.right.balance = 1;
            begin.left.balance = 0;
        } else {
            begin.balance = 0;
            begin.right.balance = 0;
            begin.left.balance = -1;
        }
        return begin;
    }

    private Node<T> bigRightTurn(Node<T> begin) {
        Node<T> right = begin;
        Node<T> left = begin.left;
        begin = begin.left.right;
        right.left = begin.right;
        left.right = begin.left;
        begin.left = left;
        begin.right = right;

        if (begin.balance == 0) {
            begin.right.balance = 0;
            begin.left.balance = 0;
        } else if (begin.balance == 1) {
            begin.balance = 0;
            begin.right.balance = 0;
            begin.left.balance = -1;
        } else {
            begin.balance = 0;
            begin.right.balance = 1;
            begin.left.balance = 0;
        }
        return begin;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        Iterator<T> it = this.iterator();
        while (it.hasNext()) {
            result.append(it.next());
            if (it.hasNext()) result.append(", ");
        }
        result.append("]");
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AVLTree<?> avlTree = (AVLTree<?>) o;
        return size == avlTree.size &&
                Objects.equals(root, avlTree.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root, size);
    }
}
