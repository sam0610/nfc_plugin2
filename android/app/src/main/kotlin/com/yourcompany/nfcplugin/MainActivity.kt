package com.yourcompany.nfcplugin

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant



class MainActivity(): FlutterActivity() {
  private val CHANNEL = "sam0610.nixon.io/nfc"
    private var mNfcAdapter: NfcAdapter? = null
    private var channel:MethodChannel? =null
    private var writeMode:Boolean = false
    private var readMode:Boolean = false
    private var msgToWrite: String =""

  override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      GeneratedPluginRegistrant.registerWith(this)
      mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
      channel =MethodChannel(flutterView, CHANNEL)

      channel!!.setMethodCallHandler { call, result ->
          when (call.method) {
              "write" -> {
                    mNfcAdapter?.let {
                        NFCUtil.enableNFCInForeground(it, this, javaClass)}
                    writeMode = true
                    msgToWrite = call.arguments.toString()
                    result.success("Tap Card to write")
              }
              "read" -> {
                    mNfcAdapter?.let {
                        NFCUtil.enableNFCInForeground(it, this, javaClass)}
                    readMode = true
                    result.success("Tap Card to read")
              }
              else -> result.notImplemented()
          }
      }

  }

    override fun onResume() {
        super.onResume()
        if(readMode || writeMode){
            mNfcAdapter?.let {
                NFCUtil.enableNFCInForeground(it, this, javaClass)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if(readMode || writeMode){
            mNfcAdapter?.let {
                NFCUtil.disableNFCInForeground(it, this)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if(intent!=null){
            var message = ""
            if(writeMode){
                try{
                    if(NFCUtil.createNFCMessage(msgToWrite,intent)){
                        message="write Nfc done"    
                        channel!!.invokeMethod("wroteNfc",message)
                        writeMode=false
                    }
                    }
                catch(e:Exception){
                    message= e.message.toString()
                }
                }
            else if(readMode)
            {
                var message = NFCUtil.retrieveNFCMessage(intent)
                channel!!.invokeMethod("gotNfc",message)
                readMode = false
            }
        }
    }


}
