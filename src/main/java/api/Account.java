package api;

import java.util.Date;

public class Account {
    private final String number;
    private final String pin;
    private int balance;
    private long block;

    public Account(String number, String pin, int balance, long block) {
        this.number = number;
        this.pin = pin;
        this.balance = balance;
        if (!isActive(block)) {
            this.block = block;
        }
    }

    private static boolean isActive(long block) {
        return block < new Date().getTime();
    }

    public boolean isActive() {
        return isActive(block);
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }

    public boolean isVerified(String pin) {
        return this.pin.equals(pin);
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }


    public long getBlock() {
        if (!isActive(block)) {
            return block;
        }
        return 0;
    }

    public void setBlock() {
        this.block = new Date().getTime() + 86_400_000L;
    }
}
