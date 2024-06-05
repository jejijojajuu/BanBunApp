import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.banbun_kotlin.R
import com.example.banbun_kotlin.Results
//import com.example.banbun_kotlin.ml.MobilenetV110224Quant
import com.example.banbun_kotlin.ml.Model
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val REQUEST_IMAGE_CAPTURE = 101
private const val REQUEST_IMAGE_GALLERY = 102
private const val DELAY_MILLISECONDS = 1000L

class Classify : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var imageView: ImageView
    private lateinit var cameraButton: Button
    private lateinit var galleryButton: Button
    private lateinit var detectButton: Button
//    private lateinit var bitmap: Bitmap

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
        val view = inflater.inflate(R.layout.fragment_classify, container, false)

        imageView = view.findViewById(R.id.image)
        cameraButton = view.findViewById(R.id.camera)
        galleryButton = view.findViewById(R.id.gallery)
        detectButton = view.findViewById(R.id.detect)

        var imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        cameraButton.setOnClickListener {
            cameraButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.pressed_color))
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE)
            }
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

//            var tensorImage = TensorImage(DataType.UINT8)
//            tensorImage.load(bitmap)
//
//            tensorImage = imageProcessor.process(tensorImage)
//
//            val model = MobilenetV110224Quant.newInstance(requireContext())
//
//            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
//            inputFeature0.loadBuffer(tensorImage.buffer)
//
//            val outputs = model.process(inputFeature0)
//            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
//
//            var maxIdx = 0
//
//
//            model.close()
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
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ).apply {
            type = "image/*"
        }
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
                    view?.findViewById<TextView>(R.id.default_message)?.text = "Image captured from camera is set."
                }
                REQUEST_IMAGE_GALLERY -> {
                    val selectedImage: Uri? = data?.data
                    imageView.setImageURI(selectedImage)
                    view?.findViewById<TextView>(R.id.default_message)?.text = "Chosen image from gallery is set."
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
