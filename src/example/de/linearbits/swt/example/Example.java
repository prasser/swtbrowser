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
package de.linearbits.swt.example;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.linearbits.swt.simplebrowser.HTMLBrowser;
import de.linearbits.swt.simplebrowser.HTMLException;

/**
 * A basic example
 * @author Fabian Prasser
 */
public class Example {
    
    /**
     * Main
     * @param args
     * @throws IOException
     * @throws HTMLException
     */
    public static void main(String[] args) throws IOException, HTMLException {
     
        // Create display and shell
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("SWT Browser");
        shell.setSize(800, 600);
        shell.setLayout(new FillLayout());
        
        // Create browser
        HTMLBrowser browser = new HTMLBrowser(shell, SWT.BORDER);
        browser.setUrl("http://en.wikipedia.org/wiki/Web_browser");
        
        // Open
        shell.open();
    
        // Event loop
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
    }
}
