package gui.panels.mainMenu;

import controllers.IPDFParser;
import controllers.PDFParser;
import gui.enums.DialogFactoryOptions;
import gui.enums.PanelFactoryOptions;
import gui.factories.PanelFactory;
import gui.interfaces.*;
import pages.Page;

import javax.swing.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class MainMenuPresenter {

    private IDialogFactory dialogFactory;
    private IPanelFactory panelFactory;

    private MainMenuView mainMenuView;
    private IFrame mainFrame;

    MainMenuPresenter(IFrame mainFrame, MainMenuView mainMenuView) {
        this.mainFrame = mainFrame;
        this.mainMenuView = mainMenuView;
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
            JProgressBar progressBar = mainMenuView.getProgressBar();
            JLabel loadingText = mainMenuView.getLoadingText();
            JButton getSelectFileToStartButton = mainMenuView.getSelectFileToStartButton();

            new Thread(() -> {
                IPDFParser pdfParser = new PDFParser();
                Set<Page> pages;

                try {
                    loadingText.setVisible(true);
                    progressBar.setVisible(true);
                    getSelectFileToStartButton.setEnabled(false);

                    pages = pdfParser.run(filePath, (text) -> loadingText.setText(text));

                    progressBar.setVisible(false);
                    loadingText.setVisible(false);
                    getSelectFileToStartButton.setEnabled(true);

                    IPanel orderPagesPanel = panelFactory.createPanel(PanelFactoryOptions.panelNames.ORDER_PAGES, new HashMap<>() {
                        {
                            put("pages", pages);
                        }
                    });

                    mainFrame.setPanel(orderPagesPanel);

                } catch (Exception e) {
                    dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<>() {
                        {
                            put("messageType", DialogFactoryOptions.dialogType.ERROR);
                            put("title", "Error");
                            put("message", e.getMessage());
                        }
                    }).run();
                }
            }).start();
        }
    }
}
