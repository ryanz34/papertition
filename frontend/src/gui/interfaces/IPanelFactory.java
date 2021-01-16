package gui.interfaces;

import gui.enums.PanelFactoryOptions;

import java.util.Map;

/**
 * Generic panel factory interface
 */
public interface IPanelFactory {
    IPanel createPanel(PanelFactoryOptions.panelNames name);

    IPanel createPanel(PanelFactoryOptions.panelNames name, Map<String, Object> arguments);
}
