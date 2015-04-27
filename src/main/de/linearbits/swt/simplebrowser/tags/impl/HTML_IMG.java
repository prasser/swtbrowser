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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.linearbits.swt.simplebrowser.HTMLBrowser;
import de.linearbits.swt.simplebrowser.HTMLCache;
import de.linearbits.swt.simplebrowser.HTMLStyle;
import de.linearbits.swt.simplebrowser.tags.HTMLContainer;
import de.linearbits.swt.simplebrowser.tags.HTMLElement;

/**
 * Image element
 * @author Fabian Prasser
 */
public class HTML_IMG extends HTMLContainer{

    /**
     * Background loader for images
     * @author Fabian Prasser
     */
    private class ImageLoader implements Runnable {

        /** Browser*/
        private final HTMLBrowser      browser;
        /** Cache*/
        private final HTMLCache<Image> cache;
        /** Label*/
        private final Label            label;
        /** Is it sized already*/
        private final boolean          sized;
        /** Style*/
        private final HTMLStyle        style;
        /** URL*/
        private final String           url;

        /**
         * Constructor
         * @param url
         * @param label
         * @param browser
         * @param style
         * @param cache
         * @param sized
         */
        public ImageLoader(String url, Label label, HTMLBrowser browser, HTMLStyle style, HTMLCache<Image> cache, boolean sized) {
            this.url = url;
            this.label = label;
            this.sized = sized;
            this.style = style;
            this.cache = cache;
            this.browser = browser;
        }

        @Override
        public void run() {
            
            // Try to obtain image from cache
            Image img = cache.get(url);
            if (img == null || img.isDisposed()) {
                
                // Load from web
                try { img = new Image(label.getDisplay(), new URL(url).openStream());} catch (IOException ex) {}
                
                // Put into cache
                if (img != null) {
                    cache.put(url, img);
                }
            }
            
            
            // If successful, update
            if (img != null) {
                final Image image = img;
                label.getDisplay().asyncExec(new Runnable(){
                    @Override
                    public void run() {
                        
                        if (label == null || label.isDisposed()) {
                            image.dispose();
                            return;
                        }

                        // Update
                        label.setRedraw(false);
                        label.setBackground(style.getBackground());
                        label.setForeground(style.getForeground());
                        
                        // Size
                        if (!sized) {
                            setSize(label, image.getBounds().width, image.getBounds().height);
                            label.setImage(image);
                            browser.layout();
                        } else {
                            label.setImage(getImage(label, image));
                        }
                        
                        // Update
                        label.setRedraw(true);
                    }
                });
            }
        }
    }
    
    /**
     * Sets the size for the given label
     * @param label
     * @param width
     * @param height
     */
    private static void setSize(Label label, int width, int height) {
        GridData data = new GridData();
        data.minimumWidth = width;
        data.minimumHeight = height;
        data.widthHint = width;
        data.heightHint = height;
        label.setLayoutData(data);
        label.setSize(width, height);
    }
    
    /** Browser*/
    private final HTMLBrowser      browser;
    /** Cache*/
    private final HTMLCache<Image> cache;

    /**
     * Constructor
     * @param browser
     * @param cache
     */
    public HTML_IMG(HTMLBrowser browser, HTMLCache<Image> cache) {
        super("img");
        this.cache = cache;
        this.browser = browser;
    }

    @Override
    public HTMLElement newInstance() {
        return new HTML_IMG(browser, cache);
    }

    @Override
    public Composite render(URL base, HTMLElement previous, Composite parent, Map<String, String> attributes, HTMLStyle style) {
        
        Composite result = super.render(base, previous, parent, attributes, style);
        
        int width = -1;
        int height = -1;
        
        if (attributes.containsKey("width")) {
            try { width = Integer.valueOf(attributes.get("width")); } catch (Exception e){};
        }
        if (attributes.containsKey("height")) {
            try { height = Integer.valueOf(attributes.get("height")); } catch (Exception e){};
        }
        
        boolean sized;
        Label label = new Label(result, SWT.NONE);
        label.setBackground(label.getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
        
        if (width < 0 || height < 0) {
            setSize(label, 32, 32);
            sized = false;
        } else {
            setSize(label, width, height);
            sized = true;
        }
        
        if (attributes.containsKey("src")) {
            String url = null;
            try {
                url = new URL(base, attributes.get("src")).toString();
            } catch (MalformedURLException e) {
                url = null;
            }
            if (url != null) {
                
                // Set from cache
                Image img = cache.get(url);
                if (img != null && !img.isDisposed()) {
                    if (!sized) {
                        setSize(label, img.getBounds().width, img.getBounds().height);
                        label.setImage(img);
                    } else {
                        label.setImage(getImage(label, img));
                    }
                    
                // Load from cache
                } else {
                    Thread thread = new Thread(new ImageLoader(url , label, browser, style, cache, sized));
                    thread.setDaemon(true);
                    thread.start();
                }
            }
        }
        
        return result;
    }

    /**
     * Returns a resized image and registers a dispose listener if required
     * @param label
     * @param image
     * @return
     */
    private Image getImage(Label label, Image image) {
        GridData data = (GridData)label.getLayoutData();
        int width = data.minimumWidth;
        int height = data.minimumHeight;
        if (width != image.getBounds().width || height != image.getBounds().height) {
            final Image resized = getResizedInstance(image, width, height);
            label.addDisposeListener(new DisposeListener(){
                @Override
                public void widgetDisposed(DisposeEvent arg0) {
                    if (resized != null && !resized.isDisposed()) {
                        resized.dispose();
                    }
                }
            });
            return resized;
        } else {
            return image;
        }
    }

    /**
     * Resizes the given image. Does not dispose the input image.
     * @param image
     * @param width
     * @param height
     * @return
     */
    private Image getResizedInstance(Image image, int width, int height) {
        Image scaled = new Image(browser.getDisplay(), width, height);
        GC gc = new GC(scaled);
        gc.setAntialias(SWT.ON);
        gc.setInterpolation(SWT.HIGH);
        gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0, 0, width, height);
        gc.dispose();
        return scaled;
    }

    @Override
    protected GridData getDefaultLayoutData() {
        GridData data = super.getDefaultLayoutData();
        data.verticalIndent = 5;
        return data;
    }
}
