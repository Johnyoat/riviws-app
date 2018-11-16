package com.hipromarketing.riviws.constants;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.models.Category;
import com.hipromarketing.riviws.models.Follow;
import com.hipromarketing.riviws.models.User;

import java.util.ArrayList;
import java.util.List;

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


//    public static List<Follow> getFollowings(){}


    public static List<Category> getCategories(){
        final List<Category> categories = new ArrayList<>();

        categories.add(new Category(HOTELS, R.drawable.room));
        categories.add(new Category(RESTAURANTS, R.drawable.food));
        categories.add(new Category(FASHION, R.drawable.shoe_shirt));
        categories.add(new Category(HEALTH, R.drawable.drugs));
        categories.add(new Category(NETWORK, R.drawable.lady_phone));
        categories.add(new Category(TRIPS, R.drawable.landscape));
        categories.add(new Category(EDUCATION, R.drawable.student_boy));
        categories.add(new Category(AUTO_MOBILE, R.drawable.whitecar));
        categories.add(new Category(TRAVELS, R.drawable.bridge));
        categories.add(new Category(COSMETICS, R.drawable.cosmetics));
        categories.add(new Category(FOOD_AND_DRINK, R.drawable.food_and_drinks));
        categories.add(new Category(NIGHT_LIFE, R.drawable.night_life));
        categories.add(new Category(SHOPPING, R.drawable.shopping));
        categories.add(new Category(HEALTH_AND_MEDICAL, R.drawable.health_and_medical));
        categories.add(new Category(BEAUTY_AND_SPA, R.drawable.beauty_and_spa));
        categories.add(new Category(HOME_AND_SERVICES, R.drawable.home_and_services));
        categories.add(new Category(LOCAL_JOINT, R.drawable.local_joint));
        categories.add(new Category(LOCAL_SERVICES, R.drawable.local_business));
        categories.add(new Category(ENTERTAINMENT, R.drawable.entertainment));
        categories.add(new Category(REAL_ESTATE, R.drawable.real_estate));
        categories.add(new Category(FINANCIAL_SERVICE, R.drawable.financial_service));
        categories.add(new Category(PUBLIC_INST, R.drawable.public_institution));
        categories.add(new Category(MASS_MEDIA, R.drawable.mass_media));
        categories.add(new Category(RELIGION, R.drawable.spirituality));
        categories.add(new Category(BARS, R.drawable.bars));
        categories.add(new Category(PLACES, R.drawable.places));

        return categories;
    }

    public static String setLocation(String placeId,String lng,String lat){
        return "https://www.google.com/maps/search/?api=1&query="+lat+","+lng+"&query_place_id="+placeId+"";
    }

}
