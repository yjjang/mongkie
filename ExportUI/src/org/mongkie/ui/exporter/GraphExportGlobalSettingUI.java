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

/*
 * GraphExportSettingUI.java
 *
 * Created on Jul 2, 2011, 4:41:32 PM
 */
package org.mongkie.ui.exporter;

import javax.swing.JPanel;
import org.mongkie.exporter.spi.Exporter;
import org.mongkie.exporter.spi.GraphExporter;

/**
 *
 * @author Yeongjun Jang <yjjang@kribb.re.kr>
 */
final class GraphExportGlobalSettingUI extends javax.swing.JPanel implements Exporter.GlobalSettingUI<GraphExporter> {

    private GraphExporter exporter;

    /**
     * Creates new form GraphExportSettingUI
     */
    GraphExportGlobalSettingUI() {
        initComponents();
    }

    private void setExportSelectionOnly(boolean selectionOnly) {
        exportSettingButtonGroup.setSelected((selectionOnly ? selectionOnlyRadioButton.getModel() : exportAllRadioButton.getModel()), true);
    }

    boolean isExportSelectionOnly() {
        return exportSettingButtonGroup.isSelected(selectionOnlyRadioButton.getModel());
    }

    private void refreshUI() {
        if (exporter.supportsSelectionOnly()) {
            setExportSelectionOnly(exporter.isExportSelectionOnly());
            selectionOnlyRadioButton.setEnabled(true);
        } else {
            setExportSelectionOnly(false);
            selectionOnlyRadioButton.setEnabled(false);
        }
    }

    @Override
    public void load(GraphExporter exporter) {
        this.exporter = exporter;
        refreshUI();
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public void apply() {
        exporter.setExportSelectionOnly(exporter.supportsSelectionOnly() ? isExportSelectionOnly() : false);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        exportSettingButtonGroup = new javax.swing.ButtonGroup();
        exportAllRadioButton = new javax.swing.JRadioButton();
        selectionOnlyRadioButton = new javax.swing.JRadioButton();
        labelExportAll = new javax.swing.JLabel();
        labelSelectionOnly = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());

        exportSettingButtonGroup.add(exportAllRadioButton);
        exportAllRadioButton.setSelected(true);
        exportAllRadioButton.setText(org.openide.util.NbBundle.getMessage(GraphExportGlobalSettingUI.class, "GraphExportGlobalSettingUI.exportAllRadioButton.text")); // NOI18N

        exportSettingButtonGroup.add(selectionOnlyRadioButton);
        selectionOnlyRadioButton.setText(org.openide.util.NbBundle.getMessage(GraphExportGlobalSettingUI.class, "GraphExportGlobalSettingUI.selectionOnlyRadioButton.text")); // NOI18N
        selectionOnlyRadioButton.setEnabled(false);

        labelExportAll.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labelExportAll.setForeground(new java.awt.Color(102, 102, 102));
        labelExportAll.setText(org.openide.util.NbBundle.getMessage(GraphExportGlobalSettingUI.class, "GraphExportGlobalSettingUI.labelExportAll.text")); // NOI18N

        labelSelectionOnly.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labelSelectionOnly.setForeground(new java.awt.Color(102, 102, 102));
        labelSelectionOnly.setText(org.openide.util.NbBundle.getMessage(GraphExportGlobalSettingUI.class, "GraphExportGlobalSettingUI.labelSelectionOnly.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(exportAllRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelExportAll))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(selectionOnlyRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelSelectionOnly)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exportAllRadioButton)
                    .addComponent(labelExportAll))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectionOnlyRadioButton)
                    .addComponent(labelSelectionOnly))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton exportAllRadioButton;
    private javax.swing.ButtonGroup exportSettingButtonGroup;
    private javax.swing.JLabel labelExportAll;
    private javax.swing.JLabel labelSelectionOnly;
    private javax.swing.JRadioButton selectionOnlyRadioButton;
    // End of variables declaration//GEN-END:variables
}
