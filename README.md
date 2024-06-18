# Android Permission Handling with Dexter

This project demonstrates how to handle runtime permissions in an Android application using the Dexter library. It includes setting up Dexter and requesting permissions based on different Android versions.

## Table of Contents

- [Introduction](#introduction)
- [Setup](#setup)
- [Usage](#usage)
  - [Requesting Permissions](#requesting-permissions)
- [Permissions for Different Android Versions](#permissions-for-different-android-versions)
- [License](#license)

## Introduction

Dexter is a library that simplifies the process of requesting runtime permissions in Android applications. This project provides a concise and efficient way to handle permissions, ensuring compatibility across different Android versions.

## Setup

To use Dexter in your project, follow these steps:

1. **Add the Dexter dependency** to your `build.gradle` file:

    ```groovy
    dependencies {
        implementation 'com.karumi:dexter:6.2.3'
    }
    ```

2. **Sync your project** with Gradle files.

## Usage

### Requesting Permissions

Here’s an example of how to request permissions using Dexter in your activity:

```kotlin
import android.Manifest
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.PermissionRequest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions()
    }

    private fun requestPermissions() {
        val permissions = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            else -> listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

        Dexter.withContext(this)
            .withPermissions(permissions)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            Log.d("Permission", "PermissionGranted")
                        } else if (report.isAnyPermissionPermanentlyDenied) {
                            Log.d("Permission", "isAnyPermissionPermanentlyDenied")
                            // permissionDialog() // Uncomment to show a dialog if needed
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .check()
    }
}
