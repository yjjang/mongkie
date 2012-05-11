/*
 *  This file is part of MONGKIE. Visit <http://www.mongkie.org/> for details.
 *  Copyright (C) 2012 Korean Bioinformation Center(KOBIC)
 * 
 *  MONGKIE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  MONGKE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 * 
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mongkie.ui.datatable.graph.actions.node;

import java.awt.Image;
import org.mongkie.datatable.spi.DataAction;
import org.mongkie.ui.datatable.graph.AbstractDataTable;
import org.mongkie.ui.datatable.graph.AbstractDataTable.AbstractModel;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Yeongjun Jang <yjjang@kribb.re.kr>
 */
@ServiceProvider(service = DataAction.class, position = 10)
public class AddNode extends AbstractNodeAction {

    @Override
    public SettingUI<AbstractDataTable> getSettingUI(AbstractDataTable table) {
        return null;
    }

    @Override
    public String getName() {
        return "Add node";
    }

    @Override
    public String getDescription() {
        return "Add a new node into the graph";
    }

    @Override
    public Image getIcon() {
        return ImageUtilities.loadImage("org/mongkie/ui/datatable/resources/plus-white.png", false);
    }

    @Override
    public void execute(AbstractDataTable table) {
        System.out.println(">> " + getDescription());
    }

    @Override
    public boolean isEnabled(AbstractModel model) {
        return model.getDisplay().getNodeDataEditSupport().isAddDataSupported();
    }

    @Override
    public boolean hideActionText() {
        return false;
    }
}
