package gui.dialogs.pageForm;

import gui.enums.DialogFactoryOptions;
import gui.interfaces.IDialogFactory;
import gui.interfaces.IFrame;
import gui.utils.ImageTools;
import pages.Page;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Set;

public class PageFormPresenter {

    IFrame mainFrame;
    PageFormView pageFormView;
    Page page;
    Set<Page> pages;

    IDialogFactory dialogFactory;

    PageFormPresenter(IFrame mainFrame, PageFormView pageFormView, Page page, Set<Page> pages) {
        this.mainFrame = mainFrame;
        this.dialogFactory = mainFrame.getDialogFactory();

        this.pageFormView = pageFormView;
        this.page = page;
        this.pages = pages;

        int previewHeight = 800;

        // Set current bois
        // plz don't read this code

        pageFormView.getPageNum().setValue(page.page);
        pageFormView.getDocumentID().setValue(page.documentID);

        JPanel preview = pageFormView.getPreviewPanel();

        preview.setLayout(new FlowLayout(FlowLayout.CENTER));
        preview.add(new JLabel(new ImageIcon(ImageTools.resize(page.image, previewHeight, (int) (page.image.getWidth() * ((float) previewHeight / page.image.getHeight()))))));
    }

    void delete() {
        boolean confirmation = (boolean) dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<>() {
            {
                put("message", "Are you sure you want to delete this page?");
                put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
            }
        }).run();

        if (confirmation) {
            pages.remove(page);
            pageFormView.close();
        }
    }

    void ok() {
        page.page = (Integer) pageFormView.getPageNum().getValue();
        page.documentID = (Integer) pageFormView.getDocumentID().getValue();

        pageFormView.close();
    }
}
