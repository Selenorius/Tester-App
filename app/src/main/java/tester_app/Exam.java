package tester_app;

import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.root;
import static tester_app.helpers.Constants.size;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import tester_app.helpers.ConsoleErrorJFrame;
import tester_app.helpers.RoundedPanel;
import tester_app.options.ButtonOption;
import tester_app.options.TextOption;
import tester_app.questions.MCQuestion;
import tester_app.questions.Question;
import tester_app.questions.TFQuestion;
import tester_app.questions.WQuestion;

public class Exam extends ConsoleErrorJFrame {
    private JScrollPane scrollPane;
    private RoundedPanel
        questionMenu,
        scoreMenu;
    private JLabel scoreLabel;

    private int
        score,
        currentIndex;
    private ArrayList<Question> questions;
    private final Image
        icon = loadIcon("/fileButtonx32.png");
    private final String settingsFile = "examSettings.txt";

    public Exam(File examFile, Tester tester) {
        this.score = 0;
        currentIndex = 0;
        String
            examName = examFile.getName().substring(0, examFile.getName().length() - 4),
            topicName = examFile.getParent().substring(7);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle(topicName + " - " + examName);
        this.setIconImage(icon);
        this.setMinimumSize(size);
        this.setSize(size);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e1) {
            consoleErrorMessage("UIManager.setLookAndFeel", e1.getMessage());
        }
        this.getContentPane().setBackground(backgroundColor);
        this.getContentPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension testerSize = getTesterSize();

                scrollPane.setPreferredSize(testerSize);
                for(Question q : questions) {
                    q.setInputAreaSize(testerSize.width, testerSize.height);
                }
            }
        });
        this.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                Dimension testerSize = getTesterSize();

                scrollPane.setPreferredSize(testerSize);
                for(Question q : questions) {
                    q.setInputAreaSize(testerSize.width, testerSize.height);
                }
            }
        });
        this.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
                File settings = new File(settingsFile);

                if(settings.isFile()) {
                    try(Scanner settingsFileIn = new Scanner(settings)) {
                        String data = settingsFileIn.nextLine();
                        if(data != "") {
                            setExtendedState(Integer.parseInt(data));
                        }
                    } catch (Exception e1) {
                        consoleErrorMessage("windowOpened.Scanner", e1.getMessage());
                    }
                }

                if(!root.exists()) {
                    root.mkdir();
                }
            }

            @Override
            public void windowClosing(WindowEvent e) {}

            @Override
            public void windowClosed(WindowEvent e) {
                updateSettings();
                tester.start();
            }

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
            
        });

        questions = new ArrayList<>();

        scoreLabel = new JLabel("Question " + (currentIndex + 1) + "  |  Score: " + score);
        scoreLabel.setForeground(Color.WHITE);

        scoreMenu = new RoundedPanel();
        scoreMenu.setBackground(fieldColor);
        scoreMenu.add(scoreLabel);

        questionMenu = new RoundedPanel();
        questionMenu.setBackground(fieldColor);
        questionMenu.setLayout(new BoxLayout(questionMenu, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(questionMenu);
        scrollPane.getViewport().setBackground(backgroundColor);
        scrollPane.setBorder(null);
        scrollPane.setSize(getTesterSize());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        this.add(scoreMenu);
        this.add(scrollPane);

        loadQuestions(examFile);

        this.setVisible(true);

        start();
    }

    @SuppressWarnings("null")
    private void loadQuestions(File f) {
        Question newQuestion = null;
        ButtonOption newButtonOption = null;
        TextOption newTextOption = null;
        String
            text = null;
        
        try(Scanner settingsFileIn = new Scanner(f)) {
            int layer = 0;
            while(settingsFileIn.hasNextLine()) {
                String line = settingsFileIn.nextLine();
                switch (layer) {
                    case 1:
                        if(line.contains("{")) {
                            line.toLowerCase();

                            if(line.contains("true_false") || line.contains("tf")) {
                                newQuestion = new TFQuestion(this);
                                newQuestion.setGoal(1);
                            } else if(line.contains("multiple_choice") || line.contains("mc")) {
                                if(line.contains("ordered") || line.contains("o")) {
                                    newQuestion = new MCQuestion(true);
                                } else {
                                    newQuestion = new MCQuestion(this);
                                }
                            } else {
                                newQuestion = new WQuestion(this);
                            }

                            ++layer;
                        }
                        else if(line.contains("}")) {
                            --layer;
                        }

                        break;

                    case 2:
                        if(line.contains("{")) {
                            ++layer;
                        }
                        else if(line.contains("}")) {
                            if(newQuestion.getClass() != TFQuestion.class) {
                                newQuestion.initGoal();
                            }
                            questions.add(newQuestion);

                            --layer;
                        }
                        else if(line.contains("\"")) {
                            text = line.substring(line.indexOf("\"") + 1);

                            if(newQuestion.getQuestionText() == null && text.contains("\"")) {
                                newQuestion.setQuestionText(text.substring(0, text.indexOf("\"")));
                                text = text.substring(text.indexOf("\"") + 1);
                            }
                            if(newQuestion.getAnswerText() == null && text.contains("\"")) {
                                newQuestion.setAnswerText(text.substring(0, text.indexOf("\"")));
                                text = text.substring(text.indexOf("\"") + 1);
                            }
                            if(newQuestion.getQuestionImage() == null && text.contains("\"")) {
                                newQuestion.setQuestionImage(loadIcon(text.substring(0, text.indexOf("\""))));
                                text = text.substring(text.indexOf("\"") + 1);
                            }
                            if(newQuestion.getAnswerImage() == null && text.contains("\"")) {
                                newQuestion.setAnswerImage(loadIcon(text.substring(0, text.indexOf("\""))));
                            }
                        }

                        break;

                    case 3:
                        if(line.contains("{")) {
                            if(newQuestion.getClass() == MCQuestion.class) {
                                newButtonOption = new ButtonOption();

                                line.toLowerCase();
                                if(line.contains("true")) {
                                    newButtonOption.setValue(true);
                                } else if(line.contains("false")) {
                                    newButtonOption.setValue(false);
                                } else {
                                    consoleErrorMessage("loadQuestions.newButtonOption.setValue",  "No value for Option.");
                                }
                            } else if(newQuestion.getClass() == WQuestion.class) {
                                newTextOption = new TextOption();
                            }

                            ++layer;
                        }
                        else if(line.contains("}")) {
                            --layer;
                        } else if(newQuestion.getClass() == TFQuestion.class) {
                            line.toLowerCase();
                            
                            if(line.contains("true")) {
                                newButtonOption = new ButtonOption();
                                newButtonOption.setText("True");
                                newButtonOption.setValue(true);
                                newQuestion.addOption(newButtonOption);

                                newButtonOption = new ButtonOption();
                                newButtonOption.setText("False");
                                newButtonOption.setValue(false);
                                newQuestion.addOption(newButtonOption);
                            } else if(line.contains("false")) {
                                newButtonOption = new ButtonOption();
                                newButtonOption.setText("True");
                                newButtonOption.setValue(false);
                                newQuestion.addOption(newButtonOption);

                                newButtonOption = new ButtonOption();
                                newButtonOption.setText("False");
                                newButtonOption.setValue(true);
                                newQuestion.addOption(newButtonOption);
                            } else {
                                consoleErrorMessage("loadQuestions.newQuestion.addOption",  "No value for TFQuestion.");
                            }
                        }
                        
                        break;

                    case 4:
                        if(line.contains("}")) {
                            if(newQuestion.getClass() == MCQuestion.class) {
                                newButtonOption.setText(text);
                                newQuestion.addOption(newButtonOption);
                            } else if(newQuestion.getClass() == WQuestion.class) {
                                newTextOption.reverseText();
                                newQuestion.addOption(newTextOption);
                            }

                            --layer;
                        }
                        else if(line.contains("\"")) {
                            text = line.substring(line.indexOf("\"") + 1);

                            if(text.contains("\"")) {
                                if(newQuestion.getClass() == WQuestion.class) {
                                    newTextOption.addText(text.substring(0, text.indexOf("\"")));
                                } else {
                                    newButtonOption.setText(text.substring(0, text.indexOf("\"")));
                                }
                            } else {
                                consoleErrorMessage("loadQuestions.newTextOption.addText", "No closing \".");
                            }
                        }
                        break;

                    default:
                        if(line.contains("{")) ++layer;
                        else if(line.contains("}")) --layer;

                        break;
                }
            }
        } catch (Exception e1) {
            consoleErrorMessage(e1);
        }
    }

    private void start() {
        Collections.shuffle(questions);

        Question currentQuestion = questions.get(0);

        if(currentQuestion.getClass() == MCQuestion.class) {
            if(!currentQuestion.isOrdered()) {
                currentQuestion.shuffle();
            }
        }

        questionMenu.add(currentQuestion);
    }

    private Dimension getTesterSize() {
        return new Dimension(this.getSize().width - 28, this.getSize().height - 48 - 39);
    }

    private void updateSettings() {
        try {
            FileWriter settingsFileOut = new FileWriter(settingsFile);
            settingsFileOut.write(
                getExtendedState() + System.lineSeparator()
            );
            settingsFileOut.close();
        } catch (Exception e1) {
            consoleErrorMessage("updateSettings", e1.getMessage());
        }
    }

    public void incrementScore() {
        ++score;
    }

    public void next() {
        questionMenu.remove(questions.get(currentIndex++));

        Question question = questions.get(currentIndex);

        questionMenu.add(question);

        if(question.getClass() == WQuestion.class) {
            question.getTextArea().requestFocus();
        }

        scoreLabel.setText("Question " + (currentIndex + 1) + "  |  Score: " + score);
    }

    public void finish() {
        String finishText = "You passed your exam with " + score + " points!";

        if(score < (int) (questions.size() * 0.6)) {
            finishText = "You failed your exam with " + score + " points!";
        } else if(score == questions.size()) {
            finishText = "You passed your exam with a perfect score of " + score + " points!";
        }

        JOptionPane.showMessageDialog(
            this,
            finishText,
            "Exam finished!",
            JOptionPane.PLAIN_MESSAGE
        );

        this.dispose();
    }

    // GETTERS
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    // SETTERS
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }
}
