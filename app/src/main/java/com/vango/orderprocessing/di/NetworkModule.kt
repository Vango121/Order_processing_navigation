package com.vango.orderprocessing.di

import com.squareup.moshi.Moshi
import com.vango.orderprocessing.auth.*
import com.vango.orderprocessing.auth.network.AuthRepository
import com.vango.orderprocessing.auth.network.AuthRepositoryImpl
import com.vango.orderprocessing.auth.network.AuthService
import com.vango.orderprocessing.remote.DataRepository
import com.vango.orderprocessing.remote.DataRepositoryImpl
import com.vango.orderprocessing.remote.DataService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    //provide retrofit
    @Singleton
    @Provides
    fun provideRetrofit(
        moshi: Moshi,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(AppConfig.BASE_URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    fun provideDataService(retrofit: Retrofit): DataService = retrofit.create(DataService::class.java)

    @Provides
    fun provideDataRepository(dataService: DataService, encryptedPrefs: EncryptedPrefs, userService: UserService): DataRepository = DataRepositoryImpl(dataService, encryptedPrefs, userService)

    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    fun provideAuthRepository(api: AuthService, encryptedPrefs: EncryptedPrefs, userService: UserService): AuthRepository = AuthRepositoryImpl(api, encryptedPrefs, userService)
}