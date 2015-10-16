package Utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static Pattern pattern;
    private static Matcher matcher;    
    private static final String REGEX_EMAIL= "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";    
    private static final String REGEX_FONE = "^\\([0-9]{2}?\\) [0-9]{4}?\\-[0-9]{4}?$";   
    private static final String REGEX_CEP = "^\\d{5,5}-?\\d{3,3}$";       





  
    public Utils() {
    }
    
    
    public static boolean isFoneValido(String fone) {
        try {
            pattern = Pattern.compile(REGEX_FONE);
            matcher = pattern.matcher(fone.toLowerCase());
            boolean matchFound = matcher.matches();

            if (!matchFound) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        } finally {
            pattern = null;
            matcher = null;
        }

    }
    
    
     public static boolean isCepValido(String cep) {
        try {
            pattern = Pattern.compile(REGEX_CEP);
            matcher = pattern.matcher(cep.toLowerCase());
            boolean matchFound = matcher.matches();

            if (!matchFound) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        } finally {
            pattern = null;
            matcher = null;
        }

    }

    public static boolean isEmailValido(String email) {

        try {            
            pattern = Pattern.compile(REGEX_EMAIL,Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(email.toLowerCase());
            boolean matchFound = matcher.matches();

            if (!matchFound) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        } finally {
            pattern = null;
            matcher = null;
        }

    }

    public static String removerAcento(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        return str;
    }

    public static String gerarSenhaMD5(String senha) {
        String sen = "";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }
        BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
        sen = hash.toString(16);
        return sen;
    }

    static public boolean isCPFValido(String cpf) {

        try {
            String strCpf = "";
            for (int i = 0; i < cpf.length(); i++) {

                if (cpf.substring(i, i + 1).contains(".") || cpf.substring(i, i + 1).contains("-")) {
                } else {
                    strCpf += cpf.substring(i, i + 1);
                }

            }

            strCpf.replace(".", "");
            strCpf.replace("-", "");

            int d1, d2;
            int digito1, digito2, resto;
            int digitoCPF;
            String nDigResult;

            d1 = d2 = 0;
            digito1 = digito2 = resto = 0;

            for (int nCount = 1; nCount < strCpf.length() - 1; nCount++) {
                digitoCPF = Integer.valueOf(strCpf.substring(nCount - 1, nCount)).intValue();
                //multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4 e assim por diante.
                d1 = d1 + (11 - nCount) * digitoCPF;
                //para o segundo digito repita o procedimento incluindo o primeiro digito calculado no passo anterior.
                d2 = d2 + (12 - nCount) * digitoCPF;
            };
            //Primeiro resto da divisão por 11.
            resto = (d1 % 11);
            //Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.
            if (resto < 2) {
                digito1 = 0;
            } else {
                digito1 = 11 - resto;
            }

            d2 += 2 * digito1;
            //Segundo resto da divisão por 11.
            resto = (d2 % 11);

            //Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.
            if (resto < 2) {
                digito2 = 0;
            } else {
                digito2 = 11 - resto;
            }

            //Digito verificador do CPF que está sendo validado.
            String nDigVerific = strCpf.substring(strCpf.length() - 2, strCpf.length());

            //Concatenando o primeiro resto com o segundo.
            nDigResult = String.valueOf(digito1) + String.valueOf(digito2);

            //comparar o digito verificador do cpf com o primeiro resto + o segundo resto.
            return nDigVerific.equals(nDigResult);
        } catch (NumberFormatException e) {
            return false;
        }


    }

    public static boolean isNumber(String string) {
        string = string.toUpperCase();
        int contNum = 0;
        int contLetra = 0;

        for (int i = 0; i < string.length(); i++) { // verifica  aquentidade de letra e numeros
            String ch = string.substring(i, i + 1);
            if (ch.contains("0") || ch.contains("1") || ch.contains("2") || ch.contains("3") || ch.contains("4")
                    || ch.contains("5") || ch.contains("6") || ch.contains("7") || ch.contains("8") || ch.contains("9")) {
                contNum++;
            } else {
                contLetra++;
            }
        }

        if (contLetra == 0) {
            return true;
        }

        if (contNum == 0) {
            return false;
        }

        return false;
    }
    
    
        //retorna o dia da semana dada uma data  
      public static String retornarDiaSemana(int ano, int mes, int dia)  
      {        
        Calendar calendario = new GregorianCalendar(ano, mes - 1, dia);  
        int diaSemana = calendario.get(Calendar.DAY_OF_WEEK);  
      
        return pesquisarDiaSemana(diaSemana);  
      }  
      
      public static String retornarDiaSemana(Date data) {        
        Calendar calendario = Calendar.getInstance();        
        calendario.setTime(data);
        int diaSemana = calendario.get(Calendar.DAY_OF_WEEK);  
        return pesquisarDiaSemana(diaSemana);  
      }
      
      //faz a pesquisa, dado um inteiro de 1 a 7  
      private static String pesquisarDiaSemana(int _diaSemana)  
      {  
        String diaSemana = null;  
      
        switch (_diaSemana)  
        {  
      
        case 1:  
        {  
          diaSemana = "Domingo";  
          break;  
        }  
        case 2:  
        {  
          diaSemana = "Segunda";  
          break;  
        }  
        case 3:  
        {  
          diaSemana = "Terça";  
          break;  
        }  
        case 4:  
        {  
          diaSemana = "Quarta";  
          break;  
        }  
        case 5:  
        {  
          diaSemana = "Quinta";  
          break;  
        }  
        case 6:  
        {  
          diaSemana = "Sexta";  
          break;  
        }  
        case 7:  
        {  
          diaSemana = "Sábado";  
          break;  
        }  
      
        }  
        return diaSemana;  
      
      }  
      
      
      // Método para formatar um valor
        public static String formataMoeda(double vlr){
          java.text.DecimalFormat df = new java.text.DecimalFormat("R$ ###,###,##0.00");
        return df.format( vlr );
        } 
    
}
