package gui.panels.mainMenu;

import gui.interfaces.IFrame;
import gui.interfaces.IPanel;

import javax.swing.*;

public class MainMenuView implements IPanel {
    private JButton selectFileToStartButton;
    private JPanel mainMenuPanel;
    private JScrollPane helpScroll;

    public MainMenuView(IFrame mainFrame) {
        MainMenuPresenter presenter = new MainMenuPresenter(mainFrame, this);

        selectFileToStartButton.addActionListener((e) -> presenter.upload());

        helpScroll.getVerticalScrollBar().setUnitIncrement(16);
    }

    JButton getSelectFileToStartButton() {
        return selectFileToStartButton;
    }

    @Override
    public JPanel getPanel() {
        return mainMenuPanel;
    }
}
