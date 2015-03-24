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
 * Strike style
 * @author Fabian Prasser
 */
public class HTML_STRIKE extends HTMLStyleElement {

    /**
     * Constructor
     */
    public HTML_STRIKE() {
        super("strike");
    }

    @Override
    public StyleRange endStyle(StringBuilder content) {
        StyleRange style = new StyleRange();
        style.start = start;
        style.length = content.length() - start;
        style.strikeout = true;
        return style;
    }

    @Override
    public HTMLElement newInstance() {
        return new HTML_STRIKE();
    }
}
