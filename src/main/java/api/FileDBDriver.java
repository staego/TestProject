package api;

import api.exceptions.DBDriverException;
import api.exceptions.LoaderException;

import java.util.Map;

public class FileDBDriver implements DBDriver{
    Loader loader;
    Map<String, String> data;

    public FileDBDriver(String file) throws DBDriverException {
        this.loader = new TxtLoader(file);
        try {
            this.data = loader.load();
        } catch (LoaderException e) {
            throw new DBDriverException("Банкомат временно не работает.");
        }
    }

    public boolean isAccount(String number) {
        return data.getOrDefault(number, null) != null;
    }

    public Account findAccount(String number) {
        try {
            String[] array = data.getOrDefault(number, "").split(" ");
            return new Account(array[0], array[1], Integer.parseInt(array[2]), Long.parseLong(array[3]));
        } catch (NullPointerException | NumberFormatException e) {
            return null;
        }
    }

    public void saveAccount(Account account) throws DBDriverException {
        String[] array = {account.getNumber(), account.getPin(), String.valueOf(account.getBalance()), String.valueOf(account.getBlock())};
        String line = String.join(" ", array);
        String tmp_line = data.getOrDefault(account.getNumber(), "");
        data.put(account.getNumber(), line);
        try {
            loader.save(data);
        } catch (LoaderException e) {
            data.put(account.getNumber(), tmp_line);
            throw new DBDriverException("По техническим причинам действие выполнить не удалось.");
        }
    }
}
