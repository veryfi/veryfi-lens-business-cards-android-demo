---
name: veryfi-lens-android-business-cards-integrate
description: "Integrates the Veryfi Lens Android Business Cards SDK (com.veryfi.lens:veryfi-lens-business-cards-sdk:2.7.0.6): Maven setup, VeryfiLens.configure, VeryfiLensSettings for BUSINESS_CARD and Any-Documents shortcuts, delegate callbacks, showCamera, showGallery, showDocumentBrowser, and uploadImages. Use when adding business-card capture on Android, or when this artifact also scans insurance cards, passports, driver licenses, nutrition facts, or shipping labels."
---

# Veryfi Lens Android — Business Cards

Walk a host Android app from Gradle to a working first scan. Change the fewest flags. Do not invent SDK APIs. Facts were read from the Android SDK at version `2.7.0.6` (commit `7f33726c`). Anything not confirmed there is marked **Unconfirmed**.

**Coordinate:** `com.veryfi.lens:veryfi-lens-business-cards-sdk:2.7.0.6`  
**Gradle flavor:** `lensBusinessCards`  
**Allowed document types:** `BUSINESS_CARD`, `INSURANCE_CARD`, `PASSPORT`, `DRIVER_LICENSE`, `NUTRITION_FACT`, `SHIPPING_LABEL`.

Public entry point: `com.veryfi.lens.VeryfiLens` with `com.veryfi.lens.VeryfiLensDelegate`. Settings type: `com.veryfi.lens.helpers.VeryfiLensSettings`.

This folder is complete by itself. Duplicate guidance across products is intentional.

## Pick a workflow

- **Quick** — the host already asked for this product. Ask nothing else. Apply the settings in [settings.md](settings.md), wire [implementation.md](implementation.md), and state the assumptions below.
- **Guided** — something material is missing. Ask only the unknown questions (at most four, one message), then continue.

### Quick workflow

```
veryfi-lens-android-business-cards-integrate quick:
- [ ] Confirm this artifact (install.md)
- [ ] Add the Maven repository and this one dependency
- [ ] Permissions (install.md)
- [ ] Credentials + settings from settings.md
- [ ] setDelegate, then configure { ok -> }, then showCamera() only when ok is true
- [ ] Handle the callbacks in callbacks.md
- [ ] Run the conflicts in troubleshooting.md
```

Assumptions when the host was specific about the product and silent on the rest: Lens camera, `BUSINESS_CARD` only, Cloud API, stitch off, gallery on, back-side prompts left at Android defaults until an ID type is added.

### Guided workflow

1. **Type** — business card, or also an Any-Documents shortcut allowed here (insurance, passport, driver license, nutrition facts, shipping label)?
2. **Entry** — camera, gallery, files, or `uploadImages`?
3. **Back** — for driver license or insurance card, is the back optional, required, or off?
4. **Template** — if `ANY_DOCUMENT` was requested, stop: this flavor rejects it. Ask support which artifact and template id to use.

Defaults: `BUSINESS_CARD`, Cloud API, camera plus gallery, stitch off.


## Rules

1. Stay inside the six allowed types. Business cards use `VeryfiCloudAPI`.
2. `stitchIsOn = false` for a card. Turn stitch on only if the host is scanning a multi-page shortcut and asked for it.
3. If `DRIVER_LICENSE` or `INSURANCE_CARD` is included, set `driverLicenseBackIsOn` / `insuranceCardBackIsOn` explicitly. Both default to true. They ignore `anyDocumentBackIsOn`.
4. A new template string is not invented here. `ANY_DOCUMENT` is not allowed. Contact Veryfi support.
5. `uploadImages` is this UI SDK. Headless card reading is `com.veryfi.lens.headless:credit-cards-sdk` and is a different product (payment cards, on device).


## Do not

- Copy a repository password, keystore, or credential from the SDK repo or the Demo app. Maven access comes from Veryfi. Keep it in `~/.gradle/gradle.properties` or CI secrets.
- Depend on a second Lens UI flavor, or on `com.veryfi.lens.core:veryfi-lens-core-sdk` (email and obsolete dictation; adds `WRITE_CONTACTS` and `RECORD_AUDIO`).
- Treat Demo types as the client contract: `SettingsHolder`, Demo analyzers, `ImageProxyUtils`, `VeryfiLensHelper`.
- Set `dictateIsOn = true` or call `VeryfiLensCore.showDictation()`.
- Set `anyDocumentType` (deprecated local regex OCR). A new template id is `anyDocumentTemplate`, and only after Veryfi support confirms it.
- Set `useLegacyModels` (default true; no partner contract).
- Quote AAR size as the installed app size. Compare the final app with the SDK linked and removed. [SDK binary sizes](https://docs.veryfi.com/lens/mobile/settings/sdk_binary_sizes/).
- Treat `allowSubmittingScreenshots` (rejects imported screenshots) and `enableScreenshots` (device screenshots of Lens screens, default true) as one switch.
- Add `ANY_DOCUMENT` or `PRESCRIPTION_LABEL` on this artifact.
- Leave driver-license or insurance-card back flags unspecified when those types are enabled.
- Treat nutrition facts, passport, driver license, shipping label, or insurance card as products separate from Any-Documents.
- Promise `documentFieldsToDetect` (receipt-only models, and this flavor is `fieldDetectionStub`).


## What to hand back

1. `implementation "com.veryfi.lens:veryfi-lens-business-cards-sdk:2.7.0.6"`.
2. `businessCardSettings()` limited to allowed types.
3. Delegate reading Cloud `data`.
4. Explicit back-side flags when an ID shortcut is in the list.
5. Support escalation when the host asks for a custom Any-Documents template or prescription labels.


## Files in this folder

- [install.md](install.md) — Maven coordinate, requirements, permissions, app size
- [implementation.md](implementation.md) — configure, delegate, and launch or camera lifecycle
- [settings.md](settings.md) — `VeryfiLensSettings` for this product, with Android defaults
- [callbacks.md](callbacks.md) — delegate payloads
- [troubleshooting.md](troubleshooting.md) — symptom, cause, fix
- [clarifications.md](clarifications.md) — Unconfirmed items, Any-Documents, fraud, field detection
- [checklist.md](checklist.md) — validation checklist before handing code to the host

