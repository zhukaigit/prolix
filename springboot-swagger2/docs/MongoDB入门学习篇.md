## MongoDB入门学习篇

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

  

