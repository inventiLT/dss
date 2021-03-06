/*
 * DSS - Digital Signature Services
 *
 * Copyright (C) 2011 European Commission, Directorate-General Internal Market and Services (DG MARKT), B-1049 Bruxelles/Brussel
 *
 * Developed by: 2011 ARHS Developments S.A. (rue Nicolas Bové 2B, L-1253 Luxembourg) http://www.arhs-developments.com
 *
 * This file is part of the "DSS - Digital Signature Services" project.
 *
 * "DSS - Digital Signature Services" is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * DSS is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * "DSS - Digital Signature Services".  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.markt.tlmanager.view.signature;

import eu.europa.ec.markt.tlmanager.core.Configuration;
import eu.europa.ec.markt.tlmanager.core.validation.ValidationLogger;
import eu.europa.ec.markt.tlmanager.core.validation.ValidationLogger.Message;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;

/**
 * Panel that wraps the controls for the first signature step.
 * 
 *
 * @version $Revision: 1867 $ - $Date: 2013-04-08 13:44:56 +0200 (Mon, 08 Apr 2013) $
 */

public class SignatureStep1 extends javax.swing.JPanel implements AWTEventListener {
    private static final ResourceBundle uiKeys = ResourceBundle.getBundle("eu/europa/ec/markt/tlmanager/uiKeys",
            Configuration.getInstance().getLocale());

    private SignatureWizardStep1 wizard;
    private DefaultListModel validationItemModel;
    private MessageLabelRenderer itemLabelRenderer;

    /**
     * Instantiates a new signature step1.
     */
    public SignatureStep1() {
        validationItemModel = new DefaultListModel();
        itemLabelRenderer = new MessageLabelRenderer();

        initComponents();
        validationTitle.setTitle(uiKeys.getString("SignatureStep1.validationTitle.title"));

        validationLog.setCellRenderer(itemLabelRenderer);
        
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
    }

    /**
     * Instantiates a new signature step1.
     * 
     * @param wizard the wizard
     */
    public SignatureStep1(final SignatureWizardStep1 wizard) {
        this();
        this.wizard = wizard;
    }

    /**
     * Clears the validationItemModel and adds all available validation items.
     * 
     * @param validationMessages the available validationMessages
     */
    public void setValidationMessages(List<ValidationLogger.Message> validationMessages) {
        validationItemModel.clear();
        for (Message message : validationMessages) {
            validationItemModel.addElement(message);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        validationTitle = new eu.europa.ec.markt.tlmanager.view.common.TitledPanel();
        validationLogScrollPane = new javax.swing.JScrollPane();
        validationLog = new javax.swing.JList();
        descriptionLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        titleLabel.setText(uiKeys.getString("SignatureStep1.titleLabel.text")); // NOI18N

        validationLog.setModel(validationItemModel);
        validationLogScrollPane.setViewportView(validationLog);

        javax.swing.GroupLayout validationTitleLayout = new javax.swing.GroupLayout(validationTitle);
        validationTitle.setLayout(validationTitleLayout);
        validationTitleLayout.setHorizontalGroup(validationTitleLayout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                validationTitleLayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addComponent(validationLogScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 421,
                                Short.MAX_VALUE).addContainerGap()));
        validationTitleLayout.setVerticalGroup(validationTitleLayout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                validationTitleLayout
                        .createSequentialGroup()
                        .addComponent(validationLogScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 80,
                                Short.MAX_VALUE).addContainerGap()));

        descriptionLabel.setText(uiKeys.getString("SignatureStep1.descriptionLabel.text")); // NOI18N

        jLabel1.setText(uiKeys.getString("SignatureStep1.jLabel1.text")); // NOI18N

        jLabel2.setText(uiKeys.getString("SignatureStep1.jLabel2.text")); // NOI18N

        jLabel3.setText(uiKeys.getString("SignatureStep1.jLabel3.text")); // NOI18N

        jLabel4.setText(uiKeys.getString("SignatureStep1.jLabel4.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()
                                .addGroup(
                                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(
                                                        layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(
                                                                        layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(titleLabel)
                                                                                .addComponent(jLabel2)
                                                                                .addComponent(
                                                                                        validationTitle,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        Short.MAX_VALUE)))
                                                .addGroup(
                                                        layout.createSequentialGroup()
                                                                .addGap(59, 59, 59)
                                                                .addGroup(
                                                                        layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jLabel4)
                                                                                .addComponent(jLabel3)))
                                                .addGroup(
                                                        layout.createSequentialGroup().addContainerGap()
                                                                .addComponent(jLabel1))
                                                .addGroup(
                                                        layout.createSequentialGroup().addContainerGap()
                                                                .addComponent(descriptionLabel))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(titleLabel)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(4, 4, 4)
                        .addComponent(jLabel3)
                        .addGap(7, 7, 7)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(descriptionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(validationTitle, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JList validationLog;
    private javax.swing.JScrollPane validationLogScrollPane;
    private eu.europa.ec.markt.tlmanager.view.common.TitledPanel validationTitle;
    // End of variables declaration//GEN-END:variables

    /** @{inheritDoc}
     * Enables an override if 'Shift' is pressed.
     */
    @Override
    public void eventDispatched(AWTEvent e) {
        if (!(e instanceof KeyEvent)) {
            return;
        }
        
        KeyEvent ke = (KeyEvent) e;
        wizard.overrideEnabled(ke.isShiftDown()); 
    }
}