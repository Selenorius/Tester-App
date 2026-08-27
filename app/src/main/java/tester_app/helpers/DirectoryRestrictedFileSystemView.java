package tester_app.helpers;

import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileSystemView;

public class DirectoryRestrictedFileSystemView extends FileSystemView {
    public DirectoryRestrictedFileSystemView() {}

    @Override
    public File createNewFolder(File containingDir) throws IOException {
        throw new IOException("Unable to create new folder");
    }
}
