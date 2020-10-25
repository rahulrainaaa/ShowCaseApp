package app.demo.showcaseapp

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val TAG = "aaaaaaaaaaaaa"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { callGetAESCredentailService() }
    }

    private fun callLoginService() = CoroutineScope(Dispatchers.Main).launch {

        val loginParams = HashMap<String, Any>()
        loginParams["username"] = "user1"
        loginParams["password"] = "password1"
        val cipherRequest = AESManager.encrypt(JSONObject(loginParams as Map<String, Any>).toString())
        val checking = AESManager.decrypt(cipherRequest)
        Log.d(TAG, "$checking")
        val httpRequestModel = HttpRequestModel(cipherRequest)
        val pd = ProgressDialog.show(this@MainActivity, title, "Login")
        pd.setCancelable(false)
        pd.show()
        val httpResponseModel = withContext(Dispatchers.IO) { RetrofitClientBuilder.loginWebService.process(httpRequestModel) }
        pd.dismiss()
        Log.d(TAG, httpResponseModel.response ?: "NULL")
        val strResponse = AESManager.decrypt(httpResponseModel.response!!)
        Log.d(TAG, strResponse ?: "EMPTY")
    }

    private fun callGetAESCredentailService() = CoroutineScope(Dispatchers.Main).launch {

        val pd = ProgressDialog.show(this@MainActivity, TAG, "AES Service")
        pd.setCancelable(false)
        pd.show()
        val httpResponseModel = withContext(Dispatchers.IO) { RetrofitClientBuilder.aesCredWebService.process(HttpRequestModel(RSAManager.publicKeyString())) }
        Log.d(TAG, httpResponseModel.response ?: "NULL")
        val plainText = RSAManager.decrypt(httpResponseModel.response!!)
        Log.d(TAG, plainText)
        pd.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}