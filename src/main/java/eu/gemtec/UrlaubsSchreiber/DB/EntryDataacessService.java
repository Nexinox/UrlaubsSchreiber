package eu.gemtec.UrlaubsSchreiber.DB;

import java.io.Serializable;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.SessionScoped;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import eu.gemtec.UrlaubsSchreiber.MODEL.Entry;

@SessionScoped
public class EntryDataacessService implements Serializable, IEntryDataacessService{
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName ="entrys")
	private EntityManager EntrysEm;
    private List<Entry> entryList;
	
	@SuppressWarnings("unchecked")
	public EntryDataacessService() {
		try {
			Context context = new InitialContext();
			EntrysEm = (EntityManager) context.lookup("java:/EntityManager");
			entryList = EntrysEm.createQuery("select e from Entry e").getResultList();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		    

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
	@Transactional
	public void newEntry(Entry entry) {
	    this.entryList.add(entry);
	    EntrysEm.persist(entry);
	}

	@Override
	@Transactional
	public void updateEntry(Entry e) {
		@SuppressWarnings("unused")
		Entry tempE = EntrysEm.find(e.getClass(), e.getId());
		tempE = e;
		
	}
}
