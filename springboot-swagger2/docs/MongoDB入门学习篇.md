# MongoDB入门学习篇

### mac上Mongod服务搭建

1、brew install mongo

2、一般brew安装的文件都在【/usr/local/Cellar】文件夹目录下。找到该文件夹下的mongdb文件夹

​	注意：可以通过【brew list [软件名称]】罗列安装包的位置。如下示例

```bash
zhukais-MacBook-Pro:4.0.5 zhukai$ brew list mongo
/usr/local/Cellar/mongodb/4.0.5/.bottle/etc/mongod.conf
/usr/local/Cellar/mongodb/4.0.5/bin/bsondump
/usr/local/Cellar/mongodb/4.0.5/bin/install_compass
/usr/local/Cellar/mongodb/4.0.5/bin/mongo
/usr/local/Cellar/mongodb/4.0.5/bin/mongod
/usr/local/Cellar/mongodb/4.0.5/bin/mongodump
/usr/local/Cellar/mongodb/4.0.5/bin/mongoexport
/usr/local/Cellar/mongodb/4.0.5/bin/mongofiles
/usr/local/Cellar/mongodb/4.0.5/bin/mongoimport
/usr/local/Cellar/mongodb/4.0.5/bin/mongoreplay
/usr/local/Cellar/mongodb/4.0.5/bin/mongorestore
/usr/local/Cellar/mongodb/4.0.5/bin/mongos
/usr/local/Cellar/mongodb/4.0.5/bin/mongostat
/usr/local/Cellar/mongodb/4.0.5/bin/mongotop
/usr/local/Cellar/mongodb/4.0.5/conf/mongod.conf
/usr/local/Cellar/mongodb/4.0.5/data/diagnostic.data/ (4 files)
/usr/local/Cellar/mongodb/4.0.5/data/journal/ (3 files)
/usr/local/Cellar/mongodb/4.0.5/data/ (21 files)
/usr/local/Cellar/mongodb/4.0.5/homebrew.mxcl.mongodb.plist
/usr/local/Cellar/mongodb/4.0.5/log/ (4 files)
```

3、进入到目录【/usr/local/Cellar/mongodb/4.0.5/】后，然后分别创建如下文件夹

log：保存日志

data：保存数据

conf：保存配置文件

4、然后进入conf文件夹，创建mongo.conf文件，文件内容如下

```
port = 12345
dbpath = data
logpath = log/mongod.log
fork = true
```

5、**服务启动命令**：`mongod -f conf/mongo.conf`

```bash
# 当前目录
zhukais-MacBook-Pro:4.0.5 zhukai$ pwd
/usr/local/Cellar/mongodb/4.0.5
# 示例如下，表示启动成功
zhukais-MacBook-Pro:4.0.5 zhukai$ bin/mongod -f conf/mongod.conf
about to fork child process, waiting until server is ready for connections.
forked process: 25851
child process started successfully, parent exiting
zhukais-MacBook-Pro:4.0.5 zhukai$
```

6、**客户端连接**：mongo localhost:12345/test

提示：可通过【mongo --help】查看帮助



### 常用命令 + 增、删、改、查

1. 查看数据库: `show dbs`

2. 进入数据库：`use zk` ，其中zk就是数据库的名称

   注意：mongo数据库可以直接use，即使不存在，也会在适当时候创建

3. 查看表：`show tables`

4. **增**：`db.user.insert({name:zhukai, age:28})`

   表示往user表中新增一条数据

5. **查**：

   1. 查所有：`db.user.find()`
   2. 根据条件查：`db.user.find(name:zhukai)`

6. **改**

   1. `db.user.update({name:zhukai}, {$set:{age:20}})`

      将第一条name=zhukai的记录的age值更新为20，其他字段值不便。即使存在多个name=zhukai的记录，也只会更新第一条

   2. `db.user.update({name:zhukai}, {age:20})`

      将第一条name=zhukai的记录的age值更新为20，**且清空所有其他字段值**。即使存在多个name=zhukai的记录，也只会更新第一条

   3. `db.user.update({name:zhukai}, {$set:{age:20}}, false, true)`

      将所有name=zhukai的记录的age值更新为20。

7. **删**：`db.user.remove({name:zhukai})`

   默认删除所有name=zhukai的记录



### 索引

- 全文索引

  全文索引的创建

  ```bash
  一个字段：db.集合名.ensureIndex({key:"text"})
  多个字段：db.集合名.ensureIndex({key_1:"text",key_2:"text"})
  所有字段：db.集合名.ensureIndex({"$**":"text"})
  ```

  全文索引的查找

  ```bash
  # 给article创建全文索引
  db.imooc_2.ensureIndex({"article":"text"})
  
  # 查找article字段包含关键词coffee的记录
  db.article.find({$text:{$search:"coffee"}})
  
  # 查找article字段包含关键词aa，或者bb，或者cc的记录
  db.article.find({$text:{$search:"aa bb cc"}})
  
  # 查找article字段包含关键词aa或者bb，且不包含cc的记录
  db.article.find({$text:{$search:"aa bb -cc"}})
  
  # 查找article字段同时包含关键词aa和bb和cc的记录
  db.article.find({$text:{$search:"\"aa\" \"bb\" \"cc\""}})
  ```

  相似度搜索

  ```bash
  # {score:{$meta:"textScore"}}写在查询条件后面可以返回返回结果的相似度。与sort一起使用，可以达到很好的实用效果。
  # 比如百度搜索时，键入关键词之后，返回结果根据匹配度依次排列，第一条是最匹配的
  db.imooc_2.find({$text:{$search:"aa bb"}}, {score:{$meta:"textScore"}}).sort({score:{$meta:"textScore"}})
  ```

- 创建索引 - 名字，name指定

  如：`db.imooc_2.ensureIndex({x:1,y:1,z:1,m:1},{"name":"normal_index"})`

  删除索引：`db.imooc_2.dropIndex("normal_index")`

- 创建索引 - 唯一行，unique指定

  格式：`db.collection.ensureIndex({},{unique:true/false})`

- 创建索引 - 稀疏性，sparse指定

  格式：`db.collection.ensureIndex({},{sparse:true/false})`



### 地理位置索引查询示例

- 给集合location的w字段创建地理位置索引之“**2d索引**”

  ```bash
  > db.location.ensureIndex({"w":"2d"})
  ```

- 制造数据

  ```bash
  > db.location.insert({w:[1,1]})
  > db.location.insert({w:[1,2]})
  > db.location.insert({w:[3,2]})
  > db.location.remove({w:[180,80]})
  ```

- $near查询

  ```bash
  # 会展示最多100条的数据
  > db.location.find({w: {$near:[1,1]}})
  { "_id" : ObjectId("5c45dec2b122bf593036fcce"), "w" : [ 1, 1 ] }
  { "_id" : ObjectId("5c45dec6b122bf593036fccf"), "w" : [ 1, 2 ] }
  { "_id" : ObjectId("5c45decab122bf593036fcd0"), "w" : [ 3, 2 ] }
  { "_id" : ObjectId("5c45df77b122bf593036fcd4"), "w" : [ 180, 80 ] }
  
  # 可指定最远距离
  > db.location.find({w: {$near:[1,1], $maxDistance: 10}})
  { "_id" : ObjectId("5c45dec2b122bf593036fcce"), "w" : [ 1, 1 ] }
  { "_id" : ObjectId("5c45dec6b122bf593036fccf"), "w" : [ 1, 2 ] }
  { "_id" : ObjectId("5c45decab122bf593036fcd0"), "w" : [ 3, 2 ] }
  ```

- $geoWithin查询

  ```bash
  # $box查找矩形范围内的店
  > db.location.find({w : {$geoWithin : {$box : [[0,0], [3,3]]}}})
  { "_id" : ObjectId("5c45dec2b122bf593036fcce"), "w" : [ 1, 1 ] }
  { "_id" : ObjectId("5c45dec6b122bf593036fccf"), "w" : [ 1, 2 ] }
  { "_id" : ObjectId("5c45decab122bf593036fcd0"), "w" : [ 3, 2 ] }
  
  # $center查找圆形范围内的店
  > db.location.find({w : {$geoWithin : {$center : [[0,0], 5]}}})
  { "_id" : ObjectId("5c45dec2b122bf593036fcce"), "w" : [ 1, 1 ] }
  { "_id" : ObjectId("5c45dec6b122bf593036fccf"), "w" : [ 1, 2 ] }
  { "_id" : ObjectId("5c45decab122bf593036fcd0"), "w" : [ 3, 2 ] }
  
  # $polygon查找多边形范围内的点
  > db.location.find({w : {$geoWithin : {$polygon : [[0,0], [0,1], [2,5], [6,1]]}}})
  { "_id" : ObjectId("5c45dec2b122bf593036fcce"), "w" : [ 1, 1 ] }
  { "_id" : ObjectId("5c45dec6b122bf593036fccf"), "w" : [ 1, 2 ] }
  { "_id" : ObjectId("5c45decab122bf593036fcd0"), "w" : [ 3, 2 ] }
  ```

- runCommand

  ```bash
  > db.runCommand({geoNear:"location", near : [1,2], maxDistance : 10, num : 2})
  {
  	"results" : [
  		{
  			"dis" : 0,
  			"obj" : {
  				"_id" : ObjectId("5c45dec6b122bf593036fccf"),
  				"w" : [
  					1,
  					2
  				]
  			}
  		},
  		{
  			"dis" : 1,
  			"obj" : {
  				"_id" : ObjectId("5c45dec2b122bf593036fcce"),
  				"w" : [
  					1,
  					1
  				]
  			}
  		}
  	],
  	"stats" : {
  		"nscanned" : 32,
  		"objectsLoaded" : 2,
  		"avgDistance" : 0.5,
  		"maxDistance" : 1,
  		"time" : 1514
  	},
  	"ok" : 1
  }
  >
  ```


### 复制集创建

1. 创建3个文件夹：

   ```
   mkdir /usr/local/Cellar/mongodb/4.0.5/replSet1/
   mkdir /usr/local/Cellar/mongodb/4.0.5/replSet2/
   mkdir /usr/local/Cellar/mongodb/4.0.5/replSet3/
   ```

   在replSet1、replSet2、replSet3文件夹下都分别创建log，data，conf文件夹。

   **解释**：

   log：存放为各个复制集下的日志文件

   data：存放数据

   conf：新建一个mongo.conf文件，用于启动配置

2. 各个mongo.conf配置文件如下：

   ```conf
   # /usr/local/Cellar/mongodb/4.0.5/replSet1/conf/mongo.conf配置如下：
   port = 12345
   logpath = /usr/local/Cellar/mongodb/4.0.5/replSet1/log/mongo.log
   dbpath = /usr/local/Cellar/mongodb/4.0.5/replSet1/data/
   fork = true
   replSet = rs2
   
   # /usr/local/Cellar/mongodb/4.0.5/replSet2/conf/mongo.conf配置如下：
   port = 12346
   logpath = /usr/local/Cellar/mongodb/4.0.5/replSet2/log/mongo.log
   dbpath = /usr/local/Cellar/mongodb/4.0.5/replSet2/data/
   fork = true
   replSet = rs2
   
   # /usr/local/Cellar/mongodb/4.0.5/replSet1/conf/mongo.conf配置如下：
   port = 12347
   logpath = /usr/local/Cellar/mongodb/4.0.5/replSet3/log/mongo.log
   dbpath = /usr/local/Cellar/mongodb/4.0.5/replSet3/data/
   fork = true
   replSet = rs2
   ```

3. 启动其中任意一个服务

4. mongo客户端连接，切换到admin数据库。执行如下命令：

   ```bash
   rs.initiate({
     _id: 'rs2',
     members: [
       {
         _id: 0,
         host: 'localhost: 12345'
       },
       {
         _id: 1,
         host: 'localhost: 12346'
       },
       {
         _id: 2,
         host: 'localhost: 12347'
       }
     ]
   })
   ```

5. 复制集常用方法总结

   **rs.initiate()**：复制集初始化，例如：rs.initiate({_id:'repl1',members:[{_id:1,host:'192.168.168.129:27017'}]}) 
   **rs.reconfig()**：重新加载配置文件，例如：

   rs.reconfig({_id:'repl1',members:[{_id:1,host:'192.168.168.129:27017'}]},{force:true})当只剩下一个secondary节点时，复制集变得不可用，则可以指定force属性强制将节点变成primary，然后再添加secondary节点
   **rs.status()**：查看复制集状态 
   **db.printSlaveReplicationInfo()**：查看复制情况 
   **rs.conf()/rs.config()**：查看复制集配置 
   **rs.slaveOk()**：在当前连接让secondary可以提供读操作 
   **rs.add()**：增加复制集节点，例如：

   rs.add('192.168.168.130:27017')
   rs.add({"_id":3,"host":"192.168.168.130:27017","priority":0,"hidden":true})指定hidden属性添加备份节点
   rs.add({"_id":3,"host":"192.168.168.130:27017","priority":0,"slaveDelay":60})指定slaveDelay属性添加延迟节点
   priority：是优先级，默认为1，如果想手动指定某个节点为primary节点，则把对应节点的priority属性设置为所有节点中最大的一个即可
   **rs.remove()**：删除复制集节点，例如：rs.remove('192.168.168.130:27017') 
   **rs.addArb()**：添加仲裁节点，例如：

   rs.addArb('192.168.168.131:27017')或者rs.add({"_id":3,"host":"192.168.168.130:27017","arbiterOnly":true})，仲裁节点，只参与投票，不接收数据

### readConcern、readPreference、writeConcern 原理解析

1. **writeConcern**

   **MongoDB支持的WriteConncern选项如下**

   1. w: <number>，数据写入到number个节点才向用客户端确认
      - {w: 0} 对客户端的写入不需要发送任何确认，适用于性能要求高，但不关注正确性的场景
      - {w: 1} 默认的writeConcern，数据写入到Primary就向客户端发送确认
      - {w: "majority"} 数据写入到副本集大多数成员后向客户端发送确认，适用于对数据安全性要求比较高的场景，该选项会降低写入性能
   2. j: <boolean> ，写入操作的journal持久化后才向客户端确认
      - 默认为"{j: false}，如果要求Primary写入持久化了才向客户端确认，则指定该选项为true
   3. wtimeout: <millseconds>，写入超时时间，仅w的值大于1时有效。
      - 当指定{w: }时，数据需要成功写入number个节点才算成功，如果写入过程中有节点故障，可能导致这个条件一直不能满足，从而一直不能向客户端发送确认结果，针对这种情况，客户端可设置wtimeout选项来指定超时时间，当写入过程持续超过该时间仍未结束，则认为写入失败。

   **{w : "majority"}解析**

   {w: 1}、{j: true}等writeConcern选项很好理解，Primary等待条件满足发送确认；但{w: "majority"}则相对复杂些，需要确认数据成功写入到大多数节点才算成功，而MongoDB的复制是通过Secondary不断拉取oplog并重放来实现的，并不是Primary主动将写入同步给Secondary，那么Primary是如何确认数据已成功写入到大多数节点的？

   1. Client向Primary发起请求，指定writeConcern为{w: "majority"}，Primary收到请求，本地写入并记录写请求到oplog，然后等待大多数节点都同步了这条/批oplog（Secondary应用完oplog会向主报告最新进度)。
   2. Secondary拉取到Primary上新写入的oplog，本地重放并记录oplog。为了让Secondary能在第一时间内拉取到主上的oplog，find命令支持一个[awaitData的选项](https://docs.mongodb.com/manual/reference/command/find/#dbcmd.find)，当find没有任何符合条件的文档时，并不立即返回，而是等待最多maxTimeMS(默认为2s)时间看是否有新的符合条件的数据，如果有就返回；所以当新写入oplog时，备立马能获取到新的oplog。
   3. Secondary上有单独的线程，当oplog的最新时间戳发生更新时，就会向Primary发送replSetUpdatePosition命令更新自己的oplog时间戳。
   4. 当Primary发现有足够多的节点oplog时间戳已经满足条件了，向客户端发送确认。

2. **readConcern vs readPreference**

MongoDB 可以通过 [writeConcern](https://yq.aliyun.com/articles/54367?spm=5176.100239.blogcont60553.7.InidvP) 来定制写策略，3.2版本后又引入了 `readConcern` 来灵活的定制读策略。

- [readPreference](https://docs.mongodb.com/manual/core/read-preference/?spm=5176.100239.blogcont60553.8.InidvP) 主要控制客户端 Driver 从复制集的哪个节点读取数据，这个特性可方便的实现读写分离、就近读取等策略。
  - `primary` 只从 primary 节点读数据，这个是默认设置
  - `primaryPreferred` 优先从 primary 读取，primary 不可服务，从 secondary 读
  - `secondary` 只从 scondary 节点读数据
  - `secondaryPreferred` 优先从 secondary 读取，没有 secondary 成员时，从 primary 读取
  - `nearest` 根据网络距离就近读取
- [readConcern](https://docs.mongodb.com/manual/reference/read-concern/?spm=5176.100239.blogcont60553.9.InidvP) 决定到某个读取数据时，能读到什么样的数据。
  - `local` 能读取任意数据，这个是默认设置
  - `majority` 只能读取到『成功写入到大多数节点的数据』

### readConcern 解决什么问题？

`readConcern` 的初衷在于解决『脏读』的问题，比如用户从 MongoDB 的 primary 上读取了某一条数据，但这条数据并没有同步到大多数节点，然后 primary 就故障了，重新恢复后 这个primary 节点会将未同步到大多数节点的数据回滚掉，导致用户读到了『脏数据』。

当指定 readConcern 级别为 majority 时，能保证用户读到的数据『已经写入到大多数节点』，而这样的数据肯定不会发生回滚，避免了脏读的问题。

需要注意的是，`readConcern` 能保证读到的数据『不会发生回滚』，但并不能保证读到的数据是最新的，这个官网上也有说明。

```
Regardless of the read concern level, the most recent data on a node may not reflect the most recent version of the data in the system.
```

有用户误以为，`readConcern` 指定为 majority 时，客户端会从大多数的节点读取数据，然后返回最新的数据。

实际上并不是这样，无论何种级别的 `readConcern`，客户端都只会从『某一个确定的节点』（具体是哪个节点由 readPreference 决定）读取数据，该节点根据自己看到的同步状态视图，只会返回已经同步到大多数节点的数据。

### readConcern 实现原理

MongoDB 要支持 majority 的 readConcern 级别，必须设置`replication.enableMajorityReadConcern`参数，加上这个参数后，MongoDB 会起一个单独的snapshot 线程，会周期性的对当前的数据集进行 snapshot，并记录 snapshot 时最新 oplog的时间戳，得到一个映射表。

| 最新 oplog 时间戳 | snapshot  | 状态        |
| ----------------- | --------- | ----------- |
| t0                | snapshot0 | committed   |
| t1                | snapshot1 | uncommitted |
| t2                | snapshot2 | uncommitted |
| t3                | snapshot3 | uncommitted |

只有确保 oplog 已经同步到大多数节点时，对应的 snapshot 才会标记为 commmited，用户读取时，从最新的 commited 状态的 snapshot 读取数据，就能保证读到的数据一定已经同步到的大多数节点。

关键的问题就是如何确定『oplog 已经同步到大多数节点』？

primary 节点

secondary 节点在 自身oplog发生变化时，会通过 replSetUpdatePosition 命令来将 oplog 进度立即通知给 primary，另外心跳的消息里也会包含最新 oplog 的信息；通过上述方式，primary 节点能很快知道 oplog 同步情况，知道『最新一条已经同步到大多数节点的 oplog』，并更新 snapshot 的状态。比如当t2已经写入到大多数据节点时，snapshot1、snapshot2都可以更新为 commited 状态。（不必要的 snapshot也会定期被清理掉）

secondary 节点

secondary 节点拉取 oplog 时，primary 节点会将『最新一条已经同步到大多数节点的 oplog』的信息返回给 secondary 节点，secondary 节点通过这个oplog时间戳来更新自身的 snapshot 状态。

### 分片服务搭建 

参考博客：https://www.cnblogs.com/zhoujinyi/p/4635444.html

#### 服务器分布

准备3台物理机

1. A物理机
   1. 配置服务器01：port = 20000
   2. 配置服务器02：port = 21000
   3. 配置服务器03：port = 22000
   4. 路由服务器01：port = 30000
   5. 分片服务器01：port = 40000
2. B物理机
   1. 分片服务器02：port = 40000
   2. 路由服务器02：port = 30000
3. C物理机
   1. 分片服务器03：port = 40000

#### 注意事项

- 配置服务器是一个普通的mongod进程，所以只需要新开一个实例即可。**配置服务器必须开启1个或则3个，开启2个则会报错**
- 路由服务器不保存数据，把日志记录一下即可。



## 权限管理

### 创建用户

1. mongo服务无认证启动。

2. 客户端连接后，进入admin数据库，执行如下命令：

   **创建root用户**：db.createUser({"user":"root", "pwd":"root", "roles":["root"]})

   **创建所有数据库的管理员**：db.createUser({"user":"admin", "pwd":"admin", "roles":["userAdminAnyDatabase"]})

3. 以认证方式重启启动，如下：

   ```
   ps -ef | grep mongo
   kill -9 mongo服务进程id
   mongod -f conf/mongod.conf --auth
   ```

4. 客户端连接后，进入admin数据库，执行`db.auth("admin", "admin")`进行认证，然后再进入对应数据库，如loanbiz来创建对应角色的用户

   1. 创建**userAdmin**角色的用户

      db.createUser({"user":"loanbiz_userAdmin", "pwd":"123456", "roles":[{"role":"userAdmin", "db":"loanbiz"}]})

   2. 创建**dbAdmin**角色的用户

      db.createUser({"user":"loanbiz_dbAdmin", "pwd":"123456", "roles":[{"role":"dbAdmin", "db":"loanbiz"}]})

   3. 创建**dbOwner**角色的用户

      db.createUser({"user":"loanbiz_dbOwner", "pwd":"123456", "roles":[{"role":"dbOwner", "db":"loanbiz"}]})

5. 再次重启服务再连接之后，进入loanbiz数据库

   1. 执行db.auth("loanbiz_userAdmin","123456")进行认证。loanbiz_userAdmin来创建用户

      db.createUser({"user":"loanbiz_readWrite", "pwd":"123456", "roles":[{"role":"readWrite", "db":"loanbiz"}]})

   2. 执行db.auth("loanbiz_dbOwner","123456")进行认证。loanbiz_dbOwner来创建用户

      db.createUser({"user":"loanbiz_readWrite2", "pwd":"123456", "roles":[{"role":"readWrite", "db":"loanbiz"}]})

### 查看当前库所有用户

show users

### 删除用户

db.drop("userName")

### 修改用户密码

方式一：db.changeUserPassword("userName", "newPassword")

方式二：db.updateUser("userName",{pwd:"newPassword"})