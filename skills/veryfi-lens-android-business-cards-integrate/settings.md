# Veryfi Lens Android — Business Cards — settings

Source of truth: Veryfi Lens Android SDK **2.7.0.6**, commit `7f33726c`. Names below were read from that tree. Items not fully confirmed are marked **Unconfirmed**. Do not copy repository passwords, keystores, or Demo-only types into the host app.


Always construct a fresh `VeryfiLensSettings()`. Android has no `VeryfiLens*Settings` subclasses. Pass that instance to `configure`. The SDK mutates it.

```kotlin
import com.veryfi.lens.helpers.DocumentType
import com.veryfi.lens.helpers.VeryfiLensSettings

fun businessCardSettings() = VeryfiLensSettings().apply {
    ignoreRemoteSettings = true            // default false; dashboard lens_settings overwrite this object during configure
    backupDocsToGallery = false            // default true; copies every scan into the device gallery
    moreMenuIsOn = false                   // default true; stored user prefs then replace host capture flags
    moreSettingsMenuIsOn = false           // default true
    autoCaptureOtherIsOn = false           // default true; OTHER auto-captures even when autoCaptureIsOn is false
    showNoDocumentDetectedWarning = false  // default true
    dictateIsOn = false                    // default true; obsolete voice capture, not a partner feature
    locationServicesIsOn = false           // default false; true raises a runtime location request
    glareDetectionIsOn = false             // default true; the glare gate is a weak algorithmic check

    dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.VeryfiCloudAPI
    documentTypes = arrayListOf(DocumentType.BUSINESS_CARD)
    showDocumentTypes = false
    stitchIsOn = false
    autoCaptureIsOn = false
    galleryIsOn = true
    autoRotateIsOn = true                  // default false
    cleanBorderIsOn = true                 // default false
    returnStitchedPDF = true               // default false
    driverLicenseBackIsOn = false          // Android default true; irrelevant until DRIVER_LICENSE is listed
    insuranceCardBackIsOn = false          // Android default true; irrelevant until INSURANCE_CARD is listed
}

```

## Defaults versus this recipe

Android initializers below are the property defaults in `VeryfiLensSettings`.

| Property | Android default | This recipe | Why |
|----------|-----------------|-------------|-----|
| `ignoreRemoteSettings` | false | true | Dashboard `lens_settings` would overwrite this object during configure |
| `backupDocsToGallery` | true | false | Default copies every scan into the device gallery |
| `moreMenuIsOn` | true | false | Stored user prefs would replace host capture flags on later configure calls |
| `moreSettingsMenuIsOn` | true | false | Settings sheet inside More |
| `autoCaptureOtherIsOn` | true | false | `OTHER` auto-captures even when `autoCaptureIsOn` is false |
| `showNoDocumentDetectedWarning` | true | false | Skip the no-document alert |
| `dictateIsOn` | true | false | Obsolete voice control; default is on |
| `glareDetectionIsOn` | true | false | Android default glare gate is a weak check |
| `locationServicesIsOn` | false | false | Leave off unless the host needs lat/lng on the package |

| `documentTypes` | null | `BUSINESS_CARD` | Add another allowed shortcut only with `showDocumentTypes = true` |
| `stitchIsOn` | true | false | Card capture is a single frame |
| `autoRotateIsOn` | false | true | Rotate the card |
| `cleanBorderIsOn` | false | true | Clean-border post process |
| `returnStitchedPDF` | false | true | PDF of the capture |
| `driverLicenseBackIsOn` | true | false | Turn on only with `DRIVER_LICENSE` when the back is in scope |
| `insuranceCardBackIsOn` | true | false | Turn on only with `INSURANCE_CARD` when the back is in scope |


When the host adds `DRIVER_LICENSE`, set `driverLicenseBackIsOn` and `driverLicenseBackIsRequired` (default false) on purpose. Same for `insuranceCardBackIsOn` / `insuranceCardBackIsRequired`. `anyDocumentBackIsOn` does not drive those two types.

`cameraProcessingMode` stays at default `Document` unless support has confirmed an `ANY_DOCUMENT` flow on a different artifact. This flavor cannot set `ANY_DOCUMENT`.

Cloud response options (`parseAddressIsOn`, `boundingBoxesIsOn`, `confidenceDetailsIsOn`) affect the Veryfi API response, not a local model.


## Suggestions

- Start with `BUSINESS_CARD` only. Add shortcuts after the first scan works.
- For install size, measure `veryfi-lens-remote-models-sdk`, which uses `checkLensFull` and also allows these shortcuts (and `ANY_DOCUMENT`). Prescription labels still need `veryfi-lens-prescription-labels-sdk` or Full.
- Gallery and `showDocumentBrowser()` are appropriate for photos the user already has. PDFs go through `uploadImages(uris)`.

