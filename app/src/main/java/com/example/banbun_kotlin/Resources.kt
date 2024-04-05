package com.example.banbun_kotlin

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Resources.newInstance] factory method to
 * create an instance of this fragment.
 */
class Resources : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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

        val view = inflater.inflate(R.layout.fragment_resources, container, false)

        val bunchyButton = view.findViewById<Button>(R.id.button1)
        bunchyButton.setOnClickListener {
            bunchyButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.pressed_color))
            resetButtonColorDelayed(bunchyButton, DELAY_MILLISECONDS)
            val intent = Intent(requireContext(), BBTV::class.java)
            startActivity(intent)
        }

        val elisaButton = view.findViewById<Button>(R.id.button2)
        elisaButton.setOnClickListener {
            elisaButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.pressed_color))
            resetButtonColorDelayed(elisaButton, DELAY_MILLISECONDS)
            val intent = Intent(requireContext(), ELISA::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun resetButtonColorDelayed(button: Button, delayMilliseconds: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(delayMilliseconds)
            button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.yellow))
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Resources.
         */
        // TODO: Rename and change types and number of parameters

        private const val DELAY_MILLISECONDS = 1000L // Adjust the delay in milliseconds here

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Resources().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}