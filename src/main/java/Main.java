
import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "@lp.lj";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";

    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        while (true) {
            System.out.println("1. Показать список всех задач");
            System.out.println("2. Выполнить задачу");
            System.out.println("3. Создать задачу");
            System.out.println("4. Удалить задачу");
            System.out.println("5. Выйти");
            int command = sc.nextInt();
            switch (command) {
                case 1 -> {
                    //объект, который умеет отправлять запросы в бд
                    Statement statement = connection.createStatement();
                    String SQL_SELECT_TASKS = "select * from task order by id desc";
                    //объект, который хранит результат запроса
                    ResultSet result = statement.executeQuery(SQL_SELECT_TASKS);
                    //просматриваем все данные, которые вернулись из БД и выводим их на экран
                    while (result.next()) {
                        System.out.println(result.getInt("id") + " "
                                + result.getString("name") + " "
                                + result.getString("state"));
                    }
                    System.out.println("");
                }
                case 2 -> {
                    //описываем запрос, не зная какие параметры там будут
                    String sql = "update task set state = 'DONE' where id = ?;";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    System.out.println("Введите идентификатор задачи");
                    int taskId = sc.nextInt();
                    preparedStatement.setInt(1, taskId);  //говорим: в первый вопросительный знак положи значение taskId
                    preparedStatement.executeUpdate();
                }
                case 3 -> {
                    String sql = "insert into task (name, state) values (?, 'IN_PROCESS');";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    System.out.println("Введите название задачи");
                    sc.nextLine();
                    String taskName = sc.nextLine();
                    preparedStatement.setString(1, taskName);  //говорим: в первый вопросительный знак положи значение taskId
                    preparedStatement.executeUpdate();
                }
                case 4 -> {
                    String sql = "delete from task where id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    System.out.println("Введите идентификатор задачи");
                    int taskId = sc.nextInt();
                    preparedStatement.setInt(1, taskId);
                    preparedStatement.executeUpdate();
                }
                case 5 -> System.exit(0);
                default -> System.err.println("Команда не распознана");    //поток для вывода ошибок
            }
        }
    }
}