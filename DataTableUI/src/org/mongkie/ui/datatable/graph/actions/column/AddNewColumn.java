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
package org.mongkie.ui.datatable.graph.actions.column;

import java.awt.Image;
import org.mongkie.datatable.DataTableControllerUI;
import org.mongkie.datatable.spi.GraphAddColumnAction;
import org.mongkie.datatable.spi.GraphDataTable;
import org.mongkie.visualization.MongkieDisplay;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Yeongjun Jang <yjjang@kribb.re.kr>
 */
@ServiceProvider(service = GraphAddColumnAction.class, position = 0)
public class AddNewColumn extends GraphAddColumnAction {

    private String title;
    private Class type;
    private Object defaultValue;

    void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    void setTitle(String title) {
        this.title = title;
    }

    void setType(Class type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return "New...";
    }

    @Override
    public String getDescription() {
        return "Create a new column";
    }

    @Override
    public Image getIcon() {
        return null;
    }

    @Override
    public void execute(GraphDataTable table) {
        table.getModel().getTable().addColumn(title, type, defaultValue);
        MongkieDisplay d = table.getModel().getDisplay();
        if (!d.isFired()) {
            d.fireGraphChangedEvent();
        }
        Lookup.getDefault().lookup(DataTableControllerUI.class).refreshModel(table, false);
    }

    @Override
    public boolean isEnabled(GraphDataTable table) {
        return true;
    }

    @Override
    public UI getUI() {
        return AddNewColumnUI.getInstance();
    }
}
