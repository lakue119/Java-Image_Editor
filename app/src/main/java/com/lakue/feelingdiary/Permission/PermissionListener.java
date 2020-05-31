package com.lakue.feelingdiary.Permission;

import java.util.List;

public interface PermissionListener {

    void onPermissionGranted();

    void onPermissionDenied(List<String> deniedPermissions);

}
