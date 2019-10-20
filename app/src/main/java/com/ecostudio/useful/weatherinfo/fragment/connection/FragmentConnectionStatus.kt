package com.ecostudio.useful.weatherinfo.fragment.connection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.ecostudio.useful.weatherinfo.R
import kotlinx.android.synthetic.main.fragment_connection_status.view.*

class FragmentConnectionStatus:Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_connection_status, container, false)

        checkBundle(view)
        setScaleAnimation(view.connectionLayout)

        return view
    }

    //Like constructor
    companion object {
        @JvmStatic
        fun newInstance(status: ConnectionStatus) = FragmentConnectionStatus().apply {
            arguments = Bundle().apply {
                putSerializable("connection_status", status)
            }
        }
    }


    private fun checkBundle(view: View){
        var status = arguments?.getSerializable("connection_status") as ConnectionStatus

        if(status != null){
            selectView(view, status)
        }
    }

    private fun selectView(view: View, status: ConnectionStatus){
        when(status){
            ConnectionStatus.IS_CONNECTED -> {
                showSwitcher(true, view)
                view.tvConnectionStatus.text= resources.getString(R.string.loading_successful)
                view.tvConnectionStatus.setTextColor(resources.getColor(R.color.colorGreen))
            }
            ConnectionStatus.IS_NOT_CONNECTED -> {
                view.tvConnectionStatus.text = resources.getString(R.string.loading_failed)
                view.tvConnectionStatus.setTextColor(resources.getColor(R.color.colorRed))
                view.statusSwitcher.showNext()
            showSwitcher(true, view)
            }
            ConnectionStatus.LOADING -> {
                view.tvConnectionStatus.text = resources.getString(R.string.loading)
                view.tvConnectionStatus.setTextColor(resources.getColor(R.color.colorText))
                showProgressBar(true, view)}
            ConnectionStatus.GONE -> {
                view.connectionLayout.visibility = View.GONE

            }
        }
    }



    private fun showProgressBar(isShow:Boolean, view: View){
        if (isShow){view.pbNetwork.visibility = View.VISIBLE}
        else {view.pbNetwork.visibility = View.GONE}
    }


    private fun showSwitcher(isShow: Boolean, view: View){
        if(isShow){view.statusSwitcher.visibility = View.VISIBLE}
        else{view.statusSwitcher.visibility = View.GONE}
    }


    private fun setScaleAnimation(view: View){
        var anim:Animation = AnimationUtils.loadAnimation(context, R.anim.animation_scale)
        view.startAnimation(anim)
    }



}