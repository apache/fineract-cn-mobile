package org.apache.fineract.utils

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import org.apache.fineract.R
import org.apache.fineract.ui.online.customers.createcustomer.CustomerAction
import org.apache.fineract.ui.online.customers.createcustomer.customeractivity.CreateCustomerActivity

/**
 * Implementation of App Widget functionality.
 */
class CustomerWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val intent = Intent(context, CreateCustomerActivity::class.java)
    intent.putExtra(ConstantKeys.CUSTOMER_ACTION, CustomerAction.CREATE)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
    val views = RemoteViews(context.packageName, R.layout.customer_widget)

    views.setImageViewResource(R.id.appwidget_img, R.drawable.ic_widget_48dp)
    views.setOnClickPendingIntent(R.id.appwidget_img, pendingIntent)

    appWidgetManager.updateAppWidget(appWidgetId, views)
}
