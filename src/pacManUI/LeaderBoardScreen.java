package pacManUI;

import PacmanGame.*;
import Util.ButtonUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;

public class LeaderBoardScreen extends JPanel {
    private MainMenu mainMenu;
    private PacmanPlay pacmanPlay;
    private JFrame gameFrame;
    private ArrayList<PlayerScore> easyScores;
    private ArrayList<PlayerScore> mediumScores;
    private ArrayList<PlayerScore> hardScores;

    public LeaderBoardScreen(MainMenu mainMenu, PacmanPlay pacmanPlay, JFrame gameFrame) {
        this.mainMenu = mainMenu;
        this.pacmanPlay = pacmanPlay;
        this.gameFrame = gameFrame;
        setPreferredSize(new Dimension(608, 576));
        setBackground(Color.BLACK);
        setLayout(null);

        loadScores();

        JLabel titleLabel = new JLabel("Leaderboard");
        titleLabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 40));
        titleLabel.setForeground(Color.CYAN);
        titleLabel.setBounds(190, 20, 300, 50);
        add(titleLabel);

        int yOffset = 80;
        createTable("EASY", easyScores, 50, yOffset);
        createTable("MEDIUM", mediumScores, 230, yOffset);
        createTable("HARD", hardScores, 410, yOffset);

        JButton mainMenuButton = new JButton("Main Menu");
        new ButtonUtil().checkJbutton(mainMenuButton);
        mainMenuButton.setBounds(180, 450, 210, 40);
        mainMenuButton.addActionListener(e -> {
            gameFrame.dispose();
            mainMenu.showMainMenu();
        });
        add(mainMenuButton);

    }

    private void loadScores() {
        easyScores = new ArrayList<>();
        mediumScores = new ArrayList<>();
        hardScores = new ArrayList<>();

        File file = new File("scores.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                PlayerScore playerScore = PlayerScore.fromString(line);
                if (playerScore != null) {
                    switch (playerScore.getDifficulty()) {
                        case "EASY":
                            easyScores.add(playerScore);
                            break;
                        case "MEDIUM":
                            mediumScores.add(playerScore);
                            break;
                        case "HARD":
                            hardScores.add(playerScore);
                            break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // File chưa tồn tại, sẽ tạo mới khi lưu
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(easyScores);
        Collections.sort(mediumScores);
        Collections.sort(hardScores);

        if (easyScores.size() > 10) easyScores = new ArrayList<>(easyScores.subList(0, 5));
        if (mediumScores.size() > 10) mediumScores = new ArrayList<>(mediumScores.subList(0, 5));
        if (hardScores.size() > 10) hardScores = new ArrayList<>(hardScores.subList(0, 5));
    }

    private void createTable(String difficulty, ArrayList<PlayerScore> scores, int xOffset, int yOffset) {
        JLabel difficultyLabel = new JLabel(difficulty);
        difficultyLabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 20));
        difficultyLabel.setForeground(Color.YELLOW);
        difficultyLabel.setBounds(xOffset + 40, yOffset, 100, 30);
        add(difficultyLabel);

        String[] columnNames = {"Name", "Score"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (PlayerScore score : scores) {
            model.addRow(new Object[]{score.getName(), score.getScore()});
        }

        if (scores.isEmpty()) {
            model.addRow(new Object[]{"No scores", ""});
        }

        JTable table = new JTable(model);
        table.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
        table.setForeground(Color.WHITE);
        table.setBackground(Color.DARK_GRAY);
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);

        table.getTableHeader().setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 14));
        table.getTableHeader().setForeground(Color.YELLOW);
        table.getTableHeader().setBackground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(xOffset, yOffset + 30, 150, 250);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane);
    }
}