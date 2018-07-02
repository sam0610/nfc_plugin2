package com.yourcompany.nfcplugin

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant



class MainActivity(): FlutterActivity() {
  private val CHANNEL = "sam0610.nixon.io/nfc"
    private var message:String?=null
    private var mNfcAdapter: NfcAdapter? = null
    private var channel:MethodChannel? =null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    GeneratedPluginRegistrant.registerWith(this)

      mNfcAdapter = NfcAdapter.getDefaultAdapter(this)

    channel =MethodChannel(flutterView, CHANNEL)
      channel!!.setMethodCallHandler { call, result ->
      // TODO
      if(call.method == "getBatteryLevel"){
        //val msg = NFCUtil.retrieveNFCMessage(intent)
        if (!message!!.isEmpty() ){
          result.success(message)

        }else{
          result.error("NA","Error ",null)
        }
      }else{
        result.notImplemented()
      }
    }


  }

    override fun onResume() {
        super.onResume()
        mNfcAdapter?.let {
            NFCUtil.enableNFCInForeground(it, this, javaClass)
        }
    }

    override fun onPause() {
        super.onPause()
        mNfcAdapter?.let {
            NFCUtil.disableNFCInForeground(it, this)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
//        val messageWrittenSuccessfully = NFCUtil.createNFCMessage(message.toString(), intent)
        message = NFCUtil.retrieveNFCMessage(intent)
        channel!!.invokeMethod("getBatteryLevel",message)
    }


  private fun getMessage():String{
    return "ok from sam"
  }



}
