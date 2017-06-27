package com.joshmr94.librarium.session;

import com.joshmr94.librarium.model.Libro;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author joshmr94
 */
public class LibroSessionTest {
    
    static LibroSession libroSession;
    
    public LibroSessionTest() {
    }
    
    @Test
    public void findLibrosTest(){
        libroSession = new LibroSession();
        List<Libro> libros = libroSession.findLibros();
        for (int i = 0; i < libros.size(); i++) {
            System.out.println(libros.get(i).getTitulo());
        }
    }
    
    @Test
    public void findLibroByIdTest(){
        libroSession = new LibroSession();
        Libro libro = libroSession.findLibroById(1L);
        System.out.println(libro.getTitulo());
    }
    
}
