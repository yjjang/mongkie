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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JPanel;
import org.mongkie.datatable.ColumnType;
import org.mongkie.datatable.spi.DataAction;
import org.mongkie.datatable.spi.GraphDataTable;
import org.netbeans.validation.api.AbstractValidator;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;
import org.netbeans.validation.api.builtin.stringvalidation.StringValidators;
import org.netbeans.validation.api.ui.ValidationGroup;
import org.netbeans.validation.api.ui.swing.ValidationPanel;
import prefuse.data.Table;

/**
 *
 * @author Yeongjun Jang <yjjang@kribb.re.kr>
 */
public class AddNewColumnUI extends javax.swing.JPanel implements DataAction.UI<GraphDataTable, AddNewColumn>, ItemListener {

    /**
     * Creates new form AddNewColumnUI
     */
    private AddNewColumnUI() {
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

        actionTitleLabel = new javax.swing.JLabel();
        columnTitleLabel = new javax.swing.JLabel();
        columnTypeLabel = new javax.swing.JLabel();
        columnTypeComboBox = new javax.swing.JComboBox();
        initialValueLabel = new javax.swing.JLabel();
        initialValueTextField = new javax.swing.JTextField();
        columnTitleTextField = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(330, 180));

        actionTitleLabel.setFont(actionTitleLabel.getFont().deriveFont(actionTitleLabel.getFont().getStyle() | java.awt.Font.BOLD));
        actionTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        actionTitleLabel.setText(org.openide.util.NbBundle.getMessage(AddNewColumnUI.class, "AddNewColumnUI.actionTitleLabel.text")); // NOI18N

        columnTitleLabel.setText(org.openide.util.NbBundle.getMessage(AddNewColumnUI.class, "AddNewColumnUI.columnTitleLabel.text")); // NOI18N

        columnTypeLabel.setText(org.openide.util.NbBundle.getMessage(AddNewColumnUI.class, "AddNewColumnUI.columnTypeLabel.text")); // NOI18N

        initialValueLabel.setText(org.openide.util.NbBundle.getMessage(AddNewColumnUI.class, "AddNewColumnUI.initialValueLabel.text")); // NOI18N

        initialValueTextField.setText(org.openide.util.NbBundle.getMessage(AddNewColumnUI.class, "AddNewColumnUI.initialValueTextField.text")); // NOI18N
        initialValueTextField.setName(org.openide.util.NbBundle.getMessage(AddNewColumnUI.class, "AddNewColumnUI.initialValueTextField.name")); // NOI18N

        columnTitleTextField.setText(org.openide.util.NbBundle.getMessage(AddNewColumnUI.class, "AddNewColumnUI.columnTitleTextField.text")); // NOI18N
        columnTitleTextField.setName(org.openide.util.NbBundle.getMessage(AddNewColumnUI.class, "AddNewColumnUI.columnTitleTextField.name")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(columnTitleLabel)
                            .addComponent(columnTypeLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(columnTitleTextField)
                            .addComponent(columnTypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(initialValueLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(initialValueTextField))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(actionTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(actionTitleLabel)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(columnTitleLabel)
                    .addComponent(columnTitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(columnTypeLabel)
                    .addComponent(columnTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(initialValueLabel)
                    .addComponent(initialValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel actionTitleLabel;
    private javax.swing.JLabel columnTitleLabel;
    private javax.swing.JTextField columnTitleTextField;
    private javax.swing.JComboBox columnTypeComboBox;
    private javax.swing.JLabel columnTypeLabel;
    private javax.swing.JLabel initialValueLabel;
    private javax.swing.JTextField initialValueTextField;
    // End of variables declaration//GEN-END:variables

    @Override
    public void load(GraphDataTable table, AddNewColumn action) {
        this.table = table.getModel().getTable();
        this.action = action;
        columnTypeComboBox.addItemListener(this);
        for (ColumnType type : ColumnType.values()) {
            columnTypeComboBox.addItem(type);
        }
    }
    private Table table;
    private AddNewColumn action;

    @Override
    public boolean apply(boolean ok) {
        if (ok) {
            action.setTitle(columnTitleTextField.getText());
            ColumnType type = (ColumnType) columnTypeComboBox.getSelectedItem();
            action.setType(type.getType());
            action.setDefaultValue(type.getValue(initialValueTextField.getText().trim()));
        }
        columnTitleTextField.setText(null);
        columnTypeComboBox.removeItemListener(this);
        columnTypeComboBox.removeAllItems();
        this.table = null;
        this.action = null;
        return ok;
    }

    @Override
    public JPanel getPanel() {
        if (validationPanel == null) {
            validationPanel = new ValidationPanel();
            validationPanel.setInnerComponent(this);
            ValidationGroup validationGroup = validationPanel.getValidationGroup();
            validationGroup.add(columnTitleTextField,
                    StringValidators.REQUIRE_NON_EMPTY_STRING, StringValidators.NO_WHITESPACE,
                    new AbstractValidator<String>(String.class) {
                        @Override
                        public void validate(Problems problems, String compName, String model) {
                            if (table.getColumn(model) != null) {
                                problems.add("Column already exists with the same title");
                            }
                        }
                    });
            validationGroup.add(initialValueTextField,
                    new AbstractValidator<String>(String.class) {
                        @Override
                        public void validate(Problems problems, String compName, String model) {
                            Validator<String> v = getValidator((ColumnType) columnTypeComboBox.getSelectedItem());
                            if (v != null) {
                                v.validate(problems, compName, model);
                            }
                        }
                    });
        }
        return validationPanel;
    }
    private ValidationPanel validationPanel;

    private Validator getValidator(ColumnType type) {
        switch (type) {
            case INTEGER:
                return StringValidators.REQUIRE_VALID_INTEGER;
            case DOUBLE:
                return StringValidators.REQUIRE_VALID_NUMBER;
            default:
                return null;
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            initialValueTextField.setText(((ColumnType) e.getItem()).getDefaultValue().toString());
        }
    }

    static AddNewColumnUI getInstance() {
        return Holder.UI;
    }

    private static class Holder {

        private static final AddNewColumnUI UI = new AddNewColumnUI();
    }
}
