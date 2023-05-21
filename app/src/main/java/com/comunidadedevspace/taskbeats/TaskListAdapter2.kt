package com.comunidadedevspace.taskbeats

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TaskListAdapter2(
    private val openTaskDetailView: (task: Task) -> Unit
) : ListAdapter<Task, TaskListViewHolder>(TaskListAdapter2) {

    // Inflando o layout de forma manual

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {

        //Criando uma variavel view

        val view: View = LayoutInflater
            .from(parent.context)

            // Inflando o layout item_task
            .inflate(R.layout.item_task, parent, false)

        //Retornando a classe TaskListViewHolder
        return TaskListViewHolder(view)

    }

    //No código fornecido, o "companion object"
    // é usado para definir a lógica de comparação de dois objetos "Task"
    //A classe DiffUtil.ItemCallback é uma classe auxiliar do Android que é usada para comparar
    // duas listas de objetos e detectar quais itens foram adicionados, removidos ou alterados.

    companion object : DiffUtil.ItemCallback<Task>() {

        // Compara os Itens
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.description == newItem.description
        }
    }

    // RecyclerView chama esse método para associar um ViewHolder aos dados. Como se fosse umn for

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task, openTaskDetailView)

    }

}
// Classe para chamar o item_task xml UI

class TaskListViewHolder(
    private val view: View
) : RecyclerView.ViewHolder(view) {

    // Chamando os atributos da tasklist
    private val tvTitle = view.findViewById<TextView>(R.id.tv_task_title)
    private val tvDesc = view.findViewById<TextView>(R.id.tv_task_description)

    @SuppressLint("SetTextI18n")
    fun bind(
        task: Task,
        openTaskDetailView: (task: Task) -> Unit
    ) {


        tvTitle.text = task.title
        tvDesc.text = "${task.id}-${task.description}"

        view.setOnClickListener {
            openTaskDetailView.invoke(task)
        }

    }
}



