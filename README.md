# Swirepay Android SDK
**Swirepay Android SDK** helps developers implement a native payment experience in their Android application. The SDK requires minimal setup to get started and helps developers process payments under 30 seconds while being PCI compliant.

The SDK supports payment flows including simple checkout, saving payment methods, paying invoice, setting up subscriptions, account creation and more.

## Requirements
- Android 5.0 (API level 21) and above
- Android Gradle Plugin 3.5.1
- Gradle 5.4.1+
- AndroidX (as of v11.0.0)

## Configuration

Add this in your app gradle dependency
``
    implementation 'com.swirepay:swirepay_android:1.0.0'
``

## Example
The SwirepaySdk class is the entry-point to the Swirepay SDK. It must be instantiated with a [Swirepay publishable/Secret key].

``
SwirepaySdk.init("pk_test_key")
``

##### 1. Payment Link
 
``
PaymentRequest(
    val amount: String,
    val currencyCode: String,
    val paymentMethodType: List<String>,
    val customer: CustomerModel?,
    val customerGid: String?,
    val notificationType: String,
    val dueDate: String?,)
``

``
class CustomerModel(
    val email: String,
    val name: String,
    val phoneNumber: String,
    val taxId: String,
    val taxStatus: String,
    val taxValue: String?
) 
``

``
val response = apiClient.fetchPaymentLink(paymentRequest, SwirepaySdk.apiKey!!).execute()
``

##### 2.Subscription Button

``
class SubscriptionButtonRequest(val currencyCode: String, val description : String, val planAmount : Int, val planBillingFrequency : String,
                                val planBillingPeriod : Int, val planGid : String, val planQuantity : Int, val planStartDate : String, val planTotalPayments : String,
                                val redirectUri : String = Utility.baseUrl, val couponGid : String? = null , val taxRates : List<String>? = null , val name : String
                                )
                                ``
                                
``
val subResponse = apiClient.createSubscriptionButton(subscriptionButtonRequest,
SwirepaySdk.apiKey!!).execute()
                ``

## Release
 The [changelog](https://github.com/swirepay/swirepay-android/blob/master/CHANGELOG.md) provides a summary of changes in each release.

## Proguard
The Swirepay Android SDK will configure your app's Proguard rules using [proguard-rules.txt](https://github.com/swirepay/swirepay-android/blob/master/app/proguard-rules.pro).

## License
Swirepay_Android_SDK is available under the MIT license. 
See the [LICENSE](https://github.com/swirepay/swirepay-android/blob/master/LICENSE) file for more info.
