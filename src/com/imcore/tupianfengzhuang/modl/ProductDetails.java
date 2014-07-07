package com.imcore.tupianfengzhuang.modl;

public class ProductDetails {
	public int id;
	public int categoryId;
	public String productName;
	public String altName;
	public String shortName;
	public int status;
	public String shortDesc;
	public String longDesc;
	public String barCode;
	public String imageUrl;
	public String productItem;
	public int price;
	public int saleTotal;
	public int favotieTotal;
	public int productItemSize;
	public int commentTotal;
	public String packing;
	public String weight;
	public String productImages;
	public int getCommentTotal() {
		return commentTotal;
	}
	public void setCommentTotal(int commentTotal) {
		this.commentTotal = commentTotal;
	}
	public String getProductImages() {
		return productImages;
	}
	public void setProductImages(String productImages) {
		this.productImages = productImages;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getAltName() {
		return altName;
	}
	public void setAltName(String altName) {
		this.altName = altName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getProductItem() {
		return productItem;
	}
	public void setProductItem(String productItem) {
		this.productItem = productItem;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getSaleTotal() {
		return saleTotal;
	}
	public void setSaleTotal(int saleTotal) {
		this.saleTotal = saleTotal;
	}
	public int getFavotieTotal() {
		return favotieTotal;
	}
	public void setFavotieTotal(int favotieTotal) {
		this.favotieTotal = favotieTotal;
	}
	public int getProductItemSize() {
		return productItemSize;
	}
	public void setProductItemSize(int productItemSize) {
		this.productItemSize = productItemSize;
	}
	public String getPacking() {
		return packing;
	}
	public void setPacking(String packing) {
		this.packing = packing;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
}
