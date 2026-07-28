package tester_app.editors;

import static tester_app.helpers.Constants.ANSI_PURPLE;
import static tester_app.helpers.Constants.ANSI_RED;
import static tester_app.helpers.Constants.ANSI_RESET;
import static tester_app.helpers.Constants.name;
import static tester_app.helpers.Constants.tab;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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

public class TextEditor extends JFrame implements ActionListener {
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JSpinner fontSizeSpinner;
    private JComboBox<String> fontBox;
    private String defaultFont = "Segoe UI Semibold";
    private int defaultFontSize = 20;
    private File root = new File("./topics");

    private JMenuBar menuBar;
    private JMenu
        fileMenu,
        fontSizeMenu,
        fontMenu;
    private JMenuItem
        openItem,
        saveItem,
        clearItem,
        exitItem;

    private final Dimension size = new Dimension(600, 600),
        SpinnerSize = new Dimension(62, 25);

    private Tester tester;

    public TextEditor(Tester testerIn) {
        tester = testerIn;

        BufferedImage icon = loadImg("tester_app/favicon.ico");

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle(name);
        this.setIconImage(icon);
        this.setMinimumSize(size);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.addPropertyChangeListener(null);
        this.getContentPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scrollPane.setPreferredSize(getEditorSize());
            }
        });
        this.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                scrollPane.setPreferredSize(getEditorSize());
            }
        });
        this.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'windowOpened'");
            }

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    deleteEmptyDirectories(root);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'windowClosed'");
            }

            @Override
            public void windowIconified(WindowEvent e) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'windowIconified'");
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'windowDeiconified'");
            }

            @Override
            public void windowActivated(WindowEvent e) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'windowActivated'");
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'windowDeactivated'");
            }
            
        });
        
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font(defaultFont, Font.PLAIN, defaultFontSize));

        scrollPane  = new JScrollPane(textArea);
        scrollPane.setSize(getEditorSize());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(SpinnerSize);
        fontSizeSpinner.setToolTipText("Change font size");
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
        fontBox.setSelectedItem(defaultFont);

        // Menu Bar
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        fontSizeMenu = new JMenu("Text Size");
        fontMenu = new JMenu("Font");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        clearItem = new JMenuItem("Clear");
        exitItem = new JMenuItem("Exit to Tester");

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        clearItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(clearItem);
        fileMenu.add(exitItem);

        fontSizeMenu.add(fontSizeSpinner);

        fontMenu.add(fontBox);

        menuBar.add(fileMenu);
        menuBar.add(fontSizeMenu);
        menuBar.add(fontBox);

        this.setJMenuBar(menuBar);
        this.add(scrollPane);
        this.setVisible(true);

        scrollPane.setPreferredSize(getEditorSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == fontBox) {
            textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
        }

        if(e.getSource() == openItem) {
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
                    if(file.isFile()) {
                        String line;
                        while(fileIn.hasNextLine()) {
                            line = fileIn.nextLine() + System.lineSeparator();
                            textArea.append(line);
                        }
                    }
                } catch (FileNotFoundException e1) {
                    System.out.println(ANSI_PURPLE + "Open Dialog");
                    System.out.print(ANSI_RED + tab());
                    e1.printStackTrace();
                    System.out.println(ANSI_RESET);
                } finally {
                    if(fileIn != null) {
                        fileIn.close();
                    }
                }
            }
        }
        if(e.getSource() == saveItem) {
            FileSystemView fsv = new DirectoryRestrictedFileSystemView(root);
            JFileChooser fileChooser = new JFileChooser(fsv.getHomeDirectory(), fsv);
            fileChooser.setCurrentDirectory(root);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
            fileChooser.setFileFilter(filter);
            fileChooser.setFont(new Font(defaultFont, Font.PLAIN, defaultFontSize));
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | UnsupportedLookAndFeelException e1) {
                e1.printStackTrace();
            }

            int response = fileChooser.showSaveDialog(null);

            if(response == JFileChooser.APPROVE_OPTION) {
                File file;
                PrintWriter fileOut;

                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    fileOut = new PrintWriter(file);
                    fileOut.println(textArea.getText());
                } catch (FileNotFoundException e1) {
                    System.out.println(ANSI_PURPLE + "Save Dialog");
                    System.out.print(ANSI_RED + tab());
                    e1.printStackTrace();
                    System.out.println(ANSI_RESET);
                }
            }
        }
        if(e.getSource() == clearItem) {
            textArea.setText("");
        }
        if(e.getSource() == exitItem) {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            tester.start();
        }
    }

    private static BufferedImage loadImg(final String path) {
        final InputStream is = TextEditor.class.getClassLoader().getResourceAsStream(path);
        BufferedImage img = null;

        try {
            img = ImageIO.read(is);
        } catch (final Exception e) {
            System.out.println(ANSI_PURPLE + "Load Image");
            System.out.print(ANSI_RED + tab());
            e.printStackTrace();
            System.out.println(ANSI_RESET);
        }

        return img;
    }

    private static void deleteEmptyDirectories(File file) throws IOException {
        for (File subDir : file.listFiles()) {

            if (subDir.isDirectory()) {
                deleteEmptyDirectories(subDir);
            }

            if(isEmpty(subDir)) subDir.delete();
        }
    }

    public static boolean isEmpty(File dir) throws IOException {
        Path path = Paths.get(dir.getAbsolutePath());

        if (Files.isDirectory(path)) {
            try (Stream<Path> entries = Files.list(path)) {
                return !entries.findFirst().isPresent();
            }
        }
            
        return false;
}

    private Dimension getEditorSize() {
        return new Dimension(this.getSize().width - 25, this.getSize().height - 75);
    }
}
