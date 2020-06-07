package eu.gemtec.UrlaubsSchreiber.VIEW;

import javax.servlet.ServletException;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.access.JaasAccessControl;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.Notification;
import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;

@CDIView("LOGIN_VIEW")
public class LoginView extends LoginForm implements View{
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "LOGIN_VIEW";

	@Override
	public void enter(ViewChangeEvent event) {
		final String parameters = event.getParameters();
        addLoginListener(loginEvent -> onLogin(loginEvent, parameters));
	}
	
	private void onLogin(LoginEvent loginEvent, final String parameters) {
        final String username = loginEvent.getLoginParameter("username");
        final String password = loginEvent.getLoginParameter("password");

        try {
            JaasAccessControl.login(username, password);
            getUI().getNavigator().navigateTo(NormieView.VIEW_NAME);

        } catch (ServletException e) {
            Notification.show("Login failed", ERROR_MESSAGE);
        }
    }

}
