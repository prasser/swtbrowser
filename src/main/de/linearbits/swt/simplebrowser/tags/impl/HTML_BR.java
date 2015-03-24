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

import de.linearbits.swt.simplebrowser.tags.HTMLContentElement;
import de.linearbits.swt.simplebrowser.tags.HTMLElement;

/**
 * Linebreak
 * @author Fabian Prasser
 */
public class HTML_BR extends HTMLContentElement {

    /**
     * Constructor
     */
    public HTML_BR() {
        super("br");
    }

    @Override
    public String getContent() {
        return System.lineSeparator();
    }

    @Override
    public HTMLElement newInstance() {
        return new HTML_BR();
    }
}
