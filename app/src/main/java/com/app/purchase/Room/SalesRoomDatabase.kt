package com.app.purchase.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.purchase.Room.Dao.AllSalesDao
import com.app.purchase.Room.Dao.CreditDebitDao
import com.app.purchase.Room.Dao.CurrencyDao
import com.app.purchase.Room.Dao.Dao
import com.app.purchase.ViewModel.CreditDebit.CreditDebitEntities
import com.app.purchase.ViewModel.CurrencyEntity
import com.app.purchase.ViewModel.SalesViewModel.SalesEntities
import com.app.purchase.ViewModel.TypeConverter.TypeConverterForSales

/**
 * Created by Android on 12/6/2018.
 */
@Database(entities = [SalesEntities::class, CreditDebitEntities::class, CurrencyEntity::class], version = 12, exportSchema = false)
@TypeConverters(TypeConverterForSales::class)
public abstract class SalesRoomDatabase : RoomDatabase() {
    abstract fun salesDao(): Dao
    abstract fun allSalesDao(): AllSalesDao?
    abstract fun creditDebit(): CreditDebitDao?
    abstract fun currency(): CurrencyDao?

    companion object {
        @Volatile
        private var INSTANCE: SalesRoomDatabase? = null

        fun getDataBase(context: Context): SalesRoomDatabase? {
//            val tempInstace: INSTANCE
//            if (tempInstace != null) {
//                return tempInstace
//            }

            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, SalesRoomDatabase::class.java, "DataBase")
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
            }

            return INSTANCE
        }

        fun DestroyInstance() {
            INSTANCE = null
        }
    }
}
