package com.veryfi.lens.business.cards.demo

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.rarepebble.colorpicker.ColorPickerView
import com.veryfi.lens.VeryfiLens
import com.veryfi.lens.VeryfiLensCredentials
import com.veryfi.lens.VeryfiLensSettings
import com.veryfi.lens.business.cards.demo.databinding.ActivityMainBinding
import com.veryfi.lens.business.cards.demo.helpers.ThemeHelper
import com.veryfi.lens.business.cards.demo.logs.LogsActivity
import com.veryfi.lens.helpers.DocumentType


class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogView: View
    private lateinit var customAlertDialogSliderView: View
    private lateinit var customAlertDialogTextFieldView: View
    private lateinit var colorPickerView: ColorPickerView
    private lateinit var barSliderView: Slider
    private lateinit var textFieldLayoutView: TextInputLayout
    private lateinit var textFieldView: TextInputEditText
    private val veryfiLensSettings = VeryfiLensSettings()
    private var autoLightDetectionIsOn = veryfiLensSettings.autoLightDetectionIsOn
    private var stitchIsOn = veryfiLensSettings.stitchIsOn
    private var allowSubmitUndetectedDocsIsOn = veryfiLensSettings.allowSubmitUndetectedDocsIsOn
    private var autoSubmitDocumentOnCapture = veryfiLensSettings.autoSubmitDocumentOnCapture
    private var backupDocsToGallery = veryfiLensSettings.backupDocsToGallery
    private var returnStitchedPDF = veryfiLensSettings.returnStitchedPDF
    private var closeCameraOnSubmit = veryfiLensSettings.closeCameraOnSubmit
    private var locationServicesIsOn = veryfiLensSettings.locationServicesIsOn
    private var originalImageMaxSizeInMB = veryfiLensSettings.originalImageMaxSizeInMB
    private var autoRotateIsOn = veryfiLensSettings.autoRotateIsOn
    private var autoDocDetectionAndCropIsOn = veryfiLensSettings.autoDocDetectionAndCropIsOn
    private var blurDetectionIsOn = veryfiLensSettings.blurDetectionIsOn
    private var autoSkewCorrectionIsOn = veryfiLensSettings.autoSkewCorrectionIsOn
    private var autoCropGalleryIsOn = veryfiLensSettings.autoCropGalleryIsOn
    private var primaryColor = veryfiLensSettings.primaryColor ?: "#ff4285f4"
    private var accentColor = veryfiLensSettings.accentColor ?: "#ff4285f4"
    private var docDetectFillUIColor = veryfiLensSettings.docDetectFillUIColor ?: "#9653BF8A"
    private var submitButtonBackgroundColor = veryfiLensSettings.submitButtonBackgroundColor
    private var submitButtonBorderColor = veryfiLensSettings.submitButtonBorderColor
    private var submitButtonFontColor = veryfiLensSettings.submitButtonFontColor
    private var docDetectStrokeUIColor = veryfiLensSettings.docDetectStrokeUIColor ?: "#00000000"
    private var submitButtonCornerRadius = veryfiLensSettings.submitButtonCornerRadius
    private var manualCropIsOn = veryfiLensSettings.manualCropIsOn
    private var moreMenuIsOn = false
    private var moreSettingsMenuIsOn = false
    private var galleryIsOn = veryfiLensSettings.galleryIsOn
    private var dictateIsOn = false
    private var emailCCIsOn = veryfiLensSettings.emailCCIsOn
    private var emailCCDomain = veryfiLensSettings.emailCCDomain
    private var rotateDocIsOn = veryfiLensSettings.rotateDocIsOn
    private var shieldProtectionIsOn = veryfiLensSettings.shieldProtectionIsOn
    private var autoDeleteAfterProcessing = veryfiLensSettings.autoDeleteAfterProcessing
    private var boostModeIsOn = veryfiLensSettings.boostModeIsOn
    private var boundingBoxesIsOn = veryfiLensSettings.boundingBoxesIsOn
    private var detectBlurResponseIsOn = veryfiLensSettings.detectBlurResponseIsOn
    private var isProduction = veryfiLensSettings.isProduction
    private var confidenceDetailsIsOn = veryfiLensSettings.confidenceDetailsIsOn
    private var parseAddressIsOn = veryfiLensSettings.parseAddressIsOn
    private var externalId = veryfiLensSettings.externalId ?: ""

    override fun onStart() {
        super.onStart()
        initVeryfiLogo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        materialAlertDialogBuilder = MaterialAlertDialogBuilder(this)
        applicationContext?.let { ThemeHelper.setBackgroundColorToStatusBar(this, it) }
        initVeryfiSettings()
        setUpClickEvents()
    }

    private fun initVeryfiLogo() {
        when (applicationContext?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                viewBinding.veryfiLogo.setImageResource(R.drawable.ic_vector_veryfi_white)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                viewBinding.veryfiLogo.setImageResource(R.drawable.ic_vector_veryfi_black)
            }
        }
    }

    private fun initVeryfiSettings() {
        viewBinding.switchLight.isChecked = autoLightDetectionIsOn
        viewBinding.switchSubmitDocumentsCapture.isChecked = autoSubmitDocumentOnCapture
        viewBinding.switchBackupDocs.isChecked = backupDocsToGallery
        viewBinding.switchCloseCameraSubmit.isChecked = closeCameraOnSubmit
        viewBinding.switchAutoRotate.isChecked = autoRotateIsOn
        viewBinding.switchBlur.isChecked = blurDetectionIsOn
        viewBinding.switchSkew.isChecked = autoSkewCorrectionIsOn
        viewBinding.switchAutoCropGallery.isChecked = autoCropGalleryIsOn
        viewBinding.switchGallery.isChecked = galleryIsOn
        viewBinding.switchRotateDoc.isChecked = rotateDocIsOn
        viewBinding.switchAutoDeleteProcessing.isChecked = autoDeleteAfterProcessing
        viewBinding.switchBoostMode.isChecked = boostModeIsOn
        viewBinding.switchBoundingBoxes.isChecked = boundingBoxesIsOn
        viewBinding.switchDetectBlurResponse.isChecked = detectBlurResponseIsOn
        viewBinding.switchIsProduction.isChecked = isProduction
        viewBinding.switchConfidenceDetails.isChecked = confidenceDetailsIsOn
        viewBinding.switchParseAddress.isChecked = parseAddressIsOn
        initColors()
        initFloatValues()
        initStringValues()
    }

    private fun initColors() {
        viewBinding.imgPrimaryColor.setBackgroundColor(Color.parseColor(primaryColor))
        viewBinding.imgAccentColor.setBackgroundColor(Color.parseColor(accentColor))
        viewBinding.imgDetectFillColor.setBackgroundColor(Color.parseColor(docDetectFillUIColor))
        viewBinding.imgSubmitBackgroundColor.setBackgroundColor(
            Color.parseColor(
                submitButtonBackgroundColor
            )
        )
        viewBinding.imgSubmitBorderColor.setBackgroundColor(Color.parseColor(submitButtonBorderColor))
        viewBinding.imgSubmitFontColor.setBackgroundColor(Color.parseColor(submitButtonFontColor))
    }

    private fun initFloatValues() {
        viewBinding.txtCornerRadius.text = submitButtonCornerRadius.toString()
    }

    private fun initStringValues() {
        viewBinding.txtExternalId.text = externalId
    }

    @SuppressLint("InflateParams")
    private fun setUpClickEvents() {
        viewBinding.btnScan.setOnClickListener {
            setVeryfiSettings()
        }

        viewBinding.switchLight.setOnCheckedChangeListener { _, isChecked ->
            autoLightDetectionIsOn = isChecked
        }

        viewBinding.switchSubmitDocumentsCapture.setOnCheckedChangeListener { _, isChecked ->
            autoSubmitDocumentOnCapture = isChecked
        }

        viewBinding.switchBackupDocs.setOnCheckedChangeListener { _, isChecked ->
            backupDocsToGallery = isChecked
        }

        viewBinding.switchCloseCameraSubmit.setOnCheckedChangeListener { _, isChecked ->
            closeCameraOnSubmit = isChecked
        }

        viewBinding.switchAutoRotate.setOnCheckedChangeListener { _, isChecked ->
            autoRotateIsOn = isChecked
        }

        viewBinding.switchBlur.setOnCheckedChangeListener { _, isChecked ->
            blurDetectionIsOn = isChecked
        }

        viewBinding.switchSkew.setOnCheckedChangeListener { _, isChecked ->
            autoSkewCorrectionIsOn = isChecked
        }

        viewBinding.switchAutoCropGallery.setOnCheckedChangeListener { _, isChecked ->
            autoCropGalleryIsOn = isChecked
        }

        viewBinding.switchGallery.setOnCheckedChangeListener { _, isChecked ->
            galleryIsOn = isChecked
        }

        viewBinding.switchRotateDoc.setOnCheckedChangeListener { _, isChecked ->
            rotateDocIsOn = isChecked
        }

        viewBinding.switchAutoDeleteProcessing.setOnCheckedChangeListener { _, isChecked ->
            autoDeleteAfterProcessing = isChecked
        }

        viewBinding.switchBoostMode.setOnCheckedChangeListener { _, isChecked ->
            boostModeIsOn = isChecked
        }

        viewBinding.switchBoundingBoxes.setOnCheckedChangeListener { _, isChecked ->
            boundingBoxesIsOn = isChecked
        }

        viewBinding.switchDetectBlurResponse.setOnCheckedChangeListener { _, isChecked ->
            detectBlurResponseIsOn = isChecked
        }

        viewBinding.switchIsProduction.setOnCheckedChangeListener { _, isChecked ->
            isProduction = isChecked
        }

        viewBinding.switchConfidenceDetails.setOnCheckedChangeListener { _, isChecked ->
            confidenceDetailsIsOn = isChecked
        }

        viewBinding.switchParseAddress.setOnCheckedChangeListener { _, isChecked ->
            parseAddressIsOn = isChecked
        }

        viewBinding.imgPrimaryColor.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_color_picker, null, false)
            showDialog(primaryColor, 0)
        }

        viewBinding.imgAccentColor.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_color_picker, null, false)
            showDialog(accentColor, 1)
        }

        viewBinding.imgDetectFillColor.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_color_picker, null, false)
            showDialog(docDetectFillUIColor, 2)
        }

        viewBinding.imgSubmitBackgroundColor.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_color_picker, null, false)
            showDialog(submitButtonBackgroundColor, 3)
        }

        viewBinding.imgSubmitBorderColor.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_color_picker, null, false)
            showDialog(submitButtonBorderColor, 4)
        }

        viewBinding.imgSubmitFontColor.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_color_picker, null, false)
            showDialog(submitButtonFontColor, 5)
        }

        viewBinding.txtCornerRadius.setOnClickListener {
            customAlertDialogSliderView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_bar_selection, null, false)
            showDialogWithSlider(submitButtonCornerRadius.toFloat(), 0)
        }

        viewBinding.txtExternalId.setOnClickListener {
            customAlertDialogTextFieldView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_edit_text, null, false)
            showDialogWithTextField(externalId, 1)
        }
    }

    private fun showDialog(color: String, typeColor: Int) {
        colorPickerView = customAlertDialogView.findViewById(R.id.colorPicker)
        colorPickerView.color = Color.parseColor(color)

        materialAlertDialogBuilder.setView(customAlertDialogView)
            .setTitle(resources.getString(R.string.settings_set_color_title))
            .setPositiveButton(resources.getString(R.string.btn_ok)) { dialog, _ ->
                val colorSelected = "#".plus(formatColor(colorPickerView.color))

                when (typeColor) {
                    0 -> {
                        primaryColor = colorSelected
                    }
                    1 -> {
                        accentColor = colorSelected
                    }
                    2 -> {
                        docDetectFillUIColor = colorSelected
                    }
                    3 -> {
                        submitButtonBackgroundColor = colorSelected
                    }
                    4 -> {
                        submitButtonBorderColor = colorSelected
                    }
                    5 -> {
                        submitButtonFontColor = colorSelected
                    }
                    6 -> {
                        docDetectStrokeUIColor = colorSelected
                    }
                }
                initColors()
                dialog.dismiss()
            }
            .setNegativeButton(resources.getString(R.string.btn_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDialogWithSlider(value: Float, type: Int) {
        barSliderView = customAlertDialogSliderView.findViewById(R.id.barSlider)
        barSliderView.value = value
        var titleDialog = ""
        when (type) {
            0 -> {
                titleDialog = resources.getString(R.string.settings_set_submit_button_corner_radius)
                barSliderView.valueFrom = 0.0f
                barSliderView.valueTo = 20.0f
                barSliderView.stepSize = 1.0f
            }
            1 -> {
                titleDialog = resources.getString(R.string.settings_set_original_image_size)
                barSliderView.valueFrom = 0.0f
                barSliderView.valueTo = 10.0f
                barSliderView.stepSize = 0.5f
            }
        }

        materialAlertDialogBuilder.setView(customAlertDialogSliderView)
            .setTitle(titleDialog)
            .setPositiveButton(resources.getString(R.string.btn_ok)) { dialog, _ ->

                when (type) {
                    0 -> {
                        submitButtonCornerRadius = barSliderView.value.toInt()
                    }
                    1 -> {
                        originalImageMaxSizeInMB = barSliderView.value
                    }
                }
                initFloatValues()
                dialog.dismiss()
            }
            .setNegativeButton(resources.getString(R.string.btn_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDialogWithTextField(value: String, type: Int) {
        textFieldLayoutView = customAlertDialogTextFieldView.findViewById(R.id.textField_layout)
        textFieldView = customAlertDialogTextFieldView.findViewById(R.id.textField)
        textFieldView.setText(value)
        var titleDialog = ""
        when (type) {
            0 -> {
                titleDialog = resources.getString(R.string.settings_set_email_cc_domain)
                textFieldLayoutView.hint = resources.getString(R.string.settings_hint_cc_domain)
            }
            1 -> {
                titleDialog = resources.getString(R.string.settings_set_external_id)
                textFieldLayoutView.hint = resources.getString(R.string.settings_hint_external_id)
            }
        }

        materialAlertDialogBuilder.setView(customAlertDialogTextFieldView)
            .setTitle(titleDialog)
            .setPositiveButton(resources.getString(R.string.btn_ok)) { dialog, _ ->

                when (type) {
                    0 -> {
                        emailCCDomain = textFieldView.text.toString()
                    }
                    1 -> {
                        externalId = textFieldView.text.toString()
                    }
                }
                initStringValues()
                dialog.dismiss()
            }
            .setNegativeButton(resources.getString(R.string.btn_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setVeryfiSettings() {
        veryfiLensSettings.autoLightDetectionIsOn = autoLightDetectionIsOn
        veryfiLensSettings.stitchIsOn = stitchIsOn
        veryfiLensSettings.allowSubmitUndetectedDocsIsOn = allowSubmitUndetectedDocsIsOn
        veryfiLensSettings.autoSubmitDocumentOnCapture = autoSubmitDocumentOnCapture
        veryfiLensSettings.backupDocsToGallery = backupDocsToGallery
        veryfiLensSettings.returnStitchedPDF = returnStitchedPDF
        veryfiLensSettings.closeCameraOnSubmit = closeCameraOnSubmit
        veryfiLensSettings.locationServicesIsOn = locationServicesIsOn
        veryfiLensSettings.originalImageMaxSizeInMB = originalImageMaxSizeInMB
        veryfiLensSettings.autoRotateIsOn = autoRotateIsOn
        veryfiLensSettings.autoDocDetectionAndCropIsOn = autoDocDetectionAndCropIsOn
        veryfiLensSettings.blurDetectionIsOn = blurDetectionIsOn
        veryfiLensSettings.autoSkewCorrectionIsOn = autoSkewCorrectionIsOn
        veryfiLensSettings.autoCropGalleryIsOn = autoCropGalleryIsOn
        veryfiLensSettings.primaryColor = primaryColor
        veryfiLensSettings.accentColor = accentColor
        veryfiLensSettings.docDetectFillUIColor = docDetectFillUIColor
        veryfiLensSettings.submitButtonBackgroundColor = submitButtonBackgroundColor
        veryfiLensSettings.submitButtonBorderColor = submitButtonBorderColor
        veryfiLensSettings.submitButtonFontColor = submitButtonFontColor
        veryfiLensSettings.docDetectStrokeUIColor = docDetectStrokeUIColor
        veryfiLensSettings.submitButtonCornerRadius = submitButtonCornerRadius
        veryfiLensSettings.manualCropIsOn = manualCropIsOn
        veryfiLensSettings.moreMenuIsOn = moreMenuIsOn
        veryfiLensSettings.moreSettingsMenuIsOn = moreSettingsMenuIsOn
        veryfiLensSettings.galleryIsOn = galleryIsOn
        veryfiLensSettings.dictateIsOn = dictateIsOn
        veryfiLensSettings.emailCCIsOn = emailCCIsOn
        veryfiLensSettings.emailCCDomain = emailCCDomain
        veryfiLensSettings.rotateDocIsOn = rotateDocIsOn
        veryfiLensSettings.shieldProtectionIsOn = shieldProtectionIsOn
        veryfiLensSettings.autoDeleteAfterProcessing = autoDeleteAfterProcessing
        veryfiLensSettings.boostModeIsOn = boostModeIsOn
        veryfiLensSettings.boundingBoxesIsOn = boundingBoxesIsOn
        veryfiLensSettings.detectBlurResponseIsOn = detectBlurResponseIsOn
        veryfiLensSettings.isProduction = isProduction
        veryfiLensSettings.confidenceDetailsIsOn = confidenceDetailsIsOn
        veryfiLensSettings.parseAddressIsOn = parseAddressIsOn
        veryfiLensSettings.externalId = externalId
        veryfiLensSettings.documentTypes = arrayListOf(DocumentType.BUSINESS_CARD)
        veryfiLensSettings.showDocumentTypes = true

        val veryfiLensCredentials = VeryfiLensCredentials()
        veryfiLensCredentials.apiKey = Application.AUTH_API_KEY
        veryfiLensCredentials.username = Application.AUTH_USERNAME
        veryfiLensCredentials.clientId = Application.CLIENT_ID
        veryfiLensCredentials.url = Application.URL

        VeryfiLens.configure(
            this.application,
            veryfiLensCredentials,
            veryfiLensSettings
        ) {
        }
        startActivity(Intent(this, LogsActivity::class.java))
    }

    private fun formatColor(color: Int): String {
        return String.format("%08x", color)
    }
}


