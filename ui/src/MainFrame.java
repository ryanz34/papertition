import enums.PanelFactoryOptions;
import factories.DialogFactory;
import factories.PanelFactory;
import interfaces.IDialogFactory;
import interfaces.IFrame;
import interfaces.IPanel;
import interfaces.IPanelFactory;
import mainMenu.MainMenuView;

import javax.swing.*;
import java.awt.*;

class MainFrame implements IFrame {

    private JFrame frame;

    private final int initialWidth = 1100;
    private final int initialHeight = 700;

    private IPanelFactory panelFactory;
    private IDialogFactory dialogFactory;

    public MainFrame() {
        panelFactory = new PanelFactory(this);
        dialogFactory = new DialogFactory(this);
    }

    private void setLookAndFeel() {
        String osName = System.getProperty("os.name").toLowerCase();

        // We don't want to apply a custom theme if we're not on macOS since the default mac look and feel is already hot
        if (!osName.contains("darwin") && !osName.contains("mac")) {
            String[] targetLAF = {
                    "GTK+",
                    "Nimbus"
            };

            for (String lafName : targetLAF) {
                try {
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        System.out.println(info.getName());
                        if (lafName.equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            return;
                        }
                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    System.out.println("Unable to load custom look and feel");
                }
            }
        }
    }

    /**
     * Sets the current panel
     *
     * @param newPanel the new panel to load
     */
    @Override
    public void setPanel(IPanel newPanel) {
        frame.setContentPane(newPanel.getPanel());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Get the panel factory initialized for this IFrame
     *
     * @return panel factory initialized for this IFrame
     */
    @Override
    public IPanelFactory getPanelFactory() {
        return panelFactory;
    }

    @Override
    public IDialogFactory getDialogFactory() {
        return dialogFactory;
    }


    void run() {

        setLookAndFeel();

        frame = new JFrame("Papertition");

        // Restrict size
        Dimension initialDimension = new Dimension(initialWidth, initialHeight);

        frame.setMinimumSize(initialDimension);

        setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.MAIN_MENU));
    }
}
