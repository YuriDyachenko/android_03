package kurs;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        //для отладки будем показывать задуманное число/слово
        //для "продакшн" можно выключить и не будет выводиться
        boolean showSecret = true;

        //столкнулся с ситуацией: если сканер создавать и закрывать в каждом task
        //то в следующем task при вызове next() выскакивает ошибка
        //в инете пишут - это потому, что закрывается System.in
        //и советуют один раз всего создавать scanner и закрывать в самом конце
        //поэтому в каждый task его передаю
        Scanner scanner = new Scanner(System.in);

        boolean exit = false;
        do {
            System.out.println();
            System.out.println("Доступны следующие задачи:");
            System.out.println("1. Угадай число (task1)");
            System.out.println("2. Угадай слово (совпадение только по начальным символам) (task2)");
            System.out.println("3. Угадай слово (совпадение по всем символам) (task2A)");
            System.out.print("Введите номер задачи (0 - для выхода): ");
            switch (readIntWithCheckBounds(scanner, 0, 3)) {
                case 1: {
                    //1. угадайка с трех попыток
                    //на самом деле три попытки недостаточно для гарантированного угадывания
                    task1(scanner, showSecret);
                    break;
                }
                case 2: {
                    //2. вариант реализации, когда совпадения ищутся только в начале
                    //задуманного слова, до первого несовпадения
                    task2(scanner, showSecret);
                    break;
                }
                case 3: {
                    //2. вариант реализации, когда совпадения ищутся на своих местах
                    //по всему задуманному слову, а не только в его начале
                    task2A(scanner, showSecret);
                    break;
                }
                case 0: {
                    exit = true;
                    break;
                }
            }
        } while (!exit);

        scanner.close();
    }

    public static void task1(Scanner scanner, boolean showSecret) {
        System.out.println("----------------- 1 -----------------");

        Random random = new Random();
        do {
            int secret = random.nextInt(10);
            int entered = -1;

            System.out.printf("\nПрограмма \"задумала\" случайное число%s, у Вас есть ТРИ попытки, чтобы его угадать!\n",
                    (showSecret) ? " (" + secret + ")" : "");

            for (int attempt = 1; attempt <= 3; attempt++) {
                entered = readIntWithAttempt(attempt, scanner);
                if (entered == secret) break;
                System.out.printf("введенное число %d %s задуманного\n", entered, (entered < secret) ? "меньше" : "больше");
            }

            if (entered == secret) System.out.println("ВЫ УГАДАЛИ ЧИСЛО!");
            else System.out.printf("ВЫ ПРОИГРАЛИ! Было задумано число: %d...\n", secret);

            System.out.print("\nПовторить игру еще раз? 1 - да / 0 - нет: ");

        } while (readIntWithCheckBounds(scanner, 0, 1) == 1);

    }

    //для task1
    //выводит номер попытки, предлагает ввести число
    public static int readIntWithAttempt(int attempt, Scanner scanner) {
        System.out.printf("попытка %d из 3, введите число от %d до %d: ", attempt, 0, 9);
        return readIntWithCheckBounds(scanner, 0, 9);
    }

    //для task1
    //вводит число с проверкой диаппазона
    //используется и для ввода угадываемого числа, и для ввода ответа 0/1
    //и выбора номера задачи
    public static int readIntWithCheckBounds(Scanner scanner, int start, int finish) {
        int i;
        while (true) {
            i = scanner.nextInt();
            if (i >= start && i <= finish) break;
            System.out.printf("неправильное число %d, попробуйте еще: ", i);
        }
        return i;
    }

    public static void task2(Scanner scanner, boolean showSecret) {
        System.out.println("----------------- 2 -----------------");

        String[] words = createWords();
        String secretWord = randomWord(words);

        System.out.printf("\nПрограмма \"задумала\" одно слово%s из:\n", (showSecret) ? " (" + secretWord + ")" : "");
        outWords(words, 8);

        while (true) {
            System.out.print("Введите слово: ");
            String answer = scanner.next();
            String correct = correctChars(secretWord, answer);

            if (correct.length() == secretWord.length() && correct.length() == answer.length()) {
                System.out.println("ВЫ УГАДАЛИ СЛОВО!");
                break;
            }

            if (correct.length() == 0)
                System.out.printf("%s - не совпало ни одной начальной буквы!", answer);
            else
                System.out.printf("%s - совпало %s!", answer, padRight(correct, '#', 15));
            System.out.println(" Попробуйте еще раз...");
        }
    }

    //для task2 и task2A
    //создание массива слов
    public static String[] createWords() {
        return new String[] {"apple", "orange", "lemon", "banana", "apricot", "avocado", "broccoli", "carrot",
                "cherry", "garlic", "grape", "melon", "leak", "kiwi", "mango", "mushroom", "nut", "olive", "pea",
                "peanut", "pear", "pepper", "pineapple", "pumpkin", "potato"};
    }

    //для task2 и task2A
    //выбор случайного слова из массива
    public static String randomWord(String[] words) {
        Random random = new Random();
        return words[random.nextInt(words.length)];
    }

    //для task2 и task2A
    //вывод массива слов по wordsPerLine в одной строке
    public static void outWords(String[] words, int wordsPerLine) {
        if (wordsPerLine <= 0) wordsPerLine = 6;
        for (int i = 0; i < words.length; i++) {
            System.out.printf("  %s", words[i]);
            if ((i + 1) % wordsPerLine == 0) System.out.println();
        }
        System.out.println();
    }

    //для task2 и task2A
    //дополняет, если "надо", строку символами до нужной длины
    public static String padRight(String s, char c, int len) {
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < len - s.length(); i++) sb.append(c);
        return sb.toString();
    }

    //для task2
    //ищет и возвращает первые совпадающие символы, до первого несовпадения
    public static String correctChars(String secret, String answer) {
        StringBuilder sb = new StringBuilder();
        int len = minInt(secret.length(), answer.length());
        for (int i = 0; i < len; i++) {
            char c = secret.charAt(i);
            if (c != answer.charAt(i)) break;
            sb.append(c);
        }
        return sb.toString();
    }

    //для task2
    //возвращает минимальное из двух целых
    public static int minInt(int a, int b) {
        return (a < b) ? a : b;
    }

    //вариант реализации второй задачи, когда совпадения ищутся на своих местах
    //по всему задуманному слову, не только в его начале до первого несовпадение
    public static void task2A(Scanner scanner, boolean showSecret) {
        System.out.println("----------------- 2a -----------------");

        String[] words = createWords();
        String secretWord = randomWord(words);

        System.out.printf("\nПрограмма \"задумала\" одно слово%s из:\n", (showSecret) ? " (" + secretWord + ")" : "");
        outWords(words, 8);

        while (true) {
            System.out.print("Введите слово: ");
            String answer = scanner.next();

            if (secretWord.equals(answer)) {
                System.out.println("ВЫ УГАДАЛИ СЛОВО!");
                break;
            }

            String matches = findMatches(secretWord, answer);
            if (matches.length() != 0)
                System.out.printf("%s - совпало %s!", answer, matches);
            else
                System.out.printf("%s - не совпало ни одной буквы!", answer);

            System.out.println(" Попробуйте еще раз...");
        }
    }

    //для task2A
    //ищет и возвращает строку совпадений
    //если не было ни одного, то возвращает пусту, иначе не понять, были совпадения или нет
    public static String findMatches(String secret, String answer) {
        boolean hasMatches = false;
        StringBuilder matches = new StringBuilder();
        for (int i = 0; i < secret.length(); i++) {
            char c = secret.charAt(i);
            if (i >= answer.length() || c != answer.charAt(i)) {
                matches.append('#');
            } else {
                matches.append(c);
                hasMatches = true;
            }
        }
        if (!hasMatches) return "";
        return padRight(matches.toString(), '#', 15);
    }

}
