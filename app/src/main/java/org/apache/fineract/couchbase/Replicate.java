package org.apache.fineract.couchbase;

import android.util.Log;

import com.couchbase.lite.BasicAuthenticator;
import com.couchbase.lite.Database;
import com.couchbase.lite.Endpoint;
import com.couchbase.lite.Replicator;
import com.couchbase.lite.ReplicatorConfiguration;
import com.couchbase.lite.URLEndpoint;
import com.google.gson.Gson;

import org.apache.fineract.data.local.PreferencesHelper;

import java.net.URI;
import java.net.URISyntaxException;

import static com.couchbase.lite.ReplicatorConfiguration.ReplicatorType;
import static org.apache.fineract.data.remote.BaseUrl.LOCALHOST_URL;
import static org.apache.fineract.utils.Constants.BASIC_AUTH_KEY;

/**
 * Created by Ahmad Jawid Muhammadi on 12/8/20.
 */

public class Replicate {

    public static final String TAG = Replicate.class.getSimpleName();

    public static void startReplicating(Database database) throws URISyntaxException {

        Endpoint targetEndpoint = new URLEndpoint(URI.create(LOCALHOST_URL));
        ReplicatorConfiguration config = new ReplicatorConfiguration(database, targetEndpoint);
        config.setReplicatorType(ReplicatorType.PUSH_AND_PULL);

       // config.setAuthenticator(new BasicAuthenticator(GATEWAY_USER_NAME, GATEWAY_PASSWORD));

        Replicator replicator = new Replicator(config);

        replicator.addChangeListener(change -> {
            if (change.getStatus().getError() != null) {
                Log.e(TAG, "Error status:  " + change.getStatus());
                Log.e(TAG, "Error message:  " + change.getStatus().getProgress());
            }
        });

        replicator.start();
    }

    /**
     * Saves the Basic Authentication in SharedPreference
     */
    public void saveBasicAuthentication(
            String username, String password,
            PreferencesHelper preferencesHelper) {
        preferencesHelper.putString(
                BASIC_AUTH_KEY,
                new Gson().toJson(new BasicAuthenticator(username, password))
        );
    }

    public BasicAuthenticator getBasicAuthentication(
            PreferencesHelper preferencesHelper) {
        return new Gson().fromJson(
                preferencesHelper.getString(BASIC_AUTH_KEY, null),
                BasicAuthenticator.class
        );
    }
}
