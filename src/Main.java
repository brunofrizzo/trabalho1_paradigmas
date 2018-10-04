import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;

import javax.persistence.*;

public class Main {

    public static void main(String[] args) {
        ExemplarDao exemplar_dao = new ExemplarDao();
        exemplar_dao.resetar();

        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        ArrayList<Emprestimo> emprestimos = new ArrayList<Emprestimo>();
        ArrayList<Reserva> reservas = new ArrayList<Reserva>();
        Scanner entrada = new Scanner(System.in);
        Integer opcao = lerOpcao();

        while (opcao > 0) {

            if (opcao == 1) { //Livros
                LivroDao livro_dao = new LivroDao();
                System.out.println("1-Incluir");
                System.out.println("2-Alterar");
                System.out.println("3-Excluir");
                System.out.println("4-Listar");
                System.out.println("5-Buscar por ISBN");
                System.out.println("6-Buscar por título");
                System.out.println("7-Buscar pela editora");
                System.out.println("0-Sair");
                System.out.println("Informe uma opção: ");
                opcao = entrada.nextInt();
                if (opcao == 1) {
                    livro_dao.cadastra();
                }else if(opcao == 2) {
                    livro_dao.altera();
                }else if(opcao == 3) {
                    livro_dao.deleta();
                }else if(opcao == 4) {
                    livro_dao.listaTodos();
                }else if(opcao == 5) {
                    livro_dao.buscaISBN();
                }else if(opcao == 6) {
                    livro_dao.buscaTitulo();
                }else if(opcao == 7) {
                    livro_dao.buscaEditora();
                }
            }else if (opcao == 2) { //Usuarios
                System.out.println("1-Incluir");
                System.out.println("2-Excluir");
                System.out.println("3-Listar");
                System.out.println("4-Buscar por nome");
                System.out.println("5-Buscar por login");
                System.out.println("0-Sair");
                opcao = entrada.nextInt();
                while(opcao < 0 || opcao > 5) {
                    System.out.println("Opcao invalida. Informe novamente: ");
                    opcao = entrada.nextInt();
                }
                if (opcao == 1) {
                    usuarios.add(cadastroUsuario(usuarios));
                }else if(opcao == 2) {
                    deletaUsuario(usuarios);
                }else if(opcao == 3) {
                    listaUsuarios(usuarios);
                }else if(opcao == 4) {
                    buscaUsuarioNome(usuarios);
                }else if(opcao == 5) {
                    buscaUsuarioLogin(usuarios);
                }
            }else if (opcao == 3) { //Emprestimos
                System.out.println("1-Efetuar empréstimo");
                System.out.println("2-Renovar empréstimo");
                System.out.println("3-Devolver livro");
                System.out.println("4-Listar");
                System.out.println("5-Buscar emprestimo por livro");
                System.out.println("6-Buscar emprestimo por exemplar de livro");
                System.out.println("7-Buscar emprestimo por usuario");
                System.out.println("0-Sair");
                opcao = entrada.nextInt();
                while(opcao < 0 || opcao > 7) {
                    System.out.println("Opcao invalida. Informe novamente: ");
                    opcao = entrada.nextInt();
                }
                if (opcao == 1) {
                    Emprestimo emp = cadastroEmprestimo(emprestimos, usuarios);
                    if (emp != null){
                        emprestimos.add(emp);
                    }
                }else if(opcao == 2) {
                    renovarEmprestimo(emprestimos, usuarios);
                }else if(opcao == 3) {
                    devolverLivro(emprestimos, usuarios);
                }else if(opcao == 4) {
                    listaEmprestimos(emprestimos);
                }else if(opcao == 5) {
                    buscaEmprestimoLivro(emprestimos);
                }else if(opcao == 6) {
                    buscaEmprestimoExemplar(emprestimos);
                }else if(opcao == 7) {
                    buscaEmprestimoUsuario(emprestimos, usuarios);
                }
            }else if (opcao == 4) {  //Reservas
                System.out.println("1-Efetuar reserva");
                System.out.println("2-Cancelar reserva");
                System.out.println("0-Sair");
                opcao = entrada.nextInt();
                while (opcao < 0 || opcao > 2) {
                    System.out.println("Opcao invalida. Informe novamente: ");
                    opcao = entrada.nextInt();
                }
                if (opcao == 1) {
                    reservas.add(cadastroReserva(usuarios));
                } else if (opcao == 2) {
                    cancelaReserva(reservas, usuarios);
                }
            }else if (opcao == 5) { //Multas
                pagarMulta(emprestimos, usuarios);
            }else{
                System.out.println("Opção invalida. Informe novamente");
            }

            opcao = lerOpcao();
        }

    }

    public static void menu() {
        System.out.println("");
        System.out.println("MENU PRINCIPAL");
        System.out.println("1-Livros");
        System.out.println("2-Usuários");
        System.out.println("3-Empréstimos");
        System.out.println("4-Reservas");
        System.out.println("5-Pagar multa");
        System.out.println("0-Sair");
    }

    public static Integer lerOpcao() {
        Scanner entrada = new Scanner(System.in);
        menu();
        System.out.println("Informe uma opção: ");
        Integer op = entrada.nextInt();
        while (op < 0 || op > 5){
            System.out.println("Opcao invalida. Informe novamente ");
            op = entrada.nextInt();
        }
        return op;
    }

    public static DateTime lerData() {
        DateTime date_time = new DateTime();
        BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
        try {
            String data_string = sc.readLine();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse(data_string);
            date_time = new DateTime(date);
        }catch(Exception ie) {
            return lerData();
        }
        return date_time;
    }

    public static DateTime dataAtual() {
        Date date = new Date();
        DateTime date_time = new DateTime(date);
        return date_time;
    }

    public static String converteData(DateTime datetime) {
        Date date = datetime.toDate();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String data = formatter.format(date);
        return data;
    }

    public static boolean usuarioComMulta(ArrayList<Usuario> usuarios, String login) {
        for (Usuario user:usuarios) {
            if (user.getLogin().equals(login) && user.getMulta())
                    return true;
        }
        return false;
    }

    public static Integer usuarioJaCadastrado(ArrayList<Usuario> usuarios, String login) {
        Integer a = 0;
        for (Usuario user:usuarios) {
            if (user.getLogin().equals(login))
                a = 1;
        }
        return a;
    }

    public static void listaUsuarios(ArrayList<Usuario> usuarios) {
        for (Usuario user:usuarios) {
            System.out.println("->" + user.getLogin());
        }
    }

    public static Usuario cadastroUsuario(ArrayList<Usuario> usuarios) {
        Usuario user = new Usuario();
        try{
            BufferedReader scannerString = new BufferedReader(new InputStreamReader(System.in));
            Scanner scanner = new Scanner(System.in);
            System.out.println("Nome: ");
            user.setNome(scannerString.readLine());
            while (user.getNome().equals("")) {
                System.out.println("Nome invalido. Informe novamente: ");
                user.setNome(scannerString.readLine());
            }
            System.out.println("Login: ");
            user.setLogin(scannerString.readLine());
            while (usuarioJaCadastrado(usuarios, user.getLogin()) == 1 || user.getLogin().equals("")) {
                System.out.println("Login ja cadastrado. Informe novamente: ");
                user.setLogin(scannerString.readLine());
            }
            System.out.println("Informe 1 se for professor ou 0 se for aluno: ");
            Integer prof = scanner.nextInt();
            while (prof < 0 || prof > 1) {
                System.out.println("Opcao invalida. Informe novamente.");
                prof = scanner.nextInt();
            }
            user.setProf(prof);
            System.out.println("Usuario cadastrado com sucesso!");
        } catch(IOException ie) {
            cadastroUsuario(usuarios);
        }
        return user;
    }

    public static void deletaUsuario(ArrayList<Usuario> usuarios) {
        try{
            BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Informe o login do usuario que deseja excluir: ");
            listaUsuarios(usuarios);
            String login = entrada.readLine();
            while (usuarioJaCadastrado(usuarios, login) == 0) {
                System.out.println("Usuario nao encontrado. Informe novamente o login: ");
                login = entrada.readLine();
            }
            for (Usuario user:usuarios) {
                if (user.getLogin().equals(login)) {
                    usuarios.remove(user);
                    System.out.println("Usuario removido com sucesso.");
                    break;
                }
            }
        } catch(IOException ie) {
            deletaUsuario(usuarios);
        }
    }

    public static void buscaUsuarioNome(ArrayList<Usuario> usuarios) {
        try{
            Usuario usuario = null;
            BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Informe o nome do usuario que deseja buscar: ");
            String nome = entrada.readLine();
            for (Usuario user:usuarios) {
                if (user.getNome().equals(nome)) {
                    usuario = user;
                }
            }
            if (usuario == null) {
                System.out.println("Usuário não encontrado.");
            }else{
                System.out.println("->Nome: " + usuario.getNome());
                System.out.println("->Login: " + usuario.getLogin());
            }
        } catch(IOException ie) {
            buscaUsuarioNome(usuarios);
        }
    }

    public static void buscaUsuarioLogin(ArrayList<Usuario> usuarios) {
        try{
            Usuario usuario = null;
            BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Informe o login do usuario que deseja buscar: ");
            String login = entrada.readLine();
            for (Usuario user:usuarios) {
                if (user.getLogin().equals(login)) {
                    usuario = user;
                }
            }
            if (usuario == null) {
                System.out.println("Usuário não encontrado.");
            }else{
                System.out.println("->Nome: " + usuario.getNome());
                System.out.println("->Login: " + usuario.getLogin());
            }
        } catch(IOException ie) {
            buscaUsuarioLogin(usuarios);
        }
    }

    public static Usuario buscaUsuarioByLogin(ArrayList<Usuario> usuarios, String login) {
        for (Usuario user:usuarios) {
            if (user.getLogin().equals(login))
                return user;
        }
        return null;
    }

    public static int atingiuLimiteEmprestimos(ArrayList<Emprestimo> emprestimos, Usuario user) {
        int cont=0, a=0;
        if (user.getProf()==1) {
            for (Emprestimo emp:emprestimos){
                if (emp.getUsuario() != null && emp.getUsuario().equals(user))
                    cont++;
            }
            if (user.getMulta()) {
                if (cont > 0)
                    a=1;
            }else{
                if (cont >= 5)
                    a=1;
            }
        }else{
            for (Emprestimo emp:emprestimos){
                if (emp.getUsuario() != null && emp.getUsuario().equals(user))
                    cont++;
            }
            if (user.getMulta()) {
                if (cont > 0)
                    a=1;
            }else{
                if (cont >= 3)
                    a=1;
            }
        }

        return a;
    }

    public static Emprestimo cadastroEmprestimo(ArrayList<Emprestimo> emprestimos, ArrayList<Usuario> usuarios) {
        LivroDao livro_dao = new LivroDao();
        Emprestimo emp = new Emprestimo();
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado. Impossível realizar emprestimo.");
            emp = null;
        }else if (livro_dao.nenhumLivroCadastrado() == 1) {
            System.out.println("Nenhum livro cadastrado. Impossível realizar emprestimo.");
            emp = null;
        }else{
            ExemplarDao exemplar_dao = new ExemplarDao();
            try{
                BufferedReader scannerString = new BufferedReader(new InputStreamReader(System.in));
                Scanner scanner = new Scanner(System.in);

                System.out.println("Informe o ISBN do livro: ");
                Long isbn = scanner.nextLong();
                Livro livro = livro_dao.buscaByISBN(isbn);
                while (livro == null) {
                    System.out.println("ISBN não cadastrado. Informe novamente: ");
                    isbn = scanner.nextLong();
                    livro = livro_dao.buscaByISBN(isbn);
                }

                if(1 == exemplar_dao.verificaDisponibilidadeExemplares(livro)){

                    livro_dao.listaExemplares(isbn);
                    System.out.println("Informe o ID do exemplar:");
                    Integer idExemplar = scanner.nextInt();
                    Exemplar exemplar = exemplar_dao.idJaCadastrado(idExemplar);
                    while (exemplar == null || !exemplar.getLivro().getIsbn().equals(isbn) || !exemplar.getDisponivel() || exemplar.getReservado()){
                        System.out.println("ID invalido. Informe novamente:");
                        idExemplar = scanner.nextInt();
                        exemplar = exemplar_dao.idJaCadastrado(idExemplar);
                    }

                    System.out.println("Informe o login do usuário: ");
                    String login = scannerString.readLine();
                    while (usuarioJaCadastrado(usuarios, login) == 0) {
                        System.out.println("Login invalido. Informe novamente: ");
                        login = scannerString.readLine();
                    }

                    Usuario user = buscaUsuarioByLogin(usuarios, login);
                    if (atingiuLimiteEmprestimos(emprestimos, user) == 1) {
                        System.out.println("Usuário atingiu o limite de emprestimos. Não foi possível registrar novo empréstimo.");
                    }else{
                        emp.setUsuario(user);
                        emp.setExemplar(exemplar);
                        emp.setData_retirada(dataAtual());
                        emp.setData_devolucao(null);
                        DateTime data_limite_devolucao;
                        if (user.getProf()==1) {
                            data_limite_devolucao = dataAtual().plusDays(15);
                        }else{
                            data_limite_devolucao = dataAtual().plusDays(7);
                        }
                        emp.setData_limite_devolucao(data_limite_devolucao);
                        exemplar.setDisponivel(false);
                        exemplar_dao.save(exemplar);
                        System.out.println("Emprestimo registrado com sucesso.");
                    }

                }else{
                    System.out.println("Nenhum exemplar disponível para emprestimo.");
                }
            } catch(IOException ie) {
                cadastroEmprestimo(emprestimos, usuarios);
            }
        }
        return emp;
    }

    public static void listaEmprestimos(ArrayList<Emprestimo> emprestimos) {
        System.out.println("");
        if (emprestimos.isEmpty())
            System.out.println("Nenhum emprestimo registrado.");
        else{
            for (Emprestimo emp:emprestimos) {
                System.out.println("Livro: " + emp.getExemplar().getLivro().getTitulo());
                System.out.println("Usuario: " + emp.getUsuario().getLogin());
                System.out.println("Data de retirada: " + converteData(emp.getData_retirada()));
                if (emp.getData_devolucao() == null)
                    System.out.println("Data de devolução: Livro ainda não devolvido");
                else
                    System.out.println("Data de devolução: " + converteData(emp.getData_devolucao()));
                System.out.println("Data limite de devolução: " + converteData(emp.getData_limite_devolucao()));
                System.out.println("");
            }
        }

    }

    public static void imprimeInfoEmprestimo(Emprestimo emp) {
        System.out.println("Livro: " + emp.getExemplar().getLivro().getTitulo() + "(ID do exemplar: " +emp.getExemplar().getId() + ")");
        System.out.println("Usuario: " + emp.getUsuario().getLogin());
        System.out.println("Data de retirada: " + converteData(emp.getData_retirada()));
        if (emp.getData_devolucao() == null)
            System.out.println("Data de devolução: Livro ainda não devolvido.");
        else
            System.out.println("Data de devolução: " + converteData(emp.getData_devolucao()));
        System.out.println("Data limite de devolução: " + converteData(emp.getData_limite_devolucao()));
    }

    public static void buscaEmprestimoLivro(ArrayList<Emprestimo> emprestimos) {
        BufferedReader scannerString = new BufferedReader(new InputStreamReader(System.in));
        Scanner scanner = new Scanner(System.in);
        Emprestimo empEncontrado = new Emprestimo();
        System.out.println("Informe 1 se deseja buscar por ISBN ou 2 se deseja buscar por titulo: ");
        Integer n = scanner.nextInt();
        while (n < 1 || n > 2) {
            System.out.println("Opcao invalida. Informe novamente: ");
            n = scanner.nextInt();
        }
        if (n==1) {
            System.out.println("Informe o ISBN do livro: ");
            Long isbn = scanner.nextLong();
            empEncontrado = null;
            for (Emprestimo emp:emprestimos) {
                if (emp.getExemplar().getLivro().getIsbn().equals(isbn)) {
                    empEncontrado = emp;
                }
            }
        }else{
            try{
                System.out.println("Informe o titulo do livro: ");
                String titulo = scannerString.readLine();
                empEncontrado = null;
                for (Emprestimo emp:emprestimos) {
                    if (emp.getExemplar().getLivro().getTitulo().equals(titulo)) {
                        empEncontrado = emp;
                    }
                }
            }catch(Exception ie) {
                buscaEmprestimoLivro(emprestimos);
            }
        }
        System.out.println("");
        if (empEncontrado!=null) {
            imprimeInfoEmprestimo(empEncontrado);
        }else{
            System.out.println("Nenhum emprestimo encontrado.");
        }
    }

    public static void buscaEmprestimoExemplar(ArrayList<Emprestimo> emprestimos) {
        Scanner scanner = new Scanner(System.in);
        Emprestimo empEncontrado = null;
        System.out.println("Informe o ID do exemplar que deseja buscar: ");
        Integer id = scanner.nextInt();
        System.out.println("");
        for (Emprestimo emp:emprestimos) {
            if (emp.getExemplar().getId().equals(id)) {
                empEncontrado = emp;
            }
        }
        if (empEncontrado != null)
            imprimeInfoEmprestimo(empEncontrado);
        else
            System.out.println("Nenhum emprestimo encontrado.");
    }

    public static void buscaEmprestimoUsuario(ArrayList<Emprestimo> emprestimos, ArrayList<Usuario> usuarios) {
        BufferedReader scannerString = new BufferedReader(new InputStreamReader(System.in));
        Emprestimo empEncontrado = new Emprestimo();

        try {
            System.out.println("Informe o login do usuario que deseja buscar:");
            String login = scannerString.readLine();
            empEncontrado = null;
            for (Emprestimo emp:emprestimos) {
                if (emp.getUsuario().getLogin().equals(login))
                    empEncontrado = emp;
            }
        }catch(Exception ie) {
            buscaEmprestimoUsuario(emprestimos, usuarios);
        }

        if (empEncontrado == null)
            System.out.println("Nenhum emprestimo encontrado. ");
        else
            imprimeInfoEmprestimo(empEncontrado);
    }

    public static boolean exemplarEmprestadoPeloUsuario(ArrayList<Emprestimo> emprestimos, Usuario user, int id) {
        for (Emprestimo emp:emprestimos) {
            if (emp.getUsuario().equals(user) && emp.getExemplar().getId() == id)
                return true;
        }
        return false;
    }

    public static void devolverLivro(ArrayList<Emprestimo> emprestimos, ArrayList<Usuario> usuarios) {
        Scanner scanner = new Scanner(System.in);
        BufferedReader scannerString = new BufferedReader(new InputStreamReader(System.in));
        ExemplarDao exemplar_dao = new ExemplarDao();
        Emprestimo emprestimo = null;

        try {
            System.out.println("Informe o ID do exemplar que deseja devolver: ");
            int id = scanner.nextInt();
            Exemplar exemplar = exemplar_dao.idJaCadastrado(id);
            while(exemplar == null || exemplar.getDisponivel()) {
                System.out.println("ID invalido. Informe novamente: ");
                id = scanner.nextInt();
                exemplar = exemplar_dao.idJaCadastrado(id);
            }

            System.out.println("Informe o login do usuario: ");
            String login = scannerString.readLine();
            Usuario user = buscaUsuarioByLogin(usuarios, login);
            while(usuarioJaCadastrado(usuarios, login) == 0 || !exemplarEmprestadoPeloUsuario(emprestimos, user, id)) {
                System.out.println("Login invalido. Informe novamente: ");
                login = scannerString.readLine();
                user = buscaUsuarioByLogin(usuarios, login);
            }

            for (Emprestimo emp:emprestimos) {
                if (emp.getUsuario().getLogin().equals(login) && emp.getExemplar().getId() == id)
                    emprestimo = emp;
            }

            System.out.println("Informe a data de devolução: ");
            DateTime data_dev = lerData();
            while(data_dev.isBefore(emprestimo.getData_retirada())) {
                System.out.println("Data invalida. Informe novamente: ");
                data_dev = lerData();
            }

            emprestimo.setData_devolucao(data_dev);
            if (emprestimo.getData_devolucao().isAfter(emprestimo.getData_limite_devolucao()))
                        emprestimo.getUsuario().setMulta(true);

            exemplar.setDisponivel(true);
            exemplar_dao.save(exemplar);

            System.out.println("Livro devolvido com sucesso.");
            System.out.println("");
        }catch(IOException ie){
            devolverLivro(emprestimos, usuarios);
        }
    }

    public static Reserva cadastroReserva(ArrayList<Usuario> usuarios) {
        Reserva reserva = new Reserva();
        LivroDao livro_dao = new LivroDao();
        ExemplarDao exemplar_dao = new ExemplarDao();

        try {
            if (livro_dao.nenhumLivroCadastrado()==1) {
                System.out.println("Nenhum livro cadastrado. Impossível efetuar reservas.");
            }else if(usuarios.isEmpty()) {
                System.out.println("Nenhum usuário cadastrado. Impossível efetuar reservas.");
            }else{
                Scanner scanner = new Scanner(System.in);
                BufferedReader scannerString = new BufferedReader(new InputStreamReader(System.in));

                System.out.println("Informe o ISBN do livro que deseja reservar:");
                Long isbn = scanner.nextLong();
                Livro livro = livro_dao.buscaByISBN(isbn);
                while(livro == null) {
                    System.out.println("ISBN não cadastrado. Informe novamente.");
                    isbn = scanner.nextLong();
                    livro = livro_dao.buscaByISBN(isbn);
                }
                Exemplar exemplar = exemplar_dao.exemplarDisponivelParaReserva(livro);
                if (exemplar == null) {
                    System.out.println("Nenhum exemplar disponível para reserva.");
                }else{

                    System.out.println("Informe o login do usuário: ");
                    String login = scannerString.readLine();
                    while(usuarioJaCadastrado(usuarios, login) == 0) {
                        System.out.println("Login inválido. Informe novamente. ");
                        login = scannerString.readLine();
                    }

                    if (usuarioComMulta(usuarios, login)) {
                        System.out.println("Usuário com multa. Impossível efetuar reserva.");
                    }else{
                        Usuario usuario = buscaUsuarioByLogin(usuarios, login);
                        reserva.setExemplar(exemplar);
                        reserva.setUsuario(usuario);
                        exemplar.setReservado(true);
                        exemplar_dao.save(exemplar);
                        System.out.println("");
                        System.out.println("Reserva efetuada com sucesso.");
                    }
                }
            }
        }catch(IOException ie) {
            cadastroReserva(usuarios);
        }

        return reserva;
    }

    public static int exemplarReservadoPeloUsuario(ArrayList<Reserva> reservas, int id) {
        int a = 0;
        for (Reserva res:reservas) {
            if (res.getExemplar().getId().equals(id))
                a=1;
        }
        return a;
    }

    public static void cancelaReserva(ArrayList<Reserva> reservas, ArrayList<Usuario> usuarios) {
        if (reservas.isEmpty())
            System.out.println("Nenhuma reserva registrada.");
        else{
            BufferedReader scannerString = new BufferedReader(new InputStreamReader(System.in));
            Scanner scanner = new Scanner(System.in);
            ExemplarDao exemplar_dao = new ExemplarDao();
            ArrayList<Reserva> reservasUsuario = new ArrayList<Reserva>();

            try {
                System.out.println("Informe o login do usuario:");
                String login = scannerString.readLine();
                while(usuarioJaCadastrado(usuarios, login)==0) {
                    System.out.println("Login inválido. Informe novamente: ");
                    login = scannerString.readLine();
                }
                System.out.println("Reservas efetuadas por esse usuário:");
                for (Reserva res:reservas){
                    if (res.getUsuario().getLogin().equals(login)) {
                        reservasUsuario.add(res);
                        System.out.println("-Reserva do livro " +res.getExemplar().getLivro().getTitulo()+ " | ID do exemplar: " +res.getExemplar().getId());
                    }
                }
                System.out.println("Informe o ID do exemplar que deseja cancelar a reserva: ");
                int id = scanner.nextInt();
                Exemplar exemplar = exemplar_dao.idJaCadastrado(id);
                while(exemplar == null || exemplarReservadoPeloUsuario(reservasUsuario, id) == 0){
                    System.out.println("ID inválido. Informe novamente: ");
                    id = scanner.nextInt();
                    exemplar = exemplar_dao.idJaCadastrado(id);
                }

                for (Reserva res:reservas){
                    if (res.getUsuario().getLogin().equals(login) && res.getExemplar().getId().equals(id)) {
                        reservas.remove(res);
                        exemplar.setReservado(false);
                        exemplar_dao.save(exemplar);
                        System.out.println("Reservada cancelada com sucesso.");
                        break;
                    }
                }

            }catch(IOException ie) {
                cancelaReserva(reservas, usuarios);
            }
        }
    }

    public static void pagarMulta(ArrayList<Emprestimo> emprestimos, ArrayList<Usuario> usuarios) {
        BufferedReader scannerString = new BufferedReader(new InputStreamReader(System.in));
        Scanner scanner = new Scanner(System.in);
        ExemplarDao exemplar_dao = new ExemplarDao();
        ArrayList<Emprestimo> emprestimos_em_atraso = new ArrayList<Emprestimo>();

        try{
            System.out.println("Informe o login do usuário: ");
            String login = scannerString.readLine();
            while(usuarioJaCadastrado(usuarios, login)==0) {
                System.out.println("Login invalido. Informe novamente: ");
                login = scannerString.readLine();
            }

            Usuario user = buscaUsuarioByLogin(usuarios, login);

            if (!usuarioComMulta(usuarios, login))
                System.out.println("Usuário sem multa.");
            else{
                System.out.println("Emprestimos em atraso: ");
                for (Emprestimo emp:emprestimos) {
                    if (emp.getUsuario() != null && emp.getUsuario().equals(user) && emp.getData_devolucao() != null && emp.getData_devolucao().isAfter(emp.getData_limite_devolucao())) {
                        System.out.println("->Emprestimo do livro " +emp.getExemplar().getLivro().getTitulo()+"(ID do exemplar: " +emp.getExemplar().getId()+")");
                        emprestimos_em_atraso.add(emp);
                    }
                }

                System.out.println("Informe o ID do exemplar que deseja pagar a multa: ");
                int id = scanner.nextInt();
                Exemplar exemplar = exemplar_dao.idJaCadastrado(id);
                while(exemplar == null || !exemplarEmprestadoPeloUsuario(emprestimos_em_atraso, user, id)) {
                    System.out.println("ID invalido. Informe novamente: ");
                    id = scanner.nextInt();
                    exemplar = exemplar_dao.idJaCadastrado(id);
                }

                for (Emprestimo emp:emprestimos_em_atraso) {
                    if (emp.getExemplar().getId() == id) {
                        System.out.println("Multa paga no valor de R$" + Days.daysBetween(emp.getData_limite_devolucao(), emp.getData_devolucao()).getDays());
                        emprestimos_em_atraso.remove(emp);
                        emprestimos.remove(emp);
                        break;
                    }
                }
                if(emprestimos_em_atraso.isEmpty())
                    user.setMulta(false);
            }
        }catch(IOException ie) {
            pagarMulta(emprestimos, usuarios);
        }
    }

    public static void renovarEmprestimo(ArrayList<Emprestimo> emprestimos, ArrayList<Usuario> usuarios) {
        if (usuarios.isEmpty())
            System.out.println("Nenhum usuário cadastrado. Impossível renovar emprestimo.");
        else if (emprestimos.isEmpty())
            System.out.println("Nenhum emprestimo cadastrado. Impossível renovar emprestimo.");
        else{
            BufferedReader scannerString = new BufferedReader(new InputStreamReader(System.in));
            Scanner scanner = new Scanner(System.in);
            ExemplarDao exemplar_dao = new ExemplarDao();

            try{
                System.out.println("Informe o login do usuário: ");
                String login = scannerString.readLine();
                while(usuarioJaCadastrado(usuarios, login) == 0) {
                    System.out.println("Login invalido. Informe novamente: ");
                    login = scannerString.readLine();
                }

                Usuario user = buscaUsuarioByLogin(usuarios, login);

                System.out.println("Emprestimos registrados para esse usuario: ");
                for (Emprestimo emp:emprestimos) {
                    if (emp.getUsuario().getLogin().equals(login)) {
                        System.out.println("->Emprestimo do livro " +emp.getExemplar().getLivro().getTitulo()+"(ID do exemplar: " +emp.getExemplar().getId()+")");
                    }
                }
                System.out.println("");
                System.out.println("Informe o ID do exemplar que deseja renovar o emprestimo: ");
                int id = scanner.nextInt();
                Exemplar exemplar = exemplar_dao.idJaCadastrado(id);
                while(exemplar == null || !exemplarEmprestadoPeloUsuario(emprestimos, user, id) || exemplar.getDisponivel()) {
                    System.out.println("ID invalido. Informe novamente: ");
                    id = scanner.nextInt();
                    exemplar = exemplar_dao.idJaCadastrado(id);
                }

                if (exemplar.getReservado()) {
                    System.out.println("Exemplar reservado. Impossível renovar emprestimo.");
                }else{
                    DateTime data_limite_devolucao;
                    for (Emprestimo emp:emprestimos) {
                        if (emp.getExemplar().getId() == id) {
                            if (user.getProf()==1) {
                                data_limite_devolucao = dataAtual().plusDays(15);
                            }else{
                                data_limite_devolucao = dataAtual().plusDays(7);
                            }
                            emp.setData_limite_devolucao(data_limite_devolucao);
                        }
                    }
                    System.out.println("Emprestimo renovado com sucesso.");
                }
            }catch(IOException ie) {
                renovarEmprestimo(emprestimos, usuarios);
            }
        }
    }

}
