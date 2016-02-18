package kidouchi.chronobook.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by iuy407 on 2/17/16.
 */
public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    ArrayList<String> results;
    private GoogleApiClient mGoogleApiClient;

    public PlacesAutoCompleteAdapter(Context context, int resource, GoogleApiClient googleApiClient) {
        super(context, resource);

        this.mGoogleApiClient = googleApiClient;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public String getItem(int position) {
        return results.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults filterResults = new FilterResults();
                if (constraint != null) {

                    String query = constraint.toString();

                    // TODO: Optimize?
                    // These bounds cover U.S.A
                    LatLngBounds bounds = new LatLngBounds(new LatLng(25.82, -124.39),
                            new LatLng(49.38, -66.94));

                    AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                            .build();

                    PendingResult<AutocompletePredictionBuffer> pResult =
                            Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, query,
                                    bounds, autocompleteFilter);

                    pResult.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
                        @Override
                        public void onResult(AutocompletePredictionBuffer autocompletePredictions) {
                            Iterator<AutocompletePrediction> iter = autocompletePredictions.iterator();
                            while(iter.hasNext()) {
                                results.add(iter.next().getFullText(null).toString());
                            }

                            filterResults.values = results;
                            filterResults.count = autocompletePredictions.getCount();

                            // Release buffer to prevent memory leaks
                            autocompletePredictions.release();
                        }
                    });
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }


}
