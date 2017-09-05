package org.apache.fineract.ui.online.identification.viewscancard;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.google.gson.reflect.TypeToken;

import org.apache.fineract.R;
import org.apache.fineract.data.models.customer.identification.ScanCard;
import org.apache.fineract.ui.adapters.ViewPagerAdapter;
import org.apache.fineract.ui.base.MifosBaseActivity;
import org.apache.fineract.utils.ConstantKeys;
import org.apache.fineract.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 03/08/17.
 */
public class ViewScanCardActivity extends MifosBaseActivity implements
        ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_view_scan_card)
    ViewPager vpViewScanCard;

    private List<ScanCard> scanCards;
    private String identificationNumber;
    private String customerIdentifier;

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
