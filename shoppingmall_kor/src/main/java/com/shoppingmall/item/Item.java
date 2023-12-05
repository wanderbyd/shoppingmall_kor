package com.shoppingmall.item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "Item")
public class Item {
    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_SEQ")
	@SequenceGenerator(name = "ITEM_SEQ", sequenceName = "ITEM_SEQ", allocationSize = 1)
    @Column(name = "itemid")
    private Long itemid;
   
    
	@Column(name = "filename")
	private String filename;

	@Column(name = "fileOriName")
	private String fileOriName;

	@Column(name = "fileUrl")
	private String fileUrl;
	
    public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFileOriName() {
		return fileOriName;
	}

	public void setFileOriName(String fileOriName) {
		this.fileOriName = fileOriName;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	@Column(name = "price")
    private Long price;
  
    @Column(name = "name")
    private String name;
    
    @Column(name = "imagePath")
    private String imagePath;
   
    @Column(name = "imagePath2")
    private String imagePath2;
   
    @Column(name = "description")
    private String description;
   
    @Column(name = "stock")
    private Integer stock;
   
    @Column(name = "sale")
    private boolean sale;
    
    @Column(name = "category")
   // @Enumerated(EnumType.STRING)
    private Category category;

    public Item() {}



	public Item(Long itemid, String filename, String fileOriName, String fileUrl, Long price, String name,
			String imagePath, String imagePath2, String description, Integer stock, boolean sale, Category category) {
		super();
		this.itemid = itemid;
		this.filename = filename;
		this.fileOriName = fileOriName;
		this.fileUrl = fileUrl;
		this.price = price;
		this.name = name;
		this.imagePath = imagePath;
		this.imagePath2 = imagePath2;
		this.description = description;
		this.stock = stock;
		this.sale = sale;
		this.category = category;
	}

	public Long getItemid() {
		return itemid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getImagePath2() {
		return imagePath2;
	}

	public void setImagePath2(String imagePath2) {
		this.imagePath2 = imagePath2;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public boolean isSale() {
		return sale;
	}

	public void setSale(boolean sale) {
		this.sale = sale;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Item [itemid=" + itemid + ", filename=" + filename + ", fileOriName=" + fileOriName + ", fileUrl="
				+ fileUrl + ", price=" + price + ", name=" + name + ", imagePath=" + imagePath + ", imagePath2="
				+ imagePath2 + ", description=" + description + ", stock=" + stock + ", sale=" + sale + ", category="
				+ category + "]";
	}


}


