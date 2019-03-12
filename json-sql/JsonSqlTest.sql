-- mysql 5.7 新特性之 json 类型 基本操作
-- 参考地址：https://blog.csdn.net/bugs4j/article/details/79862699
drop table json_start;
create table json_start (
	id int auto_increment PRIMARY KEY not null,
	person json not NULL
);

-- 插入数据
INSERT INTO json_start
VALUES
	(
		NULL,
		'{"coder":[{"name":"张三","age":"18","gender":"男"},{"name":"李四","age":"14","gender":"男"},{"name":"王五","age":"15","gender":"女"}],
	    "teacher":[{"name":"刘二","age":"22","gender":"女"},{"name":"赵三","age":"23","gender":"男"},{"name":"王五","age":"15","gender":"女"}],
	    "student":[{"name":"张三","age":"18","gender":"男"},{"name":"李四","age":"14","gender":"男"},{"name":"王五","age":"15","gender":"女"}]
    }'
	);
INSERT INTO json_start
VALUES
	(
		NULL,
		'{"handsome":[{"name":"山山","age":"18","gender":"男"},{"name":"张一山","age":"26","gender":"男"},{"name":"bug4j","age":"18","gender":"男"}],
	    "beauty":[{"name":"刘亦菲","age":"18","gender":"女"},{"name":"xixi","age":"23","gender":"女"},{"name":"周冬雨","age":"15","gender":"女"}],
	    "fatty":[{"name":"zhangqi","age":"38","gender":"男"},{"name":"pangci","age":"30","gender":"男"},{"name":"王五","age":"15","gender":"女"}]
}'
	);
INSERT INTO json_start
VALUES
	(
		NULL,
		'[{"name":"奔驰","model":"C260","price":"200RMB"},
      {"name":"劳斯莱斯","model":"定制版","price":"166RMB"},
      {"name":"奥迪","model":"A6","price":"200000000RMB"}]'
	);

--## 更新记录：update 追加内容
-- 给第一条记录追加一个manager属性，很显然这是一个对象操作,执行语句如下：
UPDATE json_start set person = json_MERGE(person,'{"manager":[{"name":"laoda","age":"38","gender":"男"}]}') where id=1;
-- 第三条记录追加一个对象：
UPDATE json_start set person = json_merge(person,'[{"name":"大众","model":"k1","price":"600000000RMB"}]') where id=3;
-- 更新记录：追加内容到数组：
update json_start set person = json_ARRAY_APPEND(person,"$", JSON_OBJECT("name","玛莎拉蒂","model","ah","price","168RMB")) where id=3;

-- 更新记录：update 修改json属性：
UPDATE json_start set person = json_set(person,'$.coder[0].name','张三三三三三') where id = 1

-- 需要注意的是，当使用json_set（）方法去修改一整个对象或者数组时，mysql或将value当成字符串来解析保存，当你的value中包含”时，或自动进行转义，这样存储起来很浪费空间，同时也会产生问题，如果需要修改整个对象，建议使用Json_object(key，value，key2,value2....)来格式化你的对象，例如：
UPDATE json_start set person = json_set(person,'$.coder[0]',JSON_OBJECT("age",20, "name","张sisisisi","gender","女")) where id = 1;
UPDATE json_start set person = json_set(person,"$[0]",JSON_OBJECT("age",18,"name","张wuwuwuuwu","gender","男")) where id = 3;

-- 删除记录使用JSON_REMOVE(doc,path1，path2,...)方法
update json_start set person = json_REMOVE(person,"$[0]") where id = 3;
update json_start set person = json_REMOVE(person,"$.coder[0]") where id = 1;

-- 查询所有的属性key,使用json_keys(doc)方法：
select id,json_keys(person) from json_start;

-- 查询指定的记录中的某个属性或者值，使用Json_extract(doc,path)方法
select id,json_extract(person,"$.student[0].name") from json_start where id = 1;
select id,json_extract(person,"$.coder[0]") from json_start where id = 1;
-- 可以使用通配符‘*’来实现多值查询
select id,json_extract(person,"$.student[*].name") from json_start where id = 1;



