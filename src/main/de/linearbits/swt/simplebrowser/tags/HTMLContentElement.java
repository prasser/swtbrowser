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

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.widgets.Composite;

import de.linearbits.swt.simplebrowser.HTMLStyle;

/**
 * A HTML element that renders content
 * @author Fabian Prasser
 */
public abstract class HTMLContentElement extends HTMLElement{

    /**
     * Constructor
     * @param tag
     */
    public HTMLContentElement(String tag) {
        super(tag, false, false, true);
    }

    @Override
    public StyleRange endStyle(StringBuilder content) {
        return null;
    }

    @Override
    public void render(HTMLElement parent, String content, HTMLStyle style, List<StyleRange> styles) {
        // Nothing to do
    }

    @Override
    public Composite render(URL base, HTMLElement previous, Composite parent, Map<String, String> attributes, HTMLStyle style) {
        return parent;
    }

    @Override
    public void startStyle(StringBuilder content) {
        // Nothing to do
    }
}
