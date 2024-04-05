package com.example.banbun_kotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Classify.newInstance] factory method to
 * create an instance of this fragment.
 */
class Classify : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var imageView: ImageView
    private lateinit var cameraButton: Button
    private lateinit var galleryButton: Button
    private lateinit var detectButton: Button

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
        val view = inflater.inflate(R.layout.fragment_classifier, container, false)

        imageView = view.findViewById(R.id.image)
        cameraButton = view.findViewById(R.id.camera)
        galleryButton = view.findViewById(R.id.gallery)
        detectButton = view.findViewById(R.id.detect)

        cameraButton.setOnClickListener {
            cameraButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.pressed_color))
            dispatchTakePictureIntent()
            resetButtonColorDelayed(cameraButton, DELAY_MILLISECONDS)
        }

        galleryButton.setOnClickListener {
            galleryButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.pressed_color))
            openGallery()
            resetButtonColorDelayed(galleryButton, DELAY_MILLISECONDS)
        }

        detectButton.setOnClickListener {
            detectButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.pressed_color))
            resetButtonColorDelayed(detectButton, DELAY_MILLISECONDS)
        }

        val detectButton = view.findViewById<Button>(R.id.detect)
        detectButton.setOnClickListener {
            val intent = Intent(requireContext(), Results::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun openGallery() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(pickPhoto, REQUEST_IMAGE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val extras = data?.extras
                    val imageBitmap = extras?.get("data") as Bitmap
                    imageView.setImageBitmap(imageBitmap)
                }
                REQUEST_IMAGE_GALLERY -> {
                    val selectedImage: Uri? = data?.data
                    imageView.setImageURI(selectedImage)
                }
            }
        }
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
         * @return A new instance of fragment Classify.
         */
        // TODO: Rename and change types and number of parameters

        private const val REQUEST_IMAGE_CAPTURE = 101
        private const val REQUEST_IMAGE_GALLERY = 102
        private const val DELAY_MILLISECONDS = 1000L // Adjust the delay in milliseconds here

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Classify().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}