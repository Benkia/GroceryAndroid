package com.example.admin.myapplication.controller.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.controller.authentication.AuthenticationManager;
import com.example.admin.myapplication.controller.database.remote.ImageDB;
import com.example.admin.myapplication.controller.database.remote.UsersDB;
import com.example.admin.myapplication.controller.handlers.BitmapReceivedHandler;
import com.example.admin.myapplication.controller.handlers.UserReceivedHandler;
import com.example.admin.myapplication.model.entities.User;

/**
 * Created by admin on 06/04/2017.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_view, container, false);

        // Get userKey from Auth
        String userKey = AuthenticationManager.getInstance().getCurrentUserId();

        initUsernameTextView(userKey, (TextView) view.findViewById(R.id.userNameTV));
        initImageView(userKey, (ImageView) view.findViewById(R.id.imageView));

        return view;
    }

    private void initUsernameTextView(String userKey, final TextView userNameTV) {
        UserReceivedHandler userReceivedHandler = new UserReceivedHandler() {
            @Override
            public void onUserReceived(User user) {
                userNameTV.setText(user.getName());
            }

            @Override
            public void removeAllUsers() {}
        };

        UsersDB.getInstance().findUserByKey(userKey, userReceivedHandler);
    }

    private void initImageView(String userKey, final ImageView imageView) {
        BitmapReceivedHandler imageReceivedHandler = new BitmapReceivedHandler() {
            @Override
            public void onBitmapReceived(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void removeAllBitmaps() {}
        };

        ImageDB.getInstance().downloadImage(userKey, imageReceivedHandler);
    }

    public void changeImageDialog(Context context) {
        // TODO:
//        // Open a dialog.
//        final Dialog dialog = new Dialog(context);
//        dialog.setContentView(R.layout.new_group_dialog);
//        dialog.setTitle("New Group");
//
//        // Get the EditText and focus on it.
//        final EditText groupTitleText = (EditText) dialog.findViewById(R.id.groupTitleText);
//        groupTitleText.requestFocus();
//
//        ImageButton confirmButton = (ImageButton) dialog.findViewById(R.id.confirm);
//
//        // If button is clicked, close the custom dialog
//        confirmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//
//                // Get the user input.
//                String groupTitle = groupTitleText.getText().toString();
//
//                // Add the new group to the database.
//                Group newGroup = new Group("", groupTitle);
//
//        // Get userKey from Auth
//        String userKey = AuthenticationManager.getCurrentUserId();
//
//                // TODO: UserGroupsDB? GroupMembersDB?
//                GroupsDB.getInstance().addNewGroup(newGroup, userKey);
//            }
//        });
//
//        dialog.show();
    }
}