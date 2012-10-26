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
package org.aerogear.android.authentication;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.aerogear.android.Builder;
import org.aerogear.android.Callback;
import org.aerogear.android.authentication.impl.RestAuthenticationModule;
import org.aerogear.android.core.HttpException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author summers
 */
@RunWith(RobolectricTestRunner.class)
public class RestAuthenticationModuleTest implements AuthenticationModuleTest {
    
     static final Builder<RestAuthenticationModule> BUILDER;
    
        static {
            try {
                BUILDER  = new RestAuthenticationModule.Builder().baseURL(new URL("http://localhost:8080/todo-server"));
            } catch (MalformedURLException ex) {
                Logger.getLogger(RestAuthenticationModuleTest.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }
    
    @Before
    public void setup() {
        Robolectric.setDefaultHttpResponse(401, "Unauthorized");
    }
    
    @After
    public void cleaRules() {
        Robolectric.clearHttpResponseRules();  
        Robolectric.clearPendingHttpResponses();
    }
    
    @Test(timeout=5000L)
    public void loginFails() throws IOException {
        RestAuthenticationModule module = BUILDER.build();
        SimpleCallback callback = new SimpleCallback();
        module.login(PASSING_USERNAME, LOGIN_PASSWORD, callback);
        
        while(!Robolectric.httpRequestWasMade()) {
        
        };
        Assert.assertNotNull(callback.exception);
        Assert.assertFalse(module.isAuthenticated());
    }
    
    
    @Test(timeout=50000L)
    public void loginSucceeds() throws IOException {
        RestAuthenticationModule module = BUILDER.build();
        Robolectric.addHttpResponseRule(LOGIN_MATCHER, VALID_LOGIN);
        SimpleCallback callback = new SimpleCallback();
        module.login(PASSING_USERNAME, LOGIN_PASSWORD, callback);
        
        while(!Robolectric.httpRequestWasMade()) {
        
        };
        Assert.assertNull(callback.exception);
        Assert.assertNotNull(callback.data);
        Assert.assertTrue(module.isAuthenticated());
        Assert.assertEquals(TOKEN, module.getAuthToken());
    }
    
    @Test(timeout=50000L)
    public void enrollSucceeds() throws IOException {
        RestAuthenticationModule module = BUILDER.build();
        Robolectric.addHttpResponseRule(ENROLL_PASS_MATCHER, ENROLL_PASS);
        SimpleCallback callback = new SimpleCallback();
        
        Map<String, String> userData = new HashMap<String, String>();
        userData.put("username", PASSING_USERNAME);
        userData.put("password", ENROLL_PASSWORD);
        userData.put("firstname", "Summers");
        userData.put("lastname", "Pittman");
        userData.put("role", "admin");
        
        module.enroll(userData, callback);
        
        while(!Robolectric.httpRequestWasMade()) {
        
        };
        Assert.assertNull(callback.exception);
        Assert.assertNotNull(callback.data);
        
        String result = new String(callback.data.getBody(), "UTF-8");
        JsonParser parser = new JsonParser();
        JsonObject resultObject = (JsonObject) parser.parse(result);
        Assert.assertEquals(PASSING_USERNAME, resultObject.get("username").getAsString());
        
        Assert.assertTrue(module.isAuthenticated());
        Assert.assertEquals(TOKEN, module.getAuthToken());
    }
    
    
    @Test(timeout=50000L)
    public void enrollFails() throws IOException {
        RestAuthenticationModule module = BUILDER.build();
        Robolectric.addHttpResponseRule(ENROLL_FAIL_MATCHER, ENROLL_FAIL);
        SimpleCallback callback = new SimpleCallback();
        
        Map<String, String> userData = new HashMap<String, String>();
        userData.put("username", FAILING_USERNAME);
        userData.put("password", ENROLL_PASSWORD);
        userData.put("firstname", "Summers");
        userData.put("lastname", "Pittman");
        userData.put("role", "admin");
        
        module.enroll(userData, callback);
        
        while(!Robolectric.httpRequestWasMade()) {
        
        };
        Assert.assertNotNull(callback.exception);
        Assert.assertNull(callback.data);
        Assert.assertFalse(module.isAuthenticated());
        Assert.assertEquals(400, ((HttpException)callback.exception).getStatusCode());
    }
    
        
    @Test(timeout=50000L)
    public void logouSucceeds() throws IOException {
        RestAuthenticationModule module = BUILDER.build();
        Robolectric.addHttpResponseRule(LOGIN_MATCHER, VALID_LOGIN);
        SimpleCallback callback = new SimpleCallback();
        module.login(PASSING_USERNAME, LOGIN_PASSWORD, callback);
        
        Assert.assertNull(callback.exception);
        Assert.assertNotNull(callback.data);
        Assert.assertTrue(module.isAuthenticated());
        Assert.assertEquals(TOKEN, module.getAuthToken());
        
        //Reset
        Robolectric.clearHttpResponseRules();
        Robolectric.setDefaultHttpResponse(200, "");
        VoidCallback voidCallback = new VoidCallback();
        
        
        module.logout(voidCallback);
        Assert.assertNull(voidCallback.exception);
        
        Assert.assertFalse(module.isAuthenticated());
        Assert.assertEquals("", module.getAuthToken());
        
    }

    
}
