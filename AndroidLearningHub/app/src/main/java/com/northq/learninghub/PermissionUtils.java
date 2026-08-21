package com.northq.learninghub;

import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;

/**
 * Unit 4 Practical: "Handle runtime permissions, including camera, storage,
 * and location permissions." Centralised here so MediaPickerActivity,
 * MapsActivity, and ContactsActivity all check permissions the same way
 * instead of duplicating the logic in every screen.
 */
public class PermissionUtils {

    public static boolean isGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }
}
