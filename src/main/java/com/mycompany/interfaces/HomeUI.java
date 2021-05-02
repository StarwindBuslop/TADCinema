package com.mycompany.interfaces;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mycompany.components.Navegacion;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;

@Theme("mytheme")
@PreserveOnRefresh
public class HomeUI extends UI {
    
    @Override
    protected void init(VaadinRequest request) {
        final WrappedSession session = getSession().getSession();
        final VerticalLayout rootLayout = new VerticalLayout();
        final Button btnLogout = new Button("Cerrar sesión");
        
        // comprueba si se ha iniciado sesión
        comprobarSesion(rootLayout, session);
        
        // invalida la sesion y redirecciona a login
        btnLogout.addClickListener(e -> {
            session.invalidate();
            Page.getCurrent().setLocation("/");
        });
        
        // panel de navegación
        final Navegacion navbar = new Navegacion();
        
        // prueba cartelera---------------------
        final Panel carteleraPanel = new Panel();
        
        BBDD bbdd = null;
        try {
            bbdd = new BBDD("movies");
        } catch (UnknownHostException ex) {
            Logger.getLogger(HomeUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        final DBCollection movies = bbdd.getColeccion();
        cargarPeliculas(movies, carteleraPanel);
        
        //--------------------------------------
        
        // ESTRUCTURA DE LA INTERFAZ
        rootLayout.addComponents(btnLogout, navbar, carteleraPanel);
        
        rootLayout.setMargin(true);
        rootLayout.setSpacing(true);
        
        setContent(rootLayout);
    }
 
    @WebServlet(urlPatterns = "/home/*", name = "HomeUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = HomeUI.class, productionMode = false)
    public static class HomeUIServlet extends VaadinServlet {
    }
    
    /**
     * Método encargado de comprobar si la sesión existe o no
     * Si no existe, redirecciona al login
     */
    private static void comprobarSesion(final VerticalLayout rootLayout, final WrappedSession session) {
        if(session.getAttribute("usuario") == null){
            Page.getCurrent().setLocation("/");
        } else {
            final Label bienvenido = new Label("Bienvenido, " + session.getAttribute("usuario"));
            rootLayout.addComponent(bienvenido);
        }
    }
    
    private static void cargarPeliculas(final DBCollection movies, final Panel panel) {
        final DBCursor cursor = movies.find();
        
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        
        DBObject movie = null;
        while(cursor.hasNext()) {
            movie = cursor.next();
            final Panel panelInner = new Panel(movie.get("name").toString());
            final VerticalLayout layoutInner = new VerticalLayout();
            final Label titulo = new Label("<strong>Título:</strong> " + movie.get("name"), ContentMode.HTML);
            final Label sala = new Label("<strong>Sala:</strong> " + movie.get("numSala"), ContentMode.HTML);
            final Label idioma = new Label("<strong>Idioma:</strong> " + movie.get("type"), ContentMode.HTML);
            Double anyo = (Double) movie.get("year");
            final Label year = new Label("<strong>Año:</strong> " + anyo.intValue(), ContentMode.HTML);
            final Label director = new Label("<strong>Director:</strong> " + movie.get("director"), ContentMode.HTML);
            Double time = (Double) movie.get("time");
            final Label duracion = new Label("<strong>Duración:</strong> " + time.intValue() + " minutos", ContentMode.HTML);
            layoutInner.addComponents(titulo, sala, idioma, year, director, duracion);
            layoutInner.setMargin(true);
            layoutInner.setSpacing(true);
            panelInner.setContent(layoutInner);
            layout.addComponent(panelInner);
        }
        
        panel.setContent(layout);
    }
    
}