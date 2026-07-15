package tester_app.editors;

import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileSystemView;

public class DirectoryRestrictedFileSystemView extends FileSystemView {
    private final File[] rootDirectories;

    DirectoryRestrictedFileSystemView(File rootDirectory) {
        this.rootDirectories = new File[] {rootDirectory};
    }

    DirectoryRestrictedFileSystemView(File[] rootDirectories) {
        this.rootDirectories = rootDirectories;
    }

    @Override
    public File createNewFolder(File containingDir) throws IOException {
        File newTopic = new File(containingDir, "New Topic");
        if(newTopic.mkdir()) {
            return containingDir;
        } else {
            throw new IOException("Unable to create new topic");
        }
    }

    @Override
    public File[] getRoots() {
        return rootDirectories;
    }

    @Override
    public boolean isRoot(File file) {
        for (File root : rootDirectories) {
            if (root.equals(file)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public File getHomeDirectory() {
        return rootDirectories[0];
    }

    @Override
    public File getParentDirectory(File dir) {
        return rootDirectories[0];
    }
}
