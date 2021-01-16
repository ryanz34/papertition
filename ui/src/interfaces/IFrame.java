package interfaces;

import javax.swing.*;

/**
 * Generic frame interface
 */
public interface IFrame {
    void setPanel(IPanel panel);

    IPanelFactory getPanelFactory();

    IDialogFactory getDialogFactory();

    JFrame getFrame();
}
