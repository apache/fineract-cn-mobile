package org.apache.fineract.utils;

import static org.apache.fineract.data.remote.BaseApiManager.retrofit;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import org.apache.fineract.data.models.error.MifosError;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * @author Rajan Maurya
 *         On 18/06/17.
 */
//TODO Write standard Error utils
public class MifosErrorUtils {

    public static final String LOG_TAG = "MifosErrorUtils";
    public static final String NETWORK_ERROR_MESSAGE = "No Internet Connection!";

    private static Gson gson = new Gson();

    public static MifosError parseError(String serverResponse) {
        return gson.fromJson(serverResponse, MifosError.class);
    }

    public static String getErrorMessage(Context context, Throwable throwable) {
        MifosError mifosError = new MifosError();
        Converter<ResponseBody, MifosError> errorConverter =
                retrofit.responseBodyConverter(MifosError.class, new Annotation[0]);

        if (throwable instanceof IOException) {
            return NETWORK_ERROR_MESSAGE;
        }

        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            int code = httpException.code();
            Response response = httpException.response();

            if (response.errorBody() != null) {
                try {
                    mifosError = errorConverter.convert(response.errorBody());
                } catch (IOException e) {
                    Log.d(LOG_TAG, e.getLocalizedMessage());
                }
            }

            switch (code) {
                case 400:
                    return mifosError.getMessage();
                case 403:
                    //access token has expired
                    break;
                case 404:
                    //you don't have permission
                    return mifosError.getMessage();
                case 409:
                    //resource already exists
                    break;
            }
        }
        return mifosError.getMessage();
    }

    public static <T> T getErrorBodyAs(Class<T> type, Response response) throws IOException {
        if (response == null || response.errorBody() == null) {
            return null;
        }
        Converter<ResponseBody, T> converter = retrofit.responseBodyConverter(type,
                new Annotation[0]);
        return converter.convert(response.errorBody());
    }

    public static Integer getStatusCode(Throwable throwable) {
        return ((HttpException) throwable).code();
    }
}
