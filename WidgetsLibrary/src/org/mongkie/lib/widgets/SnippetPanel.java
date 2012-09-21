/*
Copyright 2008-2010 Gephi
Authors : Mathieu Bastian <mathieu.bastian@gephi.org>
Website : http://www.gephi.org

This file is part of Gephi.

Gephi is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

Gephi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with Gephi.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mongkie.lib.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.ComponentUI;

//Copied from org.netbeans.lib.profiler.ui.components
public class SnippetPanel extends JPanel implements MouseListener, KeyListener, FocusListener {
    //~ Inner Classes ------------------------------------------------------------------------------------------------------------

    public static class Padding extends JPanel {
        //~ Constructors ---------------------------------------------------------------------------------------------------------

        public Padding() {
            setBackground(WidgetUtilities.getProfilerResultsBackground());
            setOpaque(true);
        }

        //~ Methods --------------------------------------------------------------------------------------------------------------
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(lineColor);
            g.drawLine(0, 0, getWidth(), 0);
        }
    }

    private static class Title extends JComponent implements Accessible {
        //~ Instance fields ------------------------------------------------------------------------------------------------------

        String name;
        private boolean collapsed;
        private boolean rollOver;

        //~ Constructors ---------------------------------------------------------------------------------------------------------
        private Title(String name) {
            this.name = name;
            setUI(new TitleUI());
        }

        //~ Methods --------------------------------------------------------------------------------------------------------------
        public void setRollOver(boolean rollOver) {
            if (rollOver == this.rollOver) {
                return;
            }

            this.rollOver = rollOver;
            repaint();
        }

        public void collapse() {
            collapsed = true;
            repaint();
        }

        public void expand() {
            collapsed = false;
            repaint();
        }
    }

    private static class TitleUI extends ComponentUI {
        //~ Instance fields ------------------------------------------------------------------------------------------------------

        private final int TITLE_X_OFFSET = 5;
        private final int TITLE_Y_OFFSET = 2;
        private final ImageIcon collapsedIcon = new ImageIcon(TitleUI.class.getResource("resources/tab_right.png")); //NOI18N
        private final ImageIcon expandedIcon = new ImageIcon(TitleUI.class.getResource("resources/tab_down.png")); //NOI18N
        private final JLabel plainPainter = new JLabel();
        private final JLabel boldPainter = new JLabel();
        private final Font plainFont = plainPainter.getFont().deriveFont(Font.PLAIN);
        private final Font boldFont = boldPainter.getFont().deriveFont(Font.BOLD);
        private Dimension preferredSize;

        //~ Methods --------------------------------------------------------------------------------------------------------------
        @Override
        public Dimension getPreferredSize(JComponent c) {
            return preferredSize;
        }

        @Override
        public void installUI(JComponent c) {
            plainPainter.setText(((Title) c).name);
            plainPainter.setIcon(collapsedIcon);
            plainPainter.setFont(plainFont);
            plainPainter.setIconTextGap(5);
            boldPainter.setText(((Title) c).name);
            boldPainter.setIcon(expandedIcon);
            boldPainter.setFont(boldFont);
            boldPainter.setIconTextGap(5);

            plainPainter.setSize(plainPainter.getPreferredSize());
            Dimension titlePreferredSize = boldPainter.getPreferredSize();
            boldPainter.setSize(titlePreferredSize);
            preferredSize = new Dimension(TITLE_X_OFFSET + titlePreferredSize.width,
                    titlePreferredSize.height + TITLE_Y_OFFSET * 2);
        }

        @Override
        public void paint(Graphics g, JComponent c) {

            Title title = (Title) c;

            g.setColor(lineColor);
            g.drawLine(0, 0, c.getWidth(), 0);

            if (title.collapsed) { // do not draw bottom line if collapsed

                if (title.rollOver || title.isFocusOwner()) {
                    g.setColor(focusedBackgroundColor);
                } else {
                    g.setColor(backgroundColor);
                }
            }

            g.drawLine(0, 1 + plainPainter.getHeight() + TITLE_Y_OFFSET,
                    c.getWidth(), 1 + plainPainter.getHeight() + TITLE_Y_OFFSET);

            if (title.rollOver || title.isFocusOwner()) {
                g.setColor(focusedBackgroundColor);
            } else {
                g.setColor(backgroundColor);
            }

            g.fillRect(0, 1, c.getWidth(), plainPainter.getHeight() + TITLE_Y_OFFSET);

            g.translate(TITLE_X_OFFSET, TITLE_Y_OFFSET);
            if (title.collapsed) {
                plainPainter.paint(g);
            } else {
                boldPainter.paint(g);
            }
            g.translate(-TITLE_X_OFFSET, -TITLE_Y_OFFSET);
        }
    }
    //~ Static fields/initializers -----------------------------------------------------------------------------------------------
    private static Color lineColor;
    private static Color backgroundColor;
    private static Color focusedBackgroundColor;

    static {
        initColors();
    }
    //~ Instance fields ----------------------------------------------------------------------------------------------------------
    private JComponent content;
    private String snippetName;
    private Title title;
    private boolean collapsed = false;

    //~ Constructors -------------------------------------------------------------------------------------------------------------
    public SnippetPanel(String snippetName, JComponent content) {
        this.snippetName = snippetName;
        this.content = content;
        setLayout(new BorderLayout());
        title = new Title(snippetName) {

            @Override
            public AccessibleContext getAccessibleContext() {
                return SnippetPanel.this.getAccessibleContext();
            }
        };
        title.setFocusable(true);
        title.addKeyListener(SnippetPanel.this);
        title.addMouseListener(SnippetPanel.this);
        title.addFocusListener(SnippetPanel.this);
        // transfer the tooltip from the content to the snippet panel
        title.setToolTipText(content.getToolTipText());
        content.setToolTipText(null);
        //**
        add(title, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
        getAccessibleContext().setAccessibleName(snippetName);
    }

    //~ Methods ------------------------------------------------------------------------------------------------------------------
    private static void initColors() {
        Color systemBackgroundColor = WidgetUtilities.getProfilerResultsBackground();

        int backgroundRed = systemBackgroundColor.getRed();
        int backgroundGreen = systemBackgroundColor.getGreen();
        int backgroundBlue = systemBackgroundColor.getBlue();
        boolean inverseColors = backgroundRed < 41 || backgroundGreen < 32 || backgroundBlue < 25;

        if (inverseColors) {
            lineColor = WidgetUtilities.getSafeColor(backgroundRed + 41, backgroundGreen + 32, backgroundBlue + 8);
            backgroundColor = WidgetUtilities.getSafeColor(backgroundRed + 7, backgroundGreen + 7, backgroundBlue + 7);
            focusedBackgroundColor = WidgetUtilities.getSafeColor(backgroundRed + 25, backgroundGreen + 25, backgroundBlue + 25);
        } else {
            lineColor = WidgetUtilities.getSafeColor(backgroundRed - 41 /*214*/, backgroundGreen - 32 /*223*/, backgroundBlue - 8 /*247*/);
            backgroundColor = WidgetUtilities.getSafeColor(backgroundRed - 7 /*248*/, backgroundGreen - 7 /*248*/, backgroundBlue - 7 /*248*/);
            focusedBackgroundColor = WidgetUtilities.getSafeColor(backgroundRed - 25 /*230*/, backgroundGreen - 25 /*230*/, backgroundBlue - 25 /*230*/);
        }
    }

    public void setCollapsed(boolean collapsed) {
        if (this.collapsed == collapsed) {
            return;
        }

        this.collapsed = collapsed;

        if (collapsed) {
            title.collapse();
        } else {
            title.expand();
        }

        content.setVisible(!collapsed);
        revalidate();
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setContent(JComponent content) {
        this.content = content;
    }

    public JComponent getContent() {
        return content;
    }

    public void setSnippetName(String snippetName) {
        this.snippetName = snippetName;
    }

    public String getSnippetName() {
        return snippetName;
    }

    @Override
    public void focusGained(FocusEvent e) {
        title.repaint();
    }

    @Override
    public void focusLost(FocusEvent e) {
        title.repaint();
    }

    @Override
    public void keyPressed(final KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            setCollapsed(!isCollapsed());
        }
    }

    @Override
    public void keyReleased(final KeyEvent evt) {
    } // not used

    @Override
    public void keyTyped(final KeyEvent evt) {
    } // not used

    @Override
    public void mouseClicked(MouseEvent e) {
    } // not used

    @Override
    public void mouseEntered(MouseEvent e) {
        title.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        title.setRollOver(true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        title.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        title.setRollOver(false);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        setCollapsed(!collapsed);
        requestFocus();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    } // not used

    @Override
    public void requestFocus() {
        if (title != null) {
            title.requestFocus();
        }
    }
}
