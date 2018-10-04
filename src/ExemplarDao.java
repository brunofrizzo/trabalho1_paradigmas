import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;
import java.util.Scanner;

public class ExemplarDao extends Exemplar{

    public Exemplar idJaCadastrado(Integer id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("livro");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Exemplar exemplar = em.find(Exemplar.class,id);
        em.getTransaction().commit();
        emf.close();
        return exemplar;
    }

    public void save(Exemplar exemplar) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("livro");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.merge(exemplar);
        em.getTransaction().commit();
        emf.close();
    }

    public void deletaByLivro(Livro l) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("livro");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Query consulta = em.createQuery("select exemplar from Exemplar exemplar");
        List<Exemplar> exemplares = consulta.getResultList();
        em.getTransaction().commit();
        emf.close();

        for (Exemplar exemplar:exemplares) {
            if (exemplar.getLivro().equals(l)) {
                em.getTransaction().begin();
                Exemplar ex = em.merge(exemplar);
                em.remove(ex);
                em.getTransaction().commit();
                emf.close();
            }
        }
    }

    public int verificaDisponibilidadeExemplares(Livro l) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("livro");
        EntityManager em = emf.createEntityManager();
        int a = 0;

        em.getTransaction().begin();
        Query consulta = em.createQuery("select exemplar from Exemplar exemplar");
        List<Exemplar> exemplares = consulta.getResultList();
        em.getTransaction().commit();
        emf.close();

        for (Exemplar exemplar:exemplares) {
            if (exemplar.getLivro().getIsbn().equals(l.getIsbn()) && exemplar.getDisponivel() && !exemplar.getReservado())
                a=1;
        }
        return a;
    }

    public Exemplar exemplarDisponivelParaReserva(Livro l) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("livro");
        EntityManager em = emf.createEntityManager();
        Exemplar ex = null;

        em.getTransaction().begin();
        Query consulta = em.createQuery("select exemplar from Exemplar exemplar");
        List<Exemplar> exemplares = consulta.getResultList();
        em.getTransaction().commit();
        emf.close();

        for (Exemplar exemplar:exemplares) {
            if (exemplar.getLivro().getIsbn().equals(l.getIsbn()) && !exemplar.getReservado() && exemplar.getDisponivel())
                ex=exemplar;
        }

        if (ex == null) {
            for (Exemplar exemplar:exemplares) {
                if (exemplar.getLivro().getIsbn().equals(l.getIsbn()) && !exemplar.getReservado())
                    ex=exemplar;
            }
        }

        return ex;
    }

    public void resetar() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("livro");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Query consulta = em.createQuery("select exemplar from Exemplar exemplar");
        List<Exemplar> exemplares = consulta.getResultList();
        em.getTransaction().commit();

        for (Exemplar ex:exemplares) {
            ex.setDisponivel(true);
            ex.setReservado(false);
            em.getTransaction().begin();
            em.merge(ex);
            em.getTransaction().commit();
        }
        emf.close();
    }

}
