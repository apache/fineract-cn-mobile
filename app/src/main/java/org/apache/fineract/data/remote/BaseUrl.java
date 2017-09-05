/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */

package org.apache.fineract.data.remote;

/**
 * @author Rajan Maurya
 */
public class BaseUrl {

    public static final String PROTOCOL_HTTPS = "https://";
    public static final String API_ENDPOINT = "pilot.kuelap.io";
    public static final String PORT = "80";
    // "/" in the last of the base url always

    public String getName() {
        return "mifos";
    }

    public static String getDefaultBaseUrl() {
        return PROTOCOL_HTTPS + API_ENDPOINT;
    }
}