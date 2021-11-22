package com.rocknhoney.nbaapp.di

import android.content.Context
import androidx.room.Room
import com.rocknhoney.nbaapp.api.RetrofitAPI
import com.rocknhoney.nbaapp.roomdb.dao.PlayerDao
import com.rocknhoney.nbaapp.roomdb.dao.TeamDao
import com.rocknhoney.nbaapp.roomdb.database.NbaDatabase
import com.rocknhoney.nbaapp.roomdb.repository.player.PlayerRepository
import com.rocknhoney.nbaapp.roomdb.repository.player.PlayerRepositoryInterface
import com.rocknhoney.nbaapp.roomdb.repository.team.TeamRepository
import com.rocknhoney.nbaapp.roomdb.repository.team.TeamRepositoryInterface
import com.rocknhoney.nbaapp.util.ConnectionLiveData
import com.rocknhoney.nbaapp.util.Util
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun injectRoomDatabase(
        @ApplicationContext context: Context
    ) =
        Room.databaseBuilder(context,NbaDatabase::class.java,"nba-database").build()

    @Singleton
    @Provides
    fun injectPlayerDao(database: NbaDatabase) = database.playerDao()

    @Singleton
    @Provides
    fun injectTeamDao(database: NbaDatabase) = database.teamDao()


    @Singleton
    @Provides
    fun injectRetrofitAPI(): RetrofitAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Util.API_URL)
            .build()
            .create(RetrofitAPI::class.java)
    }


    @Singleton
    @Provides
    fun injectPlayerRepo(dao: PlayerDao, api:RetrofitAPI) = PlayerRepository(dao,api) as PlayerRepositoryInterface

    @Singleton
    @Provides
    fun injectTeamRepo(dao: TeamDao, api:RetrofitAPI) = TeamRepository(dao,api) as TeamRepositoryInterface

    @Singleton
    @Provides
    fun injectConnectionLiveData(@ApplicationContext context: Context) = ConnectionLiveData(context)

}