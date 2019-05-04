package com.example.reezkyillma.projectandroidlatihanfinal

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*

class FullCostType : AppCompatActivity() {


    val PICK_IMAGE_REQUEST = 71
    val PERMISSION_REQUEST_CODE = 1001
    var value = 0.0
    lateinit var image : ImageView
    lateinit var post : Button
    lateinit var filepath : Uri
    lateinit var storage : FirebaseStorage
    lateinit var storageReference: StorageReference

    @RequiresApi(Build.VERSION_CODES.M)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_cost_type)

        image = findViewById(R.id.image)
        post = findViewById(R.id.post_fullcosttype)

        storage = FirebaseStorage.getInstance()

        storageReference = storage.reference

        post.setOnClickListener {
            uploadFile()
        }

        image.setOnClickListener {
            when {
                (Build.VERSION.SDK_INT >= Build
                    .VERSION_CODES.M) -> {
                    if (ContextCompat.checkSelfPermission(
                            this@FullCostType, Manifest
                                .permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(arrayOf(
                            Manifest.permission
                                .READ_EXTERNAL_STORAGE),
                            PERMISSION_REQUEST_CODE)
                    }else{
                        chooseImage()
                    }
                }
                else -> chooseImage()
            }
        }
    }

    private fun chooseImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(intent,
                "Select Picture"),
            PICK_IMAGE_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0]
                    == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this@FullCostType,
                        "Oops! Permission Denied!!"
                        , Toast.LENGTH_SHORT).show()
                else
                    chooseImage()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null && data.data !=null) {
            filepath = data.data

            try {
                var bitmap : Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filepath)
                image.setImageBitmap(bitmap)
            }catch (e : IOException){
                e.printStackTrace()
            }
        }

    }

    private fun uploadFile() {
        val progress = ProgressDialog(this)
            .apply {
                setTitle("Uploading Picture....")
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                show()
            }
        var ref : StorageReference = storageReference.child("FullCostType/"
                + UUID.randomUUID().toString())
        ref.putFile(filepath)
            .addOnSuccessListener {
                    taskSnapshot -> progress.dismiss()
                Toast.makeText(this@FullCostType, "Uploaded", Toast.LENGTH_SHORT).show();
            }.addOnFailureListener{
                    e -> progress.dismiss()
                Toast.makeText(this@FullCostType, "Failed" + e.message, Toast.LENGTH_SHORT).show();
            }.addOnProgressListener {
                    taskSnapshot ->
                value = (100.0 * taskSnapshot
                    .getBytesTransferred()/ taskSnapshot
                    .getTotalByteCount())

                progress.setMessage("Uploaded.."
                        + value.toInt() + "%")
            }
    }
}
