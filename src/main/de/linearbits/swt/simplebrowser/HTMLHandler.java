/* ******************************************************************************
 * Copyright (c) 2015 Fabian Prasser.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Fabian Prasser - initial API and implementation
 * ****************************************************************************
 */
package de.linearbits.swt.simplebrowser;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The default Handler for HTML documents.
 *
 * @author Fabian Prasser
 */
public abstract class HTMLHandler extends DefaultHandler {

    /** The builder */
    private StringBuilder sb = new StringBuilder();

    @Override
    public void characters(final char[] ch,
                           final int start,
                           final int length) throws SAXException {
        // Append
        sb.append(ch, start, length);
    }

    @Override
    public void endElement(final String uri,
                           final String localName,
                           final String qName) throws SAXException {
        handlePayload();
        end(localName);
    }

    @Override
    public void startElement(final String uri,
                         final String localName,
                         final String qName,
                         final Attributes attributes) throws SAXException {
        
        handlePayload();
        Map<String, String> map = new HashMap<String, String>();
        for (int i=0; i<attributes.getLength(); i++) {
            map.put(attributes.getQName(i), attributes.getValue(i));
        }
        start(localName, map);
    }
    
    private void handlePayload() {
        String payload = sb.toString().replace("\r\n", " ").replace("\n", " ").replace("\r", " ").replaceAll(" +", " ").trim();
        sb.setLength(0);
        if (payload.length() != 0) {
            payload(payload);
        }
    }

    /**
     * End element
     * @param tag
     * @return
     */
    protected abstract void end(String tag);

    /**
     * Payload
     * @param payload
     */
    protected abstract void payload(String payload);

    /**
     * Start element
     * @param tag
     * @param attributes
     */
    protected abstract void start(String tag, Map<String, String> attributes);
}
