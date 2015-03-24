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

import org.eclipse.swt.layout.GridData;

import de.linearbits.swt.simplebrowser.tags.HTMLContainer;
import de.linearbits.swt.simplebrowser.tags.HTMLElement;

/**
 * Paragraph element
 * @author Fabian Prasser
 */
public class HTML_P extends HTMLContainer{

    /**
     * Constructor
     */
    public HTML_P() {
        super("p");
    }

    @Override
    public HTMLElement newInstance() {
        return new HTML_P();
    }

    @Override
    protected GridData getDefaultLayoutData() {
        GridData data = super.getDefaultLayoutData();
        data.verticalIndent = 5;
        return data;
    }
}
