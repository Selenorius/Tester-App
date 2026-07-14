package tester_app;

import static tester_app.helpers.Constants.*;

import java.util.Scanner;

/*
    Decription: Create and solve your own questions to prepare for exams.
    Author: https://github.com/Selenorius
*/

public class App {
    public static void main(String[] args) {
        System.out.println(
            ANSI_PURPLE + "tester_app.App" + ANSI_RESET + System.lineSeparator() +
            tab() + "Started App." + System.lineSeparator()
        );

        Scanner in = new Scanner(System.in);
        Tester tester = new Tester();

        tester.start();

        System.out.print(ANSI_CYAN + "Enter Command: " + ANSI_RESET);
        tester.read(in.nextLine());

        in.close();
    }
}
