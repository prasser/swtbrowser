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
package de.linearbits.swt.simplebrowser.tags.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;

import de.linearbits.swt.simplebrowser.tags.HTMLElement;
import de.linearbits.swt.simplebrowser.tags.HTMLStyleElement;

/**
 * Em style
 * @author Fabian Prasser
 */
public class HTML_EM extends HTMLStyleElement {

    /**
     * Constructor
     */
    public HTML_EM() {
        super("em");
    }

    @Override
    public StyleRange endStyle(StringBuilder content) {
        StyleRange style = new StyleRange();
        style.start = start;
        style.length = content.length() - start;
        style.fontStyle = SWT.ITALIC;
        return style;
    }

    @Override
    public HTMLElement newInstance() {
        return new HTML_EM();
    }
}
