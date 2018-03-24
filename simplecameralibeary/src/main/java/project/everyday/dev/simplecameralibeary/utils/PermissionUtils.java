package project.everyday.dev.simplecameralibeary.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by vipin on 23-03-2018.
 */

public class PermissionUtils extends BaseClass {
    /*public static void checkPermission(@NonNull final Activity activity,@NonNull String[] permissions,@NonNull final PermissionResponse permissionResponse) {
        final ArrayList<String> askPermissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                askPermissionList.add(permissions[i]);
            }
        }
        if (askPermissionList.size() > 0) {
            android.os.Handler handler =new android.os.Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> grandList = new ArrayList<>();
                    ArrayList<String> deniedList = new ArrayList<>();
                    for ( String permission: askPermissionList ) {
                        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)){
                            grandList.add(permission);
                        }
                        else {
                            deniedList.add(permission);
                        }
                    }
                    if(grandList.size() == 0){
                        permissionResponse.onAllPermissionDenied();
                    }else if(deniedList.size() == 0){
                        permissionResponse.onAllPermissionGranted();
                    }else {
                        permissionResponse.onSomeGranted(grandList,deniedList);
                    }
                }
            });
        }
        else {
            permissionResponse.onAllPermissionGranted();
        }

    }*/
    public static boolean hasPermission(@NonNull final Context context, @NonNull final String permission) {
        return (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean hasPermission(@NonNull final Context context, @NonNull final String[] permissions) {
        for (String per : permissions) {
            if (ContextCompat.checkSelfPermission(context, per)
                    != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    public interface PermissionResponse {
        void onAllPermissionGranted();

        void onAllPermissionDenied();

        void onSomeGranted(ArrayList<String> grantedList, ArrayList<String> deniedList);
    }
    /*private static void requestPermissions(final @NonNull Activity activity,
                                          final @NonNull String[] permissions, final @IntRange(from = 0) int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity instanceof ActivityCompat.RequestPermissionsRequestCodeValidator) {
                ((ActivityCompat.RequestPermissionsRequestCodeValidator) activity)
                        .validateRequestPermissionsRequestCode(requestCode);
            }
            activity.requestPermissions(permissions, requestCode);
        } else if (activity instanceof ActivityCompat.OnRequestPermissionsResultCallback) {
            android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    final int[] grantResults = new int[permissions.length];

                    PackageManager packageManager = activity.getPackageManager();
                    String packageName = activity.getPackageName();

                    final int permissionCount = permissions.length;
                    for (int i = 0; i < permissionCount; i++) {
                        grantResults[i] = packageManager.checkPermission(
                                permissions[i], packageName);
                    }

                    ((ActivityCompat.OnRequestPermissionsResultCallback) activity).onRequestPermissionsResult(
                            requestCode, permissions, grantResults);
                }
            });
        }
    }*/
}
