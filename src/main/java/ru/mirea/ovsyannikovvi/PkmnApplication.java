package ru.mirea.ovsyannikovvi;

import com.fasterxml.jackson.databind.JsonNode;
import ru.mirea.ovsyannikovvi.web.http.PkmnHttpClient;
import ru.mirea.ovsyannikovvi.web.jdbc.DatabaseServiceImpl;
import ru.mirea.pkmn.Card;
import ru.mirea.pkmn.CardExport;
import ru.mirea.pkmn.CardImport;
import ru.mirea.pkmn.Student;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PkmnApplication {
    public static void main(String[] args) throws IOException, SQLException {
        CardImport cardimport = new CardImport();
        Card card;
        //card1 = cardimport.Deserialize("C:\\Users\\Ovsyanka\\IdeaProjects\\pkmn\\src\\main\\resources\\Croagunk.crd");
        card = cardimport.Fill("C:\\Users\\Ovsyanka\\IdeaProjects\\pkmn\\src\\main\\resources\\my_card.txt");
        //System.out.println(card.toString());
        //CardExport cardexport = new CardExport(card1);
        //cardexport.Serialize();

        //PkmnHttpClient pkmnHttpClient = new PkmnHttpClient();
        //JsonNode card1 = pkmnHttpClient.getPokemonCard(card.getName(), card.getNumber());
//        System.out.println(card1.toPrettyString());

//        System.out.println(card1.findValues("attacks")
//                .stream()
//                .map(JsonNode::toPrettyString)
//                .collect(Collectors.toSet()));
        DatabaseServiceImpl db = new DatabaseServiceImpl();

        //UUID UUID_student = db.createPokemonOwner(card.getPokemonOwner());
        //Student student = db.getStudentFromDatabase(card.getPokemonOwner().getFirstName());
        //UUID UUID_student = UUID.fromString("41b03a25-a0ee-4c06-8836-737a4c0991d1");
        //UUID UUID_card = db.saveCardToDatabase(card, UUID_student);
        Card card2 = db.getCardFromDatabase(card.getName(), null);
        System.out.println(card2.toString());
    }
}
