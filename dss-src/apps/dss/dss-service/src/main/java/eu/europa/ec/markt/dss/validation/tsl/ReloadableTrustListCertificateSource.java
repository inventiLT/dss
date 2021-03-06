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

package eu.europa.ec.markt.dss.validation.tsl;

import eu.europa.ec.markt.dss.CannotFetchDataException;
import eu.europa.ec.markt.dss.validation.certificate.CertificateAndContext;
import eu.europa.ec.markt.dss.validation.certificate.CertificateSource;
import eu.europa.ec.markt.dss.validation.https.HTTPDataLoader;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.ConfigurationException;
import javax.security.auth.x500.X500Principal;

import org.springframework.core.io.Resource;

/**
 * This CertificateSource reload the list of TrustedList when the method refresh is called.
 * 
 * 
 * @version $Revision: 1867 $ - $Date: 2013-04-08 13:44:56 +0200 (Mon, 08 Apr 2013) $
 */

public class ReloadableTrustListCertificateSource implements CertificateSource {

    private static final Logger LOG = Logger.getLogger(ReloadableTrustListCertificateSource.class.getName());

    private TrustedListsCertificateSource currentSource;

    private HTTPDataLoader tslLoader;

    private boolean checkSignature;

    private Resource lotlCertificate;

    private String lotlUrl;

    /**
     * @param tslLoader the tslLoader to set
     */
    public void setTslLoader(HTTPDataLoader tslLoader) {
        this.tslLoader = tslLoader;
    }

    /**
     * @param checkSignature the checkSignature to set
     */
    public void setCheckSignature(boolean checkSignature) {
        this.checkSignature = checkSignature;
    }

    /**
     * @param lotlCertificate the lotlCertificate to set
     */
    public void setLotlCertificate(Resource lotlCertificate) {
        this.lotlCertificate = lotlCertificate;
    }

    /**
     * @param lotlURl the lotlURl to set
     */
    public void setLotlUrl(String lotlURl) {
        this.lotlUrl = lotlURl;
    }

    /**
     * Reload the TrustedList
     */
    private Thread reload;

    public synchronized void refresh() {
        try {
            reload = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        LOG.info("Reload Trusted List");
                        TrustedListsCertificateSource newSource = new TrustedListsCertificateSource();
                        newSource.setTslLoader(tslLoader);
                        newSource.setCheckSignature(checkSignature);
                        newSource.setLotlCertificate(lotlCertificate);
                        newSource.setLotlUrl(lotlUrl);
                        newSource.init();
                        currentSource = newSource;
                    } catch (IOException e) {
                        e.printStackTrace();
                        LOG.log(Level.SEVERE, "", e);
                    } catch (ConfigurationException e) {
                        e.printStackTrace();
                        LOG.log(Level.SEVERE, "", e);
                    }
                }
            });
            reload.start();
        } catch (CannotFetchDataException e) {
            e.printStackTrace();
            LOG.log(Level.SEVERE, "", e);
        }
    }

    @Override
    public List<CertificateAndContext> getCertificateBySubjectName(X500Principal subjectName) {
        if (currentSource == null) {
            return Collections.emptyList();
        }
        return currentSource.getCertificateBySubjectName(subjectName);
    }
}
