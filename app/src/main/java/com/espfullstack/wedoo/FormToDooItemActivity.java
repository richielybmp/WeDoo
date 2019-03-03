package com.espfullstack.wedoo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.espfullstack.wedoo.controllers.ToDooItemController;
import com.espfullstack.wedoo.events.ToDooItemActionEvent;
import com.espfullstack.wedoo.pojo.ToDooItem;
import com.espfullstack.wedoo.pojo.UploadImages;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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

    @BindView(R.id.btn_cam_form_todo_item)
    ImageButton btnCaptureImage;

    @BindView(R.id.progressBar_todoItem)
    ProgressBar progressBar;

    @BindView(R.id.bg_loading)
    LinearLayout loadding_bg;

    @BindView(R.id.loadding_image)
    TextView loaddingImage;

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
    private Bitmap bitmapImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_to_do_item);
        ButterKnife.bind(this);

        toDooItemController = new ToDooItemController(this);
        Bundle bundle = getIntent().getExtras();
        toDooId = bundle.getInt("todoo");
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
        if (toDooItem.getImageId() != null) {
            loadding_bg.setVisibility(View.VISIBLE);
            loaddingImage.setVisibility(View.VISIBLE);
            StorageReference strRef = mStorageRef.child(toDooItem.getImageId());
            final long ONE_MEGABYTE = 1024 * 2048;
            strRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);

                    imageViewTodoItem.setImageBitmap(bm);
                    loadding_bg.setVisibility(View.GONE);
                    loaddingImage.setVisibility(View.GONE);
                }
            });
        }
    }

    public void initializeFirebase() {
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
    public void cadastrar() {
        if (inputsValidate()) {
            if (mUploadTask != null && mUploadTask.isInProgress()) {
                Toast.makeText(this, "Upload in progress...", Toast.LENGTH_SHORT).show();
            } else {
                saveWithImage();
            }
            if (imageUri == null && bitmapImage == null) {
                eventSave();
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private boolean save() {
        if (isUpdate) {
            toDooItemAction = ToDooItemActionEvent.ToDooItemAction.UPDATED;
            return toDooItemController.update(toDooItem);
        } else {
            toDooItemAction = ToDooItemActionEvent.ToDooItemAction.SAVED;
            return toDooItemController.add(toDooId, toDooItem);
        }
    }

    private void eventSave() {
        if (save()) {
            EventBus.getDefault().postSticky(new ToDooItemActionEvent(toDooItem, toDooItemAction));
            voltar();
        }
    }

    private void saveWithImage() {

        if (imageUri != null || bitmapImage != null) {
            if (bitmapImage != null) {
                imageStoragedID = System.currentTimeMillis() + "_" + toDooId + ".JPEG";
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                StorageReference fileStorage = mStorageRef.child(imageStoragedID);
                mUploadTask = fileStorage.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
//                        Log.d("idUmage-->", imageStoragedID);
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
            if (imageUri != null) {
                imageStoragedID = System.currentTimeMillis() + "_" + toDooId + "." + getFileExtension(imageUri);
                StorageReference fileStorage = mStorageRef.child(imageStoragedID);
                mUploadTask = fileStorage.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
    }

    @OnClick(R.id.btn_back_form_todo_item)
    public void voltar() {
        finish();
    }

    public Boolean inputsValidate() {
        String title = edtTitle.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            edtTitle.requestFocus();
            edtTitle.setError(getString(R.string.required));
            return false;
        }

        toDooItem.setTitle(title);
        toDooItem.setDescription(description);

        return true;
    }

    @OnClick(R.id.btn_choose_galery)
    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @OnClick(R.id.btn_cam_form_todo_item)
    public void capturePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (grantResults.length > 0) {
                if (grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //TODO ABRO A CAMERA
                } else {
                    showError(R.string.error_permission_not_granted);
                }
            } else {
                showError(R.string.permission_request_canceled);
            }
        }
    }

    public void showError(int idStringDescription) {
//        new MaterialDialog.Builder(this)
//                .title(R.string.title_error)
//                .content(idStringDescription)
//                .positiveText(R.string.label_ok)
//                .show();
        Toast.makeText(this, "Error man.", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                //Se existir nova imagem
                //imageUri = data.getData();
                imageViewTodoItem.setDrawingCacheEnabled(true);
                imageViewTodoItem.buildDrawingCache();
                bitmapImage = (Bitmap) data.getExtras().get("data");
                imageViewTodoItem.setImageBitmap(bitmapImage);
                bitmapImage = imageViewTodoItem.getDrawingCache();
            }

            if (data != null && data.getData() != null) {
                imageUri = data.getData();

                //StorageReference filePath = mStorageRef.child()

                Picasso.get().load(imageUri).into(imageViewTodoItem);
            }

        }

    }
}
