package com.example.practice_demo.profile.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.practice_demo.R
import com.example.practice_demo.databinding.FragmentProfileBinding
import com.example.practice_demo.helper.FileUtils
import com.example.practice_demo.helper.SaveSharedPreference
import com.example.practice_demo.login.data.model.UserLoginResponse
import com.example.practice_demo.profile.data.model.UserProfile
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException

private lateinit var binding: FragmentProfileBinding
const val mediaUrlPrefix = "http://api.mcomputing.eu/mobv/uploads/"

class ProfileFragment : Fragment() {
    private lateinit var profileImg: CircleImageView
    lateinit var profileViewModel: ProfileViewModel
    lateinit var user: UserLoginResponse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Options menu
        setHasOptionsMenu(true)
        user = activity?.let { activity ->

            SaveSharedPreference.getUser(activity)
                ?: throw IOException("User not found")
        }!!

        profileViewModel = ViewModelProvider(this, ProfileViewModelFactory(user))
            .get(ProfileViewModel::class.java)

        profileViewModel.profilePhotoChangedFlag.observe(viewLifecycleOwner, Observer {hasPhoto ->
            //TODO(Tu treba updatnut UI po zmene fotky)
            // hasPhoto urcuje ci po zmene user ma profilovku, alebo ju zmazal (netreba robit request)
            if(hasPhoto) {
                changeImage()
            }
        })

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
            binding.profile = UserProfile(user.username, user.email)

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun changeImage() {
        context?.let {
            Glide.with(it)
                .load(mediaUrlPrefix + user.profile)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(profileImg)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val user = activity?.let {activity ->

            SaveSharedPreference.getUser(activity)
                ?: throw IOException("User not found")
        }

        profileImg = view.findViewById(R.id.profile_image)

        if (user != null) {
            if (user.profile !== ""){
                context?.let {
                    Glide.with(it).load(mediaUrlPrefix + user.profile)
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(profileImg)
                }
            }
        }

        profileImg.setOnClickListener {
            if (askForPermissions()) {
                // Gallery intent
                val intent = Intent(Intent.ACTION_PICK)
                // V galerii budu iba obrazky
                intent.type = "image/*"
                // Start intentu
                startActivityForResult(intent, 1)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu, menu)
    }

    private fun isPermissionsAllowed(): Boolean {
        return context?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        } == PackageManager.PERMISSION_GRANTED
    }

    private fun askForPermissions(): Boolean {
        if (!isPermissionsAllowed()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            }
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            //profileImg.setImageURI(data?.data) // handle chosen image
            data?.data?.let {
                val test = FileUtils.getPath(this.context, it)
                profileViewModel.changePhoto(test)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, 1)
                } else {
                    askForPermissions()
                }
                return
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle("Permission Denied")
        }
    }
}