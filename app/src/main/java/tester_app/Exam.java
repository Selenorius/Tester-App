package tester_app;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.borderColor;
import static tester_app.helpers.Constants.deleteColor;
import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.root;
import static tester_app.helpers.Constants.size;
import static tester_app.helpers.Constants.styleButton;
import static tester_app.helpers.Constants.styleScrollPane;
import static tester_app.helpers.Constants.tab;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import tester_app.helpers.ConsoleErrorJFrame;
import tester_app.helpers.ExamTimer;
import tester_app.helpers.FrameDragListener;
import tester_app.helpers.FrameResizeListener;
import tester_app.helpers.RoundedButton;
import tester_app.helpers.RoundedMenuBar;
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
        titleMenu;
    private JLabel
        iconLabel,
        titleLabel;
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    private RoundedMenuBar menuBar;
    private ExamTimer timer;

    private int
        score,
        currentIndex;
    private ArrayList<Question> questions;
    private final Image
        icon = loadIcon("/tester_app_editorx96.png");
    private final String
        examName,
        settingsFile = "examSettings.txt";

    public Exam(File examFile, Tester tester) {
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        this.score = 0;
        currentIndex = 0;
        examName = examFile.getName().substring(0, examFile.getName().length() - 4);

        String topicName = examFile.getParent();
        if(!topicName.equals(root.getPath())) {
            topicName = topicName.replace(root.getPath(), "").substring(1);
        } else {
            topicName = "Uncategorized";
        }

        this.getContentPane().setIgnoreRepaint(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle(topicName + " - " + examName);
        this.setIconImage(icon);
        this.setMinimumSize(size);
        this.setSize(size);
        this.setLayout(layout);
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
            consoleErrorMessage("UIManager.setLookAndFeel", e1.getMessage());
        }
        this.getContentPane().setBackground(backgroundColor);
        this.addWindowListener(new WindowAdapter() {
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
            public void windowClosed(WindowEvent e) {
                updateSettings();
            }

            @Override
            public void windowStateChanged(WindowEvent e) {
                questions.get(currentIndex).adjustImage();
            }
        });
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                questions.get(currentIndex).adjustImage();
            }
        });
        FrameResizeListener frameResizeListener = new FrameResizeListener(this);
        this.addMouseListener(frameResizeListener);
        this.addMouseMotionListener(frameResizeListener);
        this.getContentPane().setBackground(fieldColor);
        this.setBackground(fieldColor);

        questions = new ArrayList<>();

        iconLabel = new JLabel();
        iconLabel.setIcon(new ImageIcon(icon.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH)));

        titleLabel = new JLabel("No label text found");
        titleLabel.setForeground(Color.WHITE);

        titleMenu = new RoundedPanel();
        titleMenu.setBackground(null);
        titleMenu.setBorderPainted(false);
        titleMenu.setLayout(layout);

        constraints.insets = new Insets(0, 0, 0, margin * 2);

        titleMenu.add(iconLabel, constraints);
        titleMenu.add(titleLabel, constraints);

        RoundedButton minButton = new RoundedButton();
        minButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setExtendedState(ICONIFIED);
            }
        });

        RoundedButton maxButton = new RoundedButton();
        maxButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(getExtendedState() == NORMAL) {
                    setExtendedState(MAXIMIZED_BOTH);
                } else {
                    setExtendedState(NORMAL);
                }
            }
        });

        RoundedButton backButton = new RoundedButton();
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        RoundedPanel space = new RoundedPanel();
        space.setBackground(null);
        space.setBorderPainted(false);

        menuBar = new RoundedMenuBar();
        menuBar.setBorderPainted(false);
        menuBar.setBackground(fieldColor);
        menuBar.setLayout(layout);
        menuBar.add(Box.createHorizontalGlue());
        FrameDragListener frameDragListener = new FrameDragListener(this);
        menuBar.addMouseListener(frameDragListener);
        menuBar.addMouseMotionListener(frameDragListener);
        addMargin(menuBar, margin);

        menuBar.add(titleMenu, constraints);

        constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 10;
        constraints.weighty = 0.5;

        menuBar.add(space, constraints);

        constraints.weightx = 0.5;

        menuBar.add(minButton, constraints);
        menuBar.add(maxButton, constraints);
        menuBar.add(backButton, constraints);

        styleButton(minButton, "Min");
        styleButton(maxButton, "Max");
        styleButton(backButton, "End exam");
        backButton.setBackground(deleteColor);

        questionMenu = new RoundedPanel();
        questionMenu.setBackground(backgroundColor);
        questionMenu.setBorderColor(borderColor);
        questionMenu.setBorderPainted(true);
        questionMenu.setLayout(layout);

        timer = new ExamTimer();

        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = 0;
        constraints.weightx = 0.5;
        constraints.weighty = 0;
        constraints.anchor = GridBagConstraints.NORTHEAST;
        constraints.insets = new Insets(margin * 3, margin * 3, margin * 3, margin * 3);

        questionMenu.add(timer, constraints);

        scrollPane = new JScrollPane(questionMenu);
        scrollPane.getViewport().setBackground(fieldColor);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(64);
        styleScrollPane(scrollPane);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.insets = new Insets(0, margin * 2, margin * 2, margin * 2);

        this.setJMenuBar(menuBar);
        this.add(scrollPane, constraints);

        constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

        loadQuestions(examFile);

        this.setVisible(false);
    }

    @SuppressWarnings("null")
    private void loadQuestions(File f) {
        Question newQuestion = null;
        ButtonOption newButtonOption = null;
        TextOption newTextOption = null;
        String
            text = null,
            qt = "",
            at = "";
        
        try(Scanner settingsFileIn = new Scanner(f)) {
            int layer = 0;
            while(settingsFileIn.hasNextLine()) {
                String line = settingsFileIn.nextLine();
                switch (layer) {
                    case 1:
                        if(line.contains("{")) {
                            line = line.toLowerCase();

                            if(line.contains("true_false") || line.contains("tf")) {
                                newQuestion = new TFQuestion(this);
                                newQuestion.setGoal(1);
                            } else if(line.contains("multiple_choice") || line.contains("mc")) {
                                newQuestion = new MCQuestion(this);
                                
                                if(line.contains("ordered")) {
                                    newQuestion.setOrdered(true);
                                } else {
                                    newQuestion.setOrdered(false);
                                }
                            } else {
                                newQuestion = new WQuestion(this);

                                if(line.contains("ordered")) {
                                    newQuestion.setOrdered(true);
                                } else {
                                    newQuestion.setOrdered(false);

                                    if(line.contains("\"")) {
                                        text = line.substring(line.indexOf("\"") + 1);

                                        newQuestion.setGoal(Integer.parseInt(text.substring(0, text.indexOf("\""))));
                                    }
                                }
                            }

                            ++layer;
                        }
                        else if(line.contains("}")) {
                            --layer;
                        }

                        break;

                    case 2:
                        if(line.contains("{")) {
                            text = null;

                            if(line.contains("\"")) {
                                if(qt != "") {
                                    qt += line.substring(0, line.indexOf("\""));

                                    newQuestion.setQuestionText(qt);

                                    qt = "";
                                } else if(at != "") {
                                    at += line.substring(0, line.indexOf("\""));

                                    newQuestion.setAnswerText(at);

                                    at = "";
                                }

                                text = line.substring(line.indexOf("\"") + 1);

                                if(newQuestion.getQuestionText() == null && text.contains("\"")) {
                                    newQuestion.setQuestionText(text.substring(0, text.indexOf("\"")));
                                    text = text.substring(text.indexOf("\"") + 1);
                                } else if(newQuestion.getAnswerText() == null && text.contains("\"")) {
                                    newQuestion.setAnswerText(text.substring(0, text.indexOf("\"")));
                                    text = text.substring(text.indexOf("\"") + 1);
                                }
                            }

                            if(line.contains("<")) {
                                text = line.substring(line.indexOf("<") + 1);

                                if(newQuestion.getQuestionImage() == null && text.contains(">")) {
                                    newQuestion.setQuestionImage(text.substring(0, text.indexOf(">")));
                                    text = text.substring(text.indexOf(">") + 1);
                                } else if(newQuestion.getAnswerImage() == null && text.contains(">")) {
                                    newQuestion.setAnswerImage(text.substring(0, text.indexOf(">")));
                                    text = text.substring(text.indexOf(">") + 1);
                                }
                            }

                            ++layer;
                        }
                        else if(line.contains("}")) {
                            if(newQuestion.getClass() != TFQuestion.class) {
                                newQuestion.initGoal();
                            }
                            questions.add(newQuestion);

                            --layer;
                        } else {
                            text = null;

                            if(line.contains("\"")) {
                                if(qt != "") {
                                    qt += line.substring(0, line.indexOf("\""));

                                    newQuestion.setQuestionText(qt);

                                    qt = "";
                                } else if(at != "") {
                                    at += line.substring(0, line.indexOf("\""));

                                    newQuestion.setAnswerText(at);

                                    at = "";
                                }

                                text = line.substring(line.indexOf("\"") + 1);

                                if(newQuestion.getQuestionText() == null && text.contains("\"")) {
                                    newQuestion.setQuestionText(text.substring(0, text.indexOf("\"")));
                                    text = text.substring(text.indexOf("\"") + 1);
                                } else if(newQuestion.getAnswerText() == null && text.contains("\"")) {
                                    newQuestion.setAnswerText(text.substring(0, text.indexOf("\"")));
                                    text = text.substring(text.indexOf("\"") + 1);
                                }
                            }

                            if(newQuestion.getQuestionText() == null) {
                                if(text == null) {
                                    qt += line + System.lineSeparator();
                                } else {
                                    qt += text + System.lineSeparator();
                                }
                            } else if(newQuestion.getAnswerText() == null) {
                                if(text == null) {
                                    at += line + System.lineSeparator();
                                } else {
                                    at += text + System.lineSeparator();
                                }
                            }

                            if(line.contains("<")) {
                                text = line.substring(line.indexOf("<") + 1);

                                if(newQuestion.getQuestionImage() == null && text.contains(">")) {
                                    newQuestion.setQuestionImage(text.substring(0, text.indexOf(">")));
                                    text = text.substring(text.indexOf(">") + 1);
                                } else if(newQuestion.getAnswerImage() == null && text.contains(">")) {
                                    newQuestion.setAnswerImage(text.substring(0, text.indexOf(">")));
                                    text = text.substring(text.indexOf(">") + 1);
                                }
                            }
                        }

                        break;
                        
                    case 3:
                        if(line.contains("{")) {
                            if(newQuestion.getClass() == MCQuestion.class) {
                                newButtonOption = new ButtonOption();

                                line = line.toLowerCase();
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
                            line = line.toLowerCase();
                            
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
                                newQuestion.addOption(newButtonOption);
                            } else if(newQuestion.getClass() == WQuestion.class) {
                                newQuestion.addOption(newTextOption);
                            }

                            --layer;
                        }
                        else if(line.contains("\"") || line.contains("<")) {
                            if(line.contains("\"")) {
                                text = line.substring(line.indexOf("\"") + 1);

                                if(text.contains("\"")) {
                                    if(newQuestion.getClass() == WQuestion.class) {
                                        newTextOption.addText(text.substring(0, text.indexOf("\"")));
                                        text = line.substring(line.indexOf("\"") + 1);
                                    } else {
                                        newButtonOption.setText(text.substring(0, text.indexOf("\"")));
                                        text = line.substring(line.indexOf("\"") + 1);
                                    }
                                } else {
                                    consoleErrorMessage("loadQuestions.option.addText", "No closing \".");
                                }
                            }

                            if(line.contains("<")) {
                                text = line.substring(line.indexOf("<") + 1);

                                if(text.contains(">")) {
                                    if(newQuestion.getClass() == MCQuestion.class) {
                                        newButtonOption.setImagePath(text.substring(0, text.indexOf(">")));
                                        text = line.substring(line.indexOf(">") + 1);
                                    }
                                } else {
                                    consoleErrorMessage("loadQuestions.option.addImage", "No closing >.");
                                }
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

    public void addQuestion(Question question) {
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.anchor = GridBagConstraints.SOUTH;
        constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

        questionMenu.add(question, constraints);
    }

    public void start() {
        Collections.shuffle(questions);

        for(Question q : questions) {
            if(q.getClass() == MCQuestion.class) {
                q.shuffle();
            }
        }

        Question currentQuestion = questions.get(0);

        addQuestion(currentQuestion);

        titleLabel.setText(examName + " | Question " + (currentIndex + 1) + "/" + questions.size() + "  |  Score: " + score);

        setVisible(true);
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

    public void saveToFile(String filePath, File parent) {
        String out = "{" + System.lineSeparator();
        for(Question q : questions) {
            String
            type = "",
            ordered = "",
            goal = "",
            text = "\"" + q.getQuestionText() + "\" ",
            answer = "\"" + q.getAnswerText() + "\" ",
            qImage = "<" + q.getQuestionImage() + "> ",
            aImage = "<" + q.getAnswerImage() + "> ",
            options = "";

            if(q.getClass() == MCQuestion.class) {
                type = "MULTIPLE_CHOICE ";

                if(q.isOrdered()) {
                    ordered = "ORDERED ";
                }
            } else if(q.getClass() == TFQuestion.class) {
                type = "TRUE_FALSE ";
            } else {
                type = "WRITTEN ";

                if(q.isOrdered()) {
                    ordered = "ORDERED ";
                } else {
                    goal = "\"" + q.getGoal() + "\" ";
                }
            }

            if(q.getQuestionText() == null) {
                text = "";
            } else if(q.getQuestionText().isBlank()) {
                text = "";
            }
            if(q.getAnswerText() == null) {
                answer = "";
            } else if(q.getAnswerText().isBlank()) {
                answer = "";
            }
            if(q.getQuestionImage() == null) {
                qImage = "";
            } else if(q.getQuestionImage().isBlank()) {
                qImage = "";
            }
            if(q.getAnswerImage() == null) {
                aImage = "";
            } else if(q.getAnswerImage().isBlank()) {
                aImage = "";
            }

            if(q.getClass() == MCQuestion.class) {
                String
                    optionText,
                    optionImage;

                for(ButtonOption o : q.getButtonOptions()) {
                    optionText = o.getText();
                    optionImage = o.getImagePath();

                    if(optionText != null) {
                        if(optionText == "null" || optionText.isBlank()) {
                            optionText = "";
                        } else {
                            optionText = "\"" + optionText + "\" ";
                        }
                    } else {
                        optionText = "";
                    }
                    if(optionImage != null) {
                        if(optionImage == "null" || optionImage.isBlank()) {
                            optionImage = "";
                        } else {
                            optionImage = "<" + optionImage + ">";
                        }
                    } else {
                        optionImage = "";
                    }

                    options += tab(3) + o.isTrue() + " {" + System.lineSeparator();
                    options += tab(4) + optionText + optionImage + System.lineSeparator();
                    options += tab(3) + "}" + System.lineSeparator();
                }
            } else if(q.getClass() == WQuestion.class) {
                for(TextOption o : q.getTextOptions()) {
                    options += tab(3) + "{" + System.lineSeparator();
                    options += o.getProperText();
                    options += tab(3) + "}" + System.lineSeparator();
                }
            } else {
                options += tab(3) + q.getButtonOptions().get(0).isTrue() + System.lineSeparator();
            }

            out += tab(1) + type + ordered + goal + "{" + System.lineSeparator();
            out += tab(2) + text + answer + qImage + aImage + "{" + System.lineSeparator();
            out += options;
            out += tab(2) + "}" + System.lineSeparator();
            out += tab(1) + "}" + System.lineSeparator();
        }
        out += "}";

        try {
            FileWriter fileOut = new FileWriter(new File(parent + "/" + filePath + ".txt"));
            fileOut.write(out);
            fileOut.close();
        } catch (Exception e1) {
            consoleErrorMessage("saveToFile", e1.getMessage());
        }
    }

    public void incrementScore() {
        ++score;
    }

    public void next() {
        questionMenu.remove(questions.get(currentIndex++));

        Question question = questions.get(currentIndex);

        addQuestion(question);

        if(question.getClass() == WQuestion.class) {
            question.getTextArea().requestFocus();
        }

        titleLabel.setText(examName + " | Question " + (currentIndex + 1) + "/" + questions.size() + "  |  Score: " + score);
    }

    public void finish() {
        timer.stop();

        String finishText = "You passed your exam with " + score + " points!";
        if(score < (int) (questions.size() * 0.6) || score == 0) {
            if(score == 1) {
                finishText = "You failed your exam with " + score + " point!";
            } else {
                finishText = "You failed your exam with " + score + " points!";
            }
        } else if(score == questions.size()) {
            if(score == 1) {
                finishText = "You passed your exam with a perfect score of " + score + " point!";
            } else {
                finishText = "You passed your exam with a perfect score of " + score + " points!";
            }
        } else if(score == 1) {
            finishText = "You passed your exam with " + score + " point!";
        }

        JOptionPane.showMessageDialog(
            this,
            finishText + System.lineSeparator() +
            "Time: " + timer.getTime(),
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

    public String getName() {
        return examName;
    }

    // SETTERS
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setQuestions(ArrayList<Question> newQuestions) {
        questions = newQuestions;
    }
}
