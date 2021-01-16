package gui.panels.orderPages;

import gui.enums.DialogFactoryOptions;
import gui.enums.PanelFactoryOptions;
import gui.interfaces.IDialogFactory;
import gui.interfaces.IFrame;
import gui.interfaces.IPanelFactory;
import pages.Page;

import java.util.HashMap;
import java.util.Set;

public class OrderPagesPresenter {

    private IDialogFactory dialogFactory;
    private IPanelFactory panelFactory;

    private OrderPagesView orderPagesView;
    private IFrame mainFrame;
    private Set<Page> pages;

    public OrderPagesPresenter(IFrame mainFrame, OrderPagesView orderPagesView, Set<Page> pages) {
        this.mainFrame = mainFrame;
        this.orderPagesView = orderPagesView;
        this.pages = pages;

        this.dialogFactory = mainFrame.getDialogFactory();
        this.panelFactory = mainFrame.getPanelFactory();
    }

    public void cancel() {
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
}
