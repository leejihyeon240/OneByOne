package com.example.onebyone

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate

class CalendarAdapter2(private val dataSet: ArrayList<com.example.onebyone.Date>) :
    RecyclerView.Adapter<CalendarAdapter2.ViewHolder>() {

    //firebase 1 -----------------------------------/
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mDatabaseRef: DatabaseReference? = null
    /*-----------------------------------------*/

    var drawable: Drawable? = null
    private lateinit var itemClickListener: AdapterView.OnItemClickListener
    val calRecycleEllipse: ImageView? = null

    var pyear: String = ""
    var pmonth: String = ""
    var pdate: String = ""
    var todayPosition: Int = -1
    var pPositionDelete: Int = -1
    var pPosition: Int = -1

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTv: TextView = view.findViewById(R.id.daily_date_cell)
        val dayTv: TextView = view.findViewById(R.id.daily_day_cell)
        val dailyRecycleEllipse: ImageView = view.findViewById(R.id.daily_recycle_ellipse)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {


        //firebase 2 -----------------------------------/
        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("OneByOne")
            .child("UserAccount")
        /*-----------------------------------------*/


        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.calendar_cell2, viewGroup, false)

        drawable = ContextCompat.getDrawable(view.context, R.drawable.cal_ellipse)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dateTv.text = dataSet[position].date
        holder.dayTv.text = dataSet[position].day

        // 오늘 날짜에 초록 표시
        if (dataSet[position].year.toString() == LocalDate.now().year.toString()) {
            if (dataSet[position].month.toString() == LocalDate.now().month.value.toString()) {
                if (dataSet[position].date.toString() == LocalDate.now().dayOfMonth.toString()) {
                    holder.dateTv.setTextColor(Color.parseColor("#309F5D"))
                    holder.dayTv.setTextColor(Color.parseColor("#309F5D"))
                    todayPosition = position
                }
            }
        }


        // 선택한 날짜에만 초록색 원 ㅠㅠㅠㅠ 감격
        if (position == pPosition) {
            holder.dateTv.setTextColor(Color.WHITE)
            holder.dayTv.setTextColor(Color.WHITE)
            holder.dailyRecycleEllipse.visibility = View.VISIBLE
            Log.d("calendar (1)  position", position.toString())
            Log.d("calendar (1)  pPosition", pPosition.toString())
        }
        if (position == pPositionDelete) {
            holder.dateTv.setTextColor(Color.BLACK)
            holder.dayTv.setTextColor(Color.BLACK)
            holder.dailyRecycleEllipse.visibility = View.INVISIBLE
            Log.d("calendar (2  position", position.toString())
            Log.d("calendar (2)  pPosition", pPosition.toString())
            if (position == todayPosition) {
                holder.dateTv.setTextColor(Color.parseColor("#309F5D"))
                holder.dayTv.setTextColor(Color.parseColor("#309F5D"))
            }
        }


        //이렇게 간단한 방법이 있는데 왜 그렇게 복잡한 방법을 사용하는거야??? 이곳 저곳에 코드를 잔뜩 집어 넣고??
        holder.itemView.setOnClickListener {

            Log.d("calendar!!- click", "뷁1")
            Log.d("calendar!!- click", pmonth)
            Log.d("calendar!!- click", pdate)
            Log.d("calendar!!- click", pPosition.toString())

            pPositionDelete = pPosition
            pyear = dataSet[position].year //holder로 가져온 값을 변수에 넣기
            pmonth = dataSet[position].month //holder로 가져온 값을 변수에 넣기
            pdate = dataSet[position].date //holder로 가져온 값을 변수에 넣기
            pPosition = position
            notifyItemChanged(pPositionDelete)
            notifyItemChanged(pPosition)


            // 선택한 날짜에 원형 표시
//            holder.dateTv.setTextColor(Color.WHITE)
//            holder.dayTv.setTextColor(Color.WHITE)
//            holder.dailyRecycleEllipse.visibility = View.VISIBLE


            Log.d("calendar!!- click", "뷁2")
            Log.d("calendar!!- click", pmonth)
            Log.d("calendar!!- click", pdate)
            Log.d("calendar!!- click", pPosition.toString())


            //변수값 인텐트로 넘기기

//            val map = HashMap<String, Any>()

//            map.put("pyear", pyear)
//            map.put("pmonth", pmonth)
//            map.put("pdate", pdate)

            // 날짜 선택 시 효과음 뾱
            val m: MediaPlayer = MediaPlayer.create(holder.itemView?.context, R.raw.sound_pop)
            m.start()
            m.setOnCompletionListener { mp ->
                mp.stop()
                mp.release()
            }

            mDatabaseRef?.child(mFirebaseAuth!!.currentUser!!.uid)!!.child("pyear").setValue(pyear)
            mDatabaseRef?.child(mFirebaseAuth!!.currentUser!!.uid)!!.child("pmonth").setValue(pmonth)
            mDatabaseRef?.child(mFirebaseAuth!!.currentUser!!.uid)!!.child("pdate").setValue(pdate)

//            mDatabaseRef?.child(mFirebaseAuth!!.currentUser!!.uid)?.push()?.setValue(map)


            val pintent2: Intent =
                Intent(holder.itemView?.context, MainActivity::class.java) //look_memo.class부분에 원하는 화면 연결
            pintent2.putExtra("daily_pyear", pyear)
            pintent2.putExtra("daily_pmonth", pmonth)
            pintent2.putExtra("daily_pdate", pdate)
//            ContextCompat.startActivity(holder.itemView?.context, pintent2, null) //액티비티 열기

        }

    }

    override fun getItemCount() = dataSet.size

    // 이거 그거임 그 중복 방지하는거 망할 똥멍청이 구글
    override fun getItemViewType(position: Int): Int {
        return position
    }


    interface list_onClick_interface {

        fun onCheckBox(pdate: Date)

    }





}

