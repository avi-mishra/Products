package Activity

import Adapter.ClickListener
import Adapter.ProductAdapter
import android.content.Intent
import android.example.products.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import models.MyProducts

class MainActivity : AppCompatActivity(), ClickListener{

    var myProducts=ArrayList<MyProducts>()
    var uri:String?=null
    val firebaseUser= FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(firebaseUser==null)
        {
            val i=Intent(this,LogIn::class.java)
            startActivity(i)
            finish()
        }
        rvProducts.layoutManager=GridLayoutManager(this@MainActivity,2)
        var adapter=ProductAdapter(myProducts,this@MainActivity)
        rvProducts.adapter=adapter
        getResults(adapter)

    }
    fun getResults(adapter: ProductAdapter){
        var prod:MyProducts
        val queue = Volley.newRequestQueue(this)
        val url = "https://makeup-api.herokuapp.com/api/v1/products.json?brand=maybelline"

        val jsonObjectRequest = JsonArrayRequest(
                Request.Method.GET, url, null,
                { response ->
                    val size = response.length()
                    for (i in 0 until size) {
                        val jsonObj = response.getJSONObject(i)
                        val img = jsonObj.get("image_link").toString()
                        val name = jsonObj.get("name").toString()
                        val price = jsonObj.get("price").toString()
                        val rate = jsonObj.get("rating").toString()
                        uri = jsonObj.get("product_link").toString()
                        prod = MyProducts(img, name, "$$price", rate, uri!!)
                        myProducts.add(prod)
                        adapter.notifyDataSetChanged()
                        Log.d("name", "getResults: $myProducts")
                    }
                },
                {
                    Log.d("volleyError", "getResults: ${it.localizedMessage}")
                })
        queue.add(jsonObjectRequest)
    }

    override fun onItemClick(products: MyProducts) {
        val i=Intent(this@MainActivity,ViewProductActivity::class.java)
        i.putExtra("url",uri)
        startActivity(i)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (firebaseUser!=null) {
            menuInflater.inflate(R.menu.nav_menu, menu)
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.navSignOut -> {
                Toast.makeText(this@MainActivity, "Signing Out", Toast.LENGTH_SHORT).show()
                FirebaseAuth.getInstance().signOut()
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
                mGoogleSignInClient.signOut(); intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                val i = Intent(this@MainActivity, LogIn::class.java)
                startActivity(i)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}