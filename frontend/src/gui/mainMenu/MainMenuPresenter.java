package gui.mainMenu;

import controllers.IPDFParser;
import controllers.PDFParser;
import gui.enums.DialogFactoryOptions;
import gui.interfaces.IDialog;
import gui.interfaces.IDialogFactory;
import gui.interfaces.IFrame;
import gui.interfaces.IPanelFactory;
import pages.Page;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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

        String filePath = (String) uploadDialog.run();

        if (filePath != null && filePath.length() > 0) {
            IPDFParser pdfParser = new PDFParser();
            Set<Page> pages;

            try {
                pages = pdfParser.run(filePath);
            } catch (Exception e) {
                dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<>() {
                    {
                        put("messageType", DialogFactoryOptions.dialogType.ERROR);
                        put("title", "Error");
                        put("message", e.getMessage());
                    }
                }).run();
            }
        }
    }
}
