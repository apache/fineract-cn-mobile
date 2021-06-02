package org.apache.fineract.couchbase

import android.content.Context
import com.couchbase.lite.CouchbaseLiteException
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration
import com.couchbase.lite.ListenerToken
import org.apache.fineract.data.local.PreferencesHelper
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.utils.Constants.DATABASE_NAME
import javax.inject.Inject

/**
 * Created by Ahmad Jawid Muhammadi on 7/8/20.
 */

class CouchbaseDatabase @Inject constructor(
        @ApplicationContext val context: Context?) {

    @Volatile
    private var database: Database? = null

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    private var listener: ListenerToken? = null

    fun getDatabase(): Database {
        // we need this check only when we use database for UI/Integration testing
        if (!::preferencesHelper.isInitialized) {
            preferencesHelper = PreferencesHelper(context)
            preferencesHelper.putUserName("ahmad")
        }
        return database
                ?: createDatabase(context, preferencesHelper.userName, DATABASE_NAME).also {
                    database = it
                    Replicate.startReplicating(it)
                }
    }

    fun deleteDatabase() {
        database?.close()
        database?.delete()
        database = null
    }

    private fun createDatabase(
            context: Context?,
            userName: String,
            databaseName: String
    ): Database {
        val configuration = DatabaseConfiguration()
//        configuration.directory = String.format(
//                "%s/%s",
//                context?.filesDir,
//                userName
       // )

        return Database(databaseName, configuration)
    }

    fun closeDatabaseForUser() {
        try {
            database?.let {
                database?.close()
                database = null
            }
        } catch (e: CouchbaseLiteException) {
            e.printStackTrace()
        }
    }
}