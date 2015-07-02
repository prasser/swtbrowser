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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.xml.sax.SAXException;

import de.linearbits.swt.simplebrowser.tags.HTMLElement;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_A;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_BR;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_Body;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_DEL;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_EM;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_H1;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_H2;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_H3;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_H4;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_H5;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_H6;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_I;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_IMG;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_LI;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_OL;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_P;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_SCRIPT;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_STRIKE;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_STRONG;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_STYLE;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_U;
import de.linearbits.swt.simplebrowser.tags.impl.HTML_UL;

/**
 * Simple renderer for HTML documents. HTML elements are rendered into native widgets.
 * 
 * @author Fabian Prasser
 */
public class HTMLRenderer extends HTMLHandler {

    /** Current content*/
    private final StringBuilder            content           = new StringBuilder();
    /** Elements*/
    private final Map<String, HTMLElement> elements          = new HashMap<String, HTMLElement>();
    /** Stack*/
    private final Stack<Composite>         stackedContainers = new Stack<Composite>();
    /** Stack*/
    private final Stack<HTMLElement>       stackedElements   = new Stack<HTMLElement>();
    /** Text style for content*/
    private final List<StyleRange>         styles            = new ArrayList<StyleRange>();
    /** Current anchor*/
    private HTML_A                         anchor;
    /** Base URL*/
    private URL                            base;
    /** Style*/
    private final HTMLStyle                style;
    /** Parent*/
    private final Composite                parent;

    /**
     * Creates a new instance
     * @param parent
     * @param browser
     * @param style
     * @param cache
     */
    public HTMLRenderer(Composite parent, HTMLBrowser browser, HTMLStyle style, final HTMLCache<Image> cache) {
        
        this.parent = parent;
        this.style = style;
        this.parent.setBackground(style.getBackground());
        this.parent.setForeground(style.getForeground());

        this.registerElement(new HTML_Body());
        this.registerElement(new HTML_LI());
        this.registerElement(new HTML_OL());
        this.registerElement(new HTML_BR());
        this.registerElement(new HTML_UL());
        this.registerElement(new HTML_P());
        this.registerElement(new HTML_H1());
        this.registerElement(new HTML_H2());
        this.registerElement(new HTML_H3());
        this.registerElement(new HTML_H4());
        this.registerElement(new HTML_H5());
        this.registerElement(new HTML_H6());
        this.registerElement(new HTML_STYLE());
        this.registerElement(new HTML_SCRIPT());
        this.registerElement(new HTML_A(browser, style));
        this.registerElement(new HTML_IMG(browser, cache));
        this.registerElement(new HTML_DEL());
        this.registerElement(new HTML_EM());
        this.registerElement(new HTML_I());
        this.registerElement(new HTML_STRIKE());
        this.registerElement(new HTML_STRONG());
        this.registerElement(new HTML_U());
    }

    /**
     * Renders the given content. Expects UTF-16 encoded HTML.
     * @param url
     * @param html
     * @throws HTMLException
     */
    public void render(String url, String html) throws HTMLException {
        render(url, html,  Charset.defaultCharset());
    }

    /**
     * Flushes the current content
     */
    private void flush() {

        if (content.length() == 0 || stackedContainers.size() == 0) {
            return;
        }
        
        String payload = content.toString();
        content.setLength(0);
        if (stackedElements.peek().isContentContainer()) {
            stackedElements.peek().render(getParentElement(), payload, style, styles);
        } else {
            Composite container = stackedContainers.peek();

            StyledText text = new StyledText(container, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP);
            text.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ));
            text.setText(payload);
            text.setBackground(style.getBackground());
            text.setForeground(style.getForeground());
            text.setFont(style.getFont());
            try {
                text.setStyleRanges(styles.toArray(new StyleRange[styles.size()]));
            } catch (Exception e) {
                // TODO
            }
        }
        styles.clear();
        
        anchor.flush(stackedContainers.peek());
    }

    /**
     * Returns the current parent element
     * @return
     */
    private HTMLElement getParentElement() {
        HTMLElement parent = null;
        if (stackedElements.size() > 1) {
            parent = stackedElements.get(stackedElements.size()-1);
        }
        return parent;
    }

    /**
     * Registers an HTML element
     * @param element
     */
    private void registerElement(HTMLElement element) {
        elements.put(element.getTag(), element);
        if (element instanceof HTML_A) {
            this.anchor = (HTML_A)element;
        }
    }

    /**
     * Renders the given content
     * @param html
     * @param charset
     * @throws HTMLException
     */
    private void render(String url, String html, Charset charset) throws HTMLException {
        try {
            this.base = new URL(url);
            InputStream input = new ByteArrayInputStream(html.getBytes(charset));
            parent.setRedraw(false);
            SAXParserImpl.newInstance(null).parse(input, this);
            parent.setRedraw(true);
        } catch (IOException e) {
            throw new HTMLException("Cannot read content", e);
        } catch (SAXException e) {
            throw new HTMLException("Cannot parse content", e);
        }
    }

    @Override
    protected void end(String tag) {
        
        if (stackedElements.size() != 0 && 
            stackedElements.peek().getTag().equals(tag)) {

            // Flush, if required
            if (!stackedElements.peek().isStyle() && !stackedElements.peek().isContentEmitter()) {
                flush();
            } 
            
            // Apply style
            if (stackedElements.peek().isStyle()) {
                StyleRange style = stackedElements.peek().endStyle(content);
                if (style != null) styles.add(style);
            }
            
            stackedElements.pop();
            stackedContainers.pop();
        }
    }

    @Override
    protected void payload(String payload) {
        
        if (content.length() != 0) {
            content.append(" ");
        }
        content.append(payload);
    }

    @Override
    protected void start(String tag, Map<String, String> attributes) {
        
        // Obtain implementation
        HTMLElement element = elements.get(tag);
        
        // Ignore unknown elements
        if (element == null) {
            return;
        }
        
        // Flush
        if (!element.isStyle() && !element.isContentEmitter()) {
            flush();
        }
        
        // Emit content
        if (element.isContentEmitter()) {
            content.append(element.getContent());
        }
        
        // Instantiate
        element = element.newInstance();
        if (element.isStyle()) {
            element.startStyle(content);
        }
        Composite base = stackedContainers.size()==0 ? parent : stackedContainers.peek();
        Composite container = element.render(this.base, getParentElement(), base, attributes, style);
        stackedElements.push(element);
        stackedContainers.push(container);
    }
}
