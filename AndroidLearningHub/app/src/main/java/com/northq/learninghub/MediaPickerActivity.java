package com.northq.learninghub;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Unit 1 Practical: "Access the device camera and gallery."
 * Unit 2 Practical: "Display images using ImageView and GalleryView."
 * Unit 4 Practical: "Handle runtime permissions, including camera and storage permissions."
 */
public class MediaPickerActivity extends AppCompatActivity {

    private final List<Uri> pickedImages = new ArrayList<>();
    private ImageAdapter adapter;
    private Uri pendingCameraUri;

    private final ActivityResultLauncher<Uri> takePicture =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                if (success && pendingCameraUri != null) {
                    pickedImages.add(0, pendingCameraUri);
                    adapter.notifyItemInserted(0);
                }
            });

    private final ActivityResultLauncher<String> pickFromGallery =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    pickedImages.add(0, uri);
                    adapter.notifyItemInserted(0);
                }
            });

    private final ActivityResultLauncher<String> requestCameraPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) launchCamera();
                else Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
            });

    private final ActivityResultLauncher<String> requestGalleryPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) pickFromGallery.launch("image/*");
                else Toast.makeText(this, "Storage permission is required", Toast.LENGTH_SHORT).show();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_picker);
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        RecyclerView grid = findViewById(R.id.imageGrid);
        grid.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new ImageAdapter(this, pickedImages);
        grid.setAdapter(adapter);

        findViewById(R.id.cameraBtn).setOnClickListener(v -> {
            if (PermissionUtils.isGranted(this, Manifest.permission.CAMERA)) {
                launchCamera();
            } else {
                requestCameraPermission.launch(Manifest.permission.CAMERA);
            }
        });

        findViewById(R.id.galleryBtn).setOnClickListener(v -> {
            String permission = Build.VERSION.SDK_INT >= 33
                    ? Manifest.permission.READ_MEDIA_IMAGES
                    : Manifest.permission.READ_EXTERNAL_STORAGE;
            if (PermissionUtils.isGranted(this, permission)) {
                pickFromGallery.launch("image/*");
            } else {
                requestGalleryPermission.launch(permission);
            }
        });
    }

    private void launchCamera() {
        try {
            File imageFile = createImageFile();
            pendingCameraUri = FileProvider.getUriForFile(
                    this, getPackageName() + ".fileprovider", imageFile);
            takePicture.launch(pendingCameraUri);
        } catch (IOException e) {
            Toast.makeText(this, "Could not create image file", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new java.util.Date());
        File storageDir = getExternalFilesDir("Pictures");
        return File.createTempFile("IMG_" + timeStamp, ".jpg", storageDir);
    }
}
