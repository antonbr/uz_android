package com.uzapp.view.main.profile;

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
 * Created by vika on 18.08.16.
 */
public class PasswordDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.profile_edit_password);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.profile_password_dialog, null);
        final EditText input = (EditText) viewInflated.findViewById(R.id.passwordField);
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = input.getText().toString();
                setResult(password);
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

    private void setResult(String password) {
        if (CommonUtils.isPasswordValid(password)) {
            Intent intent = new Intent();
            intent.putExtra("password", password);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            dismiss();
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.profile_password_not_valid), Toast.LENGTH_SHORT).show();
        }
    }
}

