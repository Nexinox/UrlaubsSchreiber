package eu.gemtec.UrlaubsSchreiber.VIEW;

import java.text.SimpleDateFormat;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.ServletException;
import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.access.JaasAccessControl;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import eu.gemtec.UrlaubsSchreiber.DB.IEntryDataacessService;
import eu.gemtec.UrlaubsSchreiber.PDF.IPDFService;

@CDIView(FelixView.VIEW_NAME)
@RolesAllowed("felix")
@Theme("mainTheme")
public class FelixView extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "FELIX_VIEW";

	@Inject
	private IEntryDataacessService entryDataacessService;
	@Inject
	private IPDFService pdfService;

	private int tableIndex = 0;
	private Button logout;
	private Button back;
	private Button oldEntys;

	@Override
	public void enter(ViewChangeEvent event) {
		logout = new Button("Logout");
		back = new Button("Zurück");
		oldEntys = new Button("Old");
		logout.addClickListener(e -> {
			try {
				JaasAccessControl.getCurrentRequest().logout();
			} catch (ServletException e1) {
				e1.printStackTrace();
			}
			getUI().getNavigator().navigateTo(LoginView.VIEW_NAME);
		});
		back.addClickListener(e -> {
			getUI().getNavigator().navigateTo(NormieView.VIEW_NAME);
		});
		oldEntys.addClickListener(e->{
			UI.getCurrent().getNavigator().navigateTo(OldFelixView.VIEW_NAME);
		});

		Table table = new Table();
		table.addContainerProperty("id", Long.class, null);
		table.addContainerProperty("Vorname", String.class, null);
		table.addContainerProperty("Nachname", String.class, null);
		table.addContainerProperty("Start", String.class, null);
		table.addContainerProperty("End", String.class, null);
		table.addContainerProperty("Opt", HorizontalLayout.class, null);

		entryDataacessService.getEntryList().forEach(e -> {
			if (!e.isSeen()) {
				tableIndex++;
				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

				Button download = new Button("PDF");
				download.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 1L;

					@SuppressWarnings("deprecation")
					@Override
					public void buttonClick(ClickEvent event) {
						e.setSeen(true);
						entryDataacessService.updateEntry(e);
						Page.getCurrent().open(pdfService.fillFelixPDF(e.getVorname(), e.getNachname(), e.getStart(),
						e.getEnd()), "down", false);

					}
				});
				table.addItem(new Object[] { e.getId(), e.getVorname(), e.getNachname(), format.format(e.getStart()),
						format.format(e.getEnd()), new HorizontalLayout(download) }, tableIndex);
			}
		});

		addComponents(new VerticalLayout(oldEntys, back, logout), table);

	}

}
