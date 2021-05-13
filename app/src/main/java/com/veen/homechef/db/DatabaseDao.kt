package com.veen.homechef.db

import androidx.room.*


@Dao
interface DatabaseDao {
    @Insert
    fun addToCart(addToCart: AddToCart)

    @Query("SELECT * FROM addtocart ORDER BY id DESC")
    fun getalldate(): List<AddToCart>

    @Insert
    fun addMultipleNotes(vararg addToCart: AddToCart)

    @Query("update addtocart set size = :value where id = :id")
    fun updatenote(value: Int?, id: Int?)

   // @Delete
    //fun delnote(Int  id)
   // fun delnote(addToCart: AddToCart)

    @Query("DELETE FROM addtocart WHERE id = :id")
    fun deleteByTitle(id: Int?)
}