package com.example.movie_rec_sys.data

class DBRepository(
    private val dbInstance: AppDB,
    private val ItemRDS: ItemRemoteDataSource
) {
    private val dbDao = dbInstance.moviesDao()
    fun findMovieByTitle(string: String): List<MovieFromDB> {
        return dbDao.findMovieByTitle(string)
    }
}