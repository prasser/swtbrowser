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

import org.eclipse.swt.custom.StyleRange;

import de.linearbits.swt.simplebrowser.tags.HTMLElement;
import de.linearbits.swt.simplebrowser.tags.HTMLStyleElement;

/**
 * Underline style
 * @author Fabian Prasser
 */
public class HTML_U extends HTMLStyleElement {

    /**
     * Constructor
     */
    public HTML_U() {
        super("u");
    }

    @Override
    public StyleRange endStyle(StringBuilder content) {
        StyleRange style = new StyleRange();
        style.start = start;
        style.length = content.length() - start;
        style.underline = true;
        return style;
    }

    @Override
    public HTMLElement newInstance() {
        return new HTML_U();
    }
}
