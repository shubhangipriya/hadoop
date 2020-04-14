/**
 * @author Nirmallya Mukherjee
 * 
 * Note: this is a sample code and is provided as part of training without any warranty.
 * You can use this code in any way at your own risk. 
 */

package com.skl.app.entity;

import java.util.UUID;

public class ProductDO {
	
	public enum FetchAttributes {
		id
	}

	private UUID uuid;
	private String type;
	private String shortDesc;
	private String longDesc;
	private byte image[];
	private double price;
	
	public ProductDO() {
		
	}
	
	public ProductDO(UUID uuid, String type, String shortDesc, String longDesc, double price, byte image[]) {
		this.uuid = uuid;
		this.type = type;
		this.shortDesc = shortDesc;
		this.longDesc = longDesc;
		this.setPrice(price);
		this.image = image;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	public String getLongDesc() {
		return longDesc;
	}
	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
