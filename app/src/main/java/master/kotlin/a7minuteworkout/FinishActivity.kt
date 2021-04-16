package master.kotlin.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.Toolbar

class FinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)
        val toolbarFinishActivity = findViewById<Toolbar>(R.id.toolbar_finish_activity)
        setSupportActionBar(toolbarFinishActivity)
        val actionbar = supportActionBar
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

        toolbarFinishActivity.setNavigationOnClickListener {
            onBackPressed()
        }

        val btnFinish:Button = findViewById(R.id.btnFinish)
        btnFinish.setOnClickListener {
            finish()
        }
    }
}