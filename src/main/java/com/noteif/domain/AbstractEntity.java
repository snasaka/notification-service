package com.noteif.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
@Data
public abstract class AbstractEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column( name="id", columnDefinition = "BINARY(16)", updatable = false )
	protected UUID id;

	@Column(name="created_by", length=50)
	protected String createdBy;

	@Column(name="date_created")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date dateCreated = new Date();

	@Column(name="updated_by", length=50)
	protected String updatedBy;

	@Column(name="date_updated")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date dateUpdated;
}
