package com.hipromarketing.riviws.utils;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hipromarketing.riviws.models.NotificationObject;
import com.hipromarketing.riviws.models.Trend;

import java.util.Map;

import static com.hipromarketing.riviws.constants.Constants.LIKED;
import static com.hipromarketing.riviws.constants.Constants.getUser;

public class PostObjectHelper {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static PostObjectHelper getInstance() {
        return new PostObjectHelper();
    }

    private PostObjectHelper(){

    }

    public void setAction(String action, String postId, String company, String uid) {

        NotificationObject object = new NotificationObject();
        object.setUser(getUser());
        object.setAction(action);
        object.setPostId(postId);
        object.setRead(false);
        object.setNewpost(true);
        object.setCompany(company);
        object.setDate(String.valueOf(System.currentTimeMillis()));

        DocumentReference ref;
        if (action.equalsIgnoreCase(LIKED)){
            ref = db.collection("users").document(uid).collection("notifications").document(postId+"1");
            object.setId(postId+"1");
        }else  {
            ref = db.collection("users").document(uid).collection("notifications").document();
            object.setId(ref.getId());
        }
        Map<String, Object> notificationObj = new ObjectMapper().convertValue(object, Map.class);
        ref.set(notificationObj);
    }

    public void readPost(NotificationObject obj){
        db.collection("users").document(getUser().getUid()).collection("notifications").document(obj.getId()).update("read",true)
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public void unLike(Trend obj){
        db.collection("users").document(obj.getUser().getUid()).collection("notifications").document(obj.getId()+"1").delete();
    }
}
