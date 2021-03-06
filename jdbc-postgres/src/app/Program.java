package app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import db.DB;
import entities.Order;
import entities.Product;
import entities.enums.OrderStatus;

public class Program {

	public static void main(String[] args) throws SQLException {
		
		Connection conn = DB.getConnection();
	
		Statement st = conn.createStatement();
			
		ResultSet rs = st.executeQuery("SELECT * FROM tb_order "
				+ "INNER JOIN tb_order_product ON tb_order.id = tb_order_product.order_id "
				+ "INNER JOIN tb_product ON tb_product.id = tb_order_product.product_id");
		
		Map<Long, Order> map = new HashMap<>();
		Map<Long, Product> mapProduct = new HashMap<>();
		while (rs.next()) {
			
			Long orderId = rs.getLong("order_id");
			if(map.get(orderId) == null) {
				Order order = order(rs);
				map.put(orderId, order);
			}
			
			Long productId = rs.getLong("product_id");
			if(map.get(productId) == null) {
				Product product = product(rs);
				mapProduct.put(productId, product);
			}
			
			map.get(orderId).getProducts().add(mapProduct.get(productId));
			
			for(Long x : map.keySet()) {
				System.out.println(map.get(x));
				for(Product p: map.get(x).getProducts()) {
					System.out.println(p);	
				}
			}
		}
		
	}
	
	private static Order order(ResultSet rs) throws SQLException {
		
		Order o = new Order();
		
		o.setId(rs.getLong("id"));
		o.setLatitude(rs.getDouble("latitude"));
		o.setLongitude(rs.getDouble("longitude"));
		o.setMoment(rs.getTimestamp("moment").toInstant());
		o.setStatus(OrderStatus.values()[rs.getInt("status")]);
		
		return o;
		
}
	@SuppressWarnings("unused")
	private static Product product(ResultSet rs) throws SQLException {
		
		Product p = new Product();
		
		p.setId(rs.getLong("id"));
		p.setDescription(rs.getString("description"));
		p.setName(rs.getString("name"));
		p.setPrice(rs.getDouble("price"));
		p.setImageUri(rs.getString("image_uri"));
		
		return p;
		
}
	
	
}
