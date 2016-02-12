package kidouchi.chronobook;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by iuy407 on 2/11/16.
 */
public abstract class EventFormValidator implements TextWatcher {

    private EditText mEditText = null;
    private TextView mTextView = null;

    public EventFormValidator(EditText editText) {
        this.mEditText = editText;
    }

    public EventFormValidator(TextView textView) {
        this.mTextView = textView;
    }

    public abstract void validate(String s);

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if (mEditText != null) {
            Log.d("TEST", s.toString());
            validate(mEditText.getText().toString());
        }
        if (mTextView != null) {
            validate(mTextView.getText().toString());
        }
    }
}
