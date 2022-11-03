package mx.mauriciogs.persistenttvseries.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mx.mauriciogs.persistenttvseries.model.db.Constants.DATABASE_NAME
import mx.mauriciogs.persistenttvseries.model.entity.TvSerieEntity

@Database(entities = [TvSerieEntity::class], version = 1)
abstract class TvSerieRoom: RoomDatabase() {

    abstract fun tvSerieDao(): TvSerieDao

    companion object {
        @Volatile
        private var INSTANCE: TvSerieRoom? = null

        fun getDatabase(context: Context): TvSerieRoom {
            // Si la instancia no es nula, entonces se retorna
            // Si es nula, se crea la base de datos (en patron Singleton)
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TvSerieRoom::class.java,
                    DATABASE_NAME)
                    // permite a Room recrear las tablas de la BD si las migraciones para migrar
                    // al esquema más reciente no son encontradas
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}

object Constants {
    const val DATABASE_NAME = "games_room_db"
    const val DATABASE_GAME_TABLE = "games"
}