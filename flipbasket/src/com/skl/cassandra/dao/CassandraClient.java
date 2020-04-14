/**
 * @author Nirmallya Mukherjee
 * 
 * Note: this is a sample code and is provided as part of training without any warranty.
 * You can use this code in any way at your own risk. 
 */

package com.skl.cassandra.dao;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ExecutionInfo;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PerHostPercentileTracker;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.ProtocolOptions.Compression;
import com.datastax.driver.core.QueryLogger;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.QueryTrace;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.policies.ExponentialReconnectionPolicy;
import com.datastax.driver.core.policies.LatencyAwarePolicy;
import com.datastax.driver.core.policies.PercentileSpeculativeExecutionPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;

public class CassandraClient {
	
	//key to get the object from servlet context. Alternatively this class can be a spring bean as well
	public static final String CASSANDRA_DAO = "cassandra_dao";
	private static final Logger logger = Logger.getLogger(CassandraClient.class.getName());
	private static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");

	private String clusterNodes;
	private String schema;
	private String uid;
	private String pwd;
	
	private Cluster cluster;	//Thread safe - maintain one per app server
	private Session session;	//Thread safe - maintain one per KS

	
	public CassandraClient() {
		
	}
	
	public CassandraClient(String clusterNodes, String schema, String uid, String pwd) throws UnknownHostException {
		this.clusterNodes = clusterNodes;
		this.schema = schema;
		this.uid = uid;
		this.pwd = pwd;
		initialize();
	}	
	
	public void initialize() throws UnknownHostException {
		logger.info("Initializing CassandraClient with " + clusterNodes + " " + schema + " " + uid);
		List<InetAddress> contactPoints = new ArrayList<InetAddress>();
		String[] tokens = StringUtils.split(clusterNodes);
		for(String token : tokens) {
			contactPoints.add(InetAddress.getByName(token));
		}
		
		//http://docs.datastax.com/en/developer/java-driver/3.1/manual
		Cluster.Builder builder= null;

		//Pooling options are almost always managed internally by the driver; its here for demonstration only
		//https://github.com/datastax/java-driver/tree/3.2.x/manual/pooling
		//http://docs.datastax.com/en/latest-java-driver-api/com/datastax/driver/core/PoolingOptions.html
		PoolingOptions poolingOptions = new PoolingOptions();
        poolingOptions.setCoreConnectionsPerHost(HostDistance.LOCAL, 1);
        poolingOptions.setMaxConnectionsPerHost(HostDistance.LOCAL, 1);		//Usually core=max for cql protocol v3; default is core=max=1
        //Just keep in mind that high values for MaxRequestsPerConnection will give clients more bandwidth and therefore put more pressure on your cluster.
        //This might require some tuning, especially if you have many clients.
        poolingOptions.setMaxRequestsPerConnection(HostDistance.LOCAL, 1000);	//Max is 32768
        poolingOptions.setConnectionsPerHost(HostDistance.REMOTE, 2, 4);	//For convenience, core and max can be set simultaneously
        
        PerHostPercentileTracker perHostPercentileTracker = PerHostPercentileTracker
        	    .builderWithHighestTrackableLatencyMillis(15000)
        	    .build();
        PercentileSpeculativeExecutionPolicy speculativeRetryPolicy =
        	    new PercentileSpeculativeExecutionPolicy(
        	        perHostPercentileTracker,
        	        99.0,     // percentile
        	        2);

        //Slow or failed query logging http://docs.datastax.com/en/developer/java-driver/3.1/manual/logging/
        //NOTE- You need to adjust your logging framework to accept log messages from the QueryLogger (see above link for setting needed)
        QueryLogger queryLogger = QueryLogger.builder().withDynamicThreshold(perHostPercentileTracker, QueryLogger.DEFAULT_SLOW_QUERY_THRESHOLD_PERCENTILE).build();
        //QueryLogger queryLogger = QueryLogger.builder().withConstantThreshold(300).withMaxQueryStringLength(1000).build();

		builder= Cluster.builder()
				//1. Initial connect points; not to be mixed with seeds and/or coordinator nodes
				.addContactPoints(contactPoints)
				//2. Retry if we get a read/write timeout; DowngradingConsistencyRetryPolicy can be an issue but convenient.
				//http://docs.datastax.com/en/developer/java-driver/3.1/manual/retries/
				//http://docs.datastax.com/en/drivers/java/3.0/com/datastax/driver/core/policies/RetryPolicy.html
				.withRetryPolicy(DefaultRetryPolicy.INSTANCE)
				//3. Wait exponentially 5 min (1000*60*5) ms base; max 1hr; trying to connect to a dead node
				.withReconnectionPolicy(new ExponentialReconnectionPolicy(300000, 3600000))
				//4. http://docs.datastax.com/en/developer/java-driver/3.1/manual/load_balancing/
				//Do round robin in one DC only, inefficient if all nodes are considered; the default is tokenaware & dcaware RR policy. May consider - LatencyAware
				.withLoadBalancingPolicy(
						LatencyAwarePolicy.builder(
							new TokenAwarePolicy(
								DCAwareRoundRobinPolicy.builder()	//You can directly use the DCaware without TokenAware as well
									.withLocalDc("dc1")				//This is case sensitive as defined in rac-dc properties file
									.withUsedHostsPerRemoteDc(2)	//Try at most 2 remote DC nodes in case all local nodes are dead in the current DC
									//Allow remote DC to participate in LOCAL ConsistencyLevel, why? Assume Quorum and only 1 node is up in DC1,
									//so use one more from DC2 to reach CL, this is great for high availability
									.allowRemoteDCsForLocalConsistencyLevel()
									.build()
							)
						)
                        .withExclusionThreshold(2.0)
                        .withScale(100, TimeUnit.MILLISECONDS)
                        .withRetryPeriod(10, TimeUnit.SECONDS)
                        .withUpdateRate(100, TimeUnit.MILLISECONDS)
                        .withMininumMeasurements(50)
                        .build()
				)
				//5. Pooling options are almost never required as the driver will manage.
				.withPoolingOptions(poolingOptions)
				//6. Can have the driver do speculative retry. Do not do if !Idempotent. http://docs.datastax.com/en/developer/java-driver/3.1/manual/speculative_execution/
				.withSpeculativeExecutionPolicy(speculativeRetryPolicy)
				//7. Compress the payload as supported by the binary protocol (LZ4, SNAPPY or NONE), dependency on net.jpountz.lz4 (can specify in pom)
				//http://docs.datastax.com/en/developer/java-driver/3.1/manual/compression/
				.withCompression(Compression.LZ4)
				//8. Add default paging to disallow large data sets; can be overridden in DAO with statement.setFetchSize. Set default CL as well
				//http://docs.datastax.com/en/developer/java-driver/3.1/manual/paging/
				.withQueryOptions(new QueryOptions().setFetchSize(2000).setConsistencyLevel(ConsistencyLevel.LOCAL_ONE))
				//9. Socket options, set when the driver will timeout + other settings
				//http://docs.datastax.com/en/developer/java-driver/3.1/manual/socket_options/
				.withSocketOptions(new SocketOptions().setConnectTimeoutMillis(2000).setReadTimeoutMillis(2000))
				;
		if(StringUtils.isNotEmpty(uid)) {
			//Credentials are needed if your cluster has authentication enabled
			builder.withCredentials(uid, pwd);
		}

		cluster = builder.build();
		session = cluster.connect(schema);
		
		//Register the speculative retry policy tracker as it does not do automatically
		cluster.register(perHostPercentileTracker);
		
		//Register the slow query logger
		cluster.register(queryLogger);
		
		printMetadata();
	}
	
	
	public void printMetadata() {
		StringBuilder sb = new StringBuilder();
		sb.append("********************************************************************************************************************\n");
		sb.append("Cassandra client initialized: " + this + "\n");
		sb.append("The cluster Object is " + cluster + "\n");
		sb.append("Session object is: " + session + "\n");
		sb.append("Cluster name is " + cluster.getClusterName() + "\n");
		sb.append("Session logged keyspace is " + session.getLoggedKeyspace() + "\n");
		
		Metadata metadata = cluster.getMetadata();
		sb.append("Connected to cluster: " + metadata.getClusterName() + "\n");
		for (Host host : metadata.getAllHosts()) {
			sb.append("Datacenter: " + host.getDatacenter() + " Host: " + host.getAddress() + "; Rack: " + host.getRack() + "\n");
			sb.append("  Tokens->" + host.getTokens() + "\n");
		}
		sb.append("Token range: " + metadata.getTokenRanges().toString() + "\n");
		sb.append("********************************************************************************************************************");
		logger.info(sb.toString());
	}
	
	
	public void displayExecutionInfo(ResultSet results) {
		ExecutionInfo executionInfo = results.getExecutionInfo();
		System.out.printf("Host (queried): %s\n", executionInfo.getQueriedHost().toString());
		for (Host host : executionInfo.getTriedHosts()) {
			System.out.printf("Host (tried): %s\n", host.toString());
		}
		QueryTrace queryTrace = executionInfo.getQueryTrace();
		System.out.printf("Trace id: %s\n\n", queryTrace.getTraceId());
		System.out.printf("%-80s | %-12s | %-10s | %-12s\n", "activity", "timestamp", "source", "source_elapsed");
		System.out.println("---------------------------------------------------------------------------------+-------------+------------+--------------");
		for(QueryTrace.Event event : queryTrace.getEvents()) {
			System.out.printf("%80s | %12s | %10s | %12s\n", event.getDescription(), millis2Date(event.getTimestamp()),
															 event.getSource(), event.getSourceElapsedMicros());
		}
	}
	
	private static Object millis2Date(long timestamp) {
		return format.format(timestamp);
	}

	

	public void destroy() {
		logger.info("Cassandra cluster shutdown initiated ...");
		session.close();
		cluster.close();
		logger.info("Cassandra session and cluster closed");
		logger.info("If you get a message and exception regarding memory leak from tomcat, please see https://datastax-oss.atlassian.net/browse/JAVA-647");
		logger.info("Since you are bouncing tomcat, you should be fine.");
	}	

	
	public Session getSession(){
		return session;
	}

	public String getClusterNodes() {
		return clusterNodes;
	}

	public void setClusterNodes(String clusterNodes) {
		this.clusterNodes = clusterNodes;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
