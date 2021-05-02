package master.kotlin.a7minuteworkout

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import master.kotlin.a7minuteworkout.databinding.ItemExerciseStatusBinding

/*
onCreateViewHolder나 onBindViewHolder는 각 뷰 하나하나
즉, RecylcerView에 사용되는 layout 한줄한줄에 해당한다.
 */
// 참고 https://wooooooak.github.io/android/2019/03/28/recycler_view/
// https://developer.android.com/guide/topics/ui/layout/recyclerview#implement-adapter
// Adapter : 리스트를 리사이클러 뷰에 바인딩 시켜주는 객체
// LayoutManager : Layout 결정하는 객체?
class ExerciseStatusAdapter(val items:ArrayList<ExerciseModel>, val context:Context) : RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

    // 두 번째로 실행되는 함수
    // ViewHolder가 여기에서 ViewHolder 생성자에 view 객체를 넘겨준다.
    // 화면에 보여지는 View의 갯수보다 +a 만큼 생성시킨다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("ViewHolder::","ViewHolderCreated")
        return ViewHolder(ItemExerciseStatusBinding.inflate(LayoutInflater.from(context), parent, false))
        // viewHolder는 layout(view)을 인자로 받아서 기억하고, 사용될 떄 return 한다.
        // LayoutInflater : view를 생성하는 방법. static으로 정의된 from에 context를 넣어서
        // 특정 xml 파일을 계층적 구조로 inflate 하는 inflate 함수를 kotlin extension 방법으로 호출
    }

    // ViewHolder에 데이터를 바인딩 해주는 함수
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("Binder::","$position")
        val model: ExerciseModel = items[position]
        holder.binding.apply {
            tvItem.text = model.getId().toString()
            when {
                model.getIsSelected() -> {
                    tvItem.background = ContextCompat.getDrawable(context, R.drawable.item_circular_thin_color_accent_border)
                    tvItem.setTextColor(Color.parseColor("#212121"))
                }
                model.getIsCompleted() -> {
                    tvItem.background = ContextCompat.getDrawable(context, R.drawable.item_circular_color_accent_background)
                    tvItem.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else -> {
                    tvItem.background = ContextCompat.getDrawable(context, R.drawable.item_circular_color_gray)
                    tvItem.setTextColor(Color.parseColor("#212121"))
                }
            }
        }

    }

    // 가장 먼저 실행되는 함수, 뿌려줄 데이터의 전체 길이 리턴
    override fun getItemCount(): Int {
        Log.d("ItemCount::", " ItemCount 호출 / ${items.size}")
        return items.size
    }

    // ViewBinding으로 변경
    class ViewHolder(val binding: ItemExerciseStatusBinding):RecyclerView.ViewHolder(binding.root)
}