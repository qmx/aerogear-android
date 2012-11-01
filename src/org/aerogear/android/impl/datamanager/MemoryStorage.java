/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.aerogear.android.impl.datamanager;

import org.aerogear.android.datamanager.IdGenerator;
import org.aerogear.android.datamanager.Store;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Memory implementation of Store {@link Store}.
 */
public class MemoryStorage<T> implements Store<T> {

    private final Map<Serializable, T> data = new HashMap<Serializable, T>();
    private final IdGenerator idGenerator;

    public MemoryStorage(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StoreType getType() {
        return StoreType.MEMORY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<T> readAll() {
        return data.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T read(Serializable id) {
        return data.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(T item) {
        Serializable newId = idGenerator.generate();
        // TODO Put newId on item
        data.put(newId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        data.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Serializable id) {
        data.remove(id);
    }

}