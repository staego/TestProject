package api;

import api.exceptions.LoaderException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TxtLoader implements Loader{
    private final String dbFile;

    public TxtLoader(String dbFile) {
        this.dbFile = dbFile;
    }

    public Map<String, String> load() throws LoaderException {
        Map<String, String> result = new HashMap<>();

        try {
            File f = new File(dbFile);
            if(!f.exists()) {
                f.createNewFile();
            }
        } catch (IOException e) {
            throw new LoaderException();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(dbFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] array = line.split(" ");
                if (array.length == 4) {
                    result.put(array[0], line);
                }
            }
        } catch (FileNotFoundException e) {
            throw new LoaderException();
        } catch (IOException ignore) { }
        return result;
    }

    public void save(Map<String, String> accounts) throws LoaderException {
        try (FileWriter writer = new FileWriter(dbFile)) {
            for(String line: accounts.values()) {
                writer.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new LoaderException();
        }
    }
}
