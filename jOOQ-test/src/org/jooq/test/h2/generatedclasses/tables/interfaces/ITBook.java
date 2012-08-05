/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses.tables.interfaces;

/**
 * This class is generated by jOOQ.
 *
 * An entity holding books
 */
public interface ITBook extends java.io.Serializable {

	/**
	 * The book ID
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public void setId(java.lang.Integer value);

	/**
	 * The book ID
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public java.lang.Integer getId();

	/**
	 * The author ID in entity 'author'
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT FK_T_BOOK_AUTHOR_ID
	 * FOREIGN KEY (AUTHOR_ID)
	 * REFERENCES PUBLIC.T_AUTHOR (ID)
	 * </pre></code>
	 */
	public void setAuthorId(java.lang.Integer value);

	/**
	 * The author ID in entity 'author'
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT FK_T_BOOK_AUTHOR_ID
	 * FOREIGN KEY (AUTHOR_ID)
	 * REFERENCES PUBLIC.T_AUTHOR (ID)
	 * </pre></code>
	 */
	public java.lang.Integer getAuthorId();

	/**
	 * The table column <code>PUBLIC.T_BOOK.CO_AUTHOR_ID</code>
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT FK_T_BOOK_CO_AUTHOR_ID
	 * FOREIGN KEY (CO_AUTHOR_ID)
	 * REFERENCES PUBLIC.T_AUTHOR (ID)
	 * </pre></code>
	 */
	public void setCoAuthorId(java.lang.Integer value);

	/**
	 * The table column <code>PUBLIC.T_BOOK.CO_AUTHOR_ID</code>
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT FK_T_BOOK_CO_AUTHOR_ID
	 * FOREIGN KEY (CO_AUTHOR_ID)
	 * REFERENCES PUBLIC.T_AUTHOR (ID)
	 * </pre></code>
	 */
	public java.lang.Integer getCoAuthorId();

	/**
	 * The table column <code>PUBLIC.T_BOOK.DETAILS_ID</code>
	 */
	public void setDetailsId(java.lang.Integer value);

	/**
	 * The table column <code>PUBLIC.T_BOOK.DETAILS_ID</code>
	 */
	public java.lang.Integer getDetailsId();

	/**
	 * The book's title
	 */
	public void setTitle(java.lang.String value);

	/**
	 * The book's title
	 */
	public java.lang.String getTitle();

	/**
	 * The year the book was published in
	 */
	public void setPublishedIn(java.lang.Integer value);

	/**
	 * The year the book was published in
	 */
	public java.lang.Integer getPublishedIn();

	/**
	 * The language of the book
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT FK_T_BOOK_LANGUAGE_ID
	 * FOREIGN KEY (LANGUAGE_ID)
	 * REFERENCES PUBLIC.T_LANGUAGE (ID)
	 * </pre></code>
	 */
	public void setLanguageId(org.jooq.test.h2.generatedclasses.enums.TLanguage value);

	/**
	 * The language of the book
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT FK_T_BOOK_LANGUAGE_ID
	 * FOREIGN KEY (LANGUAGE_ID)
	 * REFERENCES PUBLIC.T_LANGUAGE (ID)
	 * </pre></code>
	 */
	public org.jooq.test.h2.generatedclasses.enums.TLanguage getLanguageId();

	/**
	 * Some textual content of the book
	 */
	public void setContentText(java.lang.String value);

	/**
	 * Some textual content of the book
	 */
	public java.lang.String getContentText();

	/**
	 * Some binary content of the book
	 */
	public void setContentPdf(byte[] value);

	/**
	 * Some binary content of the book
	 */
	public byte[] getContentPdf();

	/**
	 * The table column <code>PUBLIC.T_BOOK.REC_VERSION</code>
	 */
	public void setRecVersion(java.lang.Integer value);

	/**
	 * The table column <code>PUBLIC.T_BOOK.REC_VERSION</code>
	 */
	public java.lang.Integer getRecVersion();

	/**
	 * The table column <code>PUBLIC.T_BOOK.REC_TIMESTAMP</code>
	 */
	public void setRecTimestamp(java.sql.Timestamp value);

	/**
	 * The table column <code>PUBLIC.T_BOOK.REC_TIMESTAMP</code>
	 */
	public java.sql.Timestamp getRecTimestamp();
}