package com.uzapp.view.login;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.InputType;
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
public class StudentIdTextInputEditText extends TextInputEditText {
    private static final String SEPARATOR = " №";
    private static final int SERIES_LENGTH = 2;
    private static final int TEXT_INPUT = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;
    private static final int NUMBER_INPUT = InputType.TYPE_CLASS_NUMBER;
    //flags to prevent infinite loop of text changed event
    private boolean textChangedFromCode = false, cursorChangedFromCode = false;
    private int lastCursorPosition;
    @BindColor(R.color.textColorHint) int hintColor;
    @BindInt(R.integer.student_id_full_length) int studentIdLength;

    public StudentIdTextInputEditText(Context context) {
        this(context, null);
    }

    public StudentIdTextInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        ButterKnife.bind(this);
    }

    public String getStudentId() {
        return getText().toString().replaceAll(SEPARATOR, "").replaceAll(" ", "");
    }


    public boolean isValid() {
        return getText().length() == 0 || getText().length() == studentIdLength;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (!cursorChangedFromCode) {
            int oldInputType = getInputType();
            int newInputType = getText().length() >= SERIES_LENGTH ? NUMBER_INPUT : TEXT_INPUT;
            if (getText().length() == SERIES_LENGTH && selEnd < SERIES_LENGTH) {
                newInputType = TEXT_INPUT;
            }
            if (oldInputType != newInputType) {
                setInputType(newInputType);
            }
            if (getText().length() > SERIES_LENGTH + SEPARATOR.length() && selEnd < SERIES_LENGTH + SEPARATOR.length()) {
                cursorChangedFromCode = true;
                setSelection(Math.min(lastCursorPosition, getText().length()));
            } else {
                lastCursorPosition = selEnd;
            }
        } else {
            cursorChangedFromCode = false;
        }
    }

    @OnTextChanged(value = R.id.studentIdField,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onStudentIdTextChanged(Editable editable) {
        if (!textChangedFromCode) {
            String oldText = editable.toString();
            int cursorPosition = getSelectionEnd();
            boolean isCursorAtTheEnd = cursorPosition == oldText.length();

            if (!isCursorAtTheEnd) {
                int index = oldText.indexOf(" ");
                while (index >= 0) {
                    if (index <= cursorPosition) {
                        cursorPosition--;
                    }
                    index = oldText.indexOf(" ", index + 1);
                }
                index = oldText.indexOf(SEPARATOR);
                if (index >= 0 && index <= cursorPosition) {
                    cursorPosition--;
                }
            }

            StringBuilder allText = new StringBuilder(oldText.replaceAll(SEPARATOR, "").replaceAll(" ", ""));
            for (int i = 0; i < SERIES_LENGTH; i++) {
                if (allText.length() > i && !Character.isLetter(allText.charAt(i))) {
                    allText.deleteCharAt(i);
                    if (cursorPosition <= i) {
                        cursorPosition--;
                    }
                }
            }
            if (allText.length() > SERIES_LENGTH) {
                for (int i = SERIES_LENGTH; i < allText.length(); i++) {
                    if (!Character.isDigit(allText.charAt(i))) {
                        allText.deleteCharAt(i);
                        if (cursorPosition <= i) {
                            cursorPosition--;
                        }
                    }
                }
            }
            if (allText.length() > SERIES_LENGTH && allText.charAt(SERIES_LENGTH) != Character.SPACE_SEPARATOR) {
                allText.insert(SERIES_LENGTH, SEPARATOR);
                if (cursorPosition > SERIES_LENGTH) {
                    cursorPosition = cursorPosition + SEPARATOR.length();
                }
            }
            textChangedFromCode = true;

            Spannable spannable = new SpannableString(allText);
            if (allText.length() > SERIES_LENGTH) {
                spannable.setSpan(new ForegroundColorSpan(hintColor), SERIES_LENGTH, SERIES_LENGTH + SEPARATOR.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            setText(spannable, TextView.BufferType.SPANNABLE);

            setSelection(Math.min(cursorPosition, getText().length()));

        } else {
            textChangedFromCode = false;
        }
    }

}
