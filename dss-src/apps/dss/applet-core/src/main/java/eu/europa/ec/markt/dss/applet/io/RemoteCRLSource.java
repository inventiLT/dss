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

package eu.europa.ec.markt.dss.applet.io;

import eu.europa.ec.markt.dss.CannotFetchDataException;
import eu.europa.ec.markt.dss.applet.shared.CRLRequestMessage;
import eu.europa.ec.markt.dss.applet.shared.CRLResponseMessage;
import eu.europa.ec.markt.dss.validation.crl.CRLSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;

/**
 * CRLSource that use the server backend for the operation execution.
 * 
 *
 * @version $Revision: 1867 $ - $Date: 2013-04-08 13:44:56 +0200 (Mon, 08 Apr 2013) $
 */

public class RemoteCRLSource extends AbstractRemoteService<CRLRequestMessage, CRLResponseMessage> implements
        CRLSource {

    @Override
    public X509CRL findCrl(X509Certificate certificate, X509Certificate issuerCertificate) throws IOException {

        try {
            CRLRequestMessage msg = new CRLRequestMessage();
            msg.setCertificate(certificate.getEncoded());
            msg.setIssuerCert(issuerCertificate.getEncoded());

            CRLResponseMessage response = sendAndReceive(msg);

            if (response.getCrl() != null) {
                CertificateFactory factory = CertificateFactory.getInstance("X509");
                X509CRL crl = (X509CRL) factory.generateCRL(new ByteArrayInputStream(response.getCrl()));
                return crl;
            } else {
                return null;
            }
        } catch (CertificateException e) {
            throw new IOException(e);
        } catch (CRLException e) {
            throw new IOException(e);
        }
    }

}
