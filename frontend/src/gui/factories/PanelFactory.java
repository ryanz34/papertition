package gui.factories;

import gui.enums.PanelFactoryOptions;
import gui.interfaces.IFrame;
import gui.interfaces.IPanel;
import gui.interfaces.IPanelFactory;
import gui.mainMenu.MainMenuView;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates IPanels for an IFrame given its name and some initializing arguments
 */
public class PanelFactory implements IPanelFactory {
    private IFrame mainFrame;

    /**
     * @param mainFrame main IFrame for the system
     */
    public PanelFactory(IFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * Generate a panel given its name with no parameters
     *
     * @param name name of the panel to create
     * @return
     */
    @Override
    public IPanel createPanel(PanelFactoryOptions.panelNames name) {
        return createPanel(name, new HashMap<>());
    }

    /**
     * Generates an IPanel given its name and (optional) initializing arguments
     *
     * @param name                    name of the panel to create
     * @param initializationArguments HashMap of values that can be used to set the initial state of a panel
     * @return
     */
    @Override
    public IPanel createPanel(PanelFactoryOptions.panelNames name, Map<String, Object> initializationArguments) {
        switch (name) {
            case MAIN_MENU:
                return new MainMenuView(mainFrame);
            default:
                throw new RuntimeException("u screwed up");
        }
    }
}
