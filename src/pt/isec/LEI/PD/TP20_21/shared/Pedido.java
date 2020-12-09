package pt.isec.LEI.PD.TP20_21.shared;

import java.io.Serializable;
import java.util.Objects;

import static pt.isec.LEI.PD.TP20_21.shared.Utils.objectToBytes;

public class Pedido implements Serializable {

    /**
     * pedido para se ligar ao servidor, é um registo se {isRegistado} é falso caso contrario é um login
     */
    public static class Conectar implements Serializable {
        private String username = "1111111111111111111111111111111111111111111111111111111111111111";
        private String password = "1111111111111111111111111111111111111111111111111111111111111111";
        private boolean registado = false;
        public static int SIZE = (Objects.requireNonNull(objectToBytes(new Conectar())).length);//na verdade o size é 205 mas meti mais 5 so pelo seguro

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
        public Conectar() {}

        public void setAll(String username, String password, boolean registado) {
            if (username.getBytes().length > 64 || password.getBytes().length > 64)
                throw new IllegalArgumentException("Username and password sizes must be smaller than 62.");
            this.username = username;
            this.password = password;
            this.registado = registado;
        }


        public Conectar(String username, String password, boolean registado) {
            setAll(username, password, registado);
        }


    }

    public static class Ping implements Serializable {
        private int lotacao;
//        public static int SIZE = (Objects.requireNonNull(objectToBytes(new Ping())).length);

        public Ping(int lotacao){
            this.lotacao = lotacao;
        }
        public Ping() {}

        public int getLotacao() {
            return lotacao;
        }

        public void setLotacao(int lotacao) {
            this.lotacao = lotacao;
        }
    }
}
