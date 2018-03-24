package project.everyday.dev.simplecameralibeary.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.annotation.IntRange;

/**
 * Created by vipin on 23-03-2018.
 */

public class Utils extends BaseClass {
    /**
     * Check if this device has a camera
     */
    public static boolean checkDeviceHasCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() throws Exception {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            throw e;
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance(@IntRange(from = 0, to = 6) int cameraId) throws Exception {
        Camera c = null;
        try {
            c = Camera.open(cameraId); // attempt to get a Camera instance
        } catch (Exception e) {
            throw e;
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}
