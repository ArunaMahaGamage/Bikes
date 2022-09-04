package com.example.bikes.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.example.bikes.BuildConfig
import com.example.bikes.ContextAwareApplication
import com.example.bikes.R
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.util.*

const val GET_IMAGE_FROM_GALLERY = 5001
const val GET_IMAGE_FROM_CAMERA = 5002
const val REQUEST_READ_WRITE_PERMISSIONS = 6001
const val REQUEST_CAMERA_PERMISSIONS = 6002
const val REQUEST_ID_MULTIPLE_PERMISSIONS = 6003
private const val REQUEST_ACCESS_FINE_LOCATION = 1451
private const val REQUEST_ACCESS_BACKGROUND_LOCATION = 1452

abstract class BaseFragment : Fragment() {

    companion object {
        var shouldPopFullBackstack = false
    }

    protected abstract var layoutID: Int
    protected var mBindingRoot: ViewDataBinding? = null

    private lateinit var baseView: View

    private var imageUri : Uri? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModels()
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        baseView = inflater.inflate(layoutID, container, false)

        mBindingRoot = DataBindingUtil.bind(baseView)
        mBindingRoot!!.lifecycleOwner = this

        initBinding()

        return baseView
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    override fun onResume() {
        if(this.fragmentTag() != "ProfileDashboardFragment"){
            if(this.fragmentTag() == "ProfileFragment") shouldPopFullBackstack = false
            if (shouldPopFullBackstack) shouldPopFullBackstack = popBackStack()
        }
        setAsCurrentActiveFragment()
        super.onResume()
    }

    protected fun navigateTo(navigationID: Int) {
        NavHostFragment.findNavController(this).navigate(navigationID)
    }

    protected fun navigateTo(action: NavDirections) {
        NavHostFragment.findNavController(this).navigate(action)
    }

    protected fun navigateToUrl(activity: Activity, url: String?) {
        url?.let {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(it)
            val packageManager = activity.packageManager
            if (i.resolveActivity(packageManager) == null) {
                // No Activity found that can handle this intent.
            } else{
                startActivity(i)
            }

        }
    }

    protected fun hideKeyboard() {
        (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    // This method links a view model provider to a nav graph (instead of an activity) so the view model is shared among all fragments in the graph
    protected fun getGraphViewModelProvider(): ViewModelProvider {
        val graphID = NavHostFragment.findNavController(this).graph.id
        return ViewModelProvider(NavHostFragment.findNavController(this).getViewModelStoreOwner(graphID), ViewModelProvider.AndroidViewModelFactory(activity?.application!!))
    }

    /**
     * Method to for the enclosing [Activity] to pass the [onBackPressed][Activity.onBackPressed] event to the fragment.
     *
     * sub-classes can override to customize behaviour.
     *
     * @return True if the event has been handled, false otherwise.
     */
    open fun onBackPressed(): Boolean {
        return popBackStack()
    }

    fun popBackStack(): Boolean {
        return try {
            NavHostFragment.findNavController(this).navigateUp()
        } catch (e: Exception) {
            false
        }
    }

    protected fun popBackStack(@IdRes destinationId: Int, inclusive: Boolean): Boolean {
        return try {
            NavHostFragment.findNavController(this).popBackStack(destinationId, inclusive)
        } catch (e: Exception) {
            false
        }
    }

    fun popFullBackstack() {
        if(this.fragmentTag() != "ProfileFragment") {
            shouldPopFullBackstack = true
            popBackStack()
        }else{
            shouldPopFullBackstack = false
        }
    }

    private fun setAsCurrentActiveFragment() {
        val thisActivity = activity
//        if (thisActivity is DashboardActivity) thisActivity.currentActiveFragment = this
    }

    protected abstract fun initViewModels()
    protected abstract fun initBinding()
    protected abstract fun initObservers()
    protected abstract fun fragmentTag(): String

    fun makeSnack(msg: String, duration: Int) {
        hideKeyboard()
        Snackbar.make(baseView, msg, duration).show()
    }

    protected fun selectImageDialog() {
        AlertDialog.Builder(context, R.style.AlertDialogTheme)
            .setMessage("How would you like to add an image?")
            .setNegativeButton("Gallery") { _, _ -> checkGalleryPermission() }
            .setPositiveButton("Camera") { _, _ -> checkCameraPermission() }
            .show()
    }

    private fun checkCameraPermission() {
        context?.let { context ->
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSIONS
            )
            } else selectImageFromCamera()
        }
    }

    private fun checkGalleryPermission() {
        context?.let { context ->
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    REQUEST_READ_WRITE_PERMISSIONS
                )
            } else selectImageFromGallery()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) selectImageFromCamera()
                else makeSnack("Unable to take picture without camera permissions", Snackbar.LENGTH_SHORT)
            }
            REQUEST_READ_WRITE_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) selectImageFromGallery()
                else makeSnack("Unable to access gallery without file access permissions", Snackbar.LENGTH_SHORT)
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GET_IMAGE_FROM_GALLERY)
    }

    private fun selectImageFromCamera() {
        context?.let { context ->
            val outputImage = File(activity?.externalCacheDir, "${UUID.randomUUID()}.jpg")
            if (outputImage.exists()) outputImage.delete()
            outputImage.createNewFile()

            imageUri = FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",
                outputImage
            )

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, GET_IMAGE_FROM_CAMERA)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GET_IMAGE_FROM_GALLERY) {
                receivedImageFromGallery(Uri.parse(data?.data.toString()))
            }
            if (requestCode == GET_IMAGE_FROM_CAMERA) {
                receivedImageFromCamera(Uri.parse(imageUri.toString()))
            }
        }
    }

    protected open fun receivedImageFromGallery(uri: Uri) {
        Log.e("No Function Override", "receivedImageFromGallery(uri: Uri) not overridden in ${fragmentTag()}")
    }

    protected open fun receivedImageFromCamera(uri: Uri) {
        Log.e("No Function Override", "receivedImageFromCamera(uri: Uri) not overridden in ${fragmentTag()}")
    }

    protected fun refreshData(completion: (() -> Unit)?) {
//        (activity as? DashboardActivity)?.refreshData(completion)
    }

    @CallSuper
    protected open fun showLoading() {
//        (activity as? AuthActivity)?.showLoading()
//        (activity as? DashboardActivity)?.showLoading()
    }

    @CallSuper
    protected open fun dismissLoading() {
//        (activity as? AuthActivity)?.dismissLoading()
//        (activity as? DashboardActivity)?.dismissLoading()
    }

    /*protected fun likeReview(review: ReviewModel?) {
        review?.let {
            if (review.liked) {
                return
            }
            CoroutineScope(Dispatchers.Main).launch {
                showLoading()
                val networkResponse =
                    it.productID?.let { it1 -> ServiceManager.reviewService.setReviewLiked(it1, it.id) }
                networkResponse?.data?.let { response ->
                    if (response.status == "success") {
                        getReview(it)
                    } else {
                        dismissLoading()
                        makeSnack(
                            "Unable to like review, try again later...",
                            Snackbar.LENGTH_SHORT
                        )
                    }
                } ?: run {
                    dismissLoading()
                    makeSnack("Unable to like review, try again later...", Snackbar.LENGTH_SHORT)
                }
            }
        }
    }*/

    /*private suspend fun getReview(review: ReviewModel) {
        val networkResponse =
            review.productID?.let { ServiceManager.reviewService.getReview(it, review.id) }
        dismissLoading()
        networkResponse?.data?.let { response ->
            if (response.status == "success") {
                AppDatabase.database.reviewModelDao.insert(response.review.getReviewModel(review.isBest))
            } else makeSnack("Unable to like review, try again later...", Snackbar.LENGTH_SHORT)
        } ?: run {
            makeSnack(
                "Unable to like review, try again later...",
                Snackbar.LENGTH_SHORT
            )
        }
    }*/

    public fun checkAndRequestPermissions(): Boolean {
        val context = ContextAwareApplication.applicationContext()
        val loc =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        val loc2 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val loc3 =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        val call = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
        val accessNetworkState =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)
        val accessWifiState =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE)
        val systemAlertWindow =
            ContextCompat.checkSelfPermission(context, Manifest.permission.SYSTEM_ALERT_WINDOW)
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (loc3 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
        if (call != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE)
        }

        /*if (accessNetworkState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_NETWORK_STATE);
        }

        if (accessWifiState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_WIFI_STATE);
        }*/if (systemAlertWindow != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SYSTEM_ALERT_WINDOW)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    public fun overlayPermission() {
        try {
            val context = ContextAwareApplication.applicationContext()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(context)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + context.packageName)
                    )
                    startActivityForResult(intent, 0)
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    open fun getAccessFineLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    (requireActivity() as Activity?)!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_ACCESS_FINE_LOCATION
                )
            } else {
                getAccessBackgroundLocationPermission()
            }
        }
    }
    public fun getAccessBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    REQUEST_ACCESS_BACKGROUND_LOCATION
                )
            }
        }
    }
}