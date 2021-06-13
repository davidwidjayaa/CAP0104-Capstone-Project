package com.example.angkoot.di

import com.example.angkoot.api.AngkootApiEndpoint
import com.example.angkoot.api.GoogleMapApiEndpoint
import com.example.angkoot.api.GoogleMapApiEndpoint.Companion.BASE_URL
import com.example.angkoot.api.GoogleMapApiEndpoint.Companion.hostname
import com.example.angkoot.data.AngkootRepository
import com.example.angkoot.data.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.example.angkoot.api.AngkootApiEndpoint.Companion.BASE_URL as ANGKOOT_BASE_API

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .certificatePinner(
                CertificatePinner.Builder()
                    .add(hostname, "sha256/Q7MIEWtOcNj+1rFg57yHxJRRwNG9xGNxPQUt/LulvbM=")
                    .add(hostname, "sha256/YZPgTZ+woNCCCIW3LH2CxQeLzB/1m42QcCTBSdgayjs=")
                    .build()
            )
            .build()

    @Provides
    @Singleton
    fun provideGoogleMapApi(
        client: OkHttpClient
    ): GoogleMapApiEndpoint =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(GoogleMapApiEndpoint::class.java)

    @Provides
    @Singleton
    fun provideAngkootApi(): AngkootApiEndpoint =
        Retrofit.Builder()
            .baseUrl(ANGKOOT_BASE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AngkootApiEndpoint::class.java)

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        googleMapApiClient: GoogleMapApiEndpoint,
        angkootApi: AngkootApiEndpoint
    ): RemoteDataSource =
        RemoteDataSource(googleMapApiClient, angkootApi)

    @Provides
    @Singleton
    fun provideRepository(
        remote: RemoteDataSource
    ): AngkootRepository =
        AngkootRepository(remote)
}