package dialogs;

import enums.DialogFactoryOptions;
import interfaces.IDialog;
import interfaces.IFrame;
import swing.ConstantsTranslator;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Set;

/**
 * Generic boolean confirmation dialog
 */
public class FilePickerView implements IDialog {
    private IFrame mainFrame;
    private int selectionMode;
    private Set<String> extensions;

    /**
     * @param mainFrame main GUI frame
     */
    public FilePickerView(IFrame mainFrame, DialogFactoryOptions.selectionMode selectionMode, Set<String> extensions) {
        ConstantsTranslator constantsTranslator = new ConstantsTranslator();

        this.mainFrame = mainFrame;
        this.selectionMode = constantsTranslator.translateFileSelectionMode(selectionMode);
        this.extensions = extensions;
    }

    /**
     * Runs the dialog
     *
     * @return path of chosen file
     */
    @Override
    public String run() {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setFileSelectionMode(selectionMode);

        if (extensions.size() > 0) {
            FileFilter filter = null;

            for (String extension : extensions) {
                filter = new FileNameExtensionFilter(String.format("%s file (.%s)", extension, extension), extension);

                fileChooser.addChoosableFileFilter(filter);
            }

            fileChooser.setFileFilter(filter);
            fileChooser.setAcceptAllFileFilterUsed(false);
        }

        if (fileChooser.showOpenDialog(mainFrame.getFrame()) == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }
}
