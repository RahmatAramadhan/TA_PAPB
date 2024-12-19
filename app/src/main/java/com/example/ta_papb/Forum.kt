package com.example.ta_papb

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class Forum : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth

    private lateinit var profilePicture: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        profilePicture = findViewById(R.id.profilePicture)

        loadProfilePicture()

        profilePicture.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }


        val posts = listOf(
            Post("Precious Girl @Emma", "My Boyfriend is so CUTEEEEE<3 @agil", 1906, 1249, 7461),
            Post("Tech Geek @JohnDoe", "Finally built my own gaming PC! #DIY #TechLife", 2300, 1500, 890),
            Post("Nature Lover @GreenHeart", "Look at this beautiful sunset I captured 🌅 #Nature", 1450, 870, 500),
            Post("Foodie @YummyTummy", "Best pasta I've ever made 🍝😋 Recipe link in bio!", 3200, 2100, 1340),
            Post("Fitness Pro @FitLife", "Morning workout done! 💪 Who's ready for the grind? #FitnessMotivation", 2700, 1800, 970),
            Post("Traveler @Wanderlust", "Exploring the streets of Tokyo 🗼✨ #TravelGoals", 3900, 2900, 1800),
            Post("Artist @CreativeSoul", "Just finished this new piece of digital art 🎨 #ArtWork", 2100, 1345, 780),
            Post("Musician @MelodyMaker", "New single out now! 🎶 Give it a listen on Spotify! #NewMusic", 4500, 3400, 2900),
            Post("Movie Buff @FilmFanatic", "Rewatched The Dark Knight. What a masterpiece! 🎥 #BestMovieEver", 1700, 1250, 610),
            Post("Bookworm @ReadMore", "Finished reading 'Atomic Habits' 📚 Highly recommend it!", 1200, 940, 520),
            Post("Gamer @ProPlayer", "Hit Diamond rank in League of Legends 🏆 #GamingLife", 3100, 2400, 1500),
            Post("Fashionista @StyleQueen", "OOTD: Feeling fabulous in this outfit ✨👗 #Fashion", 2500, 2000, 1000),
            Post("Pet Lover @FurBabyMom", "Cuteness overload! My cat just did the funniest thing 🐱❤️ #PetLife", 2900, 2100, 1350),
            Post("Chef @HomeCook", "Experimented with a new dessert recipe 🍨 It’s a hit! #Foodie", 2600, 1900, 1020),
            Post("Student @StudyGuru", "Passed my exams with flying colors! 📚 #HardWorkPaysOff", 1800, 1400, 700)
        )

        val recyclerView: RecyclerView = findViewById(R.id.rvPosts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PostAdapter(posts)
    }



    private fun loadProfilePicture() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "Pengguna tidak terautentikasi", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val photoPath = documentSnapshot.getString("photoPath")

                    if (!photoPath.isNullOrEmpty()) {
                        val photoRef: StorageReference = storage.getReference(photoPath)
                        photoRef.downloadUrl.addOnSuccessListener { uri ->
                            Picasso.get().load(uri).into(profilePicture)
                        }.addOnFailureListener {
                            Toast.makeText(this, "Gagal memuat foto profil", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal mengambil data pengguna: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}