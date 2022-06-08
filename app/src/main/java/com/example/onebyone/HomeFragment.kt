package com.example.onebyone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    //firebase 1 -----------------------------------/
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mDatabaseRef: DatabaseReference? = null
    /*-----------------------------------------*/

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
        val rootView: View = inflater.inflate(R.layout.fragment_home, container, false) as ViewGroup

        var home_loading = rootView.findViewById(R.id.home_loading) as ImageView
        home_loading.visibility=View.VISIBLE
        Log.d("time1",System.currentTimeMillis().toString())



        //firebase 2 -----------------------------------/
        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("OneByOne")
            .child("UserAccount")
        /*-----------------------------------------*/


        // 2022년 06월
        var home_status_text1 = rootView.findViewById(R.id.home_status_text1) as TextView
        home_status_text1.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월")).toString())

        // 황오복님 지갑 현황입니다
        // home_inputtext, home_outputtext, home_totaltext
        var home_status_text2 = rootView.findViewById(R.id.home_status_text2) as TextView
        var home_inputtext = rootView.findViewById(R.id.home_inputtext) as TextView
        var home_outputtext = rootView.findViewById(R.id.home_outputtext) as TextView
        var home_totaltext = rootView.findViewById(R.id.home_totaltext) as TextView
        mDatabaseRef!!.child(mFirebaseAuth!!.currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //firebase 3 -----------------------------------/
                    val user = snapshot.getValue(UserAccount::class.java)
                    /*-----------------------------------------*/

                    home_status_text2.setText(java.lang.String.valueOf(user?.getName())+"님 지갑 현황입니다")
                    home_inputtext.setText(java.lang.String.valueOf(user?.getInput()))
                    home_outputtext.setText(java.lang.String.valueOf(user?.getOutput()))
                    home_totaltext.setText(java.lang.String.valueOf((user?.getTotal())))

                    Log.d("time3",System.currentTimeMillis().toString())
                    home_loading.visibility=View.GONE

                }

                override fun onCancelled(error: DatabaseError) {}
            })


        var home_box_camera = rootView.findViewById(R.id.home_box_camera) as ImageView
        var home_box_self = rootView.findViewById(R.id.home_box_self) as ImageView

        home_box_camera.setOnClickListener {
            var intent : Intent = Intent(context,CameraActivity::class.java)
            startActivity(intent)
        }

        home_box_self.setOnClickListener {
            var intent : Intent = Intent(context,SelfAddActivity::class.java)
            startActivity(intent)
        }

        return rootView

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}