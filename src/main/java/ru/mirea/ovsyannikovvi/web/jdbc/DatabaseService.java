package ru.mirea.ovsyannikovvi.web.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.mirea.pkmn.Card;
import ru.mirea.pkmn.Student;

import java.sql.SQLException;
import java.util.UUID;

public interface DatabaseService {
    Card getCardFromDatabase(String cardName, UUID UUID_cardevolvesfrom) throws SQLException;
    Student getStudentFromDatabase(String studentName) throws SQLException;
    UUID saveCardToDatabase(Card card, UUID UUID_student) throws SQLException, JsonProcessingException;
    UUID createPokemonOwner(Student owner) throws SQLException;
}
