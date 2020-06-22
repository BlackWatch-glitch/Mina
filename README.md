Mina通信框架的了解
基础的mina通信入门
长连接和短连接
自定义编解码器的编写
自定义MyHandler
编解码工厂



协议栈：TCP/UDP
TCP：三次握手，四次挥手以及滑动窗口协议。
UDP：如何封装报文

Mina：apache开发的一个开源的网络通信框架，基于我们javaNIO来实现。

jar包

编码和解码
应用程序                                                网络

Java对象或则基本数据类型                二进制

编码成二进制                                            解码成Java对象或则基本的数据类型


1、NIOSocket
2、设置编码解码过滤器
3、设置一些session属性
4、绑定一个端口


Server和Client进行通信
MianServer.java  Myhandler.java MinaClient.java MyClientHandler.java


1、mina在应用程序中处于什么样的地位
    
主要屏蔽了网络通信的一些细节，对socket进行封装，并且是NIO的一个实现架构，可以帮助我们快速的开发网络通信，常常用户游戏的开发，中间件等服务端程序。

2、IOService接口
用于描述客户端和服务端接口，
其子类是connector和Acceptor，分别用于描述我们的客户端和服务端。
IOprocesser多线程环境来处理我们的连接请求流程。
ioFilter提供数据的过滤工作：包括编解码、日志等信息的过滤。
Handler就是我们的业务对象，自定义的handler需要实现IOHandlerAcceptor。


3、大致类图结构
                                                                        IOService
                            
                                IOConnector（客户端）                         IOAccept（服务端）
                

                                NIOSocketConnector                               NIOSocketAccept

        IOsession:描述的是客户端和服务端连接的描述，常常用户接受和发送数据



1.     IOConnector —> IOProcessor —> IOFilter —> Handler
               IOAccptor —> IOProcessor —> IOFilter —> Handler

    IOConnector, IOAccptor, NIOSockerConnector, NIOSocketAcceptor, IOSession, IOFiter,





长连接
1、长连接：通信双方长期的保持一个连接状态不断开，比如腾讯qq,当我们登录QQ的时候，我们就去连接我们腾讯服务器，一旦建立连接后，就不断开，除非发生异常，这样方式就是长连接，对于长连接比较耗费IO资源


2、短连接
通信双方不上保持一个长期的连接状态，比如HTTP协议，当客户端发起http请求，服务器处理http请求，当服务器处理完成后，返回客户端数据后，就断开连接，对于下次的连接请求需要重新发起。这种方式是我们长使用的方式。

长连接变短连接
//发送数据完数据进行关闭
     @Override
     public void messageSent(IoSession session, Object message) throws Exception {
         System.out.println("messageSent");
         session.close();    //长连接变成短连接
     }



1、IOService:实现了对网络通信的客户端与服务端之间的抽象，
用于描述客户端的子接口IOConnector，
用于描述服务端的子接口IOAcceptor

2、IOService的作用
IOService可以管理我们网络通信的客户端和服务端，并且可以管理连接双方的会话session，同样可以添加过滤器。


3、IOService类结构

通过扩展子接口和抽象的子类到达扩展的目的。
        
                             IOService                                     


IOAcceptor                                   IOConnector                    

                        abstratcIOService


abstractIOAcceptor                       abstractIOConncetor 


NioSocketAcceptor                        NioSocketConnector


4、相关API


IOService常用API：定义一些抽象的接口，可以获得我们的过滤器
1）getFilterChain()获得获得过滤器
2）setHandler(IoHandler handler)设置我们真正业务handler
3）getSessionConfig()得到会话的配置信息
4）dispose() 在我们完成关闭连接的时候 所调用的方法


IOConnector
1、connector(SocketAddress remoteAddress)主要用户发起一个连接请求
2、setConnectTimeout(int connectTimeout)连接超时的设置


IOAcceptor
1、bind(SocketAddress localAddress)绑定端口
2、getLoaclAddress() 获得本地IP地址


NioSocketAcceptor  API
1）accept(IoProcessor<NioSession> processor, ServerSocketChannel handle) 接受一个连接

2）open(SocketAddress localAddress) 打开一个socketchannel


3)  select() 获得我们的选择器

                    
 NioSocketConnector API

1）connect(SocketChannel handle, SocketAddress remoteAddress)用于描述连接请求

2）register(SocketChannel handle, AbstractPollingIoConnector.ConnectionRequest request) 注册我们的IO事件

3)  select(int timeout)返回选择器



IOFilter接口
1、IOFilter：对应用程序和网络这块的传输，就是二进制数据和对象之间的相互转化，有相应的编码和解码器。这也是我们过滤器一种，我们过滤器还可以做日志，消息确认等功能。


2.IOFilter类
是在应用层和业务层之间过滤层

3、完成自定义过滤器
就是在往handler处理之前，需要调用相应的过滤器进行过滤
Client：业务handler之前会调用我们的过滤器

Server：同样在我们接收到数据的时候，和发送数据的时候也调用了过滤器，然后才交给我们的handler。





IoSession接口

1、IOSession：主要描述网络通信双方所建立的连接之间描述。

        IOSession的作用：可以完成对于连接的一些管理，可以发送或则读取数据，并且可以设置我们会话的上下文信息。

2、IOSessionConfig：提供我们对连接的配置信息的描述，比如读缓冲区的设置等等
IOSessionConfig:设置读写缓冲区的一些信息，读和写的空闲时间，以及设置读写超时信息。

3、API
IOSession：
1）：getAttribute(Object key)根据key获得设置的上下文属性
2）：setAttribute(Object key, Object value)设置上下文属性
3）：removeAttribute(Object key)    删除上下文属性
4）：write(Object message)    发送数据
5）：read()    读取数据



IOSessionConfig:
1)、getBothIdleTime()获得读写通用的空闲时间
2)、setIdleTime(IdleStatus status, int idleTime)设置我们的读或写的空闲时间
3)、setReadBufferSize(int readBufferSize)    设置读缓冲区大小
4)、setWriteTimeout(int writeTimeout)    设置我们的写超时时间


I/O Processor
1、Processor：是以NIO为基础实现的以多线程的方式来完成我们读写工作

Processor的作用：是为我们的filter读写原始数据的多线程环境，如果mina不去实现的话，我们自己来实现NIO的话 需要自己写一个非阻塞读写的多线程的环境。

配置Processor的多线程环境：
1）通过NioSocketAcceptor(int processorCount)构造函数可以指定多线程的个数。
2）通过NioSocketConnector(int processorCount)构造函数也可以指定多线程的个数



IOBuffer
IOBuffer:基于JavaNIO中的ByteBuffer做了封装，用于操作缓冲区中的数据，包括基本数据类型以及字节数组和一些对象。其本质就是一个可动态扩展的byte数组。

IOBuffer索引说明




2、IoBuffer的索引属性
Capacity：代表当前缓冲区的大小
Position：理解成当前读写位置，也可以理解成下一个可读数据单位的位置。Position<=Capacity的时候可以完成数据的读写操作。
Limit：就是下一个不可以被读写缓冲区单元的位置。Limit<=Capacity


3.IOBuffer常用API

1)static allocate(int capacity) 已指定的大小开辟缓冲区的空间
2）setAutoExpand(boolean autoExpand)可以设置是否支持动态的扩展
3）putShort(int index, short value) 
     putString(CharSequence val, CharsetEncoder encoder)
     putInt(int value)
等待方法实现让缓冲区中放入数据。 PutXXX()
4）flip()就是让我们的limit=position,position=0;为我们读取缓冲区的数据做好准备，因为有时候，limit！=position，一般在发送数据之前调用
5）hasRemaining() 缓冲区中是否有数据：boolean是关于position<=limit=true,否则返回false
6）remaining()；返回的是缓冲区中可读数据的大小，limit-position的值
7）reset(）：实现清空数据
Clear()：实现数据的覆盖，position=0重新开始读我们缓冲区的数据





自定义编解码
   
1、自定义的编解码工厂：要实现编解码工厂就要实现 ProtocolCodecFilter这个接口
2、实现自定义编解码器：
    1）实现自定义解码器：实现ProtocolDecoder接口
    2）实现自定义编码器：实现ProtocolEecoder接口

实现定义的编码需要
1. 实现ProtocolCodecFilter这个接口
2. 实现自定义的编解码器：ProtocolDecoder和ProtocolEecoder
3. 然后就可以根据我们的自定义编解码工厂获得我们的编解码对象
3、为什么使用自定义的编码器，因为工作中往往不是通过一个字符串就可以传输所有的信息。我们传输的是自定义的协议包。并且能在应用程序和网络通信中存在对象和二进制之间转换关系。所以需要结合业务编写自定义的编解码器。
4、常用的自定义协议的方法
1）定长的方式：Aa,bb,cc,ok,no等这样的通信方式。
2）定界符helloworld | wacthman | …. | … 通过特殊的符号来区别信息。这样方式会出现粘包，半包等现象。
3）自定义协议包
    包头
    包体
        包头：数据包的版本号，以及整个数据包（包头+包体）长度
        包体：实际数据。
1、自定义协议：  包头(length, flag)flag版本信息
                        包体(content
通过客户端不断的发送指定数目的自定义数据包，然后在服务端解析。在这个过程中要解决半包的问题。










