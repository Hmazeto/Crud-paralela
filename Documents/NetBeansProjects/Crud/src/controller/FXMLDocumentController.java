package controller;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.UsuarioDAO;
import model.UsuarioDTO;
import util.DialogUtil;
import validator.UsuarioValidator;

public class FXMLDocumentController implements Initializable{
    
    private final UsuarioValidator usuarioValidator = new UsuarioValidator();

    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtLogin;
    @FXML
    private PasswordField txtSenha;

    @FXML
    private TableView<UsuarioDTO> tblUsuarios;
    @FXML
    private TableColumn<UsuarioDTO, String> colNome;
    @FXML
    private TableColumn<UsuarioDTO, String> colEmail;
    @FXML
    private TableColumn<UsuarioDTO, String> colLogin;
    @FXML
    private TableColumn<UsuarioDTO, String> colSenha;

    @FXML
    private void initialize() {
        // Configura colunas da tabela
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        colSenha.setCellValueFactory(new PropertyValueFactory<>("senha"));

        mostrarUsuarios();
    }

    @FXML
    private void cadastrar(ActionEvent event) {
        
        if(!usuarioValidator.validarUsuario(txtNome.getText(), txtEmail.getText(), txtSenha.getText(), txtLogin.getText())){
            return;
        }
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String login = txtLogin.getText();
        String senha = txtSenha.getText();

        if (nome.isEmpty() || email.isEmpty() || login.isEmpty() || senha.isEmpty()) {
            return;
        }
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setLogin(login);
        usuario.setSenha(senha);

        usuarioDAO.inserir(usuario);
        limparCampos();
        DialogUtil.showInfo("Usuário cadastrado com sucesso.");
        mostrarUsuarios(); // Atualiza tabela

    }

    @FXML
    private void carregarCampos(){
        UsuarioDTO usuarioDTO = tblUsuarios.getSelectionModel().getSelectedItem();
        
            if (usuarioDTO != null) {
                txtNome.setText (usuarioDTO.getNome());
                txtEmail.setText (usuarioDTO.getEmail());
                txtSenha.setText (usuarioDTO.getSenha());
                txtLogin.setText (usuarioDTO.getLogin());
            }
    }
    
    @FXML
    private void mostrarUsuarios() {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            List<UsuarioDTO> usuarios = usuarioDAO.listar();
            ObservableList<UsuarioDTO> selectUser = FXCollections.observableArrayList(usuarios);
            tblUsuarios.setItems(selectUser);
        } catch (Exception e) {
          logger.log(Level.SEVERE, sql, e);
            DialogUtil.showError("Erro ao inserir usuario.");
        }
    }

    @FXML
    private void deletarUsuarioSelecionado() {

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        UsuarioDTO usuarioSelecionado = tblUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSelecionado == null) {
            return;
        }

        boolean sucesso = usuarioDAO.deletar(usuarioSelecionado.getId());
        if (sucesso) {
            mostrarUsuarios(); // Atualiza a tabela
        } else {
        }
    }

    @FXML
    private void atualizarSelecionado() {

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        UsuarioDTO selecionado = tblUsuarios.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            return;
        }

        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String login = txtLogin.getText();
        String senha = txtSenha.getText();

        if (nome.isEmpty() || email.isEmpty() || login.isEmpty() || senha.isEmpty()) {
            return;
        }

        selecionado.setNome(nome);
        selecionado.setEmail(email);
        selecionado.setLogin(login);
        selecionado.setSenha(senha);

        boolean sucesso = usuarioDAO.atualizarUsuario(selecionado);

        if (sucesso) {
            mostrarUsuarios(); // Atualiza tabela
            limparCampos();
        } else {
        }
    }

    private void limparCampos() {
        txtNome.clear();
        txtEmail.clear();
        txtLogin.clear();
        txtSenha.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}
