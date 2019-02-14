package com.zk.springbootswagger2.mongo;

import com.mongodb.client.gridfs.model.GridFSFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;


@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFsTemplateTest {

  @Autowired
  private GridFsTemplate gridFsTemplate;

  @Test
  public void testStoreFileInGridFs() throws Exception {
    File file = null;
    try {
      file = ResourceUtils.getFile("classpath:mongo.properties");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    FileInputStream inputStream = new FileInputStream(file);
    ObjectId objectId = gridFsTemplate.store(inputStream, file.getName());
    System.out.println("存储的id为：" + objectId);
    System.out.println("文件名称："+file.getName());
  }

  @Test
  public void testFindFIleInGridFs() throws IOException {
    GridFSFile gfsFile = gridFsTemplate
        .findOne(Query.query(Criteria.where("filename").is("mongo.properties")));
    GridFsResource resource = gridFsTemplate.getResource(gfsFile);
    InputStream inputStream = resource.getInputStream();
    System.out.println("文件字符串如下：\n" + StreamUtils.copyToString(inputStream, Charset.forName("utf-8")));
  }

}
