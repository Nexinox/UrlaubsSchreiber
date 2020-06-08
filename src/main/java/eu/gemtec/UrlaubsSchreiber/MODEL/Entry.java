package eu.gemtec.UrlaubsSchreiber.MODEL;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Entry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String vorname;
	private String nachname;
	@Column(name="start_date")
	private Date start;
	@Column(name="end_date")
	private Date end;
	private boolean seen;

	public Entry() {
		super();
	}
	/**
	 * @param vorname
	 * @param nachname
	 * @param start
	 * @param end
	 */
	public Entry(String vorname, String nachname, Date start, Date end) {
		super();
		this.vorname = vorname;
		this.nachname = nachname;
		this.start = start;
		this.end = end;
		this.seen = false;
	}
	public String getVorname() {
		return vorname;
	}
	
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public Long getId() {
		return id;
	}
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	public boolean isSeen() {
		return seen;
	}
	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	

}
