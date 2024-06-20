package com.kg.myapplication.ui.fragments.main

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kg.myapplication.R
import com.kg.myapplication.data.entity.ShopItems
import com.kg.myapplication.data.entity.network.RetrofitBuilder
import com.kg.myapplication.databinding.FragmentMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder


class MainFragment : Fragment(R.layout.fragment_main), OrderAdapter.ItemClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    private val orderAdapter = OrderAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    private fun initAdapter() {
        binding.orderRV.adapter = orderAdapter
        binding.orderRV.layoutManager =
            GridLayoutManager(this.context, 2, GridLayoutManager.VERTICAL, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        retrofitGetData()
    }

    private fun retrofitGetData() {
        RetrofitBuilder().create().getOrderItems().enqueue(object :
            Callback<ArrayList<ShopItems>> {
            override fun onResponse(
                call: Call<ArrayList<ShopItems>>,
                response: Response<ArrayList<ShopItems>>
            ) {
                Log.d("ololo", "${response.body()}")
                response.body()?.let { orderAdapter.update(it) }
            }

            override fun onFailure(call: Call<ArrayList<ShopItems>>, t: Throwable) {
                Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onItemClick(name: String, price: String) {
        val alertDialog: AlertDialog = AlertDialog.Builder(requireContext()).create()
        alertDialog.setTitle("Внимание!")
        alertDialog.setMessage("Сейчас вы заказываете $name за $price!")
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Согласен") { dialog, which ->
            sendToWA(
                name,
                price
            )
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Открыть карту") {dialog, which ->
            findNavController().navigate(R.id.action_mainFragment_to_mapFragment)
        }
        alertDialog.show()
    }

    private fun sendToWA(name: String, price: String) {
        var textMassage = "Здравствуйте, хочу заказать у вас товары: $name за $price."

        val url =
            "https://api.whatsapp.com/send?phone=$+996500023120" + "&text=" + URLEncoder.encode(
                textMassage,
                "UTF-8"
            )
        val i = Intent(Intent.ACTION_VIEW)
        i.setData(Uri.parse(url))
        startActivity(i)
    }
}