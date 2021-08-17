package Activity

import android.annotation.SuppressLint
import android.example.products.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_view_product.*

class ViewProductActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_product)

        var url=intent.getStringExtra("url")

        wvProduct.webViewClient= WebViewClient()
        wvProduct.apply {
            loadUrl(url!!)
            settings.javaScriptEnabled=true
        }
    }
    override fun onBackPressed() {
        if (wvProduct.canGoBack())
            wvProduct.goBack()
        else
            super.onBackPressed()
    }
}

