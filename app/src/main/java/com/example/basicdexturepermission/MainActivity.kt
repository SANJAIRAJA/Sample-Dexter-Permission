package com.example.basicdexturepermission

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.basicdexturepermission.databinding.ActivityMainBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        binding.tvCameraPermission.setOnClickListener {
            val permissions = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> listOf(
                    Manifest.permission.CAMERA,
                )
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> listOf(
                    Manifest.permission.CAMERA,
                )
                else -> listOf(
                    Manifest.permission.CAMERA,
                )
            }
            checkPermission(permissions)
        }

        binding.tvLocationPermission.setOnClickListener {
            val permissions = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                else -> listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
            checkPermission(permissions)
        }

        binding.tvMultiplePermission.setOnClickListener {
            requestMultiplePermissions()
        }

        binding.tvReadAndWritePermission.setOnClickListener {
            val permissions = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> listOf(
                    Manifest.permission.READ_MEDIA_IMAGES
                )
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> listOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                )
                else -> listOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }

            checkPermission(permissions)
        }
    }

    private fun requestMultiplePermissions() {
        val permissions = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
            else -> listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

        checkPermission(permissions)

    }


    private fun checkPermission(permissions: List<String>){
        Dexter.withContext(this)
            .withPermissions(permissions)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            Log.d("Permission", "PermissionGranted")
                        }
                        if (report.isAnyPermissionPermanentlyDenied) {
                            Log.d("Permission", "isAnyPermissionPermanentlyDenied")
                            permissionDialog()
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

    private fun permissionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.dialogTitle)
        builder.setMessage(R.string.dialogMessage)
        builder.setIcon(android.R.drawable.ic_secure)

        builder.setPositiveButton("Yes") { dialogInterface, which ->
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", this.packageName, null)
                intent.data = uri
                ContextCompat.startActivity(this, intent, null)

            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }

        builder.setNegativeButton("No") { _,_ ->

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}
