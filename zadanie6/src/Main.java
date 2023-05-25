import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("String Simulation");
            StringSimulation stringSimulation = new StringSimulation();
            frame.add(stringSimulation);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            stringSimulation.runSimulation();
        });
    }
}
