package com.example.safenotes.di

import com.example.safenotes.retrofit.AuthInterceptor
import com.example.safenotes.retrofit.NotesApi
import com.example.safenotes.retrofit.QuotesApi
import com.example.safenotes.retrofit.UserApi
import com.example.safenotes.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    //for notes
    @Singleton
    @Provides
    @Named("NormalRetrofit")
    fun providesRetrofit(): Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    //auth interceptor
    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Singleton
    @Provides
    fun providesUserApi(@Named("NormalRetrofit") retrofit: Retrofit): UserApi{
        return retrofit.create(UserApi::class.java)
    }

    //for auth
    @Singleton
    @Provides
    @Named("AuthRetrofit")
    fun providesAuthRetrofit(okHttpClient: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun providesNotesApi(@Named("AuthRetrofit") retrofit: Retrofit): NotesApi{
        return retrofit.create(NotesApi::class.java)
    }

    //for quotes
    @Singleton
    @Provides
    fun providesQuotesApi(@Named("NormalRetrofit") retrofit: Retrofit): QuotesApi{
        return retrofit.create(QuotesApi::class.java)
    }
}