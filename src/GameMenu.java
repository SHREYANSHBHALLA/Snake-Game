import javax.swing.*;
import java.awt.*;

public class GameMenu extends JPanel {
    private final JButton startButton = new JButton("Start Game");
    private final JComboBox<String> difficultyBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
    private final JLabel titleLabel = new JLabel("SNAKE GAME", SwingConstants.CENTER);

    public GameMenu(GameWindow window) {
        setLayout(new BorderLayout(10, 20));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.GREEN);
        
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        centerPanel.add(new JLabel("Select Difficulty:"));
        centerPanel.add(difficultyBox);
        centerPanel.add(startButton);
        
        add(titleLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        
        startButton.addActionListener(e -> {
            window.startGame(difficultyBox.getSelectedIndex());
        });
    }
}