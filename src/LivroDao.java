import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

public class LivroDao extends Livro{

    public Livro buscaByISBN(Long isbn) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("livro");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Livro livro = em.find(Livro.class,isbn);
        em.getTransaction().commit();
        emf.close();
        return livro;
    }

    public void cadastra() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("livro");
        EntityManager em = emf.createEntityManager();

        Livro livro = new Livro();
        Scanner entrada = new Scanner(System.in);
        BufferedReader scannerString = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("ISBN: ");
            livro.setIsbn(entrada.nextLong());
            Livro livroBd = buscaByISBN(livro.getIsbn());
            while (livroBd != null) {
                System.out.println("Livro ja cadastrado. Informe outro ISBN: ");
                livro.setIsbn(entrada.nextLong());
                livroBd = buscaByISBN(livro.getIsbn());
            }
            System.out.println("Titulo: ");
            livro.setTitulo(scannerString.readLine());
            System.out.println("Editora: ");
            livro.setEditora(scannerString.readLine());
            System.out.println("Autor: ");
            livro.setAutor(scannerString.readLine());
            System.out.println("Edição: ");
            livro.setEdicao(entrada.nextInt());
            System.out.println("Ano de publicação: ");
            livro.setAno(entrada.nextInt());
            System.out.println("Quantos exemplares deseja cadastrar? ");
            Integer num_exemplares = entrada.nextInt();
            Integer j;
            Exemplar exemplar_novo = new Exemplar();
            ExemplarDao exemplar_dao = new ExemplarDao();
            for (int i=0;i<num_exemplares;i++){
                j = i+1;
                System.out.println("Informe o ID do "+ j +"° exemplar: ");
                Integer id = entrada.nextInt();
                Exemplar exemplar = exemplar_dao.idJaCadastrado(id);
                while (exemplar != null) {
                    System.out.println("ID ja cadastrado. Informe novamente: ");
                    id = entrada.nextInt();
                    exemplar = exemplar_dao.idJaCadastrado(id);
                }
                exemplar_novo.setId(id);
                exemplar_novo.setLivro(livro);
                exemplar_novo.setDisponivel(true);
                exemplar_novo.setReservado(false);
                livro.getExemplares().add(exemplar);
                em.getTransaction().begin();
                em.merge(livro);
                em.getTransaction().commit();
                exemplar_dao.save(exemplar_novo);
            }
            System.out.println("Livro cadastrado com sucesso!");
            emf.close();
        }catch (IOException ie) {
            cadastra();
        }
    }

    public void listaTodos() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("livro");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Query consulta = em.createQuery("select livro from Livro livro");
        List<Livro> livros = consulta.getResultList();
        em.getTransaction().commit();
        emf.close();
        System.out.println("LIVROS CADASTRADOS");
        for (Livro l:livros) {
            System.out.println("-------------------------");
            System.out.println("Título: " + l.getTitulo());
            System.out.println("ISBN: " + l.getIsbn());
            System.out.println("Editora: " + l.getEditora());
            System.out.println("Edição: " + l.getEdicao());
            System.out.println("Ano de publicação: " + l.getAno());
            System.out.println("Exemplares: ");
            for (Exemplar exemplar:l.getExemplares()) {
                System.out.print("ID: " + exemplar.getId() + " | ");
                if (exemplar.getDisponivel())
                    System.out.print("Disponível: Sim | ");
                else
                    System.out.print("Disponível: Não | ");
                if (exemplar.getReservado())
                    System.out.println("Reservado: Sim");
                else
                    System.out.println("Reservado: Não");
            }
        }
    }

    public void altera() {
        BufferedReader scannerString = new BufferedReader(new InputStreamReader(System.in));
        Scanner entrada = new Scanner(System.in);
        Long isbn;
        Livro livro;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("livro");
        EntityManager em = emf.createEntityManager();
        System.out.println("Informe o ISBN do livro que deseja alterar:");
        isbn = entrada.nextLong();
        livro = buscaByISBN(isbn);
        while (livro == null) {
            System.out.println("ISBN não cadastrado. Informe novamente: ");
            isbn = entrada.nextLong();
            livro = buscaByISBN(isbn);
        }
        System.out.println("ALterando livro de ISBN -" +livro.getIsbn() + "-");
        try{
            System.out.println("Titulo: ");
            livro.setTitulo(scannerString.readLine());
            System.out.println("Editora: ");
            livro.setEditora(scannerString.readLine());
            System.out.println("Autor: ");
            livro.setAutor(scannerString.readLine());
            System.out.println("Edição: ");
            livro.setEdicao(entrada.nextInt());
            System.out.println("Ano de publicação: ");
            livro.setAno(entrada.nextInt());
        }catch(IOException ie) {
            ie.printStackTrace();
        }
        em.getTransaction().begin();
        em.merge(livro);
        em.getTransaction().commit();
        em.close();
        System.out.println("Livro alterado com sucesso");
    }

    public void buscaISBN() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("livro");
        EntityManager em = emf.createEntityManager();
        Scanner entrada = new Scanner(System.in);
        Long isbn;
        System.out.println("Informe o ISBN:");
        isbn = entrada.nextLong();
        em.getTransaction().begin();
        Query consulta = em.createQuery("select livro from Livro livro where livro.isbn = "+ isbn);
        List<Livro> livros = consulta.getResultList();
        em.getTransaction().commit();
        emf.close();
        if (livros.isEmpty()){
            System.out.println("Nenhum livro encontrado. ");
        }else{
            System.out.println("LIVRO ENCONTRADO: ");
            for (Livro l:livros) {
                System.out.println("-------------------------");
                System.out.println("Título: " + l.getTitulo());
                System.out.println("Editora: " + l.getEditora());
                System.out.println("Edição: " + l.getEdicao());
                System.out.println("Ano de publicação: " + l.getAno());
                System.out.println("Exemplares: ");
                for (Exemplar exemplar:l.getExemplares()) {
                    System.out.print("ID: " + exemplar.getId() + " | ");
                    if (exemplar.getDisponivel() == true)
                        System.out.println("Disponível: Sim");
                    else
                        System.out.println("Disponível: Não");
                    if (exemplar.getReservado())
                        System.out.println("Reservado: Sim");
                    else
                        System.out.println("Reservado: Não");
                }
            }
        }
    }

    public void buscaTitulo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("livro");
        EntityManager em = emf.createEntityManager();
        BufferedReader scannerString = new BufferedReader(new InputStreamReader(System.in));
        String titulo;
        try {
            System.out.println("Informe o título:");
            titulo = scannerString.readLine();
            em.getTransaction().begin();
            Query consulta = em.createQuery("select livro from Livro livro where livro.titulo = '"+ titulo + "'");
            List<Livro> livros = consulta.getResultList();
            em.getTransaction().commit();
            emf.close();
            if (livros.isEmpty()){
                System.out.println("Nenhum livro encontrado. ");
            }else{
                System.out.println("LIVROS ENCONTRADOS: ");
                for (Livro l:livros) {
                    System.out.println("-------------------------");
                    System.out.println("Título: " + l.getTitulo());
                    System.out.println("ISBN: " + l.getIsbn());
                    System.out.println("Editora: " + l.getEditora());
                    System.out.println("Edição: " + l.getEdicao());
                    System.out.println("Ano de publicação: " + l.getAno());
                    System.out.println("Exemplares: ");
                    for (Exemplar exemplar:l.getExemplares()) {
                        System.out.print("ID: " + exemplar.getId() + " | ");
                        if (exemplar.getDisponivel())
                            System.out.println("Disponível: Sim");
                        else
                            System.out.println("Disponível: Não");
                        if (exemplar.getReservado())
                            System.out.println("Reservado: Sim");
                        else
                            System.out.println("Reservado: Não");
                    }
                }
            }
        } catch(IOException ie) {
            ie.printStackTrace();
        }
    }

    public void buscaEditora() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("livro");
        EntityManager em = emf.createEntityManager();
        BufferedReader scannerString = new BufferedReader(new InputStreamReader(System.in));
        String titulo;
        try{
            System.out.println("Informe a editora:");
            String editora = scannerString.readLine();
            em.getTransaction().begin();
            Query consulta = em.createQuery("select livro from Livro livro where livro.editora = '"+ editora + "'");
            List<Livro> livros = consulta.getResultList();
            em.getTransaction().commit();
            emf.close();
            if (livros.isEmpty()){
                System.out.println("Nenhum livro encontrado. ");
            }else{
                System.out.println("LIVROS ENCONTRADOS: ");
                for (Livro l:livros) {
                    System.out.println("-------------------------");
                    System.out.println("Título: " + l.getTitulo());
                    System.out.println("ISBN: " + l.getIsbn());
                    System.out.println("Editora: " + l.getEditora());
                    System.out.println("Edição: " + l.getEdicao());
                    System.out.println("Ano de publicação: " + l.getAno());
                    System.out.println("Exemplares: ");
                    for (Exemplar exemplar:l.getExemplares()) {
                        System.out.print("ID: " + exemplar.getId() + " | ");
                        if (exemplar.getDisponivel())
                            System.out.println("Disponível: Sim");
                        else
                            System.out.println("Disponível: Não");
                        if (exemplar.getReservado())
                            System.out.println("Reservado: Sim");
                        else
                            System.out.println("Reservado: Não");
                    }
                }
            }
        }catch(IOException ie) {
            ie.printStackTrace();
        }
    }

    public void listaExemplares(Long isbn) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("livro");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Query consulta = em.createQuery("select livro from Livro livro where livro.isbn = "+ isbn);
        List<Livro> livros = consulta.getResultList();
        em.getTransaction().commit();
        emf.close();
        if (livros.isEmpty()){
            System.out.println("Nenhum exemplar encontrado. ");
        }else{
            System.out.println("EXEMPLARES ENCONTRADO: ");
            for (Livro l:livros) {
                for (Exemplar exemplar:l.getExemplares()) {
                    System.out.print("ID: " + exemplar.getId() + " | ");
                    if (exemplar.getDisponivel())
                        System.out.print("Disponível: Sim | ");
                    else
                        System.out.print("Disponível: Não | ");
                    if (exemplar.getReservado())
                        System.out.println("Reservado: Sim");
                    else
                        System.out.println("Reservado: Não");
                }
            }
        }
    }

    public Integer nenhumLivroCadastrado() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("livro");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Query consulta = em.createQuery("select livro from Livro livro");
        List<Livro> livros = consulta.getResultList();
        em.getTransaction().commit();
        emf.close();
        if (livros.isEmpty())
            return 1;
        else
            return 0;
    }

    public void deleta() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("livro");
        EntityManager em = emf.createEntityManager();
        Scanner scanner = new Scanner(System.in);
        ExemplarDao exemplar_dao = new ExemplarDao();

        try{
            System.out.println("Informe o ISBN do livro que deseja excluir: ");
            Long isbn = scanner.nextLong();
            while (this.buscaByISBN(isbn) == null) {
                System.out.println("ISBN invalido. Informe novamente: ");
                isbn = scanner.nextLong();
            }
            Livro l = buscaByISBN(isbn);
            exemplar_dao.deletaByLivro(l);

            em.getTransaction().begin();
            Livro livro = em.merge(l);
            em.remove(livro);
            em.getTransaction().commit();
            emf.close();
        }catch(Exception ie) {
            this.deleta();
        }
    }

}
