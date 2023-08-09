package api;

import api.exceptions.DBDriverException;

public class ATM {
    private static final int MAX_DEPOSIT = 1_000_000;
    private final DBDriver dbDriver;
    private int moneyInATM;
    private Account account;

    public ATM(DBDriver dbDriver) throws DBDriverException {
        this(dbDriver, 5_000_000);
    }

    public ATM(DBDriver dbDriver, int moneyInATM) throws DBDriverException {
        this.dbDriver = dbDriver;
        this.moneyInATM = moneyInATM;
    }

    public boolean isAccount(String number) {
        return dbDriver.isAccount(number);
    }

    public boolean setAccount(String number, String pin) {
        Account account = dbDriver.findAccount(number);
        if (account != null && account.isVerified(pin)) {
            this.account = account;
            return true;
        }
        return false;
    }

    public boolean isActive(String number) {
        return dbDriver.findAccount(number).isActive();
    }

    public boolean isValid() {
        return account != null;
    }

    public int getBalance() {
        return account.getBalance();
    }

    public void depositMoney(int deposit) throws DBDriverException {
        if (deposit > MAX_DEPOSIT) {
            throw new DBDriverException("Сумма для внесения слишком большая.");
        }
        account.setBalance(account.getBalance() + deposit);
        dbDriver.saveAccount(account);
        moneyInATM += deposit;
    }

    public void withdrawMoney(int withdraw) throws DBDriverException {
        if (withdraw > account.getBalance()) {
            throw new DBDriverException("На счету не достаточно средств.");
        } else if (withdraw > moneyInATM) {
            throw new DBDriverException("В банкомате не достаточно наличных.");
        }
        account.setBalance(account.getBalance() - withdraw);
        dbDriver.saveAccount(account);
        moneyInATM -= withdraw;
    }

    public void blockAccount(String number) {
        Account account = dbDriver.findAccount(number);
        if (account != null) {
            account.setBlock();
            try {
                dbDriver.saveAccount(account);
            } catch (DBDriverException ignore) { }
        }
    }

    public void clearAccount() {
        account = null;
    }
}
