package br.ufla.dcc.ppoo;

import br.ufla.dcc.ppoo.controller.ComentarioController;
import br.ufla.dcc.ppoo.controller.MusicaController;
import br.ufla.dcc.ppoo.exceptions.CampoVazioException;
import br.ufla.dcc.ppoo.exceptions.ConfirmacaoDeSenhaException;
import br.ufla.dcc.ppoo.exceptions.EmailInvalidoException;
import br.ufla.dcc.ppoo.exceptions.EmailJaCadastradoException;
import br.ufla.dcc.ppoo.exceptions.LoginInvalidoException;
import br.ufla.dcc.ppoo.exceptions.SenhaCurtaException;
import br.ufla.dcc.ppoo.exceptions.TamanhoMaximoComentario;
import br.ufla.dcc.ppoo.model.Comentario;
import br.ufla.dcc.ppoo.view.TelaComentario;
import br.ufla.dcc.ppoo.view.TelaInicial;
import java.io.IOException;

public class BachSys {

    public static void main(String[] args) throws LoginInvalidoException, EmailJaCadastradoException, ConfirmacaoDeSenhaException, SenhaCurtaException, 
                                                 CampoVazioException, EmailInvalidoException, IOException, ClassNotFoundException {
        
        TelaInicial ti = new TelaInicial(null);
        ti.setVisible(true);
//        TelaMusicas tp = new TelaMusicas(null);
//        tp.setVisible(true);
        

    }
    
}
