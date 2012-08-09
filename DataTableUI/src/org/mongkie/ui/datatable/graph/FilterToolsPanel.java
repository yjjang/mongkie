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
package org.mongkie.ui.datatable.graph;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author Yeongjun Jang <yjjang@kribb.re.kr>
 */
public class FilterToolsPanel extends javax.swing.JPanel {

    /** Creates new form FilterToolsPanel */
    public FilterToolsPanel() {
        initComponents();
    }
    
    public List<JComponent> getToolComponents() {
        List<JComponent> tools = new ArrayList<JComponent>();
        tools.add(filterLabel);
        tools.add(filterInputTextField);
        tools.add(filterColumnComboBox);
        return tools;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filterLabel = new javax.swing.JLabel();
        filterInputTextField = new javax.swing.JTextField();
        filterColumnComboBox = new javax.swing.JComboBox();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setFocusable(false);
        setRequestFocusEnabled(false);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        filterLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/mongkie/ui/datatable/resources/funnel.png"))); // NOI18N
        filterLabel.setText(org.openide.util.NbBundle.getMessage(FilterToolsPanel.class, "FilterToolsPanel.filterLabel.text")); // NOI18N
        add(filterLabel);

        filterInputTextField.setText(org.openide.util.NbBundle.getMessage(FilterToolsPanel.class, "FilterToolsPanel.filterInputTextField.text")); // NOI18N
        filterInputTextField.setPreferredSize(new java.awt.Dimension(100, 26));
        add(filterInputTextField);

        filterColumnComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Symbol" }));
        filterColumnComboBox.setPreferredSize(new java.awt.Dimension(100, 26));
        add(filterColumnComboBox);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox filterColumnComboBox;
    private javax.swing.JTextField filterInputTextField;
    private javax.swing.JLabel filterLabel;
    // End of variables declaration//GEN-END:variables
}