package FinalProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FlashcardApp extends JFrame implements ActionListener {

    JPanel frontPanel;
    JTextField nameField;
    JButton startButton;

    JPanel quizPanel;
    JLabel questionLabel, infoLabel;
    JButton[] choices = new JButton[4];
    JButton nextButton;

    int score = 0;
    boolean answered = false;

    String playerName = "";

    Player player;
    Quiz quiz;

    public FlashcardApp() {

        setTitle("Java Quiz");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        setLocationRelativeTo(null);

        Color bg = new Color(235, 245, 255);

        frontPanel = new JPanel(new GridBagLayout());
        frontPanel.setBackground(bg);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(bg);

        JLabel title = new JLabel("WELCOME TO JAVA QUIZ");
        title.setFont(new Font("Consolas", Font.BOLD, 35));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setFont(new Font("Consolas", Font.BOLD, 18));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(300, 40));
        nameField.setFont(new Font("Consolas", Font.PLAIN, 18));
        nameField.addKeyListener(new KeyAdapter() {
           
           
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!String.valueOf(c).matches("[a-zA-Z ]")) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Numbers are not allowed!");
                }
            }
        });

        startButton = new JButton("START");
        startButton.setFont(new Font("Consolas", Font.BOLD, 18));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(this);

        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(nameLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(startButton);

        frontPanel.add(formPanel);

        quizPanel = new JPanel(new BorderLayout());
        quizPanel.setBackground(bg);

        infoLabel = new JLabel("", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Consolas", Font.BOLD, 16));

        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Consolas", Font.BOLD, 25));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        topPanel.add(infoLabel, BorderLayout.NORTH);
        topPanel.add(questionLabel, BorderLayout.CENTER);

        JPanel choicePanel = new JPanel(new GridLayout(4, 2, 10, 10));
        choicePanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        choicePanel.setBackground(bg);

        for (int i = 0; i < 4; i++) {
            choices[i] = new JButton();
            choices[i].setFont(new Font("Consolas", Font.PLAIN, 16));
            choices[i].addActionListener(this);
            choicePanel.add(choices[i]);
        }

        nextButton = new JButton("Next");
        nextButton.setPreferredSize(new Dimension(120, 40));
        nextButton.setEnabled(false);
        nextButton.addActionListener(this);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(bg);
        bottomPanel.add(nextButton);

        quizPanel.add(topPanel, BorderLayout.NORTH);
        quizPanel.add(choicePanel, BorderLayout.CENTER);
        quizPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(frontPanel);

        setVisible(true);
    }

    public void loadQuestion() {

        infoLabel.setText("Name: " + playerName + "    Score: " + score);

        questionLabel.setText(
                "<html><div style='text-align:center;'>"
                        + quiz.getQuestion()
                        + "</div></html>"
        );

        String[] opts = quiz.getOptions();

        for (int i = 0; i < 4; i++) {
            choices[i].setText(opts[i]);
            choices[i].setEnabled(true);
            choices[i].setBackground(Color.WHITE);
        }

        answered = false;
        nextButton.setEnabled(false);
    }

  
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == startButton) {

            playerName = nameField.getText();

            if (playerName.equals("")) {
                JOptionPane.showMessageDialog(this, "Please enter your name.");
                return;
            }

            player = new Player(playerName);
            quiz = new Quiz();

            remove(frontPanel);
            add(quizPanel);
            revalidate();
            repaint();

            loadQuestion();
        }

        for (int i = 0; i < 4; i++) {

            if (e.getSource() == choices[i] && !answered) {

                answered = true;

                if (i == quiz.getAnswer()) {
                    choices[i].setBackground(Color.GREEN);
                    score++;
                    player.addScore();
                } else {
                    choices[i].setBackground(Color.RED);
                    choices[quiz.getAnswer()].setBackground(Color.GREEN);
                }

                for (JButton b : choices) b.setEnabled(false);
                nextButton.setEnabled(true);
            }
        }

       if (e.getSource() == nextButton) {

            quiz.nextQuestion();

            if (!quiz.hasNextQuestion()) {

                ResultFileHandler.saveResult(
                        playerName,
                        score,
                        quiz.getTotal()
                );

                HighScoreManager.addScore(playerName, score);

                JOptionPane.showMessageDialog(this,
                        """
                        QUIZ FINISHED!
                        Name: """ + playerName + "\n" +
                                "Score: " + score + "/" + quiz.getTotal() + "\n\n" +
                                "HIGH SCORES:\n" +
                                HighScoreManager.getScores()
                );

                score = 0;
                nameField.setText("");

                remove(quizPanel);
                add(frontPanel);
                revalidate();
                repaint();

            } else {
                loadQuestion();
            }
        }
    }

    public static void main(String[] args) {
        new FlashcardApp();
    }
    }
