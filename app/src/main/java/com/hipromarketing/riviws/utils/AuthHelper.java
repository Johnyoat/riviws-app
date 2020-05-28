package com.hipromarketing.riviws.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.models.User;
import com.hipromarketing.riviws.ui.BaseHome;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

public class AuthHelper {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private AppCompatActivity activity;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LogIn Activity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog dialog;
    private CallbackManager callbackManager;
    private Context context;
    private User userBackUp;

    public static AuthHelper getInstance(Activity activity) {
        return new AuthHelper(activity);
    }


    private AuthHelper(Activity activity) {
        this.activity = (AppCompatActivity) activity;
        this.dialog = new ProgressDialog(this.activity);
        this.context = activity.getApplicationContext();
    }


    public void signUp(final String name, final String email, final String password) {
        showDialog("Please wait...", false);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "Verification email sent to " + auth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "sendEmailVerification", e);
                                    Toast.makeText(context, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                }
                            });

                            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .setPhotoUri(Uri.parse("https://firebasestorage.googleapis.com/v0/b/riviwsapp.appspot.com/o/otherimages%2Fuser-2.png?alt=media&token=f3265050-1601-402d-9018-b2b1a1e31415"))
                                    .build();
                            user.updateProfile(userProfileChangeRequest);
                            User editedUser = new User(user);
                            updateUser(editedUser);
                        }
                    }
                });
    }


    public void googleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(context, gso);
        Intent googleSignInIntent = googleSignInClient.getSignInIntent();
        activity.startActivityForResult(googleSignInIntent, RC_SIGN_IN);
        activity.setResult(RC_SIGN_IN);
    }


    public void facebookSignIn(LoginButton fbBtn) {

        callbackManager = CallbackManager.Factory.create();

        fbBtn.performClick();

        fbBtn.setReadPermissions("email", "public_profile");

        fbBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {


                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            facebookSignIn(loginResult.getAccessToken().getToken(), object.getString("email"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                showDialog(error.getMessage(), true);
            }
        });
    }


    public void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseSignCredential(authCredential, account.getEmail());
    }


    public void signInWithEmailAndPassword(String email, String password) {

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        firebaseSignCredential(credential, email);
    }

    private void facebookSignIn(String fbToken, String email) {

        AuthCredential credential = FacebookAuthProvider.getCredential(fbToken);
        firebaseSignCredential(credential, email);

    }


    private void firebaseSignCredential(AuthCredential authCredential, final String email) {
        showDialog("Please wait...", false);
        auth.signInWithCredential(authCredential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            User user = new User(Objects.requireNonNull(auth.getCurrentUser()));

                            user.setEmail(email);

                            updateUser(user);
                        } else {
                            dismissDialog();
                            Snackbar.make(activity.getWindow().getDecorView().getRootView(), "Authentication Failed.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void showDialog(String s, boolean b) {
        dialog.setMessage(s);
        dialog.setCancelable(b);
        dialog.show();
    }

    @SuppressWarnings("all")
    private void saveUserInfo(User user) {
        db.collection("users").document(user.getUid()).set(new ObjectMapper().convertValue(user, Map.class))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        success();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }


    @SuppressWarnings("all")
    private void updateUser(User user) {
        if (user.getUserName() == null) {
            userBackUp = new User(FirebaseAuth.getInstance().getCurrentUser());
        } else {
            userBackUp = user;
        }
        DocumentReference userDoc = db.collection("users").document(user.getUid());

        userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()) {
                    saveUserInfo(userBackUp);
                } else {
                    success();
                }
            }
        });
    }


    private void success() {
        if (activity != null) {
            dismissDialog();
            activity.startActivity(new Intent(activity, BaseHome.class));
        }
    }

    private void dismissDialog() {
//        if (activity.isDestroyed()){
////            if (dialog != null && dialog.isShowing()) {
////                dialog.dismiss();
////            }
//        }
    }


    public CallbackManager getCallbackManager() {
        return callbackManager;
    }


    public static int getRcSignIn() {
        return RC_SIGN_IN;
    }


    public void setContext(Context context) {
        this.context = context;
    }
}
