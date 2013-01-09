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
package org.mongkie.ui.im;

import java.awt.Component;
import javax.swing.SwingUtilities;
import org.mongkie.im.InteractionController;
import org.mongkie.im.SourceModel;
import org.mongkie.im.SourceModelChangeListener;
import org.mongkie.im.spi.InteractionSource;
import org.mongkie.visualization.MongkieDisplay;
import org.openide.util.Lookup;

/**
 *
 * @author Yeongjun Jang <yjjang@kribb.re.kr>
 */
public class CategoryPanel extends javax.swing.JPanel implements SourceModelChangeListener {

    private MongkieDisplay d;
    private String category;

    /**
     * Creates new form ProcessPanel
     */
    public CategoryPanel(MongkieDisplay d, String category) {
        this.d = d;
        this.category = category;
        initComponents();
        add(topFiller);
        for (InteractionSource is : Lookup.getDefault().lookup(InteractionController.class).getInteractionSources(category)) {
            add(new SourcePanel(d, is));
        }
        add(bottomFiller);
        Lookup.getDefault().lookup(InteractionController.class).addModelChangeListener(d, CategoryPanel.this);
    }

    @Override
    public void modelAdded(final SourceModel model) {
        if (category.equals(model.getInteractionSource().getCategory())
                && category.equals(InteractionController.CATEGORY_OTHERS)) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    add(new SourcePanel(d, model.getInteractionSource()), getComponentCount() - 2);
                }
            });
        }
    }

    @Override
    public void modelRemoved(SourceModel model) {
        for (Component c : getComponents()) {
            if (c instanceof SourcePanel && model.getInteractionSource().equals(((SourcePanel) c).getInteractionSource())) {
                remove(c);
                revalidate();
                repaint();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 4), new java.awt.Dimension(0, 4), new java.awt.Dimension(32767, 4));
        bottomFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 4), new java.awt.Dimension(0, 4), new java.awt.Dimension(32767, 4));

        setOpaque(false);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler bottomFiller;
    private javax.swing.Box.Filler topFiller;
    // End of variables declaration//GEN-END:variables
}
