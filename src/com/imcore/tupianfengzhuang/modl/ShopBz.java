package com.imcore.tupianfengzhuang.modl;

public class ShopBz {

	public int id;
	public long productId;
	public int retailPrice;
	public String weight;
	public String imageUrl;
	public String packing;
	public String productImages;
	public String sku;
	public int regularPrice;
	public int getRegularPrice() {
		return regularPrice;
	}
	public void setRegularPrice(int regularPrice) {
		this.regularPrice = regularPrice;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getProductImages() {
		return productImages;
	}
	public void setProductImages(String productImages) {
		this.productImages = productImages;
	}
	public String getPacking() {
		return packing;
	}
	public void setPacking(String packing) {
		this.packing = packing;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public int getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(int retailPrice) {
		this.retailPrice = retailPrice;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
}
