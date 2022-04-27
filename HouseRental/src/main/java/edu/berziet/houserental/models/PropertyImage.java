package edu.berziet.houserental.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "propertyimgee")
public class PropertyImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id")
	private Integer id = null;
	

	@Column(name="file_name")
	private String fileName;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="property_id")
	private Property property;

	public PropertyImage() {
		super();
	}

	public PropertyImage(Integer id, String imagePath, Property property) {
		super();
		this.id = id;
		this.fileName = imagePath;
		this.property = property;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String imagePath) {
		this.fileName = imagePath;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}
	
	
	
}
