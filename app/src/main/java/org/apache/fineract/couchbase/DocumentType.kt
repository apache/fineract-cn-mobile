package org.apache.fineract.couchbase

/**
 * Created by Ahmad Jawid Muhammadi on 14/8/20.
 */

enum class DocumentType(val value: String) {
    GROUP("Group"),
    CUSTOMER("customer"),
    ACCOUNT("Account")
}