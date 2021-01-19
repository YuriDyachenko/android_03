package kurs;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("----------------- 1 -----------------");
        Task1(scanner);

        System.out.println("----------------- 2 -----------------");
        Task2(scanner);

        scanner.close();
    }

    public static void Task1(Scanner scanner) {

        do {
            Random random = new Random();
            int secret = random.nextInt(10);
            int entered = -1;

            System.out.println();
            System.out.println("Программа \"задумала\" случайное число, у Вас есть ТРИ попытки, чтобы его угадать!");

            for (int attempt = 1; attempt <= 3; attempt++) {
                entered = readIntWithAttempt(attempt, scanner);
                if (entered == secret) break;

                String res = "больше";
                if (entered < secret) res = "меньше";

                System.out.printf("введенное число %d %s задуманного", entered, res);
                System.out.println();
            }

            if (entered == secret) System.out.println("ВЫ УГАДАЛИ ЧИСЛО!");
            else System.out.println("ВЫ ПРОИГРАЛИ!");
            System.out.println();

            System.out.print("Повторить игру еще раз? 1 - да / 0 - нет: ");

        } while (readIntWithCheckBounds(scanner, 0, 1) == 1);

    }

    //выводит номер попытки, предлагает ввести число
    public static int readIntWithAttempt(int attempt, Scanner scanner) {
        System.out.printf("попытка %d из 3, введите число от %d до %d: ", attempt, 0, 9);
        return readIntWithCheckBounds(scanner, 0, 9);
    }

    //вводит число с проверкой диаппазона
    public static int readIntWithCheckBounds(Scanner scanner, int start, int finish) {
        int i;
        while (true) {
            i = scanner.nextInt();
            if (i >= start && i <= finish) break;
            System.out.printf("неправильное число %d, попробуйте еще: ", i);
        }
        return i;
    }

    public static void Task2(Scanner scanner) {

        String[] words = {"apple", "orange", "lemon", "banana", "apricot", "avocado", "broccoli", "carrot",
                "cherry", "garlic", "grape", "melon", "leak", "kiwi", "mango", "mushroom", "nut", "olive", "pea",
                "peanut", "pear", "pepper", "pineapple", "pumpkin", "potato"};
        Random random = new Random();
        int secretIndex = random.nextInt(words.length);
        String secretWord = words[secretIndex];

        System.out.println();
        System.out.println("Программа \"задумала\" одно слово из:");
        for (int i = 0; i < words.length; i++) {
            System.out.printf("  %s", words[i]);
            if ((i + 1) % 6 == 0) System.out.println();
        }
        System.out.println();

        while (true) {
            System.out.print("Введите слово: ");
            String answer = scanner.next();
            String correct = correctChars(secretWord, answer);

            if (correct.length() == secretWord.length() && correct.length() == answer.length()) {
                System.out.println("ВЫ УГАДАЛИ СЛОВО!");
                break;
            }

            if (correct.length() == 0) System.out.printf("%s - не совпало ни одной начальной буквы!", answer);
            else System.out.printf("%s - совпало %s###############!", answer, correct);
            System.out.println(" Попробуйте еще раз...");
        }
    }

    //ищет и возвращает первые совпадающие символы, до первого несовпадения
    public static String correctChars(String secret, String answer) {
        String res = "";
        int len = minInt(secret.length(), answer.length());
        for (int i = 0; i < len; i++) {
            char c = secret.charAt(i);
            if (c == answer.charAt(i)) res += c;
        }
        return res;
    }

    //возвращает минимальное из двух целых
    public static int minInt(int a, int b) {
        if (a < b) return a;
        else return b;
    }

}
