package com.rsh.docs.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.rsh.docs.R
import com.rsh.docs.databinding.ItemDogBinding
import com.rsh.docs.model.DogBreed
import com.rsh.docs.util.getProgressDrawable
import com.rsh.docs.util.loadImage
import kotlinx.android.synthetic.main.item_dog.view.*

/**
 * Created by Nikolay Tkachenko
 *
 **/

class DogsListAdapter(val dogsList: ArrayList<DogBreed>): RecyclerView.Adapter<DogsListAdapter.DogViewHolder>(), DocClickListener {


    override fun onDogClicked(v: View) {
        val uuid = v.dogId.text.toString().toInt()
        val action = ListFragmentDirections.actionDetailFragment()
        Log.d("UUID","ADAPTER  UUID="+uuid )
        action.dogUuid = uuid
        Navigation.findNavController(v).navigate(action)
    }

    fun updateDogList(newDogsList: List<DogBreed>){
        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.item_dog, parent, false)
        val view = DataBindingUtil.inflate<ItemDogBinding>(inflater, R.layout.item_dog, parent, false)
        return DogViewHolder(view)
    }

    override fun getItemCount() = dogsList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {

        holder.view.dog = dogsList[position]
        holder.view.listener = this


        //holder.view.name.text = dogsList[position].dogBreed
        //holder.view.lifespan.text = dogsList[position].lifeSpan
        /*holder.view.setOnClickListener{
            val action = ListFragmentDirections.actionDetailFragment()
            Log.d("UUID","ADAPTER  UUID="+dogsList[position].uuid)
            action.dogUuid = dogsList[position].uuid
            Navigation.findNavController(it).navigate(action)
        }
        holder.view.imageView.loadImage(dogsList[position].imageUrl, getProgressDrawable(holder.view.imageView.context))*/



    }


    //class DogViewHolder(var view: View) : RecyclerView.ViewHolder(view)
    class DogViewHolder(var view: ItemDogBinding) : RecyclerView.ViewHolder(view.root)

}