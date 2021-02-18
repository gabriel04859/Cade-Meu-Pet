package com.gabriel.ribeiro.cademeupet.utils

import android.content.Context
import android.util.Log
import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.model.User
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.TAG
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class Location(private val onGetCurrentLatLng: OnGetCurrentLatLng? = null) {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    fun getCurrentLocation(mMap : GoogleMap, context : Context) = CoroutineScope(Dispatchers.IO).launch{
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        try {
            val location= fusedLocationProviderClient.lastLocation

            val currentLocation = LatLng(location.await().latitude,location.await().longitude)
            delay(2000L)
            onGetCurrentLatLng?.onGetCurrentLatLng(currentLocation)

            withContext(Dispatchers.Main){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,18F))
                Log.d(TAG, "getCurrentLocation: ${currentLocation.longitude}")
            }
        }catch (e : SecurityException){
            Log.i(TAG, "getCurrentLocation: Erro ao pegar localização atual: ${e.message} ")
        }
    }


}