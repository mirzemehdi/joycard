package com.mmk.joycard.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.mmk.joycard.CardScanActivity;
import com.mmk.joycard.PlacesActivity;
import com.mmk.joycard.R;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Login extends Fragment {

    private TextInputEditText username,password;
    private TextView register;
    private  Button loginButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ViewPager mViewPager;
    private ProgressDialog progressDialog;





    @Override
    public void onStart() {
        super.onStart();




       /* currentUser=mAuth.getCurrentUser();
        if (currentUser!=null) {


            Toast.makeText(getContext(), "Goes the next activity...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), PlacesActivity.class);
            startActivity(intent);
            getActivity().finish();

        }*/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login,container,false);



        mAuth= FirebaseAuth.getInstance();
        username=(TextInputEditText) view.findViewById(R.id.usernameLogin);
        password=(TextInputEditText) view.findViewById(R.id.passwordLogin);
        loginButton=(Button)view.findViewById(R.id.login);
        register=(TextView)view.findViewById(R.id.passregister);
        mViewPager=(ViewPager)getActivity().findViewById(R.id.container);





        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!username.getText().toString().isEmpty()&&!password.getText().toString().isEmpty()){

                if (isEmailValid(username.getText().toString())) {

                    signIn(username.getText().toString(), password.getText().toString());

                } else {

                    username.setError("It is not correct email format");
                }

            }

            else {

                    Toast.makeText(getContext(),"Fields can't be empty",Toast.LENGTH_SHORT).show();

                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });





        return view;

    }







    public boolean isEmailValid(String mail){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches();
    }

    public void signIn(String username,String pass){

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Login..");
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(username,pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Toast.makeText(getActivity(),"Login is success...", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), PlacesActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else{
                    progressDialog.dismiss();

                    Toast.makeText(getActivity(),"Login is failed...", Toast.LENGTH_SHORT).show();

                }


            }

        });




    }
}
