/*
 *  This file is part of MONGKIE. Visit <http://www.mongkie.org/> for details.
 *  Copyright (C) 2011 Korean Bioinformation Center(KOBIC)
 * 
 *  MONGKIE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  MONGKIE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 * 
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * RandomSettingUI.java
 *
 * Created on Mar 28, 2011, 6:23:05 PM
 */
package org.mongkie.clustering.plugins.random;

import javax.swing.JPanel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import org.mongkie.clustering.spi.ClusteringBuilder;

/**
 * 
 * @author Yeongjun Jang <yjjang@kribb.re.kr>
 */
public class RandomSettingUI extends javax.swing.JPanel implements ClusteringBuilder.SettingUI<Random> {

    private SpinnerModel clusterSizeSpinnerModel;

    /** Creates new form RandomSettingUI */
    RandomSettingUI() {
        clusterSizeSpinnerModel = new SpinnerNumberModel(
                Integer.valueOf(Random.MIN_CLUSTER_SIZE),
                Integer.valueOf(Random.MIN_CLUSTER_SIZE), null,
                Integer.valueOf(1));
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        clusterSizeLabel = new javax.swing.JLabel();
        clusterSizeSpinner = new javax.swing.JSpinner();

        clusterSizeLabel.setText(org.openide.util.NbBundle.getMessage(RandomSettingUI.class, "RandomSettingUI.clusterSizeLabel.text")); // NOI18N

        clusterSizeSpinner.setModel(clusterSizeSpinnerModel);
        clusterSizeSpinner.setPreferredSize(new java.awt.Dimension(50, 26));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clusterSizeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clusterSizeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clusterSizeLabel)
                    .addComponent(clusterSizeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public void load(Random random) {
        clusterSizeSpinner.setValue(random.getClusterSize());
    }

    @Override
    public void apply(Random random) {
        random.setClusterSize((Integer) clusterSizeSpinner.getValue());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel clusterSizeLabel;
    private javax.swing.JSpinner clusterSizeSpinner;
    // End of variables declaration//GEN-END:variables
}
