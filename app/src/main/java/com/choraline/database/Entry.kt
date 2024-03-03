package com.choraline.database

object Entry {

    const val COMPOSER_TABLE_NAME = "composerlist"
    //composer table
    const val ID= "scid"
    const val TITLE = "title"
    const val COMPOSER_IMAGE_URL = "compserImageurl"
    const val BANNER_IMAGE_URL = "bannerImageurl"
    const val COMPOSER_IMAGE_FILE_URI = "composeruri"
    const val BANNER_IMAGE_FILE_URI = "banneruri"

    const val CUSTOMER_TYPE = "type"

    // composer song table
    const val COMPOSER_SONG_TABLE_NAME = "composersongs"
    const val ALBUM_ID = "albumId"
    const val SONG_NAME = "songname"
    const val TIME_STAMP = "timestamp"
    const val IS_PAID_SONG ="ispaidsong"

    //order history
    const val HISTORY_TABLE_NAME="historytable"
    const val ORDER_ID ="order_id"
    const val ORDER_DATE = "order_date"
    const val DISCOUNT = "discount"
    const val SUBTOTAL = "subtotal"
    const val CURRENCY_SYMBOL = "currency_symbol"
    const val ORDER_DATA ="order_data"
    const val PAID_SONG_WITHOUT_SINGER=1
    const val PAID_SONG_WITH_SINGER=2;






   /* "id": "89",
    "title": "Beethoven",
    "subtitle": "9th (Choral) Symphony \/ Choral Fantasia",
    "voiceType": "SOPRANO",
    "date_created": "1523618088",*/
   //purchased Album List
    const val PURCHASED_ALBUM_LIST_TABLE_NAME = "purchasedalbumlist"
    const val PURCHASED_ID = "id"
    const val SUBTITLE = "subtitle"
    const val VOICE_TYPE = "voice_type"
    const val DATE_CREATED = "date_created"

    //purchased songs list
   /* "Sr. No.": "902",
    "song_title": "9 sop 02",
    "song_url": "http:\/\/96.57.152.179\/choraline\/mp3\/CM38\/Beethoven 9th (Choral)Symphony_Sop\/Beet 9 sop 02.mp3"
*/

    //purchase song album name

    // purchased song list

    const val PURCHASED_SONG_TABLE_NAME = "purchasedsong"
    const val SERIAL_NO = "sr_no"
    const val SONG_TITLE = "song_title"
    const val SONG_URL = "songurl"

    //composer song time
    const val URL_TABLE_NAME= "urltablename"






}