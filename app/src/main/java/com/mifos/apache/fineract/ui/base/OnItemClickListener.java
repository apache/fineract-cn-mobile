package com.mifos.apache.fineract.ui.base;

import android.view.View;

/**
 * A click listener for items.
 */
public interface OnItemClickListener {

    /**
     * Called when an item is clicked.
     *
     * @param childView View of the item that was clicked.
     * @param position  Position of the item that was clicked.
     */
    void onItemClick(View childView, int position);

    /**
     * Called when an item is long pressed.
     *
     * @param childView View of the item that was long pressed.
     * @param position  Position of the item that was long pressed.
     */
    void onItemLongPress(View childView, int position);

}
