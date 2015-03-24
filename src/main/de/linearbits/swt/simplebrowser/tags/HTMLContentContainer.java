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
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import de.linearbits.swt.simplebrowser.HTMLStyle;

/**
 * A HTML element that implements a container for text
 * @author Fabian Prasser
 */
public abstract class HTMLContentContainer extends HTMLElement {

    /** View*/
    private Composite container;
    
    /**
     * Constructor
     * @param tag
     */
    protected HTMLContentContainer(String tag) {
        super(tag, true, false, false);
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
        render(parent, container, content, style, styles);
    }
    
    @Override
    public Composite render(URL base, HTMLElement previous, Composite parent, Map<String, String> attributes, HTMLStyle style) {
        this.container = super.render(base, previous, parent, attributes, style);
        return container;
    }
    @Override
    public void startStyle(StringBuilder builder) {
        // Nothing to do
    }
    
    protected abstract void render(HTMLElement parent, Composite container, String content, HTMLStyle style, List<StyleRange> styles);
    
    protected void setFont(Control control, FontData data) {
        final Font font = new Font(control.getDisplay(), data);
        control.setFont(font);
        control.addDisposeListener(new DisposeListener(){
            @Override
            public void widgetDisposed(DisposeEvent arg0) {
                if (font != null && !font.isDisposed()) {
                    font.dispose();
                }
            }
        });
    }
}
