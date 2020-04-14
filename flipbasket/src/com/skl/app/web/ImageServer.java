/**
 * @author Nirmallya Mukherjee
 * 
 * Note: this is a sample code and is provided as part of training without any warranty.
 * You can use this code in any way at your own risk. 
 */

package com.skl.app.web;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skl.app.dao.ProductDAO;
import com.skl.app.entity.ProductDO;
import com.skl.cassandra.dao.CassandraClient;


@SuppressWarnings("serial")
public class ImageServer extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		CassandraClient cassandraClient = (CassandraClient) this.getServletContext().getAttribute(CassandraClient.CASSANDRA_DAO);
		String uuidStr = req.getParameter(ProductDO.FetchAttributes.id.toString());
		ProductDO prod = ProductDAO.getProduct(cassandraClient, uuidStr);

		System.out.println("ID received is " + prod.getUuid());
		resp.setContentType("image/jpg");
		OutputStream out = resp.getOutputStream();
		out.write(prod.getImage());
	}

}
