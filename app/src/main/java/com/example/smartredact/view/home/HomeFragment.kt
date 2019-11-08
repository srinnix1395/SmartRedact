package com.example.smartredact.view.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smartredact.R
import com.example.smartredact.common.constants.Constants
import com.example.smartredact.common.utils.replaceFragment
import com.example.smartredact.view.editor.EditorFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
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
            Toast.makeText(
                this.context,
                this.context!!.getString(R.string.open_last_edit_session),
                Toast.LENGTH_SHORT
            ).show()
        }
        btnAllPreviousSessions.setOnClickListener {
            Toast.makeText(
                this.context,
                this.context!!.getString(R.string.all_previous_sessions),
                Toast.LENGTH_SHORT
            ).show()
        }
        btnRecordANewVideo.setOnClickListener {
            Toast.makeText(
                this.context,
                this.context!!.getString(R.string.record_a_new_video),
                Toast.LENGTH_SHORT
            ).show()
        }
        btnTakeAPicture.setOnClickListener {
            Toast.makeText(
                this.context,
                this.context!!.getString(R.string.take_a_picture),
                Toast.LENGTH_SHORT
            ).show()
        }
        btnLoadMediaFromLocal.setOnClickListener {
            loadMediaFromLocal()
        }
    }

    private fun loadMediaFromLocal() {
        activity?.supportFragmentManager?.replaceFragment(EditorFragment(), null, EditorFragment::class.java.simpleName)

//        chooseFiles()
    }

    private fun chooseFiles() {
        fun openIntentGetFile() {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "video/*"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(
                Intent.createChooser(intent, "Choose a file"),
                EditorFragment.REQUEST_CODE_SELECT_FILE
            )
        }
        openIntentGetFile()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null || resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            EditorFragment.REQUEST_CODE_SELECT_FILE -> {
                val bundle = Bundle()
                bundle.putParcelable(Constants.BUNDLE, data.data)
                activity?.supportFragmentManager?.replaceFragment(EditorFragment(), bundle, EditorFragment::class.java.simpleName)
            }
        }
    }


}