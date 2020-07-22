package com.agri.chattla.ui.usersList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agri.chattla.R;
import com.agri.chattla.model.Chat;
import com.agri.chattla.model.UserFirbase;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private Context mContext;
    private List<UserFirbase> mUsers = new ArrayList<>();
    private boolean isChat;
    private String theLastMsg;

    private final onItemClick onItemClick;

    public interface onItemClick {
        void onItemClick(UserFirbase user, LinearLayout layout);
    }


    public void setmUsers(List<UserFirbase> mUsers) {
        this.mUsers = mUsers;
        notifyDataSetChanged();
    }

    public UserListAdapter(Context context, boolean isChat, onItemClick onItemClick) {
        this.mContext = context;
        this.isChat = isChat;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final UserFirbase user = mUsers.get(position);
        holder.userName.setText(user.getUserName());

//        if (user.getImageURL().equals("default")) {
//            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
//        } else {
//            Glide.with(mContext).load(user.getImageURL()).into(holder.profileImage);
//        }

        if (isChat) {
            lastMessage(user.getId(), holder.lastMsg);
        } else {
            holder.lastMsg.setVisibility(View.GONE);
        }

        if (isChat) {
            if (user.getStatus().equals("online")) {
                holder.tvStatus.setText("Online");
            } else {
                holder.tvStatus.setText("Offline");
            }
        }

        holder.bind(user, onItemClick);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView userName;
        private CircleImageView profileImage;
        private TextView lastMsg;
        private TextView tvStatus;
        private LinearLayout layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            profileImage = itemView.findViewById(R.id.profile_image);
            lastMsg = itemView.findViewById(R.id.last_msg);
            tvStatus = itemView.findViewById(R.id.tv_status);
            layout = itemView.findViewById(R.id.layout);

        }


        private void bind(final UserFirbase user, final onItemClick onItemClick) {


            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onItemClick(user, layout);
                }
            });


        }

    }

    // Check for last message
    private void lastMessage(final String userId, final TextView lastMsg) {

        theLastMsg = "default";

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getSender().equals(userId) && chat.getReceiver().equals(AppPreferences.getUserPhone(mContext)) ||
                            chat.getSender().equals(AppPreferences.getUserPhone(mContext)) && chat.getReceiver().equals(userId)) {

                        if (chat.getType().equals("text")) {
                            theLastMsg = chat.getMessage();
                        } else {
                            theLastMsg = "صورة";
                        }
                    }
                }
                switch (theLastMsg) {
                    case "default":
                        lastMsg.setText("No messages");
                        break;
                    default:
                        lastMsg.setText(theLastMsg);
                        break;
                }
                theLastMsg = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
