package tihonov.photo

import android.arch.persistence.room.*
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database

@Entity
class FavPic(val url: String, val userName: String?) {
    @PrimaryKey
    var id: Int = url.hashCode()
}

@Dao
interface FavPicDao {

    @get:Query("SELECT * FROM favpic")
    val all: MutableList<FavPic>

    @Query("SELECT * FROM favpic WHERE id = :id")
    fun getById(id: Long): FavPic

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(pic: FavPic)

    @Update
    fun update(pic: FavPic)

    @Delete
    fun delete(pic: FavPic)

}

@Database(entities = arrayOf(FavPic::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favpicDao(): FavPicDao
}
