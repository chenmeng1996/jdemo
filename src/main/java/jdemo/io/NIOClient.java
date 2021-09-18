package jdemo.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;


public class NIOClient {
    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024); //缓冲区
    private Selector selector = null; //选择器
    private boolean hasNickname = false;
    private String nickName = "user";
    private final String TAG = "welcome";
    private final String SEPECTOR = "####";

    public static void main(String[] args) throws IOException {
        new NIOClient().start();
    }

    public void start() throws IOException {
        //TCP连接服务端监听的端口
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 10000));
        socketChannel.configureBlocking(false);
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("客户端已经启动");

        //开启线程，接收服务端消息
        receiveMsgThread();

        //主线程负责发送消息
        Scanner scanner = new Scanner(System.in);
        byteBuffer = ByteBuffer.allocate(1024);
        while (true) {
            String input = scanner.next();
            if (hasNickname) {
                //单聊格式：消息@对方昵称
                if (input.contains("@")) {
                    input = input.replace("@", SEPECTOR);
                }
                writeMesage(nickName + SEPECTOR + input, socketChannel);
            } else {
                //设置昵称
                writeMesage(input, socketChannel);
                nickName = input;
                hasNickname = true;
            }
        }
    }

    private void writeMesage(String str, SocketChannel socketChannel) throws IOException {
        byteBuffer.clear();
        byteBuffer.put(str.getBytes(StandardCharsets.UTF_8));
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
    }

    private String readMessage(SocketChannel socketChannel) throws IOException {
        byteBuffer.clear();
        int len = 0;
        StringBuilder builder = new StringBuilder();
        while ((len = socketChannel.read(byteBuffer)) > 0) {
            byteBuffer.flip();
            builder.append(new String(byteBuffer.array(), 0, len, StandardCharsets.UTF_8));
        }
        return builder.toString();
    }

    //启动一个线程，用来读取服务端传来的消息
    private void receiveMsgThread() {
        new Thread(() -> {
            SocketChannel socketChannel = null;

            while (true) {
                try {
                    selector.select();
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        if (selectionKey.isReadable()) {
                            socketChannel = (SocketChannel) selectionKey.channel();
                            String msg = readMessage(socketChannel);
                            System.out.println(msg);
                            //如果返回约定的标志，说明昵称设置成功
//                            if (msg.contains(TAG)) {
//                                hasNickname = true;
//                                nickName = msg.substring(TAG.length() + 1);
//                            }
                        }
                    }

                    selector.selectedKeys().clear();
                } catch (IOException e) {
                    if (socketChannel != null) {
                        try {
                            socketChannel.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    e.printStackTrace();
                }
            }

        }).start();
    }
}
