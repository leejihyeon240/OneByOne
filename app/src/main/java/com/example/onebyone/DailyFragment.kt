package com.example.onebyone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_daily.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DailyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DailyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var recyclerView1 : RecyclerView
    var list = arrayListOf<DailyItem>(
        DailyItem(R.drawable.daily_label_food, "풀무원국물떡볶이2인", "8,900"),
        DailyItem(R.drawable.daily_label_food, "야채류", "2,000"),
        DailyItem(R.drawable.daily_label_drink, "카스캔 355ml*6", "8,900"),
        DailyItem(R.drawable.daily_label_food, "등 / 초밥의 달인", "3,000"),
        DailyItem(R.drawable.daily_label_necessity, "비닐봉투", "100"),
        DailyItem(R.drawable.daily_label_necessity_green, "캐시백", "2,000"),

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_daily, container, false)

        recyclerView1 = rootView.findViewById(R.id.recycler!!)as RecyclerView
        recyclerView1.layoutManager = LinearLayoutManager(requireContext())
        recyclerView1.adapter = DailyRecyclerAdapter(requireContext(),list)

        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DailyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DailyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}