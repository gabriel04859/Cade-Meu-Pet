package com.gabriel.ribeiro.cademeupet.ui.fragments

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.model.Address
import com.gabriel.ribeiro.cademeupet.utils.Constants
import com.gabriel.ribeiro.cademeupet.utils.CustomToast
import com.gabriel.ribeiro.cademeupet.utils.Location
import com.gabriel.ribeiro.cademeupet.utils.OnGetCurrentLatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.*

class PickAddressMapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val location = Location()



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_pick_address_maps, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        if (googleMap != null) {
            mMap = googleMap
            activity?.let { location.getCurrentLocation(mMap, it.applicationContext) }
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return
        }
        mMap.isMyLocationEnabled = true
        mMap.setOnMapClickListener { latLng ->
            showDialogChoiceAddress(latLng)
        }

    }

    private fun showDialogChoiceAddress(latLng: LatLng) {
        val alert = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.dialog_confirm_address,null)
        alert.setView(view)
        val dialog = alert.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val textViewAddress : TextView = view!!.findViewById(R.id.textViewAddressDialog)
        val textViewNo: TextView = view.findViewById(R.id.textViewNoDialog)
        val buttonConfirm : Button = view.findViewById(R.id.buttonConfirmDialogAddress)

        textViewAddress.text = latLng.toString()

        val geocoder = Geocoder(activity, Locale.getDefault())
        try{
            val addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1)
            val addressNotFormatted = addressList[0].getAddressLine(0)
            Log.i(Constants.TAG, "confirmAddressDialog: AddressNotFormateed: $addressNotFormatted")
            val splitAddress = addressNotFormatted.split(",")


            Log.i(Constants.TAG, "confirmAddressDialog: ${addressList}")

            val address = Address(splitAddress[0], splitAddress[1], splitAddress[2],
                    splitAddress[4], latLng?.latitude, latLng?.longitude)

            textViewAddress.text = "${address.street} - ${address.neighborhood}"
            textViewNo.setOnClickListener {
                dialog.cancel()
            }
            buttonConfirm.setOnClickListener {
                val addressBundle = Bundle().apply {
                    putParcelable("latLngAddress",address)
                }
                findNavController().navigate(R.id.action_pickAddressMapsFragment_to_newPostFragment,addressBundle)
                dialog.cancel()
            }

        }catch (e : IOException){
            Log.i(Constants.TAG, "confirmAddressDialog: Erro ao obter o endereco: ${e.message} ")

            dialog.cancel()
            buttonConfirm.isEnabled = false
        }

    }

}