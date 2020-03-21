package org.apache.fineract.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.item_dashbord_layout.view.*
import org.apache.fineract.R

class DashboardItem @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet?
) : CardView(context, attributeSet) {

    init {
        LayoutInflater.from(context).inflate(
                R.layout.item_dashbord_layout,
                this, true)
        attributeSet?.let {
            context.theme.obtainStyledAttributes(
                    it,
                    R.styleable.DashboardItem,
                    0, 0).let { typeArray ->
                iv_icon.setImageDrawable(typeArray.getDrawable(
                        R.styleable.DashboardItem_itemIcon))
                tv_title.text = typeArray.getString(R.styleable.DashboardItem_title)

                typeArray.recycle()
            }
        }
    }
}