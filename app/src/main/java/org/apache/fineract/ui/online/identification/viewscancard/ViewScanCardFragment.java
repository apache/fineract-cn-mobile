package org.apache.fineract.ui.online.identification.viewscancard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import org.apache.fineract.R;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.utils.CheckSelfPermissionAndRequest;
import org.apache.fineract.utils.ConstantKeys;
import org.apache.fineract.utils.ImageLoaderUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 03/08/17.
 */
public class ViewScanCardFragment extends FineractBaseFragment {

    @BindView(R.id.iv_scan_card)
    ImageView ivScanCard;

    View rootView;

    private String customerIdentifier;
    private String identifierNumber;
    private String scanIdentifier;
    public Integer requestType = 0;

    public static ViewScanCardFragment newInstance(String customerIdentifier,
            String identificationNumber, String scanIdentifier) {
        ViewScanCardFragment fragment = new ViewScanCardFragment();
        Bundle args = new Bundle();
        args.putString(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
        args.putString(ConstantKeys.IDENTIFICATION_NUMBER, identificationNumber);
        args.putString(ConstantKeys.IDENTIFICATION_SCAN_CARD, scanIdentifier);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            customerIdentifier = getArguments().getString(ConstantKeys.CUSTOMER_IDENTIFIER);
            identifierNumber = getArguments().getString(ConstantKeys.IDENTIFICATION_NUMBER);
            scanIdentifier = getArguments().getString(ConstantKeys.IDENTIFICATION_SCAN_CARD);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_scan_card, container, false);
        ButterKnife.bind(this, rootView);

        ivScanCard.setVisibility(View.VISIBLE);
        ImageLoaderUtils imageLoaderUtils = new ImageLoaderUtils(getActivity());
        imageLoaderUtils.loadImage(imageLoaderUtils.buildIdentificationScanCardImageUrl(
                customerIdentifier, identifierNumber, scanIdentifier), ivScanCard,
                R.drawable.ic_autorenew_black_24dp);

        return rootView;
    }

    public void downloadImage() {
        Bitmap bitmap = getBitmapFromView(ivScanCard);
        String directory = Environment.getExternalStorageDirectory() + File.separator + getString(
                R.string.fineract);
        File dir = new File(directory);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        File file = new File(directory + File.separator + scanIdentifier + ".png");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            Logger.getLogger(e.getMessage());
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                Logger.getLogger(e.getMessage());
            }
        }
        Toast.makeText(getContext(), getString(R.string.image_saved, file),
                Toast.LENGTH_LONG).show();
    }

    public void shareImage() {
        Uri bitmapUri = getImageUri(getContext(), getBitmapFromView(ivScanCard));
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        startActivity(Intent.createChooser(shareIntent,
                getString(R.string.share_scanned_identifier)));
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, customerIdentifier, null);
        return Uri.parse(path);
    }

    public static Bitmap getBitmapFromView(ImageView view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    public boolean checkStoragePermission() {
        if (CheckSelfPermissionAndRequest.checkMultiplePermissions(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return true;
        } else {
            requestPermission();
            return false;
        }
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                ConstantKeys.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
    }
}
