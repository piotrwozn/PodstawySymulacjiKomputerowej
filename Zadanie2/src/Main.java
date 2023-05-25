import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        final double dt = 0.05; // krok czasowy
        final double m = 1.0; // masa punktu materialnego
        final double q = 0.5; // współczynnik oporu ośrodka
        final Vector g = new Vector(0, -9.81);

        Vector r = new Vector(0, 0); // położenie początkowe
        Vector v = new Vector(10, 20); // prędkość początkowa

        // plik wynikowy
        FileWriter writer = new FileWriter("trajectoryEuler.txt");
        writer.write("x,y\n");

        // metoda Eulera
        for (double t = 0; t < 10; t += dt) {
            Vector F = new Vector(0, 0);
            F = F.add(g.multiply(m));
            F = F.subtract(v.multiply(q));
            Vector a = F.divide(m);
            r = r.add(v.multiply(dt));
            v = v.add(a.multiply(dt));
            writer.write(r.x + "," + r.y + "\n");
        }
        writer.close();

        FileWriter writer2 = new FileWriter("trajectoryBetterEuler.txt");
        writer2.write("x,y\n");

        // ulepszona metoda Eulera
        r = new Vector(0, 0);
        v = new Vector(10, 20);
        for (double t = 0; t < 10; t += dt) {
            Vector F = new Vector(0, 0);
            F = F.add(g.multiply(m));
            F = F.subtract(v.multiply(q));
            Vector a = F.divide(m);
            r = r.add(v.multiply(dt));
            v = v.add(a.multiply(dt / 2));
            F = new Vector(0, 0);
            F = F.add(g.multiply(m));
            F = F.subtract(v.multiply(q));
            a = F.divide(m);
            v = v.add(a.multiply(dt / 2));
            writer2.write(r.x + "," + r.y + "\n");
        }
        writer2.close();
    }

    public record Vector(double x, double y) {

        public Vector add(Vector v) {
                return new Vector(x + v.x, y + v.y);
            }

            public Vector subtract(Vector v) {
                return new Vector(x - v.x, y - v.y);
            }

            public Vector multiply(double s) {
                return new Vector(x * s, y * s);
            }

            public Vector divide(double s) {
                return new Vector(x / s, y / s);
            }
        }
}
