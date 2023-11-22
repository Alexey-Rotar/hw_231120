package org.example;

import java.util.Random;
import java.util.Scanner;

public class Program {

    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = '*';

    private static final int WIN_COUNT = 4;

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    private static  char[][] field;
    private static int fieldSizeX;
    private static int fieldSizeY;


    public static void main(String[] args) {
        while (true){
            initialize();
            printField();
            while (true){
                humanTurn();
                printField();
                if (checkGameState(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if (checkGameState(DOT_AI, "Победил компьютер!"))
                    break;
            }
            System.out.print("Желаете сыграть еще раз? (Y - да): ");
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
        }
    }

    /**
     * Инициализация игрового поля
     */
    static void initialize(){
        fieldSizeY = 5;
        fieldSizeX = 5;

        field = new char[fieldSizeY][fieldSizeX];
        for (int y = 0; y < fieldSizeY; y++){
            for (int x = 0; x < fieldSizeX; x++){
                field[y][x] = DOT_EMPTY;
            }
        }
    }

    /**
     * Печать текущего состояния игрового поля
     */
    private static void printField(){
        System.out.print("+");
        for (int i = 0; i < fieldSizeX; i++){
            System.out.print("-" + (i + 1));
        }
        System.out.println("-");

        for (int y = 0; y < fieldSizeY; y++){
            System.out.print(y + 1 + "|");
            for (int x = 0; x < fieldSizeX; x++){
                System.out.print(field[y][x] + "|");
            }
            System.out.println();
        }

        for (int i = 0; i < fieldSizeX * 2 + 2; i++){
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Ход игрока (человека)
     */
    static void humanTurn(){
        int x;
        int y;

        do {
            System.out.print("Введите координаты хода X и Y (X: 1..." + fieldSizeX + "; Y: 1..." + fieldSizeY + ")\nчерез пробел: ");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));

        field[y][x] = DOT_HUMAN;
    }

    /**
     * Ход игрока (компьютера)
     */
    static void aiTurn(){
        int x;
        int y;
        //если следующим ходом не сможет победить человек и не сможет победить компьютер
        if (!getWin(DOT_HUMAN, DOT_AI) && !getWin(DOT_AI, DOT_AI)) {
            do {
                x = random.nextInt(fieldSizeX);
                y = random.nextInt(fieldSizeY);
            }
            while (!isCellEmpty(x, y));
            field[y][x] = DOT_AI;
        }
    }

    /**
     * Проверка, является ли ячейка игрового поля пустой
     * @param x
     * @param y
     * @return
     */
    static boolean isCellEmpty(int x, int y){
        return field[y][x] == DOT_EMPTY;
    }

    /**
     * Проверка доступности ячейки игрового поля
     * @param x
     * @param y
     * @return
     */
    static boolean isCellValid(int x, int y){
        return x >= 0 && x< fieldSizeX && y >= 0 && y < fieldSizeY;
    }


    /**
     * Метод проверки состояния игры
     * @param dot фишка игрока
     * @param s победный слоган
     * @return результат проверки состояния игры
     */
    static boolean checkGameState(char dot, String s){
        if (checkWin(dot)){
            System.out.println(s);
            return true;
        }
        if (checkDraw()){
            System.out.println("Ничья!");
            return true;
        }
        return false; // Игра продолжается
    }

    /**
     * Проверка на ничью
     * @return
     */
    static boolean checkDraw(){
        for (int y = 0; y < fieldSizeY; y++){
            for (int x = 0; x < fieldSizeX; x++){
                if (isCellEmpty(x, y))
                    return false;
            }
        }
        return true;
    }

    /**
     * Проверка победы игрока
     * @param dot фишка игрока
     * @return признак победы
     */
    static boolean checkWin(char dot){
        for (int y = 0; y < fieldSizeY; y++){
            for (int x = 0; x < fieldSizeX; x++){
                if (check1(x, y, dot, WIN_COUNT) || check2(x, y, dot, WIN_COUNT) || check3(x, y, dot, WIN_COUNT) || check4(x, y, dot, WIN_COUNT))
                    return true;
            }
        }
        return false;
    }

    /**
     * Проверка наличия количества фишек по вертикали
     * @param x стартовая координата X
     * @param y стартовая координата Y
     * @param dot фишка игрока
     * @param winCount проверяемое количество фишек
     * @return результат проверки
     */
    static boolean check1(int x, int y, char dot, int winCount){
        int dotCount = 0;
        for (int i = 0; i < winCount && y + i < fieldSizeY; i++) {
            if (field[y + i][x] == dot)
                dotCount++;
        }
        return dotCount == winCount;
    }

    /**
     * Проверка наличия количества фишек по горизонтали
     * @param x стартовая координата X
     * @param y стартовая координата Y
     * @param dot фишка игрока
     * @param winCount проверяемое количество фишек
     * @return результат проверки
     */
    static boolean check2(int x, int y, char dot, int winCount){
        int dotCount = 0;
        for (int i = 0; i < winCount && x + i < fieldSizeX; i++) {
            if (field[y][x + i] == dot)
                dotCount++;
        }
        return dotCount == winCount;
    }

    /**
     * Проверка наличия количества фишек по диагонали вправо вниз
     * @param x стартовая координата X
     * @param y стартовая координата Y
     * @param dot фишка игрока
     * @param winCount проверяемое количество фишек
     * @return результат проверки
     */
    static boolean check3(int x, int y, char dot, int winCount){
        int dotCount = 0;
        for (int i = 0; i < winCount && x + i < fieldSizeX && y + i < fieldSizeY; i++) {
            if (field[y + i][x + i] == dot)
                dotCount++;
        }
        return dotCount == winCount;
    }

    /**
     * Проверка наличия количества фишек по диагонали вправо вверх
     * @param x стартовая координата X
     * @param y стартовая координата Y
     * @param dot фишка игрока
     * @param winCount проверяемое количество фишек
     * @return результат проверки
     */
    static boolean check4(int x, int y, char dot, int winCount){
        int dotCount = 0;
        for (int i = 0; i < winCount && y - i >= 0 && x + i < fieldSizeX; i++) {
            if (field[y - i][x + i] == dot)
                dotCount++;
        }
        return dotCount == winCount;
    }

    /**
     * Проверка может ли быть победа при одном следующем ходе
     * @param dot1 проверяемая фишка, сделав ход которой можно победить
     * @param dot2 фишка, которая устанавливается на решающее место
     * @return результат проверки
     */
    static boolean getWin(char dot1, char dot2){
        for (int y = 0; y < fieldSizeY; y++){
            for (int x = 0; x < fieldSizeX; x++){
                if (isCellEmpty(x, y)) {
                    field[y][x] = dot1;
                    if (checkWin(dot1)) {
                        field[y][x] = dot2;
                        return true;
                    }
                    else {
                        field[y][x] = DOT_EMPTY;
                    }
                }
            }
        }
        return false;
    }

}
