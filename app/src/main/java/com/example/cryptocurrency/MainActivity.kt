package com.example.cryptocurrency
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.cryptocurrency.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvAdapter: RvAdapter
    private lateinit var data: ArrayList<Modal>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        data = ArrayList<Modal>()
        apiData
        rvAdapter = RvAdapter(this,data)
        binding.Rv.layoutManager = LinearLayoutManager(this)
        binding.Rv.adapter = rvAdapter
    }
    val apiData: Unit
        get() {
            val url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"
            val queue = Volley.newRequestQueue(this)
            val jsonObjectRequest: JsonObjectRequest =

                @SuppressLint("NotifyDataSetChanged")
                object: JsonObjectRequest(Method.GET,url,null, Response.Listener {
                        response ->
                    try {
                        val dataArray = response.getJSONArray("data")
                        for (i in 0 until dataArray.length())
                        {
                            val dataObject = dataArray.getJSONObject(i)
                            val symbol = dataObject.getString("symbol")
                            val name = dataObject.getString("name")
                            val quote = dataObject.getJSONObject("quote")
                            val usd = quote.getJSONObject("USD")
                            val price = String.format("%.2f", usd.getDouble("price"))
                            data.add(Modal(name,symbol, price))
                        }
                        rvAdapter.notifyDataSetChanged()
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this, "Error 1", Toast.LENGTH_LONG).show()
                })
                {
                    override fun getHeaders(): Map<String, String> {

                        val header = HashMap<String, String>();
                        header["X-CMC_PRO_API_KEY"] = "51ab4f9a-096e-4964-9cec-be2402ea6725"
                        return header
                    }
                }
            queue.add(jsonObjectRequest)
        }
}