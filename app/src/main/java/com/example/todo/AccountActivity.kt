package com.example.todo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.databinding.ActivityAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class AccountActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAccountBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri :Uri
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null && data.data != null) {
                    imageUri = data.data!!
                    binding.profileImage.setImageURI(imageUri)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("user")

        if (uid != null) {
            databaseReference.child(uid).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    // Retrieve user data from snapshot
                    val userData = snapshot.getValue(UserData::class.java)

                    // Update UI with user data
                    userData?.let {
                        binding.etFirstName.setText(it.firstName)
                        binding.etLastName.setText(it.lastName)
                        binding.etBio.setText(it.bio)
                    }
                } else {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to fetch user data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.editImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImage.launch(intent)
        }

        binding.saveBtn.setOnClickListener{
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val bio = binding.etBio.text.toString()

            val user = UserData(firstName,lastName,bio)
            if (uid != null){
                databaseReference.child(uid).setValue(user).addOnCompleteListener{
                    if (it.isSuccessful){
                        Toast.makeText(this@AccountActivity, "Profile update Successful", Toast.LENGTH_LONG).show()

                    }

                }
            }


        }

    }

    fun uploadProfile() {
        // Make sure imageUri is not null and points to a valid file
        if (imageUri != null) {
            val uid = auth.currentUser?.uid
            if (uid != null) {
                storageReference = FirebaseStorage.getInstance().getReference("Users/$uid/profile_image")

                // Upload the image file to Firebase Storage
                storageReference.putFile(imageUri).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // If upload is successful, show a success message
                        Toast.makeText(this@AccountActivity, "Profile update Successful", Toast.LENGTH_LONG).show()
                    } else {
                        // If upload fails, show an error message
                        Toast.makeText(this@AccountActivity, "Failed to update profile", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                // Handle the case where the user is not authenticated
                Toast.makeText(this@AccountActivity, "User not authenticated", Toast.LENGTH_LONG).show()
            }
        } else {
            // Handle the case where imageUri is null or invalid
            Toast.makeText(this@AccountActivity, "Image not selected or invalid", Toast.LENGTH_LONG).show()
        }
    }






}