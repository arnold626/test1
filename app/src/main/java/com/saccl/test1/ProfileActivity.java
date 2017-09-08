package com.saccl.test1;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    private ImageView mProfileImage;
    private TextView mProfileDisplayName;
    private TextView mProfileStatus;
    private TextView mProfileTotalFriends;
    private Button mProfileSendRequestBtn;
    private Button mProfileDeclineBtn;

    private DatabaseReference mUserDatabase;
    private DatabaseReference mFriendRequestDatabase;

    private FirebaseUser mCurrentUser;
    private String mCurrentUid;


    private ProgressDialog mProgress;

    private String mCurrent_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mCurrentUid = mCurrentUser.getUid();

        final String user_id = getIntent().getStringExtra("user_id");

        mProfileImage = (ImageView) findViewById(R.id.profile_image);
        mProfileDisplayName = (TextView) findViewById(R.id.profile_display_name);
        mProfileStatus = (TextView) findViewById(R.id.profile_status);
        mProfileTotalFriends = (TextView) findViewById(R.id.profile_total_friends);
        mProfileSendRequestBtn = (Button) findViewById(R.id.profile_send_request_btn);
        mProfileDeclineBtn = (Button) findViewById(R.id.profile_decline_btn);

        mCurrent_status = "not_friends";

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Loading User Data");
        mProgress.setMessage("Please wait while we are loading data");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendRequestDatabase = FirebaseDatabase.getInstance().getReference().child("FriendRequest");

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String display_name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                mProfileDisplayName.setText(display_name);
                mProfileStatus.setText(status);
                Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.default_avator).into(mProfileImage);

                // ----  FRIENDS LIST /  REQUEST FEATURE ----
                mFriendRequestDatabase.child(mCurrentUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(user_id)){
                            String requestType = dataSnapshot.child(user_id).child("request_type").getValue().toString();
                            Toast.makeText(ProfileActivity.this, requestType, Toast.LENGTH_LONG).show();
                            if(requestType.equals("received")) {
                                mCurrent_status = "req_received";
                                mProfileSendRequestBtn.setText("ACCEPT FRIEND REQUEST");
                            } else if(requestType.equals("sent")) {
                                mCurrent_status = "req_sent";
                                mProfileSendRequestBtn.setText("CANCEL FRIEND REQUEST");
                            }

                        }

                        mProgress.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProfileSendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfileSendRequestBtn.setEnabled(false);

                // ------- NOT FRIENDS -------
                if(mCurrent_status.equals("not_friends")) {
                    mFriendRequestDatabase.child(mCurrentUid).child(user_id).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                mFriendRequestDatabase.child(user_id).child(mCurrentUid).child("request_type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mProfileSendRequestBtn.setEnabled(true);
                                        mCurrent_status = "req_sent";
                                        mProfileSendRequestBtn.setText("CANCEL FRIEND REQUEST");

                                        // Toast.makeText(ProfileActivity.this, "Request Sent!", Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else {
                                Toast.makeText(ProfileActivity.this, "Sent Request failed!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                // ------- REQUEST SENT -------
                if(mCurrent_status.equals("req_sent")) {
                    mFriendRequestDatabase.child(mCurrentUid).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendRequestDatabase.child(user_id).child(mCurrentUid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProfileSendRequestBtn.setEnabled(true);
                                    mCurrent_status = "not_friends";
                                    mProfileSendRequestBtn.setText("SEND REQUEST");

                                    Toast.makeText(ProfileActivity.this, "Request Removed!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }

            }
        });









    }
}

