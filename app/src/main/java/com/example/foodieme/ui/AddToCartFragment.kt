package com.example.foodieme.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.foodieme.R
import com.example.foodieme.databinding.AddToCartFragmentBinding
import com.example.foodieme.databinding.CartScreenBinding
import com.example.foodieme.databinding.HomeScreenBinding
import com.example.foodieme.viewmodels.AddToCartViewModel
import com.example.foodieme.viewmodels.AddToCartViewModelFactory
import com.example.foodieme.viewmodels.HomeScreenViewModel
import com.example.foodieme.viewmodels.HomeViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.cart_screen.*
import kotlin.reflect.jvm.internal.impl.resolve.constants.DoubleValue


@AndroidEntryPoint
class AddToCartFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: AddToCartFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.add_to_cart_fragment, container, false)


        var totalItemPrice : Double =2.0

        val application = requireNotNull(this.activity).application

        val addToCartViewModelFactory = AddToCartViewModelFactory(application)

        val addToCartViewModel = ViewModelProvider(this, addToCartViewModelFactory).get(
            AddToCartViewModel::class.java)

        binding.cartViewModel = addToCartViewModel

        binding.lifecycleOwner = this

        val cartAdapter = AddToCartAdapter(AddToCartClickListener {
            addToCartViewModel.onClearWithId(it)
        }, AddToQuantityClickListener {
            addToCartViewModel.onAddWithId(it)
        }, SubtractFromQuantityClickListener {
            addToCartViewModel.onSubtractWithId(it)
        });


        binding.addToCartList.adapter = cartAdapter

        addToCartViewModel.checkoutPage.observe(viewLifecycleOwner, Observer { cartItem ->
            cartItem.apply {
                cartAdapter?.checkoutMenu = cartItem
            }
        })




        addToCartViewModel.navigateToAddToOrderScreen.observe(viewLifecycleOwner, Observer { menu ->
            if(menu!=null){
                if (findNavController().currentDestination?.id == R.id.addToCartFragment) {
                    this.findNavController().navigate(AddToCartFragmentDirections.actionAddToCartFragmentToDeliveryPageFragment(menu.toTypedArray()))
                    addToCartViewModel.onOrderScreenNavigated()

                }
            }
        })

        addToCartViewModel.navigateToToast.observe(viewLifecycleOwner, Observer {
                if(it==true) {
                    Toast.makeText(activity, "A delivery is already in Progress!!", Toast.LENGTH_LONG).show()

                    addToCartViewModel.onToastNavigated()

                }
        })






        return binding.root




    }


}