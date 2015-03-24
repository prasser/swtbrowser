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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.linearbits.swt.simplebrowser.HTMLStyle;
import de.linearbits.swt.simplebrowser.tags.HTMLContainer;
import de.linearbits.swt.simplebrowser.tags.HTMLElement;

/**
 * List element
 * @author Fabian Prasser
 */
public class HTML_LI extends HTMLContainer{

    /**
     * Constructor
     */
    public HTML_LI() {
        super("li");
    }

    @Override
    public HTMLElement newInstance() {
        return new HTML_LI();
    }

    @Override
    public Composite render(URL base, HTMLElement previous, Composite parent, Map<String, String> attributes, HTMLStyle style) {
        
        Composite frame = new Composite(parent, SWT.NONE);
        GridLayout layout = getDefaultLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;
        frame.setLayout(layout);
        frame.setLayoutData(getDefaultLayoutData());
        applyStyle(frame, style);
        
        Label label = new Label(frame, SWT.NONE);
        
        String text = " - ";
        if (previous instanceof HTML_OL) {
            text = " " + String.valueOf(((HTML_OL)previous).increment()) + ". ";
        }
        
        label.setText(text);
        label.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false));
        applyStyle(label, style);
        
        Composite container = new Composite(frame, SWT.NONE);
        container.setLayout(getDefaultLayout());
        container.setLayoutData(getDefaultLayoutData());
        applyStyle(container, style);
        
        return container;
    }
}
