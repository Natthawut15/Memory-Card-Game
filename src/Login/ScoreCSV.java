package src.Login;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ScoreCSV {
    private static final String FILE_NAME = "scores.csv";

    public static class Score {
        public String username;
        public int points;
        public int seconds;

        public Score(String username, int points, int seconds) {
            this.username = username;
            this.points = points;
            this.seconds = seconds;
        }
    }

    // บันทึกเฉพาะเมื่อเกมชนะ (เรียกจาก Board.finishGame เท่านั้น)
    public static void saveScore(String username, int points, int seconds) {
        String safeName = username.replace("\n", " ").replace("\r", " ");
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(safeName + "," + points + "," + seconds + "\n");
        } catch (IOException e) {
            System.out.println("Failed to save score: " + e.getMessage());
        }
    }

    public static ArrayList<Score> getHighScores() {
        HashMap<String, Score> best = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // อ่านเลขสองช่องสุดท้าย เพื่อให้ชื่อผู้เล่นมี comma ได้
                int last = line.lastIndexOf(',');
                int middle = line.lastIndexOf(',', last - 1);
                if (middle < 0 || last <= middle) continue;

                try {
                    String name = line.substring(0, middle);
                    int points = Integer.parseInt(line.substring(middle + 1, last));
                    int seconds = Integer.parseInt(line.substring(last + 1));
                    if (name.isEmpty() || points <= 0 || points > 100 || seconds < 0) continue;

                    Score old = best.get(name);
                    if (old == null || points > old.points ||
                        (points == old.points && seconds < old.seconds)) {
                        best.put(name, new Score(name, points, seconds));
                    }
                } catch (NumberFormatException e) {
                    // ข้ามบรรทัดที่ข้อมูลไม่ถูกต้อง
                }
            }
        } catch (IOException e) {
            // ถ้ายังไม่มีไฟล์ แสดงตารางว่างได้ตามปกติ
        }

        ArrayList<Score> scores = new ArrayList<>(best.values());
        Collections.sort(scores, new Comparator<Score>() {
            @Override
            public int compare(Score a, Score b) {
                if (a.points != b.points) return Integer.compare(b.points, a.points);
                if (a.seconds != b.seconds) return Integer.compare(a.seconds, b.seconds);
                return a.username.compareToIgnoreCase(b.username);
            }
        });
        return scores;
    }
    
}

