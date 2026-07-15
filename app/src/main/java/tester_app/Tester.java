package tester_app;

import static tester_app.helpers.Constants.ANSI_CYAN;
import static tester_app.helpers.Constants.ANSI_PURPLE;
import static tester_app.helpers.Constants.ANSI_RED;
import static tester_app.helpers.Constants.ANSI_RESET;
import static tester_app.helpers.Constants.back;
import static tester_app.helpers.Constants.name;
import static tester_app.helpers.Constants.publish;
import static tester_app.helpers.Constants.quit;
import static tester_app.helpers.Constants.tab;
import static tester_app.helpers.Constants.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class Tester {
    public static int
        release,
        serial,
        modified_d,
        modified_m,
        modified_y;

    public Tester() {
        System.out.println(ANSI_PURPLE + "Tester" + ANSI_RESET);
        File versionFile = new File("version.txt");

        if (!versionFile.exists()) {
            try {
                if (versionFile.createNewFile()) {
                    try {
                        FileWriter versionFileOut = new FileWriter("version.txt");
                        versionFileOut.write(
                            0 + System.lineSeparator() +
                            0 + System.lineSeparator() +
                            0 + System.lineSeparator() +
                            0 + System.lineSeparator() +
                            0
                        );
                        versionFileOut.close();
                        System.out.println(System.lineSeparator() + "Successfully updated to " + ANSI_PURPLE + "v" + release + '.' + serial + '.' + modified_d + '.' + modified_m + '.' + modified_y + ANSI_RESET + System.lineSeparator());
                    } catch (IOException e) {
                        System.out.println(tab() + ANSI_RED + "File writing error." + ANSI_RESET);
                        e.printStackTrace();
                    }
                    
                    System.out.println(tab() + "Successfully created new version file.");
                }
            } catch (IOException e) {
                System.out.println(tab() + ANSI_RED + "Failed to create version file." + ANSI_RESET);
                e.printStackTrace();
            }
        }

        try (Scanner versionFileIn = new Scanner(versionFile)) {
            int i = 0;
            while (versionFileIn.hasNextLine()) {
                String data = versionFileIn.nextLine();
                switch(i++) {
                    case 0:
                        try {
                            release = Integer.parseInt(data);
                        }
                        catch (NumberFormatException e) {
                            release = 0;
                        }
                        break;
                    case 1:
                        try {
                            serial = Integer.parseInt(data);
                        }
                        catch (NumberFormatException e) {
                            serial = 0;
                        }
                        break;
                    case 2:
                        try {
                            modified_d = Integer.parseInt(data);
                        }
                        catch (NumberFormatException e) {
                            modified_d = 0;
                        }
                        break;
                    case 3:
                        try {
                            modified_m = Integer.parseInt(data);
                        }
                        catch (NumberFormatException e) {
                            modified_m = 0;
                        }
                        break;
                    case 4:
                        try {
                            modified_y = Integer.parseInt(data);
                        }
                        catch (NumberFormatException e) {
                            modified_y = 0;
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(tab() + ANSI_RED + "File reading error." + ANSI_RESET);
            e.printStackTrace();
        }
        
        System.out.println(tab() + "Created Tester." + System.lineSeparator());
    }

    //READ
    public int read() {
        System.out.print(ANSI_CYAN + "Your answer: " + ANSI_RESET);
        Scanner in = new Scanner(System.in);
        LocalDate timestamp = LocalDate.now();
        String s = in.nextLine();

        in.close();

        s.toLowerCase();
        s.trim();

        if(back(s)) {
            System.out.println();
            return 0;
        } else if(quit(s)) {
            System.out.println();
            return -1;
        }
        else if(update(s)) {
            System.out.println(System.lineSeparator() + ANSI_PURPLE + "Update" + ANSI_RESET);

            modified_d = timestamp.getDayOfMonth();
            modified_m = timestamp.getMonthValue();
            modified_y = timestamp.getYear();

            try {
                FileWriter versionFileOut = new FileWriter("version.txt");
                versionFileOut.write(
                    release + System.lineSeparator() +
                    ++serial + System.lineSeparator() +
                    modified_d + System.lineSeparator() +
                    modified_m + System.lineSeparator() +
                    modified_y
                );
                versionFileOut.close();
                System.out.println(tab() + "Successfully updated to " + ANSI_PURPLE + "v" + release + '.' + serial + '.' + modified_d + '.' + modified_m + '.' + modified_y + ANSI_RESET);
            } catch (IOException e) {
                System.out.println(tab() + "File writing error." + ANSI_RESET);
                e.printStackTrace();
            }
            System.out.println();

            return -1;
        }
        else if(publish(s)) {
            System.out.println(ANSI_PURPLE + "Publish" + ANSI_RESET + System.lineSeparator());

            serial = 0;
            modified_d = timestamp.getDayOfMonth();
            modified_m = timestamp.getMonthValue();
            modified_y = timestamp.getYear();

            try {
                FileWriter versionFileOut = new FileWriter("version.txt");
                versionFileOut.write(
                    ++release + System.lineSeparator() +
                    0 + System.lineSeparator() +
                    modified_d + System.lineSeparator() +
                    modified_m + System.lineSeparator() +
                    modified_y
                );
                versionFileOut.close();
                System.out.println(tab() + "Successfully published to " + ANSI_PURPLE + 'v' + release + '.' + serial + '.' + modified_d + '.' + modified_m + '.' + modified_y + ANSI_RESET);
            } catch (IOException e) {
                System.out.println(tab() + ANSI_RED + "File writing error." + ANSI_RESET);
                e.printStackTrace();
            }
            System.out.println();

            return -1;
        }
        System.out.println();

        return 1;
    }

    public void start() {
        //System.out.println(ANSI_PURPLE + "--- " + ANSI_RESET + "Welcome to " + name + '!' + ANSI_PURPLE + " ---" + ANSI_RESET + System.lineSeparator());
    }
}
