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

package eu.europa.ec.markt.dss.ws.report;

import eu.europa.ec.markt.dss.validation.report.SignatureLevelXL;

/**
 * Wrap data of a SignatureLevelXL. Used to expose the information in the Webservice.
 * 
 *
 * @version $Revision: 1867 $ - $Date: 2013-04-08 13:44:56 +0200 (Mon, 08 Apr 2013) $
 */

public class WSSignatureLevelXL {

    private String levelReached;
    private String certificateValuesVerification;
    private String revocationValuesVerification;

    /**
     * The default constructor for WSSignatureLevelXL.
     */
    public WSSignatureLevelXL() {
    }

    /**
     * 
     * The default constructor for WSSignatureLevelXL.
     * 
     * @param level
     */
    public WSSignatureLevelXL(SignatureLevelXL level) {
        if (level.getLevelReached() != null) {
            levelReached = level.getLevelReached().toString();
        }
        if (level.getCertificateValuesVerification() != null) {
            certificateValuesVerification = level.getCertificateValuesVerification().getStatus().toString();
        }
        if (level.getRevocationValuesVerification() != null) {
            revocationValuesVerification = level.getRevocationValuesVerification().getStatus().toString();
        }
    }

    /**
     * @return the levelReached
     */
    public String getLevelReached() {
        return levelReached;
    }

    /**
     * @param levelReached the levelReached to set
     */
    public void setLevelReached(String levelReached) {
        this.levelReached = levelReached;
    }

    /**
     * @return the certificateValuesVerification
     */
    public String getCertificateValuesVerification() {
        return certificateValuesVerification;
    }

    /**
     * @param certificateValuesVerification the certificateValuesVerification to set
     */
    public void setCertificateValuesVerification(String certificateValuesVerification) {
        this.certificateValuesVerification = certificateValuesVerification;
    }

    /**
     * @return the revocationValuesVerification
     */
    public String getRevocationValuesVerification() {
        return revocationValuesVerification;
    }

    /**
     * @param revocationValuesVerification the revocationValuesVerification to set
     */
    public void setRevocationValuesVerification(String revocationValuesVerification) {
        this.revocationValuesVerification = revocationValuesVerification;
    }

}
