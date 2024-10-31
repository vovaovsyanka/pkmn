package ru.mirea.ovsyannikovvi.web.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.mirea.pkmn.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class DatabaseServiceImpl implements DatabaseService{
    private final Connection connection;

    private final Properties databaseProperties;

    public DatabaseServiceImpl() throws SQLException, IOException {

        // Загружаем файл database.properties

        databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream("C:\\Users\\Ovsyanka\\IdeaProjects\\pkmn\\src\\main\\resources\\database.properties"));

        // Подключаемся к базе данных

        connection = DriverManager.getConnection(
                databaseProperties.getProperty("database.url"),
                databaseProperties.getProperty("database.user"),
                databaseProperties.getProperty("database.password")
        );
        System.out.println("Connection is "+(connection.isValid(0) ? "up" : "down"));
    }

    @Override
    public Card getCardFromDatabase(String cardName, UUID uuid_cardevolvesfrom) throws SQLException {
        // Реализовать получение данных о карте из БД
        String id = "", name = "", hp = "", evolves_from = "", game_set = "", pokemon_owner = "", stage = "", retreat_cost = "", weakness_type = "", resistance_type = "", attack_skills = "", pokemon_type = "", regulation_mark = "", card_number = "";
        Statement selectQuery = connection.createStatement();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM card WHERE");
        List<Object> parameters = new ArrayList<>();

        if (cardName != null) {
            queryBuilder.append(" \"name\" = ?");
            parameters.add(cardName);
        }

        if (uuid_cardevolvesfrom != null) {
            queryBuilder.append(" \"id\" = ?");
            parameters.add(uuid_cardevolvesfrom);
        }

        String query = queryBuilder.toString();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

// Установка параметров
        for (int i = 0; i < parameters.size(); i++) {
            preparedStatement.setObject(i + 1, parameters.get(i));
        }
        ResultSet resultOfQuery = preparedStatement.executeQuery();
        Card card = null;
        Card card_evolvesfrom = null;
        while (resultOfQuery.next()) {
            id = resultOfQuery.getString("id");
            name = resultOfQuery.getString("name");
            hp = resultOfQuery.getString("hp");
            evolves_from = resultOfQuery.getString("evolves_from");
            if (evolves_from!=null){
                card_evolvesfrom = getCardFromDatabase(null, UUID.fromString(evolves_from));
            }
            game_set = resultOfQuery.getString("game_set");
            pokemon_owner = resultOfQuery.getString("pokemon_owner");
            stage = resultOfQuery.getString("stage");
            retreat_cost = resultOfQuery.getString("retreat_cost");
            weakness_type = resultOfQuery.getString("weakness_type");
            resistance_type = resultOfQuery.getString("resistance_type");
            attack_skills = resultOfQuery.getString("attack_skills");
            pokemon_type = resultOfQuery.getString("pokemon_type");
            regulation_mark = resultOfQuery.getString("regulation_mark");
            card_number = resultOfQuery.getString("card_number");
            System.out.println("ID: " + id);
            System.out.println("name: " + name);
            System.out.println("hp: " + hp);
            System.out.println("evolves_from: " + evolves_from);
            System.out.println("game_set: " + game_set);
            System.out.println("pokemon_owner: " + pokemon_owner);
            System.out.println("stage: " + stage);
            System.out.println("retreat_cost: " + retreat_cost);
            System.out.println("weakness_type: " + weakness_type);
            System.out.println("resistance_type: " + resistance_type);
            System.out.println("attack_skills: " + attack_skills);
            System.out.println("pokemon_type: " + pokemon_type);
            System.out.println("regulation_mark: " + regulation_mark);
            System.out.println("card_number: " + card_number);
        }

        EnergyType weakness;
        try {
            weakness = EnergyType.valueOf(weakness_type);
        } catch (NullPointerException e) {
            weakness=null;
        }

        EnergyType resistance;
        try {
            resistance = EnergyType.valueOf(resistance_type);
        } catch (NullPointerException e) {
            resistance = null;
        }

        selectQuery = connection.createStatement();
        query = "SELECT * FROM student WHERE \"id\" = '"+pokemon_owner+"'";
        resultOfQuery = selectQuery.executeQuery(query);
        Student student = null;
        while (resultOfQuery.next()) {
            String firstName = resultOfQuery.getString("firstName");
            String familyName = resultOfQuery.getString("familyName");
            String patronicName = resultOfQuery.getString("patronicName");
            String group = resultOfQuery.getString("group");
            student = new Student(firstName, familyName, patronicName, group);
        }
        JSONArray jsonArray = new JSONArray(attack_skills);
        List<AttackSkill> attackSkills = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            AttackSkill Skill = new AttackSkill();
            JSONObject skill = jsonArray.getJSONObject(i);
            Skill.setName(skill.getString("name"));
            Skill.setDescription(skill.getString("description"));
            Skill.setCost(skill.getString("cost"));
            Skill.setDamage(skill.getInt("damage"));
            attackSkills.add(Skill);
        }

        card = new Card(PokemonStage.valueOf(stage), name, Integer.parseInt(hp), EnergyType.valueOf(pokemon_type), card_evolvesfrom, attackSkills, weakness, resistance, retreat_cost, game_set, regulation_mark.charAt(0), student, card_number);
        return card;
    }

    @Override
    public Student getStudentFromDatabase(String studentName) throws SQLException {
        // Реализовать получение данных о студенте из БД
        Statement selectQuery = connection.createStatement();
        String query = "SELECT * FROM student WHERE \"firstName\" = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, studentName);
        ResultSet resultOfQuery = preparedStatement.executeQuery();
        Student student = null;
        while (resultOfQuery.next()) {
            String id = resultOfQuery.getString("id");
            String firstName = resultOfQuery.getString("firstName");
            String familyName = resultOfQuery.getString("familyName");
            String patronicName = resultOfQuery.getString("patronicName");
            String group = resultOfQuery.getString("group");
            System.out.println("ID: " + id);
            System.out.println("First Name: " + firstName);
            System.out.println("Family Name: " + familyName);
            System.out.println("Patronic Name: " + patronicName);
            System.out.println("Group: " + group);
            student = new Student(firstName, familyName, patronicName, group);
        }
        return student;
    }

    @Override
    public UUID saveCardToDatabase(Card card, UUID UUID_student) throws SQLException, JsonProcessingException {
        // Реализовать отправку данных карты в БД
        UUID UUID_cardevolvesfrom = null;
        if (card.getEvolvesFrom() != null){
            UUID_cardevolvesfrom = saveCardToDatabase(card.getEvolvesFrom(), UUID_student);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        card.getSkills().get(0).setDescription(card.getSkills().get(0).getDescription().replace("'", ""));
        String jsonSkills = objectMapper.writeValueAsString(card.getSkills());
        UUID UUID_card = UUID.randomUUID();

        String query = "INSERT INTO card (id, name, hp, evolves_from, game_set, pokemon_owner, stage," +
                " retreat_cost, weakness_type, resistance_type, attack_skills, pokemon_type, regulation_mark, card_number) VALUES" +
                " ('"+UUID_card+"', '"+card.getName()+"', "+card.getHp() + ", ?, '"+card.getGameSet()+"', '"+UUID_student+"', '"
                +card.getPokemonStage()+"', '"+card.getRetreatCost()+"', ?, ?, '"+jsonSkills+"', '"
                +card.getPokemonType()+"', '"+card.getRegulationMark()+"', '"+card.getNumber()+"')";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        if (UUID_cardevolvesfrom == null){
            preparedStatement.setObject(1, null);
        } else{
            preparedStatement.setObject(1, UUID_cardevolvesfrom);
        }
        if (card.getWeaknessType() == null){
            preparedStatement.setObject(2, null);
        } else{
            preparedStatement.setString(2, card.getWeaknessType().toString());
        }
        if (card.getResistanceType() == null){
            preparedStatement.setObject(3, null);
        } else{
            preparedStatement.setString(3, card.getResistanceType().toString());
        }
        System.out.println(query);
        preparedStatement.executeUpdate();
        return UUID_card;
    }

    @Override
    public UUID createPokemonOwner(Student owner) throws SQLException {
        // Реализовать добавление студента - владельца карты в БД
        UUID UUID_student = UUID.randomUUID();
        Statement selectQuery = connection.createStatement();
        String sql = "INSERT INTO student (id, \"familyName\", \"firstName\", \"patronicName\", \"group\") VALUES ('"+UUID_student+"', '"+
                owner.getSurName()+"', '"+owner.getFirstName()+"', '"+owner.getFatherName()+"', '"+
                owner.getGroup()+"')";

        System.out.println(sql);
        selectQuery.executeUpdate(sql);
        return UUID_student;
    }
}
