import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;


public class StringSimulation extends JPanel {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 400;
    private static final double L = Math.PI;
    private static final int N = 100;
    private static final double DX = L / N;
    private static final double DT = 0.005;
    private static final int STEPS = 5000;
    private static final int DELAY = 20;
    private static final double C = 1.0;
    private double[] y;
    private double[] v;
    private double[] a;

    public StringSimulation() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        y = new double[N];
        v = new double[N];
        a = new double[N];
        initString();
    }

    private void initString() {
        for (int i = 0; i < N; i++) {
            double normX = (double) i / (N - 1); // Normalizacja wartości x do zakresu [0, 1]
            y[i] = -(4 * normX * normX - 4 * normX);
            v[i] = 0;
        }
        updateAcceleration();
    }

    private void updateAcceleration() {
        for (int i = 1; i < N - 1; i++) {
            a[i] = (y[i - 1] - 2 * y[i] + y[i + 1]) / (DX * DX);
        }
    }

    private void updateString() {
        double gamma = 0.1; // Współczynnik tłumienia wiskotycznego (dobrany eksperymentalnie)
        for (int i = 1; i < N - 1; i++) {
            double dampingForce = -gamma * v[i];
            v[i] += (a[i] + dampingForce) * DT;
            y[i] += v[i] * DT;
        }
        updateAcceleration();
    }



    private double calculateKineticEnergy() {
        double Ek = 0;
        for (int i = 0; i < N; i++) {
            Ek += DX * Math.pow(v[i], 2) / 2;
        }
        return Ek;
    }

    private double calculatePotentialEnergy() {
        double Ep = 0;
        for (int i = 0; i < N - 1; i++) {
            Ep += Math.pow((y[i + 1] - y[i]), 2) / (2 * DX);
        }
        return Ep;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);

        for (int i = 0; i < N - 1; i++) {
            double x1 = (double) i / N * WIDTH;
            double x2 = (double) (i + 1) / N * WIDTH;
            double y1 = HEIGHT / 2 + y[i] * HEIGHT / 2;
            double y2 = HEIGHT / 2 + y[i + 1] * HEIGHT / 2;
            g2d.draw(new Line2D.Double(x1, y1, x2, y2));
        }
    }

    public void runSimulation() {
        new Thread(() -> {
            int step = 0;
            while(true){
                updateString();
                double Ek = calculateKineticEnergy();
                double Ep = calculatePotentialEnergy();
                double Et = Ek + Ep;
                System.out.println("Step: " + step + " Ek: " + Ek + " Ep: " + Ep + " Et: " + Et);
                step++;
                repaint();
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}