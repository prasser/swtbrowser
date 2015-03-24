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
package de.linearbits.swt.simplebrowser.tags;

import java.util.List;

import org.eclipse.swt.custom.StyleRange;

import de.linearbits.swt.simplebrowser.HTMLStyle;

/**
 * A HTML element that implements a container
 * @author Fabian Prasser
 */
public abstract class HTMLContainer extends HTMLElement {

    /**
     * Constructor
     * @param tag
     */
    protected HTMLContainer(String tag) {
        super(tag, false, false, false);
    }

    @Override
    public StyleRange endStyle(StringBuilder builder) {
        return null;
    }

    @Override
    public String getContent() {
        return null;
    }
    
    @Override
    public void render(HTMLElement parent, String content, HTMLStyle style, List<StyleRange> styles) {
        // Nothing to do here
    }

    @Override
    public void startStyle(StringBuilder builder) {
        // Nothing to do
    }
}
