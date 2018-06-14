package com.gmb.bbm2.tools.app

import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import java.text.NumberFormat
import java.util.*

/**
 * Created by GMB on 2/15/2018.
 */
class MyBbmApplication : Application() {

    private var instanceCour: MyBbmApplication? = null
    lateinit var PACKAGE_NAME: String




    @Synchronized
    fun getInstance(): MyBbmApplication? {

        return instanceCour
    }


    override fun onCreate() {
        super.onCreate()


        /*final private ResultCallback<DriveContentsResult> contentsCallback =
                new ResultCallback<DriveContentsResult>() {
                    @Override
                    public void onResult(DriveContentsResult result) {
                        if (!result.getStatus().isSuccess()) {
                            showMessage("Error while trying to create new file contents");
                            return;
                        }

                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle("appconfig.txt")
                                .setMimeType("text/plain")
                                .build();
                        Drive.DriveApi.getAppFolder(getGoogleApiClient())
                                .createFile(getGoogleApiClient(), changeSet, result.getDriveContents())
                                .setResultCallback(fileCallback);
                    }
                };*/

        /* File path = new File(Environment.getExternalStorageDirectory(), "my_sdcard_dir/deeper_dir/notes-db");
        path.getParentFile().mkdirs();

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(path, null);
        DaoMaster.createAllTables(db, true);

        DaoMaster daoMaster = new DaoMaster(db);*/

        /*val helper = DaoMaster.DevOpenHelper(this, DB_NAME, null)
        val db = helper.getWritableDatabase()
        val daoMaster = DaoMaster(db)
        daoSession = daoMaster.newSession()

        _context = this.applicationContext


        nbreCurrentNotifHeld = getPrefNbreNotifHandle()
        dateLastNotif = getPrefDateLastNotif()

        nbreCurrentNotifSend = getPrefNbreNotifSend()
        idNotifSend = getPrefIdNotifSend()*/

        if (instanceCour == null) instanceCour = this


        PACKAGE_NAME = applicationContext.packageName

        /*********************Mes dev options */
        //initMesDevOptions()

    }










































    companion object {

        @TargetApi(Build.VERSION_CODES.KITKAT)
        fun getLocalCurrency(context: Context): Currency {

            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            val numFor = NumberFormat.getCurrencyInstance(Locale.US)
            var cur = numFor.currency

            try {

                val defCurCode = pref.getString("lst_default_currency", numFor.currency.currencyCode)

                //Log.e("MyBBmApp","getLocalCurrency this is defautCode->"+defCurCode);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    val curs = Currency.getAvailableCurrencies()

                    val it = curs.iterator()

                    while (it.hasNext()) {
                        val c = it.next()
                        if (c.currencyCode.equals(defCurCode!!, ignoreCase = true)) {
                            cur = c
                            break
                        }
                    }
                } else {

                    cur = Currency.getInstance(defCurCode)
                }


            } catch (ex: Exception) {

                ex.printStackTrace()
            }


            return cur

        }


        fun isClickMenuContextuel(context: Context): Boolean {

            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            return pref.getBoolean("swt_mouse_bouton", true)
        }
    }
}