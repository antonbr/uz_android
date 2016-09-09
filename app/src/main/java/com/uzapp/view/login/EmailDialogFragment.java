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
import android.widget.EditText;
import android.widget.Toast;

import com.uzapp.R;
import com.uzapp.util.CommonUtils;

/**
 * Created by vika on 08.09.16.
 */
public class EmailDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
        builder.setTitle(R.string.create_account_email_hint);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.login_email_dialog, null);
        final EditText input = (EditText) viewInflated.findViewById(R.id.emailField);
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = input.getText().toString();
                setResult(email);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
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
}
