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
package org.tmatesoft.svn.cli2.svn;

import java.util.Iterator;
import java.util.Map;

import org.tmatesoft.svn.core.internal.util.SVNEncodingUtil;
import org.tmatesoft.svn.core.internal.util.SVNXMLUtil;
import org.tmatesoft.svn.core.SVNProperties;

/**
 * @author TMate Software Ltd.
 * @version 1.1.2
 */
public abstract class SVNXMLCommand extends SVNCommand {

    protected SVNXMLCommand(String name, String[] aliases) {
        super(name, aliases);
    }

    protected void printXMLHeader(String header) {
        StringBuffer xmlBuffer = new StringBuffer();
        SVNXMLUtil.addXMLHeader(xmlBuffer, false);
        SVNXMLUtil.openXMLTag(null, header, SVNXMLUtil.XML_STYLE_NORMAL, null, xmlBuffer);
        getSVNEnvironment().getOut().print(xmlBuffer.toString());
    }

    protected void printXMLFooter(String header) {
        StringBuffer xmlBuffer = new StringBuffer();
        SVNXMLUtil.closeXMLTag(null, header, xmlBuffer);
        getSVNEnvironment().getOut().print(xmlBuffer.toString());
    }


    protected StringBuffer openCDataTag(String tagName, String cdata, StringBuffer target) {
        return SVNXMLUtil.openCDataTag(null, tagName, cdata, target);
    }


    protected StringBuffer openXMLTag(String tagName, int style, String attr, String value, StringBuffer target) {
        return SVNXMLUtil.openXMLTag(null, tagName, style | SVNXMLUtil.XML_STYLE_ATTRIBUTE_BREAKS_LINE, attr, value, target);
    }

    protected StringBuffer openXMLTag(String tagName, int style, Map attributes, StringBuffer target) {
        return SVNXMLUtil.openXMLTag(null, tagName, style | SVNXMLUtil.XML_STYLE_ATTRIBUTE_BREAKS_LINE, attributes, target);
    }

    protected StringBuffer closeXMLTag(String tagName, StringBuffer target) {
        return SVNXMLUtil.closeXMLTag(null, tagName, target);
    }

    protected StringBuffer printXMLPropHash(StringBuffer buffer, SVNProperties propHash, boolean namesOnly) {
        if (propHash != null && !propHash.isEmpty()) {
            buffer = buffer == null ? new StringBuffer() : buffer;
            for (Iterator propNames = propHash.nameSet().iterator(); propNames.hasNext();) {
                String propName = (String) propNames.next();
                String propVal = propHash.getStringValue(propName);
                if (namesOnly) {
                    buffer = openXMLTag("property", SVNXMLUtil.XML_STYLE_SELF_CLOSING, "name", propName, buffer);
                } else {
                    buffer = openXMLTag("property", SVNXMLUtil.XML_STYLE_PROTECT_CDATA, "name", propName, buffer);
                    buffer.append(SVNEncodingUtil.xmlEncodeCDATA(propVal));
                    buffer = closeXMLTag("property", buffer);
                }
            }
        }
        return buffer;
    }
}
