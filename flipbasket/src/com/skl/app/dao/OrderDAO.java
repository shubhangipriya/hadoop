/**
 * @author Nirmallya Mukherjee
 * 
 * Note: this is a sample code and is provided as part of training without any warranty.
 * You can use this code in any way at your own risk. 
 */

package com.skl.app.dao;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.PreparedStatement;
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
import com.datastax.driver.core.exceptions.WriteTimeoutException;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.skl.app.entity.OrderDO;
import com.skl.cassandra.dao.CassandraClient;

public class OrderDAO {

	//Passing the cassandra client may be a good idea because I could be working on different schemas and/or diff clusters!!
	public static boolean save(CassandraClient cassandraClient, OrderDO order) {
		try {
			//Is batching across RK a good idea? Discuss
			PreparedStatement prepStatement = cassandraClient.getSession().prepare(
					"BEGIN BATCH " +
					"  INSERT INTO orders (user_id, year, month, order_num, date, status, amount, tax, details, created_on) values (?,?,?,?,?,?,?,?,?,?); " +
					"  INSERT INTO orders_idx1 (user_id, year, order_num, date) values (?,?,?,?); " + 
					"  INSERT INTO orders_idx2 (user_id, year, month, order_num, city, amount) values (?,?,?,?,?,?); " +
					" APPLY BATCH"
					);
			prepStatement.setConsistencyLevel(ConsistencyLevel.LOCAL_ONE);
			//Tracing should not be switched on like this BUT selectively in case of debugging
			prepStatement.enableTracing();
			ResultSet results = cassandraClient.getSession().execute(prepStatement.bind(
					//Binds for the order table
					order.getUserId(), order.getYear(), order.getMonth(), order.getOrderNum(),
					order.getDate(), order.getStatus(), order.getAmount(), order.getTax(), order.getDetails(), order.getCreatedOn(),
					//Binds for the idx 1 table
					order.getUserId(), order.getYear(), order.getOrderNum(), order.getDate(),
					//Binds for idx 2 table
					order.getUserId(), order.getYear(), order.getMonth(), order.getOrderNum(), order.getCity(), order.getAmount()
					));
			
			//Print the trace details of the above execution
			cassandraClient.displayExecutionInfo(results);
			prepStatement.disableTracing();
			return true;
		} catch (InvalidQueryException ex) {
			System.out.format("Invalid query. Contacted node %s Issue was %s", ex.getAddress(), ex.getLocalizedMessage());
		} catch (WriteTimeoutException ex) {
			System.out.format("Nodes ack %d, required were %d. Issue was %s", ex.getReceivedAcknowledgements(), ex.getRequiredAcknowledgements(), ex.getMessage());
		} catch (QueryConsistencyException ex) {
			System.out.format("Cannot reach required consistency level. Received ack %d, Required ack %d", ex.getReceivedAcknowledgements(), ex.getRequiredAcknowledgements());
		} catch (UnavailableException ex) {
			System.out.format("Coordinator %s, Alive replicas %d, needed replicas %d", ex.getAddress(), ex.getAliveReplicas(), ex.getRequiredReplicas());
		} catch (OperationTimedOutException ex) {
			System.out.format("Coordiator %s did not respond before timeout. Issue: %s", ex.getAddress(), ex.getMessage());
		} catch (NoHostAvailableException ex) {
			System.out.println("The cluster appears to be unavailable. Listing all hosts that were contacted.");
			Map<InetSocketAddress, Throwable> hostsContacted = ex.getErrors();
			for (Map.Entry<InetSocketAddress, Throwable> entry : hostsContacted.entrySet()) {
			    System.out.println(entry.getKey() + " : " + entry.getValue());
			}
		} catch (QueryExecutionException ex) {
			System.out.format("Bad query. Issue: ", ex.getMessage());
		} catch (ServerError ex) {
			System.out.format("Contacted coordinator %s Issue was %s", ex.getAddress(), ex.getMessage());
		} catch (Exception ex) {
			System.out.format("Exception, issue was %s", ex.getMessage());
		}
		return false;
	}
	
	
	
	//This method uses the builder method which is very efficient for dynamic queries
	public static List<OrderDO> getOrders(CassandraClient cassandraClient, String userId, int year) {
		List<OrderDO> orders = new ArrayList<OrderDO>();

		try {
			Statement statement = QueryBuilder.select().from("orders")
					.where(QueryBuilder.eq("user_id", userId))
					.and(QueryBuilder.eq("year", year))
					.orderBy(QueryBuilder.desc("order_num"));
			statement.setConsistencyLevel(ConsistencyLevel.LOCAL_ONE);
			statement.setFetchSize(25);		//Paging - Get the most recent 25 orders and also helps in paging in batches of 25; UI will show everything
			statement.enableTracing();
			ResultSet results = cassandraClient.getSession().execute(statement);
			List<Row> rows = (results!=null) ? results.all() : null;

			for(Row row : rows) {
				OrderDO order = new OrderDO(userId, row.getString("details"), row.getDouble("amount"), row.getDouble("tax"), false);
				order.setCreatedOn(row.getTimestamp("created_on"));
				order.setDate(row.getInt("date"));
				order.setOrderNum(row.getUUID("order_num"));
				order.setYear(row.getInt("year"));
				orders.add(order);
			}
			cassandraClient.displayExecutionInfo(results);
			statement.disableTracing();
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

		return orders;
	}
	
}
