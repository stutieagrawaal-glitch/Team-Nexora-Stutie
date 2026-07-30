package nexora.punya.sahara

import android.app.Application
import com.google.firebase.FirebaseApp

class SaharaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Explicitly initialize Firebase
        FirebaseApp.initializeApp(this)
    }
}
