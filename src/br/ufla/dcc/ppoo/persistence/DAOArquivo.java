
package br.ufla.dcc.ppoo.persistence;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public abstract class DAOArquivo {
    private final String nomeArquivo;

    public DAOArquivo(String nomeArquivo) throws IOException {
        this.nomeArquivo = "arquivos/" + nomeArquivo;
        gerarArquivoSeNaoExiste();
    }
    
    protected String getNomeArquivo(){
        return nomeArquivo;
    }
    
    
    public void gerarArquivoSeNaoExiste() throws IOException {
        File arq = new File(nomeArquivo);
        if(!arq.exists()) {
            if(arq.createNewFile()) {
                arq.canRead();
                arq.canWrite();
                arq.canExecute();
                
            }
            
        }
  
    }
}
