package com.yourcompany.nfcplugin

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant



class MainActivity(): FlutterActivity() {
  private val CHANNEL = "sam0610.nixon.io/nfc"
    private var mNfcAdapter: NfcAdapter? = null
    private var channel:MethodChannel? =null
    private var writemode:Boolean = false
    private var msgToWrite: String =""

  override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      GeneratedPluginRegistrant.registerWith(this)
      mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
      channel =MethodChannel(flutterView, CHANNEL)

      channel!!.setMethodCallHandler { call, result ->

          when (call.method) {
              "writeMode" -> {
                  writemode = true
                  msgToWrite = call.arguments.toString()
                  result.success("write mode on")
              }
              "readMode" -> {
                  writemode = false
                  result.success("read mode on")
              }
              else -> result.notImplemented()
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
        if(intent!=null){
        if(writemode){
            var message = ""
            try{
                if(NFCUtil.createNFCMessage2(msgToWrite, intent)) message="done"
            }catch(e:Exception){
                message= e.message.toString()
            }
            writemode=false
            channel!!.invokeMethod("wroteNfc",message)
        } else{
            var message = receiveMessageFromDevice(intent)
            channel!!.invokeMethod("gotNfc",message)


        }  
        //var message = NFCUtil.retrieveNFCMessage(intent)
        }
    }

    private fun receiveMessageFromDevice(intent: Intent):String {
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action || true) {
            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            with(parcelables) {
                val inNdefMessage = this[0] as NdefMessage
                val inNdefRecords = inNdefMessage.records
                val ndefRecord0 = inNdefRecords[0]

                val inMessage = String(ndefRecord0.payload)
                return inMessage
            }
        }

        return "please check your data type"
    }


}
