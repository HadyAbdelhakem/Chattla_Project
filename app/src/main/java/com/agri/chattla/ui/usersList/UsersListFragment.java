package com.agri.chattla.ui.usersList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agri.chattla.R;
import com.agri.chattla.model.UserFirbase;
import com.agri.chattla.ui.chat.ChatActivity;
import com.agri.chattla.utils.AppPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersListFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserListAdapter userListAdapter;
    List<UserFirbase> mUsers;
    private String currentUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_users_list, container, false);

        currentUser = AppPreferences.getUserPhone(getContext());

        setupRecyclerView(view);

        readUsers();

        return view;

    }

    private void setupRecyclerView(View view) {
        userListAdapter = new UserListAdapter(getContext(), true, new UserListAdapter.onItemClick() {
            @Override
            public void onItemClick(UserFirbase user, LinearLayout layout) {
                startActivity(new Intent(getContext(), ChatActivity.class)
                        .putExtra("user", user));
            }
        });
        recyclerView = view.findViewById(R.id.recycler_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(userListAdapter);
    }

    private void readUsers() {

        mUsers = new ArrayList<>();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    UserFirbase user = dataSnapshot1.getValue(UserFirbase.class);

                    assert user != null;
                    assert firebaseUser != null;

                    if (!user.getId().equals(currentUser) && !user.getInfo().equals("Farmer")) {
                        mUsers.add(user);
                    }
                }
                userListAdapter.setmUsers(mUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
