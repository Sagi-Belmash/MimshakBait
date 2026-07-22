package com.example.memshakbait

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Outline
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import android.view.animation.DecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.sidzadaun.liquidglass.LiquidGlassView
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class EnterHouseholdActivity : BaseActivity() {

    private lateinit var enter_TEXT_welcome: MaterialTextView
    private lateinit var enter_TEXT_name: MaterialTextView

    private lateinit var enter_CARD_banner: MaterialCardView
    private lateinit var enter_LAYOUT_enter: View
    private lateinit var enter_LAYOUT_join: View

    private lateinit var enter_TEXT_lets_start: MaterialTextView
    private lateinit var enter_BTN_join: MaterialButton
    private lateinit var enter_BTN_create: MaterialButton
    private lateinit var enter_BTN_logout: MaterialButton
    private lateinit var enter_GLASS_join: LiquidGlassView
    private lateinit var enter_GLASS_create: LiquidGlassView

    private lateinit var enter_PREVIEW_qr: PreviewView
    private lateinit var enter_IMG_qr: AppCompatImageView
    private lateinit var cameraExecutor: ExecutorService

    private var hasScanned = false

    private enum class Gender {
        Male, Female, Other
    }

    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            startQrScanner()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_enter_household)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        initViews()
    }

    private fun findViews() {
        enter_TEXT_welcome = findViewById(R.id.enter_TEXT_welcome)
        enter_TEXT_name = findViewById(R.id.enter_TEXT_name)

        enter_CARD_banner = findViewById(R.id.enter_CARD_banner)
        enter_LAYOUT_enter = findViewById(R.id.enter_LAYOUT_enter)
        enter_LAYOUT_join = findViewById(R.id.enter_LAYOUT_join)

        enter_TEXT_lets_start = findViewById(R.id.enter_TEXT_lets_start)
        enter_BTN_join = findViewById(R.id.enter_BTN_join)
        enter_BTN_create = findViewById(R.id.enter_BTN_create)
        enter_BTN_logout = findViewById(R.id.enter_BTN_logout)
        enter_GLASS_join = findViewById(R.id.enter_GLASS_join)
        enter_GLASS_create = findViewById(R.id.enter_GLASS_create)

        enter_PREVIEW_qr = findViewById(R.id.enter_PREVIEW_qr)
        enter_IMG_qr = findViewById(R.id.enter_IMG_qr)
    }

    private fun initViews() {
        // TODO(Get name and gender values from db)
        setGender(Gender.Male)
        enter_TEXT_name.setText("דדי דדון")

        enter_LAYOUT_enter.doOnLayout {
            enter_CARD_banner.translationY =
                enter_CARD_banner.height.toFloat() - it.height.toFloat()
        }

        // --- Enter Household Banner ---
        enter_BTN_join.setOnClickListener { joinHousehold() }
        enter_BTN_create.setOnClickListener { createHousehold() }
        enter_BTN_logout.setOnClickListener { logout() }
        enter_GLASS_join.setupWithActivityRoot()
        enter_GLASS_create.setupWithActivityRoot()

        // --- Join Household Banner ---
        enter_PREVIEW_qr.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, 100f)
            }
        }
        enter_PREVIEW_qr.clipToOutline = true
        enter_IMG_qr.doOnLayout {
            val params = it.layoutParams
            params.height = it.width
            it.layoutParams = params
        }
    }

    private fun transitionBanner(from: View, to: View) {
        from.animate()
            .alpha(0f)
            .setDuration(200L)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    from.visibility = View.GONE

                    to.alpha = 0f
                    to.visibility = View.VISIBLE
                    to.animate()
                        .alpha(1f)
                        .setDuration(200L)
                        .start()

                    to.doOnLayout {
                        val targetY = enter_CARD_banner.height.toFloat() - it.height.toFloat()

                        enter_CARD_banner.animate()
                            .translationY(targetY)
                            .setDuration(300L)
                            .setInterpolator(DecelerateInterpolator())
                            .start()
                    }
                }
            })
            .start()
    }

    private fun setGender(gender: Gender) {
        var welcomeString: String
        var letsStartString: String

        when (gender) {
            Gender.Male -> {
                welcomeString = "ברוך הבא"
                letsStartString = "בוא"
            }
            Gender.Female -> {
                welcomeString = "ברוכה הבאה"
                letsStartString = "בואי"
            }
            else -> {
                welcomeString = "ברוכים הבאים"
                letsStartString = "בואו"
            }
        }

        enter_TEXT_welcome.setText("$welcomeString,")
        enter_TEXT_lets_start.setText("$letsStartString נתחיל")
    }

    private fun joinHousehold() {
        transitionBanner(enter_LAYOUT_enter, enter_LAYOUT_join)
        hasScanned = false
        ensureCameraPermissionThenScan()
    }

    private fun createHousehold() {

    }

    private fun logout() {
        // TODO(Log out from user)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun ensureCameraPermissionThenScan() {
        val alreadyGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (alreadyGranted) {
            startQrScanner()
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startQrScanner() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraExecutor = Executors.newSingleThreadExecutor()

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(enter_PREVIEW_qr.surfaceProvider)
            }

            val scanner = BarcodeScanning.getClient(
                BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                    .build()
            )

            val analysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        processImageProxy(scanner, imageProxy)
                    }
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analysis
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    private fun processImageProxy(scanner: BarcodeScanner, imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        barcode.rawValue?.let { qrValue ->
                            onQrCodeScanned(qrValue)
                        }
                    }
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    private fun onQrCodeScanned(value: String) {
        if (hasScanned) return
        hasScanned = true

        stopQrScanner()

        // TODO: proceed with the actual join logic using `value`
        // e.g. call your join API, then navigate on success.
    }

    private fun stopQrScanner() {
        if (::cameraExecutor.isInitialized) {
            ProcessCameraProvider.getInstance(this).get().unbindAll()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::cameraExecutor.isInitialized) {
            cameraExecutor.shutdown()
        }
    }
}