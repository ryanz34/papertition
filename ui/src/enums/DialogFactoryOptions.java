package enums;

/**
 * Enums pertaining to the dialog factory
 */
public class DialogFactoryOptions {
    public enum dialogNames {
        MESSAGE,
        CONFIRM_BOOLEAN
    }

    /**
     * Enums of dialogue types
     */
    public enum dialogType {
        ERROR,
        INFORMATION,
        WARNING,
        QUESTION,
        PLAIN
    }

    /**
     * Enums of option types
     */
    public enum optionType {
        DEFAULT_OPTION,
        YES_NO_OPTION,
        YES_NO_CANCEL_OPTION,
        OK_CANCEL_OPTION
    }
}
