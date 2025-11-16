package com.choraline.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import android.util.Log
import com.choraline.models.*
import com.choraline.utils.AppController
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    private val COMPOSER_TABLE_CREATE_QUERY = "CREATE TABLE ${Entry.COMPOSER_TABLE_NAME} (" +
            "${Entry.ID} TEXT PRIMARY KEY,${Entry.TITLE} TEXT,${Entry.COMPOSER_IMAGE_URL} TEXT,${Entry.BANNER_IMAGE_URL} TEXT," +
            "${Entry.BANNER_IMAGE_FILE_URI} TEXT,${Entry.CUSTOMER_TYPE} INTEGER)"



    private val COMPOSER_SONG_TABLE_CREATE_QUERY = "CREATE TABLE ${Entry.COMPOSER_SONG_TABLE_NAME} (" +
            "${Entry.ALBUM_ID} TEXT PRIMARY KEY,${Entry.TITLE} TEXT,${Entry.SONG_NAME} TEXT,${Entry.IS_PAID_SONG} INTEGER,${Entry.TIME_STAMP} TEXT)"


    private val HISTORY_TABLE = "CREATE TABLE ${Entry.HISTORY_TABLE_NAME} (" +
            "${Entry.ORDER_ID} TEXT,${Entry.DISCOUNT} TEXT, ${Entry.ORDER_DATE} TEXT,${Entry.SUBTOTAL} TEXT" +
            ",${Entry.CURRENCY_SYMBOL} TEXT,${Entry.ORDER_DATA} TEXT)"

    private val PURCHASE_ALBUM_QUERY = "CREATE TABLE ${Entry.PURCHASED_ALBUM_LIST_TABLE_NAME} (" +
            "${Entry.PURCHASED_ID} TEXT,${Entry.TITLE} TEXT,${Entry.VOICE_TYPE} TEXT,${Entry.SUBTITLE} TEXT" +
            ",${Entry.DATE_CREATED} TEXT)"


    private val PURCHASE_SONG_TABLE = "CREATE TABLE ${Entry.PURCHASED_SONG_TABLE_NAME} (" +
            "${Entry.PURCHASED_ID} TEXT,${Entry.SERIAL_NO} TEXT,${Entry.SONG_TITLE} TEXT,${Entry.SONG_URL} TEXT)"


    private val URL_TABLE = "CREATE TABLE ${Entry.URL_TABLE_NAME} (" +
            "${Entry.ID} INTEGER PRIMARY KEY AUTOINCREMENT,${Entry.SONG_URL} TEXT)"


    override fun onCreate(db: SQLiteDatabase?) {

        db!!.execSQL(COMPOSER_TABLE_CREATE_QUERY)
        db!!.execSQL(COMPOSER_SONG_TABLE_CREATE_QUERY)
        db!!.execSQL(HISTORY_TABLE)
        db!!.execSQL(PURCHASE_ALBUM_QUERY)
        db!!.execSQL(PURCHASE_SONG_TABLE)
        db!!.execSQL(URL_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {


        if (!columnExists(db!!, "composerlist", "type")) {
            db.execSQL("ALTER TABLE composerlist ADD COLUMN type INTEGER DEFAULT 0")
        }
    }

    private fun columnExists(db: SQLiteDatabase, tableName: String, columnName: String): Boolean {
        db.rawQuery("PRAGMA table_info($tableName)", null).use { cursor ->
            val nameIndex = cursor.getColumnIndex("name")
            while (cursor.moveToNext()) {
                if (cursor.getString(nameIndex).equals(columnName, ignoreCase = true)) {
                    return true
                }
            }
        }
        return false
    }
    companion object {
        const val DATABASE_NAME = "Choraline.db"
        const val DATABASE_VERSION = 3
        val TAG = "DatabaseHandler"
    }


    fun insertComposerListAndUpdate(composerList: ArrayList<ComposerData>, updatedComposerList: ArrayList<ComposerData>, deletdList: ArrayList<ComposerData>) {

      try{


        val sqLiteDatabase = AppController.dbInstance.writableDatabase
        for (data in deletdList) sqLiteDatabase.delete(Entry.COMPOSER_TABLE_NAME, Entry.ID + "=?", arrayOf(data.scid))

        for (data in composerList) {
            val values = ContentValues().apply {
                put(Entry.ID, data.scid)
                put(Entry.TITLE, data.title)
                put(Entry.COMPOSER_IMAGE_URL, data.composerImage)
                put(Entry.BANNER_IMAGE_URL, data.bannerImage)
                put(Entry.CUSTOMER_TYPE, data.freeStatus)
            }
            val ss = sqLiteDatabase.insert(Entry.COMPOSER_TABLE_NAME, null, values)
            Log.d(TAG, "111111111111111111111111111111======" + ss)
        }

        for (data in updatedComposerList) {
            val values = ContentValues().apply {

                put(Entry.TITLE, data.title)
                put(Entry.COMPOSER_IMAGE_URL, data.composerImage)
                put(Entry.BANNER_IMAGE_URL, data.bannerImage)
                put(Entry.CUSTOMER_TYPE, data.freeStatus)
            }

            val count = sqLiteDatabase.update(Entry.COMPOSER_TABLE_NAME, values, Entry.ID + "=?", arrayOf(data.scid))
            Log.d(TAG, "111111111111111111111111111111=====" + count)
        }
        sqLiteDatabase.close()

      }catch (e: Exception){
          Log.d(TAG, "111111111111111111111111111111=====" + e)

      }
    }


    fun getComposerList(): ArrayList<ComposerData> {
        val list = ArrayList<ComposerData>()

        try{
        val sqLiteDatabase = AppController.dbInstance.writableDatabase
        var cursor = sqLiteDatabase.query(Entry.COMPOSER_TABLE_NAME, null, null, null, null, null, null)!!

        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndexOrThrow(Entry.ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(Entry.TITLE))
            val composerImage = cursor.getString(cursor.getColumnIndexOrThrow(Entry.COMPOSER_IMAGE_URL))
            val bannerImage = cursor.getString(cursor.getColumnIndexOrThrow(Entry.BANNER_IMAGE_URL))
            val type = cursor.getString(cursor.getColumnIndexOrThrow(Entry.CUSTOMER_TYPE))

            val composerData = ComposerData()
            composerData.scid = id
            composerData.title = title
            composerData.composerImage = composerImage
            composerData.bannerImage = bannerImage
            composerData.freeStatus = type
            list.add(composerData)

        }
        sqLiteDatabase.close()
        }catch (e:Exception){
        }
        System.out.println("getComposerList------" + list.size)
        return list

    }


    fun insertComposerSong(data: String, title: String, updateList: ArrayList<ChoralWorksData>, deletdList: ArrayList<ChoralWorksData>, timeStamp: String) {
        var choralWorksModel = ChoralWorksModel()
        choralWorksModel = Gson().fromJson(data, ChoralWorksModel::class.java);


        val sqLiteDatabase = AppController.dbInstance.writableDatabase

        for (data in deletdList) sqLiteDatabase.delete(Entry.COMPOSER_SONG_TABLE_NAME, Entry.ALBUM_ID + "=?", arrayOf(data.albumId))



        for (data in choralWorksModel.response!!.paidsongList) {
            val values = ContentValues().apply {
                put(Entry.ALBUM_ID, data.albumId)
                put(Entry.TITLE, title)
                put(Entry.SONG_NAME, data.songName)
                put(Entry.TIME_STAMP, timeStamp)
                put(Entry.IS_PAID_SONG, Entry.PAID_SONG_WITHOUT_SINGER)
            }


            var ss = sqLiteDatabase.insert(Entry.COMPOSER_SONG_TABLE_NAME, null, values)
            Log.d(TAG, "ppppppp=>" + ss)

        }
        for (data in choralWorksModel.response!!.paidsongwitsingerList) {
            val values = ContentValues().apply {
                put(Entry.ALBUM_ID, data.albumId)
                put(Entry.TITLE, title)
                put(Entry.SONG_NAME, data.songName)
                put(Entry.IS_PAID_SONG, Entry.PAID_SONG_WITH_SINGER)
                put(Entry.TIME_STAMP, timeStamp)

            }


            var ss = sqLiteDatabase.insert(Entry.COMPOSER_SONG_TABLE_NAME, null, values)
            Log.d(TAG, "ppppppp=>" + ss)

        }



        val values = ContentValues().apply {
            put(Entry.TIME_STAMP, timeStamp)
        }
        sqLiteDatabase.update(Entry.COMPOSER_SONG_TABLE_NAME, values, Entry.TITLE + "=?", arrayOf(title))


        for (data in updateList) {
            val values = ContentValues().apply {
                put(Entry.TITLE, title)
                put(Entry.SONG_NAME, data.songName)


            }
            sqLiteDatabase.update(Entry.COMPOSER_SONG_TABLE_NAME, values, Entry.ALBUM_ID + "=?", arrayOf(data.albumId))

        }



        sqLiteDatabase.close()


    }

    fun getComposerAlbumTimeStamp(title: String): String {
        var time = "0"
        val sqLiteDatabase = AppController.dbInstance.writableDatabase
        val cursor = sqLiteDatabase.query(Entry.COMPOSER_SONG_TABLE_NAME, null, Entry.TITLE + "=?", arrayOf(title), null, null, null, "1")
        if (cursor.moveToNext()) {
            time = cursor.getString(cursor.getColumnIndexOrThrow(Entry.TIME_STAMP))
        }

        return time
    }

    fun getComposerSongsList(title: String, bannerImageurl: String): String {

        Log.d(TAG, "getComposerSongsList title=" + title)
        val sqLiteDatabase = AppController.dbInstance.writableDatabase
        val cursor = sqLiteDatabase.query(Entry.COMPOSER_SONG_TABLE_NAME, null, Entry.TITLE + "=?", arrayOf(title), null, null, null, null)
        val json = JSONObject()


        val response = JSONObject()
        response.put("banner_image", bannerImageurl)

        val arrayWithoutSinger = JSONArray()
        val arrayWithSinger = JSONArray()
        if (cursor.count > 0) {
            json.put("status", true)
            json.put("message", "Successfully record found!")
        } else {
            json.put("status", false)
            json.put("message", "No record found!")
        }
        while (cursor.moveToNext()) {

            val songObj = JSONObject()
            songObj.put("albumId", cursor.getString(cursor.getColumnIndexOrThrow(Entry.ALBUM_ID)))
            songObj.put("song_name", cursor.getString(cursor.getColumnIndexOrThrow(Entry.SONG_NAME)))
            val songType = cursor.getInt(cursor.getColumnIndexOrThrow(Entry.IS_PAID_SONG))
            if (songType==Entry.PAID_SONG_WITHOUT_SINGER)
            arrayWithoutSinger.put(songObj)
            else if (songType==Entry.PAID_SONG_WITH_SINGER)
            {
                arrayWithSinger.put(songObj)
            }
        }

        response.put("paidsonglist", arrayWithoutSinger)
        response.put("paidsonglistwithsinger", arrayWithSinger)
        json.put("response", response)
        sqLiteDatabase.close()

        Log.d(TAG, "" + json)
        return "" + json
    }

    fun getBannerImage(comoserName: String): String {
        val sqLiteDatabase = AppController.dbInstance.writableDatabase
        val cursor = sqLiteDatabase.query(Entry.COMPOSER_TABLE_NAME, arrayOf(Entry.BANNER_IMAGE_URL), Entry.TITLE, arrayOf(comoserName), null, null, null, null)
        var imageurl = ""
        if (cursor.moveToNext()) {
            imageurl = cursor.getString(0)
        }
        sqLiteDatabase.close()
        return imageurl


    }


    fun insertHistoryData(orderHistoryData: ArrayList<OrderHistoryData>) {
        val sqLiteDatabase = AppController.dbInstance.writableDatabase
        for (data in orderHistoryData) {

            val values = ContentValues().apply {
                put(Entry.ORDER_ID, data.order_id)
                put(Entry.SUBTOTAL, data.subtotal)
                put(Entry.CURRENCY_SYMBOL, data.currency_symbol)
                put(Entry.DISCOUNT,data.discount_percentage)
                put(Entry.ORDER_DATE, data.order_date)
                put(Entry.ORDER_DATA, Gson().toJson(data.list))
            }
            sqLiteDatabase.insert(Entry.HISTORY_TABLE_NAME, null, values)


        }
        sqLiteDatabase.close()


    }


    fun getHistoryData(): ArrayList<OrderHistoryData> {

        val sqLiteDatabase = AppController.dbInstance.writableDatabase

        val cursor = sqLiteDatabase.query(Entry.HISTORY_TABLE_NAME, null, null, null, null, null, null)
        val list = ArrayList<OrderHistoryData>()

        while (cursor.moveToNext()) {
            val model = OrderHistoryData()
            model.order_id = cursor.getString(cursor.getColumnIndexOrThrow(Entry.ORDER_ID))
            model.order_date = cursor.getString(cursor.getColumnIndexOrThrow(Entry.ORDER_DATE))
            model.discount_percentage = cursor.getString(cursor.getColumnIndexOrThrow(Entry.DISCOUNT))
            model.subtotal = cursor.getString(cursor.getColumnIndexOrThrow(Entry.SUBTOTAL))
            model.currency_symbol = cursor.getString(cursor.getColumnIndexOrThrow(Entry.CURRENCY_SYMBOL))
            val listType = object : TypeToken<ArrayList<OrderHistoryItemData>>() {

            }.type
            //val listType = TypeToken<ArrayList<ChoralWorksModel>>().type
            model.list = Gson().fromJson(cursor.getString(cursor.getColumnIndexOrThrow(Entry.ORDER_DATA)), listType);
            list.add(model)

            Log.d(TAG, "order_id->" + model.order_id + "  model.list->" + cursor.getString(cursor.getColumnIndexOrThrow(Entry.ORDER_DATA)))
        }



        sqLiteDatabase.close()
        return list
    }


    fun insertPurchaseData(list: ArrayList<PurchasedMusicData>) {
        var sqLiteDatabase = AppController.dbInstance.writableDatabase

        for (data in list) {
            val values = ContentValues().apply {
                put(Entry.PURCHASED_ID, data.id)
                put(Entry.TITLE, data.title)
                put(Entry.SUBTITLE, data.subtitle)
                put(Entry.VOICE_TYPE, data.voiceType)
                put(Entry.DATE_CREATED, data.dateCreated)
            }
            val ss = sqLiteDatabase.insert(Entry.PURCHASED_ALBUM_LIST_TABLE_NAME, null, values)

            Log.d(TAG, " insert album data ==" + ss)

            for (songData in data.songlist) {
                val values1 = ContentValues().apply {
                    put(Entry.PURCHASED_ID, data.id)
                    put(Entry.SERIAL_NO, songData.songTitle)
                    put(Entry.SONG_TITLE, songData.songTitle)
                    put(Entry.SONG_URL, songData.songUrl)
                }
                val ss = sqLiteDatabase.insert(Entry.PURCHASED_SONG_TABLE_NAME, null, values1)
                Log.d(TAG, "inser song data =>" + ss)
            }

        }
    }


    fun getPurchaseSongList(): ArrayList<PurchasedMusicData> {
        val list = ArrayList<PurchasedMusicData>()
        val sqLiteDatabase = AppController.dbInstance.writableDatabase

        val albumCursor = sqLiteDatabase.query(Entry.PURCHASED_ALBUM_LIST_TABLE_NAME, null, null, null, null, null, null)
        //Log.d(TAG, "-----------------------getPurchaseSongList---------------------------")
        while (albumCursor.moveToNext()) {
            val purchaseId = albumCursor.getString(albumCursor.getColumnIndexOrThrow(Entry.PURCHASED_ID))
          //  Log.d(TAG, "--------------111---------getPurchaseSongList---------------------------")
            val purchasedMusicData = PurchasedMusicData()
            purchasedMusicData.id = purchaseId
            purchasedMusicData.title = albumCursor.getString(albumCursor.getColumnIndexOrThrow(Entry.TITLE))
            purchasedMusicData.subtitle = albumCursor.getString(albumCursor.getColumnIndexOrThrow(Entry.SUBTITLE))
            purchasedMusicData.voiceType = albumCursor.getString(albumCursor.getColumnIndexOrThrow(Entry.VOICE_TYPE))
            purchasedMusicData.dateCreated = albumCursor.getString(albumCursor.getColumnIndexOrThrow(Entry.DATE_CREATED))

            val sonlist = ArrayList<SongsData>()

            val cursor = sqLiteDatabase.query(Entry.PURCHASED_SONG_TABLE_NAME, null, Entry.PURCHASED_ID + "=?", arrayOf(purchaseId), null, null, null)


            while (cursor.moveToNext()) {

                val songData = SongsData()
                songData.songTitle = cursor.getString(cursor.getColumnIndexOrThrow(Entry.SONG_TITLE))

                songData.songUrl = cursor.getString(cursor.getColumnIndexOrThrow(Entry.SONG_URL))
                sonlist.add(songData)
            }


            purchasedMusicData.songlist = sonlist
          //  Log.d(TAG,"11111111111 size->"+sonlist.size)
            list.add(purchasedMusicData)


        }


        return list;

    }


    fun makeShortURl(url: String): String {
        val sqLiteDatabase = AppController.dbInstance.writableDatabase
        val values = ContentValues().apply {
            put(Entry.SONG_URL, url)
        }
        val id = sqLiteDatabase.insert(Entry.URL_TABLE_NAME, null, values)
        return "" + id;
    }

    fun getShortUrlId(id:String): SongsData {

        val sqLiteDatabase = AppController.dbInstance.writableDatabase
        val cursor = sqLiteDatabase.query(Entry.URL_TABLE_NAME, null, Entry.ID+"=?", arrayOf(id), null, null, null)

        val list = ArrayList<String>()
        val data = SongsData()
     if (cursor.moveToNext())
     {
         data.id=cursor.getString(cursor.getColumnIndexOrThrow(Entry.ID))
         data.songUrl = cursor.getString(cursor.getColumnIndexOrThrow(Entry.SONG_URL))
     }
        return data
    }


    fun deleteUrl(id:String)
    {
        val sqLiteDatabase = AppController.dbInstance.writableDatabase
        sqLiteDatabase.delete(Entry.URL_TABLE_NAME,Entry.ID+"=?", arrayOf(id))
    }


    fun clearPurchaseTable()
    {
        try {
            val sqLiteDatabase= AppController.dbInstance.writableDatabase
            sqLiteDatabase.delete(Entry.PURCHASED_SONG_TABLE_NAME,null,null)
            sqLiteDatabase.delete(Entry.PURCHASED_ALBUM_LIST_TABLE_NAME,null,null)
            sqLiteDatabase.delete(Entry.HISTORY_TABLE_NAME,null,null)
            sqLiteDatabase.delete(Entry.COMPOSER_TABLE_NAME,null,null)
            sqLiteDatabase.delete(Entry.COMPOSER_SONG_TABLE_NAME,null,null)
        }catch (e:Exception)
        {

        }

    }

    fun clearPurchaseAlbumList()
    {
        try {
            val sqLiteDatabase= AppController.dbInstance.writableDatabase
            sqLiteDatabase.delete(Entry.PURCHASED_SONG_TABLE_NAME,null,null)
            sqLiteDatabase.delete(Entry.PURCHASED_ALBUM_LIST_TABLE_NAME,null,null)
        }catch (e:Exception)
        {

        }
    }

    fun clearAlbum()
    {
        try {
            val sqLiteDatabase= AppController.dbInstance.writableDatabase
            sqLiteDatabase.delete(Entry.COMPOSER_TABLE_NAME,null,null)

        }catch (e:Exception)
        {

        }
    }

}