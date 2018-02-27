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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * This class allows basic styling of how the HTML content is displayed
 * @author Fabian Prasser
 */
public class HTMLStyle {

    /** Background */
    private Color   background     = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
    /** Font */
    private Font    font           = Display.getDefault().getSystemFont();
    /** Foreground */
    private Color   foreground     = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
    /** Increase of size for h1 */
    private int     offsetH1       = 6;
    /** Increase of size for h2 */
    private int     offsetH2       = 5;
    /** Increase of size for h3 */
    private int     offsetH3       = 4;
    /** Increase of size for h4 */
    private int     offsetH4       = 3;
    /** Increase of size for h5 */
    private int     offsetH5       = 2;
    /** Increase of size for h6 */
    private int     offsetH6       = 1;
    /** Clickable links */
    private boolean clickableLinks = true;
    
    /**
     * Creates a new style
     * @param browser
     */
    HTMLStyle(HTMLBrowser browser) {
    	this.background = browser.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        this.font = browser.getDisplay().getSystemFont();
        this.foreground = browser.getDisplay().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
    }
    
    /**
     * Background
     * @return
     */
    public Color getBackground() {
        return background;
    }

    /**
     * Font
     * @return
     */
    public Font getFont() {
        return font;
    }

    /**
     * Foreground
     * @return
     */
    public Color getForeground() {
        return foreground;
    }

    /**
     * Increase in size of h1
     * @return
     */
    public int getOffsetH1() {
        return offsetH1;
    }

    /**
     * Increase in size of h2
     * @return
     */
    public int getOffsetH2() {
        return offsetH2;
    }

    /**
     * Increase in size of h3
     * @return
     */
    public int getOffsetH3() {
        return offsetH3;
    }

    /**
     * Increase in size of h4
     * @return
     */
    public int getOffsetH4() {
        return offsetH4;
    }

    /**
     * Increase in size of h5
     * @return
     */
    public int getOffsetH5() {
        return offsetH5;
    }

    /**
     * Increase in size of h6
     * @return
     */
    public int getOffsetH6() {
        return offsetH6;
    }

    /**
     * @return the clickableLinks
     */
    public boolean isClickableLinks() {
        return clickableLinks;
    }
    
    /**
     * Background
     * @param color
     */
    public void setBackground(Color color) {
        checkNull(color);
        this.background = color;
    }
    
    /**
     * @param clickableLinks the clickableLinks to set
     */
    public void setClickableLinks(boolean clickableLinks) {
        this.clickableLinks = clickableLinks;
    }
    
    /**
     * Font
     * @param font
     */
    public void setFont(Font font) {
        checkNull(font);
        this.font = font;
    }
    
    /**
     * Foreground
     * @param color
     */
    public void setForeground(Color color) {
        checkNull(color);
        this.foreground = color;
    }
    
    /**
     * Increase in size of h1
     * @param offsetH1
     */
    public void setOffsetH1(int offsetH1) {
        this.offsetH1 = offsetH1;
    }
    
    /**
     * Increase in size of h2
     * @param offsetH2
     */
    public void setOffsetH2(int offsetH2) {
        this.offsetH2 = offsetH2;
    }

    /**
     * Increase in size of h3
     * @param offsetH3
     */
    public void setOffsetH3(int offsetH3) {
        this.offsetH3 = offsetH3;
    }
    
    /**
     * Increase in size of h4
     * @param offsetH4
     */
    public void setOffsetH4(int offsetH4) {
        this.offsetH4 = offsetH4;
    }
    
    /**
     * Increase in size of h5
     * @param offsetH5
     */
    public void setOffsetH5(int offsetH5) {
        this.offsetH5 = offsetH5;
    }

    /**
     * Increase in size of h6
     * @param offsetH6
     */
    public void setOffsetH6(int offsetH6) {
        this.offsetH6 = offsetH6;
    }

    /**
     * Internal stuff
     * @param object
     */
    private void checkNull(Object object) {
        if (object == null) {
            throw new NullPointerException("Argument may not be null");
        }
    }
}
