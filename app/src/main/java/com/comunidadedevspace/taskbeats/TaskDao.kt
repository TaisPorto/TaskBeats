package com.comunidadedevspace.taskbeats

import androidx.room.*


@Dao

interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(task: Task)

//Deletando todo o codigo

    @Query("DELETE FROM task ")
    fun deleteAll()

    //Deletando por id
    @Query("DELETE from task WHERE id = :id")
    fun deleteById(id: Int)


}

