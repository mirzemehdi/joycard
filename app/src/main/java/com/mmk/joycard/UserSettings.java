package com.mmk.joycard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mmk.joycard.Constants.UserData;
import com.mmk.joycard.Model.User;

public class UserSettings extends AppCompatActivity {

    private ImageView editDisplayNameButton,editPasswordButton;
    private Button deleteUserButton,signOutButton;
    private TextInputEditText displayNameEditText;
    private TextInputLayout displayNameEditLayout;
    private InputMethodManager imm;
    private View view;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private UserData userData;
    private AlertDialog.Builder passwordChangeAlertDialogBuiler,userDeleteAlertDialogBuilder;
    private AlertDialog passwordChangeAlertDialog,userDeleteAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);


        setup();



        editDisplayNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDisplayName();
            }
        });

        editPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPassword();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDeleteAlertDialog.show();
            }
        });

    }

    public void signOut() {
        mAuth.signOut();
        Intent intent=new Intent(UserSettings.this,EntranceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void setup(){


        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        userRef= FirebaseDatabase.getInstance().getReference("Users");
        mAuth=FirebaseAuth.getInstance();
        signOutButton=(Button)findViewById(R.id.signOutButtonSettings);
        editDisplayNameButton=(ImageView)findViewById(R.id.editDisplayNameIconSettings);
        editPasswordButton=(ImageView)findViewById(R.id.editPasswordIconSettings);
        deleteUserButton=(Button)findViewById(R.id.deleteUserSettings);
        displayNameEditText=(TextInputEditText)findViewById(R.id.displayNameSettings);
        displayNameEditLayout=(TextInputLayout)findViewById(R.id.displayNameLayoutSettings);
        userData=new UserData();
        userData.getCurrentUserData(new UserData.onGetData() {
            @Override
            public void onSuccess(User currentUser) {
                if (currentUser!=null)
                displayNameEditText.setText(currentUser.getDisplayName());
            }
        });




        userDeleteAlertDialogBuilder=new AlertDialog.Builder(this);
        userDeleteAlertDialogBuilder.setTitle("Delete Account");
        userDeleteAlertDialogBuilder.setMessage("Are you sure to delete this account?");
        userDeleteAlertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUser();

            }
        });

        userDeleteAlertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userDeleteAlertDialog.dismiss();
            }
        });

        userDeleteAlertDialog=userDeleteAlertDialogBuilder.create();


    }


    public void editDisplayName(){

        showKeyboard(displayNameEditText);
        displayNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_DONE) {



                     String displayName=displayNameEditText.getEditableText().toString();
                     if (displayName.isEmpty())
                         displayNameEditText.setError("DisplayName can't be empty");
                     else {
                         hideKeyboard();
                         displayNameEditText.setEnabled(false);
                         updateDisplayName(displayName);
                     }


                }
                return true;
            }
        });

    }
    public void editPassword(){

        passwordChangeAlertDialogBuiler=new AlertDialog.Builder(this);
        View editPassView=getLayoutInflater().inflate(R.layout.password_change,null);
        passwordChangeAlertDialogBuiler.setView(editPassView);
        passwordChangeAlertDialogBuiler.setTitle("Change Password");
        passwordChangeAlertDialog=passwordChangeAlertDialogBuiler.create();
        passwordChangeAlertDialog.show();

        final TextInputEditText currentPassTextView,newPassTextView,confirmNewPassTextView;
        currentPassTextView=(TextInputEditText) editPassView.findViewById(R.id.currentPasswordEditText);
        newPassTextView=(TextInputEditText) editPassView.findViewById(R.id.newPasswordEditText);
        confirmNewPassTextView=(TextInputEditText) editPassView.findViewById(R.id.repeatNewPasswordEditText);


        Button changePassButton=(Button)editPassView.findViewById(R.id.changePasswordButton);
        changePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 final String currentPass,newPass,confirmNewPass;
                 currentPass=currentPassTextView.getText().toString();
                 newPass=newPassTextView.getText().toString();
                 confirmNewPass=confirmNewPassTextView.getText().toString();
                Log.d("Passes",currentPass);
                 userData=new UserData();
                 userData.getCurrentUserData(new UserData.onGetData() {
                     @Override
                     public void onSuccess(User currentUser) {

                         if (currentUser!=null){
                             Log.d("Passes","Current User");
                             AuthCredential authCredential=EmailAuthProvider.getCredential(currentUser.getUsername(),currentPass);
                             mAuth.getCurrentUser().reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if (task.isSuccessful()){

                                         if (newPass.equals(confirmNewPass)){

                                             mAuth.getCurrentUser().updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                 @Override
                                                 public void onSuccess(Void aVoid) {
                                                     userRef.child(mAuth.getUid()).child("password").setValue(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                         @Override
                                                         public void onSuccess(Void aVoid) {
                                                             passwordChangeAlertDialog.dismiss();
                                                             Toast.makeText(UserSettings.this,"Password is changed",Toast.LENGTH_SHORT).show();
                                                         }
                                                     });
                                                 }
                                             });


                                         }

                                         else{


                                             confirmNewPassTextView.setError("Passwords doesn't match");
                                         }


                                     }
                                     else{
                                         Log.d("Passes","Password is incorrect");
                                         currentPassTextView.setError("Paasword is incorrect");
                                     }
                                 }
                             });

                         }
                     }
                 });


            }
        });





    }
    public void showKeyboard(EditText editText){

        editText.setEnabled(true);
        editText.requestFocus();
        editText.setFocusableInTouchMode(true);

        editText.setSelection(displayNameEditText.getText().length());
        imm.showSoftInput(displayNameEditText, InputMethodManager.SHOW_FORCED);

    }
    public void hideKeyboard(){
        view=UserSettings.this.getCurrentFocus();
        if (view!=null) {

            imm  .hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }
    public void updateDisplayName(String displayName){


        String userId=mAuth.getCurrentUser().getUid();
        userRef.child(userId).child("displayName").setValue(displayName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UserSettings.this,"DisplayName is changed",Toast.LENGTH_SHORT).show();
            }
        });



    }
    public void deleteUser(){
        final ProgressDialog dialog=new ProgressDialog(UserSettings.this);
        dialog.setTitle("Delete User");
        dialog.setMessage("User is deleting...");
        dialog.show();
        final String id=mAuth.getUid();
        UserData userData=new UserData();
        userData.getCurrentUserData(new UserData.onGetData() {
            @Override
            public void onSuccess(User currentUser) {
                AuthCredential authCredential;
                if (currentUser != null){
                    authCredential = EmailAuthProvider.getCredential(currentUser.getUsername(), currentUser.getPassword());
                    mAuth.getCurrentUser().reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            userRef.child(mAuth.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mAuth.getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(UserSettings.this,"User is removed",Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(UserSettings.this,EntranceActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            dialog.dismiss();
                                            startActivity(intent);
                                        }
                                    });
                                }
                            });
                        }
                    });

                }
            }
        });


    }




}

