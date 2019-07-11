# **ChannelSftp类的主要API说明如下：** 

| 方法名                                                       | 方法说明                                                     |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| public   void put(String src, String dst)                    | 将本地文件名为src的文件上传到目标服务器，目标文件名为dst，若dst为目录，则目标文件名将与src文件名相同。采用默认的传输模式：OVERWRITE |
| public void put(String src, String dst, int mode)            | 将本地文件名为src的文件上传到目标服务器，目标文件名为dst，若dst为目录，则目标文件名将与src文件名相同。指定文件传输模式为mode（mode可选值为：ChannelSftp.OVERWRITE，ChannelSftp.RESUME，ChannelSftp.APPEND）。 |
| public   void put(String src, String dst, SftpProgressMonitor monitor) | 将本地文件名为src的文件上传到目标服务器，目标文件名为dst，若dst为目录，则目标文件名将与src文件名相同。采用默认的传输模式：OVERWRITE，并使用实现了SftpProgressMonitor接口的monitor对象来监控文件传输的进度。 |
| public void put(String src, String   dst,SftpProgressMonitor monitor, int mode) | 将本地文件名为src的文件上传到目标服务器，目标文件名为dst，若dst为目录，则目标文件名将与src文件名相同。指定传输模式为mode,并使用实现了SftpProgressMonitor接口的monitor对象来监控文件传输的进度。 |
| public void put(InputStream src, String dst)                 | 将本地的input   stream对象src上传到目标服务器，目标文件名为dst，dst不能为目录。采用默认的传输模式：OVERWRITE |
| public void put(InputStream src, String dst, int mode)       | 将本地的input   stream对象src上传到目标服务器，目标文件名为dst，dst不能为目录。指定文件传输模式为mode |
| public void put(InputStream src, String dst,   SftpProgressMonitor monitor) | 将本地的input   stream对象src上传到目标服务器，目标文件名为dst，dst不能为目录。采用默认的传输模式：OVERWRITE。并使用实现了SftpProgressMonitor接口的monitor对象来监控传输的进度。 |
| public void put(InputStream src, String   dst,SftpProgressMonitor monitor, int mode) | 将本地的input   stream对象src上传到目标服务器，目标文件名为dst，dst不能为目录。指定文件传输模式为mode。并使用实现了SftpProgressMonitor接口的monitor对象来监控传输的进度。 |
| public OutputStream put(String dst)                          | 该方法返回一个输出流，可以向该输出流中写入数据，最终将数据传输到目标服务器，目标文件名为dst，dst不能为目录。采用默认的传输模式：OVERWRITE |
| public OutputStream put(String dst, final int mode)          | 该方法返回一个输出流，可以向该输出流中写入数据，最终将数据传输到目标服务器，目标文件名为dst，dst不能为目录。指定文件传输模式为mode |
| public OutputStream put(String dst, final   SftpProgressMonitor monitor, final int mode) | 该方法返回一个输出流，可以向该输出流中写入数据，最终将数据传输到目标服务器，目标文件名为dst，dst不能为目录。指定文件传输模式为mode。并使用实现了SftpProgressMonitor接口的monitor对象来监控传输的进度。 |
| public OutputStream put(String dst, final   SftpProgressMonitor monitor, final int mode, long offset) | 该方法返回一个输出流，可以向该输出流中写入数据，最终将数据传输到目标服务器，目标文件名为dst，dst不能为目录。指定文件传输模式为mode。并使用实现了SftpProgressMonitor接口的monitor对象来监控传输的进度。offset指定了一个偏移量，从输出流偏移offset开始写入数据。 |
| get(String src, String dst)                                  | 下载文件到本地，src为目标服务器上的文件，不能为目录，dst为本地文件路径。若dst为目录，则本地文件名与目标服务器上的文件名一样。 |
| get(String src, String dst ,SftpProgressMonitor   monitor)   | 同get(String   src, String dst)，只是该方法允许传入传输进度的监控对象monitor。 |
| get(String src, String dst ,SftpProgressMonitor   monitor ,int mode) | 同get(String   src, String dst ,SftpProgressMonitor monitor)，同时，该方法增加了mode参数，允许指定文件传输模式 |
| rm(String path)                                              | 删除文件，path不能为目录，删除目录使用rmdir                  |
| rmdir(String path)                                           | 删除目录，但是只能删除空目录                                 |
| rename(String oldpath, String newpath)                       | 如果oldPath为目录，不要求目录必须为空<br/>如果newpath为目录，则newpath必须不能存在，如果已经存在该目录，则会出现重名或者移动失败<br/> 1、重命名文件或者目录 <br/> 2、移动文件或者目录 |
| ls(String path)                                              | 列出指定目录下的所有文件和子目录。该方法返回Vector对象，该列表具体存放的是LsEntry对象 |