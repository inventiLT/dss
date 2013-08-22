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

package eu.europa.ec.markt.dss.applet;

import eu.europa.ec.markt.dss.applet.model.SignatureWizardModel;
import eu.europa.ec.markt.dss.applet.wizard.AbstractWizardPanel;
import eu.europa.ec.markt.dss.common.SignatureTokenType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

/**
 * Let the user choose which kind of API will be used for the signing. 
 *
 * @version $Revision: 1867 $ - $Date: 2013-04-08 13:44:56 +0200 (Mon, 08 Apr 2013) $
 */

@SuppressWarnings("serial")
public class SignatureTokenAPIPanel extends AbstractWizardPanel {

    private static final Logger LOG = Logger.getLogger(SignatureTokenAPIPanel.class.getName());

    public static final String ID = "TOKEN";

    private final SignatureWizardModel signatureModel;

    /** Creates new form ChooseSignatureTokenAPI */
    public SignatureTokenAPIPanel(SignatureWizardModel model) {
        this.signatureModel = model;
        initComponents();

        // getWizard().setNextFinishButtonEnabled(false);

        jRadioButton1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                signatureModel.setTokenType(SignatureTokenType.PKCS11);
                getWizard().setNextFinishButtonEnabled(true);
            }
        });

        jRadioButton2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                signatureModel.setTokenType(SignatureTokenType.PKCS12);
                getWizard().setNextFinishButtonEnabled(true);
            }
        });

        jRadioButton3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                signatureModel.setTokenType(SignatureTokenType.MSCAPI);
                getWizard().setNextFinishButtonEnabled(true);
            }
        });
        
        moccaRadio.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                signatureModel.setTokenType(SignatureTokenType.MOCCA);
                getWizard().setNextFinishButtonEnabled(true);
            }
        });

        if (model.getTokenType() != null) {
            switch (model.getTokenType()) {
            case PKCS11:
                jRadioButton1.setSelected(true);
                break;
            case PKCS12:
                jRadioButton2.setSelected(true);
                break;
            case MSCAPI:
                jRadioButton3.setSelected(true);
                break;
            case MOCCA: 
                moccaRadio.setSelected(true);
                break;
            }
        }

    }

    @Override
    public Object getPanelDescriptorIdentifier() {
        return ID;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        moccaRadio = new javax.swing.JRadioButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jRadioButton1.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setMnemonic(java.util.ResourceBundle.getBundle("eu/europa/ec/markt/dss/applet/i18n").getString("PKCS11_MNEMONIC").charAt(0));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("eu/europa/ec/markt/dss/applet/i18n"); // NOI18N
        jRadioButton1.setText(bundle.getString("PKCS11")); // NOI18N
        jRadioButton1.setName("pkcs11"); // NOI18N

        jRadioButton2.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setMnemonic(java.util.ResourceBundle.getBundle("eu/europa/ec/markt/dss/applet/i18n").getString("PKCS12_MNEMONIC").charAt(0));
        jRadioButton2.setText(bundle.getString("PKCS12")); // NOI18N
        jRadioButton2.setName("pkcs12"); // NOI18N

        jRadioButton3.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setMnemonic(java.util.ResourceBundle.getBundle("eu/europa/ec/markt/dss/applet/i18n").getString("MSCAPI").charAt(0));
        jRadioButton3.setText(bundle.getString("MSCAPI")); // NOI18N
        jRadioButton3.setName("mscapi"); // NOI18N

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText(bundle.getString("TOKEN_API")); // NOI18N

        moccaRadio.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(moccaRadio);
        moccaRadio.setText("MOCCA");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(moccaRadio)
                    .addComponent(jLabel1)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton1))
                .addContainerGap(274, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(moccaRadio)
                .addContainerGap(181, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton moccaRadio;
    // End of variables declaration//GEN-END:variables

    /*
     * (non-Javadoc)
     * 
     * @see eu.europa.ec.markt.dss.applet.wizard.WizardPanel#getNextPanelDescriptor()
     */
    @Override
    public Object getNextPanelDescriptor() {
        if (signatureModel.getTokenType() != null) {
            switch (signatureModel.getTokenType()) {
            case PKCS11:
                return PKCS11ParamsPanel.ID;
            case PKCS12:
                return PKCS12ParamsPanel.ID;
            case MSCAPI:
                return ChooseCertificatePanel.ID;
            case MOCCA:
                return MOCCAParamsPanel.ID;
            default:
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Object getBackPanelDescriptor() {
        return ChooseSignaturePanel.ID;
    }

    @Override
    public void aboutToDisplayPanel() {
        getWizard().setStepsProgression(3);
        if(new MOCCAAdapter().isMOCCAAvailable()) {
            moccaRadio.setVisible(true);
        } else {
            moccaRadio.setVisible(false);
        }
    }

    @Override
    public boolean skipPanel() {
        if (signatureModel.isPreconfiguredTokenType()) {
            LOG.info("TokenType already configured : " + signatureModel.getTokenType() + ", displaying next panel "
                    + getNextPanelDescriptor());
            return true;
        } else {
            return false;
        }
    }

}