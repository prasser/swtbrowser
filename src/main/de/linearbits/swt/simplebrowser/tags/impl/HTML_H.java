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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import de.linearbits.swt.simplebrowser.HTMLStyle;
import de.linearbits.swt.simplebrowser.tags.HTMLContentContainer;
import de.linearbits.swt.simplebrowser.tags.HTMLElement;

/**
 * Generic heading
 * @author Fabian Prasser
 */
public abstract class HTML_H extends HTMLContentContainer{

    /**
     * Constructor
     * 
     * @param tag
     */
    public HTML_H(String tag) {
        super(tag);
    }

    @Override
    protected GridData getDefaultLayoutData() {
        GridData data = super.getDefaultLayoutData();
        data.verticalIndent = 5;
        return data;
    }

    /**
     * Returns the offset
     * @param style
     * @return
     */
    protected abstract int getOffset(HTMLStyle style);
    
    @Override
    protected void render(HTMLElement parent, Composite container, String content, HTMLStyle style, List<StyleRange> styles) {
        StyledText text = new StyledText(container, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP);
        text.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ));
        text.setText(content);
        text.setBackground(style.getBackground());
        text.setForeground(style.getForeground());
        FontData data = style.getFont().getFontData()[0];
        data.setStyle(SWT.BOLD);
        data.setHeight(data.getHeight() + getOffset(style));
        setFont(text, data);
    }
}
