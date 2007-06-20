/*
 * Bid.java
 *
 * Created on June 6, 2007, 9:36 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.icesoft.eb;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;
import org.jboss.seam.annotations.Name;


@Entity
@Name("bid")
@Table(name = "Bid")
public class Bid implements Serializable
{
   private Long id;
   private User user;
   private Auctionitem bidItem;
   private double bidValue;
   private Date timestamp;
   private String creditCard;
   private String creditCardName;
   private int creditCardExpiryMonth;
   private int creditCardExpiryYear;
   
   public Bid() {}
   
   public Bid(Auctionitem bidItem, User user)
   {
      this.bidItem = bidItem;
      this.user = user;
   }

   @Id @GeneratedValue
   public Long getId()
   {
      return id;
   }
   public void setId(Long id)
   {
      this.id = id;
   }
   
   @NotNull
   @Basic @Temporal(TemporalType.DATE) 
   public Date getTimestamp()
   {
      return timestamp;
   }
   public void setTimestamp(Date timestamp)
   {
      this.timestamp = timestamp;
   }

   @ManyToOne @NotNull
   public Auctionitem getAuctionItem()
   {
      return bidItem;
   }
   public void setAuctionItem(Auctionitem bidItem)
   {
      this.bidItem = bidItem;
   }
   
   @ManyToOne @NotNull
   public User getUser()
   {
      return user;
   }
   public void setUser(User user)
   {
      this.user = user;
   }
   
   
   @NotNull(message="Credit card number is required")
   @Length(min=16, max=16, message="Credit card number must 16 digits long")
   @Pattern(regex="^\\d*$", message="Credit card number must be numeric")
   public String getCreditCard()
   {
      return creditCard;
   }

   public void setCreditCard(String creditCard)
   {
      this.creditCard = creditCard;
   }
   
   @Transient
   public String getDescription()
   {
      return this.bidItem.toString();
   
   }



   @NotNull(message="Credit card name is required")
   @Length(min=3, max=70, message="Credit card name is required")
   public String getCreditCardName()
   {
      return creditCardName;
   }

   public void setCreditCardName(String creditCardName)
   {
      this.creditCardName = creditCardName;
   }

   public int getCreditCardExpiryMonth()
   {
      return creditCardExpiryMonth;
   }

   public void setCreditCardExpiryMonth(int creditCardExpiryMonth)
   {
      this.creditCardExpiryMonth = creditCardExpiryMonth;
   }

   public int getCreditCardExpiryYear()
   {
      return creditCardExpiryYear;
   }

   public void setCreditCardExpiryYear(int creditCardExpiryYear)
   {
      this.creditCardExpiryYear = creditCardExpiryYear;
   }
   
   @Override
   public String toString()
   {
      return "Bid(" + user + ","+ bidItem + ")";
   }

    @NotNull(message = "Bid value is required")
    public double getBidValue() {
        return bidValue;
    }

    public void setBidValue(double bidValue) {
        this.bidValue = bidValue;
    }
}
