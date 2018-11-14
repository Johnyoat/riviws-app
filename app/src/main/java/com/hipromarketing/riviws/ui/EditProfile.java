package com.hipromarketing.riviws.ui;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.esafirm.imagepicker.features.ImagePicker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.models.User;
import com.hipromarketing.riviws.utils.TextUtility;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;


public class EditProfile extends DialogFragment {
    AppCompatImageButton close;
    AppCompatButton save;
    AppCompatImageButton changeProfile;
    TextInputLayout name;
    TextInputLayout email;
    TextInputLayout occupation;
    TextInputLayout location;
    TextInputLayout telephone;
    CircleImageView profile;
    AppCompatImageView background;
    User user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextUtility utility;
    String img_url = "";
//    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference profileImg = FirebaseStorage.getInstance().getReference();
    private ProgressDialog dialog;


    public static EditProfile newInstance(User user) {

        Bundle args = new Bundle();

        EditProfile fragment = new EditProfile();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    public EditProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.eidt_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        close = view.findViewById(R.id.close);
        save = view.findViewById(R.id.save);
        changeProfile = view.findViewById(R.id.changeProfile);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        occupation = view.findViewById(R.id.occupation);
        location = view.findViewById(R.id.location);
        telephone = view.findViewById(R.id.telephone);
        profile = view.findViewById(R.id.profile);
        background = view.findViewById(R.id.background);

        utility = TextUtility.getInstance();
//        profileImg = storage.getReference();



        dialog = new ProgressDialog(getContext());

        if (getArguments() != null) {
            user = getArguments().getParcelable("user");
            name.getEditText().setText(user.getUserName());
            email.getEditText().setText(user.getEmail());
            telephone.getEditText().setText(user.getPhoneNumber());
            location.getEditText().setText(user.getLocation());
            occupation.getEditText().setText(user.getOccupation());
            updateUserProfilePicture(user.getProfilePhotoUrl());
        }


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAllowingStateLoss();
            }
        });


        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.create(EditProfile.this)
                        .folderMode(true)// Activity or Fragment
                        .single()
                        .start();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = utility.fieldToString(name);
                String newEmail = utility.fieldToString(email);
                String newOccupation = utility.fieldToString(occupation);
                String newTel = utility.fieldToString(telephone);
                String newLocation = utility.fieldToString(location);

                if (!newEmail.isEmpty() && !newName.isEmpty() ) {
                    user.setUserName(newName);
                    user.setEmail(newEmail);
                    user.setPhoneNumber(newTel);
                    user.setLocation(newLocation);
                    user.setOccupation(newOccupation);


                    if (!img_url.isEmpty()) {
                        File file = new File(img_url);

                        Uri fileUri = Uri.fromFile(file);

                        UUID uuid = UUID.randomUUID();
                        System.out.println(uuid.toString()+"."+ FilenameUtils.getExtension(file.getAbsolutePath()));

                        final StorageReference getImageRef = profileImg.child("profiles/" + user.getUid()+"/"+ FilenameUtils.getExtension(file.getAbsolutePath()));



                                getImageRef.putFile(fileUri)
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                        dialog.setProgress((int) progress);
                                        dialog.setMessage("Updating profile "+(int) progress+"%");
                                        dialog.setCancelable(false);
                                        dialog.show();
                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        getImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                user.setProfilePhotoUrl(uri.toString());
                                                updateBasicProfile(user);
                                                dialog.dismiss();
                                            }
                                        });
                                    }
                                });

                    } else {
                        updateBasicProfile(user);
                    }
                }
            }
        });
    }

    private void updateBasicProfile(User user) {
        UserProfileChangeRequest newUserInfo = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.getUserName())
                .setPhotoUri(Uri.parse(user.getProfilePhotoUrl()))
                .build();
        FirebaseAuth.getInstance().getCurrentUser().updateProfile(newUserInfo);

        Map<String, Object> newUser = new ObjectMapper().convertValue(user, Map.class);


        db.collection("users").document(user.getUid()).update(newUser);
        Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
    }

    private void updateUserProfilePicture(String profileUrl) {
        if (profile != null && !profileUrl.isEmpty() && getContext() != null) {
            Glide.with(getContext()).load(profileUrl).into(profile);

            Glide.with(getContext())
                    .load(profileUrl)
                    .apply(RequestOptions.signatureOf(new BlurTransformation(25, 3)).centerCrop())
//                    .thumbnail(0.1f)
                    .apply(new RequestOptions().centerCrop())
                    .into(background);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            img_url = ImagePicker.getFirstImageOrNull(data).getPath();
            if (!img_url.isEmpty()) {
                updateUserProfilePicture(img_url);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
