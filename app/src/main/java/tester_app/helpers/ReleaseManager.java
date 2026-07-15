package tester_app.helpers;

import static tester_app.Tester.release;
import static tester_app.Tester.serial;
import static tester_app.helpers.Constants.ANSI_PURPLE;
import static tester_app.helpers.Constants.ANSI_RED;
import static tester_app.helpers.Constants.ANSI_RESET;
import static tester_app.helpers.Constants.tab;

import java.io.File;

public final class ReleaseManager {
    private ReleaseManager() {}

    public static final void manageReleases() {
        File releaseFile = new File("releases/tester_app.jar");
        
        if (releaseFile.exists()) {
            System.out.println (ANSI_PURPLE + "Release Manager" + ANSI_RESET);
            File releaseDirectory = new File("releases/v" + release);
            File newReleaseFile = new File("releases/v" + release + "/Tester App " + 'v' + release + '.' + serial + ".jar");

            if(!releaseDirectory.exists()) {
                if (releaseDirectory.mkdir()) {
                    System.out.println (tab() + "Created new releases directory.");
                } else {
                    System.out.println(tab() + ANSI_RED + "Failed to create release directory." + ANSI_RESET);
                }
            }

            if(!newReleaseFile.exists()) {
                if (releaseFile.renameTo(newReleaseFile)) {
                    System.out.println (tab() + "Organized releases.");
                } else {
                    System.out.println(tab() + ANSI_RED + "Failed to rename release file." + ANSI_RESET);
                }
            } else {
                if (!newReleaseFile.delete()) {
                    System.out.println(tab() + ANSI_RED + "Failed to delete old release file." + ANSI_RESET);
                }

                if (releaseFile.renameTo(newReleaseFile)) {
                    System.out.println (tab() + "Organized releases.");
                } else {
                    System.out.println(tab() + ANSI_RED + "Failed to rename release file." + ANSI_RESET);
                }
            }

            System.out.println();
        }
    }
}
