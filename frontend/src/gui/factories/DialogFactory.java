package gui.factories;

import gui.dialogs.ConfirmBooleanDialogView;
import gui.dialogs.FilePickerView;
import gui.dialogs.MessageDialogView;
import gui.enums.DialogFactoryOptions;
import gui.interfaces.IDialog;
import gui.interfaces.IDialogFactory;
import gui.interfaces.IFrame;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * factory to make dialogue panels for the GUI
 */
public class DialogFactory implements IDialogFactory {
    private IFrame mainFrame;

    /**
     * @param mainFrame main IFrame for the system
     */
    public DialogFactory(IFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * Generate a dialog given its name with no parameters
     *
     * @param name
     * @return
     */
    @Override
    public IDialog createDialog(DialogFactoryOptions.dialogNames name) {
        return createDialog(name, new HashMap<>());
    }

    /**
     * Generates an IDialog given its name and (optional) initializing arguments
     *
     * @param name
     * @param arguments
     * @return
     */
    @Override
    public IDialog createDialog(DialogFactoryOptions.dialogNames name, Map<String, Object> arguments) {
        switch (name) {
            case FILE_PICKER:
                return new FilePickerView(mainFrame, (DialogFactoryOptions.selectionMode) arguments.get("selectionMode"), (Set<String>) arguments.get("extensions"));
            case MESSAGE:
                return new MessageDialogView(mainFrame, (String) arguments.get("message"), (String) arguments.getOrDefault("title", "Message"), (DialogFactoryOptions.dialogType) arguments.get("messageType"));
            case CONFIRM_BOOLEAN:
                return new ConfirmBooleanDialogView(mainFrame, (String) arguments.get("message"), (String) arguments.getOrDefault("title", "Confirm"), (DialogFactoryOptions.dialogType) arguments.get("messageType"), (DialogFactoryOptions.optionType) arguments.get("confirmationType"));
            default:
                throw new RuntimeException("u screwed up");
        }
    }
}
