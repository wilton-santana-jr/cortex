package Validator;

import Interfaces.validator.IContaValidator;
import Dao.RepositorioContasBDR;
import Interfaces.dao.IRepositorioContas;
import Interfaces.dao.IRepositorioTipoOperacoes;
import Interfaces.dao.IRepositorioUsuarios;
import Model.RegistroOperacao;
import Model.Usuario;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ContaValidator implements IContaValidator {

    @Override
    public void onCreditarBoletoByUsuario(Usuario usuarioOperador, Usuario usuarioSelecionado, String valorBoleto, RegistroOperacao registroOperacao,
            IRepositorioUsuarios usuarioDao, IRepositorioContas contaDao, IRepositorioTipoOperacoes tipoOperacaoDao) {

        validaSeUsuarioACreditarPossuiContaNoSistema(contaDao, usuarioSelecionado);

        validarSeOperadorEstaColocandoCreditoNoSeuProprioCartao(usuarioOperador, usuarioSelecionado);

        validarBoleto(registroOperacao, contaDao);
        
        validarValorASerPagoDoBoleto(valorBoleto);


    }

    private void validarBoleto(RegistroOperacao registroOperacao, IRepositorioContas contaDao) throws RuntimeException {
        //o numero de boleto deve seguir o padrão do banco do brasil;
        //esse numero deve ser diferente de nulo  na operação de crédito
        
        //será usado caso algum erro ocorra para restaurar o numero do boleto na tela sem o digito verificador
        //caso ocorra algum erro
        String boletoGruSemDigitoVerificador = registroOperacao.getBoleto();
        
        if (registroOperacao != null && registroOperacao.getBoleto() != null
                && !registroOperacao.getBoleto().equalsIgnoreCase("")
                && !registroOperacao.getBoleto().isEmpty()) {

            String[] boletoCampos = registroOperacao.getBoleto().split(" ");

            if (!boletoCampos[0].matches("\\d{11}")
                    || !boletoCampos[1].matches("\\d{11}")
                    || !boletoCampos[2].matches("\\d{11}")
                    || !boletoCampos[3].matches("\\d{11}")) {
                 registroOperacao.setBoleto("");
                throw new RuntimeException("Error: Não foi possível inserir os créditos por que o número do boleto é inválido. "
                        + "Informe um número de boleto válido.");
            } else {
                try {
                    setaDigitoVerificadorNoBoleto(boletoCampos, registroOperacao);
                } catch (Exception ex) {
                    registroOperacao.setBoleto("");
                    throw new RuntimeException("Error: Não foi possível inserir os créditos por que o número do boleto é inválido. "
                            + "Informe um número de boleto válido.");
                }
            }

        }


        //verificar se o boleto já foi pago
        if (contaDao.boletoFoiPago(registroOperacao.getBoleto())) {
            registroOperacao.setBoleto("");
            throw new RuntimeException("Error: Não foi possível inserir os créditos por que o número do boleto informado já foi pago. "
                    + "Informe um novo boleto para pagamento.");
        }
    }
    
    private void setaDigitoVerificadorNoBoleto(String[] boletoCampos, RegistroOperacao registroOperacao) {
        String campo1 = boletoCampos[0] + "-" + calcDigitoVerificadorGru(boletoCampos[0]);
        String campo2 = boletoCampos[1] + "-" + calcDigitoVerificadorGru(boletoCampos[1]);
        String campo3 = boletoCampos[2] + "-" + calcDigitoVerificadorGru(boletoCampos[2]);
        String campo4 = boletoCampos[3] + "-" + calcDigitoVerificadorGru(boletoCampos[3]);
        String numeroBoletoComDigitoVerificador = campo1 + " " + campo2 + " " + campo3 + " " + campo4;
        registroOperacao.setBoleto(numeroBoletoComDigitoVerificador);
    }

    private static String calcDigitoVerificadorGru(String digStr) {
        try {
            int len = digStr.length();
            int sum = 0, rem = 0, peso = 7;

            for (int k = 1; k <= len; k++) {
                sum += (peso) * Character.getNumericValue(digStr.charAt(k - 1));
                if (peso == 9) {
                    peso = 2;
                } else {
                    peso++;
                }
            }

            if ((rem = sum % 11) == 0 || (rem = sum % 11) == 10) {
                return "0";
            }
            return String.valueOf(rem);
        } catch (Exception ex) {
            return "";
        }

    }

    private void validarValorASerPagoDoBoleto(String valorBoleto) throws RuntimeException {
        //valor do boleto tem de ser >= 10 reais
        //tem de ser diferente de zero ou nulo;
        try {
            BigDecimal valor = stringToBigDecimal(valorBoleto);
            if (valor != null && valor.doubleValue() < 10.0) {
                throw new RuntimeException("Error: Não foi possível inserir os créditos por que o valor do boleto pago é inferior a R$ 10,00 reais. Peça ao usuário que efetue o pagamento do boleto com valor no mínimo de R$ 10,00 reais.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: Não foi possível inserir os créditos por que o valor do boleto pago é inferior a R$ 10,00 reais. Peça ao usuário que efetue o pagamento do boleto com valor no mínimo de R$ 10,00 reais.");
        }
    }

    private void validarSeOperadorEstaColocandoCreditoNoSeuProprioCartao(Usuario usuarioOperador, Usuario usuarioSelecionado) throws RuntimeException {
        //usuario operador não poderá colocar crédito no seu próprio cartão
        //para isso é verificado se os dois usuários são os mesmos
        if (usuarioOperador != null && usuarioSelecionado != null
                && usuarioOperador.getUsername() != null && !usuarioOperador.getUsername().isEmpty() && !usuarioOperador.getUsername().equalsIgnoreCase("")
                && usuarioSelecionado.getUsername() != null && !usuarioSelecionado.getUsername().isEmpty() && !usuarioSelecionado.getUsername().equalsIgnoreCase("")
                && usuarioOperador.getUsername().equalsIgnoreCase(usuarioSelecionado.getUsername())) {
            throw new RuntimeException("Error: Não foi possível inserir os créditos por que o usuário selecionado para receber os créditos é o mesmo usuário que está operando o sistema. \n Entre no sistema com outro usuário para colocar os créditos no seu cartão.");
        }
    }

    private void validaSeUsuarioACreditarPossuiContaNoSistema(IRepositorioContas contaDao, Usuario usuarioSelecionado) throws RuntimeException {
        //fazer a validação do credito aqui


        //verificar se o usuário selecionado possui conta se não tenta criar  uma nova conta para o usuário selecionado;
        if (!contaDao.verificaContaDeUsuario(usuarioSelecionado)) {
            throw new RuntimeException("Error: Não foi possível inserir os créditos por que o usuário não possui conta aberta. Entre em contato com o administrador do sistema.");
        }
    }

    private BigDecimal stringToBigDecimal(final String formattedString) {
        final DecimalFormatSymbols symbols;
        final char groupSeparatorChar;
        final String groupSeparator;
        final char decimalSeparatorChar;
        final String decimalSeparator;
        String fixedString;
        final BigDecimal number;
        final Locale locale = new Locale("pt", "BR"); //Define o locale como brasil

        symbols = new DecimalFormatSymbols(locale);
        groupSeparatorChar = symbols.getGroupingSeparator();
        decimalSeparatorChar = symbols.getDecimalSeparator();

        if (groupSeparatorChar == '.') {
            groupSeparator = "\\" + groupSeparatorChar;
        } else {
            groupSeparator = Character.toString(groupSeparatorChar);
        }

        if (decimalSeparatorChar == '.') {
            decimalSeparator = "\\" + decimalSeparatorChar;
        } else {
            decimalSeparator = Character.toString(decimalSeparatorChar);
        }

        fixedString = formattedString.replaceAll(groupSeparator, "");
        fixedString = fixedString.replaceAll(decimalSeparator, ".");
        number = new BigDecimal(fixedString);

        return (number);
    }

 
}