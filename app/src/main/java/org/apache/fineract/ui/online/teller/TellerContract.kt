package org.apache.fineract.ui.online.teller

import org.apache.fineract.data.models.teller.Teller
import org.apache.fineract.ui.base.MvpView

interface TellerContract {

    interface View : MvpView {

        fun showUserInterface()

        fun showTellers(tellers: List<Teller>)

        fun showEmptyTellers()

        fun showRecyclerView(status: Boolean)

        fun showProgressbar()

        fun hideProgressbar()

        fun searchedTeller(teller: Teller)
    }

    interface Presenter {

        fun fetchTellers()
    }
}
