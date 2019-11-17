package wla.googoodolls.specialcalculator.database

import androidx.room.Database
import androidx.room.RoomDatabase
import wla.googoodolls.specialcalculator.database.dao.UserDao
import wla.googoodolls.specialcalculator.database.dao.UserDataDao
import wla.googoodolls.specialcalculator.database.repos.User
import wla.googoodolls.specialcalculator.database.repos.UserData


@Database(entities = [User::class,UserData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userDataDao(): UserDataDao
}