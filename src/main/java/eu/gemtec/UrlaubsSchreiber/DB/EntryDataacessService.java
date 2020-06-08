package eu.gemtec.UrlaubsSchreiber.DB;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import eu.gemtec.UrlaubsSchreiber.MODEL.Entry;

@SessionScoped
public class EntryDataacessService implements Serializable, IEntryDataacessService{
	private static final long serialVersionUID = 1L;
	private EntityManager EntrysEm;
    private List<Entry> entryList;
	
	@SuppressWarnings("unchecked")
	public EntryDataacessService() {
		EntrysEm = Persistence.createEntityManagerFactory("entrys").createEntityManager();
	    entryList = EntrysEm.createQuery("select e from Entry e").getResultList();
	}
	
	public List<Entry> getEntryList() {
		return entryList;
	}
	public Entry getEntryById(long id) {
		for (Entry e : entryList) {
			if (e.getId() == id) {
				return e;
			}
		}
		return null;	
	}
	public void newEntry(Entry entry) {
	    this.entryList.add(entry);
	    EntrysEm.getTransaction().begin();
	    EntrysEm.persist(entry);
	    EntrysEm.getTransaction().commit();
	}

	@Override
	public void updateEntry(Entry e) {
		Entry tempE = EntrysEm.find(e.getClass(), e.getId());
		EntrysEm.getTransaction().begin();
		tempE = e;
		EntrysEm.getTransaction().commit();
	}
}
