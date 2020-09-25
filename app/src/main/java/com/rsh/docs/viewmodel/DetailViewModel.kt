package com.rsh.docs.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rsh.docs.model.DogBreed
import com.rsh.docs.model.DogDatabase
import kotlinx.coroutines.launch
import java.lang.Appendable
/**
 * Created by Nikolay Tkachenko
 *
 **/

class DetailViewModel(application: Application): BaseViewModel(application) {

    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch(uuid: Int){
        launch {
            val dog = DogDatabase(getApplication()).dogDao().getDog(uuid)
            dogLiveData.value = dog
        }
    }
}