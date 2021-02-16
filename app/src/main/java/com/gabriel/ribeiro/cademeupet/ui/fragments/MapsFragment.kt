package com.gabriel.ribeiro.cademeupet.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.model.Post
import com.gabriel.ribeiro.cademeupet.ui.activitys.PrincipalActivity
import com.gabriel.ribeiro.cademeupet.ui.viewmodel.MainViewModel
import com.gabriel.ribeiro.cademeupet.utils.*
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.POST_KEY
import com.gabriel.ribeiro.cademeupet.utils.Constants.Companion.TAG

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlin.math.log

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, OnGetCurrentLatLng {
    private lateinit var mMap : GoogleMap
    private lateinit var mainViewModel: MainViewModel
    private val location = Location(this)

    lateinit var postList : MutableList<Post>
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        mainViewModel = (activity as PrincipalActivity).mainViewModel

    }


    override fun onMapReady(googleMap: GoogleMap?) {

        if (googleMap != null) {
            mMap = googleMap
            getAllPostsMarkers(mMap)
        }

        location.getCurrentLocation(mMap,requireContext())
      
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return
        }
        mMap.isMyLocationEnabled = true

    }



    private fun getAllPostsMarkers(googleMap: GoogleMap){
        mainViewModel.postList.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {

                    resource.data?.let { posts ->
                        postList = posts
                        for (post in posts) {
                            Log.d(TAG, "getAllPostsMarkers: posts")
                            val latLng = LatLng(post.address?.latitude!!, post.address.longitude!!)
                            val markerOptions = MarkerOptions()
                                    .position(latLng)
                                    .title(post.animal?.name)
                                    .snippet(
                                            getSnippet(post))
                                    .icon(setIconType(post))
                            val marker: Marker
                            marker = googleMap.addMarker(markerOptions)
                            marker.tag = post
                            googleMap.setOnInfoWindowClickListener(this)
                        }
                    }

                }
                is Resource.Failure -> {
                    CustomToast.showToast(requireContext(), getString(R.string.houve_um_erro))
                }
            }
        })
    }

    private fun setIconType(post: Post): BitmapDescriptor {
        return if(post.animal?.type == getString(R.string.cat)){
            bitmapDescriptionForVector(requireContext(),R.drawable.ic_cat)
        }else{
            bitmapDescriptionForVector(requireContext(),R.drawable.ic_dog)
        }
    }

    private fun getSnippet(post : Post) : String{
        return if (post.animal?.sex == getString(R.string.macho)){
            getString(R.string.macho)
        }else{
            getString(R.string.femea)
        }
    }

    private fun bitmapDescriptionForVector(context: Context, vectorId : Int): BitmapDescriptor {
        val background = ContextCompat.getDrawable(context,vectorId)
        background!!.setBounds(0,0, background.intrinsicWidth,background.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(background.intrinsicWidth,background.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)

    }

    override fun onInfoWindowClick(p0: Marker?) {
        val post = p0!!.tag as Post
        val postBundle = Bundle().apply {
            putParcelable(POST_KEY,post)
        }
        findNavController().navigate(R.id.action_mapsFragment_to_detailFragment,postBundle)

    }

    override fun onGetCurrentLatLng(latLng: LatLng) {
        val results = FloatArray(postList.size)
        for(post in postList){
            Log.d(TAG, "onGetCurrentLatLng: ${post.address}")
            android.location.Location.distanceBetween(latLng.latitude, latLng.longitude, post.address?.latitude!!,
                    post.address?.longitude!!,results)

        }
        for (result in results){
            Log.d(TAG, "onGetCurrentLatLng: Distancia é ${result}")
            Log.d(TAG, "onGetCurrentLatLng: Distancia em km ${result / 1000}")
        }

    }


}