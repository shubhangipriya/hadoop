/**
 * @author Nirmallya Mukherjee
 * 
 * Note: this is a sample code and is provided as part of training without any warranty.
 * You can use this code in any way at your own risk. 
 * 
 * Here is a little sample of providing a feature of updating a user password
  		Statement update = QueryBuilder.update("flipbasket", "users")
				.with(QueryBuilder.set("password", newPwd))
				.where((QueryBuilder.eq("user_id", uid)));
        session.execute(update);
        
   Here is how you can remove the user (not an ideal use case)
        Statement delete = QueryBuilder.delete().from("flipbasket", "users")
				.where(QueryBuilder.eq("user_id", uid));
		session.execute(delete);
 * 
 */

package com.skl.app.dao;

import java.net.InetSocketAddress;
import java.util.Map;

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
import com.skl.app.entity.UserDO;
import com.skl.cassandra.dao.CassandraClient;

public class UserDAO {

	public static UserDO getUser(CassandraClient cassandraClient, String loginId) {
		UserDO user = null;

		try {
			Statement statement = QueryBuilder.select().from("users").where(QueryBuilder.eq("user_id", loginId));
			statement.setConsistencyLevel(ConsistencyLevel.LOCAL_ONE);
			ResultSet results = cassandraClient.getSession().execute(statement);

			Row row = results!=null ? results.one() : null;
			if(row!=null) {
				user = new UserDO(row.getString("user_id"));
				user.setPassword(row.getString("password"));
				user.setFirstName(row.getString("first_name"));
				user.setLastName(row.getString("last_name"));
				user.setAddress1(row.getString("address_1"));
				user.setAddress1(row.getString("address_2"));
				user.setCity(row.getString("city"));
				user.setPhone(row.getString("phone"));
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
		
		return user;
	}
	
}

