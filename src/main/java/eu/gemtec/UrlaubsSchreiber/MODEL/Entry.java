package eu.gemtec.UrlaubsSchreiber.MODEL;

import java.util.Calendar;

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
	private Calendar start;
	@Column(name="end_date")
	private Calendar end;
	

	public Entry() {
		super();
	}
	/**
	 * @param vorname
	 * @param nachname
	 * @param start
	 * @param end
	 */
	public Entry(String vorname, String nachname, Calendar start, Calendar end) {
		super();
		this.vorname = vorname;
		this.nachname = nachname;
		this.start = start;
		this.end = end;
	}
	public String getVorname() {
		return vorname;
	}
	
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public Calendar getStart() {
		return start;
	}
	public void setStart(Calendar start) {
		this.start = start;
	}
	public Calendar getEnd() {
		return end;
	}
	public void setEnd(Calendar end) {
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
	

}
