package com.choraline.utils

import android.database.Cursor
import android.net.Uri
import androidx.core.content.FileProvider


/**
 * Created by deepak Tyagi on 8/8/2017.
 */

class CustomFileProvider : FileProvider()/*@Override
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
            return(new LegacyCompatCursorWrapper(super.query(uri, projection, selection, selectionArgs, sortOrder)));
        }*/
