package com.example.nextcartapp.core.network

import com.example.nextcartapp.data.remote.api.AuthApi
import com.example.nextcartapp.data.remote.api.BodyCompositionApi
import com.example.nextcartapp.data.remote.api.CartApi
import com.example.nextcartapp.data.remote.api.FilterApi
import com.example.nextcartapp.data.remote.api.HealthConditionApi
import com.example.nextcartapp.data.remote.api.MealApi
import com.example.nextcartapp.data.remote.api.PhysicalActivityApi
import com.example.nextcartapp.data.remote.api.ProductApi
import com.example.nextcartapp.data.remote.api.ProfileApi
import com.example.nextcartapp.data.repository.AuthRepositoryImpl
import com.example.nextcartapp.data.repository.BodyCompositionRepositoryImpl
import com.example.nextcartapp.data.repository.CartRepositoryImpl
import com.example.nextcartapp.data.repository.FilterRepositoryImpl
import com.example.nextcartapp.data.repository.HealthConditionRepositoryImpl
import com.example.nextcartapp.data.repository.MealRepositoryImpl
import com.example.nextcartapp.data.repository.PhysicalActivityRepositoryImpl
import com.example.nextcartapp.data.repository.ProductRepositoryImpl
import com.example.nextcartapp.data.repository.UserRepositoryImpl
import com.example.nextcartapp.domain.repository.AuthRepository
import com.example.nextcartapp.domain.repository.BodyCompositionRepository
import com.example.nextcartapp.domain.repository.CartRepository
import com.example.nextcartapp.domain.repository.FilterRepository
import com.example.nextcartapp.domain.repository.HealthConditionRepository
import com.example.nextcartapp.domain.repository.MealRepository
import com.example.nextcartapp.domain.repository.PhysicalActivityRepository
import com.example.nextcartapp.domain.repository.ProductRepository
import com.example.nextcartapp.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideProductApi(retrofit: Retrofit): ProductApi =
        retrofit.create(ProductApi::class.java)

    @Provides
    @Singleton
    fun provideFilterApi(retrofit: Retrofit): FilterApi {
        return retrofit.create(FilterApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFilterRepository(filterApi: FilterApi): FilterRepository {
        return FilterRepositoryImpl(filterApi)
    }

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(profileApi: ProfileApi): UserRepository {
        return UserRepositoryImpl(profileApi)
    }

    // Physical Activity
    @Provides
    @Singleton
    fun providePhysicalActivityApi(retrofit: Retrofit): PhysicalActivityApi {
        return retrofit.create(PhysicalActivityApi::class.java)
    }

    @Provides
    @Singleton
    fun providePhysicalActivityRepository(api: PhysicalActivityApi): PhysicalActivityRepository {
        return PhysicalActivityRepositoryImpl(api)
    }

    // Health Conditions
    @Provides
    @Singleton
    fun provideHealthConditionApi(retrofit: Retrofit): HealthConditionApi {
        return retrofit.create(HealthConditionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHealthConditionRepository(
        api: HealthConditionApi
    ): HealthConditionRepository {
        return HealthConditionRepositoryImpl(api)
    }

    // Body Composition
    @Provides
    @Singleton
    fun provideBodyCompositionApi(retrofit: Retrofit): BodyCompositionApi {
        return retrofit.create(BodyCompositionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBodyCompositionRepository(
        api: BodyCompositionApi
    ): BodyCompositionRepository {
        return BodyCompositionRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideCartApi(retrofit: Retrofit): CartApi {
        return retrofit.create(CartApi::class.java)
    }

    // 2. Spieghiamo a Hilt che quando serve CartRepository, deve dare CartRepositoryImpl
    @Provides
    @Singleton
    fun provideCartRepository(api: CartApi): CartRepository {
        return CartRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideMealApi(retrofit: Retrofit): MealApi {
        return retrofit.create(MealApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMealRepository(api: MealApi): MealRepository {
        return MealRepositoryImpl(api)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository
}
