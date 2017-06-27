package com.joshmr94.librarium.session;

import com.joshmr94.librarium.model.Autor;
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
public class AutorSessionTest {
    
    static AutorSession autorSession;
    
    public AutorSessionTest() {
    }
    
    @Test
    public void findAutoresTest(){
        autorSession = new AutorSession();
        List<Autor> autores = autorSession.findAutores();
        for (int i = 0; i < autores.size(); i++) {
            System.out.println(autores.get(i).getNombre());
        }
    }
    
    @Test
    public void findAutorById(){
        autorSession = new AutorSession();
        Autor autor = autorSession.findAutorById(1L);
        System.out.println(autor.getNombre());
    }
    
}
