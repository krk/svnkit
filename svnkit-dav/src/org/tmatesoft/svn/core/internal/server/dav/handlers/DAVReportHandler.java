/*
 * ====================================================================
 * Copyright (c) 2004-2007 TMate Software Ltd.  All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.  The terms
 * are also available at http://svnkit.com/license.html.
 * If newer versions of this license are posted there, you may use a
 * newer version instead, at your option.
 * ====================================================================
 */
package org.tmatesoft.svn.core.internal.server.dav.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.internal.server.dav.DAVRepositoryManager;
import org.tmatesoft.svn.core.internal.server.dav.DAVResource;

/**
 * @author TMate Software Ltd.
 * @version 1.1.2
 */
public class DAVReportHandler extends ServletDAVHandler {

    private DAVReportRequest myDAVRequest;

    protected DAVReportHandler(DAVRepositoryManager connector, HttpServletRequest request, HttpServletResponse response) {
        super(connector, request, response);
    }

    protected DAVRequest getDAVRequest() {
        if (myDAVRequest == null) {
            myDAVRequest = new DAVReportRequest();
        }
        return myDAVRequest;
    }

    private DAVReportRequest getReportRequest(){
        return (DAVReportRequest) getDAVRequest();
    }

    public void execute() throws SVNException {
        readInput();

        DAVResource resource = createDAVResource(false, false);

        ReportHandler reportHandler = getReportHandler(resource);

        setDefaultResponseHeaders();
        setResponseContentType(DEFAULT_XML_CONTENT_TYPE);
        setResponseStatus(HttpServletResponse.SC_OK);

        if (reportHandler.getContentLength() > 0) {
            setResponseContentLength(reportHandler.getContentLength());
        }

        reportHandler.sendResponse();
        //TODO: In some cases native svn starts blame command and clean all out headers        
    }

    private ReportHandler getReportHandler(DAVResource resource) throws SVNException {
        if (getReportRequest().isDatedRevisionsRequest()) {
            return new DAVDatedRevisionHandler(resource, (DAVDatedRevisionRequest) getReportRequest().getReportRequest(), getResponseWriter());
        } else if (getReportRequest().isFileRevisionsRequest()) {
            return new DAVFileRevisionsHandler(resource, (DAVFileRevisionsRequest) getReportRequest().getReportRequest(), getResponseWriter(), getSVNDiffVersion());
        } else if (getReportRequest().isGetLocationsRequest()) {
            return new DAVGetLocationsHandler(resource, (DAVGetLocationsRequest) getReportRequest().getReportRequest(), getResponseWriter());
        } else if (getReportRequest().isLogRequest()) {
            return new DAVLogHandler(resource, (DAVLogRequest) getReportRequest().getReportRequest(), getResponseWriter());
        } else if (getReportRequest().isMergeInfoRequest()) {
            return new DAVMergeInfoHandler(resource, (DAVMergeInfoRequest) getReportRequest().getReportRequest(), getResponseWriter());
        } else if (getReportRequest().isGetLocksRequest()) {
            return new DAVGetLocksHandler(resource, (DAVGetLocksRequest) getReportRequest().getReportRequest(), getResponseWriter());
        } else if (getReportRequest().isReplayRequest()) {
            return new DAVReplayHandler(resource, (DAVReplayRequest) getReportRequest().getReportRequest(), getResponseWriter());
        } else if (getReportRequest().isUpdateRequest()) {
            return new DAVUpdateHandler(resource, (DAVUpdateRequest) getReportRequest().getReportRequest(), getResponseWriter());
        }
        return null;
    }
}
