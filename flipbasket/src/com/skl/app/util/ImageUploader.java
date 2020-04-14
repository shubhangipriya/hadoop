/**
 * @author Nirmallya Mukherjee
 * 
 * Note: this is a sample code and is provided as part of training without any warranty.
 * You can use this code in any way at your own risk. 
 */

package com.skl.app.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.utils.UUIDs;
import com.skl.cassandra.dao.CassandraClient;

public class ImageUploader {

	public static void main(String[] args) throws IOException {
		CassandraClient client = new CassandraClient("127.0.0.1", "flipbasket", "cassandra", "cassandra");
		PreparedStatement ps = client.getSession().prepare("insert into flipbasket.products (id,type,short_desc,long_desc,price,image) values(?,?,?,?,?,?)");
		BoundStatement bs = new BoundStatement(ps);
		
		try(Stream<Path> paths = Files.walk(Paths.get("/opt/_dv/workspace-SKL-v2/flipbasket/product_images"))) {
			paths.forEach(filePath -> {
		    	if (Files.isRegularFile(filePath)) {
		    		System.out.format("Working on %s ", filePath);
		    		FileInputStream fis;
		    		try {
		    			fis = new FileInputStream(filePath.toString());
		            	byte[] b= new byte[fis.available()+1];
		            	int length=b.length;
		            	System.out.format("Size %d bytes Obj->", length);
		            	fis.read(b);
		            	ByteBuffer buffer =ByteBuffer.wrap(b);
		            	System.out.println(buffer);
		            	
		            	client.getSession().executeAsync(bs.bind(UUIDs.random(),"Electronics","Short desc","Long desc",123.45,buffer));
		    		} catch(FileNotFoundException e) {
		    			e.printStackTrace();
		    		} catch (IOException e) {
		    			e.printStackTrace();
		    		}
		    	} else {
		        	System.out.format("Non regular file %s Ignoring.\n", filePath);
		    	}
		    });
		}
		
		client.destroy();
	}

}
