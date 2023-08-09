package api;

import api.exceptions.DBDriverException;

public interface DBDriver {
    boolean isAccount(String number);
    Account findAccount(String number);
    void saveAccount(Account account) throws DBDriverException;
}
