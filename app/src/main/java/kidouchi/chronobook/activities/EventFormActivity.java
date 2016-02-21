package kidouchi.chronobook.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import kidouchi.chronobook.EventFormValidator;
import kidouchi.chronobook.R;
import kidouchi.chronobook.alarm.AlarmReceiver;
import kidouchi.chronobook.fragments.EventCategoryFragment;
import kidouchi.chronobook.models.Event;
import kidouchi.chronobook.util.Constants;
import kidouchi.chronobook.util.ImageUtil;
import kidouchi.chronobook.util.TimeConverterUtil;
import kidouchi.chronobook.views.RoundedCornersImageView;

public class EventFormActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        EventCategoryFragment.CategoryChosenListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    /******************
     * VIEWS
     ******************/
    private EditText mTitleEditText;
    private EditText mDescEditText;

//    private MultiAutoCompleteTextView mStreetTextView;
    private EditText mLocation;
//    private EditText mCityEditText;
//    private EditText mZipcodeEditText;
//    private Spinner mStateSpinner;

    private TextView mPlaceholderTextView;
    private Bitmap mPlaceholderBitmap;
    private RoundedCornersImageView mPlaceholderImageView;
    private Button mPlaceholderUploadBtn;

    private Button mStartDateBtn;
    private Button mEndDateBtn;
    private TextView mStartDateTextView;
    private TextView mEndDateTextView;

    private CheckBox allDayCheckbox;
    private Button mStartTimeBtn;
    private Button mEndTimeBtn;
    private TextView mStartTimeTextView;
    private TextView mEndTimeTextView;

    private EditText mEventLocation;
    private ImageView mCategoryImageView;
    private FloatingActionButton mSubmitButton;

    private EventCategoryFragment eventCategoryFragment; // Event catgory dialog
    private Button pressedButton; // Holds reference to what button was most recently pressed

    private TextInputLayout titleLabel;
    private TextInputLayout descLabel;
    private TextInputLayout locationLabel;


    private CoordinatorLayout mCoordContent;

    /******************
     * GLOBAL VARIABLES
     ******************/
    private Realm realm;
    private int categoryDrawableId = 0; // Holds most recently chosen category drawable id
    private String placeholderFilepath = ""; // Holds placeholder image filepath chosen
    private Event event;
//    private GoogleApiClient mGoogleApiClient;
//    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);

//        Log.d("CHECK", )
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        realm = Realm.getDefaultInstance();

        mTitleEditText = (EditText) findViewById(R.id.event_form_title);
        mDescEditText = (EditText) findViewById(R.id.event_form_description);

        mLocation = (EditText) findViewById(R.id.event_location);
//        mCityEditText = (EditText) findViewById(R.id.event_form_city);
//        mZipcodeEditText = (EditText) findViewById(R.id.event_form_zipcode);
//        mStateSpinner = (Spinner) findViewById(R.id.event_form_state);

        mPlaceholderTextView = (TextView) findViewById(R.id.event_form_placeholder_text_view);
        mPlaceholderImageView = (RoundedCornersImageView) findViewById(R.id.event_form_placeholder_image_view);
        mPlaceholderUploadBtn = (Button) findViewById(R.id.event_form_placeholder_upload_btn);

        mStartDateBtn = (Button) findViewById(R.id.event_form_start_date_btn);
        mEndDateBtn = (Button) findViewById(R.id.event_form_end_date_btn);
        mStartDateTextView = (TextView) findViewById(R.id.event_form_start_date_text_view);
        mEndDateTextView = (TextView) findViewById(R.id.event_form_end_date_text_view);

        allDayCheckbox = (CheckBox) findViewById(R.id.all_day_checkbox);
        mStartTimeBtn = (Button) findViewById(R.id.event_form_start_time_btn);
        mEndTimeBtn = (Button) findViewById(R.id.event_form_end_time_btn);
        mStartTimeTextView = (TextView) findViewById(R.id.event_form_start_time_text_view);
        mEndTimeTextView = (TextView) findViewById(R.id.event_form_end_time_text_view);

        mEventLocation = (EditText) findViewById(R.id.event_location);
        mCategoryImageView = (ImageView) findViewById(R.id.category_image_view);

        mSubmitButton = (FloatingActionButton) findViewById(R.id.form_submit_button);

        titleLabel = (TextInputLayout) findViewById(R.id.event_form_title_label);
        titleLabel.setErrorEnabled(true);
        descLabel = (TextInputLayout) findViewById(R.id.event_form_desc_label);
        descLabel.setErrorEnabled(true);
        locationLabel = (TextInputLayout) findViewById(R.id.event_location_label);
        locationLabel.setErrorEnabled(true);
//        cityLabel = (TextInputLayout) findViewById(R.id.event_city_label);
//        cityLabel.setErrorEnabled(true);
//        zipcodeLabel = (TextInputLayout) findViewById(R.id.event_zipcode_label);
//        zipcodeLabel.setErrorEnabled(true);

        mCoordContent = (CoordinatorLayout)
                findViewById(android.R.id.content).findViewById(R.id.event_form_coord_layout);

        // Setup form validation here
        setupTitleError();
        setupDescriptionError();
        setupLocationError();

        // Hide soft keyboard whenever outside an input field
//        InputMethodManager inputMethodManager =
//                (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

        // Create the location client to start receiving updates
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();

//        setupPlaceAutoComplete();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (event != null) {
            getMenuInflater().inflate(R.menu.menu_event_view, menu);
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Connect to google api client
//        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        // Disconnect with google api client
//        mGoogleApiClient.disconnect();

        super.onStop();
    }

    public void onDelete(MenuItem menuItem) {
        // Remove from database
        realm.beginTransaction();
        if (event != null) {
            event.removeFromRealm();
        }
        realm.commitTransaction();
    }

    public boolean isValidForm() {
        return !(titleLabel.isErrorEnabled() || descLabel.isErrorEnabled()
                || locationLabel.isErrorEnabled());
    }
    public void onSubmit(View v) {
        if (isValidForm()) {
            // Insert location into DB
//            realm.beginTransaction();
//            Location loc = new Location();
//            Number locMaxId = realm.where(Location.class).max("id");
//            int nextLocId = (locMaxId != null) ? (locMaxId.intValue() + 1) : 0;
//            loc.setId(nextLocId);
//            loc.setStreet(mLocation.getText().toString());
//            loc.setState(mStateSpinner.getSelectedItem().toString());
//            loc.setCity(mCityEditText.getText().toString());
//            loc.setZipcode(mZipcodeEditText.getText().toString());
//            realm.copyToRealmOrUpdate(loc);
//            realm.commitTransaction();

            // Insert Event into DB
            realm.beginTransaction();
            Event event = new Event();
            Number eventMaxId = realm.where(Event.class).max("id");
            int nextEventId = (eventMaxId != null) ? (eventMaxId.intValue() + 1) : 0;
            event.setId(nextEventId);
            event.setPlaceHolderFilepath(placeholderFilepath);
            event.setTitle(mTitleEditText.getText().toString());
            event.setDescription(mDescEditText.getText().toString());

            // Converting time
            try {
                String startTime;
                String endTime;
                if (allDayCheckbox.isChecked()) {
                    startTime = endTime = "12:00 AM";
                } else {
                    startTime = mStartTimeTextView.getText().toString();
                    endTime = mEndTimeTextView.getText().toString();
                }

                long startDateTime = TimeConverterUtil.convertDateTimeToLong(
                        mStartDateTextView.getText().toString(),
                        startTime);
                event.setStartDateTime(startDateTime);
                setAlarm(startDateTime, event.getTitle()); // Set alarm notification when event starts
                event.setEndDateTime(TimeConverterUtil.convertDateTimeToLong(
                        mEndDateTextView.getText().toString(),
                        endTime
                ));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            event.setLocation(mLocation.getText().toString());
            event.setCategoryDrawable(categoryDrawableId);
            realm.copyToRealmOrUpdate(event);
            realm.commitTransaction();

            Intent intent = new Intent(this, EventListViewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Snackbar.make(mCoordContent, getString(R.string.invalid_form_msg), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.snackbar_confirm), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Do nothing
                    }
                }).show();
        }
    }

    private void setAlarm(long targetDateTime, String eventTitle) {
        // Set up alarm
        Intent i = new Intent(getApplicationContext(), AlarmReceiver.class);
        i.putExtra("eventTitle", eventTitle); // Send notification title
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                i, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, targetDateTime, pIntent);
    }

    public void onUpload(View v) {
        Intent choosePictureIntent = new Intent();
        choosePictureIntent.setType("image/*");
        choosePictureIntent.setAction(Intent.ACTION_GET_CONTENT);
        choosePictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(choosePictureIntent, Constants.REQUEST_CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CHOOSE_IMAGE && resultCode == RESULT_OK) {
            Picasso.with(this)
                    .load(data.getData())
                    .into(mPlaceholderImageView);

            Uri uri = data.getData();
            placeholderFilepath = ImageUtil.getRealPathFromURI(this, uri);
        }
    }

    private void setupTitleError() {
        mTitleEditText.addTextChangedListener(new EventFormValidator(mTitleEditText) {
            @Override
            public void validate(String s) {
                if (s.length() == 0) {
                    titleLabel.setError(getString(R.string.error_field_required));
                    titleLabel.setErrorEnabled(true);
                } else {
                    titleLabel.setErrorEnabled(false);
                }
            }
        });
    }

    private void setupDescriptionError() {
        mDescEditText.addTextChangedListener(new EventFormValidator(mDescEditText) {
            @Override
            public void validate(String s) {
                if (s.length() == 0) {
                    descLabel.setError(getString(R.string.error_field_required));
                    descLabel.setErrorEnabled(true);
                } else {
                    descLabel.setErrorEnabled(false);
                }
            }
        });
    }

    private void setupLocationError() {
        mLocation.addTextChangedListener(new EventFormValidator(mLocation) {
            @Override
            public void validate(String s) {
                if (s.length() == 0) {
                    locationLabel.setError(getString(R.string.error_field_required));
                    locationLabel.setErrorEnabled(true);
                } else {
                    locationLabel.setErrorEnabled(false);
                }
            }
        });
//
//        mCityEditText.addTextChangedListener(new EventFormValidator(mCityEditText) {
//            @Override
//            public void validate(String s) {
//                if (s.length() == 0) {
//                    cityLabel.setError(getString(R.string.error_field_required));
//                    cityLabel.setErrorEnabled(true);
//                } else {
//                    cityLabel.setErrorEnabled(false);
//                }
//            }
//        });

//        mZipcodeEditText.addTextChangedListener(new EventFormValidator(mZipcodeEditText) {
//            @Override
//            public void validate(String s) {
//                if (s.length() == 0) {
//                    zipcodeLabel.setError(getString(R.string.error_field_required));
//                    zipcodeLabel.setErrorEnabled(true);
//                } else if (s.length() < 5) {
//                    zipcodeLabel.setError(getString(R.string.not_valid_zipcode));
//                } else {
//                    zipcodeLabel.setErrorEnabled(false);
//                }
//            }
//        });
    }

    /**
     * Will automatically adjust start and end TIME when they are incorrect
     */
    public void onAllDayChecked(View v) {
        if (allDayCheckbox.isChecked()) {
            mStartTimeTextView.setVisibility(View.GONE);
            mStartTimeBtn.setVisibility(View.GONE);
            mEndTimeTextView.setVisibility(View.GONE);
            mEndTimeBtn.setVisibility(View.GONE);
        } else {
            mStartTimeTextView.setVisibility(View.VISIBLE);
            mStartTimeBtn.setVisibility(View.VISIBLE);
            mEndTimeTextView.setVisibility(View.VISIBLE);
            mEndTimeBtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Check the dates are correct and adjust if needed
     */
    private void checkDates() {
        Calendar cal = Calendar.getInstance();
        // Remove time, only want date
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date current = new Date(cal.getTimeInMillis());
        try {
            Date start = mStartDateTextView.getText().length() > 0 ?
                    TimeConverterUtil.convertStringToDate(mStartDateTextView.getText().toString()) : null;
            Date end = mEndDateTextView.getText().length() > 0 ?
                    TimeConverterUtil.convertStringToDate(mEndDateTextView.getText().toString()) : null;
            if (start != null) { // Check start date not empty
                Log.d("Current time", current.toString());
                if(start.before(current)) {
                    mStartDateTextView.setText("");
                    Snackbar.make(mCoordContent, R.string.date_passed_error_msg, Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.snackbar_confirm), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Do nothing
                                }
                            }).show();
                }
            }

            if (end != null) { // Check end date not empty
                if(end.before(current)) {
                    mEndDateTextView.setText("");
                    Snackbar.make(mCoordContent, R.string.date_passed_error_msg, Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.snackbar_confirm), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Do nothing
                                }
                            }).show();
                }
            }

            if (start != null && end != null) { // Check both dates are not empty
                // Adjust END date when it's before START date
                if (start.after(end)) {
                    mEndDateTextView.setText(mStartDateTextView.getText());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (pressedButton != null) {
            if (pressedButton.getId() == R.id.event_form_start_date_btn) {
                mStartDateTextView.setText(monthOfYear + 1 + "/" + dayOfMonth + "/" + year);
            }
            if (pressedButton.getId() == R.id.event_form_end_date_btn) {
                mEndDateTextView.setText(monthOfYear + 1 + "/" + dayOfMonth + "/" + year);
            }
            checkDates(); // Check dates make sense and adjust accordingly
        }
    }

    /**
     * Check the times are correct and adjust if needed
     */
    private void checkTimes() {
        try {
            if (mStartTimeTextView.getText().length() > 0 &&
                    mEndTimeTextView.getText().length() > 0) {
                Date start = TimeConverterUtil.convertStringToTime(
                                    mStartTimeTextView.getText().toString());
                Date end = TimeConverterUtil.convertStringToTime(
                                    mEndTimeTextView.getText().toString());
                // Adjust END time when it's before START time
                if (start.after(end)) {
                    mEndTimeTextView.setText(mStartTimeTextView.getText());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Convert 24 hour of day to am/pm format
        try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            final Date date = dateFormat.parse(hourOfDay + ":" + minute);
            String time = new SimpleDateFormat("h:mm a").format(date);
            if (pressedButton != null) {
                if (pressedButton.getId() == R.id.event_form_start_time_btn) {
                    mStartTimeTextView.setText(time);
                }
                if (pressedButton.getId() == R.id.event_form_end_time_btn) {
                    mEndTimeTextView.setText(time);
                }
                checkTimes(); // Check times make sense and adjust accordingly
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getCategoryChosen(View v) {
        Toast.makeText(this, "View is " + (Integer) ((ImageButton) v).getTag(), Toast.LENGTH_LONG).show();
        if (eventCategoryFragment != null) {
            eventCategoryFragment.dismiss();
            mCategoryImageView.setImageDrawable(((ImageButton) v).getDrawable());
            categoryDrawableId = (Integer) ((ImageButton) v).getTag();
        }
    }

    public void showDatePickerDialog(View v) {
        pressedButton = (Button) v;
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        pressedButton = (Button) v;
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showEventCategoryDialog(View v) {
        eventCategoryFragment = new EventCategoryFragment();
        eventCategoryFragment.show(getFragmentManager(), "eventCategory");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!realm.isClosed()) {
            realm.close();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("FAILURE", "Google Api Connection");

    }

    // Date picker dialog for when user needs to set start/end date
    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use current date as default input
            final Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),
                    (EventFormActivity) getActivity(), year, month, day);
        }
    }

    public static class TimePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Set Midnight as default time
            return new TimePickerDialog(getActivity(), (EventFormActivity) getActivity(),
                    0, 0, false);
        }
    }


    // TODO: REMOVE LATER JUST A MARKER
//    private void setupPlaceAutoComplete() {
//        mStreetTextView = (MultiAutoCompleteTextView) findViewById(R.id.event_form_address);
//        PlacesAutoCompleteAdapter adapter = new PlacesAutoCompleteAdapter(this,
//                R.layout.place_autocomplete_item_prediction, mGoogleApiClient);
//        mStreetTextView.setAdapter(adapter);
//        mStreetTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mStreetTextView.setText((String) parent.getItemAtPosition(position));
//            }
//        });
//
//        mStreetTextView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                // Remove all callbacks and messages
//                mHandler.removeCallbacksAndMessages(null);
//
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapter.re
//                    }
//                }, 500);
//            }
//        });
//    }
}
