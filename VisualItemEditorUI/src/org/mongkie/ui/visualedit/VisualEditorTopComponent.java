/*
 * This file is part of MONGKIE. Visit <http://www.mongkie.org/> for details.
 * Copyright (C) 2011 Korean Bioinformation Center(KOBIC)
 * 
 * MONGKIE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * MONGKIE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mongkie.ui.visualedit;

import java.util.logging.Logger;
import org.mongkie.visualedit.VisualEditController;
import org.mongkie.visualedit.VisualEditor;
import static org.mongkie.visualization.Config.MODE_ACTION;
import static org.mongkie.visualization.Config.ROLE_NETWORK;
import org.mongkie.visualization.VisualizationController;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import prefuse.visual.VisualItem;

/**
 *
 * @author Yeongjun Jang <yjjang@kribb.re.kr>
 */
@ConvertAsProperties(dtd = "-//org.mongkie.ui.visualedit//VisualEditor//EN",
autostore = false)
@TopComponent.Description(preferredID = VisualEditorTopComponent.PREFERRED_ID,
iconBase = "org/mongkie/ui/visualedit/resources/edit.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = MODE_ACTION, openAtStartup = true, roles = {ROLE_NETWORK, "PathwayRole"}, position = 100)
public final class VisualEditorTopComponent extends TopComponent {

    private static VisualEditorTopComponent instance;
    /**
     * path to the icon used by the component and its open action
     */
    static final String PREFERRED_ID = "VisualEditorTopComponent";

    public VisualEditorTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(VisualEditorTopComponent.class, "CTL_VisualEditorTopComponent"));
        setToolTipText(NbBundle.getMessage(VisualEditorTopComponent.class, "HINT_VisualEditorTopComponent"));
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        Lookup.getDefault().lookup(VisualizationController.class).getSelectionManager().addSelectionListener(
                Lookup.getDefault().lookup(VisualEditController.class));
    }

    <I extends VisualItem> void edit(I... items) {
        if (items.length > 0) {
            ((PropertySheet) propertySheet).setNodes(new Node[]{new VisualEditor<I>(items)});
        } else {
            disableEditing();
        }
    }

    void disableEditing() {
        ((PropertySheet) propertySheet).setNodes(new Node[]{});
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        propertySheet = new PropertySheet();

        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(propertySheet, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel propertySheet;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files
     * only, i.e. deserialization routines; otherwise you could get a
     * non-deserialized instance. To obtain the singleton instance, use
     * {@link #findInstance}.
     */
    public static synchronized VisualEditorTopComponent getDefault() {
        if (instance == null) {
            instance = new VisualEditorTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the VisualEditorTopComponent instance. Never call
     * {@link #getDefault} directly!
     */
    public static synchronized VisualEditorTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(VisualEditorTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof VisualEditorTopComponent) {
            return (VisualEditorTopComponent) win;
        }
        Logger.getLogger(VisualEditorTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }
}
