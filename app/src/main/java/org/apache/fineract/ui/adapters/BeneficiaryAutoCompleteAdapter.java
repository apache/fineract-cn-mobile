package org.apache.fineract.ui.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.apache.fineract.R;
import org.apache.fineract.data.datamanager.DataManagerCustomer;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.data.models.customer.CustomerPage;
import org.apache.fineract.injection.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class BeneficiaryAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 10;
    private static final int PAGE_INDEX = 0;

    private Context context;
    private List<Customer> resultList = new ArrayList<>();

    DataManagerCustomer dataManagerCustomer;

    @Inject
    public BeneficiaryAutoCompleteAdapter(@ApplicationContext Context context,
            DataManagerCustomer dataManagerCustomer) {
        this.context = context;
        this.dataManagerCustomer = dataManagerCustomer;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Customer getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override


    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_list_item_multiple_choice,
                    parent, false);
        }
        ((TextView) convertView.findViewById(android.R.id.text1))
                .setTextColor(ContextCompat.getColor(context, R.color.black));
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position)
                .getIdentifier());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<Customer> books = findBooks(constraint.toString()).getCustomers();

                    // Assign the data to the FilterResults
                    filterResults.values = books;
                    filterResults.count = books.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<Customer>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    /**
     * Returns a search result for the given customer title.
     */
    private CustomerPage findBooks(String term) {
        if (term.length() == 0) {
            return dataManagerCustomer.searchCustomer(PAGE_INDEX, MAX_RESULTS,
                    null).blockingFirst();
        } else {
            return dataManagerCustomer.searchCustomer(PAGE_INDEX, MAX_RESULTS,
                    term).blockingFirst();
        }
    }
}