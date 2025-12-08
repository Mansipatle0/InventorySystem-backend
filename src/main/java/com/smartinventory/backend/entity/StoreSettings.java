package com.smartinventory.backend.entity;



import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "store_settings")
public class StoreSettings {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @Column(name = "shop_name")
 private String shopName;

 @Column(name = "gst_number")
 private String gstNumber;

 @Column(name = "address")
 private String address;

 @Column(name = "invoice_prefix")
 private String invoicePrefix;

 @Column(name = "tax_percent")
 private BigDecimal taxPercent;

 @Column(name = "discount_percent")
 private BigDecimal discountPercent;

 @Column(name = "created_at", updatable = false, insertable = false)
 private LocalDateTime createdAt;

 @Column(name = "updated_at", insertable = false)
 private LocalDateTime updatedAt;

 public Long getId() {
	return id;
 }

 public void setId(Long id) {
	this.id = id;
 }

 public String getShopName() {
	return shopName;
 }

 public void setShopName(String shopName) {
	this.shopName = shopName;
 }

 public String getGstNumber() {
	return gstNumber;
 }

 public void setGstNumber(String gstNumber) {
	this.gstNumber = gstNumber;
 }

 public String getAddress() {
	return address;
 }

 public void setAddress(String address) {
	this.address = address;
 }

 public String getInvoicePrefix() {
	return invoicePrefix;
 }

 public void setInvoicePrefix(String invoicePrefix) {
	this.invoicePrefix = invoicePrefix;
 }

 public BigDecimal getTaxPercent() {
	return taxPercent;
 }

 public void setTaxPercent(BigDecimal taxPercent) {
	this.taxPercent = taxPercent;
 }

 public BigDecimal getDiscountPercent() {
	return discountPercent;
 }

 public void setDiscountPercent(BigDecimal discountPercent) {
	this.discountPercent = discountPercent;
 }

 public LocalDateTime getCreatedAt() {
	return createdAt;
 }

 public void setCreatedAt(LocalDateTime createdAt) {
	this.createdAt = createdAt;
 }

 public LocalDateTime getUpdatedAt() {
	return updatedAt;
 }

 public void setUpdatedAt(LocalDateTime updatedAt) {
	this.updatedAt = updatedAt;
 }

}
