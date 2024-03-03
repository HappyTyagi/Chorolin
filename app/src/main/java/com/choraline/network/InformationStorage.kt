package com.choraline.network

import com.choraline.models.FreeTrailData
import com.choraline.models.FreeTrailModel

import java.util.ArrayList

/**
 * Created by deepak Tyagi on 6/14/2017.
 */

class InformationStorage {

    var freeTrailModel: FreeTrailData? = null

    companion object {

        var mInstance: InformationStorage? = null

        val instance: InformationStorage
            get() {
                if (mInstance == null)
                    mInstance = InformationStorage()
                return mInstance as InformationStorage
            }
    }
}
