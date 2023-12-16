package dv.trunov.dao;

import dv.trunov.models.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {

    @Value("${database.url}")
    private String URL;

    @Value("${database.user}")
    private String username;

    @Value("${database.password}")
    private String password;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Person> index() {
        List<Person> people = new ArrayList<>();
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = this.getSelectAllStatement(connection)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Person person = new Person(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getInt("age"),
                    resultSet.getString("email")
                );
                people.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return people;
    }

    public Person show(int id) {
        Person person;
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = this.getSelectByIdStatement(connection)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            person = new Person(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("age"),
                resultSet.getString("email"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return person;
    }

    public void save(Person person) {
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = this.getInsertStatement(connection)) {
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, person.getName());
            preparedStatement.setInt(3, person.getAge());
            preparedStatement.setString(4, person.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(int id, Person updatedPerson) {
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = this.getUpdateStatement(connection)) {
            preparedStatement.setString(1, updatedPerson.getName());
            preparedStatement.setInt(2, updatedPerson.getAge());
            preparedStatement.setString(3, updatedPerson.getEmail());
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = this.getDeleteStatement(connection)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, username, password);
    }

    private PreparedStatement getSelectAllStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM person");
    }

    private PreparedStatement getSelectByIdStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM person WHERE id = ?");
    }

    private PreparedStatement getInsertStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("INSERT INTO person VALUES (?, ?, ?, ?)");
    }

    private PreparedStatement getUpdateStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("UPDATE person SET name = ?, age = ?, email = ? WHERE id = ?");
    }

    private PreparedStatement getDeleteStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("DELETE FROM person WHERE id = ?");
    }
}
