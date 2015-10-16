package Validator;

import Interfaces.validator.IUsuarioValidator;
import Dao.RepositorioAutorizacoesBDR;
import Dao.RepositorioCidadesBDR;
import Dao.RepositorioTipoUsuariosBDR;
import Dao.RepositorioUsuariosBDR;
import Interfaces.dao.IRepositorioAutorizacoes;
import Interfaces.dao.IRepositorioCidades;
import Interfaces.dao.IRepositorioTipoUsuarios;
import Interfaces.dao.IRepositorioUsuarios;
import Model.Autorizacao;
import Model.Cidade;
import Model.TipoUsuario;
import Model.Usuario;
import Utils.Utils;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class UsuarioValidator implements IUsuarioValidator {

    @Override
    public void onCreatePreCadatro(Usuario usuario, IRepositorioUsuarios daoUser, IRepositorioCidades daoCidade, IRepositorioTipoUsuarios daoTipoUsuario) {

        usuario.setEnable(false);
        usuario.setDataBloqueio(Calendar.getInstance());
        usuario.setMotivoBloqueio("Pré-Cadastro no sistema");
        usuario.getAutorizacoes().clear();
        usuario.getAutorizacoes().add(new Autorizacao("ROLE_COMUM"));
        usuario.getAutorizacoes().add(new Autorizacao("ROLE_BOLETO"));
        
        
        validaStatus(usuario);
        validaCpfAndMatricula(usuario, daoUser);
        validaLoginAndSenha(usuario, daoUser);
        validaEnderecoResidencial(usuario, daoCidade);
        validaDadosPessoaisUsuario(usuario, daoTipoUsuario);
        validaTelefonesUsuario(usuario);
        validaAutorizacoesPreCadastro(usuario);

    }

    
    
    @Override
    public void onUpdateUsuarioAtravesDaLojinha(Usuario usuarioLogado, Usuario usuarioEditado, IRepositorioUsuarios daoUser, IRepositorioCidades daoCidade, IRepositorioTipoUsuarios daoTipoUsuario, IRepositorioAutorizacoes daoAutorizacao) {

        
            //Seta as autorizacoes do usuario selecionado
            // e os campos que não podem ser editados pela lojinha
            Usuario userSelecionadoBd = daoUser.getUsuario(usuarioEditado.getUsername());
            if (userSelecionadoBd != null && !userSelecionadoBd.getUsername().equalsIgnoreCase("")) {


                usuarioEditado.setAutorizacoes(userSelecionadoBd.getAutorizacoes());
                //Seta os outros campos que não são editaveis via lojinha
                usuarioEditado.setNome(userSelecionadoBd.getNome());
                usuarioEditado.setBairro(userSelecionadoBd.getBairro());
                usuarioEditado.setCep(userSelecionadoBd.getCep());
                usuarioEditado.setCidadeResidencia(userSelecionadoBd.getCidadeResidencia());
                usuarioEditado.setComplemento(userSelecionadoBd.getComplemento());
                usuarioEditado.setCpf(userSelecionadoBd.getCpf());
                usuarioEditado.setEmail(userSelecionadoBd.getEmail());
                usuarioEditado.setFone1(userSelecionadoBd.getFone1());
                usuarioEditado.setFone2(userSelecionadoBd.getFone2());
                usuarioEditado.setFone3(userSelecionadoBd.getFone3());
                usuarioEditado.setLogradouro(userSelecionadoBd.getLogradouro());
                usuarioEditado.setNumeroDaResidencia(userSelecionadoBd.getNumeroDaResidencia());
                usuarioEditado.setPassword(userSelecionadoBd.getPassword());
                usuarioEditado.setTipoUsuario(userSelecionadoBd.getTipoUsuario());
                usuarioEditado.setUsername(userSelecionadoBd.getUsername());                                     

            } else {
                String msg = "Erro ao selecionar usuário. Selecione um usuário válido para edição de dados.";
                throw new RuntimeException(msg);
            }
        
        
        
        
        
        //cpf e matricula
        //login e senha 
        //validaCpfAndMatriculaUpdate(usuario, daoUser);        
        validaStatus(usuarioEditado);
        validaLoginAndSenhaUpdate(usuarioEditado, daoUser);
        validaCpfAndMatriculaUpdate(usuarioEditado, daoUser);
        validaEnderecoResidencial(usuarioEditado, daoCidade);
        validaDadosPessoaisUsuario(usuarioEditado, daoTipoUsuario);
        validaTelefonesUsuario(usuarioEditado);
        validaAutorizacoesCadastro(usuarioEditado, daoAutorizacao);


        //Se o usuario estiver editando a propria conta poderá editar a senha
        somenteUsuariosAdministradoresPodemAlterarTipoDeUsuarioESenha(usuarioLogado, daoAutorizacao, daoUser, usuarioEditado);



    }
    
    
    @Override
    public void onUpdateUsuario(Usuario usuarioLogado, Usuario usuarioEditado, IRepositorioUsuarios daoUser, IRepositorioCidades daoCidade, IRepositorioTipoUsuarios daoTipoUsuario, IRepositorioAutorizacoes daoAutorizacao) {

        //cpf e matricula
        //login e senha 
        //validaCpfAndMatriculaUpdate(usuario, daoUser);        
        validaStatus(usuarioEditado);
        validaLoginAndSenhaUpdate(usuarioEditado, daoUser);
        validaCpfAndMatriculaUpdate(usuarioEditado, daoUser);
        validaEnderecoResidencial(usuarioEditado, daoCidade);
        validaDadosPessoaisUsuario(usuarioEditado, daoTipoUsuario);
        validaTelefonesUsuario(usuarioEditado);
        validaAutorizacoesCadastro(usuarioEditado, daoAutorizacao);


        //Se o usuario estiver editando a propria conta poderá editar a senha
        somenteUsuariosAdministradoresPodemAlterarTipoDeUsuarioESenha(usuarioLogado, daoAutorizacao, daoUser, usuarioEditado);



    }

    
    @Override
    public void onCreateUsuarioPelaLojinha(Usuario usuario, IRepositorioUsuarios daoUser, IRepositorioCidades daoCidade,IRepositorioTipoUsuarios daoTipoUsuario, IRepositorioAutorizacoes daoAutorizacao) {

        usuario.getAutorizacoes().clear();
        usuario.getAutorizacoes().add(new Autorizacao("ROLE_COMUM"));
        usuario.getAutorizacoes().add(new Autorizacao("ROLE_BOLETO"));
        
        validaStatus(usuario);
        validaCpfAndMatricula(usuario, daoUser);
        validaLoginAndSenha(usuario, daoUser);
        validaEnderecoResidencial(usuario, daoCidade);
        validaDadosPessoaisUsuario(usuario, daoTipoUsuario);
        validaTelefonesUsuario(usuario);
        validaAutorizacoesCadastro(usuario, daoAutorizacao);
    }
       
    @Override
    public void onCreateUsuario(Usuario usuario, IRepositorioUsuarios daoUser, IRepositorioCidades daoCidade, IRepositorioTipoUsuarios daoTipoUsuario, IRepositorioAutorizacoes daoAutorizacao) {
       
        validaStatus(usuario);
        validaCpfAndMatricula(usuario, daoUser);
        validaLoginAndSenha(usuario, daoUser);
        validaEnderecoResidencial(usuario, daoCidade);
        validaDadosPessoaisUsuario(usuario, daoTipoUsuario);
        validaTelefonesUsuario(usuario);
        validaAutorizacoesCadastro(usuario, daoAutorizacao);
    }

    private void validaStatus(Usuario usuario) {
        if (usuario.getEnable() != false && usuario.getEnable() != true) {
            throw new RuntimeException("Status: Status do usuário inválido.Informe um Status válido para o usuário. Obs: O status pode ser ativo ou inativo.");
        }
    }

    private void validaCpfAndMatricula(Usuario usuario, IRepositorioUsuarios daoUser) throws RuntimeException {
        //        //validações para realmente inserir no banco de dados a inscrição
        //
        //        //Verifica se CPF é válido
        try {
            CPFValidator cpfValidator = new CPFValidator(true);
            cpfValidator.assertValid(usuario.getCpf());
        } catch (InvalidStateException e) {
            throw new RuntimeException("CPF: CPF inválido.Informe um CPF válido.");
        }

        //Verifica se ja existe um outro usuário cadastrado com o mesmo cpf
        Usuario usuarioBD = daoUser.getUsuarioByCPF(usuario.getCpf());
        if (usuarioBD != null && usuarioBD.getCpf().equalsIgnoreCase(usuario.getCpf())) {
            throw new RuntimeException("CPF: Já existe outro usuário cadastrado com o CPF informado.");
        }



        //Verifica se o usuário preencheou o campo matrícula
        if (usuario.getMatricula() == null || usuario.getMatricula().trim().equalsIgnoreCase("")) {
            throw new RuntimeException("Matrícula: Matrícula inválida.Informe uma matrícula válida par ao usuário.");
        } else {
            //Verifica se ja existe um outro usuário cadastrado com o mesma matricula
            usuarioBD = daoUser.getUsuarioByMatricula(usuario.getMatricula());
            if (usuarioBD != null && usuarioBD.getMatricula().equalsIgnoreCase(usuario.getMatricula())) {
                throw new RuntimeException("Matrícula: Já existe outro usuário cadastrado com a mesma Matrícula informada.");
            }
        }






    }

    private void validaLoginAndSenha(Usuario usuario, IRepositorioUsuarios daoUser) throws RuntimeException {
        if (usuario.getPassword() == null || usuario.getPassword().trim().contains(" ")) {
            throw new RuntimeException("Senha: Senha do usuário inválida.Informe uma senha válida para o usuário. Obs: A senha não poderá conter espaços em branco.");
        }
        if (usuario.getUsername() == null || usuario.getUsername().trim().contains(" ")) {
            throw new RuntimeException("Login: Login do usuário inválido.Informe um login válido para o usuário. Obs: O login não poderá conter espaços em branco.");
        } else {
            //verifica se o login informado ja existe no banco de dados
            Usuario usuarioBd = daoUser.getUsuario(usuario.getUsername().trim());
            if (usuarioBd != null) {
                throw new RuntimeException("Login: Login do usuário já cadastrado no banco de dados.Informe um outro login válido para o usuário. Obs: O login não poderá conter espaços em branco.");
            }
        }
    }

    private void validaEnderecoResidencial(Usuario usuario, IRepositorioCidades daoCidade) throws RuntimeException {



        //Verifica se o endereço do usuário foi informado
        if (usuario.getLogradouro() == null || usuario.getLogradouro().trim().equalsIgnoreCase("")) {
            throw new RuntimeException("Endereço: Endereço do usuário inválido.Informe um endereço válido para o usuário.");
        }

        //Verifica se o endereço do usuário foi informado
        if (usuario.getBairro() == null || usuario.getBairro().trim().equalsIgnoreCase("")) {
            throw new RuntimeException("Bairro: Bairro do usuário inválido.Informe um bairro válido para o usuário.");
        }

        //Verifica se o endereço do usuário foi informado
        if (usuario.getNumeroDaResidencia() == null || usuario.getNumeroDaResidencia().trim().equalsIgnoreCase("")) {
            throw new RuntimeException("Número da Residência: Número da Residência do usuário inválido.Informe um número de residência válido para o usuário.");
        }


        if (usuario.getCidadeResidencia() == null || usuario.getCidadeResidencia().getId() == null) {
            throw new RuntimeException("Cidade: Cidade do usuário inválida. Informe uma cidade válida.");
        } else {
            //Verifica se a cidade informada pelo usuário existe no banco de dados
            Cidade cidadeBD = daoCidade.getCidade(usuario.getCidadeResidencia().getId());
            if (cidadeBD == null) {
                throw new RuntimeException("Cidade: Cidade do usuário inválida. Informe uma cidade válida.");
            } else {
                usuario.setCidadeResidencia(cidadeBD);
            }
        }


        if (usuario.getComplemento() != null && usuario.getComplemento().trim().equalsIgnoreCase("")) {
            usuario.setComplemento(null);
        }


    }

    private void validaTelefonesUsuario(Usuario usuario) throws RuntimeException {
        //Valida Fone 01
        if (usuario.getFone1() == null || (usuario.getFone1() != null && usuario.getFone1().trim().equalsIgnoreCase(""))) {
            usuario.setFone1(null);
        } else {
            //verifica se o fone informado é inválido   
            if (usuario.getFone1() == null || (usuario.getFone1().trim().equalsIgnoreCase("")) || !Utils.isFoneValido(usuario.getFone1())) {
                throw new RuntimeException("Telefone 01: Telefone 01 informado inválido. Informe um Telefone 01 válido.");
            }
        }

        //Valida Fone 02
        if (usuario.getFone2() == null || (usuario.getFone2() != null && usuario.getFone2().trim().equalsIgnoreCase(""))) {
            usuario.setFone2(null);
        } else {
            //verifica se o fone informado é inválido   
            if (usuario.getFone2() == null || (usuario.getFone2().trim().equalsIgnoreCase("")) || !Utils.isFoneValido(usuario.getFone2())) {
                throw new RuntimeException("Telefone 02: Telefone 02 informado inválido. Informe um Telefone 02 válido.");
            }
        }

        //Valida Fone 03
        if (usuario.getFone3() == null || (usuario.getFone3() != null && usuario.getFone3().trim().equalsIgnoreCase(""))) {
            usuario.setFone3(null);
        } else {
            //verifica se o fone informado é inválido   
            if (usuario.getFone3() == null || (usuario.getFone3().trim().equalsIgnoreCase("")) || !Utils.isFoneValido(usuario.getFone3())) {
                throw new RuntimeException("Telefone 03: Telefone 03 informado inválido. Informe um Telefone 03 válido.");
            }
        }
    }

    private void validaDadosPessoaisUsuario(Usuario usuario, IRepositorioTipoUsuarios daoTipoUsuario) throws RuntimeException {
        //if (valorString != null && !(valorString.equalsIgnoreCase("")) && !Utils.isEmailValido(valorString)) {

        //Verifica se o nome do usuário foi informado
        if (usuario.getNome() == null || usuario.getNome().trim().equalsIgnoreCase("")) {
            throw new RuntimeException("Nome: Nome do usuário inválido.Informe um nome válido para o usuário.");
        }


        //Verifica se o sexo do usuário é válido
        if (!usuario.getSexo().equals("M") && !usuario.getSexo().equals("F")) {
            throw new RuntimeException("Sexo: Sexo inválido.Informe um Sexo válido.");
        }






        //valida o tipo de usuario informado pelo usuário
        if (usuario.getTipoUsuario() == null || usuario.getTipoUsuario().getId() == null) {
            throw new RuntimeException("Tipo de Usuário: Tipo de Usuário informado inválido. Informe um Tipo de Usuário válido.");
        } else {
            //Verifica se o tipo de usuario informado pelo usuário existe no banco de dados
            TipoUsuario tipoUsuarioBD = daoTipoUsuario.getTipoUsuario(usuario.getTipoUsuario().getId());
            if (tipoUsuarioBD == null) {
                throw new RuntimeException("Tipo de Usuário: Tipo de Usuário informado inválido. Informe um Tipo de Usuário válido.");
            } else {
                usuario.setTipoUsuario(tipoUsuarioBD);
            }
        }
        
         


        //verifica se email informado é inválido
        if (usuario.getEmail() == null || (usuario.getEmail().trim().equalsIgnoreCase("")) || !Utils.isEmailValido(usuario.getEmail())) {
            throw new RuntimeException("Email: Email informado inválido. Informe um Email de Usuário válido.");
        }

        //verifica se cep informado é inválido
        if (usuario.getCep() == null || (usuario.getCep().trim().equalsIgnoreCase("")) || !Utils.isCepValido(usuario.getCep())) {
            throw new RuntimeException("CEP: CEP informado inválido. Informe um CEP válido.");
        }
    }

    private void validaAutorizacoesPreCadastro(Usuario usuario) throws RuntimeException {
        //validar autorizacoes informadas no pré cadastro
        
        
        //Se o usuario for do tipo aluno adicionar a role_aluno para o mesmo
        if(usuario.getTipoUsuario().getDescricao().equals("Aluno")){
                usuario.getAutorizacoes().add(new Autorizacao("ROLE_ALUNO"));                
        }
        
        if (usuario.getAutorizacoes() == null || usuario.getAutorizacoes().isEmpty()) {
            throw new RuntimeException("Permissões: Erro ao Cadastrar Usuário.Informe permissões válidas.");
        } else {
            //verifica se as permissoes informadas foram: ROLE_COMUM, ROLE_BOLETO
            if (usuario.getAutorizacoes() != null && !usuario.getAutorizacoes().isEmpty()) {
                for (Iterator it = usuario.getAutorizacoes().iterator(); it.hasNext();) {
                    Autorizacao autorizacao = (Autorizacao) it.next();
                    //SE A PERMISSÃO NÃO FOR ROLE_COMUM OU ROLE_BOLETO OU ROLE_ALUNO RETORNA UM ERRO 
                    if (!(autorizacao.getNome().trim().equals("ROLE_COMUM") || autorizacao.getNome().trim().equals("ROLE_BOLETO") || autorizacao.getNome().trim().equals("ROLE_ALUNO"))) {                        
                        throw new RuntimeException("Permissões: Erro ao cadastrar Usuário. Informe permissões válidas.");
                    }
                }
            }
        }
    }

    private void validaAutorizacoesCadastro(Usuario usuario, IRepositorioAutorizacoes daoAutorizacao) {

        //validar autorizacoes informadas no pré cadastro
        if (usuario.getAutorizacoes() == null || usuario.getAutorizacoes().isEmpty()) {
            throw new RuntimeException("Permissões: Erro ao Cadastrar Usuário.Informe permissões válidas.");
        } else {
            //verifica se as permissoes informadas são válidas e seta apenas as permissões válidas
            if (usuario.getAutorizacoes() != null && !usuario.getAutorizacoes().isEmpty()) {
                String whereRolesName = "";
                for (Autorizacao autorizacao : usuario.getAutorizacoes()) {
                    whereRolesName = whereRolesName + "\'" + autorizacao.getNome() + "\',";
                }

                whereRolesName = whereRolesName.substring(0, whereRolesName.length() - 1);

                List<Autorizacao> listAutorizacoesUsuario = daoAutorizacao.list(whereRolesName);

                if (listAutorizacoesUsuario == null) {
                    throw new RuntimeException("Permissões: Erro ao cadastrar Usuário. Informe permissões válidas.");
                } else {
                    usuario.setAutorizacoes(listAutorizacoesUsuario);
                }

            }
        }
    }

    private void validaLoginAndSenhaUpdate(Usuario usuario, IRepositorioUsuarios daoUser) {

        //Mudar aqui para não permitir alteração do login do usuario;        
        if (usuario.getUsername() == null || usuario.getUsername().trim().contains(" ")) {
            throw new RuntimeException("Login: Login do usuário inválido.Informe um login válido para o usuário. Obs: O login não poderá conter espaços em branco.");
        } else {
            //verifica se o login informado ja pertence a outro usuario existente no banco de dados
            Usuario usuarioBd = daoUser.getUsuario(usuario.getUsername());
            if (usuarioBd != null && !usuarioBd.getUsername().equals(usuario.getUsername())) {
                throw new RuntimeException("Login: Login do usuário já cadastrado no banco de dados.Informe um outro login válido para o usuário. Obs: O login não poderá conter espaços em branco.");
            }
        }


        //verifica se o usuario preencheu uma nova senha para alteração
        //caso o usuário não tenha preenchido com uma nova senha para alteração seta a senha antiga que 
        //está no BD    
        if (usuario.getPassword() == null || usuario.getPassword().trim().equalsIgnoreCase("")) {
            Usuario userBd = daoUser.getUsuario(usuario.getUsername());
            usuario.setPassword(userBd.getPassword());
        }

        if (usuario.getPassword() == null || usuario.getPassword().trim().contains(" ")) {
            throw new RuntimeException("Senha: Senha do usuário inválida.Informe uma senha válida para o usuário. Obs: A senha não poderá conter espaços em branco.");
        }


    }

    private void validaCpfAndMatriculaUpdate(Usuario usuario, IRepositorioUsuarios daoUser) {


        //        //Verifica se CPF é válido
        try {
            CPFValidator cpfValidator = new CPFValidator(true);
            cpfValidator.assertValid(usuario.getCpf());
        } catch (InvalidStateException e) {
            throw new RuntimeException("CPF: CPF inválido.Informe um CPF válido.");
        }

        //Verifica se ja existe um outro usuário cadastrado com o mesmo cpf
        Usuario usuarioBD = daoUser.getUsuarioByCPF(usuario.getCpf());
        if (usuarioBD != null && usuarioBD.getCpf().equalsIgnoreCase(usuario.getCpf()) && !usuarioBD.getUsername().equals(usuario.getUsername())) {
            throw new RuntimeException("CPF: Já existe outro usuário cadastrado com o CPF informado.");
        }



        //Verifica se o usuário preencheu o campo matrícula
        if (usuario.getMatricula() == null || usuario.getMatricula().trim().equalsIgnoreCase("")) {
            throw new RuntimeException("Matrícula: Matrícula inválida.Informe uma matrícula válida par ao usuário.");
        } else {
            //Verifica se ja existe um outro usuário cadastrado com o mesma matricula
            usuarioBD = daoUser.getUsuarioByMatricula(usuario.getMatricula());
            if (usuarioBD != null && usuarioBD.getMatricula().equalsIgnoreCase(usuario.getMatricula()) && !usuarioBD.getUsername().equals(usuario.getUsername())) {
                throw new RuntimeException("Matrícula: Já existe outro usuário cadastrado com a mesma Matrícula informada.");
            }
        }
    }

    //Mas se o usuario Logado estiver editando a sua propria conta ele poderá editar a senha somente a senha
    private void somenteUsuariosAdministradoresPodemAlterarTipoDeUsuarioESenha(Usuario usuarioLogado, IRepositorioAutorizacoes daoAutorizacao, IRepositorioUsuarios daoUser, Usuario usuarioEditado) throws RuntimeException {
        //apenas usuarios com a role administrador poderão alterar o tipo de usuario e a senha 
        String msg = "";
        try {
            if (!(usuarioLogado.getAutorizacoes().containsAll(daoAutorizacao.list("\'ROLE_ADM\'")))) {

                Usuario userBd = daoUser.getUsuario(usuarioEditado.getUsername());

                if (usuarioEditado.getTipoUsuario().getId().longValue() != userBd.getTipoUsuario().getId().longValue()) {
                    msg = "Usuário não tem permissão para alterar o tipo do usuário. Contacte o administrador do sistema.";
                    throw new RuntimeException(msg);
                }




                //SE USUSARIO LOGADO ESTA EDITANDO O PROPRIO USUARIO ELE PODERÁ EDITAR A SUA PROPRIA SENHA
                //AGORA SE NAO ELE NÃO PODERÁ EDITAR A SENHA
                if ((usuarioLogado.getUsername().equals(usuarioEditado.getUsername()))) {






                    if (usuarioEditado.getPasswordAtual() != null
                            && !usuarioEditado.getPasswordAtual().trim().equalsIgnoreCase("")
                            && !(usuarioLogado.getPassword().equals(usuarioEditado.getPasswordAtual()))) {
                        msg = "Senha atual informada inválida. Informe a senha atual corretamente para cadastrar uma nova senha.";
                        throw new RuntimeException(msg);
                    }


                    



                } else {

                    if (!usuarioEditado.getPassword().equals(userBd.getPassword())) {
                        msg = "Usuário não tem permissão para alterar a senha do usuário. Contacte o administrador do sistema.";
                        throw new RuntimeException(msg);
                    }

                }

            }
        } catch (Exception ex) {
            throw new RuntimeException(msg);
        }
    }
}
