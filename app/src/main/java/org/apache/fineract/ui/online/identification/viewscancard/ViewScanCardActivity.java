package org.apache.fineract.ui.online.identification.viewscancard;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.reflect.TypeToken;

import org.apache.fineract.R;
import org.apache.fineract.data.models.customer.identification.ScanCard;
import org.apache.fineract.ui.adapters.ViewPagerAdapter;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.Toaster;
import org.apache.fineract.utils.ConstantKeys;
import org.apache.fineract.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 03/08/17.
 */
public class ViewScanCardActivity extends FineractBaseActivity implements
        ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_view_scan_card)
    ViewPager vpViewScanCard;

    private List<ScanCard> scanCards;
    private String identificationNumber;
    private String customerIdentifier;
    private ViewScanCardFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scan_card);
        ButterKnife.bind(this);

        String scanCardString = getIntent().getStringExtra(ConstantKeys.IDENTIFICATION_SCAN_CARD);
        identificationNumber = getIntent().getStringExtra(ConstantKeys.IDENTIFICATION_NUMBER);
        customerIdentifier = getIntent().getStringExtra(ConstantKeys.CUSTOMER_IDENTIFIER);
        int position = getIntent().getIntExtra(ConstantKeys.POSITION, 0);

        scanCards = Utils.getStringToPoJo(new TypeToken<List<ScanCard>>() {
        }, scanCardString);

        showBackButton();

        vpViewScanCard.addOnPageChangeListener(this);
        setupViewPager(vpViewScanCard);
        vpViewScanCard.setCurrentItem(position, true);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (ScanCard scanCard : scanCards) {
            adapter.addFragment(ViewScanCardFragment.newInstance(customerIdentifier,
                    identificationNumber, scanCard.getIdentifier()), scanCard.getIdentifier());
        }
        viewPager.setAdapter(adapter);
        fragment = (ViewScanCardFragment) adapter.getFragment(
                vpViewScanCard.getCurrentItem());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_identification_scan, menu);
        Utils.setToolbarIconColor(this, menu, R.color.white);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (fragment != null) {
            switch (item.getItemId()) {
                case R.id.menu_identification_scan_download:
                    fragment.requestType = 1;
                    if (fragment.checkStoragePermission()) {
                        fragment.downloadImage();
                    }
                    return true;
                case R.id.menu_identification_scan_share:
                    fragment.requestType = 2;
                    if (fragment.checkStoragePermission()) {
                        fragment.shareImage();
                    }
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode == ConstantKeys.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (fragment.requestType == 1) {
                    fragment.downloadImage();
                } else if (fragment.requestType == 2) {
                    fragment.shareImage();
                }
            } else {
                Toaster.show(vpViewScanCard, getString(R.string.permission_denied_write));
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        setToolbarTitle(scanCards.get(position).getIdentifier());
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
