/*
 Copyright 2008-2010 Gephi
 Authors : Mathieu Bastian <mathieu.bastian@gephi.org>
 Website : http://www.gephi.org

 This file is part of Gephi.

 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 2011 Gephi Consortium. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 3 only ("GPL") or the Common
 Development and Distribution License("CDDL") (collectively, the
 "License"). You may not use this file except in compliance with the
 License. You can obtain a copy of the License at
 http://gephi.org/about/legal/license-notice/
 or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
 specific language governing permissions and limitations under the
 License.  When distributing the software, include this License Header
 Notice in each file and include the License files at
 /cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
 License Header, with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"

 If you wish your version of this file to be governed by only the CDDL
 or only the GPL Version 3, indicate your decision by adding
 "[Contributor] elects to include this software in this distribution
 under the [CDDL or GPL Version 3] license." If you do not indicate a
 single choice of license, a recipient has the option to distribute
 your version of this file under either the CDDL, the GPL Version 3 or
 to extend the choice of license to its licensees as provided above.
 However, if you add GPL Version 3 code and therefore, elected the GPL
 Version 3 license, then the option applies only if the new code is
 made subject to such option by the copyright holder.

 Contributor(s):

 Portions Copyrighted 2011 Gephi Consortium.
 */
package org.mongkie.lib.widgets;

import com.bric.swing.ColorPicker;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

/**
 *
 * @author Mathieu Bastian
 */
public class JColorButton extends JButton {

    public static String EVENT_COLOR = "color";
    private Color color;
    private boolean includeOpacity;
    private final static int ICON_WIDTH = 16;
    private final static int ICON_HEIGHT = 16;
    private final static Color DISABLED_BORDER = new Color(200, 200, 200);
    private final static Color DISABLED_FILL = new Color(220, 220, 220);
    private Window owner;

    public JColorButton(Window owner, Color originalColor) {
        this(owner, originalColor, false, false);
    }

    public JColorButton(Window owner, Color originalColor, boolean rightClick, boolean includeOpacity) {
        this.owner = owner;
        this.includeOpacity = includeOpacity;
        this.color = originalColor;
        setIcon(new Icon() {

            @Override
            public int getIconWidth() {
                return ICON_WIDTH;
            }

            @Override
            public int getIconHeight() {
                return ICON_HEIGHT;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                if (c.isEnabled()) {
                    g.setColor(Color.BLACK);
                    g.drawRect(x + 2, y + 2, ICON_WIDTH - 5, ICON_HEIGHT - 5);
                    if (color != null) {
                        g.setColor(color);
                        g.fillRect(x + 3, y + 3, ICON_WIDTH - 6, ICON_HEIGHT - 6);
                    }
                } else {
                    g.setColor(DISABLED_BORDER);
                    g.drawRect(x + 2, y + 2, ICON_WIDTH - 5, ICON_HEIGHT - 5);
                    g.setColor(DISABLED_FILL);
                    g.fillRect(x + 3, y + 3, ICON_WIDTH - 6, ICON_HEIGHT - 6);
                }

            }
        });
        if (rightClick) {
            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {

                    if (SwingUtilities.isRightMouseButton(e)) {
                        Color newColor = ColorPicker.showDialog(JColorButton.this.owner, color, JColorButton.this.includeOpacity);
                        if (newColor != null) {
                            setColor(newColor);
                        }
                    }
                }
            });
        } else {
            addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Color newColor = ColorPicker.showDialog(JColorButton.this.owner, color, JColorButton.this.includeOpacity);
                    if (newColor != null) {
                        setColor(newColor);
                    }
                }
            });
        }
    }

    public void setColor(Color color) {
        if (color != this.color || (color != null && !color.equals(this.color))) {
            Color oldColor = this.color;
            this.color = color;
            firePropertyChange(EVENT_COLOR, oldColor, color);
            repaint();
        }
    }

    public Color getColor() {
        return color;
    }

    public float[] getColorArray() {
        return new float[]{color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f};
    }

    public void setIncludeOpacity(boolean includeOpacity) {
        this.includeOpacity = includeOpacity;
    }
}