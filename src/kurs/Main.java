package kurs;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        //для отладки будем показывать задуманное число/слово
        //для "продакшн" можно выключить и не будет выводиться
        boolean showSecret = false;

        //столкнулся с ситуацией: если сканер создавать и закрывать в каждом task
        //то в следующем task при вызове next() выскакивает ошибка
        //в инете пишут - это потому, что закрывается System.in
        //и советуют один раз всего создавать scanner и закрывать в самом конце
        //поэтому в каждый task его передаю
        Scanner scanner = new Scanner(System.in);

        //1. угадайка с трех попыток
        //на самом деле три попытки недостаточно для гарантированного угадывания
        task1(scanner, showSecret);

        //2. вариант реализации, когда совпадения ищутся только в начале
        //задуммного слова, до первого несовпадения
        task2(scanner, showSecret);

        //2. вариант реализации, когда совпадения ищутся на своих местах
        //по всему задуманному слову, а не только в его начале
        task2A(scanner, showSecret);

        scanner.close();
    }

    public static void task1(Scanner scanner, boolean showSecret) {
        System.out.println("----------------- 1 -----------------");

        do {
            Random random = new Random();
            int secret = random.nextInt(10);
            int entered = -1;

            String debugSecret = "";
            if (showSecret) debugSecret = " (" + secret + ")";

            System.out.println();
            System.out.printf("Программа \"задумала\" случайное число%s, у Вас есть ТРИ попытки, чтобы его угадать!\n", debugSecret);

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

    //для task1
    //выводит номер попытки, предлагает ввести число
    public static int readIntWithAttempt(int attempt, Scanner scanner) {
        System.out.printf("попытка %d из 3, введите число от %d до %d: ", attempt, 0, 9);
        return readIntWithCheckBounds(scanner, 0, 9);
    }

    //для task1
    //вводит число с проверкой диаппазона
    //используется и для ввода угадываемого числа, и для ввода ответа 0/1
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

        String debugSecret = "";
        if (showSecret) debugSecret = " (" + secretWord + ")";

        System.out.println();
        System.out.printf("Программа \"задумала\" одно слово%s из:\n", debugSecret);
        outWords(words, 8);

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
        int secretIndex = random.nextInt(words.length);
        return words[secretIndex];
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

    //для task2
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

    //для task2
    //возвращает минимальное из двух целых
    public static int minInt(int a, int b) {
        if (a < b) return a;
        else return b;
    }

    //вариант реализации второй задачи, когда совпадения ищутся на своих местах
    //по всему задуманному слову, не только в его начале до первого несовпадение
    public static void task2A(Scanner scanner, boolean showSecret) {
        System.out.println("----------------- 2a -----------------");

        String[] words = createWords();
        String secretWord = randomWord(words);

        String debugSecret = "";
        if (showSecret) debugSecret = " (" + secretWord + ")";

        System.out.println();
        System.out.printf("Программа \"задумала\" одно слово%s из:\n", debugSecret);
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
    //дополняет, если "надо", строку символами до нужной длины
    public static String padRight(String s, char c, int len) {
        int howMach = len - s.length();
        for (int i = 0; i < howMach; i++) s += c;
        return s;
    }

    //для task2A
    //ищет и возвращает строку совпадений
    //если не было ни одного, то возвращает пусту, иначе не понять, были совпадения или нет
    public static String findMatches(String secret, String answer) {
        boolean hasMatches = false;
        String matches = "";
        for (int i = 0; i < secret.length(); i++) {
            char c = secret.charAt(i);
            if (i >= answer.length() || c != answer.charAt(i)) {
                matches += '#';
            } else {
                matches += c;
                hasMatches = true;
            }
        }
        if (!hasMatches) return "";
        return padRight(matches, '#', 15);
    }

}
