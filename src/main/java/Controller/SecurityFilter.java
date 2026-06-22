package Controller;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "SecurityFilter", urlPatterns = {"*.xhtml"})
public class SecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String loginURI = req.getContextPath() + "/index.xhtml";

        boolean loggedIn = false;
        if (session != null) {
            ManagerUsuario manager = (ManagerUsuario) session.getAttribute("ManagerUsuario");
            if (manager != null && manager.getUsuarioSeleccionado() != null) {
                loggedIn = true;
            }
        }

        // Si la petición es para el login, se permite siempre
        boolean loginRequest = req.getRequestURI().equals(loginURI) 
                || req.getRequestURI().equals(req.getContextPath() + "/");
        
        // Permitir archivos CSS, JS e imágenes del sistema
        boolean resourceRequest = req.getRequestURI().startsWith(req.getContextPath() + "/jakarta.faces.resource")
                || req.getRequestURI().startsWith(req.getContextPath() + "/recursos-web");

        if (loggedIn || loginRequest || resourceRequest) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(loginURI);
        }
    }

    @Override
    public void destroy() {
    }
}
