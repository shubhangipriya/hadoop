<!-- 
 * Note: this is a sample code and is provided as part of training without any warranty.
 * You can use this code in any way at your own risk. 
 -->

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
    import="com.skl.app.entity.*,
    		java.util.List,
    		com.skl.cassandra.dao.CassandraClient,
    		com.skl.app.dao.ProductDAO
    	   "
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Flipbasket - Products page</title>
	
	<!-- Bootstrap itself -->
	<link href="/assets/css/bootstrap.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="/assets/css/font-awesome.css">
	<style type="text/css" media="screen">
		body {
			overflow: show;
			padding: 20px;
		}
	</style>
	
</head>

<body>
	<table width="100%">
		<tr>
			<td width="50%"><h1>Flipbasket</h1></td>
			<td width="50%" align="right">Welcome <%= session.getAttribute(UserDO.DisplayAttributes.firstName.toString()) %>!<br>[<a href="/logout">Logout</a>]</td>
		</tr>
	</table>
	<b>Product catalog</b><br><br>
	<%
		CassandraClient client = (CassandraClient) getServletContext().getAttribute(CassandraClient.CASSANDRA_DAO);
		List<ProductDO> list = ProductDAO.getProducts(client);
		if(list!=null) {
			for(ProductDO product : list) {
	%>
			<img src="/imgserver?<%= ProductDO.FetchAttributes.id.toString() %>=<%= product.getUuid() %>" width="100" /><br/><%= product.getType() %>&nbsp;<%= product.getPrice() %>&nbsp;<%= product.getLongDesc() %>
			<br><br>
	<%
			}
		}

	%>
</body>

</html>
