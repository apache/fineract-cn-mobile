package com.mifos.apache.fineract.ui.online.roles.roleslist;

import com.mifos.apache.fineract.data.models.rolesandpermission.Role;
import com.mifos.apache.fineract.ui.base.MvpView;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 24/08/17.
 */
public interface RolesContract {

    interface View extends MvpView {

        void showUserInterface();

        void showRoles(List<Role> roles);

        void showEmptyRoles();

        void showRecyclerView(Boolean status);

        void showProgressbar();

        void hideProgressbar();

        void showError(String message);
    }

    interface Presenter {

        void fetchRoles();
    }
}
