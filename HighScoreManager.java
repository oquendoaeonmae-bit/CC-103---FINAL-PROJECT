package FinalProject;

public class HighScoreManager {

    static String highName = "";
    static int highScore = 0;

    public static void addScore(String name, int score) {

        if (score > highScore) {
            highScore = score;
            highName = name;
        }
    }

    public static String getScores() {

        return highName + " - " + highScore;
    }
}