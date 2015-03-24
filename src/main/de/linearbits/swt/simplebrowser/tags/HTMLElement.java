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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import de.linearbits.swt.simplebrowser.HTMLStyle;

/**
 * A generic HTML element
 * @author Fabian Prasser
 */
public abstract class HTMLElement {

    /** Is this a style element*/
    private final boolean style;
    /** Is this a container element*/
    private final boolean container;
    /** Is this element a content emitter*/
    private final boolean content;
    /** The associated tag*/
    private final String  tag;

    /**
     * Constructor
     * @param tag
     * @param container
     * @param style
     * @param content
     */
    protected HTMLElement(String tag, boolean container, boolean style, boolean content) {
        this.tag = tag;
        this.container = container;
        this.style = style;
        this.content = content;
    }
    
    /**
     * End of style for the given content
     * @param content
     * @return
     */
    public abstract StyleRange endStyle(StringBuilder content);
    
    /**
     * Returns the content
     * @return
     */
    public abstract String getContent();
    
    /**
     * Returns the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * Is this a container
     * @return
     */
    public boolean isContentContainer() {
        return container;
    }

    /**
     * Is this a content emitter
     * @return
     */
    public boolean isContentEmitter() {
        return content;
    }
    
    /**
     * Is this element a style
     * @return
     */
    public boolean isStyle() {
        return style;
    }
    
    /**
     * Creates a new instance
     * @return
     */
    public abstract HTMLElement newInstance();
    
    /**
     * Renders the element
     * @param parent
     * @param content
     * @param style
     * @param styles
     */
    public abstract void render(HTMLElement parent, String content, HTMLStyle style, List<StyleRange> styles);
    
    /**
     * Renders the element
     * @param base
     * @param previous
     * @param parent
     * @param attributes
     * @param style
     * @return
     */
    public Composite render(URL base, HTMLElement previous, Composite parent, Map<String, String> attributes, HTMLStyle style) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(getDefaultLayout());
        container.setLayoutData(getDefaultLayoutData());
        applyStyle(container, style);
        return container;
    }

    /**
     * Start of style for the given content
     * @param content
     */
    public abstract void startStyle(StringBuilder content);

    /**
     * Apply the defined style to the given control
     * @param control
     * @param style
     */
    protected void applyStyle(Control control, HTMLStyle style) {
        control.setBackground(style.getBackground());
        control.setForeground(style.getForeground());
    }
    
    /**
     * Default layout
     * @return
     */
    protected GridLayout getDefaultLayout() {
        GridLayout layout = new GridLayout(1, true);
        layout.marginBottom = 0;
        layout.marginTop = 0;
        layout.marginLeft = 0;
        layout.marginRight = 0;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        return layout;
    }
    
    /**
     * Default layout data
     * @return
     */
    protected GridData getDefaultLayoutData() {
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalIndent = 0;
        data.verticalIndent = 0;
        return data;
    }
}
