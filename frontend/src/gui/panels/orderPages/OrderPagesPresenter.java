package gui.panels.orderPages;

import controllers.PDFParser;
import controllers.PDFSaver;
import controllers.PDFParser;
import controllers.PDFSaver;
import gui.enums.DialogFactoryOptions;
import gui.enums.PanelFactoryOptions;
import gui.interfaces.IDialog;
import gui.interfaces.IDialogFactory;
import gui.interfaces.IFrame;
import gui.interfaces.IPanelFactory;
import pages.Page;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

class OrderPagesPresenter {

    private IDialogFactory dialogFactory;
    private IPanelFactory panelFactory;

    private OrderPagesView orderPagesView;
    private IFrame mainFrame;
    private Set<Page> pages;

    // no time for clean architecture
    private Map<Integer, List<Page>> outputDocuments;

    OrderPagesPresenter(IFrame mainFrame, OrderPagesView orderPagesView, Set<Page> pages) {
        this.mainFrame = mainFrame;
        this.orderPagesView = orderPagesView;
        this.pages = pages;

        this.dialogFactory = mainFrame.getDialogFactory();
        this.panelFactory = mainFrame.getPanelFactory();

        updateView();
    }

    /**
     * Generates documents based on their ID and page #
     */
    void updateDocuments() {
        outputDocuments = new HashMap<>();

        for (Page page : pages) {
            if (outputDocuments.get(page.documentID) == null) {
                outputDocuments.put(page.documentID, new ArrayList<>());
            }

            outputDocuments.get(page.documentID).add(page);
        }

        for (int documentID : outputDocuments.keySet()) {
            outputDocuments.get(documentID).sort((a, b) -> {
                if (a.page < b.page) {
                    return -1;
                } else if (a.page > b.page) {
                    return 1;
                } else {
                    return 0;
                }
            });
        }
    }

    void updateView() {
        updateDocuments();

        JScrollPane scrollPane = orderPagesView.getScrollPane();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        int rowNumber = 0;

        for (int documentID : outputDocuments.keySet()) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel rowLabel = new JLabel("Document " + documentID);

            row.add(rowLabel);

            if (rowNumber % 2 == 0) {
                row.setBackground(new Color(200, 200, 200));
            }

            for (Page page : outputDocuments.get(documentID)) {
                row.add(new JLabel(page.icon));
            }

            panel.add(row);
            rowNumber++;
        }

        scrollPane.setViewportView(panel);
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
                    PDFParser pdfParser = new PDFParser();

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
            }).start();
        }
    }

    void export() {
        JButton exportButton = orderPagesView.getExportButton();

        IDialog exportDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.FILE_PICKER, new HashMap<>() {
            {
                put("selectionMode", DialogFactoryOptions.selectionMode.DIRECTORIES_ONLY);
                put("extensions", new HashSet<String>() {
                    {
                        add("pdf");
                    }
                });
                put("title", "Choose a directory to export to");
            }
        });

        String filePath = (String) exportDialog.run();

        if (filePath != null && filePath.length() > 0) {
            PDFSaver pdfSaver = new PDFSaver();

            new Thread(() -> {
                try {
                    exportButton.setEnabled(false);
                    exportButton.setText("Exporting...");

                    pdfSaver.save(filePath, outputDocuments);

                    exportButton.setEnabled(true);
                    exportButton.setText("Export");

                    dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<>() {
                        {
                            put("messageType", DialogFactoryOptions.dialogType.INFORMATION);
                            put("title", "Success");
                            put("message", "Files have successfully been exported to the selected directory");
                        }
                    }).run();

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
