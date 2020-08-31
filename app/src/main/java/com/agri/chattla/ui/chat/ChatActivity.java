package com.agri.chattla.ui.chat;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agri.chattla.ui.Payment.PaymentActivity;
import com.agri.chattla.ui.farmerMain.FarmerMainActivity;
import com.apkfuns.xprogressdialog.XProgressDialog;
import com.bumptech.glide.Glide;
import com.agri.chattla.R;
import com.agri.chattla.custom.AddReviewDialog;
import com.agri.chattla.custom.DialogImage;
import com.agri.chattla.model.Bounce;
import com.agri.chattla.model.Chat;
import com.agri.chattla.model.Consult;
import com.agri.chattla.model.UserFirbase;
import com.agri.chattla.ui.base.BaseActivity;
import com.agri.chattla.utils.AppPreferences;
import com.agri.chattla.utils.FcmNotifier;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.SiliCompressor;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.varunjohn1990.audio_record_view.AttachmentOption;
import com.varunjohn1990.audio_record_view.AttachmentOptionsListener;
import com.varunjohn1990.audio_record_view.AudioRecordView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.varunjohn1990.audio_record_view.AttachmentOption.CAMERA_ID;
import static com.varunjohn1990.audio_record_view.AttachmentOption.GALLERY_ID;

public class ChatActivity extends BaseActivity implements View.OnClickListener, AudioRecordView.RecordingListener, AttachmentOptionsListener {

    DatabaseReference reference, refExpert, refBounce;
    private TextView userName ;
    private TextView userName_ ;
    private RecyclerView recyclerView;
    private EditText textMsg;
    private Button btnSend;
    private Button btnEndConsult;
    private ImageView imgBack;
    private TextView tvEndChat;
    private CircularImageView imgProfile;
    private MediaPlayer mediaPlayer;
    private XProgressDialog dialog;

    private Uri imageUri;
    private String imagePath;

    private UserFirbase otherUser ;

    private ChatAdapter messageAdapter;
    private List<Chat> mChat;

    private String userId;

    private static final int SELECT_AUDIO = 44;

    private AudioRecordView audioRecordView;

    int PICK_Camera_IMAGE = 100;
    int SELECT_IMAGE = 120;
    private Uri fileUri;
    private String imageFilePath;
    private File photoFile;

    private MediaRecorder mediaRecorder;
    private String outputRecorder;
    private MediaRecorder recorder;
    private File audiofile;
    private String userNameTxt;
    private String myId;
    private HashMap<String, File> mapFiles;
    private HashMap<Object, Object> map;

    StorageReference storageReference;
    DatabaseReference refChat;

    private Consult consult;
    private double bounce;

    @Override
    protected void onStart() {
        super.onStart();
        AppPreferences.setIsUserInConversation(this,"in");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        audioRecordView = new AudioRecordView();
        audioRecordView.initView(findViewById(R.id.layoutMain));
        View containerView = audioRecordView.setContainerView(R.layout.layout_chatting);
        audioRecordView.setRecordingListener(this);

        myId = AppPreferences.getUserPhone(this);
        otherUser = (UserFirbase) getIntent().getExtras().get("user");
        consult = (Consult) getIntent().getExtras().get("consult");

        mediaPlayer = MediaPlayer.create(ChatActivity.this , R.raw.tone1);
        //mediaPlayer.start();

        userNameTxt = otherUser.getUserName();
        userId = otherUser.getId();

        btnEndConsult = findViewById(R.id.btn_end_col);

        refChat = FirebaseDatabase.getInstance().getReference().child("Chats").child(consult.getId());
        refBounce = FirebaseDatabase.getInstance().getReference().child("bounce");
        refBounce.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Bounce b = dataSnapshot.getValue(Bounce.class);
                    bounce = b.getAmount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        initialComponent(containerView);

        setupRecyclerview();

        outputRecorder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        mediaRecorder = new MediaRecorder();


        if (hasPermissions(ChatActivity.this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
        })) {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            mediaRecorder.setOutputFile(outputRecorder);

        } else {
            BaseActivity.requestAllPermissions(ChatActivity.this);
        }


        setListener();
        audioRecordView.getMessageView().requestFocus();

        List<AttachmentOption> attachmentOptions = new ArrayList<>();
        attachmentOptions.add(new AttachmentOption(CAMERA_ID, "كاميرا", R.drawable.ic_attachment_camera));
        attachmentOptions.add(new AttachmentOption(GALLERY_ID, "الاستديو", R.drawable.ic_attachment_gallery));
        audioRecordView.setAttachmentOptions(attachmentOptions, this);

        audioRecordView.removeAttachmentOptionAnimation(true);
        audioRecordView.showEmojiIcon(false);

        checkConsultStatus();

        readMessages();


    }

    private void checkConsultStatus() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Consults");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Consult mConsult = snapshot1.getValue(Consult.class);
                    if (mConsult != null && mConsult.getId().equals(consult.getId()) && mConsult.getStatus().equals("finished")) {
                        audioRecordView.endChat();
                        tvEndChat.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setupRecyclerview() {

        messageAdapter = new ChatAdapter(ChatActivity.this, myId, new ChatAdapter.onItemClick() {
            @Override
            public void onItemClick(Chat conversation, ImageView imageView) {
                new DialogImage(ChatActivity.this, conversation.getMessage()).show();

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(messageAdapter);

    }

    private void initialComponent(View containerView) {

        recyclerView = containerView.findViewById(R.id.recycler_view);
        imgBack = containerView.findViewById(R.id.img_back);
        userName = containerView.findViewById(R.id.tv_username);
        userName_ = containerView.findViewById(R.id.tv_username_);
        tvEndChat = containerView.findViewById(R.id.tv_end_chat);
        imgProfile = containerView.findViewById(R.id.img_profile);

        if (AppPreferences.getUserType(this).equals("Expert")) {
            btnEndConsult.setVisibility(View.GONE);
            userName.setVisibility(View.GONE);
            userName_.setVisibility(View.VISIBLE);
        }

        if (consult.getStatus().equals("finished")) {
            btnEndConsult.setVisibility(View.GONE);
            imgProfile.setVisibility(View.GONE);
            userName.setVisibility(View.GONE);
            userName_.setVisibility(View.VISIBLE);
        }

        if (otherUser.getInfo().equals("Expert")) {
            Glide.with(ChatActivity.this).load(otherUser.getProfile()).error(R.drawable.ic_expert).into(imgProfile);
        } else {
//            Glide.with(ChatActivity.this).load(AppPreferences.getMyProfile(ChatActivity.this)).error(R.drawable.ic_expert).into(imgProfile);
            imgProfile.setVisibility(View.GONE);
        }

        userName.setText(userNameTxt);
        userName_.setText(userNameTxt);
        imgBack.setOnClickListener(this);
        userName.setOnClickListener(this);
        btnEndConsult.setOnClickListener(this);

    }

    private void sendMessage(final String sender, final String reciver, final String msg, final Uri imageUri) {
        audioRecordView.showLoading();
        reference = FirebaseDatabase.getInstance().getReference("Consults").child(consult.getId());

        final String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Calendar.getInstance().getTime()).toLowerCase();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        if (imageUri != null) {
            final StorageReference fileReference = storageReference
                    .child(System.currentTimeMillis() + "." + getFileExtention(imageUri));

            Task<Uri> urlTask = fileReference.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (downloadUri == null)
                            return;
                        else {

                            Chat chat = new Chat();
                            chat.setMessage(downloadUri.toString());
                            chat.setReceiver(reciver);
                            chat.setSender(sender);
                            chat.setTime(time);
                            chat.setType("image");
                            FcmNotifier.sendNotification("صورة","رسالة جديدة",otherUser.getFcmToken());
                            refChat.push().setValue(chat);
                            audioRecordView.hideLoading();
                        }
                    }


                }
            });

        } else {

            Chat chat = new Chat();
            chat.setMessage(msg);
            chat.setReceiver(userId);
            chat.setSender(myId);
            chat.setTime(time);
            chat.setType("text");

            refChat.push().setValue(chat);
            audioRecordView.hideLoading();
            FcmNotifier.sendNotification(msg,"رسالة جديدة",otherUser.getFcmToken());

        }
    }

    private void sendVoice(final String sender, final String reciver, final String filePath) {

        audioRecordView.showLoading();
        final String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Calendar.getInstance().getTime()).toLowerCase();
        storageReference = FirebaseStorage.getInstance().getReference("Audio");
        final Uri uri = Uri.fromFile(new File(filePath));
        if (uri != null) {
            final StorageReference fileReference = storageReference
                    .child(filePath);


            Task<Uri> urlTask = fileReference.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (downloadUri == null)
                            return;
                        else {
                            Chat chat = new Chat();
                            chat.setMessage(downloadUri.toString());
                            chat.setReceiver(reciver);
                            chat.setSender(sender);
                            chat.setTime(time);
                            chat.setType("voice");
                            FcmNotifier.sendNotification("مقطع صوتى","رسالة جديدة",otherUser.getFcmToken());

                            refChat.push().setValue(chat);
                            audioRecordView.hideLoading();

                        }
                    }
                }
            });

        }

    }

    private void readMessages() {
        reference = FirebaseDatabase.getInstance().getReference("Chats").child(consult.getId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    mChat.add(chat);
                }
                messageAdapter.setList(mChat);
                recyclerView.scrollToPosition(mChat.size() - 1);
                mediaPlayer.start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setListener() {

        audioRecordView.getEmojiView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioRecordView.hideAttachmentOptionView();
//                showToast("Emoji Icon Clicked");
            }
        });

        audioRecordView.getCameraView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioRecordView.hideAttachmentOptionView();
                OpenCamera();
            }
        });

        audioRecordView.getSendView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = audioRecordView.getMessageView().getText().toString().trim();
                if (!msg.equals("")) {
                    sendMessage(myId, userId, msg, null);
                }
                audioRecordView.getMessageView().setText("");

            }
        });
    }

    @Override
    public void onRecordingStarted() {

        if (hasPermissions(ChatActivity.this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
        })) {
            try {
                startRecording();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            BaseActivity.requestAllPermissions(ChatActivity.this);
        }


    }

    @Override
    public void onRecordingLocked() {
    }

    @Override
    public void onRecordingCompleted() {
        stopRecording();
        recorder.release();

        sendVoice(myId, userId, audiofile.getAbsolutePath());


    }

    public void startRecording() throws IOException {

        //Creating file
        File dir = Environment.getExternalStorageDirectory();
        try {
            audiofile = File.createTempFile("sound", ".3gp", dir);
        } catch (IOException e) {
            debug(e.getMessage());
            return;
        }
        //Creating MediaRecorder and specifying audio source, output format, encoder & output format
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(audiofile.getAbsolutePath());
        recorder.prepare();
        recorder.start();
    }

    @Override
    public void onRecordingCanceled() {
        stopRecording();
    }

    private void stopRecording() {
        try {
            recorder.stop();
        } catch (RuntimeException stopException) {
            // handle cleanup here
        }
    }

    private void debug(String log) {
        Log.d("test", "" + log);
    }


    @Override
    public void onClick(AttachmentOption attachmentOption) {
        switch (attachmentOption.getId()) {

            case CAMERA_ID:
                if (hasPermissions(ChatActivity.this, new String[]{
                        Manifest.permission.CAMERA,
                })) {
                    OpenCamera();
                } else {
                    BaseActivity.requestAllPermissions(ChatActivity.this);
                }

                break;
            case GALLERY_ID:
                if (hasPermissions(ChatActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                })) {
                    OpenGallery();
                } else {
                    BaseActivity.requestAllPermissions(ChatActivity.this);
                }

                break;


        }
    }

    public void OpenGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_IMAGE);
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void OpenCamera() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a fileUri to store the image
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                fileUri = FileProvider.getUriForFile(this, "com.agri.chattla.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(pictureIntent,
                        PICK_Camera_IMAGE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            if (imageUri != null) {

                final Dialog sendImg = new Dialog(ChatActivity.this);
                sendImg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                sendImg.setContentView(R.layout.image_message_dialog);

                RoundedImageView imageMessage = sendImg.findViewById(R.id.imageMessage);
                imageMessage.setImageURI(imageUri);

                Button send = sendImg.findViewById(R.id.imageMessageConfirm);
                Button cancel = sendImg.findViewById(R.id.imageMessageCancel);

                send.setEnabled(true);
                cancel.setEnabled(true);

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendMessage(myId, userId, null, imageUri);
                        sendImg.cancel();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendImg.cancel();
                    }
                });

                sendImg.show();

            }
        } else if (requestCode == PICK_Camera_IMAGE && resultCode == RESULT_OK) {

            sendMessage(myId, userId, null, fileUri);

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_back:
                finishAffinity();
                startActivity(new Intent(ChatActivity.this, FarmerMainActivity.class));
                break;
            case R.id.btn_end_col:
                showDialogEndConsult();
                break;
        }
    }

    @Override
    public void onDestroy() {
        stopRecording();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        stopRecording();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppPreferences.setIsUserInConversation(this,"in");
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppPreferences.setIsUserInConversation(ChatActivity.this,null);
    }


    private String getFileExtention(Uri uri) {
        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void showDialogEndConsult() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder
                .setMessage(R.string.message_end_consultation)
                .setPositiveButton(ChatActivity.this.getResources().getText(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        endConsult();
                    }
                })
                .setNegativeButton(ChatActivity.this.getResources().getText(R.string.cancle), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    private void endConsult() {

        dialog = new XProgressDialog(this, "انتظر", XProgressDialog.THEME_HORIZONTAL_SPOT);
        dialog.show();

        FirebaseDatabase.getInstance().getReference("Consults").child(consult.getId())
                .child("status").setValue("finished");
        FirebaseDatabase.getInstance().getReference("Expert").child(userId)
                .child("consultId").setValue(null);
        audioRecordView.endChat();
        tvEndChat.setVisibility(View.VISIBLE);

        refExpert = FirebaseDatabase.getInstance().getReference().child("Expert").child(userId);
        refExpert.child("balance").setValue(otherUser.getBalance() + bounce);

        if (consult.getAddValue() != null){
            FirebaseDatabase.getInstance().getReference().child("Expert").child(consult.getCodeExpertId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null){
                        UserFirbase expertUser = snapshot.getValue(UserFirbase.class);
                        FirebaseDatabase.getInstance().getReference().child("Expert").child(consult.getCodeExpertId()).child("balance").setValue(expertUser.getBalance() + Double.parseDouble(consult.getAddValue()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        imgProfile.setVisibility(View.GONE);
        userName.setVisibility(View.GONE);
        userName_.setVisibility(View.VISIBLE);
        dialog.dismiss();
        new AddReviewDialog(ChatActivity.this, userId, myId, otherUser.getRate()).show();
    }


}
