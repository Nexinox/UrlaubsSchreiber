package eu.gemtec.UrlaubsSchreiber.DB;

import java.util.List;

import eu.gemtec.UrlaubsSchreiber.MODEL.Entry;

public interface IEntryDataacessService {
	
	public List<Entry> getEntryList();
	public Entry getEntryById(long id);
	public void newEntry(Entry entry);

}
