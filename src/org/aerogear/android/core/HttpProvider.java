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

package org.aerogear.android.core;

import java.net.URL;

/**
 * A suite a convenience functions which wrap and clean up
 * common HTTP request operations.
 * 
 */
public interface HttpProvider {

    public URL getUrl();
    
    /**
     * Issues an HTTP request, consumes the content, and cleans up 
     * after itself.
     * @return
     * @throws HttpException if the http request doesn't return status 200
     */
    public HeaderAndBody get() throws HttpException, Exception;
    
    /**
     * Issues an HTTP request, consumes the content, and cleans up 
     * after itself.
     * 
     * @return
     * @throws HttpException if the http request doesn't return status 200
     */
    public HeaderAndBody post(String data) throws RuntimeException;
    
    /**
     * Issues an HTTP request, consumes the content, and cleans up 
     * after itself.
     * 
     * @return
     * @throws HttpException if the http request doesn't return status 200
     */
    public HeaderAndBody put(String id, String data) throws RuntimeException;
    
    /**
     * Issues an HTTP request, consumes the content, and cleans up 
     * after itself.
     * 
     * @return
     * @throws HttpException if the http request doesn't return status 200
     */
    public HeaderAndBody delete(String id) throws RuntimeException;

    /**
     * 
     * Will set a default header value to be used on all calls
     * 
     * @param header Name
     * @param header Value
     */
	public void setDefaultHeader(String headerName, String headerValue);

}
