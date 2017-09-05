package org.apache.fineract.ui.online.loanaccounts.loanapplication.loandetails;

import org.apache.fineract.data.models.product.Product;
import org.apache.fineract.ui.base.MvpView;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 20/07/17.
 */

public class LoanDetailsContract {

    interface View extends MvpView {

        void showUserInterface();

        void showProducts(List<String> products);

        void setComponentsValidations(Product product);

        void showEmptyProducts();

        void showProgressbar();

        void hideProgressbar();

        void showError(String message);

        boolean validateShortName();

        boolean validateTerm();

        boolean validatePrincipalAmount();

        boolean validateRepay();
    }

    interface Presenter {

        void fetchProducts();

        List<String> filterProducts(List<Product> products);

        void setProductPositionAndValidateViews(Integer position);

        List<String> getCurrentTermUnitType(List<String> unitTypes, String unitType);
    }
}
