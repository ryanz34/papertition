package mainMenu;

import enums.DialogFactoryOptions;
import interfaces.IDialog;
import interfaces.IDialogFactory;
import interfaces.IFrame;
import interfaces.IPanelFactory;

import java.util.HashMap;
import java.util.HashSet;

class MainMenuPresenter {

    private IDialogFactory dialogFactory;
    private IPanelFactory panelFactory;

    MainMenuPresenter(IFrame mainFrame) {
        dialogFactory = mainFrame.getDialogFactory();
        panelFactory = mainFrame.getPanelFactory();
    }

    void upload() {

        IDialog uploadDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.FILE_PICKER, new HashMap<>() {
            {
                put("selectionMode", DialogFactoryOptions.selectionMode.FILES_ONLY);
                put("extensions", new HashSet<String>() {
                    {
                        add("pdf");
                    }
                });
            }
        });

        System.out.println(uploadDialog.run());

    }
}
