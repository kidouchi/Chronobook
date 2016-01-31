package kidouchi.chronobook.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import kidouchi.chronobook.R;

/**
 * Created by iuy407 on 11/23/15.
 */
public class EventCategoryFragment extends DialogFragment {

    private ImageButton mCategoryButton1;
    private ImageButton mCategoryButton2;
    private ImageButton mCategoryButton3;
    private ImageButton mCategoryButton4;
    private ImageButton mCategoryButton5;
    private ImageButton mCategoryButton6;
    private ImageButton mCategoryButton7;
    private ImageButton mCategoryButton8;

    static EventCategoryFragment newInstance() {
        return new EventCategoryFragment();
    }

    public interface CategoryChosenListener {
        public void getCategoryChosen(View v);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.event_category_dialog, null);

        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing for now
                    }
                });

        //TODO: Look for better way to do this?
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryChosenListener activity = (CategoryChosenListener) getActivity();
                activity.getCategoryChosen(v);

            }
        };

        // Set button views
        mCategoryButton1 = (ImageButton) view.findViewById(R.id.category1);
        mCategoryButton2 = (ImageButton) view.findViewById(R.id.category2);
        mCategoryButton3 = (ImageButton) view.findViewById(R.id.category3);
        mCategoryButton4 = (ImageButton) view.findViewById(R.id.category4);
        mCategoryButton5 = (ImageButton) view.findViewById(R.id.category5);
        mCategoryButton6 = (ImageButton) view.findViewById(R.id.category6);
        mCategoryButton7 = (ImageButton) view.findViewById(R.id.category7);
        mCategoryButton8 = (ImageButton) view.findViewById(R.id.category8);

        // Set drawable id into button views
        mCategoryButton1.setTag(R.drawable.ic_flight_grey_600_36dp);
        mCategoryButton2.setTag(R.drawable.ic_local_movies_grey_600_36dp);
        mCategoryButton3.setTag(R.drawable.ic_local_restaurant_grey_600_36dp);
        mCategoryButton4.setTag(R.drawable.ic_local_bar_grey_600_36dp);
        mCategoryButton5.setTag(R.drawable.ic_cake_grey_600_36dp);
        mCategoryButton6.setTag(R.drawable.ic_work_grey_600_36dp);
        mCategoryButton7.setTag(R.drawable.ic_shopping_cart_grey_600_36dp);
        mCategoryButton8.setTag(R.drawable.ic_people_grey_600_36dp);

        // Set listener to each button view
        mCategoryButton1.setOnClickListener(listener);
        mCategoryButton2.setOnClickListener(listener);
        mCategoryButton3.setOnClickListener(listener);
        mCategoryButton4.setOnClickListener(listener);
        mCategoryButton5.setOnClickListener(listener);
        mCategoryButton6.setOnClickListener(listener);
        mCategoryButton7.setOnClickListener(listener);
        mCategoryButton8.setOnClickListener(listener);

        Dialog dialog = builder.create();

        // Add enter/exit transition animation
        dialog.getWindow().getAttributes().windowAnimations = R.style.CategoryDialogAnimation;

        return dialog;
    }
}
