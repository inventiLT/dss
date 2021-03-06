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

package eu.europa.ec.markt.dss.validation.report;

import eu.europa.ec.markt.dss.validation.CertificateStatus;
import eu.europa.ec.markt.dss.validation.CertificateValidity;

import java.util.Date;

/**
 * Representation of a certificate status, used to indicate the success or the failure of the verification of revocation
 * data
 * 
 *
 * @version $Revision: 1867 $ - $Date: 2013-04-08 13:44:56 +0200 (Mon, 08 Apr 2013) $
 */

public class RevocationVerificationResult {

    private CertificateStatus certificateStatus;

    /**
     * The default constructor for RevocationVerificationResult.
     */
    public RevocationVerificationResult(CertificateStatus certificateStatus) {
        if (certificateStatus != null) {
            this.certificateStatus = certificateStatus;
        } else {
            this.certificateStatus = new CertificateStatus();
            this.certificateStatus.setValidity(CertificateValidity.UNKNOWN);
        }
    }

    /**
     * 
     * The default constructor for RevocationVerificationResult.
     */
    public RevocationVerificationResult() {
        this(null);
    }
    
    /**
     * 
     * @return
     */
    public CertificateValidity getStatus() {
        if (certificateStatus == null) {
            return CertificateValidity.UNKNOWN;
        }
        return certificateStatus.getValidity();
    }

    /**
     * 
     * @return
     */
    public Date getRevocationDate() {
        if (certificateStatus == null) {
            return null;
        }
        if (getStatus() == CertificateValidity.REVOKED) {
            return certificateStatus.getRevocationDate();
        } else {
            return null;
        }
    }

    /**
     * 
     * @return
     */
    public String getIssuer() {
        if (certificateStatus == null) {
            return null;
        }
        if (getStatus() == CertificateValidity.REVOKED) {
            if (certificateStatus.getIssuerCertificate() != null) {
                return certificateStatus.getIssuerCertificate().getSubjectDN().toString();
            }
        }
        return null;
    }

    /**
     * 
     * @return
     */
    public Date getIssuingTime() {
        if (certificateStatus == null) {
            return null;
        }
        if (getStatus() == CertificateValidity.REVOKED) {
            return certificateStatus.getRevocationObjectIssuingTime();
        } else {
            return null;
        }
    }

}
