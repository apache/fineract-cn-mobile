package com.mifos.apache.fineract.exceptions;

import retrofit2.HttpException;

/**
 * @author Rajan Maurya
 *         On 20/08/17.
 */
public class ExceptionStatusCode {

    public static Boolean isHttp401Error(Throwable throwable) {
        return ((HttpException) throwable).code() == 403;
    }
}
