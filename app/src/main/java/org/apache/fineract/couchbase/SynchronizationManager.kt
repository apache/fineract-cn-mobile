package org.apache.fineract.couchbase

import android.content.Context
import android.util.Log.e
import com.couchbase.lite.*
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.utils.Constants.DATABASE_NAME
import javax.inject.Inject


/**
 * Created by Ahmad Jawid Muhammadi on 6/8/20.
 */

class SynchronizationManager @Inject constructor(
        @ApplicationContext val context: Context
) {

    private val TAG = SynchronizationManager::class.java.simpleName

    @Inject
    lateinit var database: CouchbaseDatabase

    fun closeDatabase() {
        database.closeDatabaseForUser()
    }


    @Throws(CouchbaseLiteException::class)
    fun <T> saveDocument(identifier: String, properties: Map<String, T>) {
        val document = MutableDocument(identifier, properties)
        try {
            e(TAG, "Document: $document")
            database.getDatabase().save(document)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteDocument(identifier: String) {
        try {
            database.getDatabase().purge(identifier)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun <T> updateDocument(identifier: String, properties: Map<String, T>) {
        val document = MutableDocument(identifier, properties)
        try {
            database.getDatabase().save(document)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getDocuments(expression: Expression, limit: Int = 50, offset: Int = 0): List<HashMap<String, Any>>? {
        val list = arrayListOf<HashMap<String, Any>>()
        val query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(database.getDatabase()))
                .where(expression)
                .orderBy(Ordering.expression(Meta.id))
                .limit(Expression.intValue(limit),
                        Expression.intValue(offset)
                )

        val resultSet = query.execute()
        var result: Result? = resultSet.next()
        while (result != null) {
            val valueMap = result.getDictionary(DATABASE_NAME)
            val item = valueMap?.toMap()
            list.add(item as HashMap<String, Any>)
            result = resultSet.next()
        }

        return list
    }


    fun getDocumentForTest(identifier: String, context: Context): HashMap<String, Any> {
        database = CouchbaseDatabase(context)
        return getDocumentById(identifier)
    }

    fun getDocumentById(identifier: String): HashMap<String, Any> {
        val query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(database.getDatabase()))
                .where(Meta.id.equalTo(Expression.string(identifier))
                )
                .limit(Expression.intValue(1))

        val resultSet = query.execute().next()
        return resultSet.getDictionary(DATABASE_NAME)?.toMap() as HashMap<String, Any>
    }


    fun clearDatabase() {
        val query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(database.getDatabase()))

        val resultSet = query.execute()
        var result: Result? = resultSet.next()
        while (result != null) {
            val valueMap = result.getDictionary(DATABASE_NAME)
            TODO("implement code here to delete item")
            result = resultSet.next()
        }
    }
}