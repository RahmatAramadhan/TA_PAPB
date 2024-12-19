package com.example.ta_papb

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Forum : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum)

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
}