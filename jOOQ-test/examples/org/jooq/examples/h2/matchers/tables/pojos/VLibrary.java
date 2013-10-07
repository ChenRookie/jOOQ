/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.h2.matchers.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class VLibrary implements org.jooq.examples.h2.matchers.tables.interfaces.IVLibrary {

	private static final long serialVersionUID = 145928880;

	private java.lang.String author;
	private java.lang.String the__title;

	public VLibrary() {}

	public VLibrary(
		java.lang.String author,
		java.lang.String the__title
	) {
		this.author = author;
		this.the__title = the__title;
	}

	@Override
	public java.lang.String getAuthor() {
		return this.author;
	}

	@Override
	public void setAuthor(java.lang.String author) {
		this.author = author;
	}

	@Override
	public java.lang.String getTheTitle() {
		return this.the__title;
	}

	@Override
	public void setTheTitle(java.lang.String the__title) {
		this.the__title = the__title;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(org.jooq.examples.h2.matchers.tables.interfaces.IVLibrary from) {
		setAuthor(from.getAuthor());
		setTheTitle(from.getTheTitle());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends org.jooq.examples.h2.matchers.tables.interfaces.IVLibrary> E into(E into) {
		into.from(this);
		return into;
	}
}