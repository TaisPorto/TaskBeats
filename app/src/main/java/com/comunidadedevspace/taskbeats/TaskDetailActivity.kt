package com.comunidadedevspace.taskbeats

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
class TaskDetailActivity : AppCompatActivity() {

    // A variável tarefa é inicializada com null e é usada para armazenar a tarefa
    // que está sendo exibida na tela.
    private var task: Task? = null

    // O botão btnConcluido é uma referência ao botão que permite concluir a tarefa.
    private lateinit var btnDone: Button

    // O objeto companion define uma constante chamada TAREFA_EXTRA,
    // que é uma chave usada para passar a tarefa como um extra para outras atividades ou fragmentos.
    companion object {

        //Pegando o Retorno de uma Activity

        private const val TASK_DETAIL_EXTRA: String = "task.extra.detail"

        //Esta função é chamada para iniciar uma nova atividade TaskDetailActivity
        // e recebe um contexto (context) e uma tarefa (task) como parâmetros

        fun start(context: Context, task: Task?): Intent {

            //Este bloco adiciona uma tarefa (task) extra para ser passada para a atividade
            val intent = Intent(context, TaskDetailActivity::class.java)
                .apply {
                    putExtra(TASK_DETAIL_EXTRA, task)
                }
            return intent

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)


        //Recuparando o menu deletar

       setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        //Recuperar task
        task = intent.getSerializableExtra(TASK_DETAIL_EXTRA) as Task?

        //Recuperar o campo do XML

        val edtTitle = findViewById<EditText>(R.id.edt_task_title)
        val edtDescription = findViewById<EditText>(R.id.edt_task_description)
        btnDone = findViewById(R.id.btn_done)

        // Verifica se a tarefa não é nula
        if (task != null) {
            // Define o texto do campo edtTitle como o título da tarefa
            edtTitle.setText(task!!.title)

            // Define o texto do campo edtDescription como a descrição da tarefa
            edtDescription.setText(task!!.description)
        }

        // Ação para o Botão Concluir
        btnDone.setOnClickListener {
            val title = edtTitle.text.toString()
            val desc = edtDescription.text.toString()

            // Verifica se o título e a descrição não estão vazios.

            // Se ambos não estiverem vazios, chama a função addNewTask com os parâmetros do título e descrição.
            if (title.isNotEmpty() && desc.isNotEmpty()) {
                addNewTask(title, desc)

                // Caso contrário, mostra uma mensagem informando que os campos são obrigatórios.
            } else {
                showMessage(it, "Campos Obrigatórios")
            }
        }
    }

    /* Esta função cria uma nova tarefa com um título e descrição fornecidos, usando a classe Task,
     e retorna a ação realizada, que neste caso é uma criação (CREATE)
     */

    private fun addNewTask(title: String, description: String) {
        val newTask = Task(0, title, description)
        returnAction(newTask, ActionType.CREATE)
    }

    // Ciclo de Vida da Activity
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        /*Neste código, o método infla o arquivo de menu menu_task_detail.xml na variável menu.
         Em seguida, retorna true para informar que o menu foi criado com sucesso.
         */
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_task_detail, menu)
        return true
    }

    // Ele checa opção deletar do MENU pelo id

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_task -> {
                if (task != null) {
                    returnAction(task!!, ActionType.DELETE)
                } else {
                    showMessage(btnDone, "Item não encontrado")
                }
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    //Este código é responsável por criar um objeto Intent,
    // adicionar a ele um objeto TaskAction como um extra e definir um resultado para a atividade atual.

    private fun returnAction(task: Task, actionType: ActionType) {
        val intent = Intent()
            .apply {
                val taskAction = TaskAction(task, actionType.name)
                putExtra(TASK_ACTION_RESULT, taskAction)
            }
        //A função returnAction recebe dois parâmetros: um objeto Task e um objeto ActionType.
        // Ele usa esses parâmetros para criar um objeto TaskAction e colocá-lo como um extra na intenção.
        //  Em seguida, ele define o resultado da atividade atual como "OK" ,
        //  e chama a função finish() para fechar a atividade.
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    //Este código é responsável por exibir uma mensagem de Snackbar na tela
    private fun showMessage(view: View, message: String) {

        //A mensagem a ser exibida é passada como parâmetro "message",
        // e a View que chama o método é passada como "view"
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)

            // O método "setAction" adiciona uma ação à Snackbar, mas neste caso,
            // como o segundo parâmetro é nulo, não há ação a ser executada.
            .setAction("Action", null)

            // Por fim, o método "show" exibe a Snackbar na tela.
            .show()
    }

}


