package org.apache.fineract.ui.views;


import android.content.Context;
import android.os.Build;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import org.apache.fineract.R;


public class ViewBehavior extends CoordinatorLayout.Behavior<HeaderView> {

    private Context context;

    private int startMarginLeft;
    private int endMargintLeft;
    private int marginRight;
    private int startMarginBottom;
    private boolean isHide;

    public ViewBehavior(Context context, AttributeSet attrs) {
        this.context = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, HeaderView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, HeaderView child,
            View dependency) {
        shouldInitProperties(child, dependency);

        int maxScroll = ((AppBarLayout) dependency).getTotalScrollRange();
        float percentage = Math.abs(dependency.getY()) / (float) maxScroll;

        float childPosition = dependency.getHeight()
                + dependency.getY()
                - child.getHeight()
                - (getToolbarHeight() - child.getHeight()) * percentage / 2;


        childPosition = childPosition - startMarginBottom * (1f - percentage);

        CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.leftMargin = (int) (percentage * endMargintLeft) + startMarginLeft;
        lp.rightMargin = marginRight;
        child.setLayoutParams(lp);

        child.setY(childPosition);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (isHide && percentage < 1) {
                child.setVisibility(View.VISIBLE);
                isHide = false;
            } else if (!isHide && percentage == 1) {
                child.setVisibility(View.GONE);
                isHide = true;
            }
        }
        return true;
    }

    private void shouldInitProperties(HeaderView child, View dependency) {

        if (startMarginLeft == 0) {
            startMarginLeft = context.getResources().getDimensionPixelOffset(
                    R.dimen.header_view_start_margin_left);
        }

        if (endMargintLeft == 0) {
            endMargintLeft = context.getResources().getDimensionPixelOffset(
                    R.dimen.header_view_end_margin_left);
        }

        if (startMarginBottom == 0) {
            startMarginBottom = context.getResources().getDimensionPixelOffset(
                    R.dimen.header_view_start_margin_bottom);
        }

        if (marginRight == 0) {
            marginRight = context.getResources().getDimensionPixelOffset(
                    R.dimen.header_view_end_margin_right);
        }

    }

    public int getToolbarHeight() {
        int result = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            result = TypedValue.complexToDimensionPixelSize(tv.data,
                    context.getResources().getDisplayMetrics());
        }
        return result;
    }

}
