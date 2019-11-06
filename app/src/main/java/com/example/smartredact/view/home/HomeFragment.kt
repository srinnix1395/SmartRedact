package com.example.smartredact.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smartredact.R
import com.example.smartredact.view.editor.EditorActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        btnOpenLastEditSession.setOnClickListener {
            Toast.makeText(this.context, this.context!!.getString(R.string.open_last_edit_session), Toast.LENGTH_SHORT).show()
        }
        btnAllPreviousSessions.setOnClickListener {
            Toast.makeText(this.context, this.context!!.getString(R.string.all_previous_sessions), Toast.LENGTH_SHORT).show()
        }
        btnRecordANewVideo.setOnClickListener {
            Toast.makeText(this.context, this.context!!.getString(R.string.record_a_new_video), Toast.LENGTH_SHORT).show()
        }
        btnTakeAPicture.setOnClickListener {
            Toast.makeText(this.context, this.context!!.getString(R.string.take_a_picture), Toast.LENGTH_SHORT).show()
        }
        btnLoadMediaFromLocal.setOnClickListener {
            val intent = Intent(this.activity, EditorActivity::class.java)
            startActivity(intent)
        }
    }
}