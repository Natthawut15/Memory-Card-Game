package src.Login;

import java.io.FileWriter;
import java.io.IOException;

public class PlayerCSV {
    private static final String FILE_NAME =
        "players.csv";

    public static void savePlayer(String username) {
        try (
            FileWriter writer = new FileWriter(FILE_NAME,true)) {

            writer.write(username + "\n");
            System.out.println("Saved player: " + username);

        } catch (IOException e) {
            System.out.println("Failed to save player data");
            e.printStackTrace();
        }
    }
}
