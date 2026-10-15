package Test;
import java.util.ArrayList;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import src.Card.Card;
import src.Card.CardSet;
import src.Login.Player;
import src.Login.ScoreCSV;
import src.Game.GameLogic;
import src.Game.ScoreManager;

/*
Card Test
1	สร้างการ์ดใหม่ สถานะจับคู่ต้องเป็น false
2	เมื่อสั่ง setMatched(true) การ์ดต้องเปลี่ยนเป็นจับคู่สำเร็จ
3	getImagePath() ต้องคืนตำแหน่งรูปภาพที่กำหนดไว้

test card set
4	getName() ต้องคืนชื่อชุดการ์ดถูกต้อง
5	จำนวนการ์ดที่สร้างต้องเป็นเลขคู่
6	ต้องสร้างการ์ดทั้งหมด 16 ใบ
7	การ์ดแต่ละรูปต้องมี 2 ใบเท่านั้น
8	การ์ดทุกใบต้องยังไม่ถูกจับคู่เมื่อเริ่มเกม
9	การสลับการ์ดต้องทำให้เกิดลำดับที่แตกต่างกัน

PlayerTest
10	สร้างผู้เล่นใหม่ getUsername() ต้องคืนชื่อผู้เล่นถูกต้อง

logicgame
ข้อ	สิ่งที่ทดสอบ
11	กดการ์ดใบเดิมซ้ำ ต้องไม่นับ Moves
12	เปิดการ์ดครบ 2 ใบ Moves เพิ่ม 1
13	จับคู่ผิด ระหว่างรอห้ามเปิดการ์ดใบที่ 3
14	เมื่อจับคู่ผิด ระบบต้องสามารถคืนสถานะการ์ดและยกเลิกการรอได้
15	จับคู่ถูก การ์ดต้องเป็น matched = true และกดซ้ำไม่ได้
16	จับคู่ครบ 8 คู่ เกมต้องชนะและคะแนนเป็น 100 หากไม่เคยจับผิด
17	กด New Game ต้องรีเซ็ต Moves, คะแนน, จำนวนคู่ที่จับได้ และสถานะเกม

score test
18	คะแนนเริ่มต้นต้องเป็น 100
19	Moves 1–8 คะแนนต้องไม่ลด
20	Move 9 เป็นต้นไป คะแนนลดครั้งละ 3
21	คะแนนต้องไม่ติดลบ
22	Leaderboard ต้องเก็บคะแนนสูงสุดของผู้เล่นแต่ละคน
23	Leaderboard ต้องเรียงคะแนนจากมากไปน้อย และหากเท่ากันให้ใช้เวลาน้อยกว่าอยู่อันดับสูงกว่า

*/

public class MemoryGameTest {

    static int passed = 0;
    static int failed = 0;

    static void check(boolean condition, String testName) { 
        if (condition) {
            passed++; 
            System.out.println("[PASS] " + testName); 
        } else { 
            failed++;
            System.out.println("[FAIL] " + testName);
        } 
    }

    public static void main(String[] args) {

        System.out.println("===  Memory Card Test ===\n");
        testCard();
        testCardSet();
        testPlayer();
        testBoard();
        testScore();


        
        System.out.println("\n=== Summary ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total : " + (passed + failed));
        System.out.println(failed == 0 ? "ALL TESTS PASSED" : "SOME TESTS FAILED");

        if (failed > 0) {
            System.exit(1);
        }
    }
    
    static void testCard() { 
    System.out.println("--- Card tests ---"); 

    Card card = new Card("./Picture/animals/cat.png"); // สร้างการ์ดใหม่ (path อะไรก็ได้ เพราะเทสนี้ไม่ได้โหลดรูป)
    check(card.isMatched() == false, "New card is not matched"); // เช็คว่า isMatched() เป็น false

    card.setMatched(true);
    check(card.isMatched() == true, "setMatched(true) makes card matched");

    Card card2 = new Card("./Picture/animals/cat.png");
    check(card2.getImagePath().equals("./Picture/animals/cat.png"), "getImagePath() returns the given path");
    }


    static void testCardSet() {
    System.out.println("--- CardSet tests ---"); 

    CardSet animalSet = new CardSet("Animals", new String[] {
        "./Picture/animals/bear.png",
        "./Picture/animals/bird.png", 
        "./Picture/animals/cat.png", 
        "./Picture/animals/dog.png", 
        "./Picture/animals/lion.png", 
        "./Picture/animals/panda.png", 
        "./Picture/animals/rabbit.png", 
        "./Picture/animals/tiger.png" 
    }); 
    check(animalSet.getName().equals("Animals"), " getName() returns the given name");

     ArrayList<Card> cards = animalSet.createCards();
    check(cards.size() % 2 == 0, "Number of cards is even");

    check(cards.size() == 16, "Number of cards is exactly 16");

    boolean allPairsComplete = true;
    for (Card c : cards) {
        int count = 0; 
        for (Card other : cards) { 
            if (other.getImagePath().equals(c.getImagePath())) {
                count++; 
            } 
        } 
        if (count != 2) { 
            allPairsComplete = false;
        } 
    } 
    check(allPairsComplete, "Every image has exactly 2 cards");

    boolean noneMatched = true;
    for (Card c : cards) { 
        if (c.isMatched()) { 
            noneMatched = false; 
        } 
    } 
    check(noneMatched, "No card is matched at start");

    ArrayList<Card> firstRound = animalSet.createCards(); // สร้างการ์ดรอบแรกไว้เป็นตัวเทียบ
    boolean foundDifferentOrder = false; // สมมติไว้ก่อนว่ายังไม่เจอรอบที่ลำดับต่างออกไป
    for (int round = 0; round < 10; round++) { // สร้างการ์ดใหม่อีก 10 รอบ
        ArrayList<Card> newRound = animalSet.createCards(); // สร้างการ์ดชุดใหม่ (สลับที่ใหม่)
        for (int i = 0; i < firstRound.size(); i++) { // เทียบการ์ดทีละตำแหน่ง
            if (!firstRound.get(i).getImagePath().equals(newRound.get(i).getImagePath())) { // ถ้ารูปตำแหน่งนี้ไม่ตรงกับรอบแรก
                foundDifferentOrder = true; // แปลว่าลำดับต่างกัน
            }
        }
    }
    check(foundDifferentOrder, "Cards are shuffled (order differs between rounds)");
    }


    static void testPlayer() {
    System.out.println("--- Player tests ---");

    Player player = new Player("Nattanan"); 
    check(player.getUsername().equals("Nattanan"), "getUsername() returns the given name");
    }

    //------------------------------------//-------------------------------------------//

    static void testBoard() {
        System.out.println("--- GameLogic tests ---");
        CardSet set = new CardSet("Test", new String[] {
            "a", "b", "c", "d", "e", "f", "g", "h"
        });
        GameLogic game = new GameLogic(set.createCards());
        ArrayList<Card> cards = game.getCards();
        int pair = -1;
        int different = -1;
        for (int i = 1; i < cards.size(); i++) {
            if (cards.get(i).getImagePath().equals(cards.get(0).getImagePath())) pair = i;
            else if (different == -1) different = i;
        }

        game.flip(0);
        check(game.flip(0).equals("BLOCKED") && game.getMoves() == 0,
            "Same card cannot count twice");
        game.flip(different);
        check(game.getMoves() == 1, "Two cards increase moves");
        int third = pair;
        check(game.isWaiting() && game.flip(third).equals("BLOCKED") &&
            game.getMoves() == 1, "Wrong pair blocks third card");
        game.hideWrongPair();
        check(!game.isWaiting() && game.getFirstIndex() == -1,
            "Wrong pair can be hidden");
        game.flip(0);
        game.flip(pair);
        check(cards.get(0).isMatched() && cards.get(pair).isMatched() &&
            game.flip(0).equals("BLOCKED"), "Correct pair stays matched");

        game.reset(set.createCards());
        cards = game.getCards();
        boolean[] used = new boolean[cards.size()];
        String finalResult = "";
        for (int i = 0; i < cards.size(); i++) {
            if (used[i]) continue;
            for (int j = i + 1; j < cards.size(); j++) {
                if (!used[j] && cards.get(i).getImagePath().equals(cards.get(j).getImagePath())) {
                    game.flip(i);
                    finalResult = game.flip(j);
                    used[i] = true;
                    used[j] = true;
                    break;
                }
            }
        }
        check(finalResult.equals("WIN") && game.getMatchedPairs() == 8 &&
            game.getScore() == 100, "All 8 pairs finish game");
        game.reset(set.createCards());
        boolean clean = game.getMoves() == 0 && game.getScore() == 100 &&
            game.getMatchedPairs() == 0 && !game.isGameOver();
        for (Card card : game.getCards()) if (card.isMatched()) clean = false;
        check(clean, "New Game resets everything");
    }

        // ---------- Score tests ----------

    // คำนวณคะแนนตามจำนวน Moves
    static int calculateScore(int moves) {
        ScoreManager score = new ScoreManager();
        score.update(moves);
        return score.getScore();
    }

    static void testScore() {
        System.out.println("--- Score tests ---");

        check(calculateScore(0) == 100,
            "Start with 100 points");

        check(calculateScore(1) == 100 &&
              calculateScore(8) == 100,
            "First 8 moves do not lose points");

        check(calculateScore(9) == 97 &&
              calculateScore(10) == 94 &&
              calculateScore(11) == 91,
            "Lose 3 points after move 8");

        check(calculateScore(42) == 0 &&
              calculateScore(50) == 0,
            "Score cannot go below zero");

        Path file = Paths.get("scores.csv");
        byte[] oldData = null;
        boolean hasOldFile = Files.exists(file);
        boolean backupReady = false;

        try {
            if (hasOldFile) {
                oldData = Files.readAllBytes(file);
            }
            backupReady = true;
            Files.write(file, new byte[0]);

            ScoreCSV.saveScore("TestA", 80, 60);
            ScoreCSV.saveScore("TestA", 95, 50);
            ScoreCSV.saveScore("TestB", 90, 40);
            ScoreCSV.saveScore("TestC", 90, 30);

            ArrayList<ScoreCSV.Score> scores =
                ScoreCSV.getHighScores();

            boolean bestScore = false;

            for (ScoreCSV.Score s : scores) {
                if (s.username.equals("TestA") &&
                    s.points == 95) {
                    bestScore = true;
                }
            }

            check(scores.size() == 3 && bestScore,
                "Save highest score of each player");

            boolean correctRank =
                scores.size() == 3 &&
                scores.get(0).username.equals("TestA") &&
                scores.get(1).username.equals("TestC") &&
                scores.get(2).username.equals("TestB");

            check(correctRank,
                "Leaderboard sorts scores correctly");

        } catch (Exception e) {
            check(false, "ScoreCSV test error");
            System.out.println(e.getMessage());

        } finally {
            if (backupReady) {
                try {
                    if (hasOldFile) {
                        Files.write(file, oldData);
                    } else {
                        Files.deleteIfExists(file);
                    }
                } catch (Exception e) {
                    System.out.println(
                        "[ERROR] Cannot restore scores.csv");
                }
            }
        }
    }
}


