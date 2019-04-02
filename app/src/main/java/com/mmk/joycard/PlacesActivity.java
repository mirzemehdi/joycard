package com.mmk.joycard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmk.joycard.Data.PlacesAdapter;
import com.mmk.joycard.Model.Place;
import com.mmk.joycard.Model.User;

import java.util.ArrayList;
import java.util.List;

public class PlacesActivity extends AppCompatActivity {

    private PlacesAdapter placesAdapter;
    private RecyclerView placesRecylerView;
    private List<Place> placeList;
    private FloatingActionButton userButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference currentUserDatabase;
    private TextView userDisplayNameTextView,userBalanceTextView;
    private View cardView;
    private String userDisplayName,userBalance;
    private android.support.v7.widget.Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_places);
        setup();
        CollapsingToolbarLayout a;

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PlacesActivity.this,UserSettings.class);
                startActivity(intent);

            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // If collapsed, then do this
                    toolbar.setVisibility(View.VISIBLE);

                } else if (verticalOffset == 0) {
                    // If expanded, then do this
                    toolbar.setVisibility(View.INVISIBLE);


                } else {
                    // Somewhere in between
                    // Do according to your requirement
                }
            }
        });

        currentUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // Log.d("CurrentUser",dataSnapshot.getValue().toString());

                    User currentUserData = dataSnapshot.getValue(User.class);
                    if (currentUserData!=null) {
                        userDisplayName = currentUserData.getDisplayName();

                        userBalance = currentUserData.getBalance();
                        userBalanceTextView.setText("Balans: " + userBalance + " AZN");
                        userDisplayNameTextView.setText(userDisplayName);
                        progressDialog.dismiss();
                    }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){

            case R.id.signOutButton:
                Intent intent=new Intent(PlacesActivity.this,UserSettings.class);
                startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    public void setup(){

        toolbar=(android.support.v7.widget.Toolbar) findViewById(R.id.toolbarPlaces);
        setSupportActionBar(toolbar);
        appBarLayout=(AppBarLayout)findViewById(R.id.appbarPlaces);
        progressDialog=new ProgressDialog(PlacesActivity.this);
        progressDialog.setTitle("Wait a sec");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        mDatabase=FirebaseDatabase.getInstance();
        Log.d("CurrentUserId",currentUser.getUid());
        currentUserDatabase=mDatabase.getReference("Users").child(currentUser.getUid());

        placesRecylerView=(RecyclerView)findViewById(R.id.placesRecylerView);
        userButton=(FloatingActionButton) findViewById(R.id.profileFloatingButton);
        cardView=(View)findViewById(R.id.cardViewPlaces);

        userBalanceTextView=cardView.findViewById(R.id.cardBalanceTextView);
        userDisplayNameTextView=cardView.findViewById(R.id.cardDisplayNameTextView);

        placesRecylerView.setHasFixedSize(true);
        placesRecylerView.setLayoutManager(new LinearLayoutManager(this));
        placesRecylerView.setAdapter(placesAdapter);
        placeList=new ArrayList<>();
        addPlaceToList();
        placesAdapter=new PlacesAdapter(this,placeList);

        placesRecylerView.setAdapter(placesAdapter);


    }

    public void addPlaceToList() {

        Place place=new Place(String.valueOf(R.drawable.card),"Megafun");
        placeList.add(place);
         place=new Place(String.valueOf(R.drawable.card),"Panda");
        placeList.add(place);
         place=new Place(String.valueOf(R.drawable.card),"Baki Əyləncə mərkəzi");
        placeList.add(place);

        place=new Place(String.valueOf(R.drawable.card),"Tom and Jerry");
        placeList.add(place);
        place=new Place(String.valueOf(R.drawable.card),"Bravo");
        placeList.add(place);
        place=new Place(String.valueOf(R.drawable.card),"Araz market");
        placeList.add(place);
        place=new Place(String.valueOf(R.drawable.card),"Azza");
        placeList.add(place);
        place=new Place(String.valueOf(R.drawable.card),"Nizami Cinema");
        placeList.add(place);
        place=new Place(String.valueOf(R.drawable.card),"Park Cinema");
        placeList.add(place);
        place=new Place(String.valueOf(R.drawable.card),"Koala Park");
        placeList.add(place);
        place=new Place(String.valueOf(R.drawable.card),"Zara");
        placeList.add(place);
        place=new Place(String.valueOf(R.drawable.card),"Place");
        placeList.add(place);
        place=new Place(String.valueOf(R.drawable.card),"Place");
        placeList.add(place);


    }
}
