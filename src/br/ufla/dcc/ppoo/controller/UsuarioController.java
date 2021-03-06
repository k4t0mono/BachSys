package br.ufla.dcc.ppoo.controller;

import br.ufla.dcc.ppoo.exceptions.CampoVazioException;
import br.ufla.dcc.ppoo.exceptions.ConfirmacaoDeSenhaException;
import br.ufla.dcc.ppoo.exceptions.EmailInvalidoException;
import br.ufla.dcc.ppoo.exceptions.EmailJaCadastradoException;
import br.ufla.dcc.ppoo.exceptions.LoginInvalidoException;
import br.ufla.dcc.ppoo.exceptions.SenhaCurtaException;
import br.ufla.dcc.ppoo.model.Usuario;
import br.ufla.dcc.ppoo.persistence.UsuarioDAO;
import br.ufla.dcc.ppoo.persistence.UsuarioDAOArquivo;
import br.ufla.dcc.ppoo.seguranca.Sessao;
import java.io.IOException;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioController {
    
    private static UsuarioDAO USUARIO_DAO;
    private static UsuarioController INSTANCIA;
    private static final Sessao SESSAO = Sessao.getInstancia();
    
    private UsuarioController() throws IOException, ClassNotFoundException{
            USUARIO_DAO = UsuarioDAOArquivo.getInstancia();
    }
    
    public void cadastrar(String nome, String email, String senha, String confirmacaoSenha) 
            throws EmailJaCadastradoException, ConfirmacaoDeSenhaException,
            SenhaCurtaException, CampoVazioException, EmailInvalidoException, IOException{
        if(nome.isEmpty()){
            throw new CampoVazioException("usuário");
        }
        if(email.isEmpty()){
            throw new CampoVazioException("e-mail");
        }
        if(!verificarEmail(email)){
            throw new EmailInvalidoException();
        }
        if(getUsuario(email) != null){
            throw new EmailJaCadastradoException();
        }
        if(!senha.equals(confirmacaoSenha)){
            throw new ConfirmacaoDeSenhaException();
        }
        
        Usuario u = new Usuario(nome, email, senha);
        
        USUARIO_DAO.adicionarUsuario(u);
    }
    
    private Boolean verificarEmail(String email) {
        if(email.length() < 5) {
            return false;
        }
        Boolean achou = false;
        int posAchou = -1;
        for(int i = 0; (i < email.length()) && (!achou); i++) {
            if(email.charAt(i) == '@') {
                achou = true;
                posAchou = i;
            }
        }
        if(!achou) {
            return false;
        }
        
        achou = false;
        for(int i = posAchou; (i < email.length() - 1) && (!achou); i++) {
            if(email.charAt(i) == '.') {
                achou = true;
            }
        }
        
        if(!achou) {
            return false;
        }
        return true;   
    }
    
    
    public void iniciarSessao(String email, String senha) throws LoginInvalidoException{
        Usuario u = getUsuario(email);
        if(u == null) {
            throw new LoginInvalidoException();
        }
        if(BCrypt.checkpw(senha, u.getSenha())) {
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
    
    public static UsuarioController getInstancia() throws IOException, ClassNotFoundException {
        if(INSTANCIA == null){
            INSTANCIA = new UsuarioController();
        }
        return INSTANCIA;
    }
        
}
