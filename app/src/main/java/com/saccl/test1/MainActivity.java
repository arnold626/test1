package com.saccl.test1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewParent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Toolbar mToolbar;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;

    private DatabaseReference mUserDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Testing App");

        // Tabs
        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                    mUserDatabase.child("online").setValue(true);


                } else {
                    // User is signed out
                    sendToStart();
                }
            }
        };
    }

    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class );
        startActivity(startIntent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
            mUserDatabase.child("online").setValue(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()) {
            case R.id.main_logout_btn:
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null) {
                    mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
                    mUserDatabase.child("online").setValue(false);
                }

                FirebaseAuth.getInstance().signOut();
                sendToStart();
                break;
            case R.id.main_settings_btn:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class );
                startActivity(settingsIntent);
                // finish();
                break;
            case R.id.main_all_users_btn:
                Intent UsersIntent = new Intent(MainActivity.this, UsersActivity.class );
                startActivity(UsersIntent);
                // finish();
                break;
            case R.id.mainMenu_demo_btn:
                Intent DemoIntent = new Intent(MainActivity.this, DemoActivity.class );
                startActivity(DemoIntent);
                // finish();
                break;
            case R.id.mainMenu_fitnessUsers_btn:
                Intent FitnessUsersIntent = new Intent(MainActivity.this, UsersActivity.class );
                startActivity(FitnessUsersIntent);
                // finish();
                break;

        }

        return true;

    }

}
