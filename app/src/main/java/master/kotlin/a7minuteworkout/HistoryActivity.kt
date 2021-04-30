package master.kotlin.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import master.kotlin.a7minuteworkout.databinding.ActivityHistroyBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistroyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistroyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarHistoryActivity)
        val actionBar = supportActionBar
        actionBar?.apply{
            actionBar.setDisplayHomeAsUpEnabled(true)
            title = "History"
        }
        binding.run {
            toolbarHistoryActivity.setNavigationOnClickListener { onBackPressed() }

        }
    }
}