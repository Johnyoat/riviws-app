package com.hipromarketing.riviws.constants;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.hipromarketing.riviws.models.User;

import javax.annotation.Nullable;

public class Constants {


    private static User user;
    public static final String DEFAULT = "Select Category";
    public static final String REVIEW_DEFAULT = "Select Review Type";
    public static final String COMPANY = "Company";
    public static final String PRODUCT = "Product";
    public static final String HOTELS = "Hotels";
    public static final String RESTAURANTS = "Restaurants";
    public static final String FASHION = "Fashion";
    public static final String HEALTH = "Health";
    public static final String NETWORK = "Network";
    public static final String TRIPS = "Trips";
    public static final String EDUCATION = "Education";
    public static final String AUTO_MOBILE = "Auto Mobile";
    public static final String TRAVELS = "Travels";
    public static final String COSMETICS = "Cosmetics";
    public static final String FOOD_AND_DRINK = "Food and Drinks";
    public static final String NIGHT_LIFE = "Nightlife";
    public static final String SHOPPING = "Shopping";
    public static final String HEALTH_AND_MEDICAL = "Health and Medical";
    public static final String BEAUTY_AND_SPA = "Beauty & Spas";
    public static final String HOME_AND_SERVICES = "Home Service";
    public static final String LOCAL_JOINT = "Local Joints";
    public static final String LOCAL_SERVICES = "Local Services";
    public static final String ENTERTAINMENT = "Entertainment";
    public static final String REAL_ESTATE= "Real Estate";
    public static final String FINANCIAL_SERVICE = "Financial Services";
    public static final String PUBLIC_INST = "Public Inst.";
    public static final String MASS_MEDIA = "Mass Media";
    public static final String RELIGION = "Religion";
    public static final String BARS = "Bars";
    public static final String PLACES= "Places";

    public static final long RECYCLER_LOAD_TIME = 180;
    public static final String COMMENT = "commented on";
    public static final String LIKED = "liked";
    public static final String NOTIFICATION = "notification";
   public static final String SHOW_BANNER = "showbanner";
    public static final String API_VIDEO_DATA = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=UULUuqP5vBpWBNnUZUE32RLQ&key=AIzaSyBmW8Wm0il1LHAn95OjpQviD0dRJ-T4nto";



    public static User getUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert firebaseUser != null;
        db.collection("users").document(firebaseUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    user = documentSnapshot.toObject(User.class);
                }
            }
        });
        return user;
    }


    public static String setLocation(String placeId,String lng,String lat){
        return "https://www.google.com/maps/search/?api=1&query="+lat+","+lng+"&query_place_id="+placeId+"";
    }

}
