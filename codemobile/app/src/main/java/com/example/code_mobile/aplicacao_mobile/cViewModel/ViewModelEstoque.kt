package com.example.code_mobile.paginas.code_mobile.cViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_mobile.paginas.code_mobile.cModel.ModelCategoria
import com.example.code_mobile.paginas.code_mobile.cModel.ModelEstoque
import com.example.code_mobile.paginas.code_mobile.cService.ServiceCategoria // Importe o ServiceCategoria
import com.example.code_mobile.paginas.code_mobile.cService.ServiceEstoque
import com.example.code_mobile.token.network.RetrofithAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ViewModelEstoque : ViewModel() {
    // Estados para os campos de cadastro/edição (mantendo os existentes)
    var nome = mutableStateOf("")
        private set
    var descricao = mutableStateOf("")
        private set
    var unidadeMedida = mutableStateOf("")
        private set
    var preco = mutableStateOf("")
        private set
    var quantidade = mutableStateOf("")
        private set

    // Estados para erros de validação (mantendo os existentes)
    var nomeError = mutableStateOf<String?>(null)
        private set
    var precoError = mutableStateOf<String?>(null)
        private set
    var quantidadeError = mutableStateOf<String?>(null)
        private set

    // Estados para feedback do cadastro/edição (mantendo os existentes)
    private val _cadastroSucesso = MutableStateFlow(false)
    val cadastroSucesso: StateFlow<Boolean> = _cadastroSucesso

    private val _showLoading = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading

    private val _mensagemErro = MutableStateFlow<String?>(null)
    val mensagemErro: StateFlow<String?> = _mensagemErro

    // Estados para a tela de estoque (mantendo os existentes)
    private val _estoque = MutableStateFlow<List<ModelEstoque>>(emptyList())
    val estoque: StateFlow<List<ModelEstoque>> = _estoque

    private val _isLoadingEstoque = MutableStateFlow(false)
    val isLoadingEstoque: StateFlow<Boolean> = _isLoadingEstoque

    private val _erroCarregarEstoque = MutableStateFlow<String?>(null)
    val erroCarregarEstoque: StateFlow<String?> = _erroCarregarEstoque

    private val serviceEstoque = RetrofithAuth.retrofit.create(ServiceEstoque::class.java)
    private val serviceCategoria =
        RetrofithAuth.retrofit.create(ServiceCategoria::class.java) // Crie uma instância do ServiceCategoria

    // Novo estado para a lista de categorias
    private val _categorias = MutableStateFlow<List<ModelCategoria>>(emptyList())
    val categorias: StateFlow<List<ModelCategoria>> = _categorias

    // Novo estado para a categoria selecionada na edição
    var categoriaSelecionadaEdicao = mutableStateOf<ModelCategoria?>(null)
        private set

    // Funções para atualizar os campos (mantendo os existentes)
    fun atualizarNome(novoNome: String) {
        nome.value = novoNome
        nomeError.value = null
    }

    fun atualizarDescricao(novaDescricao: String) {
        descricao.value = novaDescricao
    }

    fun atualizarUnidadeMedida(novaUnidadeMedida: String) {
        unidadeMedida.value = novaUnidadeMedida
    }

    fun atualizarPreco(novoPreco: String) {
        preco.value = novoPreco
        precoError.value = null
    }

    fun atualizarQuantidade(novaQuantidade: String) {
        quantidade.value = novaQuantidade
        quantidadeError.value = null
    }

    // Nova função para atualizar a categoria selecionada na edição
    fun atualizarCategoriaSelecionadaEdicao(categoria: ModelCategoria) {
        categoriaSelecionadaEdicao.value = categoria
    }

    // Função para validar os campos do formulário de estoque
    fun validarCamposEstoque(): Boolean {
        var isValid = true

        if (nome.value.isBlank()) {
            nomeError.value = "O nome é obrigatório."
            isValid = false
        } else {
            nomeError.value = null
        }

        if (preco.value.isBlank()) {
            precoError.value = "O preço é obrigatório."
            isValid = false
        } else {
            try {
                preco.value.toDouble()
                precoError.value = null
            } catch (e: NumberFormatException) {
                precoError.value = "O preço deve ser um número válido."
                isValid = false
            }
        }

        if (quantidade.value.isBlank()) {
            quantidadeError.value = "A quantidade é obrigatória."
            isValid = false
        } else {
            try {
                quantidade.value.toInt()
                quantidadeError.value = null
            } catch (e: NumberFormatException) {
                quantidadeError.value = "A quantidade deve ser um número inteiro válido."
                isValid = false
            }
        }

        return isValid
    }

    // Função para cadastrar um novo item no estoque
    fun cadastrarEstoque(categoriaSelecionada: ModelCategoria?) {
        if (validarCamposEstoque()) {
            _showLoading.value = true
            viewModelScope.launch {
                try {
                    if (categoriaSelecionada == null) {
                        _mensagemErro.value = "Selecione uma categoria para o item."
                        _showLoading.value = false
                        return@launch
                    }

                    val novoProduto = ModelEstoque(
                        id = null, // ID é geralmente gerado pelo backend no cadastro
                        nome = nome.value,
                        descricao = descricao.value,
                        unidadeMedida = unidadeMedida.value,
                        quantidade = quantidade.value.toInt(),
                        preco = preco.value.toBigDecimal(), // Converte para BigDecimal
                        categoria = categoriaSelecionada // Envia ModelCategoria diretamente
                    )

                    val response = withContext(Dispatchers.IO) {
                        serviceEstoque.postProduto(novoProduto)
                    }

                    if (response.isSuccessful) {
                        Log.i(
                            "ViewModelEstoque",
                            "Item do estoque cadastrado com sucesso: ${response.body()}"
                        )
                        _cadastroSucesso.value = true
                        limparCamposEstoque()
                        carregarEstoque()
                    } else {
                        val errorBodyCadastro = response.errorBody()?.string()
                        val errorCodeCadastro = response.code()
                        val mensagemErroCadastro =
                            "Erro ao cadastrar item (Código: $errorCodeCadastro): $errorBodyCadastro"
                        Log.e("ViewModelEstoque", mensagemErroCadastro)
                        _mensagemErro.value = mensagemErroCadastro
                    }
                } catch (e: NumberFormatException) {
                    Log.e("ViewModelEstoque", "Erro de formato numérico: ${e.message}")
                    _mensagemErro.value = "Erro de formato numérico. Verifique preço."
                } catch (e: IOException) {
                    Log.e("ViewModelEstoque", "Erro de conexão ao cadastrar item: ${e.message}")
                    _mensagemErro.value = "Erro de conexão: ${e.message}"
                } catch (e: Exception) {
                    Log.e("ViewModelEstoque", "Erro inesperado ao cadastrar item: ${e.message}", e)
                    _mensagemErro.value = "Erro inesperado: ${e.message}"
                } finally {
                    _showLoading.value = false
                }
            }
        }
    }

    // Função auxiliar para converter ModelCategoria (do seu frontend) para Categoria (do seu backend)
    fun ModelCategoriaParaCategoria(modelCategoria: ModelCategoria): ModelCategoria {
        return ModelCategoria(id = modelCategoria.id, nome = modelCategoria.nome)
    }

    // Função para limpar os campos do formulário de estoque
    fun limparCamposEstoque() {
        nome.value = ""
        descricao.value = ""
        unidadeMedida.value = ""
        preco.value = ""
        quantidade.value = ""
        nomeError.value = null
        precoError.value = null
        quantidadeError.value = null
    }

    // Função para limpar a mensagem de erro
    fun limparMensagemDeErro() {
        _mensagemErro.value = null
    }

    // Função para resetar o estado de sucesso do cadastro
    fun resetCadastroSucesso() {
        _cadastroSucesso.value = false
    }

    // Função para buscar a lista de itens do estoque (mantendo a existente)
    fun carregarEstoque() {
        viewModelScope.launch {
            _isLoadingEstoque.value = true
            _erroCarregarEstoque.value = null
            Log.d("ViewModelEstoque", "Iniciando carregarEstoque()")
            try {
                val response = withContext(Dispatchers.IO) {
                    Log.d("ViewModelEstoque", "Chamando serviceEstoque.getEstoque()")
                    serviceEstoque.getProdutos()
                }
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("ViewModelEstoque", "Resposta bem-sucedida, corpo: $body")
                    _estoque.value = body ?: emptyList()
                } else {
                    val errorMessage =
                        "Erro ao carregar estoque: ${response.code()} - ${response.message()}"
                    Log.e("ViewModelEstoque", errorMessage)
                    _erroCarregarEstoque.value = errorMessage
                }
            } catch (e: IOException) {
                Log.e("ViewModelEstoque", "Erro de conexão ao carregar estoque: ${e.message}")
                _erroCarregarEstoque.value = "Erro de conexão: ${e.message}"
            } catch (e: Exception) {
                Log.e("ViewModelEstoque", "Erro inesperado ao carregar estoque: ${e.message}", e)
                _erroCarregarEstoque.value = "Erro inesperado: ${e.message}"
            } finally {
                _isLoadingEstoque.value = false
                Log.d("ViewModelEstoque", "Finalizando carregarEstoque()")
            }
        }
    }

    // Nova função para carregar todas as categorias
    fun carregarCategorias() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    serviceCategoria.getCategorias()
                }
                if (response.isSuccessful) {
                    _categorias.value = response.body() ?: emptyList()
                } else {
                    Log.e(
                        "ViewModelEstoque",
                        "Erro ao carregar categorias: ${response.code()} - ${response.message()}"
                    )
                    // Opcional: Atualizar algum estado de erro de categoria
                }
            } catch (e: IOException) {
                Log.e("ViewModelEstoque", "Erro de conexão ao carregar categorias: ${e.message}")
                // Opcional: Atualizar algum estado de erro de categoria
            } catch (e: Exception) {
                Log.e("ViewModelEstoque", "Erro inesperado ao carregar categorias: ${e.message}", e)
                // Opcional: Atualizar algum estado de erro de categoria
            }
        }
    }

    // Função para excluir um item do estoque
    fun excluirEstoque(
        item: ModelEstoque,
        onExclusaoSucesso: () -> Unit,
        onExclusaoErro: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (item.id != null) {
                    val response = withContext(Dispatchers.IO) {
                        serviceEstoque.deleteProduto(item.id) // Chama a função correta do seu serviço
                    }
                    if (response.isSuccessful) {
                        Log.i(
                            "ViewModelEstoque",
                            "Item do estoque com ID ${item.id} excluído com sucesso."
                        )
                        carregarEstoque() // Recarrega a lista após a exclusão
                        onExclusaoSucesso()
                    } else {
                        val errorBodyExclusao = response.errorBody()?.string()
                        val errorCodeExclusao = response.code()
                        val mensagemErro = when (errorCodeExclusao) {
                            404 -> "Item do estoque não encontrado."
                            else -> "Erro ao excluir o item do estoque. Tente novamente."
                        }
                        Log.e(
                            "ViewModelEstoque",
                            "Erro ao excluir item do estoque ${item.id} (Código: $errorCodeExclusao): $errorBodyExclusao"
                        )
                        onExclusaoErro(mensagemErro)
                    }
                } else {
                    Log.e(
                        "ViewModelEstoque",
                        "Erro: ID do item do estoque é nulo. Não é possível excluir."
                    )
                    onExclusaoErro("Erro: ID do item do estoque é inválido.")
                }
            } catch (e: IOException) {
                Log.e(
                    "ViewModelEstoque",
                    "Erro de conexão ao excluir item do estoque: ${e.message}"
                )
                onExclusaoErro("Erro de conexão: ${e.message}")
            } catch (e: Exception) {
                Log.e(
                    "ViewModelEstoque",
                    "Erro inesperado ao excluir item do estoque: ${e.message}",
                    e
                )
                onExclusaoErro("Erro inesperado: ${e.message}")
            }
        }
    }

    // Função para preparar os dados para a edição (modificada para carregar categorias)
    fun prepararEdicao(item: ModelEstoque) {
        item.let {
            atualizarNome(it.nome)
            atualizarDescricao(it.descricao)
            atualizarUnidadeMedida(it.unidadeMedida)
            atualizarPreco(it.preco.toString())
            atualizarQuantidade(it.quantidade.toString())
            categoriaSelecionadaEdicao.value = it.categoria
        }
        carregarCategorias()
    }

    fun atualizarEstoque(
        itemAtualizadoRecebido: ModelEstoque, // Renomeei o parâmetro para clareza
        onSucesso: () -> Unit,
        onError: (String?) -> Unit
    ) {
        itemAtualizadoRecebido.id?.let { itemId ->
            viewModelScope.launch {
                try {

                    val produtoParaAtualizar = itemAtualizadoRecebido

                    val response = withContext(Dispatchers.IO) {
                        serviceEstoque.putProduto(itemId, produtoParaAtualizar)
                    }
                    if (response.isSuccessful) {
                        onSucesso()
                        carregarEstoque()
                        limparCamposEstoque()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        onError("Erro ao atualizar item (Código: ${response.code()}): $errorBody")
                    }
                } catch (e: NumberFormatException) {
                    onError("Erro de formato numérico no preço.")
                } catch (e: IOException) {
                    onError("Erro de conexão: ${e.message}")
                } catch (e: Exception) {
                    onError("Erro inesperado: ${e.message}")
                } finally {
                    // Opcional: Resetar algum estado de loading
                }
            }
        } ?: run {
            onError("ID do item não pode ser nulo para atualização.")
        }
    }
}