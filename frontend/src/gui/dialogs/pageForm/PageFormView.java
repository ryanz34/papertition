package gui.dialogs.pageForm;

import gui.interfaces.IDialog;
import gui.interfaces.IFrame;
import pages.Page;

import javax.swing.*;
import java.awt.event.*;
import java.util.Set;

public class PageFormView extends JDialog implements IDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton deletePageButton;
    private JPanel previewPanel;
    private JSpinner pageNum;
    private JSpinner documentID;

    public PageFormView(IFrame mainFrame, Page page, Set<Page> pages) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        PageFormPresenter pageFormPresenter = new PageFormPresenter(mainFrame, this, page, pages);

        deletePageButton.addActionListener((e) -> pageFormPresenter.delete());
        buttonOK.addActionListener((e) -> pageFormPresenter.ok());
        buttonCancel.addActionListener((e) -> close());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    JPanel getPreviewPanel() {
        return previewPanel;
    }

    JSpinner getPageNum() {
        return pageNum;
    }

    JSpinner getDocumentID() {
        return documentID;
    }

    void close() {
        dispose();
    }

    @Override
    public Object run() {
        this.pack();
        this.setVisible(true);

        return null;
    }
}
