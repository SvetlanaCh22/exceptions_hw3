/*
Напишите приложение, которое будет запрашивать у пользователя следующие данные в произвольном порядке, разделенные пробелом:
Фамилия Имя Отчество дата рождения номер телефона пол
Форматы данных:
фамилия, имя, отчество - строки

дата_рождения - строка формата dd.mm.yyyy
номер_телефона - целое беззнаковое число без форматирования

пол - символ латиницей f или m.
Приложение должно проверить введенные данные по количеству. Если количество не совпадает с требуемым, вернуть код ошибки, обработать его и показать пользователю сообщение, что он ввел меньше и больше данных, чем требуется.
Приложение должно попытаться распарсить полученные значения и выделить из них требуемые параметры. Если форматы данных не совпадают, нужно бросить исключение, соответствующее типу проблемы. Можно использовать встроенные типы java и создать свои. Исключение должно быть корректно обработано, пользователю выведено сообщение с информацией, что именно неверно.
Если всё введено и обработано верно, должен создаться файл с названием, равным фамилии, в него в одну строку должны записаться полученные данные, вида
<Фамилия><Имя><Отчество><датарождения> <номертелефона><пол>
Однофамильцы должны записаться в один и тот же файл, в отдельные строки.
Не забудьте закрыть соединение с файлом.
При возникновении проблемы с чтением-записью в файл, исключение должно быть корректно обработано, пользователь должен увидеть стектрейс ошибки.

Автор: Чубченко Светлана
*/

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Main {
    // создадим поля для упорядочивания перед записью в файл
    // можно и через отдельный класс, но для такой простой программы это лишние 20 строк кода
    static String tofileFIO;
    static String tofileDat;
    static String tofilePhone;
    static String tofileGender;

    public static void main(String[] args) {
        // на английском, так как консоль в iDEA сколько не пыталась победить - не выводит русские буквы
        System.out.println("Enter first, middle and last name, date of birth and sex randomly.");
        System.out.println("Please enter 'end' to stop");
        // считаем строку и попробуем распарсить
        String str;
        do {
            clearFields();
            str = readString();
            String[] arr = str.split(" ");
            // если введена пустая строка
            if (str.isEmpty()) {
                System.out.println("[!]  You entered void line");
                continue;
            }
            for (String s : arr) {
                // проверим не дата ли?
                if (checkDate(s)) {
                    tofileDat = s;
                    continue;
                }
                // а если одна буква, то пол
                if (s.length() == 1) {
                    if (s.equals("м") || s.equals("ж")) tofileGender = "м";
                    continue;
                }
                // если число с возможным символом плюс, то телефон
                if (checkPhone(s)) {
                    tofilePhone = s;
                    continue;
                }
                tofileFIO = tofileFIO + s + " ";
            }
            // проверим поля
            if (checkFields()) {
                // выделим фамилию
                String lastName = tofileFIO.split(" ")[0];
                // сохраняем в файл
                String line = tofileFIO + tofileDat + " " + tofilePhone + " " + tofileGender;
                writeFile(lastName, line);
            }
        } while (!str.equals("end"));
    }

    // Проверка существования полей
    static boolean checkFields() {
        // если ФИО нет
        if (tofileFIO.isEmpty()) {
            System.out.println("[!]  Can't find a name");
            return false;
        }
        // если дата рождения не указана
        if (tofileDat.isEmpty()) {
            System.out.println("[!]  Can't find a date of birth");
            return false;
        }
        // если нет телефона
        if (tofilePhone.isEmpty()) {
            System.out.println("[!]  Can't find a phone");
            return false;
        }
        // если не указан пол
        if (tofileGender.isEmpty()) {
            System.out.println("[!]  Can't find a gender");
            return false;
        }
        return true;
    }

    // Обнуление полей
    static void clearFields() {
        tofileFIO = "";
        tofileDat = "";
        tofilePhone = "";
        tofileGender = "";
    }

    // Запись строки в файл. Если файл существует, то дозапись.
    static void writeFile(String pathToFile, String line) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(pathToFile+".txt", true));
            pw.println(line);
            pw.close();
        } catch (Exception e) {
            System.out.println("[!] Error during saving file");
            System.out.printf("[!] %s", e.getMessage());
        }
    }

    // Проверка строки на схожесть с датой
    static boolean checkDate(String str) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return dateFormat.parse(str) != null;
        } catch (Exception e) {
            return false;
        }
    }

    // Проверка строки на схожесть с номером телефона
    static boolean checkPhone(String str) {
        // если строка пустая - не телефон
        if (str == null || str.isEmpty()) return false;
        // если вначале плюс - пропускаем
        if (str.startsWith("+")) str = str.substring(1);
        // проверяем что все части - числа
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) return false;
        }
        return true;
    }

    // Чтение строки из консоли
    static String readString() {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        if (str.isEmpty() || (str.trim().equals(""))) {
            System.out.println("[!]  You entered void line");
            return "";
        } else {
            return str;
        }
    }
}

