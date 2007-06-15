package com.icesoft.eb;
// Generated Jun 5, 2007 2:22:49 PM by Hibernate Tools 3.2.0.b9

import javax.persistence.*;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;

import java.util.ArrayList;
import java.util.List;

/**
 * Auctionitem generated by hbm2java
 */
@Entity
@Name("auctionitem")
@Table(name = "auctionitem")
public class Auctionitem implements java.io.Serializable {

	private long itemId;
	private int bidCount;
	private String currency;
	private String description;
	private String imageFile;
	private String location;
	private double price;
	private String seller;
	private String site;
	private String title;
	private int expiresindays;
    private List<Bid> bids = new ArrayList<Bid>();

	public Auctionitem() {
	}

	public Auctionitem(long itemId, int bidCount, String currency,
			String description, String imageFile, String location,
			double price, String seller, String site, String title,
			int expiresindays) {
		this.itemId = itemId;
		this.bidCount = bidCount;
		this.currency = currency;
		this.description = description;
		this.imageFile = imageFile;
		this.location = location;
		this.price = price;
		this.seller = seller;
		this.site = site;
		this.title = title;
		this.expiresindays = expiresindays;
	}

	@Id
	@Column(name = "itemId", unique = true, nullable = false)
	@NotNull
	public long getItemId() {
		return this.itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "bidCount", nullable = false)
	@NotNull
	public int getBidCount() {
		return this.bidCount;
	}

	public void setBidCount(int bidCount) {
		this.bidCount = bidCount;
	}

	@Column(name = "currency", nullable = false, length = 5)
	@NotNull
	@Length(max = 5)
	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Column(name = "description", nullable = false, length = 80)
	@NotNull
	@Length(max = 80)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "imageFile", nullable = false, length = 45)
	@NotNull
	@Length(max = 45)
	public String getImageFile() {
		return this.imageFile;
	}

	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}

	@Column(name = "location", nullable = false, length = 45)
	@NotNull
	@Length(max = 45)
	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Column(name = "price", nullable = false, precision = 22, scale = 0)
	@NotNull
	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Column(name = "seller", nullable = false, length = 45)
	@NotNull
	@Length(max = 45)
	public String getSeller() {
		return this.seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	@Column(name = "site", nullable = false, length = 45)
	@NotNull
	@Length(max = 45)
	public String getSite() {
		return this.site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	@Column(name = "title", nullable = false, length = 45)
	@NotNull
	@Length(max = 45)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "expiresindays", nullable = false)
	@NotNull
	public int getExpiresindays() {
		return this.expiresindays;
	}

	public void setExpiresindays(int expiresindays) {
		this.expiresindays = expiresindays;
	}
        
        // not auto-generated
        @Override
        public String toString(){
            return "AuctionItem(" + itemId + "," + description + ","+ price + "," + title +","+expiresindays+")";
        }

    @OneToMany(mappedBy = "auctionItem")
    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }
}
