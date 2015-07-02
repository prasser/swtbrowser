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

import de.linearbits.swt.simplebrowser.HTMLStyle;
import de.linearbits.swt.simplebrowser.tags.HTMLElement;

/**
 * Heading 3
 * @author Fabian Prasser
 */
public class HTML_H3 extends HTML_H{

    /**
     * Constructor
     */
    public HTML_H3() {
        super("h3");
    }

    @Override
    public HTMLElement newInstance() {
        return new HTML_H3();
    }

    protected int getOffset(HTMLStyle style) {
        return style.getOffsetH3();
    }
}
