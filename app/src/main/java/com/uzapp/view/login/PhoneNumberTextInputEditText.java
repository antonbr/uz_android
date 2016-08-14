package com.uzapp.view.login;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.uzapp.R;

import butterknife.BindColor;
import butterknife.BindInt;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

/**
 * Created by vika on 12.08.16.
 */
public class PhoneNumberTextInputEditText extends TextInputEditText {
    //flag to prevent infinite loop of text changed event
    private boolean textChangedFromCode = false;
    private static final String FIRST_SIGN = "+";
    private static final String SEPARATOR = " ";
    private static final int FIRST_GROUP_LENGTH = 4;
    private static final int SECOND_GROUP_LENGTH = 7;
    private static final int THIRD_GROUP_LENGTH = 11;
    private static final int FOURTH_GROUP_LENGTH = 14;
    @BindColor(R.color.textColorHint) int hintColor;
    @BindInt(R.integer.phone_number_length) int phoneNumberLength;

    public PhoneNumberTextInputEditText(Context context) {
        this(context, null);
    }

    public PhoneNumberTextInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        ButterKnife.bind(this);
    }

    public String getPhoneNumber() {
        return getText().toString().replaceAll(SEPARATOR, "").replaceAll("\\+","");
    }

    public boolean isValid() {
        return (getText().length() == 0 ||
                getText().length() == 1 ||
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

            if (allText.toString().contains(FIRST_SIGN) && allText.charAt(0) != FIRST_SIGN.charAt(0)) {
                allText.delete(0, allText.indexOf(FIRST_SIGN));
            }
            if (!allText.toString().contains(FIRST_SIGN)) {
                allText.insert(0, FIRST_SIGN);
                cursorPosition++;
            }

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
            if (allText.length() > FOURTH_GROUP_LENGTH) {
                allText.insert(FOURTH_GROUP_LENGTH, SEPARATOR);
                if (cursorPosition > FOURTH_GROUP_LENGTH) {
                    cursorPosition++;
                }
            }
            textChangedFromCode = true;
            Spannable spannable = new SpannableString(allText);
            spannable.setSpan(new ForegroundColorSpan(hintColor), 0, FIRST_SIGN.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            setText(spannable, TextView.BufferType.SPANNABLE);
            setSelection(isCursorAtTheEnd ? allText.length() : cursorPosition);
            //TODO checkFieldsState();
        } else {
            textChangedFromCode = false;
        }
    }
}
