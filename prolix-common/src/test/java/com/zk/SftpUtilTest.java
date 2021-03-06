package com.zk;

import com.jcraft.jsch.*;
import com.zk.utils.AssertUtil;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import static com.jcraft.jsch.ChannelSftp.APPEND;
import static com.jcraft.jsch.ChannelSftp.RESUME;

public class SftpUtilTest {
    private static final String host = "localhost";
    private static final int port = 22;
    private static final String username = "zhukai";
    private static final String password = "zhukaimm5";
    private static final String tempBasePathRemote = "/Users/zhukai/temp/remote/";
    private static final String tempBasePathLocal = "/Users/zhukai/temp/local/";
    private ChannelSftp sftp = null;
    private Session sshSession = null;

    @Test
    public void testls() throws SftpException {
        String remotePath = "/";
        Vector vector = sftp.ls(remotePath);
        for (Object o : vector) {
            System.out.println(o);
        }
    }

    @Test
    public void testPut() throws Exception {
        OutputStream out = null;
        try {
            String remotePath = "/Users/zhukai/temp/temp.txt";
            out = sftp.put(remotePath);
            byte[] bytes = "这是要写到到远程文件中的内容".getBytes(Charset.forName("utf8"));
            out.write(bytes);
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @Test
    public void testPut2() throws Exception {
        OutputStream out = null;
        try {
            String remotePath = "/Users/zhukai/temp/temp.txt";
            out = sftp.put(remotePath, APPEND);
            byte[] bytes = "\r\n往远程服务文件追加内容".getBytes(Charset.forName("utf8"));
            out.write(bytes);
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @Test
    public void testPut3() throws Exception {
        OutputStream out = null;
        try {
            String remotePath = "/Users/zhukai/temp/temp.txt";
            out = sftp.put(remotePath, RESUME);
            byte[] bytes = "\r\n往远程服务文件追加内容RESUME".getBytes(Charset.forName("utf8"));
            out.write(bytes);
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @Test
    public void testPut4() throws Exception {
        String localPath = "/Users/zhukai/temp/errInfo.txt";
        String remotePath = "/Users/zhukai/temp/xxx/temp2.txt";
        mkDirIfNotExisted("/Users/zhukai/temp/xxx/");
        sftp.put(localPath, remotePath);
    }

    private void mkDirIfNotExisted(String remoteDir) {
        try {
            AssertUtil.assertTrue(remoteDir.endsWith(File.separator), "remoteDir不是以\"/\"结尾");
            sftp.cd(remoteDir);
        } catch (SftpException e) {
            try {
                sftp.mkdir(remoteDir);
                System.out.println("创建成功");
            } catch (SftpException e1) {
                e1.printStackTrace();
            }
        }
    }


    @Test
    public void testGet() throws Exception {
        // 将远程文件读取到流中
        String remotePath = tempBasePathRemote + "remote.txt";
        InputStream inputStream = sftp.get(remotePath);
        System.out.println(new String(IOUtils.toByteArray(inputStream), Charset.forName("utf8")));
        inputStream.close();
    }

    @Test
    public void testGet2() throws Exception {
        String remotePath = tempBasePathRemote + "remote.txt";
        String localPath = tempBasePathLocal + "1.txt";
        sftp.get(remotePath, localPath);
    }

    @Test
    public void testRename() throws SftpException {
        String remotePath = "/Users/zhukai/temp/temp.txt";
        String newRemotePath = "/Users/zhukai/temp/newName.txt";
        sftp.rename(remotePath, newRemotePath);
    }

    /**
     * 测试rename
     * 测试结果：若newPath已存在，会被oldPath覆盖
     */
    @Test
    public void testRename2() throws SftpException {
        sftp.rename("/Users/zhukai/temp/policyNo.txt", "/Users/zhukai/temp/errInfo.txt");
    }

    @Test
    public void testPutMode() throws SftpException {
        sftp.put(new ByteArrayInputStream(String.format(", %s", new Date().toLocaleString()).getBytes()),
                "/Users/zhukai/temp/errInfo.txt",
                ChannelSftp.OVERWRITE);
    }

    @Test
    public void testMkdir() {
        try {

            sftp.mkdir("/Users/zhukai/temp/remote/xxx");
        } catch (SftpException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testFileIsExisted() {
        Assert.assertTrue(fileIsExisted("/Users/zhukai/temp/errInfo.txt"));
        Assert.assertTrue(!fileIsExisted("/Users/zhukai/temp/"));
        Assert.assertTrue(!fileIsExisted("/Users/zhukai/temp/no"));
    }

    public boolean fileIsExisted(String filePath) {
        SftpATTRS stat = null;
        try {
            stat = sftp.lstat(filePath);
        } catch (SftpException e) {
            return false;
        }
        return !stat.isDir();
    }

    @Before
    public void before() {
        try {
            JSch jsch = new JSch();
            sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void after() {
        if (this.sftp != null) {
            if (this.sftp.isConnected()) {
                this.sftp.disconnect();
                System.out.println("============= ChannelSftp已关闭 =============");
            }
        }
        if (this.sshSession != null) {
            if (this.sshSession.isConnected()) {
                this.sshSession.disconnect();
                System.out.println("============= Session已关闭 =============");
            }
        }
    }


}
