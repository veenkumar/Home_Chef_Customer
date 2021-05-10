package com.veen.homechef.fragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.veen.homechef.R


class FirstFragment : Fragment() {
    private val SPLASH_TIME_OUT = 500L
  override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Handler().postDelayed(
            {
                this.fragmentManager?.beginTransaction()
                    ?.replace(R.id.nav_host_fragment, MainFragment())
                    ?.commit()
            }, SPLASH_TIME_OUT
        )
    }
}