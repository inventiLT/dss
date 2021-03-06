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

import eu.europa.ec.markt.tsl.jaxb.tsl.DigitalIdentityListType;
import eu.europa.ec.markt.tsl.jaxb.tsl.ExtensionType;
import eu.europa.ec.markt.tsl.jaxb.tsl.InternationalNamesType;
import eu.europa.ec.markt.tsl.jaxb.tsl.MultiLangNormStringType;
import eu.europa.ec.markt.tsl.jaxb.tsl.TSPServiceType;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Current entry of the Service for the TrustedList
 * 
 * 
 * @version $Revision: 1867 $ - $Date: 2013-04-08 13:44:56 +0200 (Mon, 08 Apr 2013) $
 */

class CurrentTrustService extends AbstractTrustService {

    private TSPServiceType service;

    /**
     * 
     * The default constructor for TrustService.
     * 
     * @param service
     */
    public CurrentTrustService(TSPServiceType service) {
        this.service = service;
    }

    @Override
    protected List<ExtensionType> getExtensions() {
        if (service.getServiceInformation() != null
                && service.getServiceInformation().getServiceInformationExtensions() != null) {
            return service.getServiceInformation().getServiceInformationExtensions().getExtension();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    protected DigitalIdentityListType getServiceDigitalIdentity() {
        return service.getServiceInformation().getServiceDigitalIdentity();
    }

    @Override
    public CurrentTrustService getCurrentServiceInfo() {
        return this;
    }

    @Override
    public String getStatus() {
        return service.getServiceInformation().getServiceStatus();
    }

    @Override
    public Date getStatusStartDate() {
        if (service.getServiceInformation() != null
                && service.getServiceInformation().getStatusStartingTime() != null) {
            return service.getServiceInformation().getStatusStartingTime().toGregorianCalendar().getTime();
        } else {
            return null;
        }
    }

    @Override
    public Date getStatusEndDate() {
        return null;
    }

    @Override
    public String getType() {
        return service.getServiceInformation().getServiceTypeIdentifier();
    }

    @Override
    public String getServiceName() {
        /* Return the english name or the first name */
        InternationalNamesType names = service.getServiceInformation().getServiceName();
        for (MultiLangNormStringType s : names.getName()) {
            if ("en".equalsIgnoreCase(s.getLang())) {
                return s.getValue();
            }
        }
        return names.getName().get(0).getValue();
    }

}
