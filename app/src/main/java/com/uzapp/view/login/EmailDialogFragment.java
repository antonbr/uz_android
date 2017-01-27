package com.uzapp.view.login;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uzapp.R;
import com.uzapp.util.CommonUtils;

/**
 * Created by vika on 08.09.16.
 */
public class EmailDialogFragment extends DialogFragment {
    private EditText inputEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
        builder.setTitle(R.string.create_account_email_hint);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.login_email_dialog, null);
        inputEditText = (EditText) viewInflated.findViewById(R.id.emailField);
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = inputEditText.getText().toString();
                    setResult(email);
                }
            });
        }
    }

    private void setResult(String email) {
        if (CommonUtils.isEmailValid(email)) {
            Intent intent = new Intent();
            intent.putExtra("email", email);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            dismiss();
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.login_email_not_valid), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputEditText.getWindowToken(), 0);
        super.onDestroyView();
    }
}
