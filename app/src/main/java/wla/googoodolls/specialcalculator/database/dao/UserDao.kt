package wla.googoodolls.specialcalculator.database.dao

import androidx.room.*
import wla.googoodolls.specialcalculator.database.repos.User

@Dao
interface UserDao {
    @Query("Select * from user Where date <=:select and date >=:sub")
    fun getUserByDate(select:String,sub:String):List<User>

    @Insert
    fun insertUser(user:User):Long

    @Update
    fun updateUser(user: User)

    @Query("select * from user")
    fun getAllUser():List<User>
}