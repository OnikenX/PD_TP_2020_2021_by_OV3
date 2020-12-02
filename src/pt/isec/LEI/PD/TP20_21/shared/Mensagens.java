package pt.isec.LEI.PD.TP20_21.shared;

import java.io.Serializable;

public class Mensagens implements Serializable {

    /**
     * pedido para se ligar ao servidor, é um registo se {isRegistado} é falso caso contrario é um login
     */
    public static class PedidoDeLigar implements Serializable{
        private String username  = "00000000000000000000000000000000000000000000000000000000000000";
        private String password = "00000000000000000000000000000000000000000000000000000000000000";
        private boolean registado = false;
        public static int SIZE = 210;//na verdade o size é 205 mas meti mais 5 so pelo seguro

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
         * Construtor de default com username e password a 62 characters e registado a false
         */
        public PedidoDeLigar() {}

         public void setAll(String username, String password, boolean registado){
            if (username.getBytes().length >62 || password.getBytes().length > 62)
                throw new IllegalArgumentException("Username and password sizes must be smaller than 62.");
            this.username = username;
            this.password = password;
            this.registado = registado;
        }


        public PedidoDeLigar(String username, String password, boolean registado) {
            setAll(username, password, registado);
        }
    }
}
