/*
 * This file is part of MONGKIE. Visit <http://www.mongkie.org/> for details.
 * Copyright (C) 2011 Korean Bioinformation Center(KOBIC)
 *
 * MONGKIE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MONGKE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mongkie.ui.series;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.mongkie.perspective.spi.BottomTopComponent;
import org.mongkie.series.SeriesData;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.WindowManager;

/**
 *
 * @author Yeongjun Jang <yjjang@kribb.re.kr>
 */
@ServiceProvider(service = BottomTopComponent.class)
public final class SliderTopComponent extends javax.swing.JPanel implements BottomTopComponent {

    private final Timer timer;

    public SliderTopComponent() {
        initComponents();
        seriesSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (seriesSlider.getValueIsAdjusting()) {
                    return;
                }
                HeatMapTopComponent.findInstance().setSlidingValue(seriesSlider.getValue());
            }
        });
        timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!seriesSlider.isEnabled()) {
                    return;
                }
                seriesSlider.setValue((seriesSlider.getValue() + 1) % (seriesSlider.getMaximum() + 1));
            }
        });
        setVisible(false);
        // Add this pane on the bottom of the main window
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                JFrame frame = (JFrame) WindowManager.getDefault().getMainWindow();
                //Replace the status line with our creation
                JComponent statusLinePanel = null;
                Container p = null;
                for (Component c : getAllComponents(frame.getContentPane())) {
                    String n = c.getName();
                    if (n != null && n.equals("statusLine")) {
                        statusLinePanel = (JComponent) c;
                        p = statusLinePanel.getParent();
                        break;
                    }
                }
                if (statusLinePanel != null && p != null) {
                    p.remove(statusLinePanel);
                    JPanel southPanel = new JPanel(new BorderLayout());
                    southPanel.add(statusLinePanel, BorderLayout.SOUTH);
                    southPanel.add(getComponent(), BorderLayout.CENTER);
                    p.add(southPanel, BorderLayout.SOUTH);
                }
            }
        });
    }

    private List<Component> getAllComponents(final Container c) {
        Component[] comps = c.getComponents();
        List<Component> compList = new ArrayList<Component>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof Container) {
                compList.addAll(getAllComponents((Container) comp));
            }
        }
        Collections.reverse(compList);
        return compList;
    }

    void heatmapUpdated(SeriesData series) {
        seriesSlider.setMaximum(series.isEmpty() ? 0 : series.getColumnCount() - 1);
        Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
        String[] cols = series.getColumns();
        for (int i = 0; i < series.getColumnCount(); i++) {
            labels.put(i, new JLabel(cols[i]));
        }
        seriesSlider.setLabelTable(labels);
        setEnabled(!series.isEmpty());
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        seriesSlider.setEnabled(enabled);

        startButton.setEnabled(enabled);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        if (!enabled) {
            if (timer.isRunning()) {
                timer.stop();
            }
            seriesSlider.setValue(0);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startButton = new javax.swing.JButton();
        pauseButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        seriesSlider = new javax.swing.JSlider();

        startButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/mongkie/ui/series/resources/play.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(startButton, org.openide.util.NbBundle.getMessage(SliderTopComponent.class, "SliderTopComponent.startButton.text")); // NOI18N
        startButton.setEnabled(false);
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        pauseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/mongkie/ui/series/resources/pause.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(pauseButton, org.openide.util.NbBundle.getMessage(SliderTopComponent.class, "SliderTopComponent.pauseButton.text")); // NOI18N
        pauseButton.setEnabled(false);
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });

        stopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/mongkie/ui/series/resources/stop.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(stopButton, org.openide.util.NbBundle.getMessage(SliderTopComponent.class, "SliderTopComponent.stopButton.text")); // NOI18N
        stopButton.setEnabled(false);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        seriesSlider.setMaximum(0);
        seriesSlider.setPaintLabels(true);
        seriesSlider.setPaintTicks(true);
        seriesSlider.setSnapToTicks(true);
        seriesSlider.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(startButton)
                .addGap(5, 5, 5)
                .addComponent(pauseButton)
                .addGap(5, 5, 5)
                .addComponent(stopButton)
                .addGap(5, 5, 5)
                .addComponent(seriesSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(startButton))
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(pauseButton))
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(stopButton))
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(seriesSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        if (!timer.isRunning()) {
            timer.start();
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
        }
    }//GEN-LAST:event_startButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        timer.stop();
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        seriesSlider.setValue(0);
    }//GEN-LAST:event_stopButtonActionPerformed

    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseButtonActionPerformed
        if (timer.isRunning()) {
            timer.stop();
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
            stopButton.setEnabled(true);
        }
    }//GEN-LAST:event_pauseButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton pauseButton;
    private javax.swing.JSlider seriesSlider;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public JComponent getComponent() {
        return this;
    }
}
