package org.apache.fineract.exceptions;

import java.io.IOException;

/**
 * @author Rajan Maurya
 *         On 23/09/17.
 */
public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No connectivity exception";
    }
}
