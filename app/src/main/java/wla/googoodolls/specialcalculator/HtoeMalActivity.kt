package wla.googoodolls.specialcalculator

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_htoe_mal.*


class HtoeMalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutPara = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        val layoutParaEdit = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,2f)
        setContentView(R.layout.activity_htoe_mal)
        ibtAdd.setOnClickListener {
            val li = LinearLayout(this)
            li.setBackgroundColor(Color.BLACK)
            li.orientation = LinearLayout.HORIZONTAL

            val et  = EditText(this)
            et.setPadding(10,0,0,0)
            et.setHint("အကွက်")
            li.addView(et,layoutParaEdit)
            con.addView(li,layoutPara)




        }
    }

    private fun newLayout() {


//
//
//        val li = LinearLayout(this)
//        li.setBackgroundColor(Color.BLACK)
//        li.orientation = LinearLayout.HORIZONTAL
//
//        val et  = EditText(this)
//        et.setPadding(10,0,0,0)
//        et.setHint("အကွက်")
//        li.addView(et)
//        liAdd.addView(li,layoutPara)
    }
}
