package ru.danilov.calcapp;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by Danilov Alexey on 02.03.2016.
 * Метод рекурсивного спуска: существуют несколько правил для выделения лексем (+,-,*,/)
 * создадим комплекс взаимовызвающихся(пока есть лексемы,которые можем обрабоать) функций,
 * каждая из которой отвечает за работу со своим типом лексем, на выходе получим результат
 * выполнения общей введенной функции
 */
public class MatchParser {
    //Начало вычислений:
    public double Parse(String s) throws Exception {
        s = s.replace(" ", "");
        /**
         * Result - контаинер для остатка не распознаной лексемы, addition "обработает, что сможет
         * остаток вернет"
         */
        Result result = addition(s);
        if (!result.exp.isEmpty()) {
            System.err.println("Выражение не возможно распознать");
            System.err.println("exp: " + result.exp);
        }
        return result.storage;
    }

    //Блок отвечающий за роботу с лексемами + и -
    private Result addition(String s) throws Exception {
        Result current = multiplication(s);
        double storage = current.storage;

        while (current.exp.length() > 0) {
            if (!(current.exp.charAt(0) == '+' || current.exp.charAt(0) == '-')) break;

            char sign = current.exp.charAt(0);
            String next = current.exp.substring(1);

            current = multiplication(next);
            if (sign == '+') {
                storage += current.storage;
            } else {
                storage -= current.storage;
            }
        }
        return new Result(storage, current.exp);
    }

    private Result checkBrecers(String s) throws Exception {
        char firstSym = s.charAt(0);
        if (firstSym == '(') {
            Result r = addition(s.substring(1));
            if (!r.exp.isEmpty() && r.exp.charAt(0) == ')') {
                r.exp = r.exp.substring(1);
            } else {
                System.err.println("Не все скобки закрыты");
            }
            return r;
        }
        return isNumber(s);
    }

    //Блок отвечающий за роботу с лексемами * и /
    private Result multiplication(String s) throws Exception {
        Result current = checkBrecers(s);

        double storage = current.storage;
        while (true) {
            if (current.exp.length() == 0) {
                return current;
            }
            char sign = current.exp.charAt(0);
            if ((sign != '*' && sign != '/')) return current;

            String next = current.exp.substring(1);
            Result right = checkBrecers(next);

            if (sign == '*') {
                storage *= right.storage;
            } else {
                storage /= right.storage;
            }

            current = new Result(storage, right.exp);
        }
    }

    private Result isNumber(String s) throws Exception {
        int i = 0;
        int dot_cnt = 0;
        boolean negative = false;
        // проверка числа на отрицательность
        if (s.charAt(0) == '-') {
            negative = true;
            s = s.substring(1);
        }
        // проверка на валидность записи числа, только цифры и точки
        while (i < s.length() && (Character.isDigit(s.charAt(i)) || s.charAt(i) == '.')) {
            // точка в одном числе может быть только одна
            if (s.charAt(i) == '.' && ++dot_cnt > 1) {
                throw new Exception("Неправильная запись '" + s.substring(0, i + 1) + "'");
            }
            i++;
        }
        if (i == 0) {
            throw new Exception("Чисел не распознано '" + s + "'");
        }
        //учиываем знак числа
        double dPart = Double.parseDouble(s.substring(0, i));
        if (negative) dPart = -dPart;
        String restPart = s.substring(i);

        return new Result(dPart, restPart);
    }

    //Перевод постфиксной записи в инфиксную
    public double ParseRPN(String exp) throws Exception {
        exp = normalizeRPNExp(exp.replace("\\s+", " "));
        Stack<String> stack = new Stack<>();
        String[] symArray = exp.split("\\s+");
        String a, b;
        int i = 0;
        while (symArray.length != i) {
            if (symArray[i].equals(" ")){
                i++;
                continue;
            }
            if (isOperator(symArray[i])) {
                b = stack.pop();
                a = stack.pop();
                if (getPriorites(symArray[i]) == 2) {
                    stack.push("(" + a + symArray[i] + b + ")");
                } else stack.push(a + symArray[i] + b);
            } else stack.push(symArray[i]);
            i++;
        }
       return Parse(stack.pop().replace("0-", "-"));
    }

    private int getPriorites(String s) {
        switch (s) {
            case "+":
                return 2;
            case "-":
                return 2;
            case "*":
                return 3;
            case "/":
                return 3;
        }
        return -1;
    }

    public boolean isOperator(String sign) {
        return (sign.equals("+") || sign.equals("-") ||
                sign.equals("*") ||
                sign.equals("/") || sign.equals("^"));
    }

    //Проверка на отрицательные числа в постфиксной нотации
    public String normalizeRPNExp(String exp) throws Exception {
        String[] symbols = exp.split("\\s+");
        String normalExp = "";
        for (int i = 0; i < symbols.length - 3; i++) {
            if (isOperator(symbols[i]) && isOperator(symbols[i + 1])
                    && !isOperator(symbols[i + 2]) && symbols[i + 3].equals("-")) {
                {
                    symbols[i + 2] = "0 " + symbols[i + 2];
                }
            }
        }
        for (String s : symbols) {
            normalExp += " " + s;
        }
        return normalExp;
    }

    //Проверка, какую запись использовали
    public boolean isDirectNote(String exp) throws Exception {
        String sign = "";
        try {
            sign = exp.substring(exp.length() - 1);
        } catch (Exception e){
            throw  new Exception("Неправильный формат записи, при постфиксной записи разделяйте операторы и операнда пробелами");
        }
        return !(sign.equals("+") || sign.equals("-") ||
                sign.equals("*") ||
                sign.equals("/") || sign.equals("^"));
    }
}