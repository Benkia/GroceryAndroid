package com.example.admin.myapplication.controller.database.remote;

import android.util.Log;

import com.example.admin.myapplication.controller.ObjectReceivedHandler;
import com.example.admin.myapplication.model.entities.Group;
import com.example.admin.myapplication.model.entities.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 05/04/2017.
 */
public class UsersDB {
    private static final String TAG = "UsersDB";
    private static UsersDB instance;
    private static final String USERS_NODE_URL = "users";
    private DatabaseReference usersRef;
    private Map<String, User> userCache = new HashMap<>();

    private UsersDB() {
        usersRef = FirebaseDatabase.getInstance().getReference().child(USERS_NODE_URL);
    }
    public static UsersDB getInstance() {
        if (instance == null) {
            instance = new UsersDB();
        }

        return instance;
    }

    public void findUserByKey(final String userKey, final ObjectReceivedHandler handler) {
        if (userCache.get(userKey) == null) {
            // Read from the database
            usersRef.child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = mapToUser(userKey, (Map<String, Object>)dataSnapshot.getValue());
                    userCache.put(userKey, user);
                    handler.onObjectReceived(user);

//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    Group group = mapToGroup(child.getKey(), ((Map<String, Object>)child.getValue()));
//
//                    groups.add(group);
//                }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read user for key: " + userKey, error.toException());
                }
            });
        }
        else {
            handler.onObjectReceived(userCache.get(userKey));
        }
    }

//    func findUserByFacebookId(facebookId: String, whenFinished: @escaping (_: User) -> Void) {
//        databaseRef.child(rootNode).queryOrdered(byChild: "facebookId").queryEqual(toValue: facebookId).observeSingleEvent(
//                of: FIRDataEventType.value, with: {(snapshot) in
//            if !(snapshot.value is NSNull) {
//                let userSnapshot = (snapshot.value as! Dictionary<String, Any>).first!
//                        let user = self.extractUser(key: userSnapshot.key as NSString, values: userSnapshot.value as! Dictionary<String, Any>)
//                whenFinished(user)
//            }
//        })
//    }

    public void addNewUser(User user) {
        // TODO: In iphone app we already have the key? WUT
        // Generate a key for the new group
//        String key = usersRef.push().getKey();
        String key = user.getKey();
        Map<String, Object> postValues = user.toMap();

        // Set the values
        usersRef.child(key).setValue(postValues);

        userCache.put(key, user);
    }

    private User mapToUser(String key, Map<String, Object> values) {
        String userKey = key;
        String userName = (String) values.get("name");
        String facebookId = (String) values.get("facebookId");

        return new User(userKey, facebookId, userName);
    }
}