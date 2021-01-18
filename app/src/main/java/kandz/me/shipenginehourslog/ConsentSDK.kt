

/*------------------------------

#project-level build.gradle
    maven {  url "https://maven.google.com"    }

#build.grade
    implementation 'com.google.android.gms:play-services-ads:17.0.0'
    implementation 'com.google.android.ads.consent:consent-library:1.0.6'

<com.google.android.gms.ads.AdView
            xmlns:ads="http://schemas.android.com/apk/res-auto"
            android:id="@+id/adView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerHorizontal="true"
            android:layout_alignParentBottom="true"
            ads:adSize="BANNER"
            ads:adUnitId="ca-app-pub-3940256099942544/6300978111">
    </com.google.android.gms.ads.AdView>

#AndroidManifext.xml inside application.
  <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="ca-app-pub-0717744179319214~5603768678"/>

#in every activity
 //----------Ads
private lateinit var insterstitialAd:InterstitialAd
private lateinit var consentSDK: ConsentSDK
        //--------end ADS

 //----------Ads
fun loadBanner(){
        adView.loadAd(ConsentSDK.getAdRequest(this@MainActivity))
    }

    fun loadInterstitial(){
        insterstitialAd = InterstitialAd(this@MainActivity)
        insterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        insterstitialAd.loadAd(ConsentSDK.Companion.getAdRequest(this@MainActivity))
        insterstitialAd.adListener = object :AdListener(){
            override fun onAdLoaded() {
                insterstitialAd.show()
                super.onAdLoaded()
            }

        }
    }

    fun initConsentSDK(context: Context){
        consentSDK = ConsentSDK.Builder(this@MainActivity)
            .addCustomLogTag("-------")
            .addPrivacyPolicy("http://kandz.me/privacypolicy/privacy_policy_tuf.html") //create here https://app-privacy-policy-generator.firebaseapp.com/
            .addPublisherId("pub-0717744179319214")
            .addTestDeviceId(AdRequest.DEVICE_ID_EMULATOR)  // use this debug
            .build()
    }
//--------end ADS

# in onCreate


//----------Ads
 //ConsentSDK.Companion.clearConsent(this)

        initConsentSDK(this)


       var firstTime = true
        if (ConsentSDK.Companion.isConsentSetup(this)){
            Log.d("-------------","YES")
            loadBanner()
            firstTime = false
        }
        else{
            Log.d("-------------","NO")
            consentSDK.requestConsent(object :ConsentSDK.ConsentStatusCallback(){
                override fun onResult(isRequestLocationInEeaOrUnknown: Boolean, isConsentPersonalized: Int) {

                    if (firstTime)
                        firstTime = false
                    else
                        loadBanner()
                }
            })
        }

//--------end ADS


 */

package kandz.me.shipenginehourslog

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import com.google.ads.consent.*
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import java.net.MalformedURLException
import java.net.URL

class ConsentSDK(contexT: Context, publisherID: String, privacyURL: String) {

    public abstract class ConsentInformationCallBack{
        abstract public fun onResult(consentInformation: ConsentInformation, consentStatus: ConsentStatus)
        abstract public fun onFailed(consentInformation: ConsentInformation, reason:String)
    }

    public abstract  class ConsentCallback{
        abstract public fun onResult(isRequestLocationInEeaOrUnknown:Boolean)
    }

    public abstract class ConsentStatusCallback{
        abstract public fun onResult(isRequestLocationInEeaOrUnknown: Boolean,isConsentPersonalized:Int)
    }

    public abstract class LocationIsEeaOrUnknownCallback{
        abstract public fun onResult(isRequestLocationInEeaOrUnknown: Boolean)
    }


    var context: Context = contexT
    var privacyUrlL:String = privacyURL
    var publisherId:String = publisherID
    var settings: SharedPreferences = initPreferences(contexT)
    var consentSDK: ConsentSDK = this


    lateinit var form: ConsentForm

    var LOG_TAG ="LOG - "
    var DEVICE_ID = ""
    var DEBUG = false


    //Debug
    constructor(contexT: Context, publisherID: String, privacyURL: String, debug:Boolean) : this(contexT,publisherID,privacyURL) {
        this.context = contexT
        this.settings = initPreferences(contexT)
        this.publisherId = publisherID
        this.privacyUrlL =privacyUrlL
        this.DEBUG = debug
        this.consentSDK = this

    }


    class Builder(contexT: Context) {

        var context: Context = contexT
        lateinit var privacyURL:String
        lateinit var publisherId:String
        var LOG_TAG ="LOG - "
        var DEVICE_ID = ""
        var DEBUG = false

        fun addTestDeviceId(device_id:String):Builder{
            this.DEVICE_ID = device_id
            this.DEBUG = true
            return this
        }

        fun addPrivacyPolicy(privacyURL:String):Builder{
            this.privacyURL = privacyURL
            return this
        }

        fun addPublisherId(publisherId:String):Builder{
            this.publisherId = publisherId
            return this
        }

        fun addCustomLogTag(LOG_TAG:String):Builder
        {
            this.LOG_TAG = LOG_TAG
            return this
        }

        public fun build():ConsentSDK{
            if (this.DEBUG){
                var consentSDK = ConsentSDK(context,publisherId,privacyURL,true)
                consentSDK.LOG_TAG = this.LOG_TAG
                consentSDK.DEVICE_ID = this.DEVICE_ID
                return consentSDK
            } else {
                return  ConsentSDK(context,publisherId,privacyURL)
            }
        }
    }



    public fun initDummyBanner(context: Context){
        var adView = AdView(context)
        adView.adSize = AdSize.BANNER
        adView.adUnitId  = DUMMY_BANNER
        adView.loadAd(AdRequest.Builder().build())
    }

    fun initPreferences(contexT: Context): SharedPreferences {
        return contexT.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }



    fun consentIsPersonalized(){
        settings.edit().putBoolean(ADS_PREFERENCE,PERSONALIZED).apply()
    }

    fun consentIsNotPersonalized(){
        settings.edit().putBoolean(ADS_PREFERENCE,NON_PERSONALIZED).apply()
    }

    fun updateUserStatus(status:Boolean){
        settings.edit().putBoolean(USER_STATUS,status).apply()
    }



    fun initConsentInformation(callback: ConsentInformationCallBack){
        val consentInformation = ConsentInformation.getInstance(context)
        if(DEBUG){
            if(!DEVICE_ID.isEmpty()){
                consentInformation.addTestDevice(DEVICE_ID)
            }
            consentInformation.debugGeography = DebugGeography.DEBUG_GEOGRAPHY_EEA
        }

        val publisherIds = arrayOf<String>(publisherId)
        consentInformation.requestConsentInfoUpdate(publisherIds, object : ConsentInfoUpdateListener {
            override fun onConsentInfoUpdated(consentStatus: ConsentStatus) {
                if (callback !=null)
                    callback.onResult(consentInformation, consentStatus)
            }


            override fun onFailedToUpdateConsentInfo(reason: String){
                callback.onFailed(consentInformation,reason)
            }
        })
    }


    public fun isRequestLocationIsEeeOrUnknown(callback:ConsentSDK.LocationIsEeaOrUnknownCallback){
        initConsentInformation(object:ConsentInformationCallBack(){
            override fun onResult(consentInformation: ConsentInformation, consentStatus: ConsentStatus) {
                callback.onResult(consentInformation.isRequestLocationInEeaOrUnknown)
            }

            override fun onFailed(consentInformation: ConsentInformation, reason: String) {
                callback.onResult(false)
            }

        })
    }

    public fun isUserLocationWithinEea(contexT: Context):Boolean{
        return initPreferences(contexT).getBoolean(USER_STATUS,false)
    }

    public fun checkConsent(callback: ConsentCallback){
        initConsentInformation(object :ConsentInformationCallBack(){
            override fun onResult(consentInformation: ConsentInformation, consentStatus: ConsentStatus) {
                when (consentStatus){
                    ConsentStatus.UNKNOWN -> {
                        if (DEBUG){
                            Log.d(LOG_TAG,"Unknown Consent")
                            Log.d(LOG_TAG,"User location within EEA:" + consentInformation.isRequestLocationInEeaOrUnknown)
                        }
                        if (consentInformation.isRequestLocationInEeaOrUnknown){
                            requestConsent(object :ConsentStatusCallback(){
                                override fun onResult(
                                        isRequestLocationInEeaOrUnknown: Boolean,
                                        isConsentPersonalized: Int
                                ) {
                                    callback.onResult(isRequestLocationInEeaOrUnknown)
                                }

                            })
                        } else {
                            consentIsPersonalized()
                            callback.onResult(consentInformation.isRequestLocationInEeaOrUnknown)
                        }
                    }
                    ConsentStatus.NON_PERSONALIZED->{
                        consentIsNotPersonalized()
                        callback.onResult(consentInformation.isRequestLocationInEeaOrUnknown)
                    }
                    else ->{
                        consentIsPersonalized()
                        callback.onResult(consentInformation.isRequestLocationInEeaOrUnknown)
                    }
                }
                updateUserStatus(consentInformation.isRequestLocationInEeaOrUnknown)
            }

            override fun onFailed(consentInformation: ConsentInformation, reason: String) {
                if (DEBUG){
                    Log.d(LOG_TAG,"Failed to update: $reason")
                    updateUserStatus(consentInformation.isRequestLocationInEeaOrUnknown)
                    callback.onResult(consentInformation.isRequestLocationInEeaOrUnknown)
                }
            }

        })
    }


    public fun requestConsent(callback: ConsentStatusCallback){
        var privacyUrl: URL? = null
        try {
            privacyUrl = URL(privacyUrlL)
        } catch (e: MalformedURLException){

        }

        form = ConsentForm.Builder(context,privacyUrl)
                .withListener(object : ConsentFormListener(){
                    override fun onConsentFormLoaded() {
                        if(DEBUG){
                            Log.d(LOG_TAG,"Consent Form is loaded");
                        }
                        form.show()
                    }

                    override fun onConsentFormError(reason: String?) {
                        if(DEBUG){
                            Log.d(LOG_TAG,"Consent Form ERROR : $reason")
                        }
                        if (callback != null){
                            consentSDK.isRequestLocationIsEeeOrUnknown(object :ConsentSDK.LocationIsEeaOrUnknownCallback(){
                                override fun onResult(isRequestLocationInEeaOrUnknown: Boolean) {
                                    callback.onResult(isRequestLocationInEeaOrUnknown,-1)
                                }
                            })
                        }
                    }

                    override fun onConsentFormOpened() {
                        if (DEBUG){
                            Log.d(LOG_TAG,"Consent Form is opened!")
                        }
                    }

                    override fun onConsentFormClosed(consentStatus: ConsentStatus?, userPrefersAdFree: Boolean?) {
                        if (DEBUG){
                            Log.d(LOG_TAG,"Consent Form Closed")
                        }

                        var isConsentPersonalized:Int
                        when (consentStatus){
                            ConsentStatus.NON_PERSONALIZED->{
                                consentIsNotPersonalized()
                                isConsentPersonalized=0
                            }
                            else ->{
                                consentIsPersonalized()
                                isConsentPersonalized=1
                            }
                        }

                        if (callback!=null){
                            consentSDK.isRequestLocationIsEeeOrUnknown(object :ConsentSDK. LocationIsEeaOrUnknownCallback(){
                                override fun onResult(isRequestLocationInEeaOrUnknown: Boolean) {
                                    callback.onResult(isRequestLocationInEeaOrUnknown,isConsentPersonalized )
                                }
                            })
                        }
                    }

                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .build()

        form.load()

    }

    companion object {

        val ADS_PREFERENCE = "ads_preference"
        val USER_STATUS = "user_status"
        val PERSONALIZED = true
        val NON_PERSONALIZED = false
        val PREFERENCES_NAME = "me.kandz.consentSDK"
        val DUMMY_BANNER = "ca-app-pub-3940256099942544/6300978111"

        lateinit var consenTSDK:ConsentSDK



        public fun getAdRequest(contexT: Context): AdRequest {
            if(isConsentPersonalized(contexT)){
                return  AdRequest.Builder().build()
            } else {
                return  AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter::class.java, getNonPersonalizedAdsBundle()).build()
            }
        }

        public fun isConsentPersonalized(context: Context):Boolean{
            var setting= initPreferences(context)
            return setting.getBoolean(ADS_PREFERENCE,PERSONALIZED)
        }

        public fun isConsentSetup(contexT: Context):Boolean{
            var setting = initPreferences(contexT)
            return setting.contains(ADS_PREFERENCE)
        }

        public fun clearConsent(contexT: Context){
            var setting = initPreferences(contexT)
            setting.edit().clear().apply()
        }

        fun initPreferences(contexT: Context): SharedPreferences {
            return contexT.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        }



        fun getNonPersonalizedAdsBundle(): Bundle {
            var extras = Bundle()
            extras.putString("npa","1")
            return extras
        }

        // Check the user location
        fun isUserLocationWithinEea(context: Context): Boolean {
            return initPreferences(context).getBoolean(USER_STATUS, false)
        }

        public fun setConSDK(conseNTSDK:ConsentSDK){
            this.consenTSDK = conseNTSDK
        }

        public fun getConSDK():ConsentSDK{
            return this.consenTSDK
        }

    }

}