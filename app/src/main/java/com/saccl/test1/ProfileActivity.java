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


import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private ImageView mProfileImage;
    private TextView mProfileDisplayName;
    private TextView mProfileStatus;
    private TextView mProfileTotalFriends;
    private Button mProfileSendRequestBtn;
    private Button mProfileDeclineBtn;

    private DatabaseReference mUserDatabase;
    private DatabaseReference mFriendRequestDatabase;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mNotificationDatabase;
    private DatabaseReference mRootDatabase;



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
        mProfileDeclineBtn.setVisibility(View.INVISIBLE);
        mProfileDeclineBtn.setEnabled(false);

        mCurrent_status = "not_friends";

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Loading User Data");
        mProgress.setMessage("Please wait while we are loading data");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendRequestDatabase = FirebaseDatabase.getInstance().getReference().child("FriendRequest");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("Notifications");
        mRootDatabase = FirebaseDatabase.getInstance().getReference();


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

                            if(requestType.equals("received")) {
                                mCurrent_status = "req_received";
                                mProfileSendRequestBtn.setText("ACCEPT FRIEND REQUEST");

                                mProfileDeclineBtn.setVisibility(View.VISIBLE);
                                mProfileDeclineBtn.setEnabled(true);

                            } else if(requestType.equals("sent")) {
                                mCurrent_status = "req_sent";
                                mProfileSendRequestBtn.setText("CANCEL FRIEND REQUEST");
                            }

                        } else {
                            mFriendDatabase.child(mCurrentUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(user_id)) {
                                        mCurrent_status = "friends";
                                        mProfileSendRequestBtn.setText("Unfriend this person");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

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

                    DatabaseReference newNotificationRef = mRootDatabase.child("Notifications").child(user_id).push();
                    String newNotificationId = newNotificationRef.getKey();

                    HashMap<String, String> notificationData = new HashMap<>();
                    notificationData.put("from", mCurrentUid);
                    notificationData.put("type", "request");

                    Map requestMap = new HashMap<>();
                    requestMap.put("FriendRequest/" + mCurrentUid + "/" + user_id + "/request_type", "sent");
                    requestMap.put("FriendRequest/" + user_id + "/" + mCurrentUid + "/request_type", "received");
                    requestMap.put("Notifications/" + user_id + "/" + newNotificationId, notificationData);


                    mRootDatabase.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if(databaseError != null) {
                                        Toast.makeText(ProfileActivity.this, "There was some error in sending request", Toast.LENGTH_LONG).show();
                                    }

                                    mCurrent_status = "req_sent";
                                    mProfileSendRequestBtn.setText("CANCEL FRIEND REQUEST");
                                    mProfileSendRequestBtn.setEnabled(true);

                                }
                            }
                    );


                    /*
                    mFriendRequestDatabase.child(mCurrentUid).child(user_id).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                mFriendRequestDatabase.child(user_id).child(mCurrentUid).child("request_type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        HashMap<String, String> notificationData = new HashMap<>();
                                        notificationData.put("from", mCurrentUid);
                                        notificationData.put("type", "request");

                                        mNotificationDatabase.child(user_id).push().setValue(notificationData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    mCurrent_status = "req_sent";
                                                    mProfileSendRequestBtn.setText("CANCEL FRIEND REQUEST");
                                                } else {
                                                }
                                            }
                                        });

                                    }
                                });

                            } else {
                                Toast.makeText(ProfileActivity.this, "Sent Request failed!", Toast.LENGTH_LONG).show();
                            }

                            mProfileSendRequestBtn.setEnabled(true);

                        }
                    }); */
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
                                }
                            });
                        }
                    });
                }

                // ------- REQUEST RECEIVED -------
                if(mCurrent_status.equals("req_received")) {

                    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());
                    //final String currentDate = DateFormat.getDateTimeInstance().format(ServerValue.TIMESTAMP); // check how to use it later

                    DatabaseReference newNotificationRef = mRootDatabase.child("Notifications").child(user_id).push();
                    String newNotificationId = newNotificationRef.getKey();

                    HashMap<String, String> notificationData = new HashMap<>();
                    notificationData.put("from", mCurrentUid);
                    notificationData.put("type", "request");

                    Map friendsMap = new HashMap<>();
                    friendsMap.put("Friends/" + mCurrentUid + "/" + user_id + "/date", currentDate);
                    friendsMap.put("Friends/" + user_id + "/" + mCurrentUid + "/date", currentDate);

                    friendsMap.put("FriendRequest/" + mCurrentUid + "/" + user_id + "/request_type", null);
                    friendsMap.put("FriendRequest/" + user_id + "/" + mCurrentUid + "/request_type", null);



                    mRootDatabase.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if(databaseError != null) {
                                        Toast.makeText(ProfileActivity.this, "There was some error in sending request", Toast.LENGTH_LONG).show();
                                    } else {

                                        mCurrent_status = "friends";
                                        mProfileSendRequestBtn.setText("UNFRIEND THIS PERSON");

                                        mProfileDeclineBtn.setVisibility(View.INVISIBLE);
                                        mProfileDeclineBtn.setEnabled(false);
                                    }

                                    mProfileSendRequestBtn.setEnabled(true);
                                }
                            }
                    );
                }

                // ------- FRIENDS -------
                if(mCurrent_status.equals("friends")) {

                    Map unfriendsMap = new HashMap<>();
                    unfriendsMap.put("Friends/" + mCurrentUid + "/" + user_id, null);
                    unfriendsMap.put("Friends/" + user_id + "/" + mCurrentUid, null);

                    mRootDatabase.updateChildren(unfriendsMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if(databaseError != null) {
                                        Toast.makeText(ProfileActivity.this, "There was some error in sending request", Toast.LENGTH_LONG).show();
                                    } else {
                                        mCurrent_status = "not_friends";
                                        mProfileSendRequestBtn.setText("SEND REQUEST");

                                        mProfileDeclineBtn.setVisibility(View.INVISIBLE);
                                        mProfileDeclineBtn.setEnabled(false);
                                    }

                                    mProfileSendRequestBtn.setEnabled(true);
                                }
                            }
                    );
                }

            }
        });

        mProfileDeclineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrent_status.equals("req_received") ) {
                    mProfileDeclineBtn.setEnabled(false);
                    Map declineMap = new HashMap<>();
                    declineMap.put("FriendRequest/" + mCurrentUid + "/" + user_id, null);
                    declineMap.put("FriendRequest/" + user_id + "/" + mCurrentUid, null);

                    mRootDatabase.updateChildren(declineMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        Toast.makeText(ProfileActivity.this, "There was some error in sending request", Toast.LENGTH_LONG).show();
                                        mProfileDeclineBtn.setEnabled(true);
                                    } else {
                                        mCurrent_status = "not_friends";
                                        mProfileSendRequestBtn.setText("SEND REQUEST");

                                        mProfileDeclineBtn.setVisibility(View.INVISIBLE);
                                        mProfileDeclineBtn.setEnabled(false);
                                    }
                                }
                            }
                    );
                }
            }
        });


    }
}

