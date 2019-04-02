package com.mmk.joycard.Constants;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmk.joycard.Model.User;

public class UserData {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference currentUserDatabase;
    private User currentUserData;
    private onGetData onGetData;


    public UserData() {

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        mDatabase=FirebaseDatabase.getInstance();
        currentUserDatabase=mDatabase.getReference("Users").child(mAuth.getUid());

    }

    public  void getCurrentUserData(final onGetData onGetData){
        this.onGetData=onGetData;


        currentUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                currentUserData= dataSnapshot.getValue(User.class);
                onGetData.onSuccess(currentUserData);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public interface onGetData{

         void onSuccess(User currentUser);
    }


}
