package gui.panels.orderPages;

import gui.interfaces.IFrame;
import gui.interfaces.IPanel;
import pages.Page;

import javax.swing.*;
import java.util.Set;

public class OrderPagesView implements IPanel {
    private JPanel panel;
    private JButton cancelButton;
    private JButton importButton;
    private JButton exportButton;
    private JScrollPane scrollPane;

    public OrderPagesView(IFrame mainFrame, Set<Page> pages) {
        OrderPagesPresenter orderPagesPresenter = new OrderPagesPresenter(mainFrame, this, pages);

        cancelButton.addActionListener((e) -> orderPagesPresenter.cancel());
        importButton.addActionListener((e) -> orderPagesPresenter.importMore());
        exportButton.addActionListener((e) -> orderPagesPresenter.export());
    }

    JScrollPane getScrollPane() {
        return scrollPane;
    }

    JButton getImportButton() {
        return importButton;
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
