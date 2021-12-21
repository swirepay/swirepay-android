# Swirepay Android SDK
**Swirepay Android SDK** helps developers implement a native payment experience in their Android application. The SDK requires minimal setup to get started and helps developers process payments under 30 seconds while being PCI compliant.

The SDK supports payment flows including simple checkout, saving payment methods, paying invoice, setting up subscriptions, account creation and more.

## Requirements
- Android 5.0 (API level 21) and above
- Android Gradle Plugin 3.5.1
- Gradle 5.4.1+
- AndroidX (as of v11.0.0)

## Integration Steps
This document reviews about process to integrate Swirepay Android SDK with your Android application.
- Create an account with Swirepay and get the API keys
- Integrate the Swirepay android SDK into your application
- Initiate call - Invoke checkout, payment method, invoice, subscriptions, account creation API from the Swirepay SDK from your application. Swirepay SDK displays appropriate screens. 
- Receive and handle response - Swirepay SDK returns the payment result for the order which should be handled in your application.


## Step 1: Create Account and Get API Keys
- Go to Swirepay Website and create an account. The account credential will be shared through mail.
- Log in to your Merchant Dashboard using the credential.
- Click the Developer menu and copy the Secret Key.  Leverage the test keys if you are testing in test mode. Secret Keys are used to authenticate the API calls made from Swirepay Android SDK.
     - Publishable Keys are primarily for Client facing web based applications.
    - Symmetric Keys are primarily for Enterprise clients who need Host-to-Host integration 

## Step 2: Integrate SDK
The Swirepay SDK is bundled as an AAR file according to the latest Android standards. We support integration from API level 19. Ensure that the **minSdkVersion** in the build.gradle of your app is equal to or greater than that.
To integrate the SDK, follow the steps below:
 Open the build.gradle file of your app module and add the Swirepay SDK in the dependency section.
 
``
    implementation 'com.swirepay:swirepay-android:1.0.11'
``

The Swirepay SDK requires you to add the permissions shown below in your Android Manifest file.
``
<manifest ...>
<uses-permission android:name="android.permission.INTERNET" />  
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<application ...>
``
Add **android.useAndroidX=true** in **gradle.properties** file.


## Step 3: Initiate call
The SwirepaySdk class is the entry-point to the Swirepay SDK. It must be instantiated with a [Swirepay publishable/Secret key].

``
SwirepaySdk.init("pk_test_key")
``

##### 1. Payment Link
 
```
PaymentRequest(
    val amount: String,
    val currencyCode: String,
    val paymentMethodType: List<String>,
    val customer: CustomerModel?,
    val customerGid: String?,
    val notificationType: String,
    val dueDate: String?,)
```

```
class CustomerModel(
    val email: String,
    val name: String,
    val phoneNumber: String,
    val taxId: String,
    val taxStatus: String,
    val taxValue: String?
) 
```

```
val response = apiClient.fetchPaymentLink(paymentRequest, SwirepaySdk.apiKey!!).execute()
```

##### 2.Subscription Button

```
class SubscriptionButtonRequest(val currencyCode: String, val description : String, val planAmount : Int, val planBillingFrequency : String,
                                val planBillingPeriod : Int, val planGid : String, val planQuantity : Int, val planStartDate : String, val planTotalPayments : String,
                                val redirectUri : String = Utility.baseUrl, val couponGid : String? = null , val taxRates : List<String>? = null , val name : String
                                )
val subResponse = apiClient.createSubscriptionButton(subscriptionButtonRequest,
SwirepaySdk.apiKey!!).execute()
```
                

##### 3.DoPayment              
After adding the SDK in dependency, initiate a payment call SwirepaySdk.doPayment().
For payment, your application passes the order info to the SDK. The relevant payment screen is displayed to the customer where they enter the required information and make the payment. After the payment is complete the customers are redirected to the Android application and a response is received on onActivityResult().
```Create an Order Info
val orderInfo = OrderInfo()
        orderInfo.amount = 100
        orderInfo.receiptEmail = "testaccountowner-stag+592@swirepay.com"
        orderInfo.receiptSms = "+919845789562"
        orderInfo.currencyCode = "INR"
        orderInfo.description = "Test"

val customer = SPCustomer("Muthu", "testaccountowner-stag+592@swirepay.com", "+919845789562")
```


###### Perform payment - Method #1
```
SwirepaySdk.doPayment(this, orderInfo,customer,REQUEST_CODE_CHECKOUT)
```


###### Perform payment- - Method #2 Customize screen with own brand colors
```
SwirepaySdk.doPayment(this,orderInfo,customer,REQUEST_CODE_CHECKOUT,"#FF0000","#FF0000","#FFFFFF")
```

## Step 4. Receive and Handle Response
After the payment is completed, you will receive the response on the onActivityResult() function of the invoking activity. In the intent extras, you will receive a set of response parameters which is used to determine if the transaction was successful or not. 
Request code will always be equal to **SwirepaySdk.REQUEST_CODE_CHECKOUT**

```
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    when (requestCode) {
        REQUEST_CODE_CHECKOUT -> {
            val paymentResult = SwirepaySdk.getPaymentCheckout(resultCode, data)
            Log.d("sdk_test", "onActivityResult: $result")
        }
    }
}
```
**paymentResult is a native Android Object**


## Release
 The [changelog](https://github.com/swirepay/swirepay-android/blob/master/CHANGELOG.md) provides a summary of changes in each release.

## Proguard
The Swirepay Android SDK will configure your app's Proguard rules using [proguard-rules.txt](https://github.com/swirepay/swirepay-android/blob/master/app/proguard-rules.pro).

## License
Swirepay_Android_SDK is available under the MIT license. 
See the [LICENSE](https://github.com/swirepay/swirepay-android/blob/master/LICENSE) file for more info.
  