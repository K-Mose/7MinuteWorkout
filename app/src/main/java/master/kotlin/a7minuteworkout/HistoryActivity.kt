package master.kotlin.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
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
        val adapter = HistoryItemAdapter(this@HistoryActivity, getAllCompletedDates())
        binding.apply {
            rvHistory.layoutManager = LinearLayoutManager(this@HistoryActivity)
            rvHistory.adapter = adapter
            Log.d("ItemSize::", "${adapter.itemCount}")

            rvHistory.visibility = if(adapter.itemCount > 0) View.VISIBLE else View.GONE
            lyIncludeNoData.llNoData.visibility = if(adapter.itemCount == 0) View.VISIBLE else View.GONE

            toolbarHistoryActivity.setNavigationOnClickListener { onBackPressed() }

        }


    }
    private fun getAllCompletedDates(): ArrayList<String>{
        val dbHandler = SqliteOpenHelper(this, null)
        val allCompletedDatesList = dbHandler.getAllCompletedDatesList()

        for( i in allCompletedDatesList){
            Log.i("Date:: ", "$i")
        }
        return allCompletedDatesList
    }
}