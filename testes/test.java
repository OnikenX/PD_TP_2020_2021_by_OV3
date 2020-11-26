public class test {
    public static void main(String[] args) {
//        LinkedList<IpServidor> ll = new LinkedList<>();
//        Respostas.RUdpClientServerPreConnection resposta = new Respostas.RUdpClientServerPreConnection(6969, ll);
//        System.out.println("size resposta + linkedlist empty:" + Objects.requireNonNull(Utils.objectToBytes(resposta)).length);
//        ll.add(new IpServidor("255.255.255.255", 6969));
//        System.out.println("Size ipservidor:"+ Objects.requireNonNull(Utils.objectToBytes(ll.get(0))).length);
//        System.out.println("size resposta + linkedlist com 1 elemento:"+ Objects.requireNonNull(Utils.objectToBytes(resposta)).length);
//        while(Objects.requireNonNull(Utils.objectToBytes(resposta)).length < 5000){
//            ll.add(new IpServidor("255.255.255.255", 6969));
//        }
//        System.out.println("Maximo de ips => " + (ll.size()-1));
        thread lol = new thread();
        System.out.println("teste 1");
        lol.start();
        System.out.println("teste 2");
        lol.start();
        System.out.println("teste 3");
        lol.start();
        System.out.println("teste 4");
        lol.start();
        System.out.println("teste 5");
        lol.start();
    }

    public static class thread extends Thread {
        public static int conas = 1;
        @Override
        public void run() {
            super.run();
            int threadnumber = conas++;
            System.out.println("testing thread "+ threadnumber + "...");
            try {
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("testing thread "+ threadnumber + "...");
        }
    }
}
