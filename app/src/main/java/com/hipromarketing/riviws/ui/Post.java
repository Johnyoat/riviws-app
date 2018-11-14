package com.hipromarketing.riviws.ui;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.models.Category;
import com.hipromarketing.riviws.models.Trend;
import com.hipromarketing.riviws.utils.UICreator;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static com.hipromarketing.riviws.constants.Constants.AUTO_MOBILE;
import static com.hipromarketing.riviws.constants.Constants.BARS;
import static com.hipromarketing.riviws.constants.Constants.BEAUTY_AND_SPA;
import static com.hipromarketing.riviws.constants.Constants.COMPANY;
import static com.hipromarketing.riviws.constants.Constants.COSMETICS;
import static com.hipromarketing.riviws.constants.Constants.DEFAULT;
import static com.hipromarketing.riviws.constants.Constants.EDUCATION;
import static com.hipromarketing.riviws.constants.Constants.ENTERTAINMENT;
import static com.hipromarketing.riviws.constants.Constants.FASHION;
import static com.hipromarketing.riviws.constants.Constants.FINANCIAL_SERVICE;
import static com.hipromarketing.riviws.constants.Constants.FOOD_AND_DRINK;
import static com.hipromarketing.riviws.constants.Constants.HEALTH;
import static com.hipromarketing.riviws.constants.Constants.HEALTH_AND_MEDICAL;
import static com.hipromarketing.riviws.constants.Constants.HOME_AND_SERVICES;
import static com.hipromarketing.riviws.constants.Constants.HOTELS;
import static com.hipromarketing.riviws.constants.Constants.LOCAL_JOINT;
import static com.hipromarketing.riviws.constants.Constants.LOCAL_SERVICES;
import static com.hipromarketing.riviws.constants.Constants.MASS_MEDIA;
import static com.hipromarketing.riviws.constants.Constants.NETWORK;
import static com.hipromarketing.riviws.constants.Constants.NIGHT_LIFE;
import static com.hipromarketing.riviws.constants.Constants.PLACES;
import static com.hipromarketing.riviws.constants.Constants.PRODUCT;
import static com.hipromarketing.riviws.constants.Constants.PUBLIC_INST;
import static com.hipromarketing.riviws.constants.Constants.REAL_ESTATE;
import static com.hipromarketing.riviws.constants.Constants.RELIGION;
import static com.hipromarketing.riviws.constants.Constants.RESTAURANTS;
import static com.hipromarketing.riviws.constants.Constants.REVIEW_DEFAULT;
import static com.hipromarketing.riviws.constants.Constants.SHOPPING;
import static com.hipromarketing.riviws.constants.Constants.TRAVELS;
import static com.hipromarketing.riviws.constants.Constants.TRIPS;
import static com.hipromarketing.riviws.constants.Constants.getUser;
import static com.hipromarketing.riviws.constants.Constants.setLocation;

/**
 * A simple {@link Fragment} subclass.
 */
public class Post extends Fragment implements GoogleApiClient.OnConnectionFailedListener {


    MaterialSpinner categories;
    MaterialSpinner reviewType;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog dialog;
    String catText = "";
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    String img_url = "";
    AppCompatImageButton gallery;
    AppCompatImageButton camera;
    AppCompatImageButton preview;
    AppCompatButton submit;
    AppCompatEditText comment;
    AppCompatTextView company;
    ScaleRatingBar rating;
    View categoryLayout;
    View companyLayout;
    View commentLayout;
    View reviewLayout;
    AppCompatEditText product;
    BottomNavigationView bnv;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;
    String TAG = "TAG";
    Trend post;

    public static Post newInstance() {

        Bundle args = new Bundle();

        Post fragment = new Post();
        fragment.setArguments(args);
        return fragment;
    }

    public Post() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gallery = view.findViewById(R.id.gallery);
        camera = view.findViewById(R.id.camera);
        preview = view.findViewById(R.id.preview);
        submit = view.findViewById(R.id.submit);
        comment = view.findViewById(R.id.message);
        company = view.findViewById(R.id.company);
        rating = view.findViewById(R.id.rating);
        product = view.findViewById(R.id.product);
        reviewType = view.findViewById(R.id.reviewType);
        reviewLayout = view.findViewById(R.id.reviewLayout);
        categoryLayout = view.findViewById(R.id.categoryLayout);
        companyLayout = view.findViewById(R.id.companyLayout);
        commentLayout = view.findViewById(R.id.commentLayout);

        assert getContext() != null && getActivity() != null;


        if (getActivity() != null) {
            bnv = getActivity().findViewById(R.id.btnNav);
        }

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Sending review");
        dialog.setCancelable(false);


        LinearLayout scan = view.findViewById(R.id.scanQR);

        post = new Trend();

//        company.setKeyListener(null);


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.
                setTitle("Action Required")
                .setMessage(R.string.acc_verification_message)
                .setPositiveButton(R.string.verifiy, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.sendEmailVerification().addOnCompleteListener(getActivity(), new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                // Re-enable button

                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(),
                                            "Verification email sent to " + user.getEmail(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e(TAG, "sendEmailVerification", task.getException());
                                    Toast.makeText(getContext(),
                                            "Failed to send verification email.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        final AlertDialog alertDialog = builder.create();


        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = reviewType.getText().toString();

                switch (key) {
                    case COMPANY:
                        company.setVisibility(View.VISIBLE);
                        product.setVisibility(View.GONE);
                        try {
                            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                                    .setCountry("GH")
                                    .build();

                            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .setFilter(typeFilter)
                                    .build(getActivity());
                            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                            // TODO: Handle the error.
                        }
                        break;
                    case PRODUCT:
                        company.setVisibility(View.GONE);
                        product.setVisibility(View.VISIBLE);
                        product.requestFocus();
                        break;

                }

            }
        });


        categories = view.findViewById(R.id.spinner);

        categories.setItems(DEFAULT, HOTELS, RESTAURANTS, FASHION, HEALTH, NETWORK, TRIPS, EDUCATION, AUTO_MOBILE, TRAVELS, COSMETICS
                , FOOD_AND_DRINK
                , NIGHT_LIFE
                , SHOPPING
                , HEALTH_AND_MEDICAL
                , BEAUTY_AND_SPA
                , HOME_AND_SERVICES
                , LOCAL_JOINT
                , LOCAL_SERVICES
                , ENTERTAINMENT
                , REAL_ESTATE
                , FINANCIAL_SERVICE
                , PUBLIC_INST
                , MASS_MEDIA
                , RELIGION
                , BARS
                , PLACES);

        categories.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                catText = item.toString();
                if (catText.equals(DEFAULT)) {
                    categoryLayout.setBackground(getActivity().getDrawable(R.drawable.error_bg));
                } else {
                    post.getCompany().setCategory(item.toString().toLowerCase());
                    categoryLayout.setBackground(getActivity().getDrawable(R.drawable.search_bg));
                }
            }
        });

        reviewType.setItems(REVIEW_DEFAULT, COMPANY, PRODUCT);

        reviewType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                catText = item.toString();
                if (catText.equals(REVIEW_DEFAULT)) {
                    reviewLayout.setBackground(getActivity().getDrawable(R.drawable.error_bg));
                } else {
                    post.getCompany().setCategory(item.toString().toLowerCase());
                    reviewLayout.setBackground(getActivity().getDrawable(R.drawable.search_bg));
                }
                String key = item.toString();

                switch (key) {
                    case COMPANY:
                        company.setVisibility(View.VISIBLE);
                        product.setVisibility(View.GONE);
                        break;
                    case PRODUCT:
                        company.setVisibility(View.GONE);
                        product.setVisibility(View.VISIBLE);
                        product.requestFocus();
                        break;
                }
            }
        });


//        validateTextWhilesTyping(company, companyLayout);

        validateTextWhilesTyping(comment, commentLayout);

        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (bnv != null) {
                            if (isOpen) {
                                bnv.setVisibility(View.GONE);
                            } else {
                                bnv.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });


        rating.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float v) {
                if (v < 1) {
                    baseRatingBar.setEmptyDrawable(getActivity().getDrawable(R.drawable.error_star));
                } else {
                    baseRatingBar.setEmptyDrawable(getActivity().getDrawable(R.drawable.star_outline));
                }
            }
        });


        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!img_url.isEmpty()) {
                    UICreator.getInstance((AppCompatActivity) getActivity()).createDialog(ImagePreview.newInstance(img_url), "imgPreview");
                }
            }
        });


        FirebaseFirestore userDb = FirebaseFirestore.getInstance();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;

                if (getUser() != null) {
                    if (validateText(comment, commentLayout) & validateRating(rating) & checkCategory(categories, categoryLayout) & checkCategory(reviewType, reviewLayout)) {

                        final DocumentReference ref = db.collection("riviws").document();

                        post.setRating(String.valueOf(rating.getRating()));
                        post.getCompany().setName(company.getText().toString().trim());
                        post.setDate(String.valueOf(System.currentTimeMillis()));
                        post.setComment(comment.getText().toString());
                        post.setId(ref.getId());
                        post.setUser(getUser());

                        String key = reviewType.getText().toString();

                        switch (key) {
                            case COMPANY:
                                post.getCompany().setName(company.getText().toString().trim());
                                break;
                            case PRODUCT:
                                post.getCompany().setName(product.getText().toString().trim());
                                break;
                        }


                        if (!img_url.isEmpty()) {
                            Uri file = Uri.fromFile(new File(img_url));

                            final StorageReference images = storageReference.child("images/" + file.getLastPathSegment());
                            images.putFile(file).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    dialog.setProgress((int) progress);
                                    dialog.setMessage("Publishing riviw " + (int) progress + "%");
                                    dialog.setCancelable(false);
                                    dialog.show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    images.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            post.setImageUrl(uri.toString());
                                            dialog.dismiss();
                                            savePostData(ref, post);

                                        }
                                    });
                                }
                            });


                        } else {
                            savePostData(ref, post);
                        }

                    }
                } else {
                    alertDialog.show();
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.create(Post.this)
                        .folderMode(true)// Activity or Fragment
                        .single()
                        .start();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.cameraOnly().start(Post.this);
            }
        });


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UICreator.getInstance((AppCompatActivity) getActivity()).createDialog(QRScan.newInstance(), "scanner");
            }
        });
    }

    void savePostData(DocumentReference ref, Trend post) {
        ref.set(post)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        showMessage("Review failed, try again");
                    }
                });

        resetFields(rating, company, comment);
        bnv.setSelectedItemId(R.id.trending);
    }

    void validateTextWhilesTyping(AppCompatEditText editText, final View view) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() < 5) {
                    view.setBackground(getActivity().getDrawable(R.drawable.error_bg));
                } else {
                    view.setBackground(getActivity().getDrawable(R.drawable.search_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    void resetFields(ScaleRatingBar rating, AppCompatTextView company, AppCompatEditText comment) {
        rating.setRating(0);
        company.setText("");
        categories.setText(DEFAULT);
        comment.setText("");
        showMessage("Review Sent");
    }

    void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    boolean validateText(AppCompatEditText editText, View view) {
        boolean valid = true;
        if (editText.getText().toString().isEmpty()) {
            view.setBackground(getActivity().getDrawable(R.drawable.error_bg));
            valid = false;
        }

        return valid;
    }


    boolean validateRating(ScaleRatingBar rating) {
        boolean valid = true;
        if (rating.getRating() < 1) {
            rating.setEmptyDrawable(getActivity().getDrawable(R.drawable.error_star));
            valid = false;
        }

        return valid;
    }

    boolean checkCategory(MaterialSpinner category, View view) {
        boolean valid = true;
        if (category.getText().toString().equalsIgnoreCase(DEFAULT)) {
            view.setBackground(getActivity().getDrawable(R.drawable.error_bg));
            valid = false;
        }
        return valid;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            img_url = ImagePicker.getFirstImageOrNull(data).getPath();

            if (!img_url.isEmpty()) {
                preview.setVisibility(View.VISIBLE);
            }
        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            assert getActivity() != null;
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                company.setText(place.getName());
                post.getCompany().setLng(String.valueOf(place.getLatLng().longitude));
                post.getCompany().setLat(String.valueOf(place.getLatLng().latitude));
                post.getCompany().setLocationUrl(setLocation(place.getId(), String.valueOf(place.getLatLng().longitude), String.valueOf(place.getLatLng().latitude)));
                post.getCompany().setCompanyID(place.getId());
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
