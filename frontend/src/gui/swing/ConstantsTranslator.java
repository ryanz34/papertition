package gui.swing;

import gui.enums.DialogFactoryOptions;

import javax.swing.*;

/**
 * Translates between gui.enums and the constants that gui.swing uses
 */
public class ConstantsTranslator {
    /**
     * Translates between gui.enums and Swing dialog type constants
     *
     * @param dialogType
     * @return
     */
    public int translateDialogType(DialogFactoryOptions.dialogType dialogType) {
        switch (dialogType) {
            case ERROR:
                return JOptionPane.ERROR_MESSAGE;
            case WARNING:
                return JOptionPane.WARNING_MESSAGE;
            case QUESTION:
                return JOptionPane.QUESTION_MESSAGE;
            case INFORMATION:
                return JOptionPane.INFORMATION_MESSAGE;
            default:
                return JOptionPane.PLAIN_MESSAGE;
        }
    }

    /**
     * Translates between gui.enums and Swing dialog option constants
     *
     * @param optionType
     * @return
     */
    public int translateOptionType(DialogFactoryOptions.optionType optionType) {
        switch (optionType) {
            case YES_NO_OPTION:
                return JOptionPane.YES_NO_OPTION;
            case YES_NO_CANCEL_OPTION:
                return JOptionPane.YES_NO_CANCEL_OPTION;
            case OK_CANCEL_OPTION:
                return JOptionPane.OK_CANCEL_OPTION;
            default:
                return JOptionPane.DEFAULT_OPTION;
        }
    }

    public int translateFileSelectionMode(DialogFactoryOptions.selectionMode fileSelection) {
        switch (fileSelection) {
            case DIRECTORIES_ONLY:
                return JFileChooser.DIRECTORIES_ONLY;
            case FILES_ONLY:
                return JFileChooser.FILES_ONLY;
            default: FILES_AND_DIRECTORIES:
                return JFileChooser.FILES_AND_DIRECTORIES;
        }
    }
}
