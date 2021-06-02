/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */

package org.apache.fineract.data.remote;

/**
 * @author Rajan Maurya
 */
public class BaseUrl {

    public static final String PROTOCOL_HTTPS = "http://";
    public static final String API_ENDPOINT = "buffalo.mifos.io:4200";
    public static final String PORT = "80";
    // "/" in the last of the base url always
    public static final String LOCALHOST_URL = "ws://10.0.2.2:4984/fineract-cn/";

    public String getName() {
        return "fineract";
    }

    public static String getDefaultBaseUrl() {
        return PROTOCOL_HTTPS + API_ENDPOINT;
    }
}