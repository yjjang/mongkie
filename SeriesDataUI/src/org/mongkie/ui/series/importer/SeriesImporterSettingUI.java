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
 * SeriesImporterSettingUI.java
 *
 * Created on Jul 6, 2011, 7:58:16 PM
 */
package org.mongkie.ui.series.importer;

import javax.swing.JPanel;
import org.mongkie.importer.spi.Importer;
import org.mongkie.importer.spi.ImporterBuilder;
import org.mongkie.series.SeriesController;
import org.mongkie.series.SeriesImporter;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import prefuse.data.Table;

/**
 *
 * @author Yeongjun Jang <yjjang@kribb.re.kr>
 */
@ServiceProvider(service = Importer.SettingUI.class)
public class SeriesImporterSettingUI extends JPanel implements Importer.SettingUI<SeriesImporter> {

    /**
     * Creates new form SeriesImporterSettingUI
     */
    public SeriesImporterSettingUI() {
        initComponents();
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

        keyFieldLabel = new javax.swing.JLabel();
        keyFieldComboBox = new javax.swing.JComboBox();
        keyFieldExplainLabel = new javax.swing.JLabel();
        headerRecordCheckBox = new javax.swing.JCheckBox();

        keyFieldLabel.setText(org.openide.util.NbBundle.getMessage(SeriesImporterSettingUI.class, "SeriesImporterSettingUI.keyFieldLabel.text")); // NOI18N

        keyFieldComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        keyFieldExplainLabel.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        keyFieldExplainLabel.setForeground(java.awt.Color.blue);
        keyFieldExplainLabel.setText(org.openide.util.NbBundle.getMessage(SeriesImporterSettingUI.class, "SeriesImporterSettingUI.keyFieldExplainLabel.text")); // NOI18N

        headerRecordCheckBox.setSelected(true);
        headerRecordCheckBox.setText(org.openide.util.NbBundle.getMessage(SeriesImporterSettingUI.class, "SeriesImporterSettingUI.headerRecordCheckBox.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(keyFieldLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(keyFieldComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(headerRecordCheckBox)
                    .addComponent(keyFieldExplainLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(keyFieldLabel)
                    .addComponent(keyFieldComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(keyFieldExplainLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(headerRecordCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox headerRecordCheckBox;
    private javax.swing.JComboBox keyFieldComboBox;
    private javax.swing.JLabel keyFieldExplainLabel;
    private javax.swing.JLabel keyFieldLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void load(SeriesImporter importer) {
        this.importer = importer;
        keyFieldComboBox.removeAllItems();
        Table nodeTable = Lookup.getDefault().lookup(SeriesController.class).getModel().getDisplay().getGraph().getNodeTable();
        for (int i = 0; i < nodeTable.getColumnCount(); i++) {
            keyFieldComboBox.addItem(nodeTable.getColumnName(i));
        }
        String keyField = importer.getKeyField();
        if ((keyField == null || nodeTable.getColumnNumber(keyField) < 0)) {
            keyFieldComboBox.setSelectedIndex(0);
        } else {
            keyFieldComboBox.setSelectedItem(keyField);
        }
        headerRecordCheckBox.setSelected(importer.hasHeaderRecord());
    }
    private SeriesImporter importer;

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public void apply() {
        importer.setKeyField((String) keyFieldComboBox.getSelectedItem());
        importer.setHasHeaderRecord(headerRecordCheckBox.isSelected());
    }

    @Override
    public boolean isUIForImporter(ImporterBuilder builder) {
        return builder instanceof SeriesImporterBuilder;
    }
}