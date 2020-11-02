package com.nabase1.simplechatapp.util;

import android.app.Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {

    private static final int My_Code = 1403;
    public static FirebaseDatabase firebaseDatabase;
    public  static DatabaseReference databaseReference;
    private static FirebaseUtils firebaseUtils;
    private  static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;
    private static Activity caller;

    private FirebaseUtils(){}


    public static void openFirebaseUtils(String ref, Activity callerActivity){
        if(firebaseUtils == null){
            firebaseUtils = new FirebaseUtils();
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();

            caller = callerActivity;
        }
        databaseReference = firebaseDatabase.getReference().child(ref);
    }
}
