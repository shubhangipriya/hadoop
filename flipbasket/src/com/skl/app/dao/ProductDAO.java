/**
 * @author Nirmallya Mukherjee
 * 
 * Note: this is a sample code and is provided as part of training without any warranty.
 * You can use this code in any way at your own risk. 
 * 
 */

package com.skl.app.dao;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.OperationTimedOutException;
import com.datastax.driver.core.exceptions.QueryConsistencyException;
import com.datastax.driver.core.exceptions.QueryExecutionException;
import com.datastax.driver.core.exceptions.ReadFailureException;
import com.datastax.driver.core.exceptions.ReadTimeoutException;
import com.datastax.driver.core.exceptions.ServerError;
import com.datastax.driver.core.exceptions.UnavailableException;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.skl.app.entity.ProductDO;
import com.skl.cassandra.dao.CassandraClient;

public class ProductDAO {

	public static List<ProductDO> getProducts(CassandraClient cassandraClient) {
		ArrayList<ProductDO> list = new ArrayList<ProductDO>();

		try {
			Statement statement = QueryBuilder.select()
									.column("id").column("type").column("short_desc").column("long_desc").column("price")
									.from("products");
			statement.setConsistencyLevel(ConsistencyLevel.LOCAL_ONE);
			ResultSet results = cassandraClient.getSession().execute(statement);

			List<Row> rows = (results!=null) ? results.all() : null;
			if(rows!=null) {
				rows.forEach(row -> {
					ProductDO product = new ProductDO();
					product.setUuid(row.getUUID("id"));
					product.setType(row.getString("type"));
					product.setShortDesc(row.getString("short_desc"));
					product.setLongDesc(row.getString("long_desc"));
					product.setPrice(row.getDouble("price"));
					list.add(product);
				});
			}
		} catch (InvalidQueryException ex) {
			System.out.format("Invalid query. Contacted node %d Issue was %s", ex.getAddress(), ex.getLocalizedMessage());
		} catch (ReadTimeoutException ex) {
			System.out.format("Nodes ack %d, required were %d. Issue was %s", ex.getReceivedAcknowledgements(), ex.getRequiredAcknowledgements(), ex.getMessage());
		} catch (ReadFailureException ex) {
			System.out.format("Non timeout issue. %d replicas experienced failure. Ack got %d ack needed %d, Data fetched %s", ex.getFailures(), ex.getReceivedAcknowledgements(), ex.getRequiredAcknowledgements(), ex.wasDataRetrieved());
		} catch (QueryConsistencyException ex) {
			System.out.format("Cannot reach required consistency level. Received ack %d, Required ack %d", ex.getReceivedAcknowledgements(), ex.getRequiredAcknowledgements());
		} catch (UnavailableException ex) {
			System.out.format("Coordinator %s Alive replicas %d needed replicas %d", ex.getAddress(), ex.getAliveReplicas(), ex.getRequiredReplicas());
		} catch (NoHostAvailableException ex) {
			System.out.println("The cluster appears to be unavailable. Listing all hosts that were contacted.");
			Map<InetSocketAddress, Throwable> hostsContacted = ex.getErrors();
			for (Map.Entry<InetSocketAddress, Throwable> entry : hostsContacted.entrySet()) {
			    System.out.println(entry.getKey() + " : " + entry.getValue());
			}
		} catch (OperationTimedOutException ex) {
			System.out.format("Coordiator %s did not respond before timeout. Issue: %s", ex.getAddress(), ex.getMessage());
		} catch (QueryExecutionException ex) {
			System.out.format("Bad query. Issue: ", ex.getMessage());
		} catch (ServerError ex) {
			System.out.format("Contacted coordinator %s Issue was %s", ex.getAddress(), ex.getMessage());
		} catch (Exception ex) {
			System.out.format("Exception, issue was %s", ex.getMessage());
		}
		
		return list;
	}

	
	
	public static ProductDO getProduct(CassandraClient cassandraClient, String uuidStr) {
		ProductDO product = new ProductDO();

		try {
			UUID uuid = UUID.fromString(uuidStr);
			Statement statement = QueryBuilder.select().from("products").where(QueryBuilder.eq("id", uuid));
			statement.setConsistencyLevel(ConsistencyLevel.LOCAL_ONE);
			ResultSet results = cassandraClient.getSession().execute(statement);

			Row row = results!=null ? results.one() : null;
			if(row!=null) {
				product.setUuid(row.getUUID("id"));
				product.setType(row.getString("type"));
				product.setShortDesc(row.getString("short_desc"));
				product.setLongDesc(row.getString("long_desc"));
				product.setPrice(row.getDouble("price"));
				
				ByteBuffer bb = row.getBytes("image");
				byte image[]= bb.array();
				product.setImage(image);
			}
		} catch (InvalidQueryException ex) {
			System.out.format("Invalid query. Contacted node %d Issue was %s", ex.getAddress(), ex.getLocalizedMessage());
		} catch (ReadTimeoutException ex) {
			System.out.format("Nodes ack %d, required were %d. Issue was %s", ex.getReceivedAcknowledgements(), ex.getRequiredAcknowledgements(), ex.getMessage());
		} catch (ReadFailureException ex) {
			System.out.format("Non timeout issue. %d replicas experienced failure. Ack got %d ack needed %d, Data fetched %s", ex.getFailures(), ex.getReceivedAcknowledgements(), ex.getRequiredAcknowledgements(), ex.wasDataRetrieved());
		} catch (QueryConsistencyException ex) {
			System.out.format("Cannot reach required consistency level. Received ack %d, Required ack %d", ex.getReceivedAcknowledgements(), ex.getRequiredAcknowledgements());
		} catch (UnavailableException ex) {
			System.out.format("Coordinator %s Alive replicas %d needed replicas %d", ex.getAddress(), ex.getAliveReplicas(), ex.getRequiredReplicas());
		} catch (NoHostAvailableException ex) {
			System.out.println("The cluster appears to be unavailable. Listing all hosts that were contacted.");
			Map<InetSocketAddress, Throwable> hostsContacted = ex.getErrors();
			for (Map.Entry<InetSocketAddress, Throwable> entry : hostsContacted.entrySet()) {
			    System.out.println(entry.getKey() + " : " + entry.getValue());
			}
		} catch (OperationTimedOutException ex) {
			System.out.format("Coordiator %s did not respond before timeout. Issue: %s", ex.getAddress(), ex.getMessage());
		} catch (QueryExecutionException ex) {
			System.out.format("Bad query. Issue: ", ex.getMessage());
		} catch (ServerError ex) {
			System.out.format("Contacted coordinator %s Issue was %s", ex.getAddress(), ex.getMessage());
		} catch (Exception ex) {
			System.out.format("Exception, issue was %s", ex.getMessage());
		}
		
		return product;
	}

}

