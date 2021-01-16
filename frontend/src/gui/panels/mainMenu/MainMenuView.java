package gui.panels.mainMenu;

import gui.interfaces.IFrame;
import gui.interfaces.IPanel;

import javax.swing.*;

public class MainMenuView implements IPanel {
    private JButton selectFileToStartButton;
    private JPanel mainMenuPanel;
    private JProgressBar progressBar;
    private JLabel loadingText;

    public MainMenuView(IFrame mainFrame) {
        MainMenuPresenter presenter = new MainMenuPresenter(mainFrame, this);

        selectFileToStartButton.addActionListener((e) -> presenter.upload());
    }

    JButton getSelectFileToStartButton() {
        return selectFileToStartButton;
    }

    JProgressBar getProgressBar() {
        return progressBar;
    }

    JLabel getLoadingText() {
        return loadingText;
    }

    @Override
    public JPanel getPanel() {
        return mainMenuPanel;
    }
}
