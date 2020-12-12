package pt.isec.LEI.PD.TP20_21.shared.Comunicacoes;

import java.io.Serializable;

public class Pedido implements Serializable {
    /**
     * pedido para se ligar ao servidor, é um registo se {isRegistado} é falso caso contrario é um login
     */
    public static class Conectar implements Serializable {
        private String username = "1111111111111111111111111111111111111111111111111111111111111111";
        private String password = "1111111111111111111111111111111111111111111111111111111111111111";
        private String nome = "1111111111111111111111111111111111111111111111111111111111111111";
        private boolean registado = false;

        @Override
        public String toString() {
            return "PedidoDeLigar{" +
                    "username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", registado=" + registado +
                    '}';
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        /**
         * @return true se é para fazer login, false para registar
         */
        public boolean isRegistado() {
            return registado;
        }

        /**
         * Construtor de default com username e password a 64 characters e registado a false
         */
        public Conectar() {
        }

        public void setAll(String username, String nome, String password, boolean registado) {
            if (username.getBytes().length > 64 || password.getBytes().length > 64 || nome.getBytes().length > 64)
                throw new IllegalArgumentException("Username and password sizes must be smaller than 62.");
            this.username = username;
            this.password = password;
            this.registado = registado;
            this.nome = nome;
        }

        public String getNome() {
            return nome;
        }

        public Conectar(String username, String nome, String password, boolean registado) {
            setAll(username, nome, password, registado);
        }


    }

    public static class Ping implements Serializable {
        private int lotacao;
//        public static int SIZE = (Objects.requireNonNull(objectToBytes(new Ping())).length);

        public Ping(int lotacao) {
            this.lotacao = lotacao;
        }

        public Ping() {
        }

        public int getLotacao() {
            return lotacao;
        }

        public void setLotacao(int lotacao) {
            this.lotacao = lotacao;
        }
    }

    public static class MensagemDM implements Serializable {
        private final String userEnvia;
        private final String userRecebe;
        private final String conteudo;

        public String getUserEnvia() {
            return userEnvia;
        }

        public String getUserRecebe() {
            return userRecebe;
        }

        public String getConteudo() {
            return conteudo;
        }

        public MensagemDM(String userEnvia, String userRecebe, String conteudo) {
            this.userEnvia = userEnvia;
            this.userRecebe = userRecebe;
            this.conteudo = conteudo;
        }
    }

    public static class MensagemGrupo implements Serializable {
        private final String userEnvia;
        private final String canalNome;
        private final String conteudo;

        public String getUserEnvia() {
            return userEnvia;
        }

        public String getCanalNome() {
            return canalNome;
        }

        public String getConteudo() {
            return conteudo;
        }

        public MensagemGrupo(String userEnvia, String canalNome, String conteudo) {
            this.userEnvia = userEnvia;
            this.canalNome = canalNome;
            this.conteudo = conteudo;
        }
    }


}
