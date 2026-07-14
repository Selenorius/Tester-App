package tester_app;

import static tester_app.helpers.Constants.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class Tester {
    protected static int
        release,
        serial,
        modified_d,
        modified_m,
        modified_y;

    public Tester() {
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
                        System.out.println(ANSI_RED + this + ": File writing error." + ANSI_RESET + System.lineSeparator());
                        e.printStackTrace();
                    }
                    
                    System.out.println(this + ": Successfully created new version file.");
                }
            } catch (IOException e) {
                System.out.println(ANSI_RED + this + ": An error occurred." + ANSI_RESET + System.lineSeparator());
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
            System.out.println(ANSI_RED + this + ": File reading error." + ANSI_RESET + System.lineSeparator());
            e.printStackTrace();
        }
        
        System.out.println(
            ANSI_PURPLE + this + ANSI_RESET + System.lineSeparator() + 
            tab() + "Successfully created Tester." + System.lineSeparator()
        );
    }

    //READ
    public int read(String s) {
        LocalDate timestamp = LocalDate.now();

        s.toLowerCase();
        s.trim();

        if(back(s)) {
            System.out.println();
            return 0;
        }
        else if(quit(s)) {
            System.out.println();
            return -1;
        }
        else if(update(s)) {
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
                System.out.println(System.lineSeparator() + "Successfully updated to " + ANSI_PURPLE + "v" + release + '.' + serial + '.' + modified_d + '.' + modified_m + '.' + modified_y + ANSI_RESET + System.lineSeparator());
            } catch (IOException e) {
                System.out.println(ANSI_RED + this + ": File writing error." + ANSI_RESET + System.lineSeparator());
                e.printStackTrace();
            }

            return -1;
        }
        else if(publish(s)) {
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
                System.out.println(System.lineSeparator() + "Successfully published to " + ANSI_PURPLE + "v" + release + '.' + serial + '.' + modified_d + '.' + modified_m + '.' + modified_y + ANSI_RESET + System.lineSeparator());
            } catch (IOException e) {
                System.out.println(ANSI_RED + this + ": File writing error." + ANSI_RESET + System.lineSeparator());
                e.printStackTrace();
            }

            return -1;
        }
        System.out.println();

        return 1;
    }

    public void start() {
        System.out.println("Welcome to " + name + ' ' + ANSI_PURPLE + release + ANSI_RESET + '.' + ANSI_PURPLE + serial + ANSI_RESET + '.' + ANSI_PURPLE + modified_d + ANSI_RESET + '.' + ANSI_PURPLE + modified_m + ANSI_RESET + '.' + ANSI_PURPLE + modified_y + ANSI_RESET + '!' + System.lineSeparator());
    }
}
