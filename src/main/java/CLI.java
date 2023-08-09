import api.ATM;
import api.exceptions.DBDriverException;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CLI {
    private static final Pattern NUMBER = Pattern.compile("(?:\\d{4}-){3}\\d{4}");
    private final ATM atm;
    private final Scanner scanner;
    private String number;

    public CLI(ATM atm) {
        this.atm = atm;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            setNumber();

            if (atm.isActive(number)) {
                setPin();
            } else {
                System.out.println("Доступ к карте временно заблокирован");
                continue;
            }
            if (atm.isValid()) {
                runMenu();
            } else {
                System.out.println("Произошла ошибка доступа.");
            }
        }
    }

    private void setNumber() {
        String tmp_number;
        while (true) {
            System.out.print("Введите номер карты (вида ХХХХ-ХХХХ-ХХХХ-ХХХХ):");
            tmp_number = scanner.nextLine();
            Matcher matcher = NUMBER.matcher(tmp_number);
            if (!matcher.find()) {
                System.out.println("Не верный формат номера карты.");
                continue;
            }
            if (atm.isAccount(tmp_number)) {
                number = tmp_number;
                break;
            }
            System.out.print("Карта с таким номером не найдена! ");
        }
    }

    private void setPin() {
        String pin;
        for (int i = 3; i > 0; i--) {
            System.out.print("Введите ПИН-код:");
            pin = scanner.nextLine();
            if (atm.setAccount(number, pin)) {
                break;
            }
            System.out.print("Не правильный ПИН-код. ");
            if (i == 1) {
                atm.blockAccount(number);
                System.out.println("Доступ к карте заблокирован на 24 часа.");
            }
        }
    }

    private void runMenu() {
        String separator = "---------------------";
        boolean flag = true;
        while (flag) {
            for (String line: new String[] {
                    "",
                    "Меню:",
                    "1 - Баланс.",
                    "2 - Снять наличные.",
                    "3 - Пополнить баланс.",
                    "0 - Завершить сеанс."}) {
                System.out.println(line);
            }
            System.out.print("Выберете пункт меню:");

            switch (scanner.nextLine()) {
                case "1" -> {
                    System.out.println(separator);
                    System.out.println("Ваш баланс: " + atm.getBalance());
                }
                case "2" -> {
                    withdrawMoney(separator);
                }
                case "3" -> {
                    depositMoney(separator);
                }
                case "0" -> {
                    flag = false;
                    atm.clearAccount();
                    number = null;
                }
                default -> {
                    System.out.println(separator);
                    System.out.println("Команда не распознана. ");
                }
            }
        }
    }

    private void withdrawMoney(String separator) {
        try {
            System.out.print("Введите сумму, которую хотите снять:");
            int withdraw = Integer.parseInt(scanner.nextLine());
            System.out.println(separator);
            try {
                atm.withdrawMoney(withdraw);
                System.out.println("Заберите деньги.");
            } catch (DBDriverException e) {
                System.out.println(e.getMessage());;
            }
        } catch (NumberFormatException e) {
            System.out.println(separator);
            System.out.println("Не верно указана сумма.");
        }
    }

    private void depositMoney(String separator) {
        try {
            System.out.print("Введите сумму, которую хотите внести:");
            int deposit = Integer.parseInt(scanner.nextLine());
            System.out.println(separator);
            try {
                atm.depositMoney(deposit);
                System.out.println("Средства зачислены.");
            } catch (DBDriverException e) {
                System.out.println(e.getMessage());;
            }
        } catch (NumberFormatException e) {
            System.out.println(separator);
            System.out.println("Не верно указана сумма.");
        }
    }
}
