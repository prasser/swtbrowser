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

import java.net.URL;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;

import de.linearbits.swt.simplebrowser.HTMLStyle;
import de.linearbits.swt.simplebrowser.tags.HTMLElement;
import de.linearbits.swt.simplebrowser.tags.HTMLEmptyContainer;

/**
 * Ordered list element
 * @author Fabian Prasser
 */
public class HTML_OL extends HTMLEmptyContainer{

    /** Counter*/
    private int count = 0;

    /**
     * Constructor
     */
    public HTML_OL() {
        super("ol");
    }

    /**
     * Increments the counter
     * @return
     */
    public int increment() {
        return ++count;
    }
    
    @Override
    public HTMLElement newInstance() {
        return new HTML_OL();
    }

    @Override
    public Composite render(URL base, HTMLElement previous, Composite parent, Map<String, String> attributes, HTMLStyle style) {
        return super.render(base, previous, parent, attributes, style);
    }
}
