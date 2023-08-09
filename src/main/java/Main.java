import api.ATM;
import api.FileDBDriver;
import api.exceptions.DBDriverException;

public class Main {
    public static void main(String[] args) {
        try {
            CLI cli = new CLI(new ATM(new FileDBDriver("src/main/resources/db.txt")));
            cli.start();
        } catch (DBDriverException e) {
            System.out.println(e.getMessage());
        }
    }
}
