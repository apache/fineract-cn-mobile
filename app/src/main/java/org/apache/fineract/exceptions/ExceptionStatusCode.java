package org.apache.fineract.exceptions;

import retrofit2.HttpException;

/**
 * @author Rajan Maurya
 *         On 20/08/17.
 */
public class ExceptionStatusCode {

    public static Boolean isHttp403Error(Throwable throwable) {
        return ((HttpException) throwable).code() == 403;
    }
}
