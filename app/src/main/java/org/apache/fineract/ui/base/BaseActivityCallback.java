package org.apache.fineract.ui.base;

public interface BaseActivityCallback {

    void showMifosProgressDialog(String message);

    void setToolbarTitle(String title);

    void hideMifosProgressDialog();

    void logout();
}
