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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A very basic synchronized cache
 * @author Fabian Prasser
 *
 * @param <T>
 */
public class HTMLCache<T> {

    /** Backing map*/
    private Map<String, T> cache = Collections.synchronizedMap(new HashMap<String, T>());
    
    /**
     * Clears the cache
     */
    public void clear() {
        cache.clear();
    }
    
    /**
     * Returns whether content is stored for the given URL
     * @param url
     * @return
     */
    public boolean contains(String url) {
        return cache.containsKey(url);
    }
    
    /**
     * Returns the content associated with the given URL
     * @param url
     * @return
     */
    public T get(String url) {
        return cache.get(url);
    }
    
    /**
     * Lists all content
     * @return
     */
    public Collection<T> list() {
        return cache.values();
    }

    /**
     * Puts the given element into the cache
     * @param url
     * @param element
     */
    public void put(String url, T element) {
        cache.put(url, element);
    }
}
