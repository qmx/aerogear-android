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

package org.aerogear.android;

import org.aerogear.android.datamanager.IdGenerator;
import org.aerogear.android.datamanager.Store;
import org.aerogear.android.impl.datamanager.DefaultIdGenerator;
import org.aerogear.android.impl.datamanager.StoreType;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an abstraction layer for a storage system.
 */

public final class DataManager {

    private final Map<String, Store> stores = new HashMap<String, Store>();
    private final IdGenerator idGenerator;

    public DataManager() {
        this.idGenerator = new DefaultIdGenerator();
    }

    public DataManager(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * Creates a new default (in memory) Store implemention.
     *
     * @param storeName The name of the actual data store object.
     */
    public Store add(String storeName) {
        return add(storeName, StoreType.MEMORY);
    }

    /**
     * Creates a new Store implemention. The actual type is determined by the type argument.
     *
     * @param storeName The name of the actual data store object.
     * @param type The type of the new data store object.
     */
    public Store add(String storeName, StoreType type) {
        Store store = AdapterFactory.createStore(type, idGenerator);
        stores.put(storeName, store);
        return store;
    }

    /**
     * Removes a Store implemention from the DataManager. The store to be removed
     * is determined by the storeName argument.
     *
     * @param storeName The name of the actual data store object.
     */
    public Store remove(String storeName) {
        return stores.remove(storeName);
    }

    /**
     * Loads a given Store implemention, based on the given storeName argument.
     *
     * @param storeName The name of the actual data store object.
     */
    public Store get(String storeName) {
        return stores.get(storeName);
    }

}