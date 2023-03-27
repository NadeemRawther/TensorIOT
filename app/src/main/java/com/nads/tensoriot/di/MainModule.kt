package com.nads.tensoriot.di


import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.nads.tensoriot.remote.DataSource
import com.nads.tensoriot.repo.TensorIotDefaultRep
import com.nads.tensoriot.repo.TensorIotRep
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope


@Module
@InstallIn(SingletonComponent::class)
class MainModule {
    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor.invoke { chain: Interceptor.Chain ->
                val orginal: Request = chain.request()
                val request = orginal.newBuilder()
                    .method(orginal.method,orginal.body)
                    .build()
                chain.proceed(request)
            }).build()
        return client
    }
    @Singleton
    @Provides
    fun providesRetrofitClient(): DataSource {
        val gson = GsonBuilder()
//            .registerTypeAdapter(VehicleListTypeAdapter::class.java, VehicleListTypeAdapter())
//            .registerTypeAdapter(PlanetListTypeAdapter::class.java, PlanetListTypeAdapter())
            .create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(providesOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(DataSource::class.java)
    }

    @Singleton
    @Provides
    fun provideRepository(service: DataSource,
                          @ApplicationScope coroutineScope: CoroutineScope): TensorIotRep{
        return TensorIotDefaultRep(service,coroutineScope)
    }

    //    @Singleton
//    @Provides
//    fun provideCoroutine(): CoroutineDispatcher {
//        return Dispatchers.IO
//    }
    @Singleton
    @Provides
    @ApplicationScope
    fun providesCoroutineScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)


//    @Singleton
//    @Provides
//    fun provideDataBase(@ApplicationContext context: Context): LandFindDataBase {
//        return Room.databaseBuilder(
//            context,
//            LandFindDataBase::class.java, "Lands.db"
//        ).build()
//    }






    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @MainImmediateDispatcher
    @Provides
    fun providesMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate


    companion object {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    }
}


@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainImmediateDispatcher