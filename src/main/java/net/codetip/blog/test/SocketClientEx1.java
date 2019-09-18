package net.codetip.blog.test;

import java.io.*;
import java.net.*;

public class SocketClientEx1 {
    public static void main(String[] args) {
        System.out.println("Client");
        try {
            Socket clientSocket = new Socket("localhost", 9000);
            System.out.println("Client1:" + clientSocket);
            DataInputStream dataIS = new DataInputStream(clientSocket.getInputStream());
            InputStreamReader inSR = new InputStreamReader(dataIS, "UTF-8");
            BufferedReader br = new BufferedReader(inSR);
            DataOutputStream dataOS = new DataOutputStream(clientSocket.getOutputStream());
            OutputStreamWriter outSW = new OutputStreamWriter(dataOS, "UTF-8");
            BufferedWriter bw = new BufferedWriter(outSW);
            //输入信息
            byte bytes[] = new byte[100];
            while(true) {
                System.out.println("----------------------------------");
                System.in.read(bytes);
                String str = new String(bytes);
                str = str.trim();
                if (str == "exit") {
                    break;
                }

                //发送数据
                bw.write(str + "\r\n");		//加上分行符，以便服务器按行读取
                bw.flush();

                //接收数据
                while((str = br.readLine()) != null) {
                    str = str.trim();
                    System.out.println("服务器回复：" + str);
                    break;
                }
            }
            inSR.close();
            dataIS.close();
            dataOS.close();
            clientSocket.close();
        } catch(UnknownHostException uhe) {
            System.out.println("Error:" + uhe.getMessage());
        } catch(ConnectException ce) {
            System.out.println("Error:" + ce.getMessage());
        } catch(IOException ioe) {
            System.out.println("Error:" + ioe.getMessage());
        } finally {
        }
    }
}
