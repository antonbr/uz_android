package com.uzapp.view.main.profile;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.uzapp.R;

import butterknife.BindColor;
import butterknife.BindInt;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

/**
 * Created by vika on 03.09.16.
 */
public class PhoneNumberEditText extends EditText {
    private static final String PHONE_PREFIX = "380";
    //flag to prevent infinite loop of text changed event
    private boolean textChangedFromCode = false;
    private static final String SEPARATOR = " ";
    private static final int FIRST_GROUP_LENGTH = 2;
    private static final int SECOND_GROUP_LENGTH = 6;
    private static final int THIRD_GROUP_LENGTH = 9;
    @BindColor(R.color.textColorHint) int hintColor;
    @BindInt(R.integer.profile_phone_number_length) int phoneNumberLength;

    public PhoneNumberEditText(Context context) {
        this(context, null);
    }

    public PhoneNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        ButterKnife.bind(this);
    }

    public String getPhoneNumber() {
        String phone = getText().toString().replaceAll(SEPARATOR, "");
        if(phone.length()>0){
            phone = PHONE_PREFIX+phone;
        }
        return phone;
    }

    public void setPhone(String phone) {
        phone = phone.replaceAll(SEPARATOR, "").replaceAll("\\+", "").replaceAll(PHONE_PREFIX, "");
        setText(phone);
    }

    public boolean isValid() {
        return (getText().length() == 0 ||
                getText().length() == phoneNumberLength);
    }

    @OnTextChanged(value = R.id.phoneField,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onPhoneTextChanged(Editable editable) {
        if (!textChangedFromCode) {
            String oldText = editable.toString();
            int cursorPosition = getSelectionEnd();
            boolean isCursorAtTheEnd = cursorPosition == oldText.length();

            if (!isCursorAtTheEnd) {
                int index = oldText.indexOf(SEPARATOR);
                while (index >= 0) {
                    if (index <= cursorPosition) {
                        cursorPosition--;
                    }
                    index = oldText.indexOf(SEPARATOR, index + 1);
                }
            }
            StringBuilder allText = new StringBuilder(oldText.replaceAll(SEPARATOR, ""));
            if (allText.length() == 0 && !isFocused()) return;


            if (allText.length() > FIRST_GROUP_LENGTH) {
                allText.insert(FIRST_GROUP_LENGTH, SEPARATOR);
                if (cursorPosition > FIRST_GROUP_LENGTH) {
                    cursorPosition++;
                }
            }
            if (allText.length() > SECOND_GROUP_LENGTH) {
                allText.insert(SECOND_GROUP_LENGTH, SEPARATOR);
                if (cursorPosition > SECOND_GROUP_LENGTH) {
                    cursorPosition++;
                }
            }
            if (allText.length() > THIRD_GROUP_LENGTH) {
                allText.insert(THIRD_GROUP_LENGTH, SEPARATOR);
                if (cursorPosition > THIRD_GROUP_LENGTH) {
                    cursorPosition++;
                }
            }
            textChangedFromCode = true;
            setText(allText, TextView.BufferType.SPANNABLE);
            setSelection(isCursorAtTheEnd ? allText.length() : cursorPosition);
        } else {
            textChangedFromCode = false;
        }
    }
}
