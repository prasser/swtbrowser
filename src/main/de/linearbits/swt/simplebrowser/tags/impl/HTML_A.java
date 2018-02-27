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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.linearbits.swt.simplebrowser.HTMLBrowser;
import de.linearbits.swt.simplebrowser.HTMLStyle;
import de.linearbits.swt.simplebrowser.tags.HTMLElement;
import de.linearbits.swt.simplebrowser.tags.HTMLStyleElement;

/**
 * Anchor tag
 * @author Fabian Prasser
 */
public class HTML_A extends HTMLStyleElement {

    /** Browser*/
    private final HTMLBrowser   browser;
    /** Style*/
    private final HTMLStyle     style;
    /** Hrefs*/
    private final Stack<String> hrefs = new Stack<String>();
    /** Labels*/
    private final Stack<String> labels = new Stack<String>();
    
    /**
     * Constructor
     * @param browser
     * @param style
     */
    public HTML_A(HTMLBrowser browser, HTMLStyle style) {
        super("a");
        this.browser = browser;
        this.style = style;
    }

    /**
     * Clears the cache
     */
    public void clear() {
        hrefs.clear();
        labels.clear();
    }

    @Override
    public StyleRange endStyle(StringBuilder content) {
        String label;
        try {
            label = content.substring(start);
            label = label.trim();
            if (label.replace(" ", "").length() == 0) {
                label = null;
            }
        } catch (Exception e) {
            label = null;
        }
        labels.push(label);
        return null;
    }

    /**
     * Flushes the set of collected hyperlinks to the rendered document
     * @param parent
     */
    public void flush(Composite parent) {
        
        // Collect
        final List<String> _hrefs = new ArrayList<String>();
        final List<String> _labels = new ArrayList<String>();
        while (!hrefs.isEmpty() && !labels.isEmpty()) {
            
            String href = hrefs.pop();
            String label = labels.pop();
            
            if (href != null && label != null && href.length() != 0 && label.length() != 0 && !href.startsWith("#")) {
                _hrefs.add(href);
                _labels.add(label);
            }
        }
        hrefs.clear();
        labels.clear();
        
        // Check
        if (_hrefs.size() == 0) {
            return;
        }

        // Text
        if (!style.isClickableLinks()) {
            
            Composite container = new Composite(parent, SWT.NONE);
            GridLayout layout = getDefaultLayout();
            layout.numColumns = 1;
            container.setLayout(layout);
            container.setLayoutData(getDefaultLayoutData());
            applyStyle(container, style);
            
            for (String href : _hrefs) {
                StyledText text = new StyledText(container, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP);
                text.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ));
                text.setText("Link: " + href);
                text.setBackground(style.getBackground());
                text.setForeground(style.getForeground());
                text.setFont(style.getFont());
                applyStyle(text, style);
            }

        // Button
        } else if (_hrefs.size() == 1) {
            
            Composite container = new Composite(parent, SWT.NONE);
            GridLayout layout = getDefaultLayout();
            layout.makeColumnsEqualWidth = false;
            layout.numColumns = 2;
            container.setLayout(layout);
            container.setLayoutData(getDefaultLayoutData());
            applyStyle(container, style);
            
            Label lbl = new Label(container, SWT.NONE);
            lbl.setText("Link:");
            applyStyle(lbl, style);

            Button button = new Button(container, SWT.PUSH);
            button.setText(_labels.get(0));
            button.addSelectionListener(new SelectionAdapter(){
                @Override
                public void widgetSelected(SelectionEvent arg0) {
                    browser.setUrl(_hrefs.get(0));
                }
            });
        } else {
            
            Composite container = new Composite(parent, SWT.NONE);
            GridLayout layout = getDefaultLayout();
            layout.makeColumnsEqualWidth = false;
            layout.numColumns = 3;
            container.setLayout(layout);
            container.setLayoutData(getDefaultLayoutData());
            applyStyle(container, style);

            Label lbl = new Label(container, SWT.NONE);
            lbl.setText("Links:");
            applyStyle(lbl, style);

            final Combo combo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
            combo.setItems(_labels.toArray(new String[_labels.size()]));
            combo.select(0);
            
            Button button = new Button(container, SWT.PUSH);
            button.setText("Go");
            button.addSelectionListener(new SelectionAdapter(){
                @Override
                public void widgetSelected(SelectionEvent arg0) {
                    int index = combo.getSelectionIndex();
                    if (index >= 0 && index < _hrefs.size()) {
                        browser.setUrl(_hrefs.get(index));
                    }
                }
            });
        }
    }
    
    @Override
    public HTMLElement newInstance() {
        return this;
    }

    @Override
    public Composite render(URL base, HTMLElement previous, Composite parent, Map<String, String> attributes, HTMLStyle style) {
        final Composite result = super.render(base, previous, parent, attributes, style);
        String href = attributes.get("href");
        if (href == null || href.startsWith("#")) {
            hrefs.push(null);
        } else {
            try {
                hrefs.push(new URL(base, href).toString());
            } catch (MalformedURLException e) {
                hrefs.push(null);
            }
        }
        return result;
    }
}
