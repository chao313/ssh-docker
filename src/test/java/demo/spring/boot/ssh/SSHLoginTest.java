package demo.spring.boot.ssh;

import com.jcraft.jsch.JSchException;

public class SSHLoginTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        testSSH();
    }

    public static void testSSH() {
        String host = "120.77.37.251";
        Integer port = 22;
        String user = "root";
        String password = "Ys15005113872";
        try {
            //使用目标服务器机上的用户名和密码登陆
            SSHHelper helper = new SSHHelper(host, port, user, password);
            String command = "docker images ";
            try {
                SSHResInfo resInfo = helper.sendCmd(command);
                System.out.println(resInfo.toString());
                //System.out.println(helper.deleteRemoteFIleOrDir(command));
                //System.out.println(helper.detectedFileExist(command));
                helper.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (JSchException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
