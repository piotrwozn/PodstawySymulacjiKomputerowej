import java.util.Scanner;
import java.util.stream.LongStream;

public class Main {
    public static void main(String[] args) {
        menu();
    }

    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        boolean gate = true;
        while (gate) {
            System.out.println("W czym chcesz podać kąt");
            System.out.println("1 -> radiany ( w zapisie pomiń * PI )");
            System.out.println("2 -> stopnie ( napisz stopnie bez znaku )");
            System.out.println("3 -> wyjdz z programu");
            String ans = scanner.next();
            int answer = checkAnswer(ans);
            if (answer == 1) {
                System.out.println("Podaj kąt w radianach ( w zapisie pomiń * PI na końcu ) ");
                double rad = scanner.nextDouble();
                rad = goBetween(rad);
                checkRad(rad);
            }
            if (answer == 2) {
                System.out.println("Podaj kat w stopniach ( pomin znak )");
                double stop = scanner.nextDouble();
                double rad = Math.toRadians(stop) / Math.PI;
                rad = goBetween(rad);
                checkRad(rad);
            }
            if (answer == 3) {
                gate = false;
            }
            if (answer == 0) {
                System.out.println("Podałeś złą opcje");
                System.out.println("Prosze podaj jeszcze raz");
                System.out.println();
            }
        }
    }

    private static double goBetween(double rad) {
        if (rad > 2) {
            while (rad > 2) {
                rad -= 2;
            }
        } else if (rad < 0) {
            while (rad < 0) {
                rad += 2;
            }
        }
        return rad;
    }

    private static void checkRad(double rad) {
        if (0 <= rad && rad <= 0.5) {
            taylorSeries(rad * Math.PI);
        }
        if (0.5 < rad && rad <= 1) {
            taylorSeries(Math.PI - rad * Math.PI);
        }
        if (1 < rad && rad <= 1.5) {
            taylorSeries(rad * Math.PI - Math.PI);
        }
        if (1.5 < rad && rad <= 2) {
            taylorSeries(2 * Math.PI - rad * Math.PI);
        }
    }

    public static int checkAnswer(String ans) {
        char[] tab = ans.toCharArray();
        int tmp = 0;
        for (char c : tab) {
            if (c != 32) {
                if (c == 49) {
                    tmp = 1;
                }
                if (c == 50) {
                    tmp = 2;
                }
                if (c == 51) {
                    tmp = 3;
                }
            }
        }
        return tmp;
    }

    public static void taylorSeries(double radians) {
        double sin = Math.sin(radians);
        int tmp = 1;
        double taylor = 0;

        System.out.println("Wyraz szeregu || Biblioteka || Taylor || Róźnica");
        for (int i = 1; i < 20; i += 2) {
            if (i == 1) {
                taylor = radians;
            } else if (i == 3 || i == 7 || i == 11 || i == 15 || i == 19) {
                taylor -= Math.pow(radians, i) / factorial(i);
            } else {
                taylor += Math.pow(radians, i) / factorial(i);
            }
            double diffrence = Math.abs(sin - taylor);
            System.out.print(tmp + " || " + sin + " || " + taylor + " || " + diffrence);
            System.out.println();
            tmp++;
        }
    }

    public static long factorial(int n) {
        return LongStream.rangeClosed(1, n).reduce(1, (long x, long y) -> x * y);
    }
}