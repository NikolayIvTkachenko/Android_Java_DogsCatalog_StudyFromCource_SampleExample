package com.rsh.docs.view


import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

import com.rsh.docs.R
import com.rsh.docs.databinding.FragmentDetailBinding
import com.rsh.docs.databinding.SendSmsDialogBinding
import com.rsh.docs.model.DogBreed
import com.rsh.docs.model.DogPalette
import com.rsh.docs.model.SmsInfo
import com.rsh.docs.util.getProgressDrawable
import com.rsh.docs.util.loadImage
import com.rsh.docs.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * Created by Nikolay Tkachenko
 *
 **/

class DetailFragment : Fragment() {


    private lateinit var  viewModel: DetailViewModel
    private var dogUuid = 0

    private lateinit var dataBinding: FragmentDetailBinding
    private var sendSmsStarted = false

    private var currentDog: DogBreed? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        //return inflater.inflate(R.layout.fragment_detail, container, false)
        setHasOptionsMenu(true)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let{
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid
            Log.d("UUID","DetailFragment  UUID="+dogUuid)

        }
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.fetch(dogUuid)

        observeViewModel()


    }

    private fun observeViewModel() {
        viewModel.dogLiveData.observe(this, Observer { dog ->
            currentDog = dog
            dog?.let{

                dataBinding.dog = dog

                it.imageUrl?.let {
                    setupBackgroundColor(it)
                }

                /*dogName.text = dog.dogBreed
                dogPorpose.text = dog.bredFor
                dogTemparement.text = dog.temperamenrt
                dogLifespan.text = dog.lifeSpan
                context?.let {
                    dogImage.loadImage(dog.imageUrl, getProgressDrawable(it))
                }*/
            }
        })
    }

    private fun setupBackgroundColor(url: String){
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object: CustomTarget<Bitmap>(){
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate{palette ->
                            val intColor = palette?.vibrantSwatch?.rgb ?:0
                            val myPalette = DogPalette(intColor)
                            dataBinding.palette = myPalette
                        }
                }

            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.action_send_sms -> {
                sendSmsStarted = true
                (activity as MainActivity).checkSmsPermission()
            }

            R.id.action_share -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun onPermissionResult(permissionGranted: Boolean){
        if(sendSmsStarted && permissionGranted){
            context?.let{
                val smsInfo = SmsInfo("", "${currentDog?.dogBreed} bred for ${currentDog?.bredFor}"
                    , currentDog?.imageUrl)
                val dialogBinding
                        = DataBindingUtil.inflate<SendSmsDialogBinding>(LayoutInflater.from(it),
                    R.layout.send_sms_dialog, null, false)

                AlertDialog.Builder(it)
                    .setView(dialogBinding.root)
                    .setPositiveButton("Send SMS"){
                        dialog, which ->
                            if(!dialogBinding.smsDestination.text.isNullOrEmpty()){
                                smsInfo.to = dialogBinding.smsDestination.text.toString()
                                sendSms(smsInfo)
                            }
                    }
                    .setNegativeButton("Cancel"){
                        dialog, which ->
                    }
                    .show()
                dialogBinding.smsInfo = smsInfo
            }
        }
    }

    private fun sendSms(smsInfo: SmsInfo) {
        val intent = Intent(context, MainActivity::class.java)
        val pi = PendingIntent.getActivity(context, 0, intent, 0)
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(smsInfo.to, null, smsInfo.text, pi, null)
        
    }

}
