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
package de.linearbits.swt.simplebrowser;

/**
 * Exception thrown by the browser
 * @author Fabian Prasser
 *
 */
public class HTMLException extends Exception {

    /** SVUID*/
    private static final long serialVersionUID = 6448225913111670729L;

    /**
     * Constructor
     * @param message
     */
    protected HTMLException(String message) {
        super(message);
    }

    /**
     * Constructor
     * @param message
     * @param cause
     */
    protected HTMLException(String message, Throwable cause) {
        super(message, cause);
    }
}
