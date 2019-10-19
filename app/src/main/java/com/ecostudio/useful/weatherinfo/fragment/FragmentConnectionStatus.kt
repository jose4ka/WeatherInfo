package com.ecostudio.useful.weatherinfo.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ecostudio.useful.weatherinfo.R
import com.ecostudio.useful.weatherinfo.fragment.connection.ConnectionStatus
import com.ecostudio.useful.weatherinfo.fragment.connection.FragmentWeatherMainInfo
import kotlinx.android.synthetic.main.fragment_connection_status.*
import kotlinx.android.synthetic.main.fragment_connection_status.view.*
import java.lang.Exception
import kotlin.concurrent.thread

class FragmentConnectionStatus:Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_connection_status, container, false)

        var status = arguments?.getSerializable("connection_status") as ConnectionStatus

        if(status != null){
            selectView(view, status)
        }

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


    /*
    * TODO: Make resources strings*/
    private fun selectView(view: View, status: ConnectionStatus){
        when(status){
            ConnectionStatus.IS_CONNECTED -> {
                showSwitcher(true, view)
                view.tvConnectionStatus.text= "Successful loaded!"
            }
            ConnectionStatus.IS_NOT_CONNECTED -> {
                view.tvConnectionStatus.text = "Can't download data..."
                view.statusSwitcher.showNext()
            showSwitcher(true, view)
            }
            ConnectionStatus.LOADING -> {
                view.tvConnectionStatus.text = "Loading..."
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






}