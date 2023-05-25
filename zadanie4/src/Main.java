import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        double h = 20; // wysokość równi nachylonej [m]
        double alpha = Math.toRadians(45); // kąt nachylenia równi [rad]
        double r = 2; // promień kuli/sfery [m]
        double m = 1; // masa kuli/sfery [kg]
        double g = 10; // przyspieszenie ziemskie [m/s^2]
        double dt = 0.05; // krok czasowy [s]

        double s_kula = 0; // początkowa pozycja kuli [m]
        double s_sfera = 0; // początkowa pozycja sfery [m]
        double v_kula = 0; // początkowa prędkość kuli [m/s]
        double v_sfera = 0; // początkowa prędkość sfery [m/s]
        double omega_kula = 0; // początkowa prędkość kątowa kuli [rad/s]
        double omega_sfera = 0; // początkowa prędkość kątowa sfery [rad/s]
        double beta_kula = 0; // początkowy kąt obrotu kuli [rad]
        double beta_sfera = 0; // początkowy kąt obrotu sfery [rad]

        double I_kula = 2.5 * m * r * r; // moment bezwładności kuli [kg*m^2]
        double I_sfera = 2.0 / 3.0 * m * r * r; // moment bezwładności sfery [kg*m^2]

        try {
            FileWriter pozycjaKulaWriter = new FileWriter("pozycja_kula.txt");
            FileWriter katKulaWriter = new FileWriter("kat_kula.txt");
            FileWriter pozycjaSferaWriter = new FileWriter("pozycja_sfera.txt");
            FileWriter katSferaWriter = new FileWriter("kat_sfera.txt");
            pozycjaKulaWriter.write("t" + "\t" + "s" + "\n");
            katKulaWriter.write("t" + "\t" + "kat" + "\n");
            pozycjaSferaWriter.write("t" + "\t" + "s" + "\n");
            katSferaWriter.write("t" + "\t" + "kat" + "\n");

            for (double t = 0; t < 10; t += dt) {
                double a_kula = g * Math.sin(alpha); // przyspieszenie kuli [m/s^2]
                double a_sfera = g * Math.sin(alpha); // przyspieszenie sfery [m/s^2]

                // Obliczenie prędkości i położenia w czasie t+dt/2
                double v_kula_half = v_kula + a_kula * dt / 2.0;
                double v_sfera_half = v_sfera + a_sfera * dt / 2.0;
                double beta_kula_half = beta_kula + omega_kula * dt / 2.0;
                double beta_sfera_half = beta_sfera + omega_sfera * dt / 2.0;
                double omega_kula_half = omega_kula + (1 / I_kula) * (h * Math.sin(alpha) * Math.cos(beta_kula_half) * dt / 2.0 - 0.5 * Math.sin(2 * alpha) * g * r * Math.sin(beta_kula_half) * dt / 2.0);
                double omega_sfera_half = omega_sfera + (1 / I_sfera) * (h * Math.sin(alpha) * Math.cos(beta_sfera_half) * dt / 2.0 - 0.5 * Math.sin(2 * alpha) * g * r * Math.sin(beta_sfera_half) * dt / 2.0);
                // Obliczenie prędkości i położenia w czasie t+dt
                v_kula += a_kula * dt;
                v_sfera += a_sfera * dt;
                beta_kula += omega_kula_half * dt;
                beta_sfera += omega_sfera_half * dt;
                omega_kula += (1 / I_kula) * (h * Math.sin(alpha) * Math.cos(beta_kula_half) * dt - 0.5 * Math.sin(2 * alpha) * g * r * Math.sin(beta_kula_half) * dt);
                omega_sfera += (1 / I_sfera) * (h * Math.sin(alpha) * Math.cos(beta_sfera_half) * dt - 0.5 * Math.sin(2 * alpha) * g * r * Math.sin(beta_sfera_half) * dt);

                s_kula += v_kula * dt;
                s_sfera += v_sfera * dt;

                // Zapisanie wyników do plików
                pozycjaKulaWriter.write(t + "\t" + s_kula + "\n");
                katKulaWriter.write(t + "\t" + beta_kula + "\n");
                pozycjaSferaWriter.write(t + "\t" + s_sfera + "\n");
                katSferaWriter.write(t + "\t" + beta_sfera + "\n");
            }

            pozycjaKulaWriter.close();
            katKulaWriter.close();
            pozycjaSferaWriter.close();
            katSferaWriter.close();

        } catch (IOException e) {
            System.out.println("Błąd zapisu do pliku: " + e.getMessage());
        }
    }
}
