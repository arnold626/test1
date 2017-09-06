package com.saccl.test1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mCreateBtn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Toolbar mToolbar;

    // ProgressDialog
    private ProgressDialog mRegProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.create_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegProgressDialog = new ProgressDialog(this);



        mDisplayName = (TextInputLayout) findViewById(R.id.reg_display_name);
        mEmail = (TextInputLayout) findViewById(R.id.reg_email);
        mPassword = (TextInputLayout) findViewById(R.id.reg_password);
        mCreateBtn = (Button) findViewById(R.id.reg_create_btn);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display_name = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                if(!(TextUtils.isEmpty(display_name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password))) {

                    mRegProgressDialog.setTitle("Registering User");
                    mRegProgressDialog.setMessage("Please wait while we create your account");
                    mRegProgressDialog.setCanceledOnTouchOutside(false);
                    mRegProgressDialog.show();

                    register_user(display_name, email, password);

                }

            }
        });




    }

    private void register_user(String display_name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            mRegProgressDialog.dismiss();

                            Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();

                        } else {

                            mRegProgressDialog.hide();

                            Toast.makeText(RegisterActivity.this, "Cannot sign in. Please check the form and try again.",
                                    Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
    }
}
