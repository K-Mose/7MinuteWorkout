package master.kotlin.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import master.kotlin.a7minuteworkout.databinding.ActivityFinishBinding
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityFinishBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarFinishActivity)
        val actionbar = supportActionBar
        actionbar?.apply{
            setDisplayHomeAsUpEnabled(true)
        }
        addDateToDatabase()
        binding.toolbarFinishActivity.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnFinish.setOnClickListener {
            finish()
        }
    }
    private fun addDateToDatabase(){
        val calendar = Calendar.getInstance()
        val dateTime = calendar.time
        Log.i("DATE:: ","$dateTime")

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN)
        val date = sdf.format(dateTime)

        val dbHandler = SqliteOpenHelper(this@FinishActivity, null)
        dbHandler.addDate(date)
        Log.i("DATE:: ", "Added - ${date.toString()}")
    }

}