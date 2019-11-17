package wla.googoodolls.specialcalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.toolbar.*
import wla.googoodolls.specialcalculator.navfragment.ThreeDFragment
import wla.googoodolls.specialcalculator.navfragment.TwoDFragment

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        loadFragment(TwoDFragment())
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.twod -> {
                loadFragment(TwoDFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.threed -> {
                loadFragment(ThreeDFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragmentContainer, fragment)
                .commit()
            return true
        }
        return false
    }
}
