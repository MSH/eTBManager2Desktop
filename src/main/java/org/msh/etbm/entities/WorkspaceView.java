package org.msh.etbm.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Handle aspects of the workspace related to its web view
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name="workspaceview")
public class WorkspaceView {

	@Id
	private Integer id;
	
	// specific information by country
	@OneToOne(mappedBy="view")
	private Workspace workspace;
	
	@Lob
	private byte[] picture;

	@Column(length=200)
	private String logoImage;
	
	@Column(length=20)
	private String pictureContentType;

	
	/**
	 * Return the URI for the workspace picture
	 * @return
	 */
	public String getPictureURI() {
		return (id == null? null: "/workspaceimg/img" + id.toString() + ".gif");
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the workspace
	 */
	public Workspace getWorkspace() {
		return workspace;
	}

	/**
	 * @param workspace the workspace to set
	 */
	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	/**
	 * @return the picture
	 */
	public byte[] getPicture() {
		return picture;
	}

	/**
	 * @param picture the picture to set
	 */
	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	/**
	 * @return the logoImage
	 */
	public String getLogoImage() {
		return logoImage;
	}

	/**
	 * @param logoImage the logoImage to set
	 */
	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}
	

	/** {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/** {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkspaceView other = (WorkspaceView) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * @return the pictureContentType
	 */
	public String getPictureContentType() {
		return pictureContentType;
	}

	/**
	 * @param pictureContentType the pictureContentType to set
	 */
	public void setPictureContentType(String pictureContentType) {
		this.pictureContentType = pictureContentType;
	}

	/**
	 * Change the content type according to the image type
	 * @param extension
	 * @return
	 */
	public boolean setPictureContentTypeByFileExtension(String extension) {
		if (extension == null)
			return false;
		
		extension = extension.toLowerCase();
		
		if (extension.equals(".gif"))
			pictureContentType = "image/gif";
		else
		if ((extension.equals(".jpg")) || (extension.equals(".jpeg")) || (extension.equals(".jpe")))
			pictureContentType = "image/jpeg";
		else
		if (extension.equals(".png"))
			pictureContentType = "image/x-png";
		else return false;
		
		return true;
	}
}
