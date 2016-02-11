package kidouchi.chronobook.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.realm.Realm;
import kidouchi.chronobook.alarm.AlarmReceiver;
import kidouchi.chronobook.R;
import kidouchi.chronobook.RoundedCornersImageView;
import kidouchi.chronobook.fragments.EventCategoryFragment;
import kidouchi.chronobook.models.Event;
import kidouchi.chronobook.models.Location;

public class EventFormActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        EventCategoryFragment.CategoryChosenListener {

    private static final int REQUEST_CHOOSE_IMAGE = 1012;

    /**
     * VIEWS
     **/
    private EditText mTitleEditText;
    private EditText mDescEditText;
    private EditText mStreetEditText;
    private EditText mCityEditText;
    private EditText mZipcodeEditText;
    private Spinner mStateSpinner;

    private TextView mPlaceholderTextView;
    private Bitmap mPlaceholderBitmap;
    //    private ImageView mPlaceholderImageView;
    private RoundedCornersImageView mPlaceholderImageView;
    private Button mPlaceholderUploadBtn;

    private Button mStartDateBtn;
    private Button mEndDateBtn;
    private TextView mStartDateTextView;
    private TextView mEndDateTextView;

    private Button mStartTimeBtn;
    private Button mEndTimeBtn;
    private TextView mStartTimeTextView;
    private TextView mEndTimeTextView;

    private EditText mEventLocation;
    private ImageView mCategoryImageView;
    private FloatingActionButton mSubmitButton;

    /**
     * GLOBAL VARIABLES
     **/
    private Realm realm;
    private EventCategoryFragment eventCategoryFragment; // Event catgory dialog
    private Button pressedButton; // Holds reference to what button was most recently pressed
    private int categoryDrawableId = 0; // Holds most recently chosen category drawable id
    private String placeholderFilepath = ""; // Holds placeholder image filepath chosen
    private Event event;

    public static String getRealPathFromURI(Context context, Uri uri) {
        String filePath = "";

        // Extract the COLUMN_DOCUMENT_ID from the given URI.
        String docID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        // ex. image:90 --> retrieve 90
        String id = docID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String selection = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column,
                selection,
                new String[]{id},
                null);

        // Get first result
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();

        return filePath;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        realm = Realm.getDefaultInstance();

        mTitleEditText = (EditText) findViewById(R.id.event_form_title);
        mDescEditText = (EditText) findViewById(R.id.event_form_description);
        mStreetEditText = (EditText) findViewById(R.id.event_form_address);
        mCityEditText = (EditText) findViewById(R.id.event_form_city);
        mZipcodeEditText = (EditText) findViewById(R.id.event_form_zipcode);
        mStateSpinner = (Spinner) findViewById(R.id.event_form_state);

        mPlaceholderTextView = (TextView) findViewById(R.id.event_form_placeholder_text_view);
//        mPlaceholderImageView = (ImageView) findViewById(R.id.event_form_placeholder_image_view);
        mPlaceholderImageView = (RoundedCornersImageView) findViewById(R.id.event_form_placeholder_image_view);
        mPlaceholderUploadBtn = (Button) findViewById(R.id.event_form_placeholder_upload_btn);

        mStartDateBtn = (Button) findViewById(R.id.event_form_start_date_btn);
        mEndDateBtn = (Button) findViewById(R.id.event_form_end_date_btn);
        mStartDateTextView = (TextView) findViewById(R.id.event_form_start_date_btn_text_view);
        mEndDateTextView = (TextView) findViewById(R.id.event_form_end_date_btn_text_view);

        mStartTimeBtn = (Button) findViewById(R.id.event_form_start_time_btn);
        mEndTimeBtn = (Button) findViewById(R.id.event_form_end_time_btn);
        mStartTimeTextView = (TextView) findViewById(R.id.event_form_start_time_btn_text_view);
        mEndTimeTextView = (TextView) findViewById(R.id.event_form_end_time_btn_text_view);

        mEventLocation = (EditText) findViewById(R.id.event_form_address);
        mCategoryImageView = (ImageView) findViewById(R.id.category_image_view);

        mSubmitButton = (FloatingActionButton) findViewById(R.id.form_submit_button);

        setupTitleError();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (event != null) {
            getMenuInflater().inflate(R.menu.menu_event_view, menu);
            return true;
        }
        return false;
    }

    public void onDelete(MenuItem menuItem) {
        // Remove from database
//        realm.beginTransaction();
//
//        Event event = mEvents.get(pos);
//        event.removeFromRealm();
//
//        realm.commitTransaction();

    }

    public void onSubmit(View v) {
        // Insert location into DB

        realm.beginTransaction();
        Location loc = new Location();
        Number locMaxId = realm.where(Location.class).max("id");
        int nextLocId = (locMaxId != null) ? (locMaxId.intValue() + 1) : 0;
        loc.setId(nextLocId);
        loc.setStreet(mStreetEditText.getText().toString());
        loc.setState(mStateSpinner.getSelectedItem().toString());
        loc.setCity(mCityEditText.getText().toString());
        loc.setZipcode(mZipcodeEditText.getText().toString());
        realm.copyToRealmOrUpdate(loc);
        realm.commitTransaction();

        // Insert Event into DB
        realm.beginTransaction();
        Event event = new Event();
        Number eventMaxId = realm.where(Event.class).max("id");
        int nextEventId = (eventMaxId != null) ? (eventMaxId.intValue() + 1) : 0;
        event.setId(nextEventId);
        event.setPlaceHolderFilepath(placeholderFilepath);
        event.setTitle(mTitleEditText.getText().toString());
        event.setDescription(mDescEditText.getText().toString());
        try {
            long startDateTime = convertDateTimeToLong(
                    mStartDateTextView.getText().toString(),
                    mStartTimeTextView.getText().toString()
            );
            event.setStartDateTime(startDateTime);
            setAlarm(startDateTime, event.getTitle()); // Set alarm notification when event starts
            event.setEndDateTime(convertDateTimeToLong(
                    mEndDateTextView.getText().toString(),
                    mEndTimeTextView.getText().toString()
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        event.setLocation(loc);
        event.setCategoryDrawable(categoryDrawableId);
        realm.copyToRealmOrUpdate(event);
        realm.commitTransaction();

        Intent intent = new Intent(this, EventListViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
        startActivityForResult(choosePictureIntent, REQUEST_CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_IMAGE && resultCode == RESULT_OK) {
            Picasso.with(this)
                    .load(data.getData())
                    .into(mPlaceholderImageView);

            Uri uri = data.getData();
            placeholderFilepath = getRealPathFromURI(this, uri);
        }
    }

    /**
     * Converts date and time string to long
     *
     * @param date string
     * @param time string
     * @return long representing date and time in milliseconds
     * @throws ParseException
     */
    private long convertDateTimeToLong(String date, String time) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a MM/dd/yyyy", Locale.US);
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.parse(time + " " + date).getTime();
    }

    private void setupTitleError() {
        final TextInputLayout titleLabel = (TextInputLayout) findViewById(R.id.event_form_title_label);
        if (!mTitleEditText.isFocused()) {
            if (mTitleEditText.getText().length() == 0) {
                titleLabel.setError(getString(R.string.title_required));
                titleLabel.setErrorEnabled(true);
            }
        }

        titleLabel.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mTitleEditText.isFocused()) {
                    if (s.length() > 0) {
                        titleLabel.setErrorEnabled(false);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Convert 24 hour of day to am/pm format
        try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            final Date date = dateFormat.parse(hourOfDay + ":" + minute);
            if (pressedButton != null) {
                if (pressedButton.getId() == R.id.event_form_start_time_btn) {
                    mStartTimeTextView.setText(new SimpleDateFormat("h:mm a").format(date));
                }
                if (pressedButton.getId() == R.id.event_form_end_time_btn) {
                    mEndTimeTextView.setText(new SimpleDateFormat("h:mm a").format(date));
                }
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
}
