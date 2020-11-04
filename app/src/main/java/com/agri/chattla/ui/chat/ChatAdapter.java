package com.agri.chattla.ui.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.agri.chattla.R;
import com.agri.chattla.model.Chat;
import com.google.firebase.auth.FirebaseUser;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rygelouv.audiosensei.player.AudioSenseiPlayerView;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chat> mChats = new ArrayList<>();


    private String myId;
    private String expertId;
    FirebaseUser firebaseUser;
    private final onItemClick onItemClick;



    public ChatAdapter(Context context,String expertId , String myId, onItemClick onItemClick) {
        this.mContext = context;
        this.expertId = expertId;
        this.myId = myId;
        this.onItemClick = onItemClick;

    }

    public interface onItemClick {
        void onItemClick(Chat conversation, ImageView imageView);
    }


    public void setList(List<Chat> mChats) {
        this.mChats = mChats;
        notifyItemInserted(mChats.size() - 1);
        notifyItemRangeChanged(mChats.size() - 1, mChats.size());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_message, parent, false);
            return new ChatAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_other_message, parent, false);
            return new ChatAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Chat chat = mChats.get(position);

//        if (chat.getSender().equals(myId)) {
//            holder.imgProfile.setImageResource(R.drawable.ic_farmer);
//        } else {
//            holder.imgProfile.setImageResource(R.drawable.ic_expert);
//
//        }


//        if (position == mChats.size() - 1) {
//
//            if (chat.isSeen()) {
//                holder.tvSeen.setText("seen");
//            } else {
//                holder.tvSeen.setText("delivered");
//            }
//
//        } else {
//            holder.tvSeen.setVisibility(View.GONE);
//        }


        holder.bind(chat,onItemClick);

    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardviewMessage;
        private RoundedImageView imgProfile;
        private TextView tvMessage;
        private TextView tvTime;
        private RoundedImageView ivImage;
        private AudioSenseiPlayerView audioPlayer;
        private TextView tvSeen;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardviewMessage = itemView.findViewById(R.id.cardview_message);
            imgProfile = itemView.findViewById(R.id.img_profile);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivImage = itemView.findViewById(R.id.iv_image);
            audioPlayer = itemView.findViewById(R.id.audio_player);
            tvSeen = itemView.findViewById(R.id.tv_seen);

            cardviewMessage.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.transparent));
            tvSeen.setVisibility(View.GONE);
        }

        public void bind(Chat chatMessage, onItemClick onItemClick) {

            tvTime.setText(chatMessage.getTime());

            if (chatMessage.getType().equals("text")) {
                ivImage.setVisibility(View.GONE);
                audioPlayer.setVisibility(View.GONE);
                tvMessage.setVisibility(View.VISIBLE);
                tvMessage.setText(chatMessage.getMessage());


            } else if (chatMessage.getType().equals("image")) {

                audioPlayer.setVisibility(View.GONE);
                tvMessage.setVisibility(View.GONE);
                ivImage.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(chatMessage.getMessage())
                        .apply(new RequestOptions().placeholder(R.drawable.loading))
                        .into(ivImage);

            } else if (chatMessage.getType().equals("voice")) {
                tvMessage.setVisibility(View.GONE);
                imgProfile.setVisibility(View.GONE);
                ivImage.setVisibility(View.GONE);
                audioPlayer.setVisibility(View.VISIBLE);
                audioPlayer.setAudioTarget(chatMessage.getMessage());
                audioPlayer.requestFocus();
                tvTime.setVisibility(View.GONE);
            }

            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onItemClick(chatMessage, ivImage);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (mChats.get(position).getSender().equals(expertId)) {
            return MSG_TYPE_LEFT ;
        } else {
            return MSG_TYPE_RIGHT ;
        }

    }
}
