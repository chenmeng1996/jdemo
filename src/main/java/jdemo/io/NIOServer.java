package jdemo.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class NIOServer {
    private Selector selector;
    private final ByteBuffer buffer = ByteBuffer.allocate(1024);
    private final Map<String, String> clientMap = new HashMap<>();
    private final List<SocketChannel> socketChannelList = new ArrayList<>();
    private final String TAG = "welcome";
    private final String SEPECTOR = "####";

    public static void main(String[] args) throws IOException {
        //一个线程，处理多个请求
        new NIOServer().start();
    }

    public void start() throws IOException {
        //打开服务器套接字通道
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //服务器配置为非阻塞IO
        ssc.configureBlocking(false);
        //绑定本地端口
        ssc.bind(new InetSocketAddress(10000));
        selector = Selector.open();
        //ServerSocketChannel注册到Selector，准备连接
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        //线程没有中断，就一直运行
        while (!Thread.currentThread().isInterrupted()) {
            //select()方法返回的值表示有多少个 Channel 可操作
            //select方法内部会一直循环询问所有注册的channel是否有满足注册事件的（即满足io条件）
            //所以调用select方法会引起阻塞，直到有一个channel已经准备好io
            int validChannel = selector.select();

            //SelectionKey是一个channel注册到selector的凭证
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = keys.iterator();
            //开始遍历所有准备好io的客户端连接，轮流处理
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();
                handle(key);
            }
            selector.selectedKeys().clear();
        }
    }

    private void handle(SelectionKey key) throws IOException {
        //从select中删除处理过的该连接
        //连接是否有效
        if (!key.isValid()) {
//            disconnet(key);
        }
        //第一步，连接
        if (key.isAcceptable()) {
            accept(key);
        }
        //第二步，读数据
        if (key.isReadable()) {
            System.out.println("可以读数据");
            read(key);
        }
        //第三步，写数据
//        if (key.isWritable()) {
//            System.out.println("可以写数据");
//        }
    }

    private void accept(SelectionKey key) throws IOException {
        //从SelectionKey中取出对应的Channel
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        //接收客户端的连接
        SocketChannel clientChannel = ssc.accept();
        //新的channel设置成非阻塞IO
        clientChannel.configureBlocking(false);
        //该channel在select上注册读事件，下一步准备读连接传来的数据
        clientChannel.register(selector, SelectionKey.OP_READ);
        System.out.println(clientChannel.getRemoteAddress().toString() + "已经上线了");
        writeMsg("请输入昵称,进行身份确认后聊天：", clientChannel);
    }

    public void read(SelectionKey key) throws IOException {
        //获取channel
        SocketChannel sc = (SocketChannel) key.channel();
        //接收消息
        String msg = readMsg(sc);
        //处理消息
        if (msg == null || "".equals(msg)) {
            String addr = sc.getRemoteAddress().toString();
            String nickName = clientMap.get(addr);
            System.out.println(nickName + "断开连接");
            clientMap.remove(addr);
            socketChannelList.remove(sc);
            sc.close();
            broadcastMsg(nickName + "已经退出群聊，" + "当前在线人数：" + clientMap.size());
            return;
        }
        String[] msgArray = msg.split(SEPECTOR);
        System.out.println(Arrays.toString(msgArray));
        if (msgArray.length == 1) {
            if (clientMap.containsValue(msgArray[0])) {
                writeMsg("昵称已经存在，请重新输入", sc);
            } else {
                clientMap.put(sc.getRemoteAddress().toString(), msgArray[0]);
                socketChannelList.add(sc);
                writeMsg(TAG + "," + msgArray[0], sc);
                broadcastMsg(msgArray[0] + "加入了群聊");
            }
        } else if (msgArray.length == 2) {
            broadcastMsg(msgArray[0] + "：【群消息】" + msgArray[1]);
        } else if (msgArray.length == 3) {
            p2pMsg(msgArray[0] + "：【私聊】" + msgArray[1], msgArray[2], sc);
        }
    }

    public void disconnet(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        //从select中删除该channel
        key.channel();
        //关闭channel
        sc.close();
    }

    public void broadcastMsg(String msg) throws IOException {
        for (SelectionKey key : selector.keys()) {
            Channel channel = key.channel();
            if (channel instanceof SocketChannel && channel.isOpen()) {
                writeMsg(msg, (SocketChannel) channel);
            }
        }
    }

    public void p2pMsg(String msg, String name, SocketChannel sourceChannel) throws IOException {
        boolean flag = false; //记录是否成功发送给指定用户
        for (SelectionKey sk : selector.keys()) {
            Channel channel = sk.channel();
            if (channel instanceof SocketChannel && channel.isOpen()) {
                SocketChannel targetChannel = (SocketChannel) channel;
                String nickName = clientMap.get(targetChannel.getRemoteAddress().toString());
                if (nickName.equals(name)) {
                    writeMsg(msg, targetChannel);
                    flag = true;
                    break;
                }
            }
        }
        if (!flag) {
            writeMsg("该用户不存在", sourceChannel);
        } else {
            writeMsg(msg+"---------【私聊发送给：" + name + "　　　状态：成功】", sourceChannel);
        }
    }

    public void writeMsg(String str, SocketChannel socketChannel) throws IOException {
        buffer.clear();
        buffer.put(str.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        socketChannel.write(buffer);
    }

    public String readMsg(SocketChannel socketChannel) throws IOException {
        buffer.clear();
        int len = 0;
        StringBuilder sb = new StringBuilder();
        while ((len = socketChannel.read(buffer)) > 0) {
            buffer.flip();
            sb.append(new String(buffer.array(), 0, len, StandardCharsets.UTF_8));
        }
        return sb.toString();
    }
}
