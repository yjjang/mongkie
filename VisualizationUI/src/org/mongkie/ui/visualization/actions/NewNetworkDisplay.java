/*
 * Visit <http://yjjang.github.io/mongkie> for details.
 * Copyright (C) 2015 Ewha Research Center for Systems Biology (ERCSB)
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
package org.mongkie.ui.visualization.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.mongkie.visualization.VisualizationControllerUI;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Window",
        id = "org.mongkie.ui.visualization.actions.NewNetworkDisplay"
)
@ActionRegistration(
        iconBase = "org/mongkie/ui/visualization/resources/tab_new.png",
        displayName = "#CTL_NewNetworkDisplay"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 200),
    @ActionReference(path = "Shortcuts", name = "D-N")
})
@Messages("CTL_NewNetworkDisplay=New...")
public final class NewNetworkDisplay implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Lookup.getDefault().lookup(VisualizationControllerUI.class).openNewDisplayTopComponent(null);
    }
}
