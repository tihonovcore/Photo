//package tihonov.photo
//
//import android.database.sqlite.SQLiteDatabase
//import android.content.ContentValues
//import android.content.Context
//import android.database.sqlite.SQLiteOpenHelper
//
//
//class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
//
//    val allNotes: List<FavPic>
//        get() {
//            var notes = ArrayList<FavPic>()
//
//            val selectQuery = "SELECT  * FROM " + FavPic.TABLE_NAME + " ORDER BY " +
//                    FavPic.COLUMN_USERNAME + " DESC"
//
//            val db = this.writableDatabase
//            val cursor = db.rawQuery(selectQuery, null)
//            if (cursor.moveToFirst()) {
//                do {
//                    val note = FavPic(
//                            cursor.getString(cursor.getColumnIndex(FavPic.COLUMN_URL)),
//                            cursor.getString(cursor.getColumnIndex(FavPic.COLUMN_USERNAME))
//                    )
//
//                    notes.add(note)
//                } while (cursor.moveToNext())
//            }
//            db.close()
//            return notes
//        }
//
//    // return count
//    val notesCount: Int
//        get() {
//            val countQuery = "SELECT  * FROM " + FavPic.TABLE_NAME
//            val db = this.readableDatabase
//            val cursor = db.rawQuery(countQuery, null)
//
//            val count = cursor.count
//            cursor.close()
//            return count
//        }
//
//    override fun onCreate(db: SQLiteDatabase) {
//        db.execSQL(FavPic.CREATE_TABLE)
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        db.execSQL("DROP TABLE IF EXISTS " + FavPic.TABLE_NAME)
//        onCreate(db)
//    }
//
//    fun insertPhoto(url: String, name: String): Long {
//        val db = this.writableDatabase
//
//        val values = ContentValues()
//
//        values.put(FavPic.COLUMN_ID, url.hashCode())
//        values.put(FavPic.COLUMN_URL, url)
//        values.put(FavPic.COLUMN_USERNAME, name)
//
//        val id = db.insert(FavPic.TABLE_NAME, null, values)
//
//        db.close()
//
//        return id
//    }
//
//    fun getPhoto(id: Long): FavPic {
//        val db = this.readableDatabase
//
//        val cursor = db.query(FavPic.TABLE_NAME,
//                arrayOf(FavPic.COLUMN_URL, FavPic.COLUMN_USERNAME),
//                FavPic.COLUMN_ID + "=?",
//                arrayOf(id.toString()), null, null, null, null)
//
//        cursor?.moveToFirst()
//
//        val PicInfo = FavPic(
//                cursor.getString(cursor.getColumnIndex(FavPic.COLUMN_URL)),
//                cursor.getString(cursor.getColumnIndex(FavPic.COLUMN_USERNAME))
//        )
//
//        cursor.close()
//
//        return PicInfo
//    }
//
////    fun deleteNote(note: FavPic) {
////        val db = this.writableDatabase
////        db.delete(FavPic.TABLE_NAME, FavPic.COLUMN_ID + " = ?",
////                arrayOf(String.valueOf(note.id)))
////        db.close()
////    }
//
//    companion object {
//
//        // Database Version
//        private val DATABASE_VERSION = 1
//
//        // Database Name
//        private val DATABASE_NAME = "notes_db"
//    }
//}