package br.ufla.dcc.ppoo.controller;

import br.ufla.dcc.ppoo.exceptions.ConfirmacaoDeSenhaException;
import br.ufla.dcc.ppoo.exceptions.EmailJaCadastradoException;
import br.ufla.dcc.ppoo.exceptions.LoginInvalidoException;
import br.ufla.dcc.ppoo.model.Usuario;
import br.ufla.dcc.ppoo.persistence.UsuarioDAO;
import br.ufla.dcc.ppoo.seguranca.Sessao;

public class UsuarioController {
    
    private static final UsuarioDAO USUARIO_DAO = UsuarioDAO.getInstancia();
    private static final UsuarioController INSTANCIA = new UsuarioController();
    private static final Sessao SESSAO = Sessao.getInstancia();
    
    // TODO: Usar hash de verdade
    public String hash(String senha) {
        return senha;
    }
    
    public void cadastrar(String nome, String email, String senha) throws EmailJaCadastradoException, ConfirmacaoDeSenhaException{
               
        senha = this.hash(senha);
        
        Usuario u = new Usuario(nome, email, senha);
        
        USUARIO_DAO.adicionarUsuario(u);
    }
    
    
    public void iniciarSessao(String email, String senha) throws LoginInvalidoException{
        Usuario u = getUsuario(email);
        if(u == null) {
            throw new LoginInvalidoException();
        }
        if(hash(senha).equals(u.getSenha())) {
            System.out.println("aqui");
            SESSAO.alterarSessao(u);
        }
        else{
            throw new LoginInvalidoException();
        }
    }
    
    public void finalizarSessao() {
        if(estaLogado()) {
            SESSAO.finalizar();
        }
    }
    
    
    public static Boolean estaLogado() {
        return SESSAO.estaLogado();
    }
    
    public String getNomeUsuarioLogado() {
        return SESSAO.getNomeUsuario();
    }
    
    public String getEmailUsuarioLogado() {
        return SESSAO.getEmailUsuario();
    }
    
    
    public Usuario getUsuario(String email) {
        return USUARIO_DAO.getUsuario(email);
    }
    
    public static UsuarioController getInstancia() {
        return INSTANCIA;
    }
        
}
