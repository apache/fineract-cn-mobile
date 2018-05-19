package org.apache.fineract.data.models.deposit

import com.google.gson.annotations.SerializedName


data class Term(
    @SerializedName("period") var period: Int? = null
) {

    private var timeUnit: TimeUnit? = null
    private var interestPayable: InterestPayable? = null

    fun getTimeUnit(): String? {
        return if (this.timeUnit != null) {
            this.timeUnit!!.name
        } else {
            null
        }
    }

    fun setTimeUnit(timeUnit: String?) {
        if (timeUnit != null) {
            this.timeUnit = TimeUnit.valueOf(timeUnit)
        }
    }

    fun getInterestPayable(): String {
        return this.interestPayable!!.name
    }

    fun setInterestPayable(interestPayable: String) {
        this.interestPayable = InterestPayable.valueOf(interestPayable)
    }
}