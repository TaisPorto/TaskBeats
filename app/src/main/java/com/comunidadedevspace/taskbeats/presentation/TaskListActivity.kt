package com.comunidadedevspace.taskbeats.presentation

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.comunidadedevspace.taskbeats.data.AppDataBase
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.Task
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.Serializable


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {


    // Declarando a imagem
    private lateinit var ctnContent: LinearLayout

    //Adapter
    private val adapter: TaskListAdapter2 by lazy {
        TaskListAdapter2(::onListItemClicked)
    }


    //Este código cria uma instância do banco de dados utilizando
    // a classe Room.databaseBuilder() que recebe três parâmetros:
    // o contexto da aplicação (applicationContext), a classe do banco de dados (AppDataBase::class.java)
    // e o nome do banco de dados ("taskbeats-database").

    private val dataBase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "taskbeats-database"
        ).build()

    }

    //Instancia do Objeto Dao para manipulação dos dados do banco de dados da aplicação

    private val dao by lazy {
        dataBase.taskDao()

    }

    // Devolve a ação de deletar
    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {

            // Pegando resultado
            val data = result.data
            val taskAction = data?.getSerializableExtra(TASK_ACTION_RESULT) as TaskAction
            val task: Task = taskAction.task

            when (taskAction.actionType) {
                ActionType.DELETE.name -> deleteById(task.id)
                ActionType.CREATE.name -> insertIntoDataBase(task)
                ActionType.UPDATE.name -> updateIntoDataBase(task)
            }
        }

    }

    //este código define o layout XML para a atividade e executa a lógica padrão de criação da atividade.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        setSupportActionBar(findViewById(R.id.toolbar))


        // Recuperando a imagem
        ctnContent = findViewById(R.id.ctn_content)


        // Variável da tasklist chamando id // Recyclerview

        val rvTask: RecyclerView = findViewById(R.id.rv_task_list)
        rvTask.adapter = adapter




        // /Ativando o Button Floating

        val fab: View = findViewById(R.id.fab_add)
        fab.setOnClickListener {
            openTaskListDetail(null)
        }

        listFromDataBase()
    }


    private fun insertIntoDataBase(task: Task) {
        CoroutineScope(IO).launch {
            dao.insert(task)
            listFromDataBase()
        }

    }

    private fun updateIntoDataBase(task: Task) {
        CoroutineScope(IO).launch {
            dao.update(task)
            listFromDataBase()
        }
    }

    private fun deleteAll() {
        CoroutineScope(IO).launch {
            dao.deleteAll()
            listFromDataBase()
        }
    }

    private fun deleteById(id: Int) {
        CoroutineScope(IO).launch {
            dao.deleteById(id)
            listFromDataBase()
        }
    }
    // Recupera uma lista de objetos do tipo 'TaskTask.
    // Em seguida, ele atualiza a lista de itens do RecyclerViewRecyclerView

    private fun listFromDataBase() {
        CoroutineScope(IO).launch {
            val myDataBaseList: List<Task> = dao.getAll()
            adapter.submitList(myDataBaseList)

        }

    }

    // Criando função de Snakbar para ser utilizado em outras ações

    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

    //  abridge a proxima tela TaskDetails

    private fun onListItemClicked(task: Task) {
        openTaskListDetail(task)
    }

    //Este código define uma função chamada "openTaskListDetail()"
    // que recebe um parâmetro opcional "task" do tipo "Task".
    // Essa função é responsável por abrir a tela de detalhes da lista de tarefas.

    private fun openTaskListDetail(task: Task? = null) {
        val intent = TaskDetailActivity.start(this, task)
        startForResult.launch(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_task_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_task1 -> {
                deleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }

    }
}

//ste código define uma enumeração com três valores constantes que são usados
// para especificar o tipo de ação que será realizada em uma tarefa.

//Crud

enum class ActionType {
    DELETE,
    UPDATE,
    CREATE
}

//este código define uma classe de dados que representa uma ação realizada em uma tarefa,
// especificando o tipo de ação e a própria tarefa.

data class TaskAction(
    val task: Task,
    val actionType: String
) : Serializable

//A constante "TASK_ACTION_RESULT" é usada para definir uma chave para identificar o
// resultado de uma ação relacionada a uma tarefa.
// Essa chave é usada para recuperar o resultado da ação após a atividade ser concluída,
// por meio do objeto "Intent" retornado.

const val TASK_ACTION_RESULT = "TASK_ACTION_RESULT"











