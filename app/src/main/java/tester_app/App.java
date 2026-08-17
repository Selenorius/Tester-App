package tester_app;

import javax.swing.SwingUtilities;

/*
    Decription: Create and solve your own questions to prepare for exams.
    Author: https://github.com/Selenorius
*/

public class App implements Runnable {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new App());
    }

    @Override
    public void run() {
        Tester tester = new Tester();
        
        tester.start();
    }
}
