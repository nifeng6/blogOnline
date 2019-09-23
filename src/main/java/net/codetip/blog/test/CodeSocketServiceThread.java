package net.codetip.blog.test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class CodeSocketServiceThread extends Thread {




    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9008);
            while (true) {
                Socket accept = serverSocket.accept();
                System.out.println("接收到一个数据来自：" + accept.getInetAddress());
                new CodeSocketContent(accept).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) serverSocket.close();
            } catch (Exception e) {
            }

        }
    }
}

class CodeSocketContent extends Thread {
    private Socket accept;


    CodeSocketContent(Socket accept) {
        this.accept = accept;

    }

    @Override
    public void run() { //运行且编译的程序
        InputStream inputStream = null;
        OutputStream outputStream = null;
        StringBuilder sb = new StringBuilder();
        try {
            inputStream = accept.getInputStream();
            outputStream = accept.getOutputStream();

            byte[] bytes = new byte[1024];
            int len = -1;

            String fileName = UUID.randomUUID() + ".py";

            len = inputStream.read(bytes);

            /* 写入Txt文件 */
            File writename = new File("./" + fileName); // 相对路径，如果没有则要建立一个新的output。txt文件
            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(new String(bytes, 0, len)); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件

            Runtime runtime = Runtime.getRuntime();
            String command = "python3 " + "./" + fileName;
            Process process = runtime.exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line = null;


            if (error.readLine() != null) {//如果报错的内容为空的时候
                while ((line = error.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } else {//如果没有报错，显示正常代码
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
            }

            br.close();
            System.out.println(sb.toString());

            outputStream.write(sb.toString().getBytes());
            writename.delete();//运行结束后删除文件

        } catch (IOException e) {
            sb.append("e.getSuppressed()");
            e.printStackTrace();
        } finally {
            if (accept != null) {
                try {
                    accept.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

}

