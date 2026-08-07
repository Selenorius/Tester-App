package tester_app.editors;

import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.name;
import static tester_app.helpers.Constants.root;
import static tester_app.helpers.Constants.size;
import static tester_app.helpers.Constants.styleScrollPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import tester_app.Tester;
import tester_app.helpers.ConsoleErrorJFrame;
import tester_app.helpers.DirectoryRestrictedFileSystemView;
import tester_app.helpers.RoundedPanel;

public class TextEditor extends ConsoleErrorJFrame implements ActionListener {
    private RoundedPanel textPanel;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JSpinner fontSizeSpinner;
    private JComboBox<String> fontBox;
    private JMenuBar menuBar;
    private JMenu
        fileMenu,
        fontSizeMenu,
        fontMenu;
    private JMenuItem
        openItem,
        saveItem,
        clearItem;

    private final Dimension SpinnerSize = new Dimension(51, 20);
    private final Image icon = loadIcon("/tester_app_editorx96.png");
    private String
        defaultFont = "Courier New",
        settingsFile = "editorSettings.txt";
    private int defaultFontSize = 18;

    private Tester tester;

    public TextEditor(Tester testerIn) {
        tester = testerIn;

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setTitle(name + " Editor");
        this.setIconImage(icon);
        this.setMinimumSize(size);
        this.setSize(size);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);
        this.getContentPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension editorSize = getEditorSize();

                textPanel.setPreferredSize(editorSize);
                scrollPane.setPreferredSize(new Dimension(editorSize.width - margin, editorSize.height - margin * 5));
            }
        });
        this.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                Dimension editorSize = getEditorSize();

                textPanel.setPreferredSize(editorSize);
                scrollPane.setPreferredSize(new Dimension(editorSize.width - margin, editorSize.height - margin * 5));
            }
        });
        this.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
                File settings = new File(settingsFile);

                if(settings.isFile()) {
                    try(Scanner settingsFileIn = new Scanner(settings)) {
                        int i = 0;
                        while(settingsFileIn.hasNextLine()) {
                            String data = settingsFileIn.nextLine();
                            switch(i++) {
                                case 0:
                                    try {
                                        if(data != "") {
                                            setExtendedState(Integer.parseInt(data));
                                        }
                                    }
                                    catch (NumberFormatException e1) {
                                        consoleErrorMessage("setExtendedState", e1.getMessage());
                                    }
                                    break;
                                case 1:
                                    try {
                                        if(data != "") {
                                            fontSizeSpinner.setValue(Integer.parseInt(data));
                                            textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, Integer.parseInt(data)));
                                        }
                                    }
                                    catch (NumberFormatException e1) {
                                        consoleErrorMessage("setFontSize", e1.getMessage());
                                    }
                                    break;
                                case 2:
                                    try {
                                        if(data != "") {
                                            fontBox.setSelectedItem(data);
                                            textArea.setFont(new Font(data, Font.PLAIN, textArea.getFont().getSize()));
                                        }
                                    }
                                    catch (NumberFormatException e1) {
                                        consoleErrorMessage("setFont", e1.getMessage());
                                    }
                                    break;
                                default:
                                    break;
                            }
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
            public void windowClosing(WindowEvent e) {
                if(textArea.getText().length() != 0) {
                    confirmCloseDialog();
                } else {
                    dispose();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                try {
                    deleteEmptyFiles(root);
                } catch (IOException e1) {
                    consoleErrorMessage("deleteEmptyFiles", e1.getMessage());
                }
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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e1) {
            consoleErrorMessage("UIManager.setLookAndFeel", e1.getMessage());
        }
        this.getContentPane().setBackground(backgroundColor);
        
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font(defaultFont, Font.PLAIN, defaultFontSize));
        textArea.setBackground(fieldColor);
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(Color.WHITE);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(getEditorSize().width - margin, getEditorSize().height - margin * 5));
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        styleScrollPane(scrollPane);

        textPanel = new RoundedPanel();
        textPanel.setSize(getEditorSize());
        textPanel.setBackground(fieldColor);
        textPanel.add(scrollPane);

        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(SpinnerSize);
        fontSizeSpinner.setValue(defaultFontSize);
        fontSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN,(int) fontSizeSpinner.getValue()));
            }
        });

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        fontBox = new JComboBox<String>(fonts);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem(defaultFont); // doesn't work anymore for some reason
        fontBox.setToolTipText("Change font");

        // Menu Bar
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        fontSizeMenu = new JMenu("Text Size");
        fontMenu = new JMenu("Font");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        clearItem = new JMenuItem("Clear");

        openItem.addActionListener(this);
        openItem.setToolTipText("Open existing file");
        saveItem.addActionListener(this);
        saveItem.setToolTipText("Save current file");
        clearItem.addActionListener(this);
        clearItem.setToolTipText("Clear text area");

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(clearItem);

        fontSizeMenu.add(fontSizeSpinner);
        fontSizeMenu.setToolTipText("Change font size");

        fontMenu.add(fontBox);

        menuBar.add(fileMenu);
        menuBar.add(fontSizeMenu);
        menuBar.add(fontBox);

        this.setJMenuBar(menuBar);
        this.add(textPanel);
        this.setVisible(true);

        scrollPane.setPreferredSize(getEditorSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == fontBox) {
            textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
        }

        if(e.getSource() == openItem) {
            try {
                FileSystemView fsv = new DirectoryRestrictedFileSystemView(root);
                JFileChooser fileChooser = new JFileChooser(fsv.getHomeDirectory(), fsv);
                fileChooser.setCurrentDirectory(root);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
                fileChooser.setFileFilter(filter);

                int response = fileChooser.showOpenDialog(null);

                if(response == JFileChooser.APPROVE_OPTION) {
                    File file;
                    Scanner fileIn = null;

                    file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    try {
                        fileIn = new Scanner(file);
                        String line;
                        while(fileIn.hasNextLine()) {
                            line = fileIn.nextLine() + System.lineSeparator();
                            textArea.append(line);
                        }
                    } catch (FileNotFoundException e1) {
                        consoleErrorMessage("actionPerformed.openItem", e1.getMessage());

                        errorMessage("Invalid file.");
                        response = fileChooser.showOpenDialog(null);
                    } finally {
                        if(fileIn != null) {
                            fileIn.close();
                        }
                    }
                }
            } catch(Exception err) {
                consoleErrorMessage(err);
            }
        }
        if(e.getSource() == saveItem) {
            try {
                FileSystemView fsv = new DirectoryRestrictedFileSystemView(root);
                JFileChooser fileChooser = new JFileChooser(fsv.getHomeDirectory(), fsv);
                fileChooser.setCurrentDirectory(root);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
                fileChooser.setFileFilter(filter);

                int response = fileChooser.showSaveDialog(null);

                if(response == JFileChooser.APPROVE_OPTION) {
                    if(fileChooser.getSelectedFile().exists()) {
                        response = JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to overwrite the existing file?",
                            "Warning",
                            JOptionPane.YES_NO_OPTION
                        );
                    } else {
                        response = JOptionPane.YES_OPTION;
                    }
                    
                    if (response == JOptionPane.YES_OPTION) {
                        try {
                            FileWriter fileOut = new FileWriter(fileChooser.getSelectedFile().getAbsolutePath());
                            fileOut.write(textArea.getText());
                            fileOut.close();
                        } catch (Exception e1) {
                            consoleErrorMessage("actionPerformed.saveItem", e1.getMessage());
                        }
                    }
                }
            } catch(Exception err) {
                consoleErrorMessage(err);
            }
        }
        if(e.getSource() == clearItem) {
            textArea.setText("");
        }
    }

    private static void deleteEmptyFiles(File file) throws IOException {
        for (File subFile : file.listFiles()) {

            if (subFile.isDirectory()) {
                deleteEmptyFiles(subFile);
            }

            if(isEmpty(subFile) || subFile.length() == 0) subFile.delete();
        }
    }

    private static boolean isEmpty(File dir) throws IOException {
        Path path = Paths.get(dir.getAbsolutePath());

        if (Files.isDirectory(path)) {
            try (Stream<Path> entries = Files.list(path)) {
                return !entries.findFirst().isPresent();
            }
        }
            
        return false;
    }

    private void updateSettings() {
        try {
            FileWriter settingsFileOut = new FileWriter(settingsFile);
            settingsFileOut.write(
                getExtendedState() + System.lineSeparator() +
                fontSizeSpinner.getValue() + System.lineSeparator() +
                fontBox.getSelectedItem()
            );
            settingsFileOut.close();
        } catch (Exception e1) {
            consoleErrorMessage("updateSettings", e1.getMessage());
        }
    }

    private void errorMessage(String text) {
        JOptionPane.showMessageDialog(this, text, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void confirmCloseDialog() {
        int response = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to close this window? All unsaved data will be lost.",
            "Warning",
            JOptionPane.YES_NO_OPTION
        );
        if (response == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }

    private Dimension getEditorSize() {
        return new Dimension(this.getSize().width - 28, this.getSize().height - 72);
    }
}
