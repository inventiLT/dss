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

package eu.europa.ec.markt.dss.applet.model;

import eu.europa.ec.markt.dss.applet.MOCCAAdapter;
import eu.europa.ec.markt.dss.applet.io.NativeHTTPDataLoader;
import eu.europa.ec.markt.dss.applet.io.RemoteAIACertificateSourceFactory;
import eu.europa.ec.markt.dss.applet.io.RemoteCRLSource;
import eu.europa.ec.markt.dss.applet.io.RemoteCertificateSource;
import eu.europa.ec.markt.dss.applet.io.RemoteOCSPSource;
import eu.europa.ec.markt.dss.applet.io.RemoteTSPSource;
import eu.europa.ec.markt.dss.common.JavaPreferencesDAO;
import eu.europa.ec.markt.dss.common.PinInputDialog;
import eu.europa.ec.markt.dss.common.SignatureTokenType;
import eu.europa.ec.markt.dss.common.UserPreferencesDAO;
import eu.europa.ec.markt.dss.signature.Document;
import eu.europa.ec.markt.dss.signature.DocumentSignatureService;
import eu.europa.ec.markt.dss.signature.FileDocument;
import eu.europa.ec.markt.dss.signature.SignaturePackaging;
import eu.europa.ec.markt.dss.signature.SignaturePolicy;
import eu.europa.ec.markt.dss.signature.asic.ASiCXMLSignatureService;
import eu.europa.ec.markt.dss.signature.cades.CAdESService;
import eu.europa.ec.markt.dss.signature.pades.PAdESServiceV2;
import eu.europa.ec.markt.dss.signature.token.AsyncSignatureTokenConnection;
import eu.europa.ec.markt.dss.signature.token.Pkcs12SignatureToken;
import eu.europa.ec.markt.dss.signature.token.RFC3370Pkcs11SignatureToken;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.token.MSCAPISignatureToken;
import eu.europa.ec.markt.dss.signature.token.Pkcs11SignatureToken;
import eu.europa.ec.markt.dss.signature.token.RFC3370Pkcs12SignatureToken;
import eu.europa.ec.markt.dss.signature.token.SignatureTokenConnection;
import eu.europa.ec.markt.dss.signature.xades.XAdESService;
import eu.europa.ec.markt.dss.validation.TrustedListCertificateVerifier;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.bouncycastle.cms.CMSSignedData;

/**
 * Contains all the information of the wizard for creating/validating a Signature.
 * 
 * 
 * @version $Revision: 1867 $ - $Date: 2013-04-08 13:44:56 +0200 (Mon, 08 Apr 2013) $
 */

public class SignatureWizardModel {

    /* Do we follow strictly the RFC3370 ? */
    private boolean STRICT_RFC3370_DEFAULT = true;

    private static final Logger LOG = Logger.getLogger(SignatureWizardModel.class.getName());

    private UserPreferencesDAO userPreferencesDAO = new JavaPreferencesDAO();

    private WizardUsage wizardUsage;

    private boolean usageParameterFound = false;

    private String serviceUrl;

    private FileDocument originalFile;

    private File signedFile;

    private File extendedFile;

    private String signatureFormat;

    private boolean preconfiguredTokenType = false;

    private SignatureTokenType tokenType;

    private String pkcs11LibraryPath;

    private String pkcs12FilePath;

    private char[] pkcs12Password;

    private DSSPrivateKeyEntry privateKey;

    private SignaturePackaging packaging;

    private String claimedRole;

    private boolean preconfiguredSignaturePolicy = false;

    private SignaturePolicy signaturePolicyType;

    private String signaturePolicyAlgo;

    private byte[] signaturePolicyValue;

    private String signaturePolicy;

    private AsyncSignatureTokenConnection pkcs11Provider;

    private boolean asics = false;

    private boolean strictRFC3370Compliance = STRICT_RFC3370_DEFAULT;

    private String moccaSignatureAlgorithm;

    /**
     * Build a SignatureTokenConnection according the model parameters.
     * 
     * @param component
     * @return
     */
    public SignatureTokenConnection createTokenConnection(final Component component) {

        if (tokenType != null) {

            if (tokenType != SignatureTokenType.PKCS11) {
                if (pkcs11Provider != null) {
                    pkcs11Provider.close();
                    pkcs11Provider = null;
                }
            }

            switch (tokenType) {
            case PKCS11:
                if (pkcs11Provider == null) {
                    LOG.info("Connection don't exists yet. Create a new one.");

                    if (isStrictRFC3370Compliance()) {
                        pkcs11Provider = new RFC3370Pkcs11SignatureToken(getPkcs11LibraryPath(), new PinInputDialog(
                                component));
                    } else {
                        pkcs11Provider = new Pkcs11SignatureToken(getPkcs11LibraryPath(), new PinInputDialog(
                                component));
                    }

                    LOG.info("Using PKCS#11: " + pkcs11Provider.getClass().getSimpleName());

                }
                return pkcs11Provider;
            case PKCS12:
                if (isStrictRFC3370Compliance()) {
                    return new RFC3370Pkcs12SignatureToken(getPkcs12Password(), new File(getPkcs12FilePath()));
                } else {
                    return new Pkcs12SignatureToken(getPkcs12Password(), new File(getPkcs12FilePath()));
                }

            case MSCAPI:
                return new MSCAPISignatureToken();

            case MOCCA:
                return new MOCCAAdapter().createSignatureToken(new PinInputDialog(component),
                        moccaSignatureAlgorithm);
            }
        }

        return null;
    }

    /**
     * Build a DocumentSignatureService according to the model parameters.
     * 
     * @return
     */
    public DocumentSignatureService createDocumentSignatureService() {

        RemoteTSPSource tspSource = new RemoteTSPSource();
        tspSource.setUrl(serviceUrl + "/tsp");
        tspSource.setDataLoader(new NativeHTTPDataLoader());

        RemoteOCSPSource ocspSource = new RemoteOCSPSource();
        ocspSource.setUrl(serviceUrl + "/ocsp");
        ocspSource.setDataLoader(new NativeHTTPDataLoader());

        RemoteCRLSource crlSource = new RemoteCRLSource();
        crlSource.setDataLoader(new NativeHTTPDataLoader());
        crlSource.setUrl(serviceUrl + "/crl");

        RemoteCertificateSource certificateSource = new RemoteCertificateSource();
        certificateSource.setDataLoader(new NativeHTTPDataLoader());
        certificateSource.setUrl(serviceUrl + "/certificate");

        RemoteAIACertificateSourceFactory certificateSourceFactory = new RemoteAIACertificateSourceFactory();

        TrustedListCertificateVerifier verifier = new TrustedListCertificateVerifier();
        verifier.setCrlSource(crlSource);
        verifier.setOcspSource(ocspSource);
        verifier.setTrustedListCertificatesSource(certificateSource);
        verifier.setAiaCertificateSourceFactory(certificateSourceFactory);

        if (signatureFormat.startsWith("XAdES-")) {
            XAdESService service = new XAdESService();
            service.setCertificateVerifier(verifier);
            service.setTspSource(tspSource);
            return service;
        } else if (signatureFormat.startsWith("CAdES-")) {
            CAdESService service = new CAdESService();
            service.setTspSource(tspSource);
            service.setCertificateVerifier(verifier);
            return service;
        } else if (signatureFormat.startsWith("PAdES-")) {
            PAdESServiceV2 padesService = new PAdESServiceV2();
            padesService.setTspSource(tspSource);
            padesService.setCertificateVerifier(verifier);
            return padesService;
        } else if (signatureFormat.startsWith("ASiC-")) {
            ASiCXMLSignatureService asicsService = new ASiCXMLSignatureService();
            asicsService.setTspSource(tspSource);
            asicsService.setCertificateVerifier(verifier);
            return asicsService;
        }

        return null;

    }

    /**
     * Guess the file type based on the first bytes of the OriginalFile content.
     * 
     * @return
     */
    public Filetype getOriginalFiletype() {
        return getFiletype(getOriginalFile());
    }

    /**
     * 
     * @return
     */
    public Filetype getSignedFiletype() {
        return getFiletype(new FileDocument(getSignedFile()));
    }

    private Filetype getFiletype(FileDocument file) {
        if (file.getName() != null && file.getName().toLowerCase().endsWith(".xml")) {
            return Filetype.XML;
        }
        FileInputStream input = null;
        try {
            input = file.openStream();
            byte[] preamble = new byte[5];
            int read = input.read(preamble);
            input.close();
            if (read < 5) {
                throw new RuntimeException();
            }
            String preambleString = new String(preamble);
            if (preambleString.equals("<?xml")) {
                return Filetype.XML;
            } else if (preambleString.equals("%PDF-")) {
                return Filetype.PDF;
            } else {
                try {
                    input = file.openStream();
                    new CMSSignedData(input);
                    return Filetype.CMS;
                } catch (Exception ex) {
                    return Filetype.BINARY;
                } finally {
                    input.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot determine the mime/type");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Get the WizardUsage from the model
     * 
     * @return
     */
    public WizardUsage getWizardUsage() {
        return wizardUsage;
    }

    /**
     * Set the WizardUsage for the model
     * 
     * @return
     */
    public void setWizardUsage(WizardUsage wizardUsage) {
        this.wizardUsage = wizardUsage;
    }

    /**
     * Get the url of the back-end service
     * 
     * @return
     */
    public String getServiceUrl() {
        return serviceUrl;
    }

    /**
     * Set the url of the back-end service
     * 
     * @param serviceUrl
     */
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    /**
     * @param tokenType the tokenType to set
     */
    public void setTokenType(SignatureTokenType tokenType) {
        if (tokenType != null) {
            userPreferencesDAO.setSignatureTokenType(tokenType);
        }
        this.tokenType = tokenType;
    }

    /**
     * @return the tokenType
     */
    public SignatureTokenType getTokenType() {
        if (tokenType == null) {
            tokenType = userPreferencesDAO.getSignatureTokenType();
        }
        return tokenType;
    }

    /**
     * Get the PKCS11 Library path
     * 
     * @return
     */
    public String getPkcs11LibraryPath() {
        if (pkcs11LibraryPath == null) {
            pkcs11LibraryPath = userPreferencesDAO.getPKCS11LibraryPath();
        }
        return pkcs11LibraryPath;
    }

    /**
     * Set the PKCS11 Library path
     * 
     * @param pkcs11LibraryPath
     */
    public void setPkcs11LibraryPath(String pkcs11LibraryPath) {
        if (pkcs11LibraryPath != null) {
            userPreferencesDAO.setPKCS11LibraryPath(pkcs11LibraryPath);
        }
        if (pkcs11Provider != null) {
            pkcs11Provider.close();
            pkcs11Provider = null;
        }
        this.pkcs11LibraryPath = pkcs11LibraryPath;
    }

    /**
     * Get the filepath for the PKCS12 File
     * 
     * @return
     */
    public String getPkcs12FilePath() {
        if (pkcs12FilePath == null) {
            pkcs12FilePath = userPreferencesDAO.getPKCS12FilePath();
        }
        return pkcs12FilePath;
    }

    /**
     * Set the filepath for the PKCS12 file
     * 
     * @param pkcs12FilePath
     */
    public void setPkcs12FilePath(String pkcs12FilePath) {
        if (pkcs12FilePath != null) {
            userPreferencesDAO.setPKCS12FilePath(pkcs12FilePath);
        }
        this.pkcs12FilePath = pkcs12FilePath;
    }

    /**
     * Get the PKCS12 password
     * 
     * @return
     */
    public char[] getPkcs12Password() {
        return pkcs12Password;
    }

    /**
     * Set the password for the PKCS12 file
     * 
     * @param pkcs12Password
     */
    public void setPkcs12Password(char[] pkcs12Password) {
        this.pkcs12Password = pkcs12Password;
    }

    /**
     * Get the file to sign
     * 
     * @return
     */
    public FileDocument getOriginalFile() {
        return originalFile;
    }

    /**
     * Set the file to sign
     * 
     * @param originalFile
     */
    public void setOriginalFile(FileDocument originalFile) {
        this.originalFile = originalFile;
    }

    /**
     * Get the signature format for the signature
     * 
     * @return
     */
    public String getSignatureFormat() {
        return signatureFormat;
    }

    /**
     * Set the signature format for the signature
     * 
     * @param signatureFormat
     */
    public void setSignatureFormat(String signatureFormat) {
        this.signatureFormat = signatureFormat;
    }

    /**
     * Get the private key used to sign
     * 
     * @return
     */
    public DSSPrivateKeyEntry getPrivateKey() {
        return privateKey;
    }

    /**
     * Set the private key that will be used to sign
     * 
     * @param privateKey
     */
    public void setPrivateKey(DSSPrivateKeyEntry privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * Get the destination of the signed data
     * 
     * @return
     */
    public File getSignedFile() {
        return signedFile;
    }

    /**
     * Set the destination of the signed data
     * 
     * @param signedFile
     */
    public void setSignedFile(File signedFile) {
        this.signedFile = signedFile;
    }

    /**
     * Get the PACKAGING method for the signature
     * 
     * @return
     */
    public SignaturePackaging getPackaging() {
        return packaging;
    }

    /**
     * Set the PACKAGING method for the signature
     * 
     * @param packaging
     */
    public void setPackaging(SignaturePackaging packaging) {
        this.packaging = packaging;
    }

    /**
     * Get the claimed role of the signer
     */
    public String getClaimedRole() {
        return claimedRole;
    }

    /**
     * Set the claimed role of the signer
     * 
     * @param claimedRole
     */
    public void setClaimedRole(String claimedRole) {
        this.claimedRole = claimedRole;
    }

    /**
     * Get the signature policy (EPES) for the signature
     */
    public String getSignaturePolicy() {
        return signaturePolicy;
    }

    /**
     * Set the signature policy (EPES) for the signature
     * 
     * @param signaturePolicy
     */
    public void setSignaturePolicy(String signaturePolicy) {
        this.signaturePolicy = signaturePolicy;
    }

    /**
     * 
     * @return
     */
    public File getExtendedFile() {
        return extendedFile;
    }

    /**
     * 
     * @param extendedFile
     */
    public void setExtendedFile(File extendedFile) {
        this.extendedFile = extendedFile;
    }

    /**
     * 
     * @return
     */
    public SignaturePolicy getSignaturePolicyType() {
        return signaturePolicyType;
    }

    /**
     * 
     * @param signaturePolicyType
     */
    public void setSignaturePolicyType(SignaturePolicy signaturePolicyType) {
        this.signaturePolicyType = signaturePolicyType;
    }

    /**
     * 
     * @return
     */
    public String getSignaturePolicyAlgo() {
        return signaturePolicyAlgo;
    }

    /**
     * 
     * @param signaturePolicyAlgo
     */
    public void setSignaturePolicyAlgo(String signaturePolicyAlgo) {
        this.signaturePolicyAlgo = signaturePolicyAlgo;
    }

    /**
     * 
     * @return
     */
    public byte[] getSignaturePolicyValue() {
        return signaturePolicyValue;
    }

    /**
     * 
     * @param signaturePolicyValue
     */
    public void setSignaturePolicyValue(byte[] signaturePolicyValue) {
        this.signaturePolicyValue = signaturePolicyValue;
    }

    /**
     * @return the preconfiguredTokenType
     */
    public boolean isPreconfiguredTokenType() {
        return preconfiguredTokenType;
    }

    /**
     * @param preconfiguredTokenType the preconfiguredTokenType to set
     */
    public void setPreconfiguredTokenType(boolean preconfiguredTokenType) {
        this.preconfiguredTokenType = preconfiguredTokenType;
    }

    /**
     * @return the preconfiguredSignaturePolicy
     */
    public boolean isPreconfiguredSignaturePolicy() {
        return preconfiguredSignaturePolicy;
    }

    /**
     * @param preconfiguredSignaturePolicy the preconfiguredSignaturePolicy to set
     */
    public void setPreconfiguredSignaturePolicy(boolean preconfiguredSignaturePolicy) {
        this.preconfiguredSignaturePolicy = preconfiguredSignaturePolicy;
    }

    /**
     * @return the strictRFC3370Compliance
     */
    public boolean isStrictRFC3370Compliance() {
        return strictRFC3370Compliance;
    }

    /**
     * @param strictRFC3370Compliance the strictRFC3370Compliance to set
     */
    public void setStrictRFC3370Compliance(boolean strictRFC3370Compliance) {
        this.strictRFC3370Compliance = strictRFC3370Compliance;
    }

    /**
     * @return the usageParameterFound
     */
    public boolean isUsageParameterFound() {
        return usageParameterFound;
    }

    /**
     * @param usageParameterFound the usageParameterFound to set
     */
    public void setUsageParameterFound(boolean usageParameterFound) {
        this.usageParameterFound = usageParameterFound;
    }

    public String getMoccaSignatureAlgorithm() {
        return moccaSignatureAlgorithm;
    }

    public void setMoccaSignatureAlgorithm(String moccaSignatureAlgorithm) {
        this.moccaSignatureAlgorithm = moccaSignatureAlgorithm;
    }

}
