package gui.panels.orderPages;

import gui.interfaces.IFrame;
import gui.interfaces.IPanel;
import pages.Page;

import javax.swing.*;
import java.util.Set;

public class OrderPagesView implements IPanel {
    private JPanel panel;
    private JButton cancelButton;

    public OrderPagesView(IFrame mainFrame, Set<Page> pages) {
        OrderPagesPresenter orderPagesPresenter = new OrderPagesPresenter(mainFrame, this, pages);

        cancelButton.addActionListener((e) -> orderPagesPresenter.cancel());
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
