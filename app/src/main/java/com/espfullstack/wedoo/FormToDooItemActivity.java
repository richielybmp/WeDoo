package com.espfullstack.wedoo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.espfullstack.wedoo.controllers.ToDooItemController;
import com.espfullstack.wedoo.events.ToDooItemActionEvent;
import com.espfullstack.wedoo.pojo.ToDooItem;
import com.espfullstack.wedoo.pojo.UploadImages;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

public class FormToDooItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    @BindView(R.id.btn_back_form_todo_item)
    ImageButton btnBack;

    @BindView(R.id.fab_createToDooItem)
    FloatingActionButton btnCreateTodoItem;

    @BindView(R.id.edt_title)
    EditText edtTitle;

    @BindView(R.id.edt_description)
    EditText edtDescription;

    @BindView(R.id.preview_image_item_todo)
    ImageView imageViewTodoItem;

    @BindView(R.id.btn_choose_galery)
    ImageButton btnChooseImage;

    @BindView(R.id.progressBar_todoItem)
    ProgressBar progressBar;

    @BindView(R.id.bg_loading)
    LinearLayout loadding_bg;

    int toDooId;
    ToDooItem toDooItem;
    ToDooItemController toDooItemController;

    ToDooItemActionEvent.ToDooItemAction toDooItemAction;

    boolean isUpdate = false;
    private Uri imageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private String imageStoragedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_to_do_item);
        ButterKnife.bind(this);

        toDooItemController = new ToDooItemController(this);
        Bundle bundle = getIntent().getExtras();
        toDooId =  bundle.getInt("todoo");
        toDooItem = (ToDooItem) bundle.getSerializable("todoo_item");

        initializeFirebase();

        if (toDooItem == null) {
            toDooItem = new ToDooItem();
        } else {
            init(toDooItem);
        }
    }

    private void init(ToDooItem toDooItem) {
        isUpdate = true;
        edtTitle.setText(toDooItem.getTitle());
        edtDescription.setText(toDooItem.getDescription());
    }

    public void initializeFirebase(){
        FirebaseApp.initializeApp(this);
        mStorageRef = FirebaseStorage.getInstance().getReference("images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("images");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @OnClick(R.id.fab_createToDooItem)
    public void cadastrar(){
        if(inputsValidate()){
            if(mUploadTask != null && mUploadTask.isInProgress()){
                Toast.makeText(this, "Upload in progress...", Toast.LENGTH_SHORT).show();
            }else{
                saveWithImage();
            }
            if(imageUri == null){
                eventSave();
            }
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private boolean save() {
        if(isUpdate) {
            toDooItemAction = ToDooItemActionEvent.ToDooItemAction.UPDATED;
            return toDooItemController.update(toDooItem);
        } else {
            toDooItemAction = ToDooItemActionEvent.ToDooItemAction.SAVED;
            return toDooItemController.add(toDooId, toDooItem);
        }
    }

    private void eventSave(){
        if(save()) {
            EventBus.getDefault().postSticky(new ToDooItemActionEvent(toDooItem, toDooItemAction));
            voltar();
        }
    }

    private void saveWithImage(){

        if(imageUri != null ){
            imageStoragedID = System.currentTimeMillis()+"_"+toDooId+"."+getFileExtension(imageUri);
            StorageReference fileStorage = mStorageRef.child(imageStoragedID);
            mUploadTask = fileStorage.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                                    progressBar.setProgress(0);
                                    loadding_bg.setVisibility(View.GONE);
                                }
                            }, 500);
                            Toast.makeText(FormToDooItemActivity.this, "Upload Sucessful", Toast.LENGTH_SHORT).show();
                            UploadImages uploadImages = new UploadImages(taskSnapshot.getStorage().getDownloadUrl().toString());

                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(uploadImages);
                            Log.d("idUmage-->", imageStoragedID);
                            Log.d("uploadId-->", uploadId);
                            toDooItem.setImageId(imageStoragedID);
                            eventSave();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FormToDooItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            loadding_bg.setVisibility(View.VISIBLE);

                        }
                    });
        }
    }

    @OnClick(R.id.btn_back_form_todo_item)
    public void voltar(){
        finish();
    }

    public Boolean inputsValidate(){
        String title = edtTitle.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        if(TextUtils.isEmpty(title)){
            edtTitle.requestFocus();
            edtTitle.setError(getString(R.string.required));
            return false;
        }

        toDooItem.setTitle(title);
        toDooItem.setDescription(description);

        return true;
    }

    @OnClick(R.id.btn_choose_galery)
    public void chooseImage(){
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageViewTodoItem);
        }

    }
}
