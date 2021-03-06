package com;

import java.io.IOException;
import java.security.PrivilegedExceptionAction;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

public class ConnectionHadoop {

	public static void main(String[] args) {
		PrivilegedExceptionAction<Void> pea = new PrivilegedExceptionAction<Void>() {

			@Override
			public Void run() throws Exception {
				Configuration config = new Configuration();
				config.set("fs.defaultFS", "hdfs://192.168.0.150:9000/user/bdi");
				config.set("hadoop.job.ugi", "bdi");
				config.setBoolean("dfs.support.append", true);
				
				FileSystem fs = FileSystem.get(config);
				
				Path upFileName = new Path("word.txt");
				if(fs.exists(upFileName)) {
					fs.delete(upFileName,true);
				}
				FSDataOutputStream fsdo = fs.create(upFileName);
				fsdo.writeUTF("hi hi hi hey hey lol start hi");
				fsdo.close();
				FileStatus[] fss = fs.listStatus(new Path("/user/bdi"));
				for(FileStatus file:fss) {
					System.out.println(file);
				}
				return null;
			}
		};
		
		UserGroupInformation ugi = UserGroupInformation.createRemoteUser("bdi");
		try {
			ugi.doAs(pea);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}