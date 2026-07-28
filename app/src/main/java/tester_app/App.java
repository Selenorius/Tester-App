package tester_app;

import static tester_app.helpers.ReleaseManager.manageReleases;

/*
    Decription: Create and solve your own questions to prepare for exams.
    Author: https://github.com/Selenorius
*/

public class App {
    public static void main(String[] args) {
        Tester tester = new Tester();

        manageReleases();

        //tester.start();
        //tester.read();

        tester.startEditor();
    }
}
