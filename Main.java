import java.util.Random;

public class Main {
    public static void main(String[] args) {
        RBTree tree = new RBTree();
        Random r = new Random();

        for (int i = 0; i < 10; i++) {
            int n = r.nextInt(1, 50);
            tree.add(n);
            System.out.println("Dodano " + n);
        }

        System.out.println("Wysokość: " + tree.height());

    }
}