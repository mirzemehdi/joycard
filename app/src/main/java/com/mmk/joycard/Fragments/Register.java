package com.mmk.joycard.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mmk.joycard.CardScanActivity;
import com.mmk.joycard.Model.User;
import com.mmk.joycard.PlacesActivity;
import com.mmk.joycard.R;

import java.util.HashMap;

public class Register extends Fragment {


    private TextInputEditText username,password,displayName;
    private Button registerBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_register,container,false);
        mAuth= FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference("Users");
        username=(TextInputEditText) view.findViewById(R.id.usernameRegister);
        password=(TextInputEditText) view.findViewById(R.id.passwordRegister);
        displayName=(TextInputEditText) view.findViewById(R.id.displayNameRegister);
        registerBtn=(Button)view.findViewById(R.id.register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!username.getText().toString().isEmpty()){

                    progressDialog=new ProgressDialog(getContext());
                    progressDialog.setTitle("Register..");
                    progressDialog.setMessage("Please wait....");
                    progressDialog.setCancelable(false);
                    progressDialog.show();



                    mAuth.createUserWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                writeToDatabase(mAuth.getUid(), displayName.getText().toString(),username.getText().toString(),password.getText().toString());
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), PlacesActivity.class);

                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();


                            }
                        }
                    });

            }
            }
        });

        return view;
    }


    public void writeToDatabase(String id,String displayName,String username,String password){
        User newUser=new User();
        newUser.setUsername(username);
        newUser.setBalance("0");
        newUser.setDisplayName(displayName);
        newUser.setPassword(password);
        newUser.setUserId(id);


        myRef.child(id).setValue(newUser).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(getActivity(),"Account is Created",Toast.LENGTH_SHORT).show();

                }



            }
        });

    }

}
