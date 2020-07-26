 package com.cs230.assignment3;

 import android.content.Intent;
 import android.os.Bundle;
 import android.util.Log;
 import android.view.Menu;
 import android.view.MenuItem;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.Toast;

 import androidx.appcompat.app.AppCompatActivity;
 import androidx.lifecycle.ViewModelProviders;
 import androidx.recyclerview.widget.LinearLayoutManager;
 import androidx.recyclerview.widget.RecyclerView;

 import com.cs230.assignment3.adapter.ContactAdapter;
 import com.cs230.assignment3.adapter.ContactAdapter.OnContactSelectedListener;
 import com.cs230.assignment3.model.Contact;
 import com.cs230.assignment3.viewmodel.MainActivityViewModel;
 import com.firebase.ui.auth.AuthUI;
 import com.google.android.material.snackbar.Snackbar;
 import com.google.firebase.auth.FirebaseAuth;

 import com.google.firebase.firestore.CollectionReference;
 import com.google.firebase.firestore.DocumentSnapshot;
 import com.google.firebase.firestore.FirebaseFirestore;
 import com.google.firebase.firestore.FirebaseFirestoreException;
 import com.google.firebase.firestore.Query;

 import java.util.ArrayList;
 import java.util.Collections;

 import static com.google.firebase.auth.FirebaseAuth.getInstance;

 public class MainActivity extends AppCompatActivity implements
         View.OnClickListener,
         OnContactSelectedListener {

     private static final String TAG = "MainActivity";

     private static final int RC_SIGN_IN = 9001;

     private static final int LIMIT = 50;

     private RecyclerView mContactRecycler;
     private ViewGroup mEmptyView;
     private FirebaseFirestore mFirestore;
     private Query mQuery;
     private ContactAdapter mAdapter;

     private MainActivityViewModel mViewModel;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);


         mContactRecycler = findViewById(R.id.recycler_contact);
         mEmptyView = findViewById(R.id.view_empty);

         // View model
         mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

         // Enable Firestore logging
         FirebaseFirestore.setLoggingEnabled(true);

         // Initialize Firestore and the main RecyclerView
         initFirestore();
         initRecyclerView();

     }

     private void initFirestore() {
         mFirestore = FirebaseFirestore.getInstance();


         // Get the users
         mQuery = mFirestore.collection("users");
     }

     private void initRecyclerView() {
         if (mQuery == null) {
             Log.w(TAG, "not initializing RecyclerView");
         }

         mAdapter = new ContactAdapter(mQuery, this) {

             @Override
             protected void onDataChanged() {
                 // Show/hide content if the query returns empty.
                 if (getItemCount() == 0) {
                     mContactRecycler.setVisibility(View.GONE);
                     mEmptyView.setVisibility(View.VISIBLE);
                 } else {
                     mContactRecycler.setVisibility(View.VISIBLE);
                     mEmptyView.setVisibility(View.GONE);
                 }
             }

             @Override
             protected void onError(FirebaseFirestoreException e) {
                 // Show a snackbar on errors
                 Snackbar.make(findViewById(android.R.id.content),
                         "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
             }
         };

         mContactRecycler.setLayoutManager(new LinearLayoutManager(this));
         mContactRecycler.setAdapter(mAdapter);
     }

     @Override
     public void onStart() {
         super.onStart();

         // Start sign in if necessary
         if (shouldStartSignIn()) {
             startSignIn();

             return;
         }


         // Start listening for Firestore updates
         if (mAdapter != null) {
             mAdapter.startListening();
         }
     }

     @Override
     public void onStop() {
         super.onStop();
         if (mAdapter != null) {
             mAdapter.stopListening();
         }
     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu_main, menu);
         return super.onCreateOptionsMenu(menu);
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
             case R.id.menu_sign_out:
                 AuthUI.getInstance().signOut(this);
                 startSignIn();
                 break;
         }
         return super.onOptionsItemSelected(item);
     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == RC_SIGN_IN) {
             mViewModel.setIsSigningIn(false);

             if (resultCode != RESULT_OK && shouldStartSignIn()) {
                 startSignIn();
             }

             String current_uname = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
             String current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
             CollectionReference contacts = mFirestore.collection("users");
             Contact new_user = new Contact(current_uname);
             contacts.document(current_uid).set(new_user);

         }
     }



     @Override
     public void onContactSelected(DocumentSnapshot contact) {
         // Go to the details page for the selected restaurant
         Intent intent = new Intent(this, ChatScreen.class);
         intent.putExtra("userid", contact.getId());

         startActivity(intent);
     }

     private boolean shouldStartSignIn() {
         return (!mViewModel.getIsSigningIn() && getInstance().getCurrentUser() == null);
     }

     private void startSignIn() {
         // Sign in with FirebaseUI
         Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                 .setAvailableProviders(Collections.singletonList(
                         new AuthUI.IdpConfig.EmailBuilder().build()))
                 .setIsSmartLockEnabled(false)
                 .build();

         startActivityForResult(intent, RC_SIGN_IN);

         mViewModel.setIsSigningIn(true);


     }

     private void showTodoToast() {
         Toast.makeText(this, "TODO: Implement", Toast.LENGTH_SHORT).show();
     }

     @Override
     public void onClick(View view) {

     }
 }
