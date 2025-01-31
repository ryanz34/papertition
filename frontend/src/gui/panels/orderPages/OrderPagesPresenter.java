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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

        if (outputDocuments.get(-1) != null) {
            invalidIndexingMessage();
        }
    }

    void invalidIndexingMessage() {
        dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<>() {
            {
                put("messageType", DialogFactoryOptions.dialogType.WARNING);
                put("title", "Index Error");
                put("message", "Some pages were not indexed correctly. These pages were assigned an ID of -1.");
            }
        }).run();
    }

    /**
     * Generates documents based on their ID and page #
     */
    void updateDocuments() {
        outputDocuments = new HashMap<>();

        for (Page page : pages) {
            // If the page is -1, then we'll put it with the documentID -1 group so it can be corrected
            int documentID = page.page == -1 ? -1 : page.documentID;

            if (outputDocuments.get(documentID) == null) {
                outputDocuments.put(documentID, new ArrayList<>());
            }

            outputDocuments.get(documentID).add(page);
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

        List<Integer> sortedDocumentIDs = new ArrayList<>(outputDocuments.keySet());
        sortedDocumentIDs.sort((a, b) -> {
            if (a > b) {
                return 1;
            } else if (a < b) {
                return -1;
            } else {
                return 0;
            }
        });

        for (int documentID : sortedDocumentIDs) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel rowLabel = new JLabel("Document " + documentID);

            row.add(rowLabel);

            if (rowNumber % 2 == 0) {
                row.setBackground(new Color(220, 220, 220));
            }

            for (Page page : outputDocuments.get(documentID)) {
                JLabel thumbnail = new JLabel(page.icon);

                thumbnail.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);

                        dialogFactory.createDialog(DialogFactoryOptions.dialogNames.PAGE_FORM, new HashMap<>() {
                            {
                                put("page", page);
                                put("pages", pages);
                            }
                        }).run();

                        updateView();
                    }
                });

                row.add(thumbnail);
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

                if (outputDocuments.get(-1) != null) {
                    invalidIndexingMessage();
                }
            }).start();
        }
    }

    void export() {

        if (outputDocuments.get(-1) != null) {
            boolean confirmation = (boolean) dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<>() {
                {
                    put("messageType", DialogFactoryOptions.dialogType.WARNING);
                    put("title", "Index Error");
                    put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
                    put("message", "Some pages were not indexed correctly. Are you sure you want to proceed with the export?\n\nThe un-indexed pages will be saved in document -1.");
                }
            }).run();

            if (!confirmation) {
                return;
            }
        }

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
