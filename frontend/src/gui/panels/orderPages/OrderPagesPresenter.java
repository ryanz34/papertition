package gui.panels.orderPages;

import controllers.IPDFParser;
import controllers.PDFParser;
import gui.enums.DialogFactoryOptions;
import gui.enums.PanelFactoryOptions;
import gui.interfaces.IDialog;
import gui.interfaces.IDialogFactory;
import gui.interfaces.IFrame;
import gui.interfaces.IPanelFactory;
import pages.Page;

import javax.swing.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class OrderPagesPresenter {

    private IDialogFactory dialogFactory;
    private IPanelFactory panelFactory;

    private OrderPagesView orderPagesView;
    private IFrame mainFrame;
    private Set<Page> pages;

    OrderPagesPresenter(IFrame mainFrame, OrderPagesView orderPagesView, Set<Page> pages) {
        this.mainFrame = mainFrame;
        this.orderPagesView = orderPagesView;
        this.pages = pages;

        this.dialogFactory = mainFrame.getDialogFactory();
        this.panelFactory = mainFrame.getPanelFactory();

        updateView();
    }

    void updateView() {
        JList documentList = orderPagesView.getDocumentList();

        DefaultListModel listModel = new DefaultListModel();

        int i = 0;

        JPanel wtf = new JPanel();
        for (Page page : pages) {
            listModel.add(i++, new ImageIcon(page.image));
        }

        documentList.setModel(listModel);

    }

    void cancel() {
        boolean confirmation = (boolean) dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<>() {
            {
                put("message", "Are you sure you want to quit? You will lose all progress");
                put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
            }
        }).run();

        if (confirmation) {
            mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.MAIN_MENU));
        }
    }

    void importMore() {
        JButton importButton = orderPagesView.getImportButton();

        IDialog uploadDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.FILE_PICKER, new HashMap<>() {
            {
                put("extensions", new HashSet<String>() {
                    {
                        add("pdf");
                    }
                });
                put("selectionMode", DialogFactoryOptions.selectionMode.FILES_ONLY);
            }
        });

        String filePath = (String) uploadDialog.run();

        if (filePath != null && filePath.length() > 0) {
            new Thread(() -> {
                try {
                    IPDFParser pdfParser = new PDFParser();

                    importButton.setEnabled(false);

                    pages.addAll(pdfParser.run(filePath, importButton::setText));
                } catch (Exception e) {
                    dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<>() {
                        {
                            put("messageType", DialogFactoryOptions.dialogType.ERROR);
                            put("title", "Error");
                            put("message", e.getMessage());
                        }
                    }).run();
                }

                importButton.setEnabled(true);
                importButton.setText("Import");

                updateView();
            }).run();
        }
    }

    void export() {

    }
}
