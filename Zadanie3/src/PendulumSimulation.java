import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class PendulumSimulation extends JPanel {
    private double G = 9.81; // acceleration due to gravity
    private double L = 1.0; // length of the pendulum
    private double M = 1.0; // mass of the pendulum bob
    private double R = 0.1; // radius of the pendulum bob
    private double DT = 0.01; // time step

    private double theta = Math.PI / 4.0; // initial angle
    private double omega = 0.0; // initial angular velocity

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        JFrame frame = new JFrame("Pendulum Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PendulumSimulation pendulum = new PendulumSimulation();
        frame.add(pendulum);
        frame.setSize(400, 400);
        frame.setVisible(true);
        System.out.println("Jakim sposobem liczyć ? ");
        System.out.println("1->Euler");
        System.out.println("2->ImprovedEuler");
        System.out.println("3->RK4");
        int ans = scanner.nextInt();
        FileWriter writer = null;
        if(ans == 1) {
            writer = new FileWriter("trajectoryEuler.txt");
        }
        if(ans == 2) {
            writer = new FileWriter("trajectoryBetterEuler.txt");
        }
        if(ans == 3) {
            writer = new FileWriter("trajectoryRK4.txt");
        }
        writer.write("theta,omega\n");
        for (int i = 0; i < 1000; i++) {
            if(ans == 1) {
                pendulum.theta = pendulum.calculateThetaEuler(pendulum.theta, pendulum.omega);
                pendulum.omega = pendulum.calculateOmegaEuler(pendulum.theta, pendulum.omega);
            }
            if(ans == 2) {
                pendulum.theta = pendulum.calculateThetaImprovedEuler(pendulum.theta, pendulum.omega);
                pendulum.omega = pendulum.calculateOmegaImprovedEuler(pendulum.theta, pendulum.omega);

            }
            if(ans == 3) {
                pendulum.theta = pendulum.calculateThetaRK4(pendulum.theta, pendulum.omega);
                pendulum.omega = pendulum.calculateOmegaRK4(pendulum.theta, pendulum.omega);
            }
            writer.write(pendulum.theta + "," + pendulum.omega + "\n");
            pendulum.repaint();
            try {
                Thread.sleep((int) (pendulum.DT * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        writer.close();
    }


    private double calculateThetaEuler(double theta, double omega) {
        return theta + omega * DT;
    }

    private double calculateOmegaEuler(double theta, double omega) {
        return omega - (M*G / L) * Math.sin(theta) * DT;
    }

    private double calculateThetaImprovedEuler(double theta, double omega) {
        double k1 = omega;
        double k2 = omega - (M*G / L) * Math.sin(theta) * DT;
        return theta + 0.5 * (k1 + k2) * DT;
    }

    private double calculateOmegaImprovedEuler(double theta, double omega) {
        double k1 = -(M*G / L) * Math.sin(theta) * DT;
        double k2 = -(M*G / L) * Math.sin(theta + k1) * DT;
        return omega + 0.5 * (k1 + k2);
    }

    private double calculateThetaRK4(double theta, double omega) {
        double k1 = omega;
        double l1 = -(M*G / L) * Math.sin(theta);
        double k2 = omega + 0.5 * l1 * DT;
        double l2 = -(M*G / L) * Math.sin(theta + 0.5 * k1 * DT);
        double k3 = omega + 0.5 * l2 * DT;
        double l3 = -(M*G / L) * Math.sin(theta + 0.5 * k2 * DT);
        double k4 = omega + l3 * DT;
        double l4 = -(M*G / L) * Math.sin(theta + k3 * DT);
        return theta + (k1 + 2 * k2 + 2 * k3 + k4) * DT / 6.0;
    }

    private double calculateOmegaRK4(double theta, double omega) {
        double k1 = -(M*G / L) * Math.sin(theta);
        double l1 = omega;
        double k2 = -(M*G / L) * Math.sin(theta + 0.5 * k1 * DT);
        double l2 = omega + 0.5 * l1 * DT;
        double k3 = -(M*G / L) * Math.sin(theta + 0.5 * k2 * DT);
        double l3 = omega + 0.5 * l2 * DT;
        double k4 = -(M*G / L) * Math.sin(theta + k3 * DT);
        double l4 = omega + l3 * DT;
        return omega + (k1 + 2 * k2 + 2 * k3 + k4) * DT / 6.0;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();
        int cx = width / 2;
        int cy = 100;
        int x = (int) (cx + L * Math.sin(theta) * width / 2.0);
        int y = (int) (cy + L * Math.cos(theta) * height / 2.0);
        g2d.setColor(Color.BLACK);
        g2d.drawLine(cx, cy, x, y);
        g2d.setColor(Color.RED);
        g2d.fillOval(x - (int) (R * width), y - (int) (R * height), (int) (2 * R * width), (int) (2 * R * height));
    }
}

