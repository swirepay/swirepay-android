package com.swirepay.android_sdk.ui.base

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.http.SslError
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
//import androidx.core.app.ActivityCompat.TAG
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import com.swirepay.android_sdk.Configuration
import com.swirepay.android_sdk.Utility
import com.swirepay.android_sdk.databinding.ActivityPaymentBinding
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity
//import com.swirepay.swirepay_sdk_playground.MainActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


//import com.swirepay.swirepay_sdk_playground.MainActivity


abstract class BaseActivity : AppCompatActivity() {


    val binding: ActivityPaymentBinding by lazy { ActivityPaymentBinding.inflate(layoutInflater) }
    abstract val param_id: String
    var result_id = ""

//    var webView: WebView? = null
    private val TAG: String = BaseActivity::class.java.getSimpleName()
    private var mCM: String? = null
    private var mUM: ValueCallback<Uri>? = null
    private var mUMA: ValueCallback<Array<Uri>>? = null
    private val FCR = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    abstract fun onRedirect(url: String?)
    @SuppressLint("SetJavaScriptEnabled")
    fun loadUrl(url: String) {
        binding.webView.clearCache(true)
        binding.webView.clearHistory()
        binding.progress.visibility = View.VISIBLE
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.requestFocusFromTouch()
        binding.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

        val webSettings: WebSettings = binding.webView.settings
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        webSettings.useWideViewPort = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        Log.d("sdk_test", "loadUrl: $url")
        binding.webView.loadUrl(url)
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val localUrl = request?.url.toString()
                Log.d("sdk_test", "shouldOverrideUrlLoading: $localUrl")
                val uri = request?.url
                val id = uri?.getQueryParameter(param_id)
                if (id != null) {
                    result_id = id
                }
                if (isThisFinalUrl(localUrl)) {
                    onRedirect(localUrl)
                    return true
                }

                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                Log.d(PaymentActivity.TAG, "onReceivedError: $error")
            }

//            override fun onReceivedError(
//                view: WebView?,
//                request: WebResourceRequest?,
//                error: WebResourceError?
//            ) {
//                Log.d(PaymentActivity.TAG, "onReceivedError: ${error.toString()}")
//                super.onReceivedError(view, request, error)
//            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                Log.d(PaymentActivity.TAG, "onReceivedErrorssl: ${error.toString()}")
                super.onReceivedSslError(view, handler, error)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progress.visibility = View.GONE
                Log.d(PaymentActivity.TAG, "onPageFinished: ")
            }
        }


        //===============

        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) !== PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                1
            )
        }

        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true

        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.mixedContentMode = 0
            binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else if (Build.VERSION.SDK_INT >= 19) {
            binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else if (Build.VERSION.SDK_INT < 19) {
            binding.webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
//        binding.webView.setWebViewClient(Callback())
//        binding.webView.loadUrl("https://infeeds.com/")
        binding.webView.setWebChromeClient(object : WebChromeClient() {
            //For Android 3.0+
            fun openFileChooser(uploadMsg: ValueCallback<Uri>?) {
                mUM = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "*/*"
                startActivityForResult(
                    Intent.createChooser(i, "File Chooser"),
                    FCR
                )
            }

            // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
            fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String?) {
                mUM = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "*/*"
                startActivityForResult(
                    Intent.createChooser(i, "File Browser"),
                    FCR
                )
            }

            //For Android 4.1+
            fun openFileChooser(
                uploadMsg: ValueCallback<Uri>?,
                acceptType: String?,
                capture: String?
            ) {
                mUM = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "*/*"
                startActivityForResult(
                    Intent.createChooser(i, "File Chooser"),
                    FCR
                )
            }

            //For Android 5.0+
            override fun onShowFileChooser(
                webView: WebView, filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                mUMA?.onReceiveValue(null)
                mUMA = filePathCallback
                var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent!!.resolveActivity(getPackageManager()) != null) {
                    var photoFile: File? = null
                    try {
                        photoFile = createImageFile()
                        takePictureIntent.putExtra("PhotoPath", mCM)
                    } catch (ex: IOException) {
                        Log.e(TAG, "Image file creation failed", ex)
                    }
                    if (photoFile != null) {
                        mCM = "file:" + photoFile.getAbsolutePath()
                        takePictureIntent!!.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile)
                        )
                    } else {
                        takePictureIntent = null
                    }
                }
                val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                contentSelectionIntent.type = "*/*"
                val intentArray: Array<Intent?>
                intentArray = takePictureIntent?.let { arrayOf(it) } ?: arrayOfNulls(0)
                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                startActivityForResult(chooserIntent, FCR)
                return true
            }
        })

    }

    //

    class Callback : WebViewClient() {
        override fun onReceivedError(
            view: WebView,
            errorCode: Int,
            description: String,
            failingUrl: String
        ) {
//            Toast.makeText(this.getApplicationContext(), "Failed loading app!", Toast.LENGTH_SHORT).show()

            Log.e("====Tag","Failed loading app!")

        }
    }

    // Create an image file
    @Throws(IOException::class)
    open fun createImageFile(): File? {
        @SuppressLint("SimpleDateFormat") val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "img_" + timeStamp + "_"
        val storageDir: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    ///
    companion object {
        fun isThisFinalUrl(url: String?): Boolean {
            println("url====")
            println(url)
            if (url != null && url.contains(Utility.baseUrl)) {
                return true
            }
            return false
        }

    }

    var backPressedTime: Long = 0
    override fun onBackPressed() {
        super.onBackPressed()
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            setResult(RESULT_CANCELED, Intent().apply {
                putExtra(
                    PaymentActivity.FAILURE_REASON,
                    PaymentActivity.FAILURE_REASON_USER_CANCELLED
                )
            })
            finish()
        } else {
            backPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "Click back again to exit!", Toast.LENGTH_LONG).show()
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (Build.VERSION.SDK_INT >= 21) {
            var results: Array<Uri>? = null
            //Check if response is positive
            if (resultCode == RESULT_OK) {
                if (requestCode == FCR) {
                    if (null == mUMA) {
                        return
                    }
                    if (intent == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {
                            results = arrayOf<Uri>(Uri.parse(mCM))
                        }
                    } else {
                        val dataString = intent.dataString
                        if (dataString != null) {
                            results = arrayOf(Uri.parse(dataString))
                        }
                    }
                }
            }
            mUMA?.onReceiveValue(results)
            mUMA = null
        } else {
            if (requestCode == FCR) {
                if (null == mUM) return
                val result = if (intent == null || resultCode != RESULT_OK) null else intent.data
                mUM!!.onReceiveValue(result)
                mUM = null
            }
        }
    }


    override fun onKeyDown(keyCode: Int, @NonNull event: KeyEvent): Boolean {
        if (event.getAction() === KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (binding.webView.canGoBack()) {
                        binding.webView.goBack()
                    } else {
                        finish()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
    }

}