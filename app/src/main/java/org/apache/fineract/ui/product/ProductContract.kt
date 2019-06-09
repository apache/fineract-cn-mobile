package org.apache.fineract.ui.product

import org.apache.fineract.data.models.product.Product
import org.apache.fineract.ui.base.MvpView

interface ProductContract {

    interface View : MvpView {

        fun showUserInterface()

        fun showProduct(products: List<Product>)

        fun showEmptyProduct()

        fun showRecyclerView(status: Boolean)

        fun showProgressbar()

        fun hideProgressbar()

        fun searchedProduct(products: List<Product>)
    }

    interface Presenter {

        fun getProductsPage()

        fun searchProduct(products : List<Product>, identifier: String)
    }
}