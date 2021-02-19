package com.gabriel.ribeiro.cademeupet.ui.activitys

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.gabriel.ribeiro.cademeupet.R
import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import com.gabriel.ribeiro.cademeupet.databinding.ActivityPrincipalBinding
import com.gabriel.ribeiro.cademeupet.repository.MainRepository
import com.gabriel.ribeiro.cademeupet.ui.viewmodel.MainViewModel
import com.gabriel.ribeiro.cademeupet.utils.Constants
import com.gabriel.ribeiro.cademeupet.utils.CustomToast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrincipalActivity : AppCompatActivity() {

    private var _binding : ActivityPrincipalBinding? = null
    private val binding : ActivityPrincipalBinding get() = _binding!!

    private lateinit var appBarConfiguration: AppBarConfiguration
    val mainViewModel: MainViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.navHostFragment)

        binding.bottomNavigationViewMain.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupActionBarWithNavController(this,navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.mapsFragment -> supportActionBar?.hide()
                R.id.registerFragment -> {binding.bottomNavigationViewMain.visibility = View.GONE
                supportActionBar?.show()}
                R.id.loginFragment -> {supportActionBar?.hide()
                    binding.bottomNavigationViewMain.visibility = View.GONE}
                R.id.chatFragment -> supportActionBar?.hide()
                else -> {
                    binding.bottomNavigationViewMain.visibility = View.VISIBLE
                    supportActionBar?.show()
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean = NavigationUI.navigateUp(navController,appBarConfiguration)

    private fun isServiceOk() : Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val available = googleApiAvailability.isGooglePlayServicesAvailable(this)
        when {
            available == ConnectionResult.SUCCESS -> {
                Log.i(Constants.TAG, "isServiceOk: service work")
                return true
            }
            googleApiAvailability.isUserResolvableError(available) -> {
                Log.i(Constants.TAG, "isServiceOk: Erro with service, but can fixed it ")
                val dialog = googleApiAvailability.getErrorDialog(this,available, Constants.ERROR_SERVICE_DIALOG)
                dialog.show()

            }
            else -> {
                Log.i(Constants.TAG, "isServiceOk: Erro with Service")
            }
        }
        return false

    }

    private fun isMapEnabled() : Boolean{
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGps()
            return false
        }
        return true
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("O aplicativo necessita do GPS para funcinar corretamente")
        builder.setCancelable(false)
        builder.setPositiveButton(getString(R.string.sim)) { _, _ ->
            val intentEnableGps = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(intentEnableGps, Constants.ENABLE_GPS_CODE)
        }
        val alert = builder.create()
        alert.show()
    }

    private fun onGetPermissionLocation(){
        if (!isAccessLocationGranted()){
            requestAccessLocationPermission()
        }else{

        }

    }

    private fun checkMapServices() : Boolean{
        if(isServiceOk()){
            if (isMapEnabled()){
                return true
            }


        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == Constants.LOCATION_PERMISSION){
            if (grantResults.firstOrNull() != PackageManager.PERMISSION_GRANTED){
                CustomToast.showToast(applicationContext,"O aplicativo necessita da " +
                        "sua localização para funcionar corretamente")
                requestAccessLocationPermission()
            }
        }
    }

    override fun onResume() {
        checkMapServices()
        super.onResume()
    }

    private fun requestAccessLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION), Constants.LOCATION_PERMISSION)
    }

    private fun isAccessLocationGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Constants.ENABLE_GPS_CODE -> {
                Log.i(Constants.TAG, "onActivityResult: Ok")
                onGetPermissionLocation()
            }
            else -> {

            }
        }
    }

    fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
    }

    fun dismissProgressBar(){
        binding.progressBar.visibility = View.GONE
    }
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}