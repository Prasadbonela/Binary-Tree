package com.epam.rd.autocode.collection.tree;

import java.util.Objects;
import java.util.Optional;

public class BinaryTree {

    private static final String INDENT = "-~-";
    private static final String EOL = System.lineSeparator();

    private Node root;
    private int size;

    private static final class Node {
        Integer e;
        Node left;
        Node right;

        Node(Integer e) {
            this.e = e;
        }

        public String toString() {
            return "Node[" + e + "]";
        }
    }

    public BinaryTree() {
        super();
    }

    public BinaryTree(Integer... elements) {
        Objects.requireNonNull(elements, "Elements cannot be null");
        for (Integer element : elements) {
            Objects.requireNonNull(element, "Element cannot be null");
            add(element);
        }
    }

    public boolean add(Integer element) {
        Objects.requireNonNull(element, "Element cannot be null");
        if (root == null) {
            root = new Node(element);
            size++;
            return true;
        }
        return addRecursive(root, element);
    }

    private boolean addRecursive(Node current, Integer element) {
        if (element.equals(current.e)) {
            return false;
        } else if (element < current.e) {
            if (current.left == null) {
                current.left = new Node(element);
                size++;
                return true;
            } else {
                return addRecursive(current.left, element);
            }
        } else {
            if (current.right == null) {
                current.right = new Node(element);
                size++;
                return true;
            } else {
                return addRecursive(current.right, element);
            }
        }
    }

    public void addAll(Integer... ar) {
        Objects.requireNonNull(ar, "Array cannot be null");
        for (Integer element : ar) {
            Objects.requireNonNull(element, "Element cannot be null");
            add(element);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        inOrderTraversal(root, sb);
        if (sb.length() > 1) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        return sb.toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.e).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    public Optional<Integer> remove(Integer element) {
        Objects.requireNonNull(element, "Element cannot be null");
        Optional<Integer> removedElement = removeRecursive(root, null, element);
        if (removedElement.isPresent()) {
            size--;
        }
        return removedElement;
    }

    private Optional<Integer> removeRecursive(Node node, Node parent, Integer element) {
        if (node == null) {
            return Optional.empty();
        }

        if (element.equals(node.e)) {
            Integer removedValue = node.e;
            if (node.left == null && node.right == null) {
                // Node has no children
                if (parent == null) {
                    root = null;
                } else if (parent.left == node) {
                    parent.left = null;
                } else {
                    parent.right = null;
                }
            } else if (node.left == null) {
                // Node has only right child
                if (parent == null) {
                    root = node.right;
                } else if (parent.left == node) {
                    parent.left = node.right;
                } else {
                    parent.right = node.right;
                }
            } else if (node.right == null) {
                // Node has only left child
                if (parent == null) {
                    root = node.left;
                } else if (parent.left == node) {
                    parent.left = node.left;
                } else {
                    parent.right = node.left;
                }
            } else {
                // Node has two children
                Node successorParent = node;
                Node successor = node.right;
                while (successor.left != null) {
                    successorParent = successor;
                    successor = successor.left;
                }
                node.e = successor.e;
                if (successorParent.left == successor) {
                    successorParent.left = successor.right;
                } else {
                    successorParent.right = successor.right;
                }
            }
            return Optional.of(removedValue);
        } else if (element < node.e) {
            return removeRecursive(node.left, node, element);
        } else {
            return removeRecursive(node.right, node, element);
        }
    }

    public int size() {
        return size;
    }

    String asTreeString() {
        StringBuilder sb = new StringBuilder();
        asTreeString(sb, root, 0);
        return sb.toString();
    }

    private void asTreeString(StringBuilder sb, Node node, int k) {
        if (node == null) {
            return;
        }
        asTreeString(sb, node.right, k + 1);
        sb.append(INDENT.repeat(k));
        sb.append(String.format("%3s", node.e)).append(EOL);
        asTreeString(sb, node.left, k + 1);
    }
}
