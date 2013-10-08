/*
 * (C) Copyright 2011 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     mariana
 */

package org.nuxeo.pdfjs;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.platform.ui.web.tag.fn.DocumentModelFunctions;
import org.nuxeo.ecm.webapp.action.ConversionAction;

@Name("pdfJsPreviewActionBean")
@Scope(ScopeType.CONVERSATION)
public class PdfJsPreviewActionBeanBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public static String PDFJS_PREVIEW = "testPDFJS";

    @In(create = true, required = false)
    protected transient CoreSession documentManager;

    @In(create = true)
    protected NavigationContext navigationContext;

    @In(create = true)
    protected ConversionAction conversionActions;

    protected String currentDocPDFPreviewURL;

    public String getCurrentDocPDFPreviewURL() {
        return currentDocPDFPreviewURL;
    }

    public void setCurrentDocPDFPreviewURL(String currentDocPDFPreviewURL) {
        this.currentDocPDFPreviewURL = currentDocPDFPreviewURL;
    }

    public String getDocumentURL(DocumentModel documentModel,
            String blobPropertyName, String filename) throws ClientException {
        currentDocPDFPreviewURL = DocumentModelFunctions.fileUrl(
                "downloadFile", documentModel, blobPropertyName, filename);
        return PDFJS_PREVIEW;
    }

    public String getDocumentURL(DocumentModel documentModel,
            String listElement, int index, String blobPropertyName,
            String fileName) {
        currentDocPDFPreviewURL = DocumentModelFunctions.complexFileUrl(
                "downloadFile", documentModel, listElement, index,
                blobPropertyName, fileName);
        return PDFJS_PREVIEW;
    }

    public String getDocumentPdfURL(String docRef, String fileFieldFullName)
            throws ClientException {
        currentDocPDFPreviewURL = conversionActions.generatePdfFile(docRef,
                fileFieldFullName);
        return PDFJS_PREVIEW;
    }
}