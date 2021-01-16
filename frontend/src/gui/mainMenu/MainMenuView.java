package gui.mainMenu;

import gui.interfaces.IFrame;
import gui.interfaces.IPanel;

import javax.swing.*;

public class MainMenuView implements IPanel {
    private JButton selectFileToStartButton;
    private JPanel mainMenuPanel;

    public MainMenuView(IFrame mainFrame) {
        MainMenuPresenter presenter = new MainMenuPresenter(mainFrame);

        selectFileToStartButton.addActionListener((e) -> presenter.upload());
    }

    @Override
    public JPanel getPanel() {
        return mainMenuPanel;
    }
}
