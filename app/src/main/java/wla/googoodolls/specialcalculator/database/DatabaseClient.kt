package wla.googoodolls.specialcalculator.database

import android.content.Context
import androidx.room.Room

class DatabaseClient private constructor(mCtx: Context) {
    //our app database object
    val appDatabase: AppDatabase = Room.databaseBuilder(mCtx, AppDatabase::class.java, "threed").build()
    companion object {
        private var mInstance: DatabaseClient? = null
        @Synchronized
        fun getInstance(mCtx: Context): DatabaseClient {
            if (mInstance == null) {
                mInstance = DatabaseClient(mCtx)
            }
            return mInstance!!
        }
    }
}