package SERVER;


import SERVER.HELPER.Hybrid_Encryption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

    /**
     *
     * @author thuanvo
     */
    public class MultiThreadBus {
        static List<MultiThread> listServerThreads;

        public List<MultiThread> getListServerThreads() {
            return listServerThreads;
        }

        public MultiThreadBus() {
            listServerThreads = new ArrayList<>();
        }

        public void add(MultiThread serverThread){
            listServerThreads.add(serverThread);
        }

        public void writeSocket(int clientID, String json, String clientKey) throws IOException {
            for(MultiThread serverThread : Server.multiThreadBus.getListServerThreads()){
                if (serverThread.getClientID() == clientID) {
                    if (clientKey == ""){
                        serverThread.writeSocket(json);
                        break;
                    }else {
                        String data = Hybrid_Encryption.encryptAES(json, clientKey);
                        serverThread.writeSocket(data);
                    }
                }
            }
        }

        public void removeSocket(int clientID){
            for(int i=0; i < Server.multiThreadBus.getLength(); i++){
                if(Server.multiThreadBus.getListServerThreads().get(i).getClientID() == clientID){
                    Server.multiThreadBus.listServerThreads.remove(i);
                }
            }
        }

        public int getLength(){
            return listServerThreads.size();
        }
}
