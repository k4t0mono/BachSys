package br.ufla.dcc.ppoo.view;

import br.ufla.dcc.ppoo.componentes.Painel;
import br.ufla.dcc.ppoo.componentes.Tabela;
import br.ufla.dcc.ppoo.controller.MusicaController;
import br.ufla.dcc.ppoo.controller.UsuarioController;
import br.ufla.dcc.ppoo.model.Musica;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class TelaPrincipal extends Tela {
    
    private Painel painelDadosUsuario;
    private Painel painelAcoes;
    private Painel painelListaMusica;
    private JButton btnAdicionarMusica;
    private JButton btnRemoverMusica;
    private JButton btnLogout;
    private JButton btnSair;
    private Tabela tblMusicas;
    private JScrollPane painelDeRolagem;
    private JCheckBox boxMusicasUsuario;
    private List<Musica> musicas;
    private JTextField txtBusca;
    private JButton btnBuscar;
    
    
    public TelaPrincipal(Tela t) {
        super("BachSys", 800, 600, t);
        atualizarListaMusicas();
        painelDeRolagem = null;
        construirTela();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        adicionarAcoes();
        
        
    }
    
    private void atualizarListaMusicas() {
        try {
            musicas = MusicaController.getInstancia().getMusicas();
        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(null, cnfe.getMessage() + ". Recomendados chamar um técnico.", 
                                                                "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            System.out.println("ATUALIZAR MUSICAS DEU RUIM");
        }
    }
    
    private void atualizarListaMusicas(List<Musica> ms) {
        musicas = ms;
    }
    
    @Override
    protected void construirTela() {
        adicionaPainelAcoes();
        adicionarPainelDadosUsuario();
        adicionarPainelListaMusica();
        
    }
    
    
    private void setBoxMusicasTrue() {
        if(boxMusicasUsuario.isSelected()) {
            boxMusicasUsuario.setSelected(false);
        }

        boxMusicasUsuario.setSelected(true);
    }
    
    @Override
    protected void adicionarAcoes() {
        Tela t = this;
        
        btnAdicionarMusica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                TelaCadastroMusica tcm = new TelaCadastroMusica(t);
                tcm.setVisible(true);
                tcm.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentHidden(ComponentEvent e) {
                        setBoxMusicasTrue();
                    }
                });
            }
        });
        
       
        btnRemoverMusica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(boxMusicasUsuario.isSelected()) {
                    int colCheckBox = Tabela.getColunaCheckBox();
                    for(int row = 0; row < tblMusicas.getRowCount(); row++) {
                        if((Boolean) (tblMusicas.getModel().getValueAt(row, colCheckBox))) {
                            String nome = tblMusicas.getModel().getValueAt(row, 0).toString();
                            
                            try {
                                String email = UsuarioController.getInstancia().getEmailUsuarioLogado();
                                MusicaController.getInstancia().removerMusica(nome, email);
                                musicas = MusicaController.getInstancia().getMusicas(email);
                                tblMusicas.removeRowSelectionInterval(row, row);
                            } catch (IOException ex) {
                                System.out.println("deu rui no arquivo 1");
                            } catch (ClassNotFoundException ex) {
                                System.out.println("deu rui no arquivo 2");
                            }
                        }
                    }
                    setBoxMusicasTrue();
                }
            }
        });
        
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    if(UsuarioController.getInstancia().estaLogado()) {
                        Integer opcao = JOptionPane.showConfirmDialog(null, "Deseja realmente realizar logout?", "Realizar logout?",
                                                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                        if(opcao == JOptionPane.YES_OPTION) {
                            getTelaAnterior().setVisible(true);
                            UsuarioController.getInstancia().finalizarSessao();
                            setVisible(false);
                        }
                    }
                } catch (ClassNotFoundException cnfe) {
                    
                } catch (IOException ioe) {
                    
                }
            }
        });
        
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                confirmarSaida();
            }
        });
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                confirmarSaida();
            }
            
        });
        
        boxMusicasUsuario.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                
                Boolean mostrarCheckBox;
                try {
                    String email = UsuarioController.getInstancia().getEmailUsuarioLogado();
                    if (ie.getStateChange() == ItemEvent.SELECTED) {
                        musicas = MusicaController.getInstancia().getMusicas(email);
                        mostrarCheckBox = true;
                    } else {
                        musicas = MusicaController.getInstancia().getMusicas();
                        mostrarCheckBox = false;
                    }

                    criaTabelaMusicas(musicas, mostrarCheckBox);
                } catch (ClassNotFoundException cnfe) {
                    JOptionPane.showMessageDialog(null, cnfe.getMessage() + ". Recomendados chamar um técnico.", 
                                                                "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ioe) {
                    System.out.println("Box musicas deu ruim");
                }
            }
        });
        
    }
    
    private Integer confirmarSaida() {
        Integer opcao = JOptionPane.showConfirmDialog(null, "Desja realizar login em outra conta?", "Realizar novo login?",
                                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        
        switch (opcao) {
            case JOptionPane.NO_OPTION:
                try {
                    UsuarioController.getInstancia().finalizarSessao();
                    System.exit(0);
                } catch(ClassNotFoundException ex){
                    System.out.println("flag1");  
                } catch(IOException ex){
                    System.out.println("flag2");
                }
            case JOptionPane.YES_OPTION:
                try{
                    UsuarioController.getInstancia().finalizarSessao();
                    setVisible(false);
                    getTelaAnterior().setVisible(true);
                } catch(ClassNotFoundException ex){
                    System.out.println("flag3");
                } catch(IOException ex){
                    System.out.println("flag4");
                }
                
                return DO_NOTHING_ON_CLOSE;
            case JOptionPane.CANCEL_OPTION:
                return DO_NOTHING_ON_CLOSE;
            default:
                //TODO: adicionar excecao
                return EXIT_ON_CLOSE;
        }
        
    }

    
    
    private void adicionaPainelAcoes() {
        painelAcoes = new Painel(150, 350);
        painelAcoes.setBackground(Color.yellow);
        adicionarComponente(painelAcoes, GridBagConstraints.WEST, 
                GridBagConstraints.NONE, 1, 0, 1, 1);
        
        
        
        btnAdicionarMusica = new JButton("Adicionar");
        btnAdicionarMusica.setToolTipText("Adicionar uma musica ao catalogo");
        painelAcoes.adicionarComponente(btnAdicionarMusica, GridBagConstraints.CENTER,
                                        GridBagConstraints.BOTH, 0, 0, 1, 1, 0.5, 0);

        btnRemoverMusica = new JButton("Remover");
        btnRemoverMusica.setToolTipText("Remover mussicas do catalogo");
        painelAcoes.adicionarComponente(btnRemoverMusica, GridBagConstraints.CENTER,
                                        GridBagConstraints.BOTH, 1, 0, 1, 1, 0.5, 0);

        btnLogout = new JButton("Logout");
        btnLogout.setToolTipText("Fazer Logout do sistema");
        painelAcoes.adicionarComponente(btnLogout, GridBagConstraints.CENTER,
                                        GridBagConstraints.BOTH, 2, 0, 1, 1, 0.5, 0);

        btnSair = new JButton("Sair");
        btnSair.setToolTipText("Fazer logout e finalizar o programa");
        painelAcoes.adicionarComponente(btnSair, GridBagConstraints.CENTER,
                                        GridBagConstraints.HORIZONTAL, 3, 0, 1, 1, 0.5, 0);
     
    }

    
    
    private void adicionarPainelDadosUsuario() {
        try {
            painelDadosUsuario = new Painel(150, 200);
        
            painelDadosUsuario.setBackground(Color.white);
            adicionarComponente(painelDadosUsuario, GridBagConstraints.WEST, 
                    GridBagConstraints.NONE, 0, 0, 1, 1);

            String nome = UsuarioController.getInstancia().getNomeUsuarioLogado();
            JLabel lbNome = new JLabel("Bem vindo, " + nome);
            painelDadosUsuario.adicionarComponente(lbNome, GridBagConstraints.CENTER, 
                                        GridBagConstraints.HORIZONTAL, 0, 0, 1, 1);

            String email = UsuarioController.getInstancia().getEmailUsuarioLogado();

            JLabel lbEmail = new JLabel("Email: " + email);
            painelDadosUsuario.adicionarComponente(lbEmail, GridBagConstraints.CENTER, 
                                        GridBagConstraints.HORIZONTAL, 1, 0, 1, 1);

            Integer qtdMusicas;

            qtdMusicas = MusicaController.getInstancia().getQtdMusicas(email);

            JLabel lbQtdMusica = new JLabel("<html><body>Quantidade de"
                                            + " músicas cadastradas: " + qtdMusicas + "</body></html>");
            painelDadosUsuario.adicionarComponente(lbQtdMusica, GridBagConstraints.CENTER, 
                                    GridBagConstraints.HORIZONTAL, 2 , 0, 1, 4);
        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(null, cnfe.getMessage() + ". Recomendados chamar um técnico.", 
                                                                "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            System.out.println("adicionar painel erro");
        }
        
    }

    
    
    private void adicionarPainelListaMusica() {
        painelListaMusica = new Painel(600, 550);
        adicionarComponente(painelListaMusica, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 1, 1, 2);
        painelListaMusica.setBackground(Color.red);
        
        
       
        criaTabelaMusicas(musicas, false);

        boxMusicasUsuario = new JCheckBox("Ver apenas minhas músicas");
        painelListaMusica.adicionarComponente(boxMusicasUsuario, GridBagConstraints.WEST,
                                            GridBagConstraints.NONE, 0, 2, 1, 1, 0.1, 0.1);
        adicionarBarraBusca();
    }
    
    
    private void criaTabelaMusicas(List<Musica> musicas, Boolean mostrarCheckBox) {
        
        if(painelDeRolagem != null) {
            painelDeRolagem.removeAll();
            painelListaMusica.remove(painelDeRolagem);
        }
        
        String[] colunas = {"Nome", "Autor", "Album", "Gênero", "Ano" };
        
        
        tblMusicas = new Tabela(musicas, colunas, mostrarCheckBox);
        
        
        painelDeRolagem = new JScrollPane();
        painelDeRolagem.setViewportView(tblMusicas);
        
        painelListaMusica.adicionarComponente(painelDeRolagem, GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH, 1, 0, 3, 1, 1, 1);
        
        painelListaMusica.revalidate();
//        atualizarListaMusicas(musicas);
        
        adicionarAcoesTabela();
        
    }
    
    private void adicionarAcoesTabela() {
        Tela t = this;
        
        tblMusicas.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if(me.getClickCount() == 2){
                    int column = tblMusicas.getSelectedColumn();
                    int row = tblMusicas.getSelectedRow();
                    if(column == tblMusicas.getColunaCheckBox()) {
                        return;
                    }
                    TelaDadosMusica tdm = new TelaDadosMusica(musicas.get(row), t);
                    
                    tdm.setVisible(true);
                    tdm.addComponentListener(new ComponentAdapter() {
                        @Override
                        public void componentHidden(ComponentEvent e) {
                            if(tdm.musicaAlterada()) {
                                setBoxMusicasTrue();
                            }
                        }
                    });

                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
                return;
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                return;
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                return;
            }

            @Override
            public void mouseExited(MouseEvent me) {
                return;
            }
            
        });
    }

    private void adicionarBarraBusca() {
        txtBusca = new JTextField(20);
        

        
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(txtBusca.getText().equals("")) {
                    return;
                }
                //TODO: buscar
            }
        });
        
        
        
        painelListaMusica.adicionarComponente(txtBusca, GridBagConstraints.CENTER,
                                        GridBagConstraints.NONE, 0, 0, 1, 1);
    
        painelListaMusica.adicionarComponente(btnBuscar, GridBagConstraints.CENTER,
                                        GridBagConstraints.NONE, 0, 1, 1, 1);
    }
    
    
    
}
