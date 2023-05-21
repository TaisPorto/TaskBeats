package com.comunidadedevspace.taskbeats

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


//@Entity é uma anotação usada no Room, uma biblioteca de persistência de dados do Android,
// para indicar que uma classe representa uma entidade da tabela de um banco de dados relacional.
@Entity


//A classe é marcada como "data class",
// o que significa que o compilador gerará automaticamente métodos
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String


//A classe também implementa a interface "Serializable",
// o que significa que um objeto da classe pode ser serializado,
) : Serializable
