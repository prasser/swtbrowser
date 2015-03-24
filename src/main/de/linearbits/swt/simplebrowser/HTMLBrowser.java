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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;

/**
 * This class implements a browser widget for SWT
 * @author Fabian Prasser
 */
public class HTMLBrowser {
    
    /**
     * Background worker, for fetching an HTML page
     * @author Fabian Prasser
     */
    private class HTMLLoader implements Runnable {

        /** Charset*/
        private Charset       charset;
        /** Connection*/
        private URLConnection conn;
        /** Flag*/
        private boolean       stop = false;
        /** URL*/
        private final String  url;
        /** Browser*/
        private final HTMLBrowser browser;

        /**
         * Constructor
         * @param browser
         * @param url
         * @param charset
         */
        private HTMLLoader(HTMLBrowser browser, String url, Charset charset) {
            this.url = url;
            this.charset = charset;
            this.browser = browser;
        }

        @Override
        public void run() {

            try {
                BufferedReader in = null;
                try {
                    String html = cachePage.get(url);
                    if (html == null) {
                         
                        final StringBuilder builder;
                        conn = new URL(url).openConnection();
                        conn.setConnectTimeout(timeoutConnect);
                        conn.setReadTimeout(timeoutRead);
                        in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
                        builder = new StringBuilder();
                        String line;
                        while ((line = in.readLine()) != null) {
                            builder.append(line).append("\n");
                        }
                        in.close();
                        html = builder.toString();
                        cachePage.put(url, html);
                    }
                    
                    synchronized(this) {
                        if (!stop) {
                            final String _html = html;
                            browser.getDisplay().asyncExec(new Runnable() {
                                public void run() {
                                    try {
                                        setContent(_html, url);
                                    } catch (HTMLException e) {
                                        // Nothing to do
                                    }
                                }
                            });
                        }
                    }
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            } catch (Exception e) {
                // Do nothing
            }
        }
        
        /**
         * Stops this worker
         */
        public void stop() {
            synchronized(this) {
                stop = true;
            }
        }
    }

    /** Caching and history*/
    private final List<String>      urls           = new ArrayList<String>();
    /** Caching and history*/
    private final List<String>      history        = new ArrayList<String>();
    /** Caching and history*/
    private final HTMLCache<Image>  cacheImage     = new HTMLCache<Image>();
    /** Caching and history*/
    private final HTMLCache<String> cachePage      = new HTMLCache<String>();
    
    /** View*/
    private final Composite         root;
    /** View*/
    private final ScrolledComposite scroller;
    /** View*/
    private final HTMLStyle         htmlstyle;
    
    /** Interaction and settings*/
    private List<LocationListener>  listeners      = new ArrayList<LocationListener>();
    /** Interaction and settings*/
    private HTMLLoader              loader         = null;
    /** Interaction and settings*/
    private String                  location       = null;
    /** Interaction and settings*/
    private int                     offset         = -1;
    /** Interaction and settings*/
    private int                     timeoutRead    = 3000;
    /** Interaction and settings*/
    private int                     timeoutConnect = 3000;

    /**
     * Creates a new instance
     * @param parent
     * @param style
     */
    public HTMLBrowser(Composite parent, int style) {
        
        scroller = new ScrolledComposite(parent, SWT.V_SCROLL | style);
        scroller.setLayout(new FillLayout());
        scroller.setExpandHorizontal(true);
        scroller.setExpandVertical(true);
        
        if (isMac()) {
	        scroller.getVerticalBar().addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					scroller.redraw();
				}
	        });
        }

        root = new Composite(scroller, SWT.NONE);
        root.setLayout(getRootLayout(scroller));
        htmlstyle = new HTMLStyle(this);
        root.setBackground(htmlstyle.getBackground());
        root.setForeground(htmlstyle.getForeground());
        
        // Dispose images in cache
        root.addDisposeListener(new DisposeListener(){
            @Override
            public void widgetDisposed(DisposeEvent arg0) {
                for (Image image : cacheImage.list()) {
                    if (image != null && !image.isDisposed()) {
                        image.dispose();
                    }
                }
                cacheImage.clear();
            } 
        });
        
        scroller.setContent(root);
        scroller.addControlListener(new ControlAdapter(){
            public void controlResized(ControlEvent arg0) {
                updateScrollbars();
            }
        });
        
        updateScrollbars();
    }

    /**
     * Returns the root layout
     * @param scroller
     * @return
     */
    private Layout getRootLayout(ScrolledComposite scroller) {
    	int offset = scroller.getVerticalBar().getSize().x;
    	offset = offset == 0 ? 30 : offset;
        GridLayout layout = new GridLayout(1, false);
        layout.marginWidth = 2;
        layout.marginHeight = 2;
        layout.marginTop = 2;
        layout.marginBottom = 2;
        layout.marginLeft = 2;
        layout.marginRight = 2 + offset;
        return layout;
    }

    /**
     * Adds a location listener
     * @param listener
     */
    public void addLocationListener(LocationListener listener) {
        listeners.add(listener);
    }

    /**
     * Are we running on an OSX system
     * @return
     */
    private static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0;
    }
    
    /**
     * Go backwards in the history
     * @throws HTMLException 
     */
    public void back() throws HTMLException {
        if (!isBackEnabled()) {
            return;
        }
        this.offset--;
        this.location = urls.get(offset);
        this.render(this.urls.get(offset), this.history.get(offset));
        this.fireLocationEvent();
    }

    /**
     * Go backwards in the history
     * @throws HTMLException 
     */
    public void forward() throws HTMLException {
        if (!isForwardEnabled()) {
            return;
        }
        this.offset++;
        this.location = urls.get(offset);
        this.render(this.urls.get(offset), this.history.get(offset));
        this.fireLocationEvent();
    }

    /**
     * @return the openTimeout
     */
    public int getConnectTimeout() {
        return timeoutConnect;
    }

    /**
     * Returns the current location, null, if there is no location or the content has been set directly
     * @return
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * @return the readTimeout
     */
    public int getReadTimeout() {
        return timeoutRead;
    }
    
    /**
     * Returns the widget's display
     * @return
     */
    public Display getDisplay() {
    	return root.getDisplay();
    }
    
    /**
     * Can we go backwards in the history
     * @return
     */
    public boolean isBackEnabled() {
        return this.history.size() > 0 && this.offset > 0;
    }

    /**
     * Can we go forwards in the history
     * @return
     */
    public boolean isForwardEnabled() {
        return this.history.size() > 0 && this.offset < history.size() - 1;
    }
    
    /**
     * Layouts this widget
     */
    public void layout() {
        root.layout();
        updateScrollbars();
    }

    /**
     * Removes a location listener
     * @param listener
     */
    public void removeLocationListener(LocationListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * @param openTimeout the openTimeout to set
     */
    public void setConnectTimeout(int openTimeout) {
        this.timeoutConnect = openTimeout;
    }

    /**
     * Sets the HTML content of the browser
     * @param html
     * @throws HTMLException 
     */
    public void setContent(String html) throws HTMLException {
        setContent(html, "http://localhost");
    }
    /**
     * Sets the layout data
     * @param arg0
     * @see org.eclipse.swt.widgets.Control#setLayoutData(java.lang.Object)
     */
    public void setLayoutData(Object arg0) {
        scroller.setLayoutData(arg0);
    }

    /**
     * @param readTimeout the readTimeout to set
     */
    public void setReadTimeout(int readTimeout) {
        this.timeoutRead = readTimeout;
    }
    
    /**
     * Sets the URL of the browser. Expected charset at the location is UTF-8
     * @param url
     */
    public void setUrl(String url) {
        setUrl(url, StandardCharsets.UTF_8);
    }
    /**
     * Sets the URL of the browser
     * @param url
     * @param charset
     */
    public void setUrl(String url, Charset charset) {
        if (loader != null) {
            loader.stop();
        }
        clear();
        loader = new HTMLLoader(this, url, charset);
        Thread t = new Thread(loader);
        t.setDaemon(true);
        t.start();
    }
    
    /**
     * Clear
     */
    private void clear() {
        root.setRedraw(false);
        for (Control c : root.getChildren()) {
            c.dispose();
        }
        root.setRedraw(true);
    }
    
    /**
     * Fire event
     */
    private void fireLocationEvent(){
        LocationEvent event = new LocationEvent(root);
        event.location = this.location;
        for (LocationListener listener : listeners) {
            listener.changed(event);
        }
    }

    /**
     * Renders the content
     * @param url
     * @param html
     * @throws HTMLException 
     */
    private void render(String url, String html) throws HTMLException {
        clear();
        resetScroller();
        this.root.setBackground(htmlstyle.getBackground());
        this.root.setForeground(htmlstyle.getForeground());
        HTMLRenderer renderer = new HTMLRenderer(root, this, htmlstyle, cacheImage);
        renderer.render(url, html);
        root.layout(true);
        updateScrollbars();
        resetScroller();
        scroller.redraw();
    }
    
    @SuppressWarnings("unused")
    private int countElements(Control control) {
        int count = 1;
        if (control instanceof Composite) {
            for (Control child : ((Composite)control).getChildren()) {
                count += countElements(child);
            }
        }
        return count;
    }

    /**
     * Resets the scroller
     */
    private void resetScroller() {
        scroller.setFocus();
        scroller.setOrigin(0, 0);
    }
    
    /**
     * Returns the associated style
     * @return
     */
    public HTMLStyle getStyle() {
        return this.htmlstyle;
    }

    /**
     * Sets the HTML content of the browser
     * @param html
     * @throws HTMLException 
     */
    private void setContent(String html, String url) throws HTMLException {
        render(url, html);
        this.location = url;
        this.history.add(html);
        this.urls.add(url);
        this.offset = this.history.size() - 1;
        this.fireLocationEvent();
    }

    /**
     * Updates the scroller
     */
    private void updateScrollbars() {
        scroller.setMinSize(root.computeSize(scroller.getSize().x, SWT.DEFAULT));
    }
}
