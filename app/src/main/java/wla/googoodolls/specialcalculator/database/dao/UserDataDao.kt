package wla.googoodolls.specialcalculator.database.dao

import androidx.room.*
import wla.googoodolls.specialcalculator.database.repos.UserData
@Dao
interface UserDataDao {
    @Query("Select * from userdata where user_id==:user_id")
    fun getByUser(user_id:Int):List<UserData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUserData(userData:UserData)

    @Update
    fun updateUserData(userData: UserData)

    @Query("select * from userdata")
    fun getAllUsersData():List<UserData>

}