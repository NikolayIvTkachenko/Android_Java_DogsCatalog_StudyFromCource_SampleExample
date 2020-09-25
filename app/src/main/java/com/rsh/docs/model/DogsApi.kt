package com.rsh.docs.model

import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created by Nikolay Tkachenko
 *
 **/

interface DogsApi {

    @GET("DevTides/DogsApi/master/dogs.json")
    fun getDogs(): Single<List<DogBreed>>

    //@GET("endpoint2.json")
    //fun getDataFromEndpoint2():

}

//https://raw.githubusercontent.com/DevTides/DogsApi/master/dogs.json