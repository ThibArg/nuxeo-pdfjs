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
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.DocumentBlobHolder;
import org.nuxeo.ecm.platform.rendition.Rendition;
import org.nuxeo.ecm.platform.rendition.service.RenditionService;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.platform.ui.web.tag.fn.DocumentModelFunctions;
import org.nuxeo.runtime.api.Framework;

@Name("pdfJsPreviewActionBean")
@Scope(ScopeType.CONVERSATION)
public class PdfJsPreviewActionBeanBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Log log = LogFactory.getLog(PdfJsPreviewActionBeanBean.class);

    protected static final String PDF_MIMETYPE = "application/pdf";

    public static String PDFJS_PREVIEW = "testPDFJS";

    @In(create = true, required = false)
    protected transient CoreSession documentManager;

    @In(create = true)
    protected NavigationContext navigationContext;

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

    public String getDocumentPdfURL(DocumentModel doc, String viewId)
            throws ClientException {
        currentDocPDFPreviewURL = DocumentModelFunctions.documentUrl(
                "rendition", doc, viewId, new HashMap<String, String>(), false);
        return PDFJS_PREVIEW;
    }

    public Rendition getPdfRendition(DocumentModel doc) throws ClientException {
        RenditionService rs = Framework.getLocalService(RenditionService.class);
        List<Rendition> renditions = rs.getAvailableRenditions(doc);
        for (Rendition rendition : renditions) {
            if ("pdf".equals(rendition.getName())) {
                return rendition;
            }
        }
        return null;
    }

    public boolean isPdfFile(DocumentModel doc, String blobPropertyName)
            throws ClientException {
        if (doc == null || StringUtils.isEmpty(blobPropertyName)) {
            return false;
        }
        BlobHolder bh = new DocumentBlobHolder(doc, blobPropertyName);
        try {
            return PDF_MIMETYPE.equals(bh.getBlob().getMimeType());
        } catch (ClientException e) {
            log.error("Can not test mime type for blob " + doc.getTitle(), e);
            return false;
        }
    }

}