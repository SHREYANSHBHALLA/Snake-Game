import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ScoreManager {
    private static final String SCORES_FILE = "highscores.dat";
    private Map<String, Integer> scores;

    public ScoreManager() {
        scores = new TreeMap<>(Collections.reverseOrder());
        loadScores();
    }

    public void saveScore(String playerName, int score) {
        if (score > scores.getOrDefault(playerName, 0)) {
            scores.put(playerName, score);
            saveScores();
        }
    }

    public List<Map.Entry<String, Integer>> getTopScores(int limit) {
        return scores.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .limit(limit)
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private void loadScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SCORES_FILE))) {
            scores = (Map<String, Integer>) ois.readObject();
        } catch (FileNotFoundException e) {
            scores = new TreeMap<>(Collections.reverseOrder());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading scores: " + e.getMessage());
            scores = new TreeMap<>(Collections.reverseOrder());
        }
    }

    private void saveScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SCORES_FILE))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            System.err.println("Error saving scores: " + e.getMessage());
        }
    }
}