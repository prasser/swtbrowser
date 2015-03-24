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

import de.linearbits.swt.simplebrowser.tags.HTMLElement;
import de.linearbits.swt.simplebrowser.tags.HTMLEmptyContentContainer;

/**
 * Style element
 * @author Fabian Prasser
 */
public class HTML_STYLE extends HTMLEmptyContentContainer{

    /**
     * Constructor
     */
    public HTML_STYLE() {
        super("style");
    }

    @Override
    public HTMLElement newInstance() {
        return new HTML_STYLE();
    }
}
