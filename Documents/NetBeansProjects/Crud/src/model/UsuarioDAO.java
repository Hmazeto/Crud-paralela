package model;

import connection.conexao;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.DialogUtil;

public class UsuarioDAO {
    
    private static final Logger logger = Logger.getLogger(UsuarioDAO.class.getName());
    
    ArrayList<UsuarioDTO> listaUsuarios = new ArrayList <>();
    // CREATE
    public void inserir(UsuarioDTO usuario) {
        String sql = "INSERT INTO usuario (nome, email, login, senha) VALUES (?, ?, ?, ?)";
        try (Connection conn = conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getLogin());
            stmt.setString(4, usuario.getSenha());
            stmt.executeUpdate();
            
            DialogUtil.showInfo("Usuário inserido com sucesso.");
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, sql, e);
            DialogUtil.showError("Erro ao inserir usuario.");
            
            
        }
    }

    // READ
    public List<UsuarioDTO> listar() {
        List<UsuarioDTO> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try (Connection conn = conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                UsuarioDTO usuario = new UsuarioDTO(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("login"),
                    rs.getString("senha")
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, sql, e);
            DialogUtil.showError("Erro ao listar usuario.");
        }
        return usuarios;
    }

    // UPDATE
    public void atualizar(UsuarioDTO usuario) {
        String sql = "UPDATE usuario SET nome = ?, email = ?, login = ?, senha = ? WHERE id = ?";
        try (Connection conn = conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getLogin());
            stmt.setString(4, usuario.getSenha());
            stmt.setInt(5, usuario.getId());
            stmt.executeUpdate();
            
            DialogUtil.showInfo("Usuário atualizado com sucesso.");
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, sql, e);
            DialogUtil.showError("Erro ao atualizar.");
        }
    }
    
    // Função para verificar se o usuário existe pelo email
    public boolean verificarUsuarioExiste(String email) {
        String sql = "SELECT 1 FROM usuario WHERE email = ?";
        try (Connection conn = conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // retorna true se encontrou um resultado
                
            }
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, sql, e);
            DialogUtil.showError("Erro ao verificar usuario existente.");
        }
        return false; // se der erro ou não encontrar
    }

    // DELETE
    public boolean deletar(int id) {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (Connection conn = conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;           
        } catch (SQLException e) {
            logger.log(Level.SEVERE, sql, e);
            DialogUtil.showError("Erro ao deletar usuario.");
        }return false;
    }
    
    public void deletarTudo() {
        
 
        String sql = "TRUNCATE TABLE usuario";
        try (Connection conn = conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, sql, e);
            DialogUtil.showError("Erro ao deletar tudo.");
        }
        
    }
    public boolean atualizarUsuario(UsuarioDTO usuario) {
        
    String sql = "UPDATE usuario SET nome = ?, email = ?, login = ?, senha = ? WHERE id = ?";

    try (Connection conn = conexao.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, usuario.getNome());
        stmt.setString(2, usuario.getEmail());
        stmt.setString(3, usuario.getLogin());
        stmt.setString(4, usuario.getSenha());
        stmt.setInt(5, usuario.getId());

        int linhasAfetadas = stmt.executeUpdate();
        return linhasAfetadas > 0;
    
    } catch (SQLException e) {
            logger.log(Level.SEVERE, sql, e);
            DialogUtil.showError("Erro ao atualizar usuario.");
        
    }return false;
}

}
