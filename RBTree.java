public class RBTree {

    private Node root;

    private void rotateRight(Node node) {
        Node parent = node.parent;
        Node leftChild = node.left;

        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.right = node;
        node.parent = leftChild;

        if (parent == null) {
            root = leftChild;
        } else if (parent.left == node) {
            parent.left = leftChild;
        } else if (parent.right == node) {
            parent.right = leftChild;
        }

        leftChild.parent = parent;
    }

    private void rotateLeft(Node node) {
        Node parent = node.parent;
        Node rightChild = node.right;

        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        rightChild.left = node;
        node.parent = rightChild;

        if (parent == null) {
            root = rightChild;
        } else if (parent.left == node) {
            parent.left = rightChild;
        } else if (parent.right == node) {
            parent.right = rightChild;
        }

        rightChild.parent = parent;
    }

    public void add(int key) {
        add(key, 0);
    }

    public void add(int key, int value) {
        Node newNode = new Node(key, value);
        newNode.black = false;

        Node node = root;
        Node parent = null;

        while (node != null) {
            if (key == node.key) {
                System.out.println("Obiekt o danym kluczu już istnieje");
            }
            parent = node;
            node = key < node.key ? node.left : node.right;
        }

        newNode.parent = parent;
        if (parent == null) {
            root = newNode;
        } else if (key < parent.key) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        addFix(newNode);
    }

    private void addFix(Node node) {
        Node parent = node.parent;

        if (node == root) {
            node.black = true;
            return;
        }
        if (parent.black == true) return;

        Node grandparent = parent.parent;
        Node uncle;

        if (grandparent.left == parent) {
            uncle = grandparent.right;
        }
        else if (grandparent.right == parent) {
            uncle = grandparent.left;
        }

        if (uncle != null && uncle.black == false) {
            parent.black = true;
            uncle.black = true;
            grandparent.black = false;
            addFix(grandparent);
            return;
        }

        if (parent == grandparent.left) {
            if (node == parent.right) {
                rotateLeft(parent);
                node = parent;
                parent = node.parent;
            }

            parent.black = true;
            grandparent.black = false;
            rotateRight(grandparent);
        } else {
            if (node == parent.left) {
                rotateRight(parent);
                node = parent;
                parent = node.parent;
            }

            parent.black = true;
            grandparent.black = false;
            rotateLeft(grandparent);
        }
    }

    private void remove(Node node) {
        Node child = null;
        Node removedNode = node;

        if (node.left == null ^ node.right == null) {
            child = node.left == null ? node.right : node.left;
        } else if (node.right != null) {
            Node inOrderSuccessor = node.right;
            while (inOrderSuccessor.left != null) {
                inOrderSuccessor = inOrderSuccessor.left;
            }
            remove(inOrderSuccessor);

            node.key = inOrderSuccessor.key;
            node.value = inOrderSuccessor.value;

            removedNode = inOrderSuccessor;
        }

        removeFix(removedNode);

        if (removedNode == node)  {
            if (node.parent == null) {
                root = child;
            }
            else if (node.parent.left == node) {
                node.parent.left = child;
            }
            else if (node.parent.right == node) {
                node.parent.right = child;
            }
            if (child != null) child.parent = node.parent;
        }
    }

    public int remove(int key) {
        Node node = getNode(key);
        remove(node);
        return node.value;
    }

    private void removeFix(Node node) {
        if (node.black == false) return;
        if (node == root) return;

        Node parent = node.parent;
        if (node == parent.right && parent.left != null) {
            if (parent.left.black == false) {
                parent.left.black = true;
                parent.black = false;

                rotateRight(parent);
            }
            if ((parent.left.left == null || parent.left.left.black == true) &&
                    (parent.left.right == null || parent.left.right.black == true)) {
                parent.left.black = false;

                if (parent.black == false) {
                    parent.black = true;
                } else {
                    removeFix(parent);
                }
            } else {
                if (parent.left.left == null || parent.left.left.black == true) {
                    parent.left.right.black = true;
                    parent.left.black = false;
                    rotateLeft(parent.left);
                }
                parent.left.black = parent.black;
                parent.black = true;
                parent.left.left.black = true;
                rotateRight(parent);
            }

        } else if (node == parent.left && parent.right != null) {
            if (parent.right.black == false) {
                parent.right.black = true;
                parent.black = false;

                rotateLeft(parent);
            }
            if ((parent.right.left == null || parent.right.left.black == true) &&
                    (parent.right.right == null || parent.right.right.black == true)) {
                parent.right.black = false;

                if (parent.black == false) {
                    parent.black = true;
                } else {
                    removeFix(parent);
                }
            } else {
                if (parent.right.right == null || parent.right.right.black == true) {
                    parent.right.left.black = true;
                    parent.right.black = false;
                    rotateRight(parent.right);
                }
                parent.right.black = parent.black;
                parent.black = true;
                parent.right.right.black = true;
                rotateLeft(parent);
            }
        }
    }

    private Node getNode(int key) {
        Node node = root;
        while (node != null) {
            if (key == node.key) {
                return node;
            }
            node = key < node.key ? node.left : node.right;
        }

        System.out.println("Nie ma takiego node'a w drzewie");
        return null;
    }

    public int get(int key) {
        return getNode(key).value;
    }

    public int height() {
        return height(root);
    }

    private int height(Node node) {
        if (node == null) return 0;
        return (Math.max(height(node.left), height(node.right))) + 1;
    }

}
