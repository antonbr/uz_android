package com.uzapp.view.search;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.uzapp.util.Constants;

import java.lang.ref.WeakReference;

/**
 * Created by vika on 15.07.16.
 */
public class SearchEditText extends EditText {
    private static final int MESSAGE_TEXT_CHANGED_CODE = 1;
    private int autocompleteDelay = Constants.SEARCH_AUTOCOMPLETE_DELAY;
    private MyHandler handler;
    private ContentChangedListener contentChangedListener;

    public interface ContentChangedListener {
        void onSearchLetterEntered(String msg);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new MyHandler(this);
        addTextChangedListener(textWatcher);
    }

    public void setContentChangedListener(ContentChangedListener contentChangedListener) {
        this.contentChangedListener = contentChangedListener;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        showKeyboard();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        contentChangedListener = null;
        hideKeyboard();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
    }

    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void filterSearchResult(String msg) {
        if (contentChangedListener != null) {
            contentChangedListener.onSearchLetterEntered(msg);
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            handler.removeMessages(MESSAGE_TEXT_CHANGED_CODE);
            handler.sendMessageDelayed(handler.obtainMessage(MESSAGE_TEXT_CHANGED_CODE, s.toString()), autocompleteDelay);
        }
    };


    private static class MyHandler extends Handler {
        private final WeakReference<SearchEditText> editTextWeakRef;

        MyHandler(SearchEditText editText) {
            this.editTextWeakRef = new WeakReference<SearchEditText>(editText);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SearchEditText editText = editTextWeakRef.get();
            if (editText != null) {
                editText.filterSearchResult((String) msg.obj);
            }
        }
    }
}