/*
 * This file is part of MONGKIE. Visit <http://www.mongkie.org/> for details.
 * Visit <http://www.mongkie.org> for details about MONGKIE.
 * Copyright (C) 2012 Korean Bioinformation Center (KOBIC)
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
package org.mongkie.ui.visualization.tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Yeongjun Jang <yjjang@kribb.re.kr>
 */
public class AddonsBar extends JPanel {

    public AddonsBar() {
        super(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        setBackground(Color.WHITE);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (Component c : getComponents()) {
                    c.setEnabled(enabled);
                }
            }
        });
        setOpaque(enabled);
    }
}
