package com.mycompany.interfaces;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Theme("mytheme")
public class LoginUI extends UI {

    // Nombre de clase de la interfaz completa
    public static final String CLASSNAME = "login";

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        // obtengo la sesion
        final WrappedSession session = getSession().getSession();
        
        // Layout general con css
        final CssLayout layout = new CssLayout();
        layout.setStyleName(CLASSNAME);

        // Layouts, panel y el label de información
        final VerticalLayout verticalLayout = new VerticalLayout();
        final Label labelInfo = new Label("Inicia sesión para entrar a las funcionalidades de la aplicación");
        final Panel loginPanel = new Panel("Inicio de sesión");
        final FormLayout form = new FormLayout();

        // Campos usuario y contraseña
        final TextField username = new TextField("Usuario");
        final PasswordField password = new PasswordField("Contraseña");

        // Layout simulando un div inline
        final HorizontalLayout divButtons = new HorizontalLayout();

        // Botón de inicio de sesión
        final Button btnLogin = new Button("Iniciar sesión");
        btnLogin.setStyleName("primary");

        // Botón de registro
        final Button btnRegister = new Button("Regístrate");

        divButtons.addComponents(btnLogin, btnRegister);
        divButtons.setSpacing(true);

        //
        btnLogin.addClickListener(e -> {
            if (camposValidos(username, password)) {

                MongoClient mongoClient;
                try {
                    mongoClient = new MongoClient("localhost", 27017);
                    DB db = mongoClient.getDB("TADCinemaDB");
                    if (existeUsuario(username, password, db)) {
                        session.setAttribute("usuario", username.getValue());
                        Page.getCurrent().setLocation("/home");
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(LoginUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        // redireccion a registro al pulsar el boton
        btnRegister.addClickListener(e -> {
            Page.getCurrent().setLocation("/registro");
        });

        // Se añaden los componentes al formulario
        form.addComponents(username, password, divButtons);
        form.setSpacing(true);
        form.setMargin(true);
        form.setStyleName(CLASSNAME + "-form");
        
        loginPanel.setContent(form);
        loginPanel.setWidth("440px");

        verticalLayout.addComponents(labelInfo, loginPanel);
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);

        layout.addComponent(verticalLayout);

        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "LoginUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = LoginUI.class, productionMode = false)
    public static class LoginUIServlet extends VaadinServlet {
    }

    /**
     * Método encargado de comprobar si existe el usuario en base de datos
     * @param username nombre de usuario
     * @param password contraseña
     * @param db base de datos
     * @return TRUE/FALSE
     */
    public static boolean existeUsuario(TextField username, PasswordField password,
            DB db) {
        boolean existe = false;

        // obtengo la colección de los usuarios
        DBCollection usuarios = db.getCollection("usuarios");

        // cursor para iterar la lista de usuarios
        final DBCursor cursor = usuarios.find();

        DBObject usuario;
        // recorre la lista y si lo encuentra, sale del bucle
        while (cursor.hasNext()) {
            usuario = cursor.next();
            if (usuario.get("username").equals(username.getValue())) {
                if (usuario.get("contraseña").equals(password.getValue())) {
                    existe = true;
                    break;
                } else {
                    Notification.show("Error", "Contraseña no válida", Notification.Type.ERROR_MESSAGE);
                }
            } else {
                Notification.show("Error", "Usuario no válido", Notification.Type.ERROR_MESSAGE);
            }
        }

        return existe;

    }

    /**
     * Método encargado de comprobar que los campos sean válidos
     * @param username campo de usuario
     * @param password campo de contraseña
     * @return TRUE/FALSE
     */
    public static boolean camposValidos(TextField username, PasswordField password) {
        boolean esCorrecto = true;

        List<String> errores = new ArrayList<>();

        if (username.getValue() == "") {
            esCorrecto = false;
            errores.add("El campo 'Usuario' es obligatorio.");
        }
        if (password.getValue() == "") {
            esCorrecto = false;
            errores.add("El campo 'Contraseña' es obligatorio.");
        }

        if (!esCorrecto) {
            String salidaError = "";
            for (String error : errores) {
                salidaError += error + "\n";
            }
            Notification notification = new Notification("Error", salidaError, Notification.Type.ERROR_MESSAGE);
            notification.show(Page.getCurrent());
        }

        return esCorrecto;
    }
}
