package org.apache.fineract.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import org.apache.fineract.data.local.PreferencesHelper;
import org.apache.fineract.data.remote.BaseUrl;
import org.apache.fineract.data.remote.EndPoints;
import org.apache.fineract.data.remote.MifosInterceptor;


/**
 * @author Rajan Maurya
 *         On 03/08/17.
 */
public class ImageLoaderUtils {

    private Context context;

    PreferencesHelper preferencesHelper;

    public ImageLoaderUtils(Context context) {
        this.context = context;
        preferencesHelper = new PreferencesHelper(context);
    }

    public String buildIdentificationScanCardImageUrl(String customerIdentifier,
            String identificationNumber, String scanIdentifier) {
        return BaseUrl.getDefaultBaseUrl() +
                EndPoints.API_CUSTOMER_PATH + "/customers/"
                + customerIdentifier + "/identifications/"
                + identificationNumber + "/scans/" + scanIdentifier + "/image";
    }

    public String buildCustomerPortraitImageUrl(String customerIdentifier) {
        return BaseUrl.getDefaultBaseUrl() +
                EndPoints.API_CUSTOMER_PATH + "/customers/"
                + customerIdentifier  + "/portrait";
    }

    public GlideUrl buildGlideUrl(String imageUrl) {
        return new GlideUrl(imageUrl, new LazyHeaders.Builder()
                .addHeader(MifosInterceptor.HEADER_TENANT, preferencesHelper.getTenantIdentifier())
                .addHeader(MifosInterceptor.HEADER_AUTH, preferencesHelper.getAccessToken())
                .addHeader(MifosInterceptor.HEADER_USER, preferencesHelper.getUserName())
                .build());
    }

    public void loadImage(String imageUrl, final ImageView imageView, Integer placeHolder) {
        Glide.with(context)
                .load(buildGlideUrl(imageUrl))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(placeHolder)
                .error(placeHolder)
                .centerCrop()
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap result) {
                        // check a valid bitmap is downloaded
                        if (result == null || result.getWidth() == 0)
                            return;
                        // set to image view
                        imageView.setImageBitmap(result);
                        imageView.setVisibility(View.VISIBLE);
                    }
                });
    }
}
