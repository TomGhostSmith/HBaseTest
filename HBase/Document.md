# HBase Lab

### 操作实体类

#### Get

```java
public class Get
{
    // 行键
    Key key;  
    
    // 列簇与列限定符，Column类中包含了两者的信息
    List<Column> columns;  
    
    // 限定的时间戳。如果没有限定，就认为选择最近的时间
    // 如果有限定，就返回在该限定时间前最后一次写入的结果
    Long timestamp;  
}
```

#### Put

```java
public class Put
{
    // 行键
    Key key; 
    
    // 修改的列-值对
    // 其中每个列中包含了列簇和列限定符两者的信息，值为Object对象
    List<Value> values;  
}

```

#### Result

```java
public class Result
{
    Key key;  // 行键
    List<Value> values;  // 结果的键值对，仅包含Get请求中对应的列
}
```

### 接口设计

#### Client

```Java
public interface Client
{
    // 在Client中，其他方法可以直接调用下列两个方法向HMaster发送请求，并获得结果
    Result sendGetRequest(String tableName, Get get);
    void sendPutRequest(String tableName, Put put);
}
```

#### HMaster

```java
public interface HMaster
{
    // 对外接口
    
    // 考虑到数据库存在添加Table的可能，故在HMaster中保留了相应方法
    // 但该方法需要通过其他途径调用
    // （例如HBase内部调用，或仅针对特定Client开放调用权限）
    void addTable(String name);
    
    // 考虑到查询的语义往往是在特定Table的背景下发生
    // 故不在下列两个方法的get和put参数中设置table相应信息
    // 而是将tableName作为一个单独变量进行传输
    // 每个方法均需要首先确认Table，然后转发给对应HRegionServer
	Result search(String tableName, Get get);   // 用于在HBase系统中查询数据
    void modify(String tableName, Put put);   // 用于在HBase系统中修改或新增数据
    
    
    // 对内接口
    // 根据key值返回对应的RegionServer编号，以便将请求转发给对应Server
    int findHRegion(Key key);
    
    // 根据当前各HRegionServer的存储情况进行调整
    void updateHRegion();  
}
```

#### HRegionServer

```java
public interface HRegionServer
{
    // 对外接口
    // 在当前Region中查询数据
    // 首先再次检查Key的范围，然后检查列簇与列标识符是否存在
    // 随后在对应Store中依次查找MemStore和StoreFile
    Result search(Get get);  
    
    // 在当前Region中修改或新增数据
    // 首先再次检查Key范围，然后检查是否需要新增列簇与列标识符
    // 随后将内容写入MemStore和HLog中
    void modify(Put put);  
    
    // 对内接口
    // 考虑到当前HRegionServer负责的Key范围可能会发生调整，
    // 仍然需要检查请求的Key是否在范围内
    // 如果不在范围内，该函数的调用方可以直接返回错误信息给HMaster
    boolean contain(Key key);
    
    // 当MemStore达到一定数量时，调用dump()将数据写入StoreFile中，并对HLog进行更新
    void dump();
}
```

#### ZooKeeper

```java
public interface ZooKeeperCaller
{
    // 该部分为ZooKeeper中需要处理的业务
    // 根据名称存储StoreFile文件，其名称可以是Region编号+列簇名称+对应Key的范围
    void saveStoreFile(String fileName, byte[] file);  
    // 根据名称获取响应的StoreFile文件
    byte[] loadStoreFile(String fileName);  
}
```

### 组员分工

吴伟杰、廖妍昕：类设计与接口设计

龚诗涛、张栋梁：原型代码实现与简单测试